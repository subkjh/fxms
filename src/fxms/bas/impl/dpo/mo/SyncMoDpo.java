package fxms.bas.impl.dpo.mo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.LogApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.co.CoCode.MO_STATUS;
import fxms.bas.event.FxEvent;
import fxms.bas.exp.FxTimeoutException;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.fxo.adapter.FxConfAdapter;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.FireEventDfo;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.mo.MoImpl;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.PsVoRawList;
import fxms.bas.vo.SyncMo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;

/**
 * 관리대상에 대한 동기화를 진행한다.
 * 
 * @author subkjh
 *
 */
public class SyncMoDpo implements FxDpo<Long, SyncMo> {

	private void addValue(String owner, long pollMsdate, PsVoRaw vo, FxEvent sign) {

		if (vo == null) {
			return;
		}

		List<PsVoRaw> valueList = new ArrayList<PsVoRaw>();
		valueList.add(vo);
		ValueApi.getApi().addValue(new PsVoRawList(owner, pollMsdate, valueList, sign), true);
	}

	private SyncMo getSyncMo(long moNo) throws MoNotFoundException, Exception {

		Mo mo = MoApi.getApi().getMo(moNo);

		// 상위 관리대상이 존재하면 상위가 동기화를 진행해야함으로 상위 관리대상을 찾는다.
		if (Mo.hasUpper(mo)) {
			mo = MoApi.getApi().getMo(mo.getUpperMoNo());
		}

		SyncMo syncMo = new SyncMo(mo);

		Map<String, Object> para = new HashMap<>();
		para.put("upperMoNo", mo.getMoNo());

		List<Mo> list = MoApi.getApi().getMoList(para);
		for (Mo a : list) {
			syncMo.addMo(a);
		}

		Logger.logger.trace("Select MO\n{}", syncMo.getDebug());

		return syncMo;
	}

	/**
	 * 관리대상의 구성을 저장소에 적용한다.
	 *
	 * @param syncMo 적용할 내용
	 * @throws Exception
	 */
	private void updateSyncMo(SyncMo syncMo, boolean updateUpper, boolean broadcast) throws Exception {

		new UpdateSyncMoDfo().update(syncMo, updateUpper);

		if (broadcast) {
			new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Mo, updateUpper));
			new FireEventDfo().fireEvent(new MoImpl(syncMo.getUpper()), ALARM_CODE.mo_updated.getAlcdNo());
		}

	}

	@Override
	public SyncMo run(FxFact fact, Long moNo) throws Exception {
		return sync(moNo, false);
	}

	public SyncMo sync(long moNo, boolean isUpdate) throws Exception {

		SyncMo syncMo = getSyncMo(moNo);
		Mo upper = syncMo.getUpper();

		Logger.logger.info("{}", syncMo);

		long mstimeStart = System.currentTimeMillis();
		StringBuffer inPara = new StringBuffer();
		StringBuffer errmsg = new StringBuffer();

		inPara.append("mo-no=");
		inPara.append(upper.getMoNo());

		List<FxConfAdapter> adapterList = AdapterApi.getApi().getAdapters(FxConfAdapter.class, upper);
		StringBuffer sb = new StringBuffer();

		for (FxConfAdapter adapter : adapterList) {
			try {
				Logger.logger.info("sync {}", adapter.getClass().getName());
				adapter.sync(syncMo);
				sb.append(Logger.makeSubString(adapter.getClass().getName(), syncMo.toLogString()));

			} catch (FxTimeoutException e) {
				// 타임아웃이면 오류 처리한다.
				Logger.logger.error(e);
				errmsg.append(e.getClass().getSimpleName());
				break;
			} catch (Exception e) {
				Logger.logger.fail("{} {} ", syncMo, e.getMessage());
				sb.append(Logger.makeSubString(adapter.getClass().getName(), "error " + e.getClass().getName()));

				// 에러 내용을 수집하여 exception을 보낸다.
				errmsg.append(" ").append(adapter.getClass().getSimpleName()).append(" error ")
						.append(e.getClass().getSimpleName());
			}
		}

		Logger.logger.info(Logger.makeString("SYNC", upper.getMoNo(), sb.toString()));
		Logger.logger.trace("Detected MO\n{}", syncMo.getDebug());

		// 로그를 기록한다.
		if (errmsg.length() > 0) {

			// 상태를 기록한다.
			PsVoRaw vo = new PsVoRaw(upper.getMoNo(), PsApi.MO_STATUS_PS_ID, MO_STATUS.Offline.getNo());
			addValue("sync", System.currentTimeMillis(), vo, null);

			LogApi.getApi().logUserWorkHst(User.USER_NO_SYSTEM, "SYSTEM", "SYSTEM", "sync", inPara.toString(), null, -1,
					errmsg.toString(), mstimeStart, "MO", moNo, upper.getMoName());

			throw new Exception(errmsg.toString());

		} else {

			// 수집한 구성 정보를 기록할 조건이 되면 기록한다.
			if (isUpdate && syncMo != null && syncMo.sizeAll() > 0) {
				updateSyncMo(syncMo, false, true);
			}

			// 상태를 기록한다.
			PsVoRaw vo = new PsVoRaw(upper.getMoNo(), PsApi.MO_STATUS_PS_ID, MO_STATUS.Online.getNo());
			addValue("sync", System.currentTimeMillis(), vo, null);

			LogApi.getApi().logUserWorkHst(User.USER_NO_SYSTEM, "SYSTEM", "SYSTEM", "sync", inPara.toString(), null, 0,
					null, mstimeStart, "MO", moNo, upper.getMoName());
		}

		return syncMo;
	}

}

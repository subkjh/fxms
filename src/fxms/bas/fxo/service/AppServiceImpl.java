package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;

import fxms.bas.api.AppApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.api.VarApi;
import fxms.bas.co.CoCode.FXSVC_ST_CD;
import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiFilter;
import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.Mo;
import fxms.bas.signal.AliveSignal;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.PsStatReqVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

/**
 * 
 * @author subkjh
 *
 */
public class AppServiceImpl extends FxServiceImpl implements AppService {

	/**
	 *
	 */
	private static final long serialVersionUID = 2429276239016556946L;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(AppService.class.getSimpleName(), AppServiceImpl.class, args);
	}

	private final long DATA_CHECK_CYCLE = 60 * 1000L; // reload 데이터 확인 주기
	private final long STORAGE_CHECK_CYCLE = 3600 * 1000L; // 저장소 확인 주소
	private long nextDataCheckedTime = System.currentTimeMillis() + DATA_CHECK_CYCLE;
	private long nextStorageToCheckTime = System.currentTimeMillis() + STORAGE_CHECK_CYCLE; // 스토레지 다음 확인 시간

	public AppServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public String checkStorage(String memo) throws RemoteException, Exception {

		String ret = null;

		try {
			ret = AppApi.getApi().checkStorage(memo);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		} finally {
			logger.info("{} --> {}", memo, ret);
		}

	}

	@Override
	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws RemoteException, Exception {
		int ret = 0;
		try {
			ret = AppApi.getApi().generateStatistics(psTbl, psKindName, psDtm);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("{}.{}.{} --> {}", psTbl, psKindName, psDtm, ret);
		}
	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		notiFilter.add(Mo.class);
		notiFilter.add(Alarm.class);
		return notiFilter;
	}

	@Override
	public List<String> getSameDays(String date, int count) throws RemoteException, Exception {
		List<String> ret = null;
		try {
			ret = AppApi.getApi().getSameDays(date, count);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("date={}, count={} --> {}", date, count, ret);
		}
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		super.onNotify(noti);

		if (noti instanceof AliveSignal) {

			AliveSignal signal = (AliveSignal) noti;

			ServiceApi.getApi().updateServiceStatus(signal.getMsIpaddr(), signal.getServiceName(),
					signal.getStartTime(), FXSVC_ST_CD.ON, signal.getRmiPort(), signal.getServicePort());

		}

	}

	@Override
	public boolean requestMakeStat(List<PsStatReqVo> reqList) throws RemoteException, Exception {

		Boolean ret = null;
		try {
			ret = AppApi.getApi().requestMakeStat(reqList);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("req.size={} --> {}", reqList.size(), ret);
		}

	}

	@Override
	public boolean responseMakeStat(PsStatReqVo req) throws RemoteException, Exception {
		Boolean ret = null;
		try {
			ret = AppApi.getApi().responseMakeStat(req);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("{} --> {}", req, ret);
		}
	}

	@Override
	protected void onChanged(ReloadType type) throws Exception {

		super.onChanged(type);

		if (type == ReloadType.PsItem) {
			this.checkStorage("recieved reload signal");
		}

	}

	@Override
	protected void onCycle(long mstime) {

		super.onCycle(mstime);

		checkDataUpdate(mstime);

		if (nextStorageToCheckTime < mstime) {

			try {
				checkStorage("Time to check storage.");
			} catch (Exception e) {
				logger.error(e);
			}

			nextStorageToCheckTime = (mstime + STORAGE_CHECK_CYCLE);
		}

	}

	@Override
	protected void onInit(StringBuffer sb) throws Exception {
		super.onInit(sb);

		FxApi.initServiceApi(AppApi.class);
	}

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();

		try {
			ServiceApi.getApi().updateServiceStatus(FxCfg.getIpAddress(), FxServiceImpl.serviceName,
					FxCfg.getCfg().getLong(FX_PARA.fxmsStartTime.getKey(), 0), FXSVC_ST_CD.ON,
					FxCfg.getCfg().getRmiPort(), FxCfg.getCfg().getFxServicePort());
		} catch (Exception e) {
			logger.error(e);
		}

		try {
			checkStorage("Check before running.");
		} catch (Exception e) {
			logger.error(e);
		}

	}

	/**
	 * 외부 변동 내역을 읽어 API에 통보한다.<br>
	 * AppService가 담당해야 한다.
	 */
	private void checkDataUpdate(long mstime) {

		if (nextDataCheckedTime > mstime) {
			return;
		}

		nextDataCheckedTime += DATA_CHECK_CYCLE;

		String date = DateUtil.getDtmStr(mstime);

		VarApi api = VarApi.getApi();
		StringBuffer sb = new StringBuffer();
		List<ReloadType> list = api.getUpdatedData();

		for (ReloadType data : list) {

			if (sb.length() > 0) {
				sb.append(",");
			}

			sb.append(data.name());

			try {

				// NotiReceiver에게 이벤트 전달
				this.sendEvent(new ReloadSignal(data), true, true);

				api.appliedData(data, DateUtil.getDtm());

			} catch (Exception e) {

				Logger.logger.error(e);
				sb.append(" error:").append(e.getMessage());
			}

			try {
				this.onChanged(data);
			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}

		Logger.logger.debug("{} : {}", date, sb.toString());
	}
}

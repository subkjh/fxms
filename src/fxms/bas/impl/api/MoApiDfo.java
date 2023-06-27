package fxms.bas.impl.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.event.FxEvent;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.impl.dbo.all.FX_MX_ATTR_DEF;
import fxms.bas.impl.dpo.mo.AddMoDpo;
import fxms.bas.impl.dpo.mo.GetMoDfo;
import fxms.bas.impl.dpo.mo.GetMoListDfo;
import fxms.bas.impl.dpo.mo.GetRtValuesDfo;
import fxms.bas.impl.dpo.mo.ReqSyncMoDpo;
import fxms.bas.impl.dpo.mo.SetDelFlagMoDpo;
import fxms.bas.impl.dpo.mo.SetMoDfo;
import fxms.bas.impl.dpo.mo.SyncMoDpo;
import fxms.bas.impl.dpo.mo.UpdateMoDpo;
import fxms.bas.mo.Mo;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.SyncMo;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDaoEx;

/**
 * 저장소 작업이 완료된 MoApi
 *
 * @author subkjh
 *
 */
public class MoApiDfo extends MoApi {

	private Map<String, String> moAttrKeyMap;

	public MoApiDfo() {
	}

	@Override
	public Mo addMo(int userNo, String moClass, Map<String, Object> para, String reason, boolean broadcast)
			throws Exception {
		return new AddMoDpo().addMo(userNo, moClass, para, reason, broadcast);
	}

	@Override
	public Mo deleteMo(int userNo, long moNo, String reason, boolean broadcast) throws MoNotFoundException, Exception {
		return new SetDelFlagMoDpo().deleteMo(userNo, moNo, reason, broadcast);
	}

	@Override
	public Mo getMo(long moNo) throws MoNotFoundException, Exception {

		Mo mo = getMoCached(moNo);

		if (mo == null) {
			mo = new GetMoDfo().selectMo(moNo);
			if (mo == null) {
				mo = NullMo;
			}
			setMoCached(moNo, mo);
		}

		if (mo.getMoNo() != NullMo.getMoNo()) {
			return mo;
		}

		throw new MoNotFoundException(moNo);
	}

	@Override
	public List<Mo> getMoList(Map<String, Object> para) throws Exception {
		return new GetMoListDfo().selectMoList(para);
	}

	@Override
	public <T extends Mo> List<T> getMoList(Map<String, Object> para, Class<T> classOfMo) throws Exception {
		return new GetMoListDfo().selectMoList(para, classOfMo);
	}

	@Override
	public List<PsVoRaw> getRtValues(long moNo) throws Exception {
		Mo mo = new GetMoDfo().selectMo(moNo);
		return new GetRtValuesDfo().getRtValues(mo);
	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {
		super.onEvent(noti);

		moAttrKeyMap = null;
	}

	@Override
	public boolean setupMo(long moNo, String method, Map<String, Object> datas) throws Exception {
		return new SetMoDfo().set(moNo, method, datas);
	}

	@Override
	public SyncMo sync(long moNo, boolean now, boolean update) throws Exception {
		if (now) {
			return new SyncMoDpo().sync(moNo, update);
		} else {
			new ReqSyncMoDpo().requestSync(moNo);
			return null;
		}
	}

	@Override
	public Mo updateMo(int userNo, long moNo, Map<String, Object> para, boolean broadcast) throws Exception {
		return new UpdateMoDpo().updateMo(userNo, moNo, para, broadcast);
	}

	/**
	 * 관리대상의 속성이 변경된 경우 그 이력을 기록해야할 내용을 조회한다.
	 *
	 * @return
	 */
	protected synchronized Map<String, String> getAttrKeyMap() {

		if (moAttrKeyMap != null) {
			return moAttrKeyMap;
		}

		Map<String, String> keyMap = new HashMap<>();
		List<FX_MX_ATTR_DEF> list;
		try {
			list = ClassDaoEx.SelectDatas(FX_MX_ATTR_DEF.class, null);
			for (FX_MX_ATTR_DEF attr : list) {
				keyMap.put(attr.getAttrId(), attr.getAttrName());
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		moAttrKeyMap = keyMap;
		return keyMap;
	}
}

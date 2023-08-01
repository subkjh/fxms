package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.co.CoCode.ALARM_RLSE_RSN_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.AlarmHandlerQid;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.ao.AlarmAckDfo;
import fxms.bas.impl.dpo.ao.AlarmUpdateDfo;
import fxms.bas.impl.dpo.ao.AlcdMap;
import fxms.bas.impl.handler.dto.AckAlarmCurPara;
import fxms.bas.impl.handler.dto.AoSetReasonPara;
import fxms.bas.impl.handler.dto.ClearAlarmPara;
import fxms.bas.impl.handler.dto.FireAlarmPara;
import fxms.bas.impl.handler.dto.SelectAlarmHstListPara;
import fxms.bas.impl.handler.dto.SelectAlarmPara;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCode;
import subkjh.bas.BasCfg;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 알람 HANDLER
 * 
 * @author subkjh
 *
 */
public class AlarmHandler extends BaseHandler {

	@MethodDescr(name = "알람인지", description = "알람 인지를 설정한다.")
	public Object ackAlarm(SessionVo session, AckAlarmCurPara para) throws Exception {

		Map<String, Object> datas = new HashMap<>();
//		datas.put("alarmNo", para.getAlarmNo());
		datas.put("ackDtm", para.getAckDtm());
		datas.put("ackUserNo", session.getUserNo());

		return new AlarmAckDfo().ackAlarm(datas);
	}

	@MethodDescr(name = "알람해제", description = "발생된 알람을 해제한다.")
	public Object clearAlarm(SessionVo session, ClearAlarmPara para) throws Exception {

		Alarm alarm = AlarmApi.getApi().clearAlarm(para.getAlarmNo(), System.currentTimeMillis(),
				ALARM_RLSE_RSN_CD.ByUser, para.getRsnMemo(), session.getUserNo());

		return alarm;
	}

	@MethodDescr(name = "알람생성", description = "알람을 생성한다.")
	public Object fireAlarm(SessionVo session, FireAlarmPara para) throws Exception {

		Mo mo = MoApi.getApi().getMo(para.getMoNo());
		AlarmCode ac = AlcdMap.getMap().getAlarmCode(para.getAlarmName());

		Map<String, Object> etcData = null;
		if (isEmpty(para.getAlarmKey()) == false) {
			etcData = new HashMap<>();
			etcData.put("alarmKey", para.getAlarmKey());
		}

		AlarmApi.getApi().fireAlarm(mo, para.getMoInstance(), ac.getAlcdNo(), null, para.getAlarmMsg(), etcData);

		return AlarmApi.getApi().getCurAlarm(mo, para.getMoInstance(), ac.getAlcdNo());
	}

	@MethodDescr(name = "알람생성", description = "알람을 생성한다.")
	public Object getAlarm(SessionVo session, SelectAlarmPara para) throws Exception {
		return ClassDaoEx.SelectDatas(FX_AL_ALARM_HST.class, FxApi.makePara("alarmNo", para.getAlarmNo()),
				FX_AL_ALARM_HST.class);
	}

	@MethodDescr(name = "현재알람그리드용조회", description = "현재 알람 조회")
	public Object selectAlarmCurGridList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return ClassDaoEx.SelectDatas(FX_AL_ALARM_CUR.class, makePara4Ownership(session, parameters),
				FX_AL_ALARM_CUR.class);
	}

	@MethodDescr(name = "알람그리드용조회", description = "발생, 해제 알람을 포함하여 그리드용으로 조회한다.")
	public Object selectAlarmHstGridList(SessionVo session, SelectAlarmHstListPara item) throws Exception {

		Map<String, Object> wherePara = makeWherePara(item);
		wherePara.put("OCCUR_DTM >= ", item.getStartOccurDtm());
		wherePara.put("OCCUR_DTM <= ", item.getEndOccurDtm());

		return ClassDaoEx.SelectDatas(FX_AL_ALARM_HST.class, makePara4Ownership(session, wherePara),
				FX_AL_ALARM_HST.class);
	}

	@MethodDescr(name = "알람발생원인등록", description = "알람이 발생한 원인이 있다면 메모한다.")
	public Object setAlarmReason(SessionVo session, AoSetReasonPara item) throws Exception {

		Map<String, Object> datas = ObjectUtil.toMap(item);
		datas.put("rsnRegUserNo", session.getUserNo());
		return new AlarmUpdateDfo().updateAlarm(item.getAlarmNo(), datas);
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(AlarmHandlerQid.QUERY_XML_FILE));
	}
}

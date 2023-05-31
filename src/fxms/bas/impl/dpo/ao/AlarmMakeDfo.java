package fxms.bas.impl.dpo.ao;

import fxms.bas.api.InloApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.ModelApi;
import fxms.bas.api.PsApi;
import fxms.bas.event.FxEvent;
import fxms.bas.exp.MoException;
import fxms.bas.exp.NotFoundException;
import fxms.bas.exp.PsItemNotFoundException;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.Inlo;
import fxms.bas.vo.MoModel;
import fxms.bas.vo.OccurAlarm;
import fxms.bas.vo.PsItem;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 알람을 생성한다.
 * 
 * @author subkjh
 *
 */
public class AlarmMakeDfo implements FxDfo<AlarmOccurEvent, Alarm> {

	@Override
	public OccurAlarm call(FxFact fact, AlarmOccurEvent event) throws Exception {
		return makeAlarm(event);
	}

	public OccurAlarm makeAlarm(AlarmOccurEvent event) throws Exception {
		Mo mo = MoApi.getApi().getMo(event.getMoNo());
		Mo upper = mo;
		if (mo.getMoNo() != mo.getUpperMoNo()) {
			upper = MoApi.getApi().getMo(mo.getUpperMoNo());
		}
		return makeAlarm(event, mo, upper);
	}

	public OccurAlarm makeAlarm(AlarmOccurEvent event, Mo mo, Mo upper) throws Exception {

		AlarmCode alarmCode = AlcdMap.getMap().getAlarmCode(event.getAlcdNo());

		// 장비정보가 없고 해당 MO가 없거나 비관리이면 경보를 생성하지 않습니다.
		if (upper != null) {
			if (upper.isMngYn() == false || upper.getAlarmCfgNo() == AlarmCfg.NO_ALARM_CFG) {
				throw new MoException(upper.getMoNo(),
						Lang.get("Parent MO unmanagement or alarm occurrence condition is not"));
			}
		}

		OccurAlarm alarm = new OccurAlarm();

		alarm.setMo(mo);
		if (upper != null) {
			alarm.setUpperMoNo(upper.getMoNo());
			alarm.setInloNo(upper.getInloNo());
			alarm.setUpperMoName(upper.getMoName());
			alarm.setUpperMoDispName(upper.getMoName());
		}

		alarm.setAlcdNo(alarmCode.getAlcdNo());
		alarm.setAlarmKey(event.getAlarmKey());
		alarm.setAlarmLevel(event.getAlarmLevel());
		alarm.setMoInstance(event.getMoInstance());
		alarm.setFpactCd(event.getFpactCd());
		alarm.setAlarmCfgNo(event.getAlarmCfgNo());
		alarm.setAlarmMsg(event.getAlarmMsg());
		alarm.setOccurDtm(DateUtil.getDtm(event.getEventMstime())); // 발생일시
		if (event.getPsVal() != null)
			alarm.setPsVal(event.getPsVal().doubleValue());
		if (event.getCmprVal() != null)
			alarm.setCmprVal(event.getCmprVal().doubleValue());

		if (event.getPsId() != null) {
			try {
				PsItem item = PsApi.getApi().getPsItem(event.getPsId());
				alarm.setPsName(item.getPsName());
			} catch (PsItemNotFoundException e) {
			}
			alarm.setPsId(event.getPsId());
			alarm.setPsDtm(event.getPsDate());
		}

		try {
			ObjectUtil.initObject(mo, alarm);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		alarm.setAlcdName(alarmCode.getAlcdName());
		alarm.setAlarmNo(0);

		// 등록일시
		alarm.setRegDtm(DateUtil.getDtm());

		if (alarm.getInloNo() > 0) {
			Inlo inlo = InloApi.getApi().getInlo(alarm.getInloNo());
			alarm.setInloName(inlo == null ? "" : inlo.getInloName());
		}

		if (upper != null) {
			alarm.setModelNo(upper.getModelNo());
			try {
				MoModel model = ModelApi.getApi().getModel(upper.getModelNo());
				alarm.setModelName(model == null ? "" : model.getModelName());
			} catch (NotFoundException e) {
			}
		}

		alarm.setStatus(FxEvent.STATUS.added);

		return alarm;

	}

}

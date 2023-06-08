package fxms.bas.impl.dpo.ao.iqr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.AppApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.exp.NotFoundException;
import fxms.bas.impl.api.AlarmApiDfo;
import fxms.bas.impl.api.AppApiDfo;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.ao.AlCfgMap;
import fxms.bas.impl.dpo.ao.AlcdMap;
import fxms.bas.impl.dpo.ao.iqr.IQRCounter.IQR;
import fxms.bas.mo.Mo;
import fxms.bas.vo.AlarmCfgMem;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValues;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * IQR 알람을 확인한다.<br>
 * 알람발생 조건에 IQR 내용이 있다면 확인하여 알람을 발생한다.
 * 
 * @author subkjh
 *
 */
public class AlarmMakeIqrDfo implements FxDfo<CheckIqrDto, Boolean> {

	class Data {
		final Mo mo;
		final float nowValue;
		final List<Float> values;

		Data(Mo mo, float nowValue) {
			this.mo = mo;
			this.nowValue = nowValue;
			this.values = new ArrayList<>();
		}
	}

	public static void main(String[] args) throws Exception {
		AppApi.api = new AppApiDfo();
		ValueApi.api = new ValueApiDfo();
		MoApi.api = new MoApiDfo();
		AlarmApi.api = new AlarmApiDfo();
		AlarmMakeIqrDfo dfo = new AlarmMakeIqrDfo();
		dfo.check(ObjectUtil.toObject(FxApi.makePara("psId", "E02V4", "psDtm", 20230607101500L), CheckIqrDto.class));
	}

	private final IQRCounter counter = new IQRCounter();
	private final int SIZE = 12;

	@Override
	public Boolean call(FxFact fact, CheckIqrDto dto) throws Exception {
		return check(dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean check(CheckIqrDto dto) throws Exception {

		// 알람코드가 없다면 처리하지 않는다.
		try {
			AlcdMap.getMap().getAlarmCode(ALARM_CODE.VALUE_OVER_IQR_MAX.getAlcdNo());
		} catch (NotFoundException e) {
			Logger.logger.trace("{}", e.getMessage());
			return false;
		} catch (Exception e) {
			throw e;
		}

		Map<Long, Data> map = new HashMap<>();
		PsKind psKind = PsApi.getApi().getPsKind(dto.getPsKindName());
		PsItem psItem = PsApi.getApi().getPsItem(dto.getPsId());
		String date = dto.getDate();
		long startDtm = dto.getPsDtm();
		long endDtm = psKind.getHstimeEnd(startDtm);
		String startTime = String.valueOf(startDtm).substring(8);
		String endTime = String.valueOf(endDtm).substring(8);

		// 1. 기준값 조회
		List<PsValues> values = ValueApi.getApi().getValues(psItem.getPsId(), psKind.getPsKindName(),
				psItem.getDefKindCol(), startDtm, endDtm);
		initMap(map, values);

		// 2. 비교 일자 조회
		List<String> dates = AppApi.getApi().getSameDays(date, SIZE);
		Logger.logger.debug("dates={}", dates);

		// 3. 비교값 조회
		for (String s : dates) {
			List<PsValues> preValues = ValueApi.getApi().getValues(psItem.getPsId(), psKind.getPsKindName(),
					psItem.getDefKindCol(), Long.valueOf(s + startTime), Long.valueOf(s + endTime));
			setValue(map, preValues);
		}

		// 4. IQR 체크 및 알람 통보
		for (Data data : map.values()) {
			checkIqr(dto, data);
		}

		return true;
	}

	private void checkIqr(CheckIqrDto dto, Data data) {

		IQR iqr = counter.getIqrRange(data.values);

		List<AlarmCfgMem> memList = AlCfgMap.getMap().getAlarmCfgMem(data.mo.getAlarmCfgNo(), dto.getPsId());

		if (memList.size() == 0) {

			// 설정 조건이 없으면 기본값(1.5)로 비교한다.
			if (iqr.max < data.nowValue) {
				fireAlarm(dto, data, iqr, iqr.max, null, ALARM_CODE.VALUE_OVER_IQR_MAX.getAlcdNo());
			}

		} else {

			for (AlarmCfgMem mem : memList) {
				checkMem(dto, data, iqr, mem);
			}

		}
	}

	private void checkMem(CheckIqrDto dto, Data data, IQR iqr, AlarmCfgMem mem) {

		Map<String, Object> ext = FxApi.makePara("psId", dto.getPsId(), data.nowValue);

		ALARM_LEVEL level = null;
		float compVal;

		if (mem.getAlCriCmprVal() != null && data.nowValue > iqr.getMax(mem.getAlCriCmprVal().floatValue())) {
			compVal = iqr.getMax(mem.getAlCriCmprVal().floatValue());
			level = ALARM_LEVEL.Critical;
		} else if (mem.getAlMajCmprVal() != null && data.nowValue > iqr.getMax(mem.getAlMajCmprVal().floatValue())) {
			compVal = iqr.getMax(mem.getAlMajCmprVal().floatValue());
			level = ALARM_LEVEL.Major;
		} else if (mem.getAlMinCmprVal() != null && data.nowValue > iqr.getMax(mem.getAlMinCmprVal().floatValue())) {
			compVal = iqr.getMax(mem.getAlMinCmprVal().floatValue());
			level = ALARM_LEVEL.Minor;
		} else if (mem.getAlWarCmprVal() != null && data.nowValue > iqr.getMax(mem.getAlWarCmprVal().floatValue())) {
			compVal = iqr.getMax(mem.getAlWarCmprVal().floatValue());
			level = ALARM_LEVEL.Warning;
		} else {
			// 조건이 설정되어 있지 않다면 처리하지 않는다.
			return;
		}

		ext.put("compVal", compVal);
		ext.put("alarmLevel", level.getAlarmLevel());
		ext.put("alarmCfgNo", 0);

		fireAlarm(dto, data, iqr, compVal, ext, mem.getAlcdNo());

	}

	private void fireAlarm(CheckIqrDto dto, Data data, IQR iqr, Number compVal, Map<String, Object> etcData,
			int alcdNo) {

		// 데이터가 너무 작으면 무시한다.
		if (data.values.size() < SIZE - 2)
			return;

		String instance = dto.getPsId() + "_" + dto.getPsKindName() + "_" + dto.getPsDtm();

		String message = Lang.get("The current value is outside the IQR maximum.",
				"now:" + data.nowValue + " > com:" + compVal);

		if (etcData == null) {
			etcData = new HashMap<>();
		}
		etcData.put("alarmMemo", Arrays.toString(iqr.datas));

		try {
			AlarmApi.getApi().fireAlarm(data.mo, instance, alcdNo, null, message, etcData);

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	private void initMap(Map<Long, Data> map, List<PsValues> values) {
		for (PsValues v : values) {
			map.put(v.getMoNo(), new Data(v.getMo(), v.getValueOnly().get(0).floatValue()));
		}
	}

	private void setValue(Map<Long, Data> map, List<PsValues> values) {
		for (PsValues v : values) {
			Data data = map.get(v.getMoNo());
			if (data != null) {
				data.values.add(v.getValueOnly().get(0).floatValue());
			}
		}
	}
}

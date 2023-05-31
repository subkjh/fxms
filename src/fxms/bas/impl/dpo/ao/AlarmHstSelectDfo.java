package fxms.bas.impl.dpo.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.AlarmSelectDto;
import fxms.bas.vo.Alarm;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 테스트 알람을 발생하는 DFO
 * 
 * @author subkjh
 *
 */
public class AlarmHstSelectDfo implements FxDfo<AlarmSelectDto, List<Alarm>> {

	public static void main(String[] args) {

		AlarmHstSelectDfo dfo = new AlarmHstSelectDfo();
		Map<String, Object> para = FxApi.makePara("moClass", "SENSOR", "occurDtmStart",
				DateUtil.getDtm(System.currentTimeMillis() - 86400000) //
				, "occurDtmEnd", DateUtil.getDtm());
		try {
			List<Alarm> list = dfo.call(null, ObjectUtil.toObject(para, AlarmSelectDto.class));
			for (Alarm alarm : list) {
				System.out.println(FxmsUtil.toJson(alarm));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Alarm> call(FxFact fact, AlarmSelectDto data) throws Exception {
		return selectAlarmHst(data.getOccurDtmStart(), data.getOccurDtmEnd(), ObjectUtil.toMap(data));
	}

	public List<Alarm> selectAlarmHst(long startDate, long endDate, Map<String, Object> para) throws Exception {

		if (para == null) {
			para = new HashMap<>();
		}

		para.put("occurDtm >=", startDate);
		para.put("occurDtm <=", endDate);

		return ClassDaoEx.SelectDatas(FX_AL_ALARM_HST.class, para, Alarm.class);
	}

	public List<Alarm> selectAlarmHst(Map<String, Object> para) throws Exception {
		return ClassDaoEx.SelectDatas(FX_AL_ALARM_HST.class, para, Alarm.class);
	}
}

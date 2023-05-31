package fxms.bas.impl.dpo.ao;

import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_AL_ALARM_CUR;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Alarm;
import subkjh.dao.ClassDaoEx;

/**
 * 테스트 알람을 발생하는 DFO
 * 
 * @author subkjh
 *
 */
public class AlarmCurSelectDfo implements FxDfo<Map<String, Object>, List<Alarm>> {

	public static void main(String[] args) {

		AlarmCurSelectDfo dfo = new AlarmCurSelectDfo();
		try {
			List<Alarm> list = dfo.call(null, FxApi.makePara("moClass", "SENSOR"));
			for ( Alarm alarm : list ) {
				System.out.println(FxmsUtil.toJson(alarm));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Alarm> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectAlarmCur(data);
	}

	public List<Alarm> selectAlarmCur(Map<String, Object> para) throws Exception {
		return ClassDaoEx.SelectDatas(FX_AL_ALARM_CUR.class, para, Alarm.class);
	}

}

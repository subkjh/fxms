package fxms.bas.impl.dpo.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.FxApi;
import fxms.bas.impl.api.AlarmApiService;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.mo.GetMoListDfo;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;

/**
 * 테스트 알람을 발생하는 DFO
 * 
 * @author subkjh
 *
 */
public class AlarmTestDfo implements FxDfo<Map<String, Object>, List<Alarm>> {

	public static void main(String[] args) {

		AlarmApi.api = new AlarmApiService();

		AlarmTestDfo dfo = new AlarmTestDfo();
		FxFact fact = new FxFact();
		try {
			dfo.call(fact, FxApi.makePara("moClass", "SENSOR"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Alarm> call(FxFact fact, Map<String, Object> data) throws Exception {

		List<Alarm> ret = new ArrayList<>();
		List<Mo> list = new GetMoListDfo().selectMoList(data);
		for (Mo mo : list) {
			ret.add(makeTestAlarm(mo, 10000));
		}
		return ret;
	}

	public Alarm makeTestAlarm(Mo mo, int alcdNo) throws Exception {
		return AlarmApi.getApi().fireAlarm(mo.getMoNo(), alcdNo, null);
	}

}

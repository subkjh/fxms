package fxms.bas.impl.dpo.ao;

import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.filter.AlarmFilter;
import fxms.bas.fxo.filter.AlarmNotifier;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.OccurAlarm;

/**
 * 알람에 대한 2차 행위를 수행한다.
 * 
 * @author subkjh
 *
 */
public class AlarmFilterDfo implements FxDfo<Alarm, Alarm> {

	@Override
	public Alarm call(FxFact fact, Alarm alarm) throws Exception {
		return filter(alarm);
	}

	public Alarm filter(Alarm alarm) throws Exception {

		for (AlarmNotifier a : FxActorParser.getParser().getActorList(AlarmNotifier.class)) {
			a.notify(alarm);
		}

		return alarm;
	}

	public OccurAlarm filter(AlarmOccurEvent event, OccurAlarm alarm, Mo mo) throws Exception {

		OccurAlarm ret = alarm;
		for (AlarmFilter a : FxActorParser.getParser().getActorList(AlarmFilter.class)) {
			ret = a.filter(event, alarm, mo);
		}

		return ret;
	}
}

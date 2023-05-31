package fxms.bas.impl.api;

import fxms.bas.impl.dpo.vo.ValueAddInfluxDfo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;

/**
 * 수집데이터를 InfluxDB를 이용하여 작업한다.
 * 
 * @author subkjh
 *
 */
public class ValueApiInfluxDb extends ValueApiDfo {

	@Override
	protected int addValues(PsVoList datas) throws Exception {

		int ret = super.addValues(datas); // RDB에 1차 기록한다.

		try {
			new ValueAddInfluxDfo().add(datas); // influxDB에 또 기록한다.
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

		return ret;
	}

}

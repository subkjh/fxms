package com.fxms.bio.filter;

import fxms.bas.alarm.AlarmEvent;
import fxms.bas.alarm.AlarmFilter;
import fxms.bas.alarm.dbo.ClearAlarmDbo;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.mo.Mo;
import subkjh.bas.log.Logger;

public class BioAlarmFilter extends AlarmFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7802065704821031766L;

	@Override
	public void filter(OccurAlarmDbo alarm) {

	}

	@Override
	public void filter(ClearAlarmDbo alarm) {

	}

	@Override
	public OccurAlarmDbo filter(AlarmEvent event, OccurAlarmDbo alarm, Mo mo) {

		Logger.logger.info(".........................");
		
		return alarm;
	}

}

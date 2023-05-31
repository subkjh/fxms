package test.bas.service;

import fxms.bas.fxo.service.AlarmService;
import fxms.bas.fxo.service.AlarmServiceImpl;
import fxms.bas.fxo.service.FxServiceImpl;

public class AlarmServiceTest {
	public static void main(String[] args) {
		FxServiceImpl.start(AlarmService.class.getSimpleName(), AlarmServiceImpl.class,
				new String[] { ".", "fxservice.port=0" });
	}
}

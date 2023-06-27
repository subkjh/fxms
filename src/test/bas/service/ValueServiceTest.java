package test.bas.service;

import java.util.Map;

import fxms.bas.api.PsApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.api.ValueApi;
import fxms.bas.api.ValueApi.StatFunction;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.ValueService;
import fxms.bas.fxo.service.ValueServiceImpl;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;

public class ValueServiceTest {

	public static void main(String[] args) {

		ValueApi.api = new ValueApiDfo();

		FxServiceImpl.start(ValueService.class.getSimpleName(), ValueServiceImpl.class,
				new String[] { ".", "fxservice.port=0" });

		ValueServiceTest test = new ValueServiceTest();

		try {
			test.test2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ValueService getService() throws FxServiceNotFoundException, Exception {
		return ServiceApi.getApi().getService(ValueService.class);
	}

	void test() throws Exception {
		ValueService service = getService();
		String psKind = PsKind.PSKIND_15M;
		String psId = "MoStatus";
		long startDtm = 20230410000000L;
		long endDtm = 20230410235959L;
		PsItem item = PsApi.getApi().getPsItem(psId);

		Map<Long, Number> ret = service.getStatValue(item.getPsId(), psKind, startDtm, endDtm, StatFunction.Avg);
		System.out.println(FxmsUtil.toJson(ret));

		for (Long moNo : ret.keySet()) {
			if (ret.get(moNo).floatValue() > 0) {
				System.out.println(moNo + " " + ret.get(moNo));
				System.out.println(FxmsUtil
						.toJson(service.getValues(moNo, null, psId, psKind, item.getDefKindCol(), startDtm, endDtm)));
			}
		}
	}

	void test2() throws Exception {
		ValueService service = getService();
		String psKind = PsKind.PSKIND_15M;
		String psId = "MoStatus";
		long startDtm = 20230410000000L;
		long endDtm = 20230410235959L;
		PsItem item = PsApi.getApi().getPsItem(psId);

		System.out.println(FxmsUtil.toJson(service.getValues(psId, psKind, item.getDefKindCol(), startDtm, endDtm)));
	}
}

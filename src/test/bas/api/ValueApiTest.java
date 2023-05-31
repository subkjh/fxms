package test.bas.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.api.ValueApi.StatFunction;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.utils.DateUtil;

public class ValueApiTest {

	public static void main(String[] args) {

		ValueApi.api = new ValueApiDfo();
		MoApi.api = new MoApiDfo();

		ValueApiTest test = new ValueApiTest();
		try {
			test.test();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void test() throws Exception {

		ValueApi api = new ValueApiDfo();

		long startDtm = Long.valueOf(DateUtil.getYmd() + "000000");
		long endDtm = DateUtil.getDtm();
		long moNo = 1000;
		String psId = "MoStatus";
		String psKindName = "MIN15";
		String psKindCol = "AVG";

		PsVoRawList list = new PsVoRawList("test", System.currentTimeMillis());
		list.add(new PsVoRaw(moNo, psId, 1));
		int val0 = api.addValue(list, true);

		moNo = 1002263;

		PsValueComp val1 = api.getCurValue(moNo, null, psId);
		List<PsValueSeries> val2 = api.getValues(moNo, psId, psKindName, startDtm, endDtm);
		Map<Long, Number> val3 = api.getStatValue(psId, PsApi.getApi().getPsKind(psKindName), psKindCol, startDtm,
				endDtm, StatFunction.Count);
		List<PsValues> val4 = api.getValues(moNo, psKindName, startDtm, endDtm);
		List<PsValues> val5 = api.getValues(psId, psKindName, psKindCol, startDtm, endDtm);
		List<PsValues> val6 = api.getValues(moNo, psId, psKindName, psKindCol, startDtm, endDtm);

		System.out.println("\n\n\n\n\n");

		System.out.println(val0);
		System.out.println(FxmsUtil.toJson(val1));
		System.out.println(FxmsUtil.toJson(val2));
		System.out.println(FxmsUtil.toJson(val3));
		System.out.println(FxmsUtil.toJson(val4));
		System.out.println(FxmsUtil.toJson(val5));
		System.out.println(FxmsUtil.toJson(val6));

	}

}

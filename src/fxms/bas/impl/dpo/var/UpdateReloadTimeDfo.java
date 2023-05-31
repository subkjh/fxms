package fxms.bas.impl.dpo.var;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.VarApi;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.utils.DateUtil;

/**
 * 데이터 변경 시간을 업데이트 한다.
 * 
 * @author subkjh
 *
 */
public class UpdateReloadTimeDfo implements FxDfo<String, Boolean> {

	public static void main(String[] args) {

		UpdateReloadTimeDfo dfo = new UpdateReloadTimeDfo();
		FxFact fact = new FxFact("type", "TEST");
		try {
			dfo.call(fact, "TEST");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean call(FxFact fact, String data) throws Exception {
		long hstime = DateUtil.getDtm();
		return updateReloadTime(data, hstime);
	}

	public boolean updateReloadTime(String type, long hstime) throws Exception {

		String varName = VarApi.UPDATED_TIME_VAR + type;
		Map<String, Object> datas = FxApi.makePara("varVal", hstime);
		datas.put("varGrpName", "TIME");
		datas.put("varDispName", type + " 적용 최종시간");
		datas.put("varDesc", "최종 업데이트 된 시간을 나타낸다.");
		datas.put("chgDtm", DateUtil.getDtm());

		return new SetVarDfo().setVar(varName, datas);

	}
}

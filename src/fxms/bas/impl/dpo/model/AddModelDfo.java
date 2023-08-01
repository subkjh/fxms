package fxms.bas.impl.dpo.model;

import java.util.Map;

import fxms.bas.impl.dbo.all.FX_CF_MODEL;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

public class AddModelDfo implements FxDfo<Map<String, Object>, Integer> {

	@Override
	public Integer call(FxFact fact, Map<String, Object> datas) throws Exception {
		int userNo = fact.getUserNo();
		return addModel(userNo, datas);
	}

	public int addModel(int userNo, Map<String, Object> data) throws Exception {

		if (data.get("modelClCd") == null) {
			data.put("modelClCd", "NONE");
		}
		FxTableMaker.initRegChgMap(userNo, data);

		ClassDaoEx.open()//
				.setNextVal(FX_CF_MODEL.FX_SEQ_MODELNO, Integer.class, data, "modelNo")//
				.insertOfClass(FX_CF_MODEL.class, data)//
				.close();

		return ((Integer) data.get("modelNo")).intValue();
	}
}

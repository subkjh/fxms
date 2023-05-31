package fxms.bas.impl.dpo.model;

import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.MoModel;
import subkjh.bas.co.lang.Lang;

public class GetModelDfo implements FxDfo<Integer, MoModel> {

	@Override
	public MoModel call(FxFact fact, Integer modelNo) throws Exception {

		return selectModel(modelNo);
	}

	public MoModel selectModel(Map<String, Object> para) throws Exception {
		List<MoModel> list = new GetModelsDfo().selectModelList(para);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() == 0) {
			throw new MoNotFoundException(Lang.get("Could not find model."));
		} else {
			throw new Exception(Lang.get("Too many models have been searched."));
		}
	}

	public MoModel selectModel(int modelNo) throws Exception {
		return selectModel(FxApi.makePara("modelNo", modelNo));
	}

}

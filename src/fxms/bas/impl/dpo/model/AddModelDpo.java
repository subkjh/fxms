package fxms.bas.impl.dpo.model;

import java.util.Map;

import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.MoModel;
import subkjh.bas.co.log.Logger;

public class AddModelDpo implements FxDpo<Map<String, Object>, MoModel> {

	@Override
	public MoModel run(FxFact fact, Map<String, Object> data) throws Exception {

		int userNo = fact.getUserNo();

		try {
			return addModel(userNo, data);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

	public MoModel addModel(int userNo, Map<String, Object> datas) throws Exception {

		try {

			int modelNo = new AddModelDfo().addModel(userNo, datas);
			MoModel model = new GetModelDfo().selectModel(modelNo);
			return model;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

}

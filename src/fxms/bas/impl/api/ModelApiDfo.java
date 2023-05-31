package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.ModelApi;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.model.AddModelDpo;
import fxms.bas.impl.dpo.model.DeleteModelDfo;
import fxms.bas.impl.dpo.model.GetModelDfo;
import fxms.bas.impl.dpo.model.GetModelMappsDfo;
import fxms.bas.impl.dpo.model.GetModelsDfo;
import fxms.bas.impl.dpo.model.UpdateModelDfo;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.MoModel;

/**
 * 저장소 작업이 완료된 MoApi
 *
 * @author subkjh
 *
 */
public class ModelApiDfo extends ModelApi {

	@Override
	public MoModel addModel(int userNo, Map<String, Object> para) throws Exception {

		MoModel model = new AddModelDpo().addModel(userNo, para);

		setCache(model.getModelNo(), model);

		new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Model));

		return model;
	}

	@Override
	public boolean deleteModel(int modelNo, String modelName) throws Exception {

		if (new DeleteModelDfo().deleteModel(modelNo, modelName)) {

			setCache(modelNo, null);

			return true;
		}

		return false;
	}

	@Override
	public Map<String, Integer> getMappInloAll(String mngDiv) throws Exception {
		return new GetModelMappsDfo().selectMappModel(mngDiv);
	}

	@Override
	public List<MoModel> getModels(Map<String, Object> para) throws Exception {
		return new GetModelsDfo().selectModelList(para);
	}

	@Override
	public MoModel updateModel(int userNo, int modelNo, Map<String, Object> para) throws Exception {

		boolean ret = new UpdateModelDfo().updateModel(userNo, modelNo, para);

		if (ret) {

			MoModel model = new GetModelDfo().selectModel(modelNo);

			setCache(model.getModelNo(), model);

			new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Model));

			return model;

		}

		return null;

	}

}

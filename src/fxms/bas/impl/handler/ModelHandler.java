package fxms.bas.impl.handler;

import fxms.bas.api.ModelApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.ModelHandlerQid;
import fxms.bas.impl.handler.dto.mo.MoSearchCountDto;
import fxms.bas.impl.handler.dto.model.AddModelDto;
import fxms.bas.impl.handler.dto.model.DeleteModelDto;
import fxms.bas.impl.handler.dto.model.UpdateModelDto;
import subkjh.bas.BasCfg;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 모델 관련 처리
 * 
 * @author subkjh
 *
 */
public class ModelHandler extends BaseHandler {

	@MethodDescr(name = "모델 삭제", description = "등록된 모델을 삭제한다.")
	public Object deleteModel(SessionVo session, DeleteModelDto model) throws Exception {
		ModelApi.getApi().deleteModel(model.getModelNo(), model.getModelName());
		return model;
	}

	@MethodDescr(name = "모델 추가", description = "모델을 추가한다.")
	public Object insertModel(SessionVo session, AddModelDto data) throws Exception {
		return ModelApi.getApi().addModel(session.getUserNo(), ObjectUtil.toMap(data));
	}

	@MethodDescr(name = "모델목록조회", description = "등록된 모델을 조회한다.")
	public Object selectModelList(SessionVo session, MoSearchCountDto item) throws Exception {
		return ModelApi.getApi().getModels(ObjectUtil.toMap(item));
	}

	@MethodDescr(name = "모델 수정", description = "등록된 모델을 수정한다.")
	public Object updateModel(SessionVo session, UpdateModelDto item) throws Exception {
		return ModelApi.getApi().updateModel(session.getUserNo(), item.getModelNo(), ObjectUtil.toMap(item));
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(ModelHandlerQid.QUERY_XML_FILE));
	}
}

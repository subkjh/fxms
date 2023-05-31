package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.event.FxEvent;
import fxms.bas.exp.NotFoundException;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.MoModel;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 관리대상 관련 메소드를 가지는 API
 * 
 * @author subkjh
 *
 */
public abstract class ModelApi extends FxApi {

	/** 정의안됨 : 0 */
	public static final int NONE_MODEL_NO = 0;

	/** use DBM */
	public static ModelApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 *
	 * @return DBM
	 */
	public synchronized static ModelApi getApi() {
		if (api != null)
			return api;

		api = makeApi(ModelApi.class);

		try {
			api.reload(ReloadType.All);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	public static Map<String, MoModel> toNameMap(List<MoModel> list) {
		Map<String, MoModel> modelMap = new HashMap<>();
		for (MoModel model : list) {
			modelMap.put(model.getModelName(), model);
		}
		return modelMap;
	}

	public static Map<Integer, MoModel> toNoMap(List<MoModel> list) {
		Map<Integer, MoModel> modelMap = new HashMap<>();
		for (MoModel model : list) {
			modelMap.put(model.getModelNo(), model);
		}
		return modelMap;
	}

	private final Map<Integer, MoModel> modelMap;

	public ModelApi() {
		this.modelMap = new HashMap<>();
	}

	private void loadModel() {

		long procMstime = System.currentTimeMillis();

		Map<Integer, MoModel> tmp = new HashMap<>();
		try {
			List<MoModel> modelList = getModels(null);
			for (MoModel model : modelList) {
				tmp.put(model.getModelNo(), model);
			}

			synchronized (this.modelMap) {
				this.modelMap.clear();
				this.modelMap.putAll(tmp);
			}

			LogApi.getApi().addSystemLog(ReloadType.Model, procMstime, this.modelMap.size(), null);

		} catch (Exception e) {
			Logger.logger.error(e);
			LogApi.getApi().addSystemLog(ReloadType.Model, procMstime, -1, e);
		}

	}

	protected void setCache(int modelNo, MoModel model) {
		synchronized (this.modelMap) {
			if (model == null) {
				this.modelMap.remove(modelNo);
			} else {
				this.modelMap.put(model.getModelNo(), model);
			}
		}
	}

	public abstract MoModel addModel(int userNo, Map<String, Object> para) throws Exception;

	/**
	 * 구분에 해당되는 모든 매핑데이터를 조회한다.
	 * 
	 * @param mngDiv 구분
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, Integer> getMappInloAll(String mngDiv) throws Exception;

	/**
	 * 모델을 조회한다.
	 *
	 * @param modelNo 모델번호
	 * @return 조회된 모델
	 */
	public MoModel getModel(int modelNo) throws NotFoundException {

		synchronized (this.modelMap) {
			MoModel model = modelMap.get(modelNo);
			if (model != null) {
				return model;
			}

			throw new NotFoundException("Model", modelNo);
		}
	}

	/**
	 * 모델명, 제조사가 일치한 모델을 조회한다.
	 * 
	 * @param modelName  모델명
	 * @param vendorName 제조사명
	 * @return
	 */
	public MoModel getModel(String modelName, String vendrName) {

		try {
			List<MoModel> list = getModels(makePara("modelName", modelName, "vendrName", vendrName));
			return list.size() > 0 ? list.get(0) : null;
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return null;
	}

	/**
	 * 조건에 맞는 모델 목록을 조회한다.
	 * 
	 * @param para 조건
	 * @return 모델목록
	 * @throws Exception
	 */
	public abstract List<MoModel> getModels(Map<String, Object> para) throws Exception;

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		synchronized (this.modelMap) {
			sb.append(Logger.makeString("model.size", this.modelMap.size()));
		}
		return sb.toString();
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {
		super.onEvent(noti);
	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		// 관리대상 변경
		if (type == ReloadType.Model || type == ReloadType.All) {
			loadModel();
		}

	}

	/**
	 * 
	 * @param modelNo
	 * @param modelName
	 * @return
	 * @throws Exception
	 */
	public abstract boolean deleteModel(int modelNo, String modelName) throws Exception;

	/**
	 * 모델을 수정한다.
	 * 
	 * @param userNo
	 * @param modelNo
	 * @param para
	 * @param mappData
	 * @throws Exception
	 */
	public abstract MoModel updateModel(int userNo, int modelNo, Map<String, Object> para) throws Exception;

}

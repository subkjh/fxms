package fxms.bas.impl.dpo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.ModelNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MAPP_ETC;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class GetModelMappsDfo implements FxDfo<Map<String, Object>, Map<String, Integer>> {

	class Data {
		final String mappId;
		final int modelNo;

		Data(String mappId, int modelNo) {
			this.mappId = mappId;
			this.modelNo = modelNo;
		}
	}

	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moClass", "NODE");
		GetModelMappsDfo dpo = new GetModelMappsDfo();
		FxFact fact = new FxFact("para", para);
		try {
//			System.out.println(FxmsUtil.toJson(dpo.call(fact, "para")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Integer> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectMappModel(data);

	}

	public Map<String, Integer> selectMappModel(String mngDiv) throws Exception {
		return selectMappModel(FxApi.makePara("mngDiv", mngDiv));
	}

	public int selectMappModel(String mngDiv, String mappId) throws Exception {

		List<Data> list = select(FxApi.makePara("mngDiv", mngDiv, "mappId", mappId));
		if (list.size() == 1) {
			return list.get(0).modelNo;
		} else if (list.size() == 0) {
			throw new ModelNotFoundException(mappId);
		} else {
			throw new Exception(Lang.get("Too many models have been searched."));
		}
	}

	/**
	 * 
	 * @param para 조건
	 * @return key:mappId, value:modelNo
	 * @throws Exception
	 */
	public Map<String, Integer> selectMappModel(Map<String, Object> para) throws Exception {

		List<Data> list = select(para);

		Map<String, Integer> retMap = new HashMap<String, Integer>();
		for (Data obj : list) {
			retMap.put(obj.mappId, obj.modelNo);
		}
		return retMap;
	}

	private List<Data> select(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			List<Data> ret = new ArrayList<>();
			List<FX_MAPP_ETC> list = tran.select(FX_MAPP_ETC.class, para);
			for (FX_MAPP_ETC obj : list) {
				try {
					ret.add(new Data(obj.getMappId(), Integer.parseInt(obj.getObjData())));
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}

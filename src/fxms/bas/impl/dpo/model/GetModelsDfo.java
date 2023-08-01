package fxms.bas.impl.dpo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_CF_MODEL;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.MoModel;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class GetModelsDfo implements FxDfo<Map<String, Object>, List<MoModel>> {

	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moClass", "NODE");
		GetModelsDfo dpo = new GetModelsDfo();
		try {
			System.out.println(FxmsUtil.toJson(dpo.selectModelList(para)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MoModel> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectModelList(data);

	}

	public List<MoModel> selectModelList(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			List<FX_CF_MODEL> list = tran.selectDatas(FX_CF_MODEL.class, para);
			List<MoModel> ret = new ArrayList<>();
			for (FX_CF_MODEL a : list) {
				ret.add(new MoModel(a.getModelNo(), a.getMoClass(), a.getModelName(), a.getVendrName()));
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

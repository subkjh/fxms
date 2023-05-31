package fxms.bas.impl.dpo.co;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CO_CD;
import fxms.bas.impl.dbo.all.FX_CO_CD_CL;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Code;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.database.DBManager;

/**
 * 코드 목록 조회
 * 
 * @author subkjh
 *
 */
public class CdCodeSelectDfo implements FxDfo<Map<String, Object>, List<Code>> {

	@Override
	public List<Code> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectCodes(data);
	}

	public List<Code> selectCodes(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		if (para == null) {
			para = new HashMap<String, Object>();
		}
		para.put("useYn", "Y");

		try {
			tran.start();
			List<Code> ret = new ArrayList<Code>();
			List<FX_CO_CD> list = tran.select(FX_CO_CD.class, para);
			for (FX_CO_CD data : list) {
				ret.add(new Code(data.getCdClass(), data.getCdCode(), data.getCdName()));
			}

			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Map<String, Code> selectCodeMap(String cdClass) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		Map<String, Object> para = new HashMap<>();
		para.put("useYn", "Y");
		para.put("cdClass", cdClass);
		Map<String, Code> ret = new HashMap<>();

		try {
			tran.start();
			List<FX_CO_CD> list = tran.select(FX_CO_CD.class, para);
			for (FX_CO_CD data : list) {
				ret.put(data.getCdCode(), new Code(data.getCdClass(), data.getCdCode(), data.getCdName()));
			}
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public FX_CO_CD_CL selectCl(String cdClass) throws Exception {
		return ClassDaoEx.SelectData(FX_CO_CD_CL.class, FxApi.makePara("cdClass", cdClass));
	}

}
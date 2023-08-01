package fxms.bas.impl.dpo.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_V_ACUR;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsValueComp;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;

/**
 * 수집 데이터 알람 확인
 * 
 * @author subkjh
 *
 */
public class SelectCurValueDfo implements FxDfo<Map<String, Object>, List<PsValueComp>> {

	@Override
	public List<PsValueComp> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectCurValues(data);
	}

	/**
	 * 
	 * @param voList
	 * @throws Exception
	 */
	public List<PsValueComp> selectCurValues(Map<String, Object> para) throws Exception {

		DataBase database = DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE);
		ClassDao tran = database.createClassDao();

		try {
			tran.start();
			List<FX_V_ACUR> list = tran.selectDatas(FX_V_ACUR.class, para);

			List<PsValueComp> ret = new ArrayList<>();
			for (FX_V_ACUR value : list) {
				ret.add(new PsValueComp(value.getMoNo(), null, value.getPsId(), value.getPreCollDtm(),
						value.getPreCollVal(), value.getCurCollDtm(), value.getCurCollVal()));
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
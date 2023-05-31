package fxms.bas.impl.dpo.inlo;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MAPP_INLO;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.mapp.MappData;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 설치위치를 기록하는 DPO
 * 
 * @author subkjh
 *
 */
public class SetMappInloDfo implements FxDpo<MappData, Boolean> {

	@Override
	public Boolean run(FxFact fact, MappData data) throws Exception {

		int userNo = fact.getUserNo();
		int inloNo = fact.getInt("inloNo");
		String inloName = fact.getString("inloName");

		return setMapping(userNo, data, inloNo, inloName);
	}

	/**
	 * 
	 * @param userNo
	 * @param mappData
	 * @param inloNo
	 * @param inloName
	 * @return
	 * @throws Exception
	 */
	public boolean setMapping(int userNo, MappData mappData, int inloNo, String inloName) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			FX_MAPP_INLO mapp = new FX_MAPP_INLO();
			mapp.setMngDiv(mappData.getMngDiv());
			mapp.setMappData(mappData.getMappData());

			FX_MAPP_INLO old = tran.selectOne(FX_MAPP_INLO.class,
					FxApi.makePara("mngDiv", mapp.getMngDiv(), "mappData", mapp.getMappData()));
			if (old != null) {
				mapp = old;
			}

			mapp.setInloNo(inloNo);
			mapp.setInloName(inloName);
			mapp.setMappDescr(mappData.getMappDescr());
			mapp.setMappId(mappData.getMappId().toString());
			FxTableMaker.initRegChg(userNo, mapp);

			if (old != null) {
				tran.updateOfClass(FX_MAPP_INLO.class, mapp);
			} else {
				tran.insertOfClass(FX_MAPP_INLO.class, mapp);
			}
			tran.commit();

			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}
}

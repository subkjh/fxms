package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MappingApi;
import fxms.bas.co.DATA_STATUS;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MAPP_AL;
import fxms.bas.impl.dbo.all.FX_MAPP_ETC;
import fxms.bas.impl.dbo.all.FX_MAPP_MO;
import fxms.bas.impl.dpo.mo.MoPsIdDeleteDfo;
import fxms.bas.impl.dpo.mo.MoPsIdSelectDfo;
import fxms.bas.impl.dpo.mo.MoPsIdSetDfo;
import fxms.bas.vo.mapp.MappData;
import fxms.bas.vo.mapp.MappMo;
import fxms.bas.vo.mapp.MappMoPs;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

public class MappingApiDB extends MappingApi {

	private <T> T selectOne(Class<T> classOfT, Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return (T) tran.selectOne(classOfT, para);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<MappMo> doSelectMapp(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			List<FX_MAPP_MO> list = tran.select(FX_MAPP_MO.class, para);
			List<MappMo> ret = new ArrayList<MappMo>();
			for (FX_MAPP_MO mo : list) {
				ret.add(new MappMo(mo.getMappId(), mo.getMoNo()));
			}
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	protected int doSelectMappAlcdNo(String mngDiv, String mappId) throws Exception {
		FX_MAPP_AL data = selectOne(FX_MAPP_AL.class, makePara("mngDiv", mngDiv, "mappId", mappId));
		return data != null ? data.getAlcdNo() : null;

	}

	@Override
	protected Object doSelectMappEtc(String mngDiv, Object mappId) throws Exception {
		FX_MAPP_ETC data = selectOne(FX_MAPP_ETC.class, makePara("mngDiv", mngDiv, "mappId", mappId));
		return data != null ? data.getObjData() : null;
	}

	@Override
	protected List<MappMoPs> doSelectMappMoPs(Map<String, Object> para) throws Exception {
		return new MoPsIdSelectDfo().selectMappMoPs(para);
	}

	@Override
	protected Map<String, MappMoPs> doSelectMappMoPsAll(String mngDiv) throws Exception {
		List<MappMoPs> list = new MoPsIdSelectDfo().selectMappMoPs(mngDiv);

		Map<String, MappMoPs> ret = new HashMap<String, MappMoPs>();
		for (MappMoPs ps : list) {
			ret.put(ps.getMappId(), ps);
		}
		return ret;

	}

	@Override
	protected void doSetEtc(int userNo, MappData mappData, Object objData, String objDescr) throws Exception {

		FX_MAPP_ETC mapp = new FX_MAPP_ETC();
		mapp.setMngDiv(mappData.getMngDiv());
		mapp.setMappData(mappData.getMappData());

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_MAPP_ETC old = tran.selectOne(FX_MAPP_ETC.class,
					makePara("mngDiv", mapp.getMngDiv(), "mappData", mapp.getMappData()));
			if (old != null) {
				mapp = old;
			}

			mapp.setObjData(objData == null ? "" : objData.toString());
			mapp.setObjDescr(objDescr);
			mapp.setMappDescr(mappData.getMappDescr());
			mapp.setMappId(mappData.getMappId().toString());
			FxTableMaker.initRegChg(userNo, mapp);

			if (old != null) {
				tran.updateOfClass(FX_MAPP_ETC.class, mapp);
			} else {
				tran.insertOfClass(FX_MAPP_ETC.class, mapp);
			}

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	public DATA_STATUS setMappPs(int userNo, MappData mappData, long moNo, String moName, String psId, String psName)
			throws Exception {
		return new MoPsIdSetDfo().set(userNo, mappData, moNo, moName, psId, psName);
	}

	protected void setMapping(ClassDao tran, int userNo, MappData mappData, long moNo, String moName) throws Exception {

		FX_MAPP_MO mapp = new FX_MAPP_MO();
		mapp.setMngDiv(mappData.getMngDiv());
		mapp.setMappData(mappData.getMappData());

		try {

			FX_MAPP_MO old = tran.selectOne(FX_MAPP_MO.class,
					makePara("mngDiv", mapp.getMngDiv(), "mappData", mapp.getMappData()));
			if (old != null) {
				mapp = old;
			}

			mapp.setMoNo(moNo);
			mapp.setMoName(moName);
			mapp.setMappDescr(mappData.getMappDescr());
			mapp.setMappId(mappData.getMappId().toString());

			FxTableMaker.initRegChg(userNo, mapp);

			if (old != null) {
				tran.updateOfClass(FX_MAPP_MO.class, mapp);
			} else {
				tran.insertOfClass(FX_MAPP_MO.class, mapp);
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

	@Override
	public DATA_STATUS removeMappPs(MappData mappData) throws Exception {
		return new MoPsIdDeleteDfo().delete(mappData);
	}

}

package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.dbo.VarDbo;
import fxms.bas.dao.FxConfDao;
import fxms.bas.dbo.mo.FxServiceMoUpdateStatusAllDbo;
import fxms.bas.dbo.mo.FxServiceMoUpdateStatusDbo;
import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.FxServiceMo;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;

public class ServiceApiDB extends ServiceApi {

	@Override
	public void doSetServiceStatus(String serviceStatus) throws Exception {

		FxServiceMoUpdateStatusAllDbo dbo = new FxServiceMoUpdateStatusAllDbo(FxCfg.getCfg().getIpAddress(),
				serviceStatus, FxApi.getDate(0));
		FxConfDao.getDao().update(dbo, null);
	}

	@Override
	protected VarDbo doSelectVar(String varName) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("varName", varName);

			return tran.selectOne(VarDbo.class, para);

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<VarDbo> doSelectVarAll() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			return tran.select(VarDbo.class, null);

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void doSetServiceStatus(FxServiceMo fxService, long startDate, String serviceStatus) throws Exception {
		FxConfDao.getDao().update(
				new FxServiceMoUpdateStatusDbo(fxService.getMoNo(), startDate, serviceStatus, FxApi.getDate(0)), null);
	}

	@Override
	protected void doUpdateVarValue(String varName, Object varValue) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			VarDbo dbo = new VarDbo();
			dbo.setVarName(varName);
			dbo.setVarValue(String.valueOf(varValue));
			dbo.setChgDate(FxApi.getDate(0));
			dbo.setChgUserNo(User.USER_NO_SYSTEM);

			tran.update(dbo, null);

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	protected void initApi() throws Exception {
		super.initApi();
	}

}

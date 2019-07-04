package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.api.FxApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.FxConfDao;
import fxms.bas.co.vo.FxVar;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.co.FxServiceMoUpdateStatusAllDbo;
import fxms.bas.impl.co.FxServiceMoUpdateStatusDbo;
import fxms.bas.impl.co.VarDbo;
import fxms.bas.mo.ServiceMo;

public class ServiceApiDB extends ServiceApi {

	@Override
	public void doSetServiceStatus(String serviceStatus) throws Exception {

		FxServiceMoUpdateStatusAllDbo dbo = new FxServiceMoUpdateStatusAllDbo(FxCfg.getCfg().getIpAddress(), serviceStatus,
				FxApi.getDate(0));
		FxConfDao.getDao().update(dbo, null);
	}

	@Override
	protected FxVar doSelectVar(String varName) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("varName", varName);

			VarDbo var = tran.selectOne(VarDbo.class, para);
			return new FxVar(var.getVarName(), var.getVarValue());
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<FxVar> doSelectVarAll() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			List<VarDbo> list = tran.select(VarDbo.class, null);
			List<FxVar> ret = new ArrayList<FxVar>();
			for (VarDbo e : list) {
				ret.add(new FxVar(e.getVarName(), e.getVarValue()));
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
	protected void doSetServiceStatus(ServiceMo fxService, long startDate, String serviceStatus) throws Exception {
		FxConfDao.getDao().update(new FxServiceMoUpdateStatusDbo(fxService.getMoNo(), startDate, serviceStatus, FxApi.getDate(0)),
				null);
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

			tran.updateOfClass(VarDbo.class, dbo, null);

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

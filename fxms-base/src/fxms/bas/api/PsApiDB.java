package fxms.bas.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.pso.item.PsItem;
import subkjh.bas.BasCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;

public class PsApiDB extends PsApi {

	private String[] getPsApiDbFiles() {

		File file = new File(BasCfg.getHomeDeployConfSql());
		List<String> list = new ArrayList<String>();
		setFiles(file, list);

		return list.toArray(new String[list.size()]);
	}

	private void setFiles(File file, List<String> list) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				if (f.isDirectory()) {
					setFiles(f, list);
				} else {
					if (f.getName().endsWith("ps-api-db.xml")) {
						list.add(f.getPath());
					}
				}
			}
		}
	}

	@Override
	public <T> List<T> doSelectMoList(Class<T> classOfT, Map<String, Object> para) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao(getPsApiDbFiles());

		String qid = "SELECT-CLASS-" + classOfT.getName();

		try {
			tran.start();

			if (tran.getSqlBean(qid) == null) {
				return tran.select(classOfT, para);
			} else {
				return tran.select(classOfT, qid, para);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}


	@Override
	protected List<PsItem> doSelectPsItemAll() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			return tran.select(PsItem.class, null);

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}

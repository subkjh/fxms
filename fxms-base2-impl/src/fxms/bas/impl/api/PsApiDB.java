package fxms.bas.impl.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.PsApi;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.po.StatMakeReqDbo;
import fxms.bas.po.item.PsItem;
import fxms.bas.po.noti.NotiReqMakeStat;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;

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

	@Override
	public void onNotify(FxEvent noti) throws Exception {
		super.onNotify(noti);
		if (noti instanceof NotiReqMakeStat) {
			try {
				makeStatReq((NotiReqMakeStat) noti);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 
	 * @param vo
	 * @throws Exception
	 */
	private void makeStatReq(NotiReqMakeStat vo) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("psTable", vo.getPsTable());
			para.put("psDate", vo.getPsDate());
			para.put("psType", vo.getPsType());

			if (tran.selectOne(StatMakeReqDbo.class, para) == null) {
				StatMakeReqDbo dbo = new StatMakeReqDbo(vo.getPsTable(), vo.getPsType(), vo.getPsDate());
				dbo.setMakeReqNo(tran.getNextVal(StatMakeReqDbo.FX_SEQ_MAKEREQNO, Long.class));
				dbo.setMakePossibleDate(PS_TYPE.getPsType(vo.getPsType()).getHstimeNext(dbo.getPsDate(), 1));
				tran.insertOfClass(StatMakeReqDbo.class, dbo);
				tran.commit();
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}
}

package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_TBL_COL_DEF;
import fxms.bas.impl.dbo.all.FX_TBL_DEF;
import fxms.bas.impl.dbo.all.FX_TBL_IDX_DEF;
import fxms.bas.vo.FxTableVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

public class FxTableHandler extends BaseHandler {

	public Object deleteFxtableData(SessionVo session, Map<String, Object> parameters) throws Exception {

		String tabName = this.getString(parameters, "repository");
		Map<String, Object> data = parameters;

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			FxTableVo tab = getFxTable(tran, tabName);
			tran.delete(tab.getTable(), data);

			return data;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object getFxtableColumns(SessionVo session, Map<String, Object> parameters) throws Exception {
		return ClassDaoEx.SelectDatas(FX_TBL_COL_DEF.class, parameters, FX_TBL_COL_DEF.class);
	}

	public Object getFxtableIndexes(SessionVo session, Map<String, Object> parameters) throws Exception {
		return ClassDaoEx.SelectDatas(FX_TBL_IDX_DEF.class, parameters, FX_TBL_IDX_DEF.class);
	}

	public Object getFxtables(SessionVo session, Map<String, Object> parameters) throws Exception {
		return ClassDaoEx.SelectDatas(FX_TBL_DEF.class, parameters, FX_TBL_DEF.class);
	}

	public Object insertFxtableData(SessionVo session, Map<String, Object> parameters) throws Exception {

		String tabName = this.getString(parameters, "repository");
		Map<String, Object> data = parameters;

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("tabName", tabName);

			FxTableVo tab = getFxTable(tran, tabName);

			for (FX_TBL_COL_DEF col : tab.getSequenceColumns()) {
				long seqNo = tran.getNextVal(col.getSeqName(), Long.class);
				data.put(col.getColName(), seqNo);
			}

			initTableColumns(session, tab, data);
			tran.insert(tab.getTable(), data);

			return data;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object updateFxtableData(SessionVo session, Map<String, Object> parameters) throws Exception {

		String tabName = this.getString(parameters, "repository");
		Map<String, Object> data = parameters;

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("tabName", tabName);

			FxTableVo tab = getFxTable(tran, tabName);

			initTableColumns(session, tab, data);

			tran.updateOfClass(FX_TBL_DEF.class, tab);

			return data;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createQidDao();
	}

	private FxTableVo getFxTable(ClassDao tran, String tabName) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("tabName", tabName);

		FX_TBL_DEF tmp = tran.selectOne(FX_TBL_DEF.class, para);
		if (tmp == null) {
			return null;
		}

		FxTableVo tab = new FxTableVo(tmp.getTblName(), tmp.getTblCmnt());

		List<FX_TBL_COL_DEF> colList = tran.selectDatas(FX_TBL_COL_DEF.class, para);
		if (colList != null) {
			tab.getColumns().addAll(colList);
		}

		List<FX_TBL_IDX_DEF> idxList = tran.selectDatas(FX_TBL_IDX_DEF.class, para);
		if (idxList != null) {
			tab.getIndexes().addAll(idxList);
		}

		return tab;
	}

	private void initTableColumns(SessionVo session, FxTableVo tab, Map<String, Object> data) {
		if (tab.getColumn("CHG_USER_NO") != null) {
			data.put("chgUserNo", session.getUserNo());
		}
		if (tab.getColumn("REG_USER_NO") != null) {
			data.put("regUserNo", session.getUserNo());
		}
		if (tab.getColumn("REG_DATE") != null) {
			data.put("regDate", DateUtil.getDtm());
		}
		if (tab.getColumn("CHG_DATE") != null) {
			data.put("chgDate", DateUtil.getDtm());
		}
	}

}
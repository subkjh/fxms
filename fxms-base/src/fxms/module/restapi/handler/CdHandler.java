package fxms.module.restapi.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.dao.FxConfDao;
import fxms.bas.dbo.FX_CF_VAR;
import fxms.bas.dbo.FX_UI_CHART;
import fxms.bas.dbo.cd.FX_CD_CODE;
import fxms.bas.dbo.cd.FX_CD_OP;
import fxms.bas.dbo.cd.FX_CD_OP_ATTR;
import fxms.bas.dbo.cd.FX_CD_OP_MENU;
import fxms.bas.dbo.diag.FX_DI_LINE;
import fxms.bas.dbo.diag.FX_DI_MAIN;
import fxms.bas.dbo.diag.FX_DI_NODE;
import fxms.bas.dbo.diag.FX_DI_PROPERTY;
import fxms.bas.dbo.user.FX_UR_TIME;
import fxms.bas.exception.NotDefineException;
import fxms.bas.exception.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.noti.TargetFxEvent;
import fxms.bas.signal.ReloadSignal;
import fxms.module.restapi.handler.func.DiagramSet;
import fxms.module.restapi.handler.func.OpCodeUpdate;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.BasCfg;
import subkjh.bas.dao.control.DaoListener;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.fxdao.vo.FX_TAB;
import subkjh.bas.fxdao.vo.FX_TAB_COL;
import subkjh.bas.fxdao.vo.FX_TAB_IDX;
import subkjh.bas.fxdao.vo.FxTableVo;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User.USER_TYPE;

public class CdHandler extends CommHandler {

	public static void main(String[] args) throws Exception {
		CdHandler handler = new CdHandler();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cdType", "CODE");
		parameters.put("useYn", true);
		handler.getCodeList(null, parameters);
	}

	/**
	 * 코드 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addCode(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CD_CODE item = add(session, parameters, new FX_CD_CODE(), null);

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
		}

		return item;
	}

	public Object addFxtableData(SessionVo session, Map<String, Object> parameters) throws Exception {

		String tabName = this.getString(parameters, "repository");
		Map<String, Object> data = parameters;

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("tabName", tabName);

			FxTableVo tab = getFxTable(tran, tabName);

			for (FX_TAB_COL col : tab.getSequenceColumns()) {
				long seqNo = tran.getNextVal(col.getSeqName(), Long.class);
				data.put(col.getColName(), seqNo);
			}

			initTableColumns(session, tab, data);

			tran.insert(tab, data);

			return data;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object deleteCode(SessionVo session, Map<String, Object> parameters) throws Exception {
		FX_CD_CODE item = delete(session, parameters, new FX_CD_CODE(), null);

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
		}
		return item;
	}

	public Object deleteFxtableData(SessionVo session, Map<String, Object> parameters) throws Exception {

		String tabName = this.getString(parameters, "repository");
		Map<String, Object> data = parameters;

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			FxTableVo tab = getFxTable(tran, tabName);

			tran.delete(tab, data);

			return data;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object getAttrValueList(SessionVo session, Map<String, Object> para) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createFxDao(BasCfg.getHomeDeployConfSql("attr-qid.xml"));

		String qid = getString(para, "qid");

		try {
			tran.start();

			Map<String, Object> parameters = makePara(session, para);

			if (tran.getSqlBean(qid) == null) {
				throw new NotFoundException("qid", qid);
			}

			return tran.select(null, qid, parameters);

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object getChartList(SessionVo session, Map<String, Object> parameters) throws Exception {
		List<FX_UI_CHART> chartList = FxConfDao.getDao().select(FX_UI_CHART.class, parameters);
		chartList.sort(new Comparator<FX_UI_CHART>() {

			@Override
			public int compare(FX_UI_CHART o0, FX_UI_CHART o1) {
				return o0.getSeqBy() - o1.getSeqBy();
			}
		});
		return chartList;
	}

	public Object getCodeList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_CD_CODE.class, parameters);
	}

	public Object getOpTimeList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_UR_TIME.class, parameters);
	}

	/**
	 * 시스템 시간 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getSystemTime(SessionVo session, Map<String, Object> parameters) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("system-time", System.currentTimeMillis());
		return ret;
	}

	public Object getConfDataList(SessionVo session, Map<String, Object> parameters) throws Exception {

		String dataClass = getString(parameters, "dataClass");
		Class<?> classOfD = null;
		try {
			classOfD = Class.forName(dataClass);
		} catch (ClassNotFoundException e) {
			Logger.logger.error(e);
		}
		parameters.put("userNo", session.getUserNo());

		return FxConfDao.getDao().select(classOfD, parameters);
	}

	@SuppressWarnings("rawtypes")
	public Object getDataList(SessionVo session, Map<String, Object> parameters) throws Exception {

		Object val = parameters.get("dataClassList");
		if (val == null)
			throw new NotDefineException("dataClassList");

		if ((val instanceof List) == false) {
			throw new Exception("dataClassList is not a List");
		}

		List<Class<?>> classList = new ArrayList<Class<?>>();
		List list = (List) val;
		Class<?> classOfD = null;
		for (Object dataClass : list) {
			try {
				classOfD = Class.forName(dataClass.toString());
				classList.add(classOfD);
			} catch (ClassNotFoundException e) {
				throw e;
			}
		}

		Map<String, Object> ret = new HashMap<String, Object>();
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			parameters.put("userNo", session.getUserNo());

			for (Class<?> classOfT : classList) {
				ret.put(classOfT.getName(), tran.select(classOfT, parameters));
			}

			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object getDiagram(SessionVo session, Map<String, Object> parameters) throws Exception {

		int diagNo = getInt(parameters, "diagNo", -1);

		if (diagNo < 0) {
			getString(parameters, "diagTitle");
			parameters.put("userNo", session.getUserNo());
		}

		Map<String, Object> ret = new HashMap<String, Object>();
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			FX_DI_MAIN main = tran.selectOne(FX_DI_MAIN.class, parameters);
			if (main == null) {
				throw new NotFoundException("diagram", parameters.toString());
			}

			Map<String, Object> para = new HashMap<>();
			para.put("diagNo", main.getDiagNo());

			ret.put("node", tran.select(FX_DI_NODE.class, para));
			ret.put("line", tran.select(FX_DI_LINE.class, para));
			ret.put("property", tran.select(FX_DI_PROPERTY.class, para));
			ret.put("main", main);

			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object getDiagramList(SessionVo session, Map<String, Object> parameters) throws Exception {
		Map<String, Object> para = makePara(session, parameters);
		return FxConfDao.getDao().select(FX_DI_MAIN.class, para);
	}

	public Object getFxtableColumns(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_TAB_COL.class, parameters);
	}

	public Object getFxtableIndexes(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_TAB_IDX.class, parameters);
	}

	public Object getFxtables(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_TAB.class, parameters);
	}

	public Object getOpList(SessionVo session, Map<String, Object> parameters) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			List<FX_CD_OP> opList = tran.select(FX_CD_OP.class, parameters);
			List<FX_CD_OP_ATTR> attrList = tran.select(FX_CD_OP_ATTR.class, parameters);
			List<FX_CD_OP_MENU> memuList = tran.select(FX_CD_OP_MENU.class, parameters);

			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("op-list", opList);
			ret.put("op-attr-list", attrList);
			ret.put("op-menu-list", memuList);

			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * QID를 호출하여 결과를 조회한다.
	 * 
	 * @param session
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public Object getQidSelect(SessionVo session, Map<String, Object> para) throws Exception {

		String file = getString(para, "qid-file-name");
		String qid = getString(para, "qid");

		DbTrans tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createDbTrans(BasCfg.getHomeDeployConfSql(file));

		try {
			tran.start();

			Map<String, Object> ret = new HashMap<String, Object>(para);
			Map<String, Object> parameters = new HashMap<String, Object>(para);

			if (session.getUserType() != USER_TYPE.admin.getCode()) {
				parameters.put("userNo", session.getUserNo());
			}

			if (tran.getSqlBean(qid) == null) {
				throw new NotFoundException("qid", qid);
			}

			DaoListener listener = new DaoListener() {

				@Override
				public void onExecuted(Object data, Exception ex) throws Exception {

				}

				@Override
				public void onFinish(Exception ex) throws Exception {

				}

				@SuppressWarnings("unchecked")
				@Override
				public void onSelected(int rowNo, Object data) throws Exception {

					List<Object[]> datas = (List<Object[]>) ret.get("datas");

					if (datas == null) {
						datas = new ArrayList<Object[]>();
						ret.put("datas", datas);
					}

					datas.add((Object[]) data);
				}

				@Override
				public void onStart(String[] colNames) throws Exception {
					ret.put("columns", colNames);
				}

			};

			tran.setDaoListener(listener);
			tran.selectQid2Res(qid, parameters);
			return ret;

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object getVarList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_CF_VAR.class, parameters);
	}

	public Object selectQid(SessionVo session, Map<String, Object> para) throws Exception {

		String file = getString(para, "qid-file-name");
		String qid = getString(para, "qid");

		DbTrans tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createDbTrans(BasCfg.getHomeDeployConfSql(file));

		try {
			tran.start();

			Map<String, Object> parameters = new HashMap<String, Object>(para);
			if (session.getUserType() != USER_TYPE.admin.getCode()) {
				parameters.put("userNo", session.getUserNo());
			}

			if (tran.getSqlBean(qid) == null) {
				throw new NotFoundException("qid", qid);
			}

			return tran.selectQid2Res(qid, parameters);

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object sendFxEvent(SessionVo session, Map<String, Object> parameters) throws Exception {

		String type = getString(parameters, "type");
		String target = getString(parameters, "target");

		FxServiceImpl.fxService.send(new TargetFxEvent(type, target));

		return parameters;

	}

	public Object setDiagram(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new DiagramSet().set(session, parameters);
	}

	public Object updateCode(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CD_CODE item = update(session, parameters, new FX_CD_CODE(), null);

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
		}

		return item;
	}

	public Object updateFxtableData(SessionVo session, Map<String, Object> parameters) throws Exception {

		String tabName = this.getString(parameters, "repository");
		Map<String, Object> data = parameters;

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("tabName", tabName);

			FxTableVo tab = getFxTable(tran, tabName);

			initTableColumns(session, tab, data);

			tran.update(tab, data);

			return data;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 운용 기능에 대한 권한을 변경한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateOpcode(SessionVo session, Map<String, Object> parameters) throws Exception {

		int opNo = getInt(parameters, "opNo");
		int ugrpNo = getInt(parameters, "ugrpNo");
		Object value = parameters.get("includeSubYn");

		new OpCodeUpdate(opNo, ugrpNo, value != null && value.toString().equalsIgnoreCase("y"));

		return parameters;

	}

	/**
	 * 운용자 화면 크기 설정
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateOpSize(SessionVo session, Map<String, Object> parameters) throws Exception {

		int opNo = getInt(parameters, "opNo");
		int uiWidth = getInt(parameters, "uiWidth", -1);
		int uiHeight = getInt(parameters, "uiHeight", -1);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("opNo", opNo);

		if (uiWidth > 0) {
			map.put("uiWidth", uiWidth);
		}

		if (uiHeight > 0) {
			map.put("uiHeight", uiHeight);
		}

		if (uiWidth > 0 || uiHeight > 0) {
			DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao().updateOfClass(FX_CD_OP.class, map);
		}

		return map;
	}

	private FxTableVo getFxTable(FxDaoExecutor tran, String tabName) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("tabName", tabName);

		FxTableVo tab = tran.selectOne(FxTableVo.class, para);
		List<FX_TAB_COL> colList = tran.select(FX_TAB_COL.class, para);
		if (colList != null) {
			tab.getColumns().addAll(colList);
		}

		List<FX_TAB_IDX> idxList = tran.select(FX_TAB_IDX.class, para);
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
			data.put("regDate", FxApi.getDate());
		}
		if (tab.getColumn("CHG_DATE") != null) {
			data.put("chgDate", FxApi.getDate());
		}
	}

}

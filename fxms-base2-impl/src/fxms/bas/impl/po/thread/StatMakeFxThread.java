package fxms.bas.impl.po.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.control.DaoExecutor;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.api.FxApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.VoApi;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.bas.impl.api.VoApiDB;
import fxms.bas.impl.po.RemakeStatReq;
import fxms.bas.impl.po.StatMakeReqDbo;
import fxms.bas.impl.po.StatMakeReqHstDbo;
import fxms.bas.po.item.PsItem;

public class StatMakeFxThread extends CycleFxThread implements FxServiceMember {

	class StatDao extends DaoExecutor {

		public StatDao() {
			super.setDatabase(DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE));
		}

		public void makeStat() throws Exception {
			List<StatMakeReqDbo> reqList = getReqList();
			for (StatMakeReqDbo req : reqList) {
				makeStat(req);
			}
		}

		private List<StatMakeReqDbo> getReqList() throws Exception {

			FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
			Map<String, Object> para = new HashMap<String, Object>();
			try {
				tran.start();
				para.put("makePossibleDate <= ", FxApi.getDate(0));
				return tran.select(StatMakeReqDbo.class, para);

			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			} finally {
				tran.stop();
			}
		}

		private String getSqlDelete(String psTable, PS_TYPE dstType, long psDate) {

			String destTable = psTable + dstType.getTableTag(psDate);
			StringBuffer sql = new StringBuffer();

			sql.append("delete from " + destTable + " where " + VoApiDB.PS_DATE.getName() + "=" + dstType.getHstimeStart(psDate));

			return sql.toString();

		}

		private String getSqlMake(String psTable, PS_TYPE srcType, PS_TYPE dstType, long psDate) throws Exception {

			List<PsItem> itemList = PsApi.getApi().getPsItemList(psTable);
			StringBuffer sql = new StringBuffer();
			StringBuffer dest = new StringBuffer();
			StringBuffer src = new StringBuffer();
			String destTable = psTable + dstType.getTableTag(psDate);
			String srcTable = psTable + srcType.getTableTag(psDate);

			dest.append(VoApiDB.MO_NO.getName());
			dest.append(", ");
			dest.append(VoApiDB.MO_INSTANCE.getName());
			dest.append(", ");
			dest.append(VoApiDB.PS_DATE.getName());
			dest.append(", ");
			dest.append(VoApiDB.DATA_COUNT.getName());
			dest.append(", ");
			dest.append(VoApiDB.INS_DATE.getName());

			src.append(VoApiDB.MO_NO.getName());
			src.append(", ");
			src.append(VoApiDB.MO_INSTANCE.getName());
			src.append(", ");
			src.append(dstType.getHstimeStart(psDate));
			src.append(", count(1)");
			src.append(", " + FxApi.getDate(0));

			for (PsItem item : itemList) {
				dest.append("\n, ");
				src.append("\n, ");
				item.fillSql(dest, src, PS_TYPE.RAW);
			}

			sql.append("insert into " + destTable + " ( ");
			sql.append(dest);
			sql.append("\n )\n select ");
			sql.append(src);
			sql.append("\nfrom " + srcTable);

			sql.append("\n where " + VoApiDB.PS_DATE.getName() + " >= " + dstType.getHstimeStart(psDate));
			sql.append("\n and " + VoApiDB.PS_DATE.getName() + " <= " + dstType.getHstimeEnd(psDate));
			sql.append("\n group by ");
			sql.append(VoApiDB.MO_NO.getName());
			sql.append(", ");
			sql.append(VoApiDB.MO_INSTANCE.getName());

			return sql.toString();

		}

		private void makeStat(StatMakeReqDbo req) throws Exception {

			String insSql;
			String delSql;
			String errmsg = null;

			StatMakeReqHstDbo hst = new StatMakeReqHstDbo();
			hst.setChgDate(FxApi.getDate(0));
			hst.setChgUserNo(0);
			hst.setMakeReqNo(req.getMakeReqNo());
			hst.setPsDate(req.getPsDate());
			hst.setPsTable(req.getPsTable());
			hst.setPsType(req.getPsType());
			hst.setRegDate(FxApi.getDate(0));
			hst.setRegUserNo(0);
			hst.setStartDate(FxApi.getDate(0));

			try {
				insSql = getSqlMake(req.getPsTable(), PS_TYPE.RAW, PS_TYPE.getPsType(req.getPsType()), req.getPsDate());
				delSql = getSqlDelete(req.getPsTable(), PS_TYPE.getPsType(req.getPsType()), req.getPsDate());

				int rowSize = 0;

				try {
					start();

					executeSql(delSql);

					rowSize = executeSql(insSql);

					hst.setRowSize(rowSize);

					commit();

				} catch (Exception e) {
					Logger.logger.error(e);
					hst.setRowSize(-1);
				} finally {
					stop();
				}

			} catch (Exception e) {
				Logger.logger.error(e);
				errmsg = e.getClass().getSimpleName() + " : " + e.getMessage();
				hst.setRowSize(-1);
			}

			hst.setMakeState(StatMakeReqDbo.STATE_COMPLETED);
			hst.setRetMsg(errmsg);
			hst.setEndDate(FxApi.getDate(0));

			FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

			try {
				tran.start();

				tran.deleteOfClass(req.getClass(), req, null);
				tran.insertOfClass(hst.getClass(), hst);

				tran.commit();

				reqMap.remove(req.getKey());

			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			} finally {
				tran.stop();
			}
		}
	}

	private Map<String, RemakeStatReq> reqMap;

	private StatDao statDao;

	private final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private String yyyymmdd = "";

	public StatMakeFxThread() throws Exception {

		super(StatMakeFxThread.class.getSimpleName(), "period 60");

		reqMap = new HashMap<String, RemakeStatReq>();
		statDao = new StatDao();

		initMap();

	}

	@Override
	public String getState(LOG_LEVEL level) {
		return super.toString() + " YYYYMMDD=" + yyyymmdd;
	}

	public synchronized int add(RemakeStatReq req) {

		RemakeStatReq old = reqMap.get(req.getKey());
		if (old != null) {
			return 0;
		}

		try {
			insert(req);
			reqMap.put(req.getKey(), req);
			return 1;
		} catch (Exception e) {
			Logger.logger.error(e);
			return -1;
		}
	}

	@Override
	protected void doInit() {
		makeTables();
	}

	private void makeTables() {
		String today = YYYYMMDD.format(new Date(System.currentTimeMillis()));

		if (yyyymmdd.equals(today) == false) {

			try {
				String createMsg = VoApi.getApi().makePsTables();
				String dropMsg = VoApi.getApi().dropPsTables();

				Logger.logger.info("PS TABLES\n" + createMsg + dropMsg);

				yyyymmdd = today;

			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}
	}

	@Override
	protected void doCycle(long mstime) {

		makeTables();

		try {
			statDao.makeStat();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	private void initMap() throws Exception {

		reqMap.clear();

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("makeState", StatMakeReqDbo.STATE_READY);

			List<RemakeStatReq> reqList = tran.select(RemakeStatReq.class, para);

			for (RemakeStatReq req : reqList) {
				reqMap.put(req.getKey(), req);
			}

			Logger.logger.debug("STAT-MAKE-REQ({})", reqMap.size());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	private void insert(RemakeStatReq req) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			StatMakeReqDbo dbo = new StatMakeReqDbo(req.getPsTable(), req.getPsType(), req.getPsDate());
			dbo.setMakeReqNo(tran.getNextVal(StatMakeReqDbo.FX_SEQ_MAKEREQNO, Long.class));
			dbo.setMakePossibleDate(PS_TYPE.getPsType(req.getPsType()).getHstimeNext(dbo.getPsDate(), 1));
			tran.insertOfClass(dbo.getClass(), dbo);
			tran.commit();

			req.setMakeReqNo(dbo.getMakeReqNo());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	public void startMember() throws Exception {
		start();
	}
}

package fxms.bas.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.dbo.cd.OpCode;
import fxms.bas.dbo.user.FX_UR_TIME;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.user.dbo.UserLogDbo;

public class FxConfDao {

	private static FxConfDao dao;

	public static FxConfDao getDao() {
		if (dao == null) {
			dao = new FxConfDao();
		}
		return dao;
	}

	public static Map<String, Object> makePara(Object... para) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (para.length > 0) {
			for (int i = 0; i < para.length; i += 2) {
				map.put(String.valueOf(para[i]), para[i + 1]);
			}
		}
		return map;
	}

	private FxConfDao() {

	}

	/**
	 * 데이터를 삭제한다.
	 * 
	 * @param obj
	 *            삭제할 데이터
	 * @return 삭제된 데이터
	 * @throws Exception
	 */
	public <T> T deleteOfObject(T obj) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.deleteOfObject(obj);
			tran.commit();
			return obj;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 데이터를 추가한다.
	 * 
	 * @param obj
	 *            추가할 데이터
	 * @return 추가한 데이터
	 * @throws Exception
	 */
	public <T> T insert(T obj) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.insert(obj);
			tran.commit();
			return obj;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 리스트 데이터를 추가한다.
	 * 
	 * @param classOfT
	 *            데이터 종류
	 * @param list
	 *            데이터 목록
	 * @return 추가한 건수
	 * @throws Exception
	 */
	public int insertList(Class<?> classOfT, List<?> list) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			int ret = tran.insertOfClass(classOfT, list);
			tran.commit();
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public UserLogDbo insertUsrLog(UserLogDbo obj, OpCode opcode) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			long opSeqno = tran.getNextVal("FX_SEQ_OPSEQNO", Long.class);
			obj.setOpSeqno(opSeqno);
			tran.insert(obj);

			if (opcode != null && opcode.getDataType() != null) {
				FX_UR_TIME data = new FX_UR_TIME();
				data.setDataCount(0);
				data.setDataType(opcode.getDataType());
				data.setDoneDate(obj.getSrtDate().longValue());
				data.setUserNo(obj.getUserNo().intValue());
				tran.update(data, null);
			}

			tran.commit();
			return obj;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 데이터를 조회한다.
	 * 
	 * @param classOfT
	 *            조회할 종류
	 * @param parameters
	 *            조건
	 * @return 조회 목록
	 * @throws Exception
	 */
	public <T> List<T> select(Class<T> classOfT, Map<String, Object> parameters) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();
			return tran.select(classOfT, parameters);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 하나의 데이터를 조회한다.
	 * 
	 * @param classOfT
	 *            조회할 종류
	 * @param parameters
	 *            조건
	 * @return 조회된 데이터
	 * @throws Exception
	 */
	public <T> T selectOne(Class<T> classOfT, Map<String, Object> parameters) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();
			List<T> list = tran.select(classOfT, parameters);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 데이터를 수정한다.
	 * 
	 * @param obj
	 *            수정할 데이터
	 * @param para
	 *            추가되는 조건
	 * @return 수정된 데이터
	 * @throws Exception
	 */
	public <T> T update(T obj, Map<String, Object> para) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.update(obj, para);
			tran.commit();
			return obj;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 데이터를 수정한다.
	 * 
	 * @param classOf
	 *            수정할 데이터 클래스
	 * @param para
	 *            수정할 데이터
	 * @throws Exception
	 */
	public void updateOfClass(Class<?> classOf, Map<String, Object> para) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.updateOfClass(classOf, para);
			tran.commit();
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}

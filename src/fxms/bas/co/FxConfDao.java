package fxms.bas.co;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_MODEL;
import fxms.bas.impl.handler.dto.mo.MoSearchCountDto;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class FxConfDao {

	public static void main(String[] args) throws Exception {
		MoSearchCountDto item = new MoSearchCountDto();
		FxConfDao.getDao().select(FX_CF_MODEL.class, item);
	}

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
	 * @param obj 삭제할 데이터
	 * @return 삭제된 데이터
	 * @throws Exception
	 */
	public <T> T deleteOfObject(T obj) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			tran.deleteOfObject(obj.getClass(), obj);
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
	 * @param obj 추가할 데이터
	 * @return 추가한 데이터
	 * @throws Exception
	 */
	public <T> T insert(T obj) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			tran.insertOfClass(obj.getClass(), obj);
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
	 * @param classOfT 데이터 종류
	 * @param list     데이터 목록
	 * @return 추가한 건수
	 * @throws Exception
	 */
	public int insertList(Class<?> classOfT, List<?> list) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

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

	public <T> List<T> select(Class<T> classOfT, Object whereObj) throws Exception {
		return select(classOfT, whereObj, classOfT);
	}

	/**
	 * 데이터를 조회한다.
	 * 
	 * @param classOfT   조회할 종류
	 * @param parameters 조건
	 * @return 조회 목록
	 * @throws Exception
	 */
	public <T> List<T> select(Class<?> classOfT, Object whereObj, Class<T> classOfResult) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return tran.selectDatas(classOfT, whereObj, classOfResult);
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
	 * @param classOfT   조회할 종류
	 * @param parameters 조건
	 * @return 조회된 데이터
	 * @throws Exception
	 */
	public <T> T selectOne(Class<T> classOfT, Map<String, Object> parameters) throws Exception {
		return selectOne(classOfT, parameters, classOfT);
	}

	public <T> T selectOne(Class<?> classOfT, Map<String, Object> parameters, Class<T> classOfResult) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return tran.selectOne(classOfT, parameters, classOfResult);
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
	 * @param classOf 수정할 데이터 클래스
	 * @param para    수정할 데이터
	 * @throws Exception
	 */
	public void updateOfClass(Class<?> classOf, Object updateObj) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			tran.updateOfClass(classOf, updateObj);
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

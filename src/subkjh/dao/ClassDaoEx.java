package subkjh.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MO;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.database.DBManager;
import subkjh.dao.exp.TooManyDatasException;
import subkjh.dao.util.FxTableMaker;

/**
 * 
 * @author subkjh
 *
 */
public class ClassDaoEx {

	public static <T> T getNextVal(String sequence, Class<T> classOfT) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return tran.getNextVal(sequence, classOfT);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public static int InsertOfClass(Class<?> classOfDbo, Object datas) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			int ret = tran.insertOfClass(classOfDbo, datas);
			tran.commit();
			return ret;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public static void main(String[] args) {

		List<Thread> list = new ArrayList<>();

		for (int i = 0; i < 10000; i++) {

			list.clear();

			for (int n = 0; n < 3; n++) {

				Thread th = new Thread() {
					public void run() {
						try {
							ClassDaoEx dao = ClassDaoEx.open();
							System.out.println("count = " + dao.selectCount(FX_MO.class, null));
							dao.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				th.setName("TEST" + n);
				th.start();

				list.add(th);

			}

			for (Thread th : list) {
				try {
					th.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 입력된 인자를 이용하여 Class Map을 생성한다.
	 * 
	 * @param parameters
	 * @return
	 */
	public static Map<String, Object> makePara(Object... parameters) {

		if (parameters == null || parameters.length < 2) {
			return null;
		}

		Map<String, Object> para = new HashMap<String, Object>();
		for (int i = 0; i < parameters.length; i += 2) {
			para.put(String.valueOf(parameters[i]), parameters[i + 1]);
		}

		return para;
	}

	public static ClassDaoEx open() throws Exception {
		ClassDaoEx dao = new ClassDaoEx();
		dao.start();
		return dao;
	}

	public static ClassDaoEx open(String dbName) throws Exception {
		ClassDaoEx dao = new ClassDaoEx();
		dao.start(dbName);
		return dao;
	}

	public static int SelectCount(Class<?> classOfTable, Object whereObj) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return tran.selectCount(classOfTable, whereObj);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public static <T> T SelectData(Class<T> classOfDbo, Object para) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return tran.selectOne(classOfDbo, para);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param classOfDbo    테이블클래스
	 * @param para          조건
	 * @param classOfResult 데이타클래스
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> SelectDatas(Class<?> classOfDbo, Object para, Class<T> classOfResult) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return tran.select(classOfDbo, para, classOfResult);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public static <T> List<T> SelectDatas(Class<T> classOfT, Object para) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			return tran.select(classOfT, para);
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}

	public static int UpdateOfClass(Class<?> classOfDbo, Object datas) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			int ret = tran.updateOfClass(classOfDbo, datas);
			tran.commit();
			return ret;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	private ClassDao tran;
//	private StringBuffer msg = new StringBuffer();
	private Exception exception = null;
	private int processedCount; // 최근 처리 건수

	public ClassDaoEx close() throws Exception {
		close(exception);
		return this;
	}

	public ClassDaoEx deleteOfClass(Class<?> classOfDbo, Map<String, Object> para) throws Exception {

		try {
			processedCount = tran.deleteOfClass(classOfDbo, para);
		} catch (Exception e) {
			close(e);
			throw e;
		}

//		if (msg.length() > 0)
//			msg.append(", ");
//		msg.append("deleted ").append(classOfDbo.getSimpleName()).append(" = ").append(ret);

		return this;
	}

	public ClassDaoEx executeSql(String sql) throws Exception {
		try {
			processedCount = tran.executeSql(sql);
		} catch (Exception e) {
			close(e);
			throw e;
		}

		return this;
	}

	public ClassDaoEx executeSql(String sql, Collection<Object[]> datas) throws Exception {
		try {
			processedCount = tran.executeSql(sql, datas);
		} catch (Exception e) {
			close(e);
			throw e;
		}

		return this;
	}

	/**
	 * 
	 * @return 최근 처리 건수
	 */
	public int getProcessedCount() {
		return processedCount;
	}

	public ClassDaoEx insertOfClass(Class<?> classOfDbo, Object datas) throws Exception {
		try {
			processedCount = tran.insertOfClass(classOfDbo, datas);
		} catch (Exception e) {
			this.exception = e;
			close();
			throw e;
		}

//		if (msg.length() > 0)
//			msg.append(", ");
//		msg.append("inserted ").append(classOfDbo.getSimpleName()).append(" = ").append(processedCount);

		return this;
	}

	public int selectCount(Class<?> classOfTable, Object para) throws Exception {
		int ret;
		try {
			ret = tran.selectCount(classOfTable, para);
			return ret;
		} catch (Exception e) {
			close(e);
			throw e;
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param classOfDbo
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public <T> T selectData(Class<T> classOfDbo, Object para) throws Exception {
		try {
			List<T> list = tran.select(classOfDbo, para);
			if (list.size() == 1) {
				return list.get(0);
			} else if (list.size() > 0) {
				throw new TooManyDatasException(list.size());
			}
			return null;
		} catch (Exception e) {
			close(e);
			throw e;
		}
	}

	public int selectDataCount(Class<?> classOfTable, Object whereObj) throws Exception {
		try {
			return tran.selectCount(classOfTable, whereObj);
		} catch (Exception e) {
			close(e);
			throw e;
		}
	}

	public <T> List<T> selectDatas(Class<T> classOfT, Object para) throws Exception {
		try {
			return tran.select(classOfT, para);
		} catch (Exception e) {
			close(e);
			throw e;
		}
	}

	public <T> List<T> selectDatas(Class<T> classOfT, Object para, Class<T> classOfResult) throws Exception {
		try {
			return tran.select(classOfT, para, classOfResult);
		} catch (Exception e) {
			close(e);
			throw e;
		}
	}

	/**
	 * 데이터를 수정한다.
	 * 
	 * @param classOfDbo 수정에 사용할 클래스
	 * @param para       검색 조건
	 * @param data       수정할 데이터
	 * @return
	 * @throws Exception
	 */
	public ClassDaoEx setOfClass(Class<?> classOfDbo, Object para, Map<String, Object> data) throws Exception {

		try {
			Object obj = selectData(classOfDbo, para);

			if (obj != null) {
				ObjectUtil.toObject(data, obj);
				FxTableMaker.initRegChg(0, obj);
				updateOfClass(classOfDbo, obj);

			} else {
				obj = classOfDbo.newInstance();
				ObjectUtil.toObject(data, obj);
				FxTableMaker.initRegChg(0, obj);
				insertOfClass(classOfDbo, obj);
			}

		} catch (Exception e) {
			throw e;
		}

		return this;
	}

	public ClassDaoEx updateOfClass(Class<?> classOfDbo, Object datas) throws Exception {
		try {
			processedCount = tran.updateOfClass(classOfDbo, datas);
		} catch (Exception e) {
			close(e);
			throw e;
		}

//		if (msg.length() > 0)
//			msg.append(", ");
//		msg.append("updated ").append(classOfDbo.getSimpleName()).append(" = ").append(processedCount);

		return this;
	}

	private void close(Exception ex) throws Exception {

		if (tran == null) {
			return;
		}

		try {
			if (ex == null) {

				tran.commit();

//				if (msg.length() > 0) {
//					Logger.logger.info("{}", msg);
//				}

			} else {
				Logger.logger.error(ex);
				tran.rollback();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
			tran = null;
		}

	}

	private ClassDaoEx start() throws Exception {

		tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		tran.start();

		return this;
	}

	private ClassDaoEx start(String dbName) throws Exception {

		tran = DBManager.getMgr().getDataBase(dbName).createClassDao();
		tran.start();

		return this;
	}
}

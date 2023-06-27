package subkjh.dao.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.dao.database.DataBase;
import subkjh.dao.queries.SqlPointer;
import subkjh.dao.util.QidParser;

/**
 * 쿼리 XML 화일을 분석하여 보관하는 클래스
 * 
 * @since 2009-10-28
 * @author subkjh
 * 
 */
public class QidPool {

	/** key=QID|DATABASE, select, insert, update, delete 관련 내용을 가지고 있는다. */
	private Map<String, SqlBean> qidMap = new HashMap<String, SqlBean>();
	private Map<String, DaoResult> resultMap = new HashMap<String, DaoResult>();
	private Map<String, SqlConst> constMap = new HashMap<String, SqlConst>();

	private String database;
	private List<String> fileList = new ArrayList<String>();

	public QidPool(String database, InputStream inputStream) throws Exception {
		this.database = database;

		QidParser parser = new QidParser(constMap, resultMap, database);
		parser.parse(inputStream, null);

		setRet(parser);
	}

	public QidPool(String database, Map<String, SqlConst> constMap) {
		this.database = database;

		if (constMap != null) {
			for (String key : constMap.keySet()) {
				this.constMap.put(key, constMap.get(key));
			}
		}
	}

	/**
	 * 사용할 SqlBean을 추가합니다.
	 * 
	 * @param sqlBean
	 * @return 이미 있다면 기존의 SqlBean이고 없다면 null을 제공합니다.
	 */
	public void add(SqlBean sqlBean) {

		SqlBean sqlBeanOld = qidMap.get(sqlBean.getKey());

		if (sqlBeanOld != null) {
			if (sqlBean.equals(sqlBeanOld) == false) {
				Logger.logger.info("SQLBEAN({}) Check QID Replace", sqlBean.getKey());
			}
		}

		qidMap.put(sqlBean.getKey(), sqlBean);

	}

	/**
	 * 상수를 추가합니다.
	 * 
	 * @param id  상수ID
	 * @param val 상수값
	 */
	public void addConst(String id, String val) {
		SqlConst sqlConst = new SqlConst();
		sqlConst.id = id;
		sqlConst.text = val;
		sqlConst.database = null;
		constMap.put(sqlConst.id, sqlConst);
	}

	/**
	 * 쿼리 XML 화일 내용을 등록합니다.
	 * 
	 * @param ignoreNotExist 없으면 무시할지 여부
	 * @param xmlFiles       추가할 화일 목록
	 * @throws Exception
	 */
	public void addFile(String... fileNames) throws FileNotFoundException, Exception {

		for (String file : fileNames) {
			if (exist(file) == false)
				throw new FileNotFoundException(file);
		}

		QidParser parser = new QidParser(constMap, resultMap, database);
		parser.setFileList(fileList);
		parser.parse(fileNames);

		fileList = parser.getFileList();

		setRet(parser);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public String getConst(String name) {
		if (constMap == null)
			return null;
		SqlConst sqlConst = constMap.get(name);
		return sqlConst == null ? null : sqlConst.text;
	}

	/**
	 * 같은 클래스 종류의 SqlBean QID를 제공합니다.
	 * 
	 * @param c 찾고자하는 SqlBean Class
	 * @return QID 목록
	 */
	public List<String> getIdList(Class<? extends SqlBean> c) {
		List<String> queryIds = new ArrayList<String>();
		synchronized (qidMap) {
			for (SqlBean sql : qidMap.values()) {
				if (sql.getClass() == c) {
					queryIds.add(sql.getQid());
				}
			}
		}
		return queryIds;
	}

	/**
	 * key=QID|DATABASE<br>
	 * 
	 * @return
	 */
	public Map<String, SqlBean> getQidMap() {
		return qidMap;
	}

	public Map<String, DaoResult> getResultMap() {
		return resultMap;
	}

	/**
	 * 데이터베이스 종류에 맞는 QID 내용을 제공합니다.<br>
	 * QID + 탭 + 데이터베이스
	 * 
	 * @param qid
	 * @param database
	 * @return SqlBean
	 */
	public SqlBean getSqlBean(String qid, DataBase database) {
		synchronized (qidMap) {
			if (database == null)
				return null;
			SqlBean sqlBean = qidMap.get(qid + "|" + database.getKind());
			if (sqlBean == null)
				sqlBean = qidMap.get(qid);
			return sqlBean;
		}
	}

	/**
	 * SqlBean 목록을 새로운 List담아 제공합니다.
	 * 
	 * @return
	 */
	public List<SqlBean> getSqlBeanList() {
		List<SqlBean> list = new ArrayList<SqlBean>();
		if (qidMap != null) {
			for (SqlBean sqlBean : qidMap.values()) {
				list.add(sqlBean);
			}
		}
		return list;
	}

	@Override
	public String toString() {
		return "Qid Count=[" + qidMap.size() + "]";
	}

	/**
	 * 파일이 폴더 또는 jar에 존재하는지 확인합니다.
	 * 
	 * @param filename 확인할 파일명
	 * @return 존재여부
	 */
	private boolean exist(String filename) {

		File file = new File(filename);
		if (file.exists())
			return true;

		return new SqlPointer().getResource(filename) != null;
	}

	private void setRet(QidParser parser) throws Exception {
		Map<String, DaoResult> retMap = parser.getResultMap();
		List<SqlBean> qidList = parser.getQidList();
		for (SqlBean qid : qidList) {
			add(qid);
		}

		for (DaoResult ret : retMap.values()) {
			DaoResult retOld = resultMap.get(ret.rID);
			if (retOld.equals(ret) == false) {
				Logger.logger.info("Check Same ReulstID different Attr", ret.rID);
			}
			resultMap.put(ret.rID, ret);
		}

	}
}

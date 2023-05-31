package subkjh.dao.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.dao.exp.DBTypeNotFoundException;
import subkjh.dao.model.QidPool;
import subkjh.dao.model.SqlBean;
import subkjh.dao.queries.QueriesQid;
import subkjh.dao.queries.SqlPointer;

/**
 * 데이터베이스 목록 및 데이터베이스 접근을 위한 트랜잭션을 제공합니다.
 * 
 * @author subkjh
 * @since 2009-10-29
 * 
 */
public class DBManager {

	private static DBManager manager = null;

	public static DBManager getMgr() {
		if (manager == null) {
			manager = new DBManager();
			try {
				manager.init();
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
		return manager;
	}

	public static void main(String[] args) throws Exception {
		DBManager.getMgr();
	}

	/**
	 * 등록된 데이터 베이스 MAP<br>
	 * key : 데이터베이스명
	 */
	private final Map<String, DataBase> dbMap;

	/**
	 * 
	 */
	private QidPool sqlPool = null;

	private final QidPool sqlPoolCommon;

	private DBManager() {
		this.dbMap = new HashMap<String, DataBase>();
		this.sqlPoolCommon = initSqlPoolCommon();
	}

	/**
	 * 데이터베이스를 추가합니다.<br>
	 * 동일한 명칭이 있다면 그 내용을 제거됩니다.
	 * 
	 * @param database 등록할 데이터 베이스
	 * @return 이전에 등록된 데이터베이스. 만약 이전에 등록된 데이터베이스가 없다면 null을 제공합니다.
	 */
	public DataBase addDataBase(DataBase database) {
		synchronized (dbMap) {
			return dbMap.put(database.getName(), database);
		}
	}

	/**
	 * 파일을 이용하여 데이터베이스를 등록합니다.
	 * 
	 * @param file 정의된 XMl 형식의 화일명
	 * @throws Exception
	 */
	public void addDataBase(File file) throws Exception {
		addDataBase(new FileInputStream(file), file);
	}

	/**
	 * 파일을 이용하여 데이터베이스를 등록합니다.
	 * 
	 * @param inputStream 입력 스트림
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void addDataBase(InputStream inputStream, File file) throws Exception {

		SAXBuilder builder = new SAXBuilder();

		Document document = builder.build(inputStream);
		if (document == null)
			return;

		Element root = document.getRootElement();

		List<Element> nodes = root.getChildren();
		if (nodes == null)
			return;

		String enable;
		String name;
		String javaClass;
		DataBase database = null;
		List<Element> children;
		StringBuffer sb = new StringBuffer();

		for (Element node : nodes) {

			if (node.getName().equals("database")) {

				enable = node.getAttributeValue("enable");
				if (enable != null && enable.equalsIgnoreCase("false"))
					continue;

				name = node.getAttributeValue("name");
				javaClass = node.getAttributeValue("java-class");
				if (javaClass == null) {
					throw new DBTypeNotFoundException(name);
				}

				database = (DataBase) Class.forName(javaClass).newInstance();
				database.setName(name);

				children = node.getChildren();

				for (Element child : children) {
					if (child.getName().equalsIgnoreCase("driver")) {
						database.setDriver(child.getTextTrim());
					} else if (child.getName().equalsIgnoreCase("url")) {
						database.setUrl(child.getTextTrim());
					} else if (child.getName().equalsIgnoreCase("dbname")) {
						database.setDbName(child.getTextTrim());
					} else if (child.getName().equalsIgnoreCase("user")) {
						database.setUser(child.getTextTrim());
					} else if (child.getName().equalsIgnoreCase("password")) {
						database.setPassword(child.getTextTrim());
					} else if (child.getName().equalsIgnoreCase("isAutoCommit")) {
						database.setAutoCommit(child.getTextTrim().equalsIgnoreCase("true"));
					} else if (child.getName().equalsIgnoreCase("isReadOnly")) {
						database.setReadOnly(child.getTextTrim().equalsIgnoreCase("true"));
					} else if (child.getName().equalsIgnoreCase("reconnectTryCount")) {
						database.setReconnectRetry(Integer.parseInt(child.getTextTrim()));
					} else if (child.getName().equalsIgnoreCase("reconnectTimeout")) {
						database.setReconnectWaitTimeSec(Integer.parseInt(child.getTextTrim()));
					} else if (child.getName().equalsIgnoreCase("timeoutLogin")) {
						database.setTimeoutLogin(Integer.parseInt(child.getTextTrim()));
					} else if (child.getName().equalsIgnoreCase("countConnectionMax")) {
						database.setCountConnectionMax(Integer.parseInt(child.getTextTrim()));
					} else if (child.getName().equalsIgnoreCase("permitConnectionPoolOver")) {
						database.setPermitConnectionPoolOver("true".equalsIgnoreCase(child.getTextTrim()));
					} else if (child.getName().equalsIgnoreCase("secondsWaitPool")) {
						database.setSecondsWaitPool(Integer.parseInt(child.getTextTrim()));
					} else if (child.getName().equalsIgnoreCase("sqlSelectKeepAlive")) {
						database.setSqlSelectKeepAlive(child.getTextTrim().trim());
					} else if (child.getName().equalsIgnoreCase("const")) {
						try {
							database.addConst(child.getAttributeValue("id"), child.getTextTrim());
						} catch (Exception e) {
							Logger.logger.error(e);
						}
					} else {
						database.setProperty(child.getName(), child.getTextTrim());
					}

					//
				}

				database.check();

				addDataBase(database);

				sb.append(Logger.makeSubString(name, "ADDED"));

			}
		}

		Logger.logger.info(Logger.makeString("database", file.getName(), sb.toString()));

	}

	/**
	 * 
	 * @param databaseType
	 * @param para         아래 속성들이 포함 될 수 있습니다.<br>
	 *                     ipAddress<br>
	 *                     port<br>
	 *                     databaseName<br>
	 *                     sid<br>
	 *                     serviceName<br>
	 *                     user<br>
	 *                     password<br>
	 * @return 데이터베이스
	 * @throws Exception
	 */
	public DataBase createDataBase(Class<? extends DataBase> classOfDb, String ipAddress, int port,
			Map<String, Object> para) throws Exception {

		DataBase database = (DataBase) classOfDb.newInstance();
		database.setName(ipAddress + ":" + port);

		String url = database.makeUrl(ipAddress, port, para);
		database.setUrl(url);

		Object user = para.get("user");
		Object password = para.get("password");

		if (user != null && user.toString().trim().length() > 0)
			database.setUser(user.toString().trim());
		if (password != null && password.toString().trim().length() > 0)
			database.setPassword(password.toString().trim());

		return database;
	}

	/**
	 * 명칭의 데이터베이스를 제공합니다.
	 * 
	 * @param name 데이터베이스명
	 * @return 데이터 베이스
	 */
	public DataBase getDataBase(String name) {
		synchronized (dbMap) {
			return dbMap.get(name);
		}
	}

	// /**
	// *
	// * @param database
	// * 연결할 데이터베이스
	// * @return 연결 후 Connection
	// * @throws Exception
	// */
	// public Connection getConnection(DataBase database) throws TRExpO {
	// try {
	// Class.forName(database.getDriver()).newInstance();
	//
	// Connection c;
	//
	// if (database.getUser() != null && database.getUser().length() > 0) {
	// c = DriverManager.getConnection(database.getUrl(), database.getUser(),
	// database.getPassword());
	// } else {
	// c = DriverManager.getConnection(database.getUrl());
	// }
	//
	// c.setAutoCommit(database.isAutoCommit());
	// c.setReadOnly(database.isReadOnly());
	//
	// return c;
	// } catch (Exception ex) {
	// Logger.logger.error(ex);
	// throw new TRExpO(ERRNO.ERROR, ex.getMessage());
	// }
	// }

	/**
	 * 
	 * @return 등록된 데이터베이스 명칭 목록
	 */
	public String[] getDataBaseNames() {
		synchronized (dbMap) {
			return dbMap.keySet().toArray(new String[dbMap.size()]);
		}
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();

		if (dbMap == null) {
			sb.append("No Databases");
		} else {

			for (DataBase db : dbMap.values()) {
				sb.append(db.getDebug());
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public SqlBean getSqlBean(String qid, DataBase database) {

		SqlBean ret = null;

		if (sqlPool != null) {
			ret = sqlPool.getSqlBean(qid, database);
			if (ret != null)
				return ret;
		}

		if (sqlPoolCommon != null) {
			ret = sqlPoolCommon.getSqlBean(qid, database);
		}

		return ret;
	}

	private void init() throws Exception {
		File f = getDataBaseDefineFile();
		if (f.exists()) {
			addDataBase(f);
		}
	}

	public File getDataBaseDefineFile() {
		return new File(BasCfg.getHomeDeployConf("databases.xml"));
	}

	private QidPool initSqlPoolCommon() {

		QidPool ret = null;
		String filename = QueriesQid.QUERY_XML_FILE;

		File file = new File(BasCfg.getHome(filename));

		if (file.exists()) {
			Logger.logger.info("FILE-PATH(" + file.getPath() + ") EXIST");
			try {
				FileInputStream is = new FileInputStream(file);
				ret = new QidPool(null, is);
				is.close();
			} catch (Exception e1) {
				Logger.logger.error(e1);
			}
		} else {
			try {
				InputStream inputStream = new SqlPointer().getResource(filename);
				if (inputStream != null) {
					Logger.logger.info("URL-PATH(" + new SqlPointer().getUrl(filename) + ") EXIST");
					ret = new QidPool(null, inputStream);
				}
			} catch (Exception e1) {
				Logger.logger.error(e1);
			}
		}

		if (ret == null) {
			Logger.logger.info("FILE-PATH(" + file.getPath() + ") NOT FOUND");

		}

		return ret;
	}

	/**
	 * 등록된 데이터베이스 목록을 출력합니다.
	 */
	public void printDataBaseNames() {
		String names[] = getDataBaseNames();
		if (names == null)
			return;

		for (String name : names) {
			System.out.println(name);
		}
	}

	/**
	 * @param sqlPool the sqlPool to set
	 */
	public void setSqlPool(QidPool sqlPool) {
		this.sqlPool = sqlPool;
	}
}

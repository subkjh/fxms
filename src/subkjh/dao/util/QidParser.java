package subkjh.dao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.dao.CommDao;
import subkjh.dao.exp.ParsingException;
import subkjh.dao.exp.ResultMapNotFoundException;
import subkjh.dao.model.DaoResult;
import subkjh.dao.model.RetMappVo;
import subkjh.dao.model.SqlBean;
import subkjh.dao.model.SqlConst;
import subkjh.dao.model.SqlCreateBean;
import subkjh.dao.model.SqlDeleteBean;
import subkjh.dao.model.SqlElement;
import subkjh.dao.model.SqlInitBean;
import subkjh.dao.model.SqlInsertBean;
import subkjh.dao.model.SqlSelectBean;
import subkjh.dao.model.SqlUpdateBean;
import subkjh.dao.model.TestSqlElement;
import subkjh.dao.queries.SqlPointer;

/**
 * 쿼리가 있는 XML 화일을 읽어 SqlBean을 제공합니다.<br>
 * 
 * &lt;include filename=''/&gt;이 존재하면 해당 파일을 먼저 분석합니다.<br>
 * 
 * resultMap --> select | create | insert | delete | update | init 순으로 분석합니다.
 * <br>
 * 
 * 다음은 샘플입니다.<br>
 * &lt;?xml version="1.0" encoding="EUC-KR" ?&gt; <br>
 * &ltqueries&gt;<br>
 * &lt;resultMap id="RESULT_GWS_LAST_PERFTIME" javaClass="java.lang.Long"/&gt;
 * <br>
 * &lt;resultMap id="RESULT_GWS_PERF"
 * javaClass="com.daims.si.skb.egov.gw.bean.XmlGwsPerf"&gt;<br>
 * &lt;result attr="setPerfTime()" field="PERF_TIME" /&gt;<br>
 * &lt;result attr="setServiceType()" field="SERVICE_TYPE" /&gt;<br>
 * &lt;/resultMap&gt;<br>
 * &lt;delete id="DELETE_GWS_PERF"&gt;<br>
 * &lt;![CDATA[<br>
 * DELETE <br>
 * FROM <br>
 * GWS_PERF <br>
 * ]]&gt;<br>
 * &lt;sub id="DELETE_GWS_PERF_REMOVED"&gt;<br>
 * &lt;![CDATA[ <br>
 * WHERE IS_SEND = 'y'<br>
 * ]]&gt;<br>
 * &lt;/sub&gt;<br>
 * &lt;/delete&gt;<br>
 * 
 * &lt;select id="SELECT_GWS_LAST_PERFTIME"<br>
 * resultMap="RESULT_GWS_LAST_PERFTIME"&gt;<br>
 * &lt;![CDATA[ <br>
 * SELECT <br>
 * MAX(PERF_TIME)<br>
 * FROM<br>
 * GWS_PERF<br>
 * ]]&gt;<br>
 * <br>
 * &lt;/select&gt;<br>
 * &lt;/queries&gt;<br>
 * 
 * 
 * @author subkjh
 * 
 */
public class QidParser {

	class Data {
		InputStream inputStream;
		File file;
	}

	public static void main(String[] args) {

		QidParser parser = new QidParser();

		File file = new File(BasCfg.getHomeDeployConfSql() + File.separator + "test.xml");
		try {
			FileInputStream is = new FileInputStream(file);
			parser.parse(is, file);
			is.close();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("v1", 1);
//			map.put("v2", "CC");
			map.put("v2_1", "cc-2");
			map.put("v3", "aaa");

			for (SqlBean bean : parser.getQidList()) {
				System.out.println(
						bean.getQid() + "    >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				System.out.println(bean.getSql(map));
				System.out.println(Arrays.toString(bean.getPara(map)));
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/** key : id, value : 상수 */
	private Map<String, SqlConst> constMap;
	private Map<String, DaoResult> resultMap;
	/** 파싱할 데이터베이스 대상 */
	private String usedDatabase;
	private Map<String, SqlBean> qidMap;
	private List<String> fQidList = new ArrayList<String>();
	/** 상수로 파싱된 파일 목록 */
	private List<String> fConstList = new ArrayList<String>();

	private List<String> fResultList = new ArrayList<String>();

	public QidParser() {
		constMap = new HashMap<String, SqlConst>();
		resultMap = new HashMap<String, DaoResult>();
		usedDatabase = null;
		qidMap = new HashMap<String, SqlBean>();
	}

	/**
	 * SQL XML 파서
	 * 
	 * @param _logger   사용할 로거
	 * @param _constMap 상수맵
	 * @param database  데이터베이스
	 */
	public QidParser(Map<String, SqlConst> constMap, Map<String, DaoResult> resultMap, String database) {

		this.constMap = constMap != null ? constMap : new HashMap<String, SqlConst>();
		this.resultMap = resultMap != null ? resultMap : new HashMap<String, DaoResult>();
		this.usedDatabase = database;
		qidMap = new HashMap<String, SqlBean>();

	}

	public Map<String, SqlConst> getConstMap() {
		return constMap;
	}

	public List<String> getFileList() {
		return fQidList;
	}

	public List<SqlBean> getQidList() {

		List<SqlBean> sqlBeanList = new ArrayList<SqlBean>();
		for (SqlBean sql : qidMap.values()) {
			if (sql.getQid() == null || sql.getQid().trim().length() == 0) {
				Logger.logger.fail("Remove SqlBean  [" + sql.getQid() + "]");
				continue;
			}

			if (sql.isValid() == false) {
				Logger.logger.fail("Remove SqlBean [" + sql.getQid() + "]");
				continue;
			}

			sqlBeanList.add(sql);
		}

		Collections.sort(sqlBeanList, new Comparator<SqlBean>() {

			@Override
			public int compare(SqlBean o1, SqlBean o2) {
				return o1.getKey().compareTo(o2.getKey());
			}

		});

		return sqlBeanList;
	}

	public Map<String, DaoResult> getResultMap() {
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	public void parse(InputStream inputStream, File file) throws Exception {
		SAXBuilder builder = new SAXBuilder();

		Document document = builder.build(inputStream);

		if (document == null)
			return;

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();

		constParse(children, file);
		constCheck();
		resultParse(children, file);
		resultCheck();
		qidParse(children, file);
	}

	/**
	 * xmlfiles을 분석하여 SqlBean 목록을 제공합니다.
	 * 
	 * @param ignoreNotExist 파일이 없어도 무시할 지 여부
	 * @param xmlArr         파일목록
	 * @return 분석된 쿼리빈
	 * @throws Exception
	 */
	public void parse(String... xmlArr) throws Exception {

		if (xmlArr == null || xmlArr.length == 0)
			return;

		Logger.logger.debug("files={}", Arrays.toString(xmlArr));

		InputStream inputStream;
		for (String xmlfile : xmlArr) {
			inputStream = getInputStream(xmlfile, fConstList);
			if (inputStream != null) {
				parse(inputStream, new File(xmlfile));
			}
		}

	}

	public void setFileList(List<String> fileList) {
		for (String file : fileList) {
			if (fQidList.contains(file) == false)
				fQidList.add(file);
			if (fConstList.contains(file) == false)
				fConstList.add(file);
			if (fResultList.contains(file) == false)
				fResultList.add(file);
		}
	}

	/**
	 * 
	 * @param sqlBean
	 * @param queries
	 * @throws Exception
	 */
	private List<SqlBean> addSqlBean(SqlBean _sqlBean) {

		if (CommDao.isDefaultTrace)
			System.out.println(_sqlBean);

		SqlBean sqlBeanArray[];

		// 데이터베이스가 콤마로 구분하여 여러개에서 사용할 수 있다면
		// 예) "oracle,altibase,mysql"
		// 해당 갯수만큼 SqlBean 생성해야 합니다.
		String databases = _sqlBean.getDatabase();

		if (databases != null && databases.trim().length() > 0) {
			String databaseArray[] = databases.split(",");
			if (databaseArray.length == 1) {
				sqlBeanArray = new SqlBean[] { _sqlBean };
			} else {
				sqlBeanArray = new SqlBean[databaseArray.length];
				for (int i = 0; i < databaseArray.length; i++) {
					sqlBeanArray[i] = (SqlBean) _sqlBean.clone();
					sqlBeanArray[i].setDatabase(databaseArray[i].trim());
				}
			}
		} else {
			sqlBeanArray = new SqlBean[] { _sqlBean };
		}

		List<SqlBean> beanList = new ArrayList<SqlBean>();

		for (SqlBean sqlBean : sqlBeanArray) {
			if (containsDatabase(sqlBean.getDatabase())) {
				putSqlBean(sqlBean);
				beanList.add(sqlBean);
			}
		}

		return beanList;
	}

	/**
	 * 상수안의 상수를 치환합니다.
	 */
	private void constCheck() {
		String replacement;
		for (SqlConst sc : constMap.values()) {

			if (sc.text.indexOf('@') < 0)
				continue;

			for (SqlConst sc2 : constMap.values()) {
				if (sc.id.equals(sc2.id))
					continue;

				replacement = sc2.text.replaceAll("\\$", "\\\\\\$");
				sc.text = sc.text.replaceAll("@\\{" + sc2.id + "\\}", replacement);

			}
		}
	}

	@SuppressWarnings("unchecked")
	private void constParse(InputStream inputStream, File file) throws Exception {

		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = builder.build(inputStream);
		} catch (Exception e) {
			Logger.logger.fail("{}", file);
			throw e;
		}

		if (document == null)
			return;

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();

		constParse(children, file);
	}

	private void constParse(List<Element> children, File file) throws Exception {

		String id, text;
		SqlConst sqlConst, sqlConstOld;
		Data data;

		// 상수처리
		for (Element child : children) {

			if (child.getName().equals("include")) {
				data = getIncludeData(child, file, fConstList);
				if (data != null) {
					constParse(data.inputStream, data.file);
				}
			}

			else if (child.getName().equals("const")) {

				id = child.getAttributeValue("id");
				text = child.getTextTrim();
				if (id == null || id.length() == 0)
					continue;

				sqlConst = new SqlConst();
				sqlConst.id = id;
				sqlConst.database = child.getAttributeValue("database");
				sqlConst.text = text;

				if (containsDatabase(sqlConst.database)) {
					sqlConstOld = constMap.get(sqlConst.id);
					if (sqlConstOld != null) {
						if (sqlConstOld.text.equals(text) == false) {
							if (usedDatabase != null && usedDatabase.equalsIgnoreCase(sqlConst.database) == false) {
								Logger.logger.check("DUP CONST '{}'", id);
							}
						}
					}

					constMap.put(sqlConst.id, sqlConst);
					Logger.logger.trace(sqlConst.id);
				}
			}
		}

	}

	private boolean containsDatabase(String database) {

		if (usedDatabase == null)
			return true;

		if (database == null)
			return true;

		String ss[] = database.trim().split(",|;");

		for (String s : ss) {
			if (s.equalsIgnoreCase(usedDatabase))
				return true;
		}

		return false;
	}

	/**
	 * 입력된 SqlBean에서 Qid만 변경하여 복사합니다.
	 * 
	 * @param _sqlBean
	 * @param qid
	 * @param resultMapName
	 * @param resultBeanMap
	 * @return
	 * @throws Exception
	 */
	private SqlBean copy(SqlBean _sqlBean, String qid, String resultMapName) throws Exception {

		SqlBean sqlBeanNew = (SqlBean) _sqlBean.clone();
		sqlBeanNew.setQid(qid);

		if (sqlBeanNew instanceof SqlSelectBean) {
			if (resultMapName != null) {
				DaoResult map = resultMap.get(resultMapName);
				if (map == null) {
					throw new ResultMapNotFoundException(resultMapName);
				}
				((SqlSelectBean) sqlBeanNew).setResultMap(map);
			}

		}

		return sqlBeanNew;
	}

	/**
	 * include에 대한 화일을 가져옵니다.
	 * 
	 * @param child    include가 포함된 항목
	 * @param file     현재 화일
	 * @param fileList 처리한 화일 목록
	 * @return
	 * @throws Exception
	 */
	private Data getIncludeData(Element child, File file, List<String> fileList) throws Exception {

		String fileInc;

		String filename = child.getAttributeValue("filename");
		if (filename != null) {
			if (filename.charAt(0) == '/') {
				fileInc = BasCfg.getHomeDeployConfSql() + filename;
			} else if (file != null) {
				fileInc = file.getParent() + File.separator + filename;
			} else {
				return null;
			}

			InputStream inputStream = getInputStream(fileInc, fileList);
			if (inputStream == null)
				return null;

			Data data = new Data();
			data.inputStream = inputStream;
			data.file = new File(fileInc);
			return data;
		}

		return null;
	}

	private InputStream getInputStream(String filename, List<String> list) throws Exception {

		// 이미 처리할 화일이면 무시합니다.
		if (list.contains(filename)) {
			return null;
		}

		File file = new File(filename);

		if (file.exists()) {
			Logger.logger.trace("FILE : " + filename);
			list.add(filename);
			try {
				return new FileInputStream(filename);
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException(filename);
			}
		} else {

			InputStream inputStream = new SqlPointer().getResource(filename);
			if (inputStream != null) {
				list.add(filename);
				return inputStream;
			}

		}

		return null;
	}

	/**
	 * 노드가 유효한지 여부를 판단합니다.
	 * 
	 * @param element
	 * @return 유효하면 true
	 */
	private boolean isEnable(Element element) {
		String s = element.getAttributeValue("enable");
		return s == null || s.equalsIgnoreCase("true");
	}

	private List<SqlElement> parse(String database, Element e) throws Exception {
		SqlElement newElement;

		List<SqlElement> ret = new ArrayList<SqlElement>();

		for (Object o : e.getContent()) {
			if (o instanceof org.jdom.Element) {
				newElement = parseTest(database, (Element) o);
				if (newElement != null) {
					ret.add(newElement);
				}
			} else if (o instanceof org.jdom.CDATA) {
				ret.add(new SqlElement(((org.jdom.Text) o).getTextTrim()));
			} else if (o instanceof org.jdom.Comment) {
			} else if (o instanceof org.jdom.Text) {
				ret.add(new SqlElement(((org.jdom.Text) o).getTextTrim()));
			}
		}

		return ret;
	}

	private SqlElement parseTest(String parentDatebase, Element child) throws ParsingException, Exception {

		String database;
		String var;
		String equals;
		TestSqlElement te = null;

		var = child.getAttributeValue("var");
		equals = child.getAttributeValue("equals");

		if (child.getName().equals("test") && isEnable(child)) {

			database = child.getAttributeValue("database");
			if (database == null) {
				te = new TestSqlElement(var, equals, replaceConst(child.getText()));
			} else {
				if (parentDatebase == null) {
					te = new TestSqlElement(var, equals, replaceConst(child.getText()));
				} else {
					String ss[] = database.split(",|;");
					for (String s : ss) {
						if (s.equals(parentDatebase)) {
							te = new TestSqlElement(var, equals, replaceConst(child.getText()));
							break;
						}
					}
				}
			}

			if (te != null) {
				List<SqlElement> list = parse(parentDatebase, child);
				if (list != null) {
					te.getChildren().addAll(list);
				}
			}
		}

		return te;
	}

	/**
	 * 쿼리빈을 맵에 존재여부를 확인한 후 추가합니다.
	 * 
	 * @param sqlBean    쿼리빈
	 * @param sqlBeanMap 맵
	 * @param replace    대체여부
	 * @throws Exception 추가되지 않은 경우
	 */
	private void putSqlBean(SqlBean sqlBean) {

		SqlBean sqlBeanOld = qidMap.get(sqlBean.getKey());

		if (sqlBeanOld != null) {
			if (sqlBeanOld.equals(sqlBean)) {
				Logger.logger.check("Replace QID '{}'", sqlBean.getKey());
			}
		}

		qidMap.put(sqlBean.getKey(), sqlBean);
	}

	/**
	 * 화일을 분석하여 SqlBean 목록을 queries에 넣고 SqlSelectResultMap 내용을 results에 기록합니다.
	 * 
	 * @param inputStream
	 * @param queries
	 * @param results
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private boolean qidParse(InputStream inputStream, File file) throws ParsingException, Exception {

		SAXBuilder builder = new SAXBuilder();

		Document document;
		try {
			document = builder.build(inputStream);
		} catch (Exception e) {
			throw new ParsingException(e.getMessage());
		}

		if (document == null)
			return false;

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();

		return qidParse(children, file);

	}

	private boolean qidParse(List<Element> children, File file) throws Exception {

		boolean ret = true;
		SqlBean sqlBean;
		String resultMapName;
		String id;
		String source;
		String qid;
		String database;
		List<SqlBean> beanList;
		Data data;
		List<SqlElement> eList;

		for (Element child : children) {

			if (child.getName().equals("resultMap"))
				continue;
			if (child.getName().equals("include")) {
				data = getIncludeData(child, file, fQidList);
				if (data != null) {
					qidParse(data.inputStream, data.file);
				}
				continue;
			}

			sqlBean = null;
			qid = child.getAttributeValue("id");

			database = child.getAttributeValue("database");

			if (containsDatabase(database) == false)
				continue;

			try {
				if (child.getName().equals("insert") && isEnable(child)) {
					sqlBean = new SqlInsertBean(qid, database);
				} else if (child.getName().equals("update") && isEnable(child)) {
					sqlBean = new SqlUpdateBean(qid, database);
				} else if (child.getName().equals("delete") && isEnable(child)) {
					sqlBean = new SqlDeleteBean(qid, database);
				} else if (child.getName().equals("create") && isEnable(child)) {
					sqlBean = new SqlCreateBean(qid, database);
				} else if (child.getName().equals("init") && isEnable(child)) {
					sqlBean = new SqlInitBean(qid, database);
				} else if (child.getName().equals("select") && isEnable(child)) {
					
					resultMapName = child.getAttributeValue("resultMap");
										
					DaoResult map = resultMap.get(resultMapName);
					if (map == null) {
						throw new ResultMapNotFoundException(resultMapName);
					}
					sqlBean = new SqlSelectBean(qid, map, database);
				} else if (child.getName().equals("query") && isEnable(child)) {
					sqlBean = new SqlBean(qid, database);
				}
			} catch (Exception e) {
				Logger.logger.fail("qid[" + qid + "]");
				throw e;
			}

			if (sqlBean != null) {

				Element descrChild = child.getChild("descr");
				if (descrChild != null) {
					sqlBean.setDescr(descrChild.getTextTrim());
				}

				beanList = addSqlBean(sqlBean);

				for (SqlBean bean : beanList) {
					eList = parse(bean.getDatabase(), child);
					if (eList != null) {
						bean.getChildren().addAll(eList);
					}
				}

			}

		}

		SqlBean sqlBeanNew, sqlBeanOld;
		boolean isCopyed;

		for (Element child : children) {

			if (child.getName().equals("copy") == false)
				continue;

			id = child.getAttributeValue("id");
			source = child.getAttributeValue("source");
			isCopyed = false;

			sqlBeanOld = qidMap.get(source);
			if (sqlBeanOld == null) {
				for (String key : qidMap.keySet().toArray(new String[qidMap.size()])) {
					if (key.startsWith(source + "|")) {
						sqlBeanNew = copy(qidMap.get(key), id, child.getAttributeValue("resultMap"));
						qidMap.put(sqlBeanNew.getKey(), sqlBeanNew);
						isCopyed = true;
					}
				}
				if (isCopyed == false)
					throw new Exception("QID(" + source + ") NOT FOUND");
			} else {
				sqlBeanNew = copy(sqlBeanOld, id, child.getAttributeValue("resultMap"));
				qidMap.put(sqlBeanNew.getKey(), sqlBeanNew);
			}
		}

		return ret;
	}

	/**
	 * SQL문에서 상수 부분을 대치합니다.
	 * 
	 * @param sql
	 * @return
	 */
	private String replaceConst(String sql) {
		if (constMap.size() > 0) {
			String ret = sql;
			String replacement;

			for (SqlConst sc : constMap.values()) {
				replacement = sc.text;
				replacement = replacement.replaceAll("\\$", "\\\\\\$");
				ret = ret.replaceAll("@\\{" + sc.id + "\\}", replacement);
			}

			return ret;
		} else {
			return sql;
		}
	}

	private void resultCheck() throws Exception {
		for (DaoResult bean : resultMap.values()) {
			resultCheck(bean);
		}
	}

	private void resultCheck(DaoResult bean) throws Exception {

		if (bean.getIncludeIdList() == null || bean.getIncludeIdList().size() == 0)
			return;

		for (String incId : bean.getIncludeIdList()) {
			DaoResult sub = resultMap.get(incId);
			if (sub == null) {
				throw new Exception("INCLUDE-QID(" + incId + ") NOT FOUND");
			}

			resultCheck(sub);

			bean.add(sub.getFields());
		}
	}

	@SuppressWarnings("unchecked")
	private void resultParse(InputStream inputStream, File file) throws Exception {

		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build(inputStream);
		if (document == null)
			return;

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();

		resultParse(children, file);
	}

	/**
	 * ResultMap을 전역변수 resultMap에 추가합니다.
	 * 
	 * @param resultList 추가할 ResultMap Element
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void resultParse(List<Element> resultList, File file) throws Exception {

		List<Element> children2;
		DaoResult result;
		RetMappVo entry;
		String id;
		DaoResult resultBeanOld;
		Data data;

		// Result Map 먼저 처리
		for (Element child : resultList) {

			if (child.getName().equals("include")) {
				data = getIncludeData(child, file, fResultList);
				if (data != null) {
					resultParse(data.inputStream, data.file);
				}

			} else if (child.getName().equals("resultMap")) {

				result = new DaoResult(child.getAttributeValue("id"), child.getAttributeValue("javaClass"),
						child.getAttributeValue("keyCase"));

				children2 = child.getChildren();

				if (children2 != null) {
					for (Element e : children2) {

						if (e.getName().equals("result") && isEnable(e)) {
							entry = new RetMappVo(e.getAttributeValue("attr"), e.getAttributeValue("field"));
							result.add(entry);
						} else if (e.getName().equals("include") && isEnable(e)) {

							id = e.getAttributeValue("id");
							result.addIncludeId(id);

						}
					}
				}

				resultBeanOld = resultMap.get(result.rID);
				if (resultBeanOld != null) {
					if (resultBeanOld.equals(result) == false) {
						Logger.logger.check("Dup ResultMap {}", result.rID);
					}
				}

				resultMap.put(result.rID, result);

			}

		}
	}

}

package subkjh.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL 문을 정의하는 놈.
 * 
 * @author subkjh
 * 
 */
public class SqlBean implements Serializable, Cloneable {

	/** descr 태그를 추가할지 결정합니다. 기본값은 false */
	public static boolean isIncludeDescr = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3906207900900399966L;

	public static String addPrevString(String str, String prevStr) {
		if (str == null)
			return prevStr;
		String ss[] = str.split("\n");
		String ret = "";
		for (String s : ss) {
			ret += prevStr + s + "\n";
		}
		return ret;
	}

	public static void main(String[] args) {

		String sql = "		INSERT INTO IP_PERF_LANIF\n ("
				+ "			PERF_TIME , DEVICE_ID , IF_ID , INBPS , OUTBPS , "
				+ "			INPPS , OUTPPS , INEPS , INDPS , INUNIPS , OUTUNIPS ,"
				+ "			INNONUNIPS , OUTNONUNIPS , INBPS_USAGE, OUTBPS_USAGE, OUTEPS, OUTDPS" + "		) VALUES ("
				+ "		 	$perfTime, $deviceId, $ifId, $inbps, $outbps, -- 우리나라 \n"
				+ "			$inpps, $outpps, $ineps, $indps, $inunips, $outunips,"
				+ "			$innonunips, $outnonunips, $ipbpsUsage, $outbpsUsage, $outeps, $outdps" + "		)";

		sql = "INSERT INTO #AAA(#{Field1}_aa,#{Field2}aad,a,b,c,#d)values( $field1,$field2,#a,#b,#c,$d)";
		SqlBean bean;
		try {
			bean = new SqlInsertBean("TEST", sql);
			System.out.println("AAAA");
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("AAA", "TABLE");
			para.put("Field1", "e");
			para.put("Field2", "f");
			para.put("field1", "1");
			para.put("field2", "2");
			para.put("a", "3");
			para.put("b", "4");
			para.put("c", "5");
			para.put("d", "t");
			System.out.println(bean.getSql(para));
			System.out.println(bean.getSql(null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// "SELECT A, B, C FROM AAA WHERE name=$name AND kk=#kk AND kk=#ASD");

	}

	/** 하위 Query */
	private List<SqlElement> children;
	/** 어떤 종류의 데이터 베이스인지 */
	private String database;
	/** 쿼리에 대한 설명 */
	private String descr;
	/** 관련된 자바 클래스. 이 데이터는 MakeDao와 관련하여서만 사용합니다. */
	private String paraJavaClass;
	/** Query ID */
	private String qid;

	/**
	 * 
	 * @param qid      QID
	 * @param sql      쿼리
	 * @param database 사용되는 데이터베이스 종류 명<br>
	 *                 null이거나 공백이면 모든 데이터베이스에서 사용할 수 있다는 의미입니다.
	 * @throws Exception
	 */
	public SqlBean(String qid, String database) {
		this.qid = qid;
		this.database = database;
	}

	public List<SqlElement> getChildren() {
		if (children == null) {
			children = new ArrayList<SqlElement>();
		}

		return children;
	}

	public String getVars() {
		StringBuffer sb = new StringBuffer();
		for (SqlElement e : getChildren()) {
			sb.append(e.getVars());
		}
		return sb.toString();
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SqlBean) {
			SqlBean target = (SqlBean) obj;
			try {
				if (target.qid.equals(qid)) {
					if (database == null && target.database == null)
						return true;
					if (database != null && target.database != null)
						return database.equals(target.database);
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

	/**
	 * 객체를 이용하여 SQL문과 사용할 인수를 제공합니다.
	 * 
	 * @param c
	 * @return SQ 및 인수
	 */
	@SuppressWarnings("rawtypes")
	public BatchVo getBatchSql(Collection c) {
		BatchVo batch = null;
		for (Object o : c) {
			if (batch == null) {
				batch = new BatchVo(getSql(o));
			}
			batch.addValue(getPara(o));
		}
		return batch;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * 쿼리에 대한 설명을 제공합니다.
	 * 
	 * @since 2009-11-03
	 * @return 쿼리에 대한 설명<br>
	 *         null인 경우 공백으로 제공합니다.
	 */
	public String getDescr() {
		return descr == null ? "" : descr;
	}

	/**
	 * Hash Key로 사용되는 내용을 제공합니다.
	 * 
	 * @return KEY QID 또는 QID + 탭 + DATABASE
	 */
	public String getKey() {
		return database == null ? qid : qid + "|" + database;
	}

	@SuppressWarnings("rawtypes")
	public QueryResult getOneSql(Object obj) {

		if (obj == null) {

			StringBuffer sql = new StringBuffer();
			for (SqlElement e : getChildren()) {
				if ((e instanceof TestSqlElement) == false) {
					sql.append(e.getSql());
					sql.append(" ");
				}
			}
			return new QueryResult(sql.toString(), (Object[]) null);

		} else if (obj instanceof List) {
			return makeSQL(((List) obj).get(0));
		} else {
			return makeSQL(obj);
		}
	}

	/**
	 * 쿼리에 사용할 인수를 제공합니다.
	 * 
	 * @param obj 인수를 구할때 사용할 객체
	 * @return 인수배열
	 */
	public Object[] getPara(Object obj) {

		if (obj == null)
			return new Object[0];

		List<Object> list = new ArrayList<Object>();
		List<Object> entry;
		TestSqlElement te;

		for (SqlElement e : getChildren()) {
			if (e instanceof TestSqlElement) {
				te = (TestSqlElement) e;
				if (te.isAddable(obj)) {
					entry = te.getPara4Obj(obj);
					if (entry != null)
						list.addAll(entry);
				}
			} else {
				entry = e.getPara4Obj(obj);
				if (entry != null)
					list.addAll(entry);
			}
		}

		return list.toArray(new Object[list.size()]);
	}

	/**
	 * 이 메소드는 MakeDao에서만 유효합니다.
	 * 
	 * @return 파라메터로 오는 자바 클래스
	 */
	public String getParaJavaClass() {
		return paraJavaClass;
	}

	/**
	 * QID를 제공합니다.
	 * 
	 * @return QID
	 */
	public String getQid() {
		return qid;
	}

	public String getSql(Object obj) {

		StringBuffer sb = new StringBuffer();
		TestSqlElement te;

		for (SqlElement e : getChildren()) {

			if (e instanceof TestSqlElement) {
				te = (TestSqlElement) e;

				if (te.isAddable(obj)) {
					sb.append(te.getSql(obj));
					sb.append("\n");
				}
			} else {
				sb.append(e.getSql(obj).trim());
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public String getSqlOrg() {

		StringBuffer sb = new StringBuffer();

		for (SqlElement e : getChildren()) {
			sb.append(e.getSql().trim());
			sb.append("\n");
		}

		return sb.toString();
	}

	// /**
	// * $, #등 변수가 처리되고 ?로 된 상태의 SQL 문장. 단, 치환 관련 내용은 그래도 존재한다.
	// *
	// * @param o
	// * @return 쿼리문
	// */
	// public String getSql(Object o) {
	//
	// if (o == null) return getSql();
	//
	// SqlParaBean ret = makeSQL(o);
	//
	// return ret.getSql();
	//
	// }

	// /**
	// * replace 되는 변수를 제거한다.<br>
	// * 배치 작업에서 처음에 replace하고 나중에는 필요없기에 제거해야 한다.<br>
	// * 이때 이 클래스를 clone하여 사용해야 다른 스레드가 원래의 내용을 사용할 수 있음을 염두해 주자
	// */
	// public void removeReplaceVar() {
	// SqlVar var;
	//
	// for (int i = varList.size() - 1; i >= 0; i--) {
	// var = varList.get(i);
	// if (var.isReplace()) varList.remove(i);
	// }
	// }

	public String getXml() {

		String tag = getXmlTag();
		StringBuffer ret = new StringBuffer();
		TestSqlElement tb;

		ret.append("<" + tag + " id=\"" + getQid() + "\">\n");

		for (SqlElement e : getChildren()) {
			if (e instanceof TestSqlElement) {
				tb = (TestSqlElement) e;
				ret.append("\t<test var=\"" + tb.getVar() + "\"><![CDATA[ ");
				ret.append(tb.getSql());
				ret.append(" ]]></test>\n");
			} else {
				ret.append("\t<![CDATA[\n");
				ret.append(addPrevString(e.getSql(), "\t"));
				ret.append("\t]]>\n");
			}
		}

		if (isIncludeDescr) {
			ret.append("\t<descr>\n");
			ret.append("\t<![CDATA[\n");
			ret.append(addPrevString(getDescr(), "\t"));
			ret.append("\t]]>\n");
			ret.append("\t</descr>\n");
		}

		ret.append("</" + tag + ">\n");
		return ret.toString();
	}

	public boolean isValid() {
		return getChildren().size() > 0;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * 쿼리에 대한 설명을 설정합니다.
	 * 
	 * @since 2009-11-03
	 * @param descr the descr to set
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * 이 메소드는 MakeDao에서만 유효합니다.
	 * 
	 * @param paraJavaClass 설정할 인수 클래스
	 */
	public void setParaJavaClass(String paraJavaClass) {
		this.paraJavaClass = paraJavaClass;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "|" + qid //
				+ (database == null ? "" : "|" + database);
	}

	/**
	 * 쿼리가 어떤 형태인지를 나타냅니다.
	 * 
	 * @return 쿼리형태
	 */
	protected String getXmlTag() {
		return "query";
	}

	/**
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 * 
	 *         0번째 : SQL 1번째 : Para에 대한 OBJECT[]
	 */

	private QueryResult makeSQL(Object obj) {

		String retSql = getSql(obj);

		Object value[] = getPara(obj);

		return new QueryResult(retSql, value);
	}

}

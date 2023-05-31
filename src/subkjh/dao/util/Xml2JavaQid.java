package subkjh.dao.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;
import subkjh.dao.model.DaoResult;
import subkjh.dao.model.SqlBean;
import subkjh.dao.model.SqlElement;
import subkjh.dao.model.SqlInitBean;
import subkjh.dao.model.SqlSelectBean;
import subkjh.dao.model.TestSqlElement;

public class Xml2JavaQid {

	public Xml2JavaQid() {

	}

	/**
	 * 쿼리 XML 화일을 분석하여 DAO 자바 클래스를 생성에 사용되는 QID 목록을 출력합니다.
	 * 
	 * @param xmlFile  xml 화일
	 * @param daoClass DAO 클래스 명
	 * @param folder   폴더
	 */
	public File make(String xmlFile, String javaClassName, String folder) {

		Collection<SqlBean> list;
		try {
			QidParser parser = new QidParser();
			parser.parse(xmlFile);
			list = parser.getQidList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		List<SqlBean> sqlBeans = new ArrayList<SqlBean>();
		for (SqlBean bean : list)
			sqlBeans.add(bean);

		Collections.sort(sqlBeans, new Comparator<SqlBean>() {
			@Override
			public int compare(SqlBean o1, SqlBean o2) {
				return o1.getQid().compareTo(o2.getQid());
			}
		});

		String ret = "";

		DaoResult result;
		String remarks;
		SqlBean sqlBean;
		boolean isImport = false;

		for (int index = 0, size = sqlBeans.size(); index < size; index++) {

			sqlBean = sqlBeans.get(index);

			if (sqlBean instanceof SqlInitBean)
				continue;

			remarks = "";

			remarks = sqlBean.getVars();

			result = sqlBean instanceof SqlSelectBean ? ((SqlSelectBean) sqlBean).getResultMap() : null;

			ret += "/**\n";
			ret += "* para : " + remarks + "<br>\n";
			if (result != null)
				ret += "* result : " + result.rID + "=" + result.javaClass.getName() + "<br>\n";

			while (true) {
				ret += "* ---------------------------------------------------------------------------------- <br>\n";
				ret += "* database : " + sqlBean.getDatabase() + "<br>\n";
				ret += "* sql <br><br>\n ";

				ret += "* " + makeRemark(sqlBean.getSqlOrg().replaceAll("\n", "<br>")) + " <br>\n";

				if (sqlBeans.size() > index + 1 && sqlBeans.get(index + 1).getQid().equals(sqlBean.getQid())) {
					index++;
					sqlBean = sqlBeans.get(index);
				} else {
					break;
				}

			}

			ret += "*/\n";

			StringBuffer para = new StringBuffer();
			for (SqlElement e : sqlBean.getChildren()) {
				if (e instanceof TestSqlElement) {
					if (para.length() > 0)
						para.append(", ");
					para.append("\"" + ((TestSqlElement) e).getVar() + "\"");
				}
			}

			// if (para.length() > 0) {
			// ret += "@RetreivePara(para={" + para + "})\n";
			// isImport = true;
			// }

			ret += "public final String " + qid2JavaField(sqlBean.getQid()) + " = \"" + sqlBean.getQid() + "\";\n";
			ret += "\n";

		}

		String className = DaoUtil.getJavaClassSimpleName(javaClassName);

		// String filename = xmlFile.replaceFirst("deploy/conf/sql/", "");
		String filename = xmlFile;

		// 3. DAO 만들기
		String daoContents = "package " + DaoUtil.getJavaPackageName(javaClassName) + ";\n";

		if (isImport)
			daoContents += "import subkjh.def.RetreivePara;\n";

		daoContents += "\n";
		daoContents += "/**" + "\n";
		daoContents += "* File : " + filename + "<br>" + "\n";
		daoContents += "* @since " + Logger.getDate(0) + "\n";
		daoContents += "* @author subkjh " + "\n";
		daoContents += "*\n";
		daoContents += "*/" + "\n";
		daoContents += "\n";
		daoContents += "\n";

		daoContents += "public class " + className + " { \n\n";
		daoContents += "/** 쿼리 모임 화일명. " + filename + "*/\n";
		daoContents += "public static final String QUERY_XML_FILE = \"" + filename + "\";\n\n";

		daoContents += "public " + className + "() { \n";
		daoContents += "} \n";
		daoContents += ret;

		daoContents += "}";

		String filenameJava = folder + File.separator + className + ".java";

		FileUtil.writeToFile(filenameJava, daoContents, false);

		return new File(filenameJava);
	}

	private String makeRemark(String s) {
		String ret = s.replaceAll("/\\*", "'");
		return ret.replaceAll("\\*/", "'");
	}

	private String qid2JavaField(String qid) {
		String ret = qid.replaceAll("-", "_");
		return ret;
	}

	// private String makeJavaClassName(String s) {
	// String ss[] = s.split("_");
	// String className = "";
	// for (String _s : ss) {
	// className += toUpperFirst(_s);
	// }
	// return className;
	// }

	// private String toUpperFirst(String s) {
	// if (s != null && s.length() > 1) {
	// return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	// } else {
	// return s;
	// }
	// }

}

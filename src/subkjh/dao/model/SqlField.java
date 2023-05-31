package subkjh.dao.model;

import java.util.List;

public class SqlField {

	/** 필드 */
	SqlVar field;

	/** 쿼리 */
	String sqlXml;

	String sqlParsed;
	
	List<SqlVar> sqlVars;

	public SqlField(String field, String sql) throws Exception {

		if (field.indexOf("()") > 0) {
			this.field = new SqlVar(field.replaceFirst("\\(\\)", ""), SqlVar.VAR_TYPE_METHOD);
		}
		else {
			this.field = new SqlVar(field, SqlVar.VAR_TYPE_FIELD);
		}

		this.sqlXml = sql;

		SqlParser parser = new SqlParser(sql);
		parser.parse();
		sqlParsed = parser.getStrParsed();
		sqlVars = parser.getSqlVars();
	}
}

package subkjh.bas.dao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.define.COLUMN_OP;

/**
 * 데이터베이스 스키마를 이용하여 Java class를 내용을 제공하는 클래스입니다.
 * 
 * @since 2009-10-28
 * 
 * @author subkjh
 * 
 */
public class MakeJavaSource {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy.MM.dd HH:mm");

	MakeJavaSource() {

	}

	private String getColumn(Column col) {
		StringBuffer sb = new StringBuffer();

		// @FxColumn(name = "MO_ANAME", size = 100, nullable = true, comment =
		// "관리대상별칭")

		// sb.append("/**");
		// sb.append(" " + col.getName() + " " + col.getDataType() + "(" +
		// col.getDataLength()
		// + (col.getDataScale() > 0 ? "," + col.getDataScale() : "") + ") "
		// + (col.getDataDefault() == null ? "" : "default:" +
		// col.getDataDefault()) + " " + col.getComments());
		// sb.append("*/" + "\n");

		if (col.getSequence() != null && col.getSequence().isEmpty() == false) {
			sb.append("public static final String " + col.getSequence() + "  = \"" + col.getSequence() + "\"; \n");
		}

		sb.append("@FxColumn(");

		sb.append("name = \"" + col.getName() + "\"");

		sb.append(", size = " + col.getDataLength() + "");

		if (col.getOperator() != COLUMN_OP.all) {
			sb.append(", operator = COLUMN_OP." + col.getOperator() + "");
		}

		if (col.isNullable()) {
			sb.append(", nullable = " + col.isNullable() + "");
		}

		sb.append(", comment = \"" + col.getComments() + "\"");

		if (col.getDataDefault() != null && col.getDataDefault().isEmpty() == false) {
			sb.append(", defValue = \"" + col.getDataDefault() + "\"");
		}

		if (col.getSequence() != null && col.getSequence().isEmpty() == false) {
			sb.append(", sequence = \"" + col.getSequence() + "\"");
		}

		sb.append(")\n");

		if (col.getDataDefault() == null || col.getDataDefault().length() == 0) {
			sb.append("private " + col.getFieldType().getSimpleName() + " " + col.getFieldName() + ";" + "\n");
		} else {
			if (col.getFieldType() == boolean.class) {
				sb.append("private " + col.getFieldType().getSimpleName() + " " + col.getFieldName() + " = "
						+ col.getDataDefaultBoolean() + ";" + "\n");
			} else {
				sb.append("private " + col.getFieldType().getSimpleName() + " " + col.getFieldName() + " = "
						+ col.getDataDefaultUse() + ";" + "\n");
			}

		}
		sb.append("\n\n");

		return sb.toString();
	}

	private String getColumnMethod(Column col) {
		String ret = "";

		ret += "/**" + "\n";
		ret += "* " + col.getComments() + "\n";
		ret += "* @return " + col.getComments() + "\n";
		ret += "*/" + "\n";

		ret += "public " + col.getFieldType().getSimpleName() + " " + col.getGetter() + "() {" + "\n";
		ret += "return " + col.getFieldName() + ";" + "\n";
		ret += "}" + "\n";

		ret += "/**" + "\n";
		ret += "* " + col.getComments() + "\n";
		ret += "*@param " + col.getFieldName() + " " + col.getComments() + "\n";
		ret += "*/" + "\n";

		ret += "public void " + col.getSetter() + "(" + col.getFieldType().getSimpleName() + " " + col.getFieldName()
				+ ") {" + "\n";
		ret += "\tthis." + col.getFieldName() + " = " + col.getFieldName() + ";" + "\n";
		ret += "}" + "\n";

		return ret;
	}

	/**
	 * 눈으로 확인할 수 있는 시간 형식으로 넘긴다.
	 * 
	 * @return yyyyMMddHHmmss의 값
	 */
	String getDate() {
		return YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
	}

	private String getHeader(Table table) {
		// @FxTable(name = "FX_TEST_T1", comment = "관리대상테이블")
		// @FxIndex(name = "PK", type = INDEX_TYPE.PK, columns = { "moNo" })
		// @FxIndex(name = "UK", type = INDEX_TYPE.UK, columns = { "upperMoNo",
		// "moClass", "moName" })

		StringBuffer sb = new StringBuffer();

		sb.append("package " + table.getPackageName() + ";\n");

		sb.append("\n\n");
		sb.append("import subkjh.bas.dao.define.INDEX_TYPE;\n");
		sb.append("import subkjh.bas.fxdao.define.FxColumn;\n");
		sb.append("import subkjh.bas.fxdao.define.FxIndex;\n");
		sb.append("import subkjh.bas.fxdao.define.FxTable;\n");
		sb.append("import subkjh.bas.dao.define.COLUMN_OP;\n");
		sb.append("import java.io.Serializable;\n");

		sb.append("\n");
		sb.append("/**" + "\n");
		// sb.append("* Table : " + table.getName() + "<br>" + "\n");
		// sb.append("* Comment : " + table.getComment() + "<br>" + "\n");
		sb.append("* @since " + getDate() + "\n");
		sb.append("* @author subkjh " + "\n");
		sb.append("* autometic create by subkjh.dao " + "\n");
		sb.append("*\n");
		sb.append("*/" + "\n");
		sb.append("\n");
		sb.append("\n");

		sb.append("@FxTable(");
		sb.append("name = \"" + table.getName() + "\"");
		sb.append(", comment = \"" + table.getComment() + "\"");
		sb.append(")\n");

		for (Index idx : table.getIndexList()) {
			sb.append("@FxIndex(");
			sb.append("name = \"" + idx.getName() + "\"");
			sb.append(", type = INDEX_TYPE." + idx.getIndexType() + "");
			sb.append(", columns = {");
			String colArr[] = idx.getColArr();
			sb.append("\"" + colArr[0] + "\"");
			for (int i = 1; i < colArr.length; i++) {
				sb.append(", \"" + colArr[i] + "\"");

			}
			sb.append("}");

			if (idx.isFk()) {
				sb.append(", fkTable = \"" + idx.getFkTable() + "\"");
				sb.append(", fkColumn = \"" + idx.getFkColumn() + "\"");
			}

			sb.append(")\n");
		}

		sb.append("public class " + table.getClassSimpleName() + " implements Serializable {" + "\n");
		sb.append("\n");
		sb.append("public " + table.getClassSimpleName() + "() {\n }\n\n");

		return sb.toString();
	}

	public String make(Table table) {

		String ret = "";

		ret += getHeader(table);

		for (Column col : table.getColumnList()) {
			ret += getColumn(col);
		}

		for (Column col : table.getColumnList()) {
			ret += getColumnMethod(col);
		}

		ret += "}" + "\n";

		return ret;
	}

}

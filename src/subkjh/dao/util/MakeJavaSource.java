package subkjh.dao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import subkjh.dao.def.Column;
import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.Index;
import subkjh.dao.def.Table;

/**
 * 데이터베이스 스키마를 이용하여 Java class를 내용을 제공하는 클래스입니다.
 * 
 * @since 2009-10-28
 * 
 * @author subkjh
 * 
 */
public class MakeJavaSource extends MakeSourceBase {

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

		sb.append("    @FxColumn(name = \"").append(col.getName()).append("\"");
		sb.append(", size = ").append(col.getDataLength());

		if (col.getOperator() != COLUMN_OP.all) {
			sb.append(", operator = COLUMN_OP.").append(col.getOperator());
		}
		if (col.isNullable()) {
			sb.append(", nullable = ").append(col.isNullable());
		}

		sb.append(", comment = \"").append(col.getComments()).append("\"");

		if (col.getDataDefault() != null && col.getDataDefault().isEmpty() == false) {
			sb.append(", defValue = \"").append(col.getDataDefault()).append("\"");
		}

		if (col.getSequence() != null && col.getSequence().isEmpty() == false) {
			sb.append(", sequence = \"").append(col.getSequence()).append("\"");
		}

		sb.append(")\n");

		sb.append(getField("private", col));

		return sb.toString();
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

		int index;
		StringBuffer sb = new StringBuffer();

		sb.append("package " + table.getPackageName() + ";\n");

		sb.append("\n\n");
		sb.append("import subkjh.dao.def.Column.COLUMN_OP;\n");
		sb.append("import subkjh.dao.def.FxColumn;\n");
		sb.append("import subkjh.dao.def.FxIndex;\n");
		sb.append("import subkjh.dao.def.FxTable;\n");
		sb.append("import subkjh.dao.def.Index.INDEX_TYPE;\n");

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
			sb.append("name = \"" + idx.getIndexName() + "\"");
			sb.append(", type = INDEX_TYPE." + idx.getIndexType() + "");
			sb.append(", columns = {");

			index = 0;
			for (String col : idx.getColumnNames()) {
				if (index > 0)
					sb.append(",");
				sb.append("\"").append(col).append("\"");
				index++;
			}

			sb.append("}");

			if (idx.isFk()) {
				sb.append(", fkTable = \"" + idx.getFkTable() + "\"");
				sb.append(", fkColumn = \"" + idx.getFkColumn() + "\"");
			}

			sb.append(")\n");
		}

		// sb.append("public class " + table.getClassSimpleName() + " implements
		// Serializable {" + "\n");
		sb.append("public class " + table.getClassSimpleName() + "  {" + "\n");
		sb.append("\n");
		sb.append("public " + table.getClassSimpleName() + "() {\n }\n\n");

		return sb.toString();
	}

	public String make(Table table) {

		StringBuffer sb = new StringBuffer();

		sb.append(getHeader(table));

		for (Column col : table.getColumns()) {
			sb.append(getColumn(col));
		}

		for (Column col : table.getColumns()) {
			sb.append(getter(col));
			sb.append(setter(col));
		}

		sb.append("}\n");

		return sb.toString();
	}

}

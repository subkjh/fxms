package subkjh.dao.util;

import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.def.Column;
import subkjh.dao.def.Table;

public class MakeDtoSource extends MakeSourceBase {

//

	public String make(Table table) {

		StringBuffer sb = new StringBuffer();
		String className = table.getClassSimpleName() + "Dto";

		sb.append("package ").append(table.getPackageName()).append(";\n");
		sb.append("\n\n");
		sb.append("import java.io.Serializable;\n");
		sb.append("import fxms.bas.fxo.FxAttr;\n\n\n");
		sb.append("/**\n");
		sb.append("* @description  ").append(table.getComment()).append("\n");
		sb.append("* @author  fxms auto \n");
		sb.append("* @date ").append(DateUtil.getYmd()).append("\n");
		sb.append("*/\n\n");

		sb.append("public class ").append(className).append(" implements Serializable {\n\n");
		sb.append("    private static final long serialVersionUID = 1L;").append("\n\n");
		sb.append("    public ").append(className).append("() { }\n\n");

		for (Column col : table.getColumns()) {
			sb.append(getColumn(col));
		}

		for (Column col : table.getColumns()) {
			sb.append(getter(col));
			sb.append(setter(col));

		}

		sb.append("\n}\n");

		return sb.toString();

	}

	private String getColumn(Column col) {
		StringBuffer sb = new StringBuffer();

		sb.append("    @FxAttr(description =\"").append(col.getComments()).append("\"");

		if (col.isNullable())
			sb.append(", required = false");
		if (col.getExample() != null) {
			sb.append(", example = \"").append(col.getExample()).append("\"");
		} else if (col.hasDefaultValue()) {
			sb.append(", example = \"").append(col.getDataDefault()).append("\"");
		}

		sb.append(")\n");
		sb.append(getField("private", col));

		return sb.toString();
	}

	public String makeSwagger(Table table) {

		StringBuffer sb = new StringBuffer();
		String className = table.getClassSimpleName() + "Dto";

		sb.append("package ").append(table.getPackageName()).append(";\n");
		sb.append("\n\n");
		sb.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
		sb.append("import lombok.Getter;\n");
		sb.append("import lombok.Setter;\n");

		sb.append("/**\n");
		sb.append("* @description  ").append(table.getComment()).append("\n");
		sb.append("* @author  fxms auto \n");
		sb.append("* @date ").append(DateUtil.getYmd()).append("\n");
		sb.append("*/\n\n");

		sb.append("@Schema(description = \"데이터 유형 객체 설명\")\n");
		sb.append("@Setter\n");
		sb.append("@Getter\n");
		sb.append("public class ").append(className).append("  {\n\n");
		sb.append("    public ").append(className).append("() { }\n\n");

		for (Column col : table.getColumns()) {
			sb.append(getColumnSwagger(col));
		}

		sb.append("\n}\n");

		return sb.toString();

	}

	private String getColumnSwagger(Column col) {
		StringBuffer sb = new StringBuffer();

		sb.append("    @Schema(description =\"").append(col.getComments()).append("\"");

		if (col.isNullable() == false)
			sb.append(", requiredMode = Schema.RequiredMode.REQUIRED");
		if (col.getExample() != null) {
			sb.append(", example = \"").append(col.getExample()).append("\"");
		} else if (col.hasDefaultValue()) {
			sb.append(", example = \"").append(col.getDataDefault()).append("\"");
		}

		sb.append(")\n");
		sb.append(getField("public", col));

		return sb.toString();
	}

	public void printParameters(Table table) {
		System.out.println(table.getComment());
		for (Column col : table.getColumns()) {
			StringBuffer sb = new StringBuffer();
			sb.append(col.getFieldName()).append("\t");
			sb.append(col.getFieldType().getSimpleName()).append("\t");
			if (col.isNullable()) {
				sb.append("선택\t");
			} else {
				sb.append("필수\t");
			}
			sb.append(col.getComments());
			System.out.println(sb.toString());
		}
		System.out.println();
	}

}

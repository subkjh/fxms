package subkjh.dao.util;

import subkjh.dao.def.Column;

public class MakeSourceBase {

	protected String getter(Column col) {
		StringBuffer sb = new StringBuffer();

		sb.append("/**\n");
//		sb.append(" * ").append(col.getComments()).append("\n");
		sb.append(" * @return ").append(col.getComments()).append("\n");
		sb.append("*/\n");

		sb.append("public ").append(col.getFieldType().getSimpleName()).append(" ").append(col.getGetter())
				.append("() { \n");
		sb.append("    return ").append(col.getFieldName()).append(";\n");
		sb.append("}\n");

		return sb.toString();
	}

	protected String setter(Column col) {
		StringBuffer sb = new StringBuffer();

		sb.append("/**\n");
//		sb.append(" * ").append(col.getComments()).append("\n");
		sb.append(" * @param ").append(col.getFieldName()).append(" ").append(col.getComments()).append("\n");
		sb.append("*/\n");

		sb.append("public void ").append(col.getSetter()).append("(").append(col.getFieldType().getSimpleName())
				.append(" ").append(col.getFieldName()).append(") { \n");
		sb.append("    this.").append(col.getFieldName()).append(" = ").append(col.getFieldName()).append(";\n");
		sb.append("}\n");

		return sb.toString();

	}

	protected String getField(String accessRestrictor, Column col) {
		StringBuffer sb = new StringBuffer();

		sb.append("    ").append(accessRestrictor).append(" ").append(col.getFieldType().getSimpleName()).append(" ");

		if (col.getDataDefault() == null || col.getDataDefault().length() == 0) {
			sb.append(col.getFieldName()).append(";\n");
		} else {

			if (col.getFieldType() == String.class) {
				// 문자열이면 기본값에 큰따움표(")로 묶는다.
				sb.append(col.getFieldName()).append(" = \"").append(col.getDataDefaultUse()).append("\";\n");

			} else if (col.getFieldType() == boolean.class) {
				// 논리값인 경우 true, false로 설정한다.
				sb.append(col.getFieldName()).append(" = ").append(col.getDataDefaultBoolean()).append(";\n");
			} else {
				// 기타인 경우 그대로 넣는다.
				sb.append(col.getFieldName()).append(" = ").append(col.getDataDefaultUse()).append(";\n");
			}
		}

		sb.append("\n");

		return sb.toString();
	}
}

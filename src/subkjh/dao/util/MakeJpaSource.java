package subkjh.dao.util;

import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.def.Column;
import subkjh.dao.def.Table;

public class MakeJpaSource extends MakeSourceBase {

//

	public String make(Table table) {

		StringBuffer sb = new StringBuffer();

		sb.append("package " + table.getPackageName() + ";\n");

		sb.append("\n\n");
		sb.append("import java.io.Serializable;\n");
		sb.append("import javax.persistence.Column;\n");
		sb.append("import javax.persistence.Entity;\n");
		sb.append("import javax.persistence.Id;\n");
		sb.append("import javax.persistence.Table;\n");
		sb.append("import javax.persistence.Transient;\n");
		sb.append("import io.swagger.annotations.ApiModel;\n");
		sb.append("import io.swagger.annotations.ApiModelProperty;\n");
		sb.append("import lombok.Data;\n");

		sb.append("\n\n");

		sb.append("/**\n");
		sb.append("* @description  ").append(table.getComment()).append("\n");
		sb.append("* @author  fxms auto \n");
		sb.append("* @date ").append(DateUtil.getYmd()).append("\n");
		sb.append("*/\n\n");

		sb.append("@Entity").append("\n");
		sb.append("@Data").append("\n");
		sb.append("@Table(name = \"").append(table.getName()).append("\")").append("\n");
		sb.append("@ApiModel(\"").append(table.getComment()).append("\")").append("\n\n");

		sb.append("public class " + table.getClassSimpleName() + " implements Serializable {" + "\n");
		sb.append("\n");
		sb.append("private static final long serialVersionUID = 1L;").append("\n\n");
		sb.append("public " + table.getClassSimpleName() + "() {\n}\n\n");

		for (Column col : table.getColumns()) {
			sb.append(getColumn(col));
		}

		sb.append("\n}\n");

		return sb.toString();

	}

	private String getColumn(Column col) {

		StringBuffer sb = new StringBuffer();

		if (col.isPk()) {
			sb.append("@Id\n");
		}

		sb.append("@ApiModelProperty(\"").append(col.getComments()).append("\")").append("\n");
		sb.append("@Column(name = \"").append(col.getName()).append("\")").append("\n");

		sb.append(getField("private", col));

		return sb.toString();
	}

}

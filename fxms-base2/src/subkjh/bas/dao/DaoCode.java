package subkjh.bas.dao;

public class DaoCode {

	public static final String RET_LOG = "\n--- query ---\n {} \n--- parameters --- \n {} \n--- result ---\nret({}) ms({})";
	public static final String ERR_LOG = "\n--- query ---\n {} \n--- parameters --- \n {} \n--- result ---\n{}";

	public static String getTraceLog(String qid, String sql, String para, int size, long ptime) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n--- query ---\n");
		if (qid != null) {
			sb.append("qid=");
			sb.append(qid);
			sb.append("\n");
		}
		sb.append(" ");
		sb.append(sql);
		sb.append("\n--- parameters ---\n");
		sb.append(" ");
		sb.append(para);
		sb.append("\n--- result ---\n");
		sb.append(" ret(");
		sb.append(size);
		sb.append(") ms(");
		sb.append(System.currentTimeMillis() - ptime);
		sb.append(")");
		return sb.toString();
	}

	public static String getDebugLog(String qid, String para, int size, long ptime) {
		StringBuffer sb = new StringBuffer();
		if (qid != null) {
			sb.append("QID(");
			sb.append(qid);
			sb.append(")");
		}
		if (para != null) {
			sb.append("PARA(");
			sb.append(para);
			sb.append(")");
		}
		sb.append("SIZE(");
		sb.append(size);
		sb.append(")");
		sb.append("MS(");
		sb.append(System.currentTimeMillis() - ptime);
		sb.append(")");
		return sb.toString();
	}

	
}

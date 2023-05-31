package subkjh.dao.database;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.dao.def.Column;
import subkjh.dao.def.Sequence;
import subkjh.dao.exp.DBObjectDupException;
import subkjh.dao.exp.TableNotFoundException;

public class Altibase extends DataBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4059091855215728733L;

	public static final int PORT = 20300;

	public static void main(String[] args) {
		Altibase a = new Altibase();
		// List<String> s =
		// a.splitUrl("jdbc:Altibase://167.1.21.31:20300/mydb");
		// List<String> s =
		// a.splitUrl("jdbc:oracle:thin:@167.1.21.37:1521:orcl");
		List<String> s = a.splitUrl(
				"jdbc:mysql://167.1.21.31/nprism?useUnicode=true&amp;user=nprism&amp;password=nprism03&amp;characterEncoding=utf8");
		System.out.println(s);
	}

	public Altibase() {
		setDriver("Altibase.jdbc.driver.AltibaseDriver");
		setPort(PORT);

		addConst(CONST_NVL, "nvl");
		addConst(CONST_TRUNC, "trunc");
	}

	@Override
	public void setUrl(String url) {
		super.setUrl(url);
		List<String> ret = splitUrl(url);
		if (ret != null && ret.size() > 2) {
			setIpAddress(ret.get(2));
		}

		if (ret != null && ret.size() > 3) {
			try {
				int port = Integer.parseInt(ret.get(3));
				setPort(port);
			} catch (Exception e) {
			}
		}

		if (ret != null && ret.size() > 4) {
			setDbName(ret.get(4));
		}
	}

	@Override
	public String getSqlCreate(Sequence seq) {
		return "create sequence " + makeColSize(seq.getSequenceName(), 32) + "start with " + seq.getValueMin()
				+ " increment by " + seq.getIncBy() + " maxvalue " + seq.getValueMax() + " minvalue "
				+ seq.getValueMin() + (seq.isCycle() ? " cycle  " : "") + " cache 20";
	}

	@Override
	public String makeUrl(Map<String, Object> para) {

		if (para != null) {
			Object databaseName = para.get("databaseName");
			if (databaseName != null) {
				setDbName(databaseName + "");
			}
		}

		return "jdbc:Altibase://" + getIpAddress() + ":" + getPort() + "/" + getDbName();
	}

	@Override
	public Exception makeException(Exception e, String _msg) {

		String msg = _msg == null ? "" : _msg;
		msg += "EXCEPTION=[" + e.getMessage() + "]";

		Logger.logger.error(e);
		Logger.logger.fail((e != null ? e.getClass().getName() + " : " : "") + msg);

		try {
			String errmsg = e.getMessage();
			if (e instanceof IOException) {
				return e;
			} else if (errmsg != null) {
				errmsg = errmsg.toLowerCase();
				if (errmsg.indexOf("communication") >= 0 //
						|| errmsg.indexOf("socket") >= 0) {
					if (errmsg.indexOf("failure") >= 0 //
							|| errmsg.indexOf("close") >= 0 //
							|| errmsg.indexOf("error") >= 0) {
						return new IOException(e.getMessage());
					}
				} else if (errmsg.indexOf("name is already") >= 0) {
					return new DBObjectDupException(e.getMessage());
				} else if (errmsg.indexOf("duplicate") >= 0) {
					return new DBObjectDupException(e.getMessage());
				} else if (errmsg.indexOf("already exists") >= 0) {
					return new DBObjectDupException(e.getMessage());
				} else if (errmsg.indexOf("table not found") >= 0) {
					return new TableNotFoundException(e.getMessage());
				}
			}
		} catch (Exception ex1) {
		}

		return e;
	}

	@Override
	public String getSqlAdd(Column column, String tableName) {
		return "alter table " + tableName + " add column ( " + getSqlCreate(column) + ")";
	}

	@Override
	public String getSqlDelete(Column column, String tableName) {
		return "alter table " + tableName + " drop column " + column.getName();
	}

	@Override
	public String getSqlUpdate(Column column, String tableName) {

		Column colNew = (Column) column.clone();
		colNew.setName(colNew.getName() + "_BAK");

		String sql1 = "alter table " + tableName + " add column ( " + getSqlCreate(colNew) + ")";
		String sql2 = "update " + tableName + " set " + colNew.getName() + " = " + column.getName();
		String sql3 = "alter table " + tableName + " drop column " + column.getName();
		String sql4 = "alter table " + tableName + " add column ( " + getSqlCreate(column) + ")";
		String sql5 = "update " + tableName + " set " + column.getName() + " = " + colNew.getName();
		String sql6 = "alter table " + tableName + " drop column " + colNew.getName();

		return sql1 + ";\n" //
				+ sql2 + ";\n" //
				+ sql3 + ";\n" //
				+ sql4 + ";\n" //
				+ sql5 + ";\n" //
				+ sql6 + ";\n";

		// return "alter table " + tableName + " modify column ( " +
		// getSqlCreate(column) + ")";
	}
}

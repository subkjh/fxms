package subkjh.dao.database;

import java.util.Map;

/**
 * GRANT ALL PRIVILEGES ON nprismdb.* TO nprism@localhost IDENTIFIED BY
 * 'nprism03!@' WITH GRANT OPTION;<br>
 * <br>
 * 
 * CREATE DATABASE nprismdb DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;<br>
 * 
 * @author subkjh
 * 
 */

public class MariaDb extends MySql {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9125952088647423314L;

	public static final int PORT = 3306;

	public MariaDb() {

		setDriver("org.mariadb.jdbc.Driver");
		setPort(PORT);

		addConst(CONST_NVL, "ifnull");
		addConst(CONST_TRUNC, "truncate");
	}

	@Override
	public String makeUrl(Map<String, Object> para) {
		return "jdbc:mariadb://" + makeUri(para);
	}

}
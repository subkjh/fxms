package subkjh.dao.model;

public class SqlConst {

	/** ID */
	public String id;
	/** 사용하는 데이터베이스 */
	public String database;
	/** 상수값 */
	public String text;

	@Override
	public String toString() {
		return id + "|" + database + "|" + text;
	}
}

package subkjh.dao.database;

import java.util.Map;

public class InfluxDB extends DataBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8252229097793088227L;

	@Override
	public Exception makeException(Exception e, String msg) {
		return null;
	}

	@Override
	public String makeUrl(Map<String, Object> para) {
		return null;
	}

	/**
	 * InfluxDB에 맞는 함수를 
	 * @param func
	 * @return
	 */
	public static String toFunction(String func) {
		if (func.equalsIgnoreCase("avg")) {
			return "MEAN";
		}
		return func;
	}
}

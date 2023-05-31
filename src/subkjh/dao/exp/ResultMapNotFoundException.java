package subkjh.dao.exp;

public class ResultMapNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1307841584140630265L;

	public ResultMapNotFoundException(String name) {
		super("RESULT-MAP(" + name + ") NOT FOUND");
	}
}

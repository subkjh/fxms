package subkjh.dao.exp;

public class QidNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2868331450946263272L;

	public QidNotFoundException(String qid) {
		super("QID(" + qid + ") NOT FOUND");
	}
}

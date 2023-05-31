package fxms.bas.exp;

/**
 * 자식이 존재하여 처리하지 못함을 통보함
 * 
 * @author subkjh
 *
 */
public class ExistChildException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3043366057517500633L;

	private String childType;
	private long childCount;

	public ExistChildException(String childType, long childCount, String msg) {
		super(msg == null ? msg = childType + ":" + childCount : msg);
		this.childType = childType;
		this.childCount = childCount;
	}

	public long getChildCount() {
		return childCount;
	}

	public String getChildType() {
		return childType;
	}

}

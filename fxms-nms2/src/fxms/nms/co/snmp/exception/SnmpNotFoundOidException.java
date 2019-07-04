package fxms.nms.co.snmp.exception;

/**
 * 
 * @author subkjh
 *
 */
public class SnmpNotFoundOidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1198744606168499944L;
	
	private String oid;

	public SnmpNotFoundOidException(String oid) {
		this.oid = oid;
	}

	public String getOid ( ) {
		return oid;
	}

	public void setOid ( String oid ) {
		this.oid = oid;
	}

}

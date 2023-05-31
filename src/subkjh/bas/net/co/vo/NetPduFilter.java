package subkjh.bas.net.co.vo;

/**
 * 
 * @author subkjh
 * 
 * @param <PDU>
 */
public interface NetPduFilter<PDU extends NetPdu> {

	/**
	 * 
	 * @param pdu
	 * @return
	 */
	public PDU filter(PDU pdu);

}

package subkjh.bas.net.co.vo;

/**
 * 바이트 배열을 이용하여 분석된 객체
 * 
 * @author subkjh
 * 
 * @param <PDU>
 *            분석된 PDU
 */
public class ByteParsedBean<PDU extends NetPdu> {

	/** PDU */
	private PDU pdu;

	/** 남은 바이트배열 */
	private byte bytes[];

	/**
	 * 
	 */
	public ByteParsedBean() {

	}

	/**
	 * 
	 * @param pdu
	 *            생성된 PDU
	 * @param bytes
	 *            남은 바이트배열
	 */
	public ByteParsedBean(PDU pdu, byte bytes[]) {
		this.pdu = pdu;
		this.bytes = bytes;
	}

	/**
	 * 
	 * @return 남은 바이트배열
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * 생성된 PDU<br>
	 * null인 경우 바이트배열이 부족할 경우 임.
	 * 
	 * @return 생성된 PDU
	 */
	public PDU getPdu() {
		return pdu;
	}

	/**
	 * 
	 * @return 바이트배열 존재 여부
	 */
	public boolean isBytes() {
		return bytes != null && bytes.length > 0;
	}

	/**
	 * 
	 * @param bytes
	 *            남은 바이트배열
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * 
	 * @param pdu
	 *            생성된 PDU
	 */
	public void setPdu(PDU pdu) {
		this.pdu = pdu;
	}
}

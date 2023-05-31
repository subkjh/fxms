package subkjh.bas.net.co.vo;

/**
 * 바이트배열을 데이터로 하는 PDU
 * 
 * @author subkjh
 * 
 */
public class NetPduByte extends NetPdu {

	private byte data[];

	@Override
	public byte[] getBytes() {
		return data;
	}

	/**
	 * 
	 * @return 바이트배열
	 */
	public byte[] getData() {
		return data;
	}

	@Override
	public String getString() {
		if (data == null) return "";
		return "size=" + data.length + "|" + new String(data) + "|";
	}

	/**
	 * 
	 * @param data
	 *            바이트배열
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return super.toString() + " | " + (data == null ? "null" : data.length + "");
	}

}

package subkjh.bas.net.co.vo;

/**
 * 소켓으로부터 받은 바이트배열
 * 
 * @author subkjh
 * 
 */
public class RecvBytes {

	/** 어디서 왔는지를 나타내는 키 */
	private Object key;

	/** 받은 바이트배열 */
	private byte bytes[];

	public byte[] getBytes() {		
		return bytes;
	}

	public Object getKey() {
		return key;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return NetPdu.getString4Key(key) + "|len=" + (bytes == null ? 0 : bytes.length);
	}

}

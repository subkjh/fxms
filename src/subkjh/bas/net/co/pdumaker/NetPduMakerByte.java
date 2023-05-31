package subkjh.bas.net.co.pdumaker;

import subkjh.bas.co.log.Logger;
import subkjh.bas.net.co.vo.ByteParsedBean;
import subkjh.bas.net.co.vo.NetPduByte;

/**
 * 바이트배열로 PDU를 만듭니다.
 * 
 * @author subkjh
 * 
 * @param <KEY>
 */
public class NetPduMakerByte extends NetPduMaker<NetPduByte> {

	private int sizePacking = 0;

	/**
	 * 
	 * @param name
	 *            명칭
	 * @param logger
	 *            사용할 로거
	 */
	public NetPduMakerByte(String name, Logger logger) {
		super(name, logger);
	}

	/**
	 * 
	 * @return PDU를 구성하는 바이트배열 크기
	 */
	public int getSizePacking() {
		return sizePacking;
	}

	/**
	 * PDU를 구성하는 바이트의 크기를 지정합니다.
	 * 
	 * @param sizePacking
	 *            PDU를 구성하는 바이트배열 크기
	 * 
	 */
	public void setSizePacking(int sizePacking) {
		this.sizePacking = sizePacking;
	}

	@Override
	protected ByteParsedBean<NetPduByte> makePdu(byte[] bytes) throws Exception {

		if (sizePacking > 0) {
			if (bytes.length < sizePacking)
				return new ByteParsedBean<NetPduByte>(null, bytes);
			NetPduByte netPduByte = new NetPduByte();
			byte data[] = new byte[sizePacking];
			byte ren[] = new byte[bytes.length - sizePacking];

			System.arraycopy(bytes, 0, data, 0, data.length);
			System.arraycopy(bytes, data.length, ren, 0, ren.length);

			netPduByte.setData(data);
			ByteParsedBean<NetPduByte> bean = new ByteParsedBean<NetPduByte>(netPduByte, ren);
			return bean;

		} else {
			NetPduByte netPduByte = new NetPduByte();
			netPduByte.setData(bytes);
			ByteParsedBean<NetPduByte> bean = new ByteParsedBean<NetPduByte>(netPduByte, null);
			return bean;
		}
	}

}

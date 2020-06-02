package fxms.nms.co.tl1;

import fxms.nms.co.tl1.vo.AM;
import fxms.nms.co.tl1.vo.ORMF;
import fxms.nms.co.tl1.vo.ORMF_HEADER;
import subkjh.bas.co.log.Logger;
import subkjh.bas.net.co.pdumaker.NetPduMaker;
import subkjh.bas.net.co.vo.ByteParsedBean;

public abstract class NetPduMakerTL1 extends NetPduMaker<NetPduTL1> {

	private final byte LF = 0x0a;
	@SuppressWarnings("unused")
	private final byte CR = 0x0d;
	/** ; */
	private final byte END = ';';
	/** &lt */
	private final byte END_ACK = '<';
	/** &gt */
	@SuppressWarnings("unused")
	private final byte CONTINUE = '>';
	private String charset = "utf-8";

	// public static void main(String[] args) throws Exception {
	// String s = FileUtil.getString(new File("datas/tl1data.txt"));
	// NetPduMakerTL1 maker = new NetPduMakerTL1("nnn", Logger.logger);
	// ByteParsedBean<NetPduTL1> bean = maker.makePdu(s.getBytes());
	// System.out.println(bean.getPdu() + ", " + bean.getBytes().length);
	// while (bean.getBytes() != null && bean.getBytes().length > 0) {
	// bean = maker.makePdu(bean.getBytes());
	// System.out.println(bean.getPdu() + ", " + bean.getBytes().length);
	// Thread.sleep(1000);
	// }
	//
	// }

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public NetPduMakerTL1(String name, Logger logger) {
		super(name, logger);
	}

	/**
	 * 문자열을 이용하여 TL1 PDU를 생성한다.<br>
	 * 
	 * @param s
	 *            ';'까지 받은 문자열
	 * @return
	 * @throws Exception
	 */
	public abstract NetPduTL1 makePdu(String s) throws Exception;

	/**
	 * 
	 * @param index
	 * @param line
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected NetPduTL1 makePdu(int index, String line[], String s) throws Exception {

		ORMF_HEADER header = new ORMF_HEADER(line[index]);

		if (line[index + 1].trim().toCharArray()[0] == 'M') {
			ORMF pdu = new ORMF(s, index);
			pdu.setHeader(header);
			return pdu;
		} else {
			return new AM(header, s);
		}
	}

	@Override
	protected ByteParsedBean<NetPduTL1> makePdu(byte[] bytes) throws Exception {

		int index = getIndexEnd(bytes);

		if (index > 0) {

			if (getLogger().isTrace()) {
				getLogger().trace("[" + new String(bytes, 0, index + 1) + "]");
			}

			NetPduTL1 pdu = makePdu(new String(bytes, 0, index + 1, charset));
			if (pdu != null) {
				byte ren[] = new byte[bytes.length - (index + 1)];
				System.arraycopy(bytes, (index + 1), ren, 0, ren.length);
				return new ByteParsedBean<NetPduTL1>(pdu, ren);
			} else {
				return new ByteParsedBean<NetPduTL1>(null, bytes);
			}

		} else {
			return new ByteParsedBean<NetPduTL1>(null, bytes);
		}
	}

	/**
	 * 바이트배열에서 Terminator 인덱스를 찾습니다.
	 * 
	 * @param bytes
	 * @return
	 */
	private int getIndexEnd(byte bytes[]) {

		for (int index = 3; index < bytes.length; index++) {
			if (bytes[index - 1] == LF && (bytes[index] == END)) {
				if (index + 1 < bytes.length && bytes[index + 1] == '\n')
					return index + 1;
				return index;
			}
		}

		for (int index = 3; index < bytes.length; index++) {
			if (bytes[index - 1] == LF && bytes[index] == END_ACK) {
				if (index + 1 < bytes.length && bytes[index + 1] == '\n')
					return index + 1;
				return index;
			}
		}

		return -1;
	}

}

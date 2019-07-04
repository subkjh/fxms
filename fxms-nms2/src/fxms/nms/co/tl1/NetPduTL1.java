package fxms.nms.co.tl1;

import subkjh.bas.net.co.vo.NetPdu;
import fxms.nms.co.tl1.vo.ACK;
import fxms.nms.co.tl1.vo.AM;
import fxms.nms.co.tl1.vo.ORMF;
import fxms.nms.co.tl1.vo.ORMF_HEADER;
import fxms.nms.co.tl1.vo.TL1KeepAlive;

public class NetPduTL1 extends NetPdu {

	public enum MessageType {

		/** OS(운용터미널)에서 NE로 요청 */
		InputCommand

		/** Input Command의 수행 상태 전달 */
		, Acknowledgment

		/** Input Command의 결과 */
		, OutputResponse

		/** NE에서 OS로 통보 */
		, Autonomous
	}

	public static NetPduTL1 makePdu(String s) throws Exception {

		String contents = s.trim();
		String line[] = contents.split("\n");

		if (line.length == 2 && line[1].charAt(0) == '<') {
			return new ACK(s);
		}

		// 2015-07-03 by subkjh
		// 서버에서 보내는 keep-alive 신호 ( 텔레필드 )
		if (line[0].startsWith("HELLO")) {
			TL1KeepAlive pdu = new TL1KeepAlive();
			pdu.setContents(line[0]);
			return pdu;
		}

		ORMF_HEADER header = new ORMF_HEADER(line[0]);

		if (line[1].trim().toCharArray()[0] == 'M') {
			ORMF pdu = new ORMF(s);
			pdu.setHeader(header);
			return pdu;
		} else {
			AM pdu = new AM(s);
			pdu.setHeader(header);
			return pdu;
		}
	}

	private String contents;

	@Override
	public byte[] getBytes() throws Exception {
		return null;
	}

	public String getContents() {
		return contents;
	}

	@Override
	public String getString() {
		return null;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@Override
	public String toString() {
		return contents;
	}
}

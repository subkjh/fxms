package subkjh.bas.net.co;

import subkjh.bas.net.co.vo.NetPdu;

public interface NetSender<PDU extends NetPdu> {

	/**
	 * 입력된 PDU를 보냅니다.
	 * 
	 * @param pdu
	 *            보낼 PDU
	 * @return 보낸 바이트 수
	 * @throws Exception
	 */
	public int send(PDU pdu) throws Exception;

}

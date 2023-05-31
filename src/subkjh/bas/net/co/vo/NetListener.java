package subkjh.bas.net.co.vo;

/**
 * Net상태를 수신하는 리슨너
 * 
 * @author subkjh
 * 
 */
public interface NetListener {

	public static final String tcpConnectFail = "tcpConnectFail";
	public static final String tcpConnected = "tcpConnected";
	public static final String tcpDisconnected = "tcpDisconnected";
	public static final String tcpAccepted = "tcpAccepted";

	public static final String udpConnected = "udpConnected";
	
	public static final String PduMakerFilterAdded = "PduMakerFilterAdded";
	public static final String PduMakerFilterRemove = "PduMakerFilterRemove";
	public static final String PduMakerClosed = "PduMakerClosed";
	public static final String PduMakerOpend = "PduMakerOpend";
	public static final String PduMakerPduAdded = "PduMakerPduAdded";
	public static final String PduMakerBytesAdded = "PduMakerBytesAdded";

	/**
	 * 네트워크 상태를 수신받는 메소드
	 * 
	 * @param state
	 *            상태
	 * @param obj
	 *            그때의 값
	 */
	public void onNetState(String state, Object obj);

}

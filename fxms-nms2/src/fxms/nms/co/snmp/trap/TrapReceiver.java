package fxms.nms.co.snmp.trap;

import java.util.concurrent.LinkedBlockingQueue;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.log.Loggable;

import com.adventnet.snmp.snmp2.Snmp3Message;
import com.adventnet.snmp.snmp2.SnmpAPI;
import com.adventnet.snmp.snmp2.SnmpClient;
import com.adventnet.snmp.snmp2.SnmpPDU;
import com.adventnet.snmp.snmp2.SnmpSession;
import com.adventnet.snmp.snmp2.UDPProtocolOptions;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.nms.co.snmp.SnmpUtil;
import fxms.nms.co.snmp.trap.vo.RecvTrapVo;

/**
 * 트랩 수신 서버
 * 
 * @author subkjh
 * 
 */
public class TrapReceiver extends FxActorImpl implements Loggable, FxServiceMember {

	public class SnmpTrapd implements SnmpClient {

		@Override
		public boolean authenticate(SnmpPDU pdu, String community) {

			if (pdu.getVersion() == SnmpAPI.SNMP_VERSION_3) {
				return !((Snmp3Message) pdu.getMsg()).isAuthenticationFailed();
			} else {

				if (community == null || community.trim().length() == 0)
					return true;
				return (pdu.getCommunity().equals(community));
			}
		}

		@Override
		public boolean callback(SnmpSession session, SnmpPDU pdu, int requestID) {

			mstimeRecv = System.currentTimeMillis();

			RecvTrapVo vo = new RecvTrapVo();
			vo.mstimeRecv = System.currentTimeMillis();
			vo.pdu = pdu;

			try {
				queue.put(vo);
			} catch (InterruptedException e) {
			}

			countRecv++;

			return true;
		}

		@Override
		public void debugPrint(String debugOutput) {
			System.out.println(debugOutput);
		}

	}

	public static void main(String[] args) {
		try {
			TrapReceiver tr = new TrapReceiver();
			tr.receiveTrap(162, "", 3, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private LinkedBlockingQueue<RecvTrapVo> queue;
	private long mstimeRecv;
	private long countRecv;

	public TrapReceiver() {
		queue = new LinkedBlockingQueue<RecvTrapVo>();
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return "queue=" + queue.size() + "|countRecv=" + countRecv + "|hstimeRecv=" + FxApi.getTime(mstimeRecv);
	}

	/**
	 * 
	 * @param port        사용할 포트
	 * @param community   커뮤니티
	 * @param countThread 사용할 스레드 수
	 * @param receiveAll  모두 받을지 여부
	 * @throws Exception
	 */
	public void receiveTrap(int port, String community, int countThread, boolean receiveAll) throws Exception {
		setPara(PORT, String.valueOf(port));
		setPara(COMMUNITY, community);
		setPara(PARSER_COUNT, String.valueOf(countThread));

		startMember();
	}

	public static final String PORT = "snmp-port";
	public static final String PARSER_COUNT = "snmp-parser-count";
	public static final String BUFFER_SIZE = "snmp-buffer-size";
	public static final String COMMUNITY = "snmp-trap-community";

	public void setCommunity(String s) {
		setPara("snmp-trap-community", s);
	}

	public void setPort(int port) {
		setPara(PORT, String.valueOf(port));
	}

	public void setThreadSize(int size) {
		setPara(PARSER_COUNT, String.valueOf(size));
	}

	public void setBufferSize(int size) {
		setPara(BUFFER_SIZE, String.valueOf(size));
	}

	@Override
	public void startMember() throws Exception {

		int port = getFxPara().getInt(PORT, 162);
		int thrSize = getFxPara().getInt(PARSER_COUNT, 3);
		int bufSize = getFxPara().getInt(BUFFER_SIZE, 1000000);
		String community = getFxPara().getString(COMMUNITY);

		StringBuffer sb = new StringBuffer();
		sb.append(Logger.makeString("TRAP-RECEIVER", getName()));
		sb.append(Logger.makeSubString(PORT, port));
		sb.append(Logger.makeSubString(PARSER_COUNT, thrSize));
		sb.append(Logger.makeSubString(BUFFER_SIZE, bufSize));
		sb.append(Logger.makeSubString(COMMUNITY, community));
		FxServiceImpl.logger.info(sb.toString());

		TrapThread th;
		for (int i = 0; i < thrSize; i++) {
			th = new TrapThread(queue);
			th.setName(getName() + "-Thr#" + (i + 1));
			th.start();
		}

		SnmpAPI api = new SnmpAPI();
		api.setName(getName() + "-TrapSnmpApi");
		api.setCharacterEncoding(SnmpUtil.CHARSET.displayName());

		UDPProtocolOptions option = new UDPProtocolOptions();
		option.setLocalPort(port);
		option.setReceiveBufferSize(bufSize);

		SnmpSession session = new SnmpSession(api);
		session.addSnmpClient(new SnmpTrapd());
		session.setProtocolOptions(option);
		session.setCommunity(community);
		session.open();

		FxServiceImpl.logger.info("Waiting to receive traps in the port " + option.getLocalPort() + "...");
	}

}

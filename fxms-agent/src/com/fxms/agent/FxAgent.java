package com.fxms.agent;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.fxms.agent.method.AgentMethod;
import com.fxms.agent.method.KeepAlive;
import com.fxms.agent.method.SystemStateNotify;
import com.fxms.agent.pdu.FxAgentPdu;
import com.fxms.agent.pdu.NotifyPdu;
import com.fxms.agent.pdu.RequestPdu;
import com.fxms.agent.pdu.ResponsePdu;

import fxms.bas.fxo.thread.FxThread;
import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.log.Logger;

/**
 * FX Agent
 * 
 * @author SUBKJH-DEV
 *
 */
public class FxAgent extends QueueFxThread<FxAgentPdu> {

	public static void main(String[] args) {

		String remoteHost = "125.7.128.42";
		// String remoteHost = "localhost";
		int localPort = 63801;
		int remotePort = 63800;

		if (args.length >= 1) {
			localPort = Integer.valueOf(args[0]);
		}
		if (args.length >= 2) {
			remoteHost = args[1];
		}
		if (args.length >= 3) {
			remotePort = Integer.valueOf(args[2]);
		}

		FxAgent agent = new FxAgent(localPort, remoteHost, remotePort, null);

		agent.setCycleMethod(new SystemStateNotify());
		agent.start();
	}

	private Map<String, AgentMethod> methodMap;
	private int localPort;
	private String remoteHost;
	private int remotePort;
	private DatagramSocket socket;
	private NotifyPdu notiPdu;
	private AgentMethod cycleMethod = new KeepAlive();
	private long prevAgentSeqno;
	private long agentSeqno;
	private String version = "0.0.1";
	private LoopFxThread looper;

	/**
	 * 
	 * @param localPort
	 *            내 포트
	 * @param remoteHost
	 *            매니저 주소
	 * @param remotePort
	 *            매니터 포트
	 * @param looper
	 *            루핑작업자
	 */
	public FxAgent(int localPort, String remoteHost, int remotePort, LoopFxThread looper) {

		super("HoleAgent#" + localPort, FxAgentCode.cycleNotifyAgent);

		this.methodMap = new HashMap<String, AgentMethod>();
		this.localPort = localPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
		this.notiPdu = new NotifyPdu();

		try {
			notiPdu.setSessionId(InetAddress.getLocalHost().getHostName() + "-" + getName());
		} catch (UnknownHostException e) {
			notiPdu.setSessionId(getName());
		}

		try {
			notiPdu.setIpAddress(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			notiPdu.setIpAddress("localhost");
		}

		startReceiver();

		this.looper = looper;

		if (looper != null) {
			looper.start();
		}
	}

	/**
	 * 에이전트의 기능 메소드 등록
	 * 
	 * @param method
	 *            기능
	 */
	public void addMethod(AgentMethod method) {
		methodMap.put(method.getMethod(), method);
	}

	public AgentMethod getCycleMethod() {
		return cycleMethod;
	}

	public int getLocalPort() {
		return localPort;
	}

	public LoopFxThread getLooper() {
		return looper;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public String getVersion() {
		return version;
	}

	public boolean isValidSeqno(long seqno) {
		return prevAgentSeqno == seqno || agentSeqno == seqno;
	}

	/**
	 * 주기적으로 매니저에게 보낼 내용을 생성하는 메소드 설정
	 * 
	 * @param agentMethod
	 *            메소드
	 */
	public void setCycleMethod(AgentMethod agentMethod) {
		this.cycleMethod = agentMethod;
	}

	public final Map<String, AgentMethod> getMethodMap() {
		return methodMap;
	}

	private void onReceive(FxAgentPdu receivedPdu) {

		String msg = "ok";
		RequestPdu pdu = null;

		try {
			if (receivedPdu instanceof RequestPdu) {

				pdu = (RequestPdu) receivedPdu;

				AgentMethod method = methodMap.get(pdu.getMethod());

				if (isValidSeqno(pdu.getAgentSeqno())) {
					if (method == null) {
						put(new ResponsePdu(pdu, pdu.getParameters()));
					} else {
						Map<String, Object> map = method.call(this, pdu);
						put(new ResponsePdu(pdu, map));
					}
				} else {
					msg = "agent-seqno is invalid";
				}

			} else {
				msg = "not request";
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			if (pdu != null) {
				put(new ResponsePdu(pdu, e.getMessage()));
			}
		} finally {
			Logger.logger.trace("{} : pdu={}", msg, receivedPdu);
		}

	}

	private void startReceiver() {

		new FxThread(getName() + "-receiver") {

			@Override
			protected void doInit() {
			}

			@Override
			protected void doWork() {

				try {
					socket = new DatagramSocket(localPort);
				} catch (SocketException e) {
					Logger.logger.error(e);
					return;
				}

				Logger.logger.info("wait-port={}", localPort);
				FxAgentPdu pdu;

				while (isContinue()) {

					// receive Data
					DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
					try {
						socket.receive(packet);
						pdu = FxAgentPdu.make(packet);

						Logger.logger.trace("recv={}", pdu);

						onReceive(pdu);

					} catch (Exception e) {
						Logger.logger.error(e);
						continue;
					}

				}

				socket.close();

			}

		}.start();

	}

	@Override
	protected void doInit() {
	}

	@Override
	protected void doWork(FxAgentPdu data) throws Exception {

		try {
			byte[] sendData = data.getSendData().getBytes(FxAgentCode.charset);

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(remoteHost),
					remotePort);
			socket.send(sendPacket);

			Logger.logger.debug("send={}", data.getSendData());

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@Override
	protected void onNoDatas(long index) {

		// 보낼 데이터가 없으면 keep-alive용 내용을 보낸다.

		NotifyPdu pdu = (NotifyPdu) notiPdu.clone();

		pdu.setMethod(cycleMethod.getMethod());
		try {
			pdu.setParameters(cycleMethod.call(this, null));
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		prevAgentSeqno = agentSeqno;
		agentSeqno = System.currentTimeMillis();

		pdu.setAgentSeqno(agentSeqno);

		put(pdu);

	}

}

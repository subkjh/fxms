package com.fxms.agent;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fxms.agent.FxAgentPduProcessor.NotiStatus;
import com.fxms.agent.pdu.FxAgentPdu;
import com.fxms.agent.pdu.NotifyPdu;
import com.fxms.agent.pdu.RequestPdu;
import com.fxms.agent.pdu.ResponsePdu;

import fxms.bas.fxo.thread.FxThread;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

/**
 * FxAgent 관리자
 * 
 * @author SUBKJH-DEV
 *
 */
public class FxAgentManager extends FxThread {

	class Data {
		FxAgentPduProcessor listener;
		long mstime;
	}

	public static void main(String[] args) {
		FxAgentManager manager = new FxAgentManager();
		manager.start();
	}

	private DatagramSocket serverSocket = null;
	private Map<String, FxAgentPdu> map;
	private Map<Long, Data> requestMap;
	private long serverSeqno = System.currentTimeMillis();
	private FxAgentManager manager;
	private FxAgentPduProcessor pduProcessor;
	private int localPort = 8110;

	public FxAgentManager() {

		super("FxAgentManager");

		manager = this;
		map = Collections.synchronizedMap(new HashMap<String, FxAgentPdu>());
		requestMap = Collections.synchronizedMap(new HashMap<Long, Data>());
	}

	public int getLocalPort() {
		return localPort;
	}

	@Override
	public String getState(LOG_LEVEL level) {

		StringBuffer sb = new StringBuffer();
		sb.append(super.getState(level));
		sb.append(", agent=" + map.size());
		sb.append(", request=" + requestMap.size());

		return sb.toString();
	}

	/**
	 * 대상에 요청을 보내고 받은 응답을 수신자에게 건낸다.
	 * 
	 * @param ip
	 *            대상
	 * @param method
	 *            기능
	 * @param parameters
	 *            인자
	 * @param responseReceiver
	 *            응답 수신자
	 */
	public synchronized void request(String ip, String method, Map<String, Object> parameters,
			FxAgentPduProcessor responseReceiver) {

		checkListener();

		Logger.logger.trace("ip={}, method={}, parameters={}", ip, method, parameters);

		long seqno = getNextServerSeqno();

		FxAgentPdu agent = map.get(ip);
		if (agent == null) {
			responseReceiver.onReceive(NotiStatus.NotFoundAgent, null);
			return;
		} else {
			if ((agent.getRecvMstime() + FxAgentCode.cycleNotifyAgent * 1000L) < System.currentTimeMillis()) {
				map.remove(ip);
				responseReceiver.onReceive(NotiStatus.NotFoundAgent, null);
				return;
			}
		}

		try {

			RequestPdu req = new RequestPdu(agent, method, parameters);
			req.setManagerSeqno(seqno);

			String sendData = req.getSendData();
			byte[] bytes = sendData.getBytes(FxAgentCode.charset);
			DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, agent.getRemoteHost(),
					agent.getRemotePort());

			if (responseReceiver != null) {
				Data data = new Data();
				data.listener = responseReceiver;
				data.mstime = System.currentTimeMillis();
				requestMap.put(seqno, data);
			}

			serverSocket.send(sendPacket);

			Logger.logger.trace("send={}", sendData);

		} catch (Exception e) {
			Logger.logger.error(e);
			responseReceiver.onReceive(NotiStatus.SendError, null);
		}
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public void setPduProcessor(FxAgentPduProcessor pduProcessor) {
		this.pduProcessor = pduProcessor;
	}

	private void checkListener() {

		if (requestMap.size() < FxAgentCode.sizeAgentToCheck) {
			return;
		}

		Data pdu;
		Long keyArr[] = requestMap.keySet().toArray(new Long[requestMap.size()]);
		for (Long key : keyArr) {
			pdu = requestMap.get(key);
			if (pdu != null) {
				if (pdu.mstime + (120 * 1000L) < System.currentTimeMillis()) {
					requestMap.remove(key);
				}
			}
		}
	}

	private void checkPdu() {

		if (map.size() < FxAgentCode.sizeAgentToCheck) {
			return;
		}

		FxAgentPdu pdu;
		String keyArr[] = map.keySet().toArray(new String[map.size()]);
		for (String key : keyArr) {
			pdu = map.get(key);
			if (pdu != null) {
				if (pdu.getRecvMstime() + (FxAgentCode.cycleNotifyAgent * 1000L) < System.currentTimeMillis()) {
					map.remove(key);
				}
			}
		}
	}

	private synchronized long getNextServerSeqno() {
		return serverSeqno++;
	}

	@Override
	protected void doInit() {
	}

	@Override
	protected void doWork() {
		FxAgentPdu pdu;
		Data listener;
		String msg;
		try {

			serverSocket = new DatagramSocket(localPort);

			Logger.logger.info(
					Logger.makeString("HoleManager [" + getName() + "] PORT=" + manager.getLocalPort(), "STARTED"));

			while (isContinue()) {

				try {
					// receive Data
					DatagramPacket receivePacket = new DatagramPacket(new byte[4096], 4096);
					serverSocket.receive(receivePacket);

					pdu = FxAgentPdu.make(receivePacket);

					map.put(pdu.getIpAddress(), pdu);

					if (pdu instanceof NotifyPdu) {
						if (pduProcessor != null) {
							pduProcessor.onReceive(NotiStatus.NotifyPdu, pdu);
						}
						msg = "notify";
					} else if (pdu instanceof ResponsePdu) {
						listener = requestMap.remove(pdu.getManagerSeqno());
						if (listener != null) {
							listener.listener.onReceive(NotiStatus.RecvPdu, pdu);
							msg = "ok";
						} else {
							msg = "not-found-listener";
						}
					} else {
						msg = "";
					}

					Logger.logger.trace("recv={}, msg={}", pdu, msg);

				} catch (Exception e) {
					Logger.logger.error(e);
				}

				checkPdu();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}
	}

}

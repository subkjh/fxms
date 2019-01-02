package com.fxms.agent.pdu;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgentCode;
import com.google.gson.Gson;

import subkjh.bas.log.Logger;

/**
 * FxAgent PDU
 * 
 * @author SUBKJH-DEV
 *
 */
public abstract class FxAgentPdu implements Cloneable {

	public enum PduType {
		notify, request, response, undefined;

		public static PduType get(String name) {
			for (PduType t : PduType.values()) {
				if (name.equalsIgnoreCase(t.name())) {
					return t;
				}
			}

			return undefined;
		}
	}

	@SuppressWarnings("unchecked")
	public static FxAgentPdu make(DatagramPacket packet) throws Exception {

		String data = new String(packet.getData(), FxAgentCode.charset).trim();
		Map<String, Object> para;

		Logger.logger.trace(data);

		try {
			para = new Gson().fromJson(data, Map.class);
		} catch (Exception e) {
			Logger.logger.fail(data);
			Logger.logger.error(e);
			throw e;
		}

		String type = String.valueOf(para.get("pdu-type"));
		PduType pduType = PduType.get(type);
		if (pduType == PduType.undefined) {
			throw new Exception("pdu-type is undefined");
		}

		FxAgentPdu pdu = null;

		if (pduType == PduType.notify) {
			pdu = new NotifyPdu();
		} else if (pduType == PduType.request) {
			pdu = new RequestPdu();
		} else if (pduType == PduType.response) {
			pdu = new ResponsePdu();
		}

		pdu.setRemoteHost(packet.getAddress());
		pdu.setRemotePort(packet.getPort());
		pdu.setRecvMstime(System.currentTimeMillis());

		pdu.setSessionId(String.valueOf(para.get("session-id")));

		try {
			pdu.setAgentSeqno(Double.valueOf(para.get("agent-seqno").toString()).longValue());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			pdu.setManagerSeqno(Double.valueOf(para.get("manager-seqno").toString()).longValue());
		} catch (Exception e) {
			e.printStackTrace();
		}

		pdu.setIpAddress(String.valueOf(para.get("ip-address")));
		pdu.setMethod(String.valueOf(para.get("method")));
		pdu.setParameters((Map<String, Object>) para.get("parameters"));

		return pdu;
	}

	private String ipAddress;

	private String sessionId;
	private long agentSeqno;
	private long managerSeqno;
	private String method;
	private Map<String, Object> parameters;
	private InetAddress remoteHost;
	private int remotePort;
	private long recvMstime;
	public FxAgentPdu() {

	}

	public FxAgentPdu(FxAgentPdu pdu) {
		this.ipAddress = pdu.ipAddress;
		this.sessionId = pdu.sessionId;
		this.agentSeqno = pdu.agentSeqno;
		this.managerSeqno = pdu.managerSeqno;
		this.method = pdu.method;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public long getAgentSeqno() {
		return agentSeqno;
	}

	public abstract Map<String, Object> getHeader();

	public String getIpAddress() {
		return ipAddress;
	}

	public String getKey() {
		return ipAddress + "-" + managerSeqno;
	}

	public long getManagerSeqno() {
		return managerSeqno;
	}

	public String getMethod() {
		return method;
	}

	public int getParaInt(String name, int defaultVal) {
		Object val = parameters == null ? null : parameters.get(name);
		if (val == null) {
			return defaultVal;
		}

		if (val instanceof Number) {
			return ((Number) val).intValue();
		} else {
			return Double.valueOf(val.toString()).intValue();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getParaList(String name) {
		Object val = parameters == null ? null : parameters.get(name);
		if (val == null) {
			return null;
		}

		if (val instanceof List) {
			return (List) val;
		} else {
			List list = new ArrayList();
			list.add(val);
			return list;
		}
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public String getParaString(String name) {
		
		Object val = parameters == null ? null : parameters.get(name);
		if (val == null)
			return null;
		
		return val.toString();
	}

	public long getRecvMstime() {
		return recvMstime;
	}

	public InetAddress getRemoteHost() {
		return remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public String getSendData() {

		Map<String, Object> map = getHeader();

		map.put("session-id", getSessionId());
		map.put("ip-address", getIpAddress());
		map.put("agent-seqno", getAgentSeqno());
		map.put("manager-seqno", getManagerSeqno());
		map.put("method", getMethod());
		map.put("parameters", getParameters());

		return new Gson().toJson(map);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setAgentSeqno(long agentSeqno) {
		this.agentSeqno = agentSeqno;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setManagerSeqno(long managerSeqno) {
		this.managerSeqno = managerSeqno;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public void setRecvMstime(long recvMstime) {
		this.recvMstime = recvMstime;
	}

	public void setRemoteHost(InetAddress remoteHost) {
		this.remoteHost = remoteHost;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(getClass().getSimpleName());
		sb.append(",ip=" + ipAddress);
		// if (remoteHost != null) {
		// sb.append(",remote-ip=" + remoteHost.getHostAddress());
		// sb.append(",remote-port=" + remotePort);
		// }
		sb.append(",method=" + method);
		sb.append(",seqno=" + managerSeqno + ":" + agentSeqno);

		return sb.toString();
	}
}

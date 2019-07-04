package fxms.nms.co.snmp.trap.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.nms.co.vo.EventLog;

/**
 * 받은 트랩 로그
 * 
 * @author subkjh
 * 
 */
public class TrapEventLog extends EventLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8785864919008548131L;

	private List<TrapEventLogDtl> details;

	/** 트랩 수신 서버 주소 * */
	private String ipAddrRecv;
	/** 수신 일련 번호 */
	private long seqnoRecv;
	/** 트랩 OID */
	private String trapOid;
	private String trapOidName;

	/** UPTIME */
	private String trapUptime;
	/** 트랩 버전 */
	private int trapVer;

	/** Genenral Trap Code */
	private int trapTypeGen = -1;

	/** Specific Trap Code */
	private int trapTypeSpe = -1;

	/** 경보코드번호 */
	private int alcdNo;

	private String ipAddress;
	
	public TrapEventLog()
	{
		
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @deprecated
	 * 
	 * @param detail
	 */
	@Deprecated
	public void addDetail(TrapEventLogDtl detail) {
		if (details == null)
			details = new ArrayList<TrapEventLogDtl>();
		details.add(detail);
	}

	public List<TrapEventLogDtl> getDetails() {
		return details;
	}

	/**
	 *
	 * @return 파일에 기록한 내용
	 * @since 2013.02.19 by subkjh
	 */
	public String getFileLine() {
		String s = getHstimeRecv() + "";
		if (s.length() == 14) {
			return s.substring(8, 10) + ":" + s.substring(10, 12) + ":" + s.substring(12) + " " + getLogMsg() + ","
					+ getAlarmCode() + "|" + getAlarmLevel();
		} else {
			return getHstimeRecv() + ":" + getLogMsg() + "," + getAlarmCode() + "|" + getAlarmLevel();
		}

	}

	public String getIpAddrRecv() {
		return ipAddrRecv;
	}

	// public String getIpAddrSend() {
	// return getIpAddress();
	// }
	//
	// public long getMoNoNode() {
	// return getMoNo();
	// }

	public long getSeqnoRecv() {
		return seqnoRecv;
	}

	// public String getTrapMemo() {
	// return getLogMsg();
	// }

	public String getTrapOid() {
		return trapOid;
	}

	public String getTrapOidName() {
		return trapOidName;
	}

	public int getTrapTypeGen() {
		return trapTypeGen;
	}

	public int getTrapTypeSpe() {
		return trapTypeSpe;
	}

	public String getTrapUptime() {
		return trapUptime;
	}

	public int getTrapVer() {
		return trapVer;
	}

	public void setDetails(List<TrapEventLogDtl> details) {
		this.details = details;

		String logMsg;

		if (details != null && details.size() > 0) {
			logMsg = details.get(0).getVarOidName() + "=" + details.get(0).getVarValueName();
			for (int i = 1, size = details.size(); i < size; i++) {
				logMsg += "|" + details.get(i).getVarOidName() + "=" + details.get(i).getVarValueName();
			}
		} else {
			logMsg = "";
		}

		// setLogMsg(logMsg);
	}

	public void setIpAddrRecv(String ipAddrRecv) {
		this.ipAddrRecv = ipAddrRecv;
	}

	// public void setIpAddrSend(String ipAddrSend) {
	// setIpAddress(ipAddrSend);
	// }
	//
	// public void setMoNoNode(long moNoNode) {
	// setMoNo(moNoNode);
	// }

	public void setSeqnoRecv(long seqnoRecv) {
		this.seqnoRecv = seqnoRecv;
	}

	// public void setTrapMemo(String trapMemo) {
	// setLogMsg(trapMemo);
	// }

	public void setTrapOid(String trapOid) {
		this.trapOid = trapOid;
	}

	public void setTrapOidName(String trapOidName) {
		this.trapOidName = trapOidName;
	}

	public void setTrapTypeGen(int trapTypeGen) {
		this.trapTypeGen = trapTypeGen;
	}

	public void setTrapTypeSpe(int trapTypeSpe) {
		this.trapTypeSpe = trapTypeSpe;
	}

	public void setTrapUptime(String trapUptime) {
		this.trapUptime = trapUptime;
	}

	public void setTrapVer(int trapVer) {
		this.trapVer = trapVer;
	}

	public int getAlcdNo() {
		return alcdNo;
	}

	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

}

package fxms.nms.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subkjh.bas.fxdao.control.FxTableMaker;
import fxms.bas.impl.mo.FxMo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.property.HasAddableData;
import fxms.bas.mo.property.HasIp;
import fxms.bas.mo.property.MoModelable;
import fxms.bas.mo.property.MoOwnership;
import fxms.nms.dbo.FN_NE_STATUS;
import fxms.nms.mo.property.MoSnmppable;
import fxms.nms.mo.property.SnmpPass;
import fxms.nms.mo.property.TelnetPass;

public class NeMo extends FxMo implements MoSnmppable, MoModelable, HasAddableData, HasIp, MoOwnership {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3546593997485208598L;

	public static final int STATUS_SNMP_ONLINE = 1;
	public static final int STATUS_SNMP_TIMEOUT = -1;
	public static final int STATUS_SNMP_ERROR = -2;

	public static final String MO_CLASS = "NE";

	public static void main(String[] args) {
		try {
			System.out.println(new FxTableMaker().makeTableList(NeMo.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private MoConfig moConfig;
	/** 결과가 NULL인 OID를 가지고 있는 MAP */
	private List<String> nullOidList;
	/** 존재하지 않은 OID */
	private transient List<String> oidNotExistList;

	private SnmpPass snmpPass;
	private TelnetPass telnetPass;
	private FN_NE_STATUS neStatus;

	public NeMo() {
		setMoClass(MO_CLASS);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NeMo) {
			NeMo target = (NeMo) obj;
			if (target.getMoNo() == getMoNo() //
					&& target.getIpAddress().equals(getIpAddress()))
				return true;
		}
		return super.equals(obj);
	}

	public MoConfig getMoConfig() {
		if (moConfig == null) {
			moConfig = new MoConfig(this);
		}
		return moConfig;
	}

	public long getIpNum() {
		return HasIp.getIpNum(getIpAddress());
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

	public FN_NE_STATUS getNeStatus() {
		if (neStatus == null) {
			neStatus = new FN_NE_STATUS();
		}
		return neStatus;
	}

	/**
	 * 결과가 NULL인 oid를 추가합니다.
	 * 
	 * @param oid
	 */
	public List<String> getNullOidList() {
		if (nullOidList == null) {
			nullOidList = new ArrayList<String>();
		}
		return nullOidList;
	}

	/**
	 * 없는 OID 정보를 추가합니다.
	 * 
	 * @param oid
	 */
	public List<String> getOidNotExistList() {
		if (oidNotExistList == null) {
			oidNotExistList = new ArrayList<String>();
		}
		return oidNotExistList;
	}

	@Override
	public SnmpPass getSnmpPass() {
		if (getSnmpString() != null && snmpPass == null) {
			try {
				snmpPass = new SnmpPass(getSnmpString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return snmpPass;
	}

	public TelnetPass getTelnetPass() {
		if (getTelnetString() != null && telnetPass == null) {
			try {
				telnetPass = new TelnetPass(getTelnetString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return telnetPass;
	}

	/**
	 * 입력된 oid가 NULL을 가지는지 여부
	 * 
	 * @param oid
	 * @return true이면 oid는 null이므로 무시합니다.
	 */
	public boolean isNullOid(String oid) {
		return nullOidList == null ? false : nullOidList.contains(oid);
	}

	@Override
	public boolean isSnmp() {
		return false;
	}

	/**
	 * 
	 * 
	 * @param oid
	 * @return OID가 존재하지 않은지 여부
	 */
	public boolean oidNotExist(String oid) {
		if (oidNotExistList == null)
			return false;
		return oidNotExistList.contains(oid);
	}

	@Override
	public List<Object> getAddableDatas() {
		getNeStatus().setMoNo(getMoNo());
		return Arrays.asList(getNeStatus());
	}

	private String ipAddress;
	private String ipAddress2;
	private int modelNo = -1;
	private String categoryCd = "ETC";
	private int inloNo = -1;
	private String inloMemo;
	private String netType = "IP";
	private String contactName;
	private String contactTelno;
	private String nodeMemo;
	private String nodeIdExtra;
	private int installYmd;
	private boolean syslogRecvYn = true;
	private boolean trapRecvYn = true;
	private boolean pingPollYn = true;
	private boolean snmppingPollYn = true;
	private boolean confAutoSyncYn = true;
	private boolean bgpRecvYn = false;
	private boolean flowRecvYn = false;
	private int pollCycleSnmp = 0;
	private int pollCyclePing = 0;
	private String snmpString;
	private String telnetString;
	private String bgpString;
	private String flowString;
	private String msIpaddr;
	private String userData;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress2() {
		return ipAddress2;
	}

	public void setIpAddress2(String ipAddress2) {
		this.ipAddress2 = ipAddress2;
	}

	public int getModelNo() {
		return modelNo;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public String getCategoryCd() {
		return categoryCd;
	}

	public void setCategoryCd(String categoryCd) {
		this.categoryCd = categoryCd;
	}

	public int getInloNo() {
		return inloNo;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public String getInloMemo() {
		return inloMemo;
	}

	public void setInloMemo(String inloMemo) {
		this.inloMemo = inloMemo;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTelno() {
		return contactTelno;
	}

	public void setContactTelno(String contactTelno) {
		this.contactTelno = contactTelno;
	}

	public String getNodeMemo() {
		return nodeMemo;
	}

	public void setNodeMemo(String nodeMemo) {
		this.nodeMemo = nodeMemo;
	}

	public String getNodeIdExtra() {
		return nodeIdExtra;
	}

	public void setNodeIdExtra(String nodeIdExtra) {
		this.nodeIdExtra = nodeIdExtra;
	}

	public int getInstallYmd() {
		return installYmd;
	}

	public void setInstallYmd(int installYmd) {
		this.installYmd = installYmd;
	}

	public boolean isSyslogRecvYn() {
		return syslogRecvYn;
	}

	public void setSyslogRecvYn(boolean syslogRecvYn) {
		this.syslogRecvYn = syslogRecvYn;
	}

	public boolean isTrapRecvYn() {
		return trapRecvYn;
	}

	public void setTrapRecvYn(boolean trapRecvYn) {
		this.trapRecvYn = trapRecvYn;
	}

	public boolean isPingPollYn() {
		return pingPollYn;
	}

	public void setPingPollYn(boolean pingPollYn) {
		this.pingPollYn = pingPollYn;
	}

	public boolean isSnmppingPollYn() {
		return snmppingPollYn;
	}

	public void setSnmppingPollYn(boolean snmppingPollYn) {
		this.snmppingPollYn = snmppingPollYn;
	}

	public boolean isConfAutoSyncYn() {
		return confAutoSyncYn;
	}

	public void setConfAutoSyncYn(boolean confAutoSyncYn) {
		this.confAutoSyncYn = confAutoSyncYn;
	}

	public boolean isBgpRecvYn() {
		return bgpRecvYn;
	}

	public void setBgpRecvYn(boolean bgpRecvYn) {
		this.bgpRecvYn = bgpRecvYn;
	}

	public boolean isFlowRecvYn() {
		return flowRecvYn;
	}

	public void setFlowRecvYn(boolean flowRecvYn) {
		this.flowRecvYn = flowRecvYn;
	}

	public int getPollCycleSnmp() {
		return pollCycleSnmp;
	}

	public void setPollCycleSnmp(int pollCycleSnmp) {
		this.pollCycleSnmp = pollCycleSnmp;
	}

	public int getPollCyclePing() {
		return pollCyclePing;
	}

	public void setPollCyclePing(int pollCyclePing) {
		this.pollCyclePing = pollCyclePing;
	}

	public String getSnmpString() {
		return snmpString;
	}

	public void setSnmpString(String snmpString) {
		this.snmpString = snmpString;
	}

	public String getTelnetString() {
		return telnetString;
	}

	public void setTelnetString(String telnetString) {
		this.telnetString = telnetString;
	}

	public String getBgpString() {
		return bgpString;
	}

	public void setBgpString(String bgpString) {
		this.bgpString = bgpString;
	}

	public String getFlowString() {
		return flowString;
	}

	public void setFlowString(String flowString) {
		this.flowString = flowString;
	}

	public String getMsIpaddr() {
		return msIpaddr;
	}

	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public void setMoConfig(MoConfig moConfig) {
		this.moConfig = moConfig;
	}

	public void setNullOidList(List<String> nullOidList) {
		this.nullOidList = nullOidList;
	}

	public void setOidNotExistList(List<String> oidNotExistList) {
		this.oidNotExistList = oidNotExistList;
	}

	public void setSnmpPass(SnmpPass snmpPass) {
		this.snmpPass = snmpPass;
	}

	public void setTelnetPass(TelnetPass telnetPass) {
		this.telnetPass = telnetPass;
	}

	public void setNeStatus(FN_NE_STATUS neStatus) {
		this.neStatus = neStatus;
	}

}

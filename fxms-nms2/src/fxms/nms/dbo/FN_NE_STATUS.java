package fxms.nms.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.03.27 16:24
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FN_NE_STATUS", comment = "NE상태테이블")
@FxIndex(name = "FN_NE_STATUS__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
public class FN_NE_STATUS implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8399151945524195814L;

	public FN_NE_STATUS() {
	}

	@FxColumn(name = "MO_NO", size = 19, comment = "NE관리번호")
	private long moNo;

	@FxColumn(name = "SYS_NAME", size = 100, nullable = true, comment = "SysName")
	private String sysName;

	@FxColumn(name = "SYS_DESCR", size = 1000, nullable = true, comment = "SysDescr")
	private String sysDescr;

	@FxColumn(name = "SYS_OBJECT_ID", size = 200, nullable = true, comment = "SysObjectID")
	private String sysObjectId;

	@FxColumn(name = "SYS_LOCATION", size = 200, nullable = true, comment = "SysLocation")
	private String sysLocation;

	@FxColumn(name = "SYS_UPTIME", size = 20, nullable = true, comment = "SysUptime")
	private long sysUptime;

	@FxColumn(name = "SYS_SERVICES", size = 9, nullable = true, comment = "SysServices")
	private int sysServices;

	@FxColumn(name = "SYS_UPTIME_CHG_DATE", size = 14, nullable = true, comment = "SysUptime변경일시")
	private long sysUptimeChgDate;

	@FxColumn(name = "STATUS_ICMP", size = 1, comment = "ICMP PING 상태")
	private int statusIcmp;

	@FxColumn(name = "STATUS_SNMP", size = 1, comment = "SNMP PING 상태 ")
	private int statusSnmp;

	@FxColumn(name = "STATUS_ICMP_CHG_DATE", size = 14, nullable = true, comment = "변경일시(ICMP상태)")
	private long statusIcmpChgDate;

	@FxColumn(name = "STATUS_SNMP_CHG_DATE", size = 14, nullable = true, comment = "변경일시(SNMP상태)")
	private long statusSnmpChgDate;

	@FxColumn(name = "VER_SW", size = 100, nullable = true, comment = "Version ( SW )")
	private String verSw;

	@FxColumn(name = "VER_FW", size = 100, nullable = true, comment = "Version ( FW )")
	private String verFw;

	@FxColumn(name = "VER_HW", size = 100, nullable = true, comment = "Version ( HW )")
	private String verHw;

	@FxColumn(name = "TRAP_LAST_DATE", size = 14, nullable = true, comment = "TRAP최근수신일시")
	private long trapLastDate;

	@FxColumn(name = "SYSLOG_LAST_DATE", size = 14, nullable = true, comment = "SYSLOG최근수신일시")
	private long syslogLastDate;

	@FxColumn(name = "CONF_SYNC_DATE", size = 14, nullable = true, comment = "구성동기일시")
	private long confSyncDate;

	/**
	 * NE관리번호
	 * 
	 * @return NE관리번호
	 */
	public long getMoNo() {
		return moNo;
	}

	/**
	 * NE관리번호
	 * 
	 * @param moNo
	 *            NE관리번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * SysName
	 * 
	 * @return SysName
	 */
	public String getSysName() {
		return sysName;
	}

	/**
	 * SysName
	 * 
	 * @param sysName
	 *            SysName
	 */
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	/**
	 * SysDescr
	 * 
	 * @return SysDescr
	 */
	public String getSysDescr() {
		return sysDescr;
	}

	/**
	 * SysDescr
	 * 
	 * @param sysDescr
	 *            SysDescr
	 */
	public void setSysDescr(String sysDescr) {
		this.sysDescr = sysDescr;
	}

	/**
	 * SysObjectID
	 * 
	 * @return SysObjectID
	 */
	public String getSysObjectId() {
		return sysObjectId;
	}

	/**
	 * SysObjectID
	 * 
	 * @param sysObjectId
	 *            SysObjectID
	 */
	public void setSysObjectId(String sysObjectId) {
		this.sysObjectId = sysObjectId;
	}

	/**
	 * SysLocation
	 * 
	 * @return SysLocation
	 */
	public String getSysLocation() {
		return sysLocation;
	}

	/**
	 * SysLocation
	 * 
	 * @param sysLocation
	 *            SysLocation
	 */
	public void setSysLocation(String sysLocation) {
		this.sysLocation = sysLocation;
	}

	/**
	 * SysUptime
	 * 
	 * @return SysUptime
	 */
	public long getSysUptime() {
		return sysUptime;
	}

	/**
	 * SysUptime
	 * 
	 * @param sysUptime
	 *            SysUptime
	 */
	public void setSysUptime(long sysUptime) {
		this.sysUptime = sysUptime;
	}

	/**
	 * SysServices
	 * 
	 * @return SysServices
	 */
	public int getSysServices() {
		return sysServices;
	}

	/**
	 * SysServices
	 * 
	 * @param sysServices
	 *            SysServices
	 */
	public void setSysServices(int sysServices) {
		this.sysServices = sysServices;
	}

	/**
	 * SysUptime변경일시
	 * 
	 * @return SysUptime변경일시
	 */
	public long getSysUptimeChgDate() {
		return sysUptimeChgDate;
	}

	/**
	 * SysUptime변경일시
	 * 
	 * @param sysUptimeChgDate
	 *            SysUptime변경일시
	 */
	public void setSysUptimeChgDate(long sysUptimeChgDate) {
		this.sysUptimeChgDate = sysUptimeChgDate;
	}

	/**
	 * ICMP PING 상태
	 * 
	 * @return ICMP PING 상태
	 */
	public int getStatusIcmp() {
		return statusIcmp;
	}

	/**
	 * ICMP PING 상태
	 * 
	 * @param statusIcmp
	 *            ICMP PING 상태
	 */
	public void setStatusIcmp(int statusIcmp) {
		this.statusIcmp = statusIcmp;
	}

	/**
	 * SNMP PING 상태
	 * 
	 * @return SNMP PING 상태
	 */
	public int getStatusSnmp() {
		return statusSnmp;
	}

	/**
	 * SNMP PING 상태
	 * 
	 * @param statusSnmp
	 *            SNMP PING 상태
	 */
	public void setStatusSnmp(int statusSnmp) {
		this.statusSnmp = statusSnmp;
	}

	/**
	 * 변경일시(ICMP상태)
	 * 
	 * @return 변경일시(ICMP상태)
	 */
	public long getStatusIcmpChgDate() {
		return statusIcmpChgDate;
	}

	/**
	 * 변경일시(ICMP상태)
	 * 
	 * @param statusIcmpChgDate
	 *            변경일시(ICMP상태)
	 */
	public void setStatusIcmpChgDate(long statusIcmpChgDate) {
		this.statusIcmpChgDate = statusIcmpChgDate;
	}

	/**
	 * 변경일시(SNMP상태)
	 * 
	 * @return 변경일시(SNMP상태)
	 */
	public long getStatusSnmpChgDate() {
		return statusSnmpChgDate;
	}

	/**
	 * 변경일시(SNMP상태)
	 * 
	 * @param statusSnmpChgDate
	 *            변경일시(SNMP상태)
	 */
	public void setStatusSnmpChgDate(long statusSnmpChgDate) {
		this.statusSnmpChgDate = statusSnmpChgDate;
	}

	/**
	 * Version ( SW )
	 * 
	 * @return Version ( SW )
	 */
	public String getVerSw() {
		return verSw;
	}

	/**
	 * Version ( SW )
	 * 
	 * @param verSw
	 *            Version ( SW )
	 */
	public void setVerSw(String verSw) {
		this.verSw = verSw;
	}

	/**
	 * Version ( FW )
	 * 
	 * @return Version ( FW )
	 */
	public String getVerFw() {
		return verFw;
	}

	/**
	 * Version ( FW )
	 * 
	 * @param verFw
	 *            Version ( FW )
	 */
	public void setVerFw(String verFw) {
		this.verFw = verFw;
	}

	/**
	 * Version ( HW )
	 * 
	 * @return Version ( HW )
	 */
	public String getVerHw() {
		return verHw;
	}

	/**
	 * Version ( HW )
	 * 
	 * @param verHw
	 *            Version ( HW )
	 */
	public void setVerHw(String verHw) {
		this.verHw = verHw;
	}

	/**
	 * TRAP최근수신일시
	 * 
	 * @return TRAP최근수신일시
	 */
	public long getTrapLastDate() {
		return trapLastDate;
	}

	/**
	 * TRAP최근수신일시
	 * 
	 * @param trapLastDate
	 *            TRAP최근수신일시
	 */
	public void setTrapLastDate(long trapLastDate) {
		this.trapLastDate = trapLastDate;
	}

	/**
	 * SYSLOG최근수신일시
	 * 
	 * @return SYSLOG최근수신일시
	 */
	public long getSyslogLastDate() {
		return syslogLastDate;
	}

	/**
	 * SYSLOG최근수신일시
	 * 
	 * @param syslogLastDate
	 *            SYSLOG최근수신일시
	 */
	public void setSyslogLastDate(long syslogLastDate) {
		this.syslogLastDate = syslogLastDate;
	}

	/**
	 * 구성동기일시
	 * 
	 * @return 구성동기일시
	 */
	public long getConfSyncDate() {
		return confSyncDate;
	}

	/**
	 * 구성동기일시
	 * 
	 * @param confSyncDate
	 *            구성동기일시
	 */
	public void setConfSyncDate(long confSyncDate) {
		this.confSyncDate = confSyncDate;
	}
}

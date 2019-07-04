package fxms.nms.co.snmp.trap.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.co.noti.FxEventImpl;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.co.snmp.vo.SNMP;
import fxms.nms.co.snmp.vo.SNMP.TrapType;

public class TrapVo extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7756284690806178724L;

	private String ipAddress;
	private SNMP.Version ver;
	private TrapType trapType;
	private String trapOid;
	private long uptime;
	private List<OidValue> list;
	private long mstimeRecv;
	private boolean throwOut = false;

	public TrapVo() {
		setEventType("trap");
	}

	public void add(OidValue data) {
		if (list == null) {
			list = new ArrayList<OidValue>();
		}
		list.add(data);
	}

	public List<OidValue> find(String oid, boolean startWiths) {

		List<OidValue> ret = new ArrayList<OidValue>();

		if (list == null) {
			return ret;
		}

		for (OidValue e : list) {
			if (startWiths && e.getOid().startsWith(oid)) {
				ret.add(e);
			} else if (e.getOid().equals(oid)) {
				ret.add(e);
			}
		}

		return ret;
	}

	/**
	 * 
	 * @return 트랩을 보낸 IP 주소
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	public List<OidValue> getList() {
		return list;
	}

	public long getMstimeRecv() {
		return mstimeRecv;
	}

	public String getString() {
		if (list == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (OidValue e : list) {
			if (sb.length() > 0)
				sb.append("\n");
			sb.append(e.toString());
		}
		return sb.toString();
	}

	public String getTrapOid() {
		return trapOid;
	}

	public TrapType getTrapType() {
		return trapType;
	}

	public long getUptime() {
		return uptime;
	}

	public String getValueString() {
		if (list == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (OidValue e : list) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(e.getValue());
		}
		return sb.toString();
	}

	public SNMP.Version getVer() {
		return ver;
	}

	public boolean isThrowOut() {
		return throwOut;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setList(List<OidValue> list) {
		this.list = list;
	}

	public void setMstimeRecv(long mstimeRecv) {
		this.mstimeRecv = mstimeRecv;
	}

	public void setThrowOut(boolean throwOut) {
		this.throwOut = throwOut;
	}

	public void setTrapOid(String trapOid) {
		this.trapOid = trapOid;
	}

	public void setTrapType(TrapType trapType) {
		this.trapType = trapType;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public void setVer(SNMP.Version ver) {
		this.ver = ver;
	}

	public String getFileLine() {
		StringBuffer sb = new StringBuffer();
		sb.append(getTrapType());
		if (list != null) {
			for (OidValue e : list) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(e.toString());
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		String values = getString();
		if (values.length() > 0) {
			return getTrapType() + "\n" + values;
		} else {
			return getTrapType() + "";
		}
	}

}

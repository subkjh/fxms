package com.fxms.nms.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fxms.nms.dbo.FN_MO_NE;
import com.fxms.nms.dbo.FN_NE_STATUS;
import com.fxms.nms.mo.property.MoSnmppable;
import com.fxms.nms.mo.property.SnmpPass;
import com.fxms.nms.mo.property.TelnetPass;

import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.property.HasAddableData;
import fxms.bas.mo.property.MoIpAddressable;
import fxms.bas.mo.property.MoModelable;
import fxms.bas.mo.property.MoOwnership;
import fxms.bas.mo.property.Moable;
import subkjh.bas.fxdao.control.FxTableMaker;

public class NeMo extends FN_MO_NE
		implements MoSnmppable, MoModelable, HasAddableData, MoIpAddressable, Moable, MoOwnership {

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
		return MoIpAddressable.getIpNum(getIpAddress());
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

}

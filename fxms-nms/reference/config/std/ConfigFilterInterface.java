package com.daims.dfc.filter.config.std;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.api.DfApi;
import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.MoInterface;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.filter.config.std.beans.AddrBean;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;
import com.daims.dfc.mib.IF_MIB;
import com.daims.dfc.mib.IP_MIB;
import com.daims.dfc.service.conf.ConfApi;
import com.daims.dfc.service.conf.beans.BwCode;

/**
 * 장비로 부터 인터페이스 정보를 가져오는 전반부 필터입니다.<br>
 * para<br>
 * ifTypeToRemove : 제거할 인터페이스 종류
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterInterface extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6707303370591158934L;

	private int ifTypes[] = null;

	private final IF_MIB MIB = new IF_MIB();
	private final IP_MIB IPMIB = new IP_MIB();

	public ConfigFilterInterface() {

	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {

			List<MoInterface> moList = getMoInterfaceList(node);

			// ADMIN STATUS가 UP인 경우에 한하여 관리로 처리합니다.
			if (moList != null) {

				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoInterface.MO_CLASS);

				for (MoInterface mo : moList) {
					if (mo.getIfStatusAdmin() == IF_MIB.ifAdminStatus_up) {
						mo.setManaged(true);
					}
					else {
						mo.setManaged(false);
					}
				}
			}

			configMo.addMoListDetected(moList);

			return new Ret(moList.size());

		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}

	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoInterface.MO_CLASS };
	}

	protected MoInterface getInterface(SnmpMo node, int ifIndex) throws Exception {

		String dotidx = "." + ifIndex;
		OidValue ovArr[];

		try {
			ovArr = snmpget(node //
					, MIB.ifType + dotidx //
					, MIB.ifName + dotidx //
					, MIB.ifDescr + dotidx //
					, MIB.ifSpeed + dotidx //
					, MIB.ifHighSpeed + dotidx //
					, MIB.ifHCInOctets + dotidx //
					, MIB.ifAdminStatus + dotidx //
					, MIB.ifOperStatus + dotidx //
					, MIB.ifAlias + dotidx //
					, MIB.ifPhysAddress + dotidx);
		}
		catch (Exception e) {
			ServiceImpl.logger.fail("MO-NO-NODE(" + node.getMoNo() + ")IF-INDEX(" + ifIndex + ") "
					+ e.getClass().getSimpleName() + ":" + e.getMessage());
			return null;
		}

		boolean isBit64 = false;
		String ifAlias = null;
		if (ovArr[8].isNull() == false) {
			try {
				ifAlias = ovArr[8].getValue();
			}
			catch (Exception e) {
			}
		}
		long ifSpeed = ovArr[3].getLong(-1);
		if (ovArr[4].isNull() == false) {
			if (ovArr[4].getLong(0) > 0) ifSpeed = ovArr[4].getLong(0) * 1000000L;
			isBit64 = true;
		}

		if (ovArr[5].isNull()) isBit64 = false;

		MoInterface port;
		try {
			port = DfApi.makeObject(MoInterface.class);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			port = new MoInterface();
		}

		// 대역폭 코드 설정
		BwCode bwCode = ConfApi.getApi().getBwCode(ifSpeed);

		MoInterface.set(port, ovArr[1].getValue(), ifAlias, ovArr[2].getValue(), null, null, ifSpeed, isBit64,
				ovArr[6].getInt(-1), ovArr[7].getInt(-1), ovArr[9].getMacAddress(), ovArr[0].getInt(-1), ifIndex,
				bwCode);

		if (ServiceImpl.logger.isRaw()) {
			ServiceImpl.logger.raw(port.getIfType() + "|" + port.getIfName() + "|" + port.getIfDescr());
		}

		return port;
	}

	@Override
	protected String getOidToCheck() {
		return null;
	}

	private List<Integer> getIfIndexListFromNode(SnmpMo node) throws Exception {

		List<OidValue> varList = snmpwalk(node, MIB.ifType);
		if (varList == null || varList.size() == 0) return new ArrayList<Integer>();

		List<Integer> ifIndexList = new ArrayList<Integer>();
		int ifType;
		int ifIndex;

		for (int i = 0; i < varList.size(); i++) {
			ifType = varList.get(i).getInt();
			ifIndex = Integer.parseInt(varList.get(i).getInstance(1));

			if (isIfTypeToRemove(ifType)) continue;

			ifIndexList.add(ifIndex);
		}

		return ifIndexList;
	}

	private Map<String, AddrBean> getIpAddrTable2(SnmpMo snmpnode) {
		Map<String, AddrBean> map = new HashMap<String, AddrBean>();
		AddrBean addrBean = null;
		List<OidValue>[] varList;
		String oidArray[] = new String[] { IPMIB.ipAdEntIfIndex, IPMIB.ipAdEntAddr, IPMIB.ipAdEntNetMask,
				IPMIB.ipAdEntBcastAddr };
		try {
			varList = snmpwalk(snmpnode, oidArray);
		}
		catch (SnmpTimeoutException e) {
			return null;
		}
		catch (Exception e) {
			return map;
		}

		for (int index = 0; index < varList[0].size(); index++) {

			addrBean = new AddrBean();
			addrBean.index = varList[0].get(index).getInt(0);
			addrBean.ipaddr = varList[1].get(index).getValue();
			addrBean.netmask = varList[2].get(index).getValue();
			addrBean.calcBcastAddr(varList[3].get(index).getInt(0));

			map.put(addrBean.ipaddr, addrBean);

		}

		return map;
	}

	private List<MoInterface> getMoInterfaceList(SnmpMo snmpnode) throws Exception {
		List<MoInterface> portList = new ArrayList<MoInterface>();
		MoInterface port = null;
		List<Integer> ifIndexList = null;

		Map<String, AddrBean> addtable = getIpAddrTable2(snmpnode);
		if (addtable == null) throw new Exception("Timeout");

		// ifIndex 목록을 가져온다.
		ifIndexList = getIfIndexListFromNode(snmpnode);

		ServiceImpl.logger.debug("MO-NO-NODE(" + snmpnode.getMoNo() + ")INTF-SIZE("
				+ (ifIndexList == null ? 0 : ifIndexList.size()) + ")");

		if (ifIndexList != null) {

			for (Integer ifIndex : ifIndexList) {

				for (int i = 0; i < 3; i++) {
					port = getInterface(snmpnode, ifIndex);
					if (port != null) break;
					Thread.sleep(200);
				}

				if (port != null && port.getIfType() > 0) {
					// 인터페이스명이 없는 경우가 발생. 이때는 무시합니다.
					if (port.getMoName() == null || port.getMoName().trim().length() == 0) continue;

					if (addtable != null) {
						setAddrTable(addtable, port);
					}

					portList.add(port);
				}

			}
		}

		return portList;
	}

	private boolean isIfTypeToRemove(int ifType) {
		if (ifTypes == null) {

			String para = getProperty("IGNORE_IFTYPE", null);

			if (para == null || para.length() == 0) {
				ifTypes = new int[] {};
			}
			else {
				String ss[] = para.split(",|;");
				ifTypes = new int[ss.length];
				for (int i = 0; i < ss.length; i++) {
					{
						try {
							ifTypes[i] = Integer.parseInt(ss[i]);
						}
						catch (Exception e) {
							ServiceImpl.logger.error(e);
							ifTypes[i] = -1;
						}
					}
				}
			}
		}

		for (int index : ifTypes) {
			if (index == ifType) return true;
		}

		return false;
	}

	/**
	 * 인터페이스 IP 주소 , Subnetmak , broadcast 주소를 설정한다.
	 * 
	 * @param addrtbl
	 *            Hashtable 장비에서 로드한 모든 주소테이블
	 * @param ifBean
	 *            InterfaceInfo 설정을 할 인터페이스정보
	 */
	private void setAddrTable(Map<String, AddrBean> addtable, MoInterface port) {
		try {

			for (AddrBean addrb : addtable.values()) {
				if (port.getIfIndex() == addrb.index) {
					port.setIpAddress(addrb.ipaddr);
					port.setIfNetmask(addrb.netmask);
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package com.fxms.nms.fxactor.conf.snmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.nms.fxactor.conf.data.IfAddr;
import com.fxms.nms.mo.NeIfMo;
import com.fxms.nms.mo.property.MoSnmppable;
import com.fxms.nms.snmp.beans.OidValue;
import com.fxms.nms.snmp.exception.SnmpTimeoutException;
import com.fxms.nms.snmp.mib.IFMIB;
import com.fxms.nms.snmp.mib.IP_MIB;

import fxms.bas.exception.FxTimeoutException;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.noti.FxEvent;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class ConfIfActor extends ConfSnmpFxActor {

	private int ifTypes[] = null;
	private final IFMIB MIB = new IFMIB();
	private final IP_MIB IPMIB = new IP_MIB();

	@Override
	public void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception {

		if (moClasses.length > 0 && Arrays.asList(moClasses).contains(NeIfMo.MO_CLASS) == false) {
			return;
		}

		MoSnmppable node = getSnmp(children.getParent());

		String ifTypeIgnore = getParaStr("if-type-ignore");
		String onlyHasIp = getParaStr("only-has-ip");
		String ifTypes[] = (ifTypeIgnore == null ? new String[0] : ifTypeIgnore.split(","));
		List<String> ifTypeIgnoreList = Arrays.asList(ifTypes);

		try {

			List<NeIfMo> moList = getIfMoList(node);
			List<NeIfMo> toAddList = new ArrayList<NeIfMo>();

			// ADMIN STATUS가 UP인 경우에 한하여 관리로 처리합니다.
			if (moList != null) {

				children.setStatusAllChildren(FxEvent.STATUS.raw, FxEvent.STATUS.deleted, NeIfMo.MO_CLASS);

				for (NeIfMo ifMo : moList) {
					if (ifMo.getIfStatusAdmin() == IFMIB.ifAdminStatus_up) {
						ifMo.setMngYn(true);
					} else {
						ifMo.setMngYn(false);
					}

					if (ifTypeIgnoreList.contains(ifMo.getIfType() + "")) {
						continue;
					}

					if ("true".equalsIgnoreCase(onlyHasIp)) {
						if (ifMo.getIpAddress() != null) {
							toAddList.add(ifMo);
						}
					} else {
						toAddList.add(ifMo);
					}
				}
			}

			children.addMoListDetected(toAddList);

		} catch (Exception e) {
			throw e;
		}
	}

	private List<Integer> getIfIndexListFromNode(MoSnmppable node) throws Exception {

		List<OidValue> varList = snmpwalk(node, MIB.ifType);
		if (varList == null || varList.size() == 0)
			return new ArrayList<Integer>();

		List<Integer> ifIndexList = new ArrayList<Integer>();
		int ifType;
		int ifIndex;

		for (int i = 0; i < varList.size(); i++) {
			ifType = varList.get(i).getInt();
			ifIndex = Integer.parseInt(varList.get(i).getInstance(1));

			if (isIfTypeToRemove(ifType))
				continue;

			ifIndexList.add(ifIndex);
		}

		return ifIndexList;
	}

	private List<NeIfMo> getIfMoList(MoSnmppable snmpnode) throws Exception {

		List<NeIfMo> portList = new ArrayList<NeIfMo>();
		NeIfMo port = null;
		List<Integer> ifIndexList = null;

		Map<String, IfAddr> addtable = getIpAddrTable2(snmpnode);
		if (addtable == null)
			throw new Exception("Timeout");

		// ifIndex 목록을 가져온다.
		ifIndexList = getIfIndexListFromNode(snmpnode);

		Logger.logger.debug("MO-NO-NODE({})INTF-SIZE({})", snmpnode.getMoNo(),
				(ifIndexList == null ? 0 : ifIndexList.size()));

		if (ifIndexList != null) {

			for (Integer ifIndex : ifIndexList) {

				for (int i = 0; i < 3; i++) {
					port = getInterface(snmpnode, ifIndex);
					if (port != null)
						break;
					Thread.sleep(200);
				}

				if (port != null && port.getIfType() > 0) {
					// 인터페이스명이 없는 경우가 발생. 이때는 무시합니다.
					if (port.getMoName() == null || port.getMoName().trim().length() == 0)
						continue;

					if (addtable != null) {
						setAddrTable(addtable, port);
					}

					portList.add(port);
				}

			}
		}

		return portList;
	}

	protected NeIfMo getInterface(MoSnmppable node, int ifIndex) throws Exception {

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
		} catch (Exception e) {
			Logger.logger.fail("MO-NO-NODE(" + node.getMoNo() + ")IF-INDEX(" + ifIndex + ") "
					+ e.getClass().getSimpleName() + ":" + e.getMessage());
			return null;
		}

		boolean isBit64 = false;
		String ifAlias = null;
		if (ovArr[8].isNull() == false) {
			try {
				ifAlias = ovArr[8].getValue();
			} catch (Exception e) {
			}
		}
		long ifSpeed = ovArr[3].getLong(-1);
		if (ovArr[4].isNull() == false) {
			if (ovArr[4].getLong(0) > 0)
				ifSpeed = ovArr[4].getLong(0) * 1000000L;
			isBit64 = true;
		}

		if (ovArr[5].isNull())
			isBit64 = false;

		NeIfMo port;
		try {
			port = ObjectUtil.makeObject4Use(NeIfMo.class);
		} catch (Exception e) {
			Logger.logger.error(e);
			port = new NeIfMo();
		}

		NeIfMo.set(port, ovArr[1].getValue(), ifAlias, ovArr[2].getValue(), null, null, ifSpeed, isBit64,
				ovArr[6].getInt(-1), ovArr[7].getInt(-1), ovArr[9].getMacAddress(), ovArr[0].getInt(-1), ifIndex);

		return port;
	}

	private Map<String, IfAddr> getIpAddrTable2(MoSnmppable snmpnode) {
		Map<String, IfAddr> map = new HashMap<String, IfAddr>();
		IfAddr addrBean = null;
		List<OidValue>[] varList;
		String oidArray[] = new String[] { IPMIB.ipAdEntIfIndex, IPMIB.ipAdEntAddr, IPMIB.ipAdEntNetMask,
				IPMIB.ipAdEntBcastAddr };
		try {
			varList = snmpwalk(snmpnode, oidArray);
		} catch (SnmpTimeoutException e) {
			return null;
		} catch (Exception e) {
			return map;
		}

		for (int index = 0; index < varList[0].size(); index++) {

			addrBean = new IfAddr();
			addrBean.index = varList[0].get(index).getInt(0);
			addrBean.ipaddr = varList[1].get(index).getValue();
			addrBean.netmask = varList[2].get(index).getValue();
			addrBean.calcBcastAddr(varList[3].get(index).getInt(0));

			map.put(addrBean.ipaddr, addrBean);

		}

		return map;
	}

	private boolean isIfTypeToRemove(int ifType) {
		if (ifTypes == null) {

			Object o = getPara("ignoreIftype");
			String para = o == null ? null : o.toString();
			if (para == null || para.length() == 0) {
				ifTypes = new int[] {};
			} else {
				String ss[] = para.split(",|;");
				ifTypes = new int[ss.length];
				for (int i = 0; i < ss.length; i++) {
					{
						try {
							ifTypes[i] = Integer.parseInt(ss[i]);
						} catch (Exception e) {
							Logger.logger.error(e);
							ifTypes[i] = -1;
						}
					}
				}
			}
		}

		for (int index : ifTypes) {
			if (index == ifType)
				return true;
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
	private void setAddrTable(Map<String, IfAddr> addtable, NeIfMo port) {
		try {

			for (IfAddr addrb : addtable.values()) {
				if (port.getIfIndex() == addrb.index) {
					port.setIpAddress(addrb.ipaddr);
					port.setIfNetmask(addrb.netmask);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

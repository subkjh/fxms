package com.daims.dfc.filter.config.cisco;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.MoCard;
import com.daims.dfc.common.mo.MoInterface;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoVLan;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * 
 * @author subkjh
 * 
 */
@SuppressWarnings("unused")
public class ConfigFilterCiscoVLan extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5645145477973781211L;

	private final String CISCO_PHYICAL_INDEX = ".1.3.6.1.4.1.9.9.109.1.1.1.1.2";

	/**
	 * Object vlanIndex <br>
	 * OID 1.3.6.1.4.1.9.5.1.9.2.1.1<br>
	 * Type VlanIndex <br>
	 * Permission read-only<br>
	 * Status deprecated<br>
	 * MIB CISCO-STACK-MIB ; - View Supporting Images this link will generate a
	 * new window<br>
	 * Description An index value that uniquely identifies the<br>
	 * Virtual LAN associated with this information.<br>
	 */
	private final String vlanIndex = ".1.3.6.1.4.1.9.5.1.9.2.1.1";

	/**
	 * Object vmVlan<br>
	 * OID 1.3.6.1.4.1.9.9.68.1.2.2.1.2<br>
	 * Type INTEGER<br>
	 * Permission read-write<br>
	 * Status current<br>
	 * Range 0 - 4095<br>
	 * MIB CISCO-VLAN-MEMBERSHIP-MIB ; - View Supporting Images this link will
	 * generate a new window<br>
	 * Description "The VLAN id of the VLAN the port is assigned to<br>
	 * when vmVlanType is set to static or dynamic.<br>
	 * This object is not instantiated if not applicable.<br>
	 * 
	 * The value may be 0 if the port is not assigned to a VLAN.<br>
	 * 
	 * If vmVlanType is static, the port is always assigned to a VLAN and the
	 * object may not be set to 0.<br>
	 * 
	 * If vmVlanType is dynamic the object's value is 0 if the port is currently
	 * not assigned to a VLAN. In addition, the object may be set to 0 only.<br>
	 */
	private final String vmVlan = ".1.3.6.1.4.1.9.9.68.1.2.2.1.2";

	/**
	 * Object vtpVlanName <br>
	 * OID 1.3.6.1.4.1.9.9.46.1.3.1.1.4<br>
	 * Type DisplayString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-VTP-MIB ; - View Supporting Images this link will generate a
	 * new window Description "The name of this VLAN. This name is used as the
	 * ELAN-name for an ATM LAN-Emulation segment of this VLAN." <br>
	 */
	private final String vtpVlanName = ".1.3.6.1.4.1.9.9.46.1.3.1.1.4";

	/**
	 * Object vtpVlanIfIndex<br>
	 * OID 1.3.6.1.4.1.9.9.46.1.3.1.1.18<br>
	 * Type InterfaceIndexOrZero <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB CISCO-VTP-MIB ; - View Supporting Images this link will generate a
	 * new window Description "The value of the ifIndex corresponding to this
	 * VLAN ID. If the VLAN ID does not have its corresponding interface, this
	 * object has the value of zero." <br>
	 */
	private final String vtpVlanIfIndex = ".1.3.6.1.4.1.9.9.46.1.3.1.1.18";

	/**
	 * Object vtpVlanState <br>
	 * OID 1.3.6.1.4.1.9.9.46.1.3.1.1.2<br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status current <br>
	 * Values 1 : operational<br>
	 * 2 : suspended<br>
	 * 3 : mtuTooBigForDevice<br>
	 * 4 : mtuTooBigForTrunk<br>
	 * 
	 * MIB CISCO-VTP-MIB ; - View Supporting Images this link will generate a
	 * new window Description "The state of this VLAN.<br>
	 * 
	 * The state 'mtuTooBigForDevice' indicates that this device cannot
	 * participate in this VLAN because the VLAN's MTU is larger than the device
	 * can support.<br>
	 * 
	 * The state 'mtuTooBigForTrunk' indicates that while this VLAN's MTU is
	 * supported by this device, it is too large for one or more of the device's
	 * trunk ports."<br>
	 */
	private final String vtpVlanState = ".1.3.6.1.4.1.9.9.46.1.3.1.1.2";

	/**
	 * Object vtpVlanType<br>
	 * OID 1.3.6.1.4.1.9.9.46.1.3.1.1.3<br>
	 * Type VlanType <br>
	 * 1:ethernet<br>
	 * 2:fddi<br>
	 * 3:tokenRing<br>
	 * 4:fddiNet<br>
	 * 5:trNet<br>
	 * 6:deprecated<br>
	 * 
	 * Permission read-only<br>
	 * Status current <br>
	 * MIB CISCO-VTP-MIB ; - View Supporting Images this link will generate a
	 * new window Description The type of this VLAN.<br>
	 */
	private final String vtpVlanType = ".1.3.6.1.4.1.9.9.46.1.3.1.1.3";

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {

			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoVLan.MO_CLASS);
			List<MoVLan> vlanList = vlan(node);
			if (vlanList != null && vlanList.size() > 0) {
				configMo.addMoListDetected(vlanList);
				makeVLanInterfaceBw(configMo.getMoList(MoInterface.class), vlanList);
			}

		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

		// 위 처리중 오류가 발생하더라고 다른 작업은 계속 수행을 위해 OK를 리턴합니다.

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoCard.MO_CLASS, MoVLan.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return vtpVlanName;
	}

	private void getMember(SnmpMo node, List<MoVLan> vlanList) throws Exception {

		List<OidValue> varList = snmpwalk(node, vmVlan);
		String vlanId;
		for (OidValue var : varList) {
			vlanId = var.getValue();
			for (MoVLan vlan : vlanList) {
				if (vlan.getVlanId().equals(vlanId)) {
					vlan.addIfIndex(var.getInstance(1));
				}
			}
		}
	}

	private List<MoVLan> getVlanIdList(SnmpMo node) throws Exception {

		List<MoVLan> vlanList = new ArrayList<MoVLan>();
		MoVLan moVLan;
		List<OidValue>[] varList = snmpwalk(node, vtpVlanName, vtpVlanIfIndex);

		OidValue name, ifIndex;
		for (int index = 0; index < varList[0].size(); index++) {

			name = varList[0].get(index);
			ifIndex = varList[1].get(index);

			// interface group의 ifIndex와 매핑되는 값이 있는 vlan만 등록합니다.
			if (ifIndex.getInt() > 0) {

				moVLan = new MoVLan();
				moVLan.setMoName(name.getValue());
				moVLan.setMoAlias(moVLan.getMoName());
				moVLan.setMoType(MoVLan.MO_CLASS);
				moVLan.setManaged(true);
				moVLan.setSnmpIndex(name.getInstance(1));
				moVLan.setVlanId(moVLan.getSnmpIndex());
				moVLan.setIfIndex(ifIndex.getInt(0));
				vlanList.add(moVLan);
			}
		}
		return vlanList;
	}

	private List<MoVLan> vlan(SnmpMo node) {
		try {
			List<MoVLan> vlanList = getVlanIdList(node);
			if (vlanList != null && vlanList.size() > 0) getMember(node, vlanList);
			return vlanList;
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			return null;
		}
	}

	private void makeVLanInterfaceBw(List<MoInterface> ifList, List<MoVLan> vlanList) {
		if (ifList == null || vlanList == null) return;

		int ifIndex;
		long bw = 0;
		MoInterface vlanInterface;

		for (MoVLan vlan : vlanList) {
			vlanInterface = null;
			for (MoInterface port : ifList) {
				if (port.getIfIndex() == vlan.getIfIndex()) {
					vlanInterface = port;
					break;
				}
			}
			if (vlanInterface.getIfSpeed() > 0) continue;

			if (vlan.getIfIndexList() == null) continue;
			bw = 0;
			for (String n : vlan.getIfIndexList()) {
				try {
					ifIndex = Integer.parseInt(n);
					for (MoInterface port : ifList) {
						if (port.getIfIndex() == ifIndex) {
							bw += port.getIfSpeed();
							System.out.println(vlan.getIfIndexList() + "   :  " + ifIndex + " : " + port.getIfSpeed());
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}
}

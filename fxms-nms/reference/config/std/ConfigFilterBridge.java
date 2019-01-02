package com.daims.dfc.filter.config.std;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.filter.config.std.beans.DfcBridge;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * 
 * @author subkjh
 * @since 3.0
 */
public class ConfigFilterBridge extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8178575574046150976L;

	// public static void main(String[] args) throws Exception {
	// ConfApi.api = new ConfApiImpl();
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("167.1.21.97", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	// node.setSysServices(76);
	// BridgeConfigFilter filter = new BridgeConfigFilter();
	// ConfigMo configMo = new ConfigMo(node);
	// filter.filter(configMo);
	//
	// System.out.println(configMo.getAttachObject(ConfigMo.ATTACH_OBJECT_KEY__LINK_BRIDGE));
	//
	// ConfApi.api.snmputil.close();
	// }

	/**
	 * Object dot1dTpFdbPort <br>
	 * OID 1.3.6.1.2.1.17.4.3.1.2 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status mandatory <br>
	 * MIB BRIDGE-MIB ; - View Supporting Images this link will generate a new
	 * window Description "Either the value '0', or the port number of the port
	 * on which a frame having a source address equal to the value of the
	 * corresponding instance of dot1dTpFdbAddress has been seen. A value of '0'
	 * indicates that the port number has not been learned but that the bridge
	 * does have some forwarding/filtering information about this address (e.g.
	 * in the dot1dStaticTable). Implementors are encouraged to assign the port
	 * value to this object whenever it is learned even for addresses for which
	 * the corresponding value of dot1dTpFdbStatus is not learned(3)."
	 */
	private final String dot1dTpFdbPort = ".1.3.6.1.2.1.17.4.3.1.2";

	/**
	 * Object dot1dTpFdbStatus <br>
	 * OID 1.3.6.1.2.1.17.4.3.1.3 <br>
	 * Type INTEGER <br>
	 * Permission read-only<br>
	 * Status mandatory <br>
	 * Values 1 : other<br>
	 * 2 : invalid<br>
	 * 3 : learned<br>
	 * 4 : self<br>
	 * 5 : mgmt<br>
	 * 
	 * MIB BRIDGE-MIB ; - View Supporting Images this link will generate a new
	 * window Description "The status of this entry. The meanings of the values
	 * are:
	 * 
	 * other(1) : none of the following. This would include the case where some
	 * other MIB object (not the corresponding instance of dot1dTpFdbPort, nor
	 * an entry in the dot1dStaticTable) is being used to determine if and how
	 * frames addressed to the value of the corresponding instance of
	 * dot1dTpFdbAddress are being forwarded.
	 * 
	 * invalid(2) : this entry is not longer valid (e.g., it was learned but has
	 * since aged-out), but has not yet been flushed from the table.
	 * 
	 * learned(3) : the value of the corresponding instance of dot1dTpFdbPort
	 * was learned, and is being used.
	 * 
	 * self(4) : the value of the corresponding instance of dot1dTpFdbAddress
	 * represents one of the bridge's addresses. The corresponding instance of
	 * dot1dTpFdbPort indicates which of the bridge's ports has this address.
	 * 
	 * mgmt(5) : the value of the corresponding instance of dot1dTpFdbAddress is
	 * also the value of an existing instance of dot1dStaticAddress."
	 */
	private final String dot1dTpFdbStatus = ".1.3.6.1.2.1.17.4.3.1.3";

	@SuppressWarnings("unchecked")
	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		// 브리지를 지원하는 장비가 아니면 무시
		if ((node.getSysServices() & 0x02) != 0x02) {
			return Ret.OK;
		}

		List<DfcBridge> bridgeList = null;
		try {
			bridgeList = getBridge(node, configMo);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}

		if (bridgeList != null && bridgeList.size() > 0) {
			Object valueObj = configMo.getAttachObject(ConfigMo.ATTACH_OBJECT_KEY__LINK_BRIDGE);
			if (valueObj instanceof List) {
				List<DfcBridge> listOld = (List<DfcBridge>) valueObj;
				listOld.addAll(bridgeList);
				configMo.addAttachObject(ConfigMo.ATTACH_OBJECT_KEY__LINK_BRIDGE, listOld);
			}
			else {
				configMo.addAttachObject(ConfigMo.ATTACH_OBJECT_KEY__LINK_BRIDGE, bridgeList);
			}

			return new Ret(bridgeList.size());
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoNode.MO_CLASS };
	}

	private List<DfcBridge> getBridge(MoNode node, ConfigMo configMo) throws Exception {

		List<OidValue> varList;
		try {
			varList = snmpwalk(node, dot1dTpFdbPort);
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			throw e;
		}

		List<DfcBridge> retList = new ArrayList<DfcBridge>();
		DfcBridge dfcBridge;
		String macAddress;

		for (int index = 0; index < varList.size(); index++) {

			macAddress = varList.get(index).getOid().replace(dot1dTpFdbPort + ".", "");

			dfcBridge = new DfcBridge();
			dfcBridge.setMoNoNode(node.getMoNo());
			dfcBridge.setIpAddress(node.getIpAddress());
			dfcBridge.setIfIndex(varList.get(index).getInt(-1));
			try {
				dfcBridge.setMacAddressPeer(makeMac(macAddress));
			}
			catch (Exception e) {
				dfcBridge.setMacAddressPeer(macAddress);
			}
			dfcBridge.setHstimeSync(node.getHstimeSync());

			retList.add(dfcBridge);
		}

		try {
			varList = snmpwalk(node, dot1dTpFdbStatus);
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			throw e;
		}

		for (int index = 0; index < varList.size(); index++) {
			if (varList.get(index).getInt(-1) == 2) {
				macAddress = varList.get(index).getOid().replace(dot1dTpFdbStatus + ".", "");
				try {
					macAddress = makeMac(macAddress);
				}
				catch (Exception e) {
				}
				for (int i = retList.size() - 1; i >= 0; i--) {
					if (retList.get(i).getMacAddressPeer().equals(macAddress)) {
						retList.remove(i);
						break;
					}
				}
			}
		}

		ServiceImpl.logger.debug("Bridge size=" + retList.size());

		return retList;
	}

	private String makeMac(String s) throws Exception {
		if (s == null || s.length() == 0) return "";
		String ss[] = s.split("\\.");
		if (ss.length != 6) return s;

		return String.format("%02x%02x.%02x%02x.%02x%02x", Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
				Integer.parseInt(ss[2]), Integer.parseInt(ss[3]), Integer.parseInt(ss[4]), Integer.parseInt(ss[5]));

	}

}

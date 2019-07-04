package fxms.nms.co.snmp.trap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import fxms.nms.api.TrapApi;
import fxms.nms.co.snmp.SnmpUtil;
import fxms.nms.co.snmp.exception.SnmpTimeoutException;
import fxms.nms.co.snmp.mib.IFMIB;
import fxms.nms.co.snmp.mib.IP_MIB;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.fxactor.conf.snmp.std.beans.AddrBean;
import fxms.nms.mo.NeIfMo;
import fxms.nms.mo.NeMo;
import fxms.nms.mo.property.MoSnmppable;

/**
 * MAC 주소 가져오는 클래스
 * 
 * @author subkjh
 * 
 */
public class GetMacAddress {

	public static void main(String[] args) throws Exception {

		GetMacAddress mac = new GetMacAddress();
		System.out.println(mac.getMacAddress("14.4.44.226"));
	}

	private final IFMIB MIB = new IFMIB();
	private final IP_MIB IPMIB = new IP_MIB();

	/**
	 * IP주소를 사용하고 있는 장비의 MAC 정보를 찾아 제공합니다.<br>
	 * 
	 * @param ipAddress
	 *            IP주소
	 * @return MAC주소
	 */
	public String getMacAddress(String ipAddress) {

		SnmpUtil snmpUtil = null;

		try {
			snmpUtil = SnmpUtil.getSnmpUtil("GetMac");

			NeMo node = TrapApi.makeNodeSnmp(ipAddress, NeMo.class);

			String mac;
			mac = getMacAddress(snmpUtil, node);
			if (mac != null && mac.length() > 0)
				return mac;
			return null;

		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		} finally {
			if (snmpUtil != null)
				snmpUtil.close();
		}
	}

	protected NeIfMo getInterface(SnmpUtil snmpUtil, MoSnmppable node, int ifidx) throws SnmpTimeoutException, Exception {
		String dotidx = "." + ifidx;
		NeIfMo port = ObjectUtil.getClass4Use(NeIfMo.class).newInstance();

		port.setIfIndex(ifidx);

		List<OidValue> valueList;
		try {
			valueList = snmpUtil.snmpget(node, MIB.ifPhysAddress + dotidx);
			port.setMacAddress(valueList.get(0).getMacAddress());
		} catch (SnmpTimeoutException e1) {
			throw e1;
		} catch (Exception e1) {
			throw e1;
		}

		return port;
	}

	private List<Integer> getIfIndexListFromNode(SnmpUtil snmpUtil, MoSnmppable node) throws Exception {

		List<OidValue> valueList = snmpUtil.snmpwalk(node, MIB.ifIndex);
		List<Integer> list = new ArrayList<Integer>();

		for (OidValue value : valueList) {
			list.add(value.getInt(0));
		}

		return list;
	}

	private Map<String, AddrBean> getIpAddrTable(SnmpUtil snmpUtil, MoSnmppable node) {
		Map<String, AddrBean> map = new HashMap<String, AddrBean>();
		AddrBean addrBean = null;
		List<OidValue>[] varList;
		String oidArray[] = new String[] { IPMIB.ipAdEntIfIndex, IPMIB.ipAdEntAddr, IPMIB.ipAdEntNetMask, IPMIB.ipAdEntBcastAddr };

		try {
			varList = snmpUtil.snmpwalk(node, oidArray);
		} catch (SnmpTimeoutException e) {
			return null;
		} catch (Exception e) {
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

	/**
	 * 노드의 IP주소가 사용하고 있는 MAC주소를 장비에서 찾아 제공합니다.
	 * 
	 * @param node
	 *            노드
	 * @return 노드가 사용하는 MAC 주소
	 */
	private String getMacAddress(SnmpUtil snmpUtil, NeMo node) {

		try {

			NeIfMo port;
			List<Integer> ifindexList;

			Map<String, AddrBean> addtable = getIpAddrTable(snmpUtil, node);
			if (addtable == null)
				return null;

			// ifIndex 목록을 가져온다.
			ifindexList = getIfIndexListFromNode(snmpUtil, node);

			for (int ifIndex : ifindexList) {

				port = getInterface(snmpUtil, node, ifIndex);

				if (port == null)
					continue;

				if (addtable != null) {
					setAddrTable(addtable, port);
				}

				if (node.getIpAddress().equals(port.getIpAddress())) {
					return port.getMacAddress();
				}

			}
			return null;

		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

	/**
	 * 인터페이스 IP 주소 , Subnetmak , broadcast 주소를 설정한다.
	 * 
	 * @param addrtbl
	 *            Hashtable 장비에서 로드한 모든 주소테이블
	 * @param ifBean
	 *            InterfaceInfo 설정을 할 인터페이스정보
	 */
	private void setAddrTable(Map<String, AddrBean> addtable, NeIfMo port) {
		try {
			for (AddrBean addrb : addtable.values()) {
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

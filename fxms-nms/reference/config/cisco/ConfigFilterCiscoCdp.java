package com.daims.dfc.filter.config.cisco;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.link.DfcLinkAuto;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoInterface;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * Cisco 장비에 대해 CDP 내역을 수집
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterCiscoCdp extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7698409370488833152L;

	/**
	 * CiscoNetworkProtocol <br>
	 * An indication of the type of address contained in the corresponding
	 * instance of cdpCacheAddress.
	 */
	@SuppressWarnings("unused")
	private final String cdpCacheAddressType = ".1.3.6.1.4.1.9.9.23.1.2.1.1.3";

	/**
	 * DisplayString<br>
	 * The Device-ID string as reported in the most recent CDP message. The
	 * zero-length string indicates no Device-ID field (TLV) was reported in the
	 * most recent CDP message.
	 */
	private final String cdpCacheDeviceId = ".1.3.6.1.4.1.9.9.23.1.2.1.1.6";

	/**
	 * Integer32<br>
	 * A unique value for each device from which CDP messages are being
	 * received.
	 */
	@SuppressWarnings("unused")
	private final String cdpCacheDeviceIndex = ".1.3.6.1.4.1.9.9.23.1.2.1.1.2";

	/**
	 * DisplayString <br>
	 * 
	 * The Port-ID string as reported in the most recent CDP message. This will
	 * typically be the value of the ifName object (e.g., 'Ethernet0'). The
	 * zero-length string indicates no Port-ID field (TLV) was reported in the
	 * most recent CDP message.
	 */
	private final String cdpCacheDevicePort = ".1.3.6.1.4.1.9.9.23.1.2.1.1.7";

	/**
	 * Integer32<br>
	 * Normally, the ifIndex value of the local interface. For 802.3 Repeaters
	 * for which the repeater ports do not have ifIndex values assigned, this
	 * value is a unique value for the port, and greater than any ifIndex value
	 * supported by the repeater; the specific port number in this case, is
	 * given by the corresponding value of cdpInterfacePort.
	 */
	@SuppressWarnings("unused")
	private final String cdpCacheIfIndex = ".1.3.6.1.4.1.9.9.23.1.2.1.1.1";

	@SuppressWarnings("unchecked")
	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		List<DfcLinkAuto> linkInfoList = null;
		try {
			linkInfoList = showCdpNeighbors(node, configMo);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}

		if (linkInfoList != null && linkInfoList.size() > 0) {
			Object valueObj = configMo.getAttachObject(ConfigMo.ATTACH_OBJECT_KEY__LINK_CDP);
			if (valueObj instanceof List) {
				List<DfcLinkAuto> listOld = (List<DfcLinkAuto>) valueObj;
				listOld.addAll(linkInfoList);
				configMo.addAttachObject(ConfigMo.ATTACH_OBJECT_KEY__LINK_CDP, listOld);
			}
			else {
				configMo.addAttachObject(ConfigMo.ATTACH_OBJECT_KEY__LINK_CDP, linkInfoList);
			}

			return new Ret(linkInfoList.size());
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return null;
	}

	@Override
	protected String getOidToCheck() {
		return cdpCacheDeviceId;
	}

	private List<DfcLinkAuto> showCdpNeighbors(MoNode node, ConfigMo configMo) throws Exception {

		String oidArray[] = new String[] { cdpCacheDeviceId, cdpCacheDevicePort };
		List<OidValue>[] varList;
		try {
			varList = snmpwalk(node, oidArray);
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			throw e;
		}

		List<DfcLinkAuto> retList = new ArrayList<DfcLinkAuto>();
		DfcLinkAuto linkInfo;
		Mo mo;
		for (int index = 0; index < varList[0].size(); index++) {
			linkInfo = new DfcLinkAuto();
			linkInfo.setMoNoNode(node.getMoNo());
			linkInfo.setIfIndex(Integer.parseInt(varList[0].get(index).getInstance(cdpCacheDeviceId).split("\\.")[0]));
			linkInfo.setSysNameDest(varList[0].get(index).getValue());
			linkInfo.setIfNameDest(varList[1].get(index).getValue());
			linkInfo.setLinkedType("CDP");
			linkInfo.setHstimeSync(node.getHstimeSync());

			linkInfo.setSysName(node.getSysName());
			mo = configMo.getMo4SnmpIndex(MoInterface.MO_CLASS, linkInfo.getIfIndex() + "");
			if (mo != null) {
				linkInfo.setMoNoIf(mo.getMoNo());
				if (mo instanceof MoInterface) {
					linkInfo.setIfName(((MoInterface) mo).getIfName());
				}
			}

			retList.add(linkInfo);
		}

		ServiceImpl.logger.debug("CDP size=" + retList.size());

		return retList;
	}

}

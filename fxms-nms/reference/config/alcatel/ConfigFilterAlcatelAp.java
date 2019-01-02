package com.daims.dfc.filter.config.alcatel;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.api.MoApi;
import com.daims.dfc.common.mo.MoAp;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * 알카텔 루슨트 WLAN 콘트롤러를 이용하여 AP 목록을 가져오는 필터
 * 
 * @author subkjh
 * 
 */

public class ConfigFilterAlcatelAp extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9196239888265701211L;

	private final String wlsxWlanAPTable = ".1.3.6.1.4.1.14823.2.2.1.5.2.1.4";
	private final String wlsxWlanAPEntry = wlsxWlanAPTable + ".1";

	// private final String wlanAPdot11aAntennaGain = wlsxWlanAPEntry + ".7";
	// private final String wlanAPGroupName = wlsxWlanAPEntry + ".4";
	private final String wlanAPIpAddress = wlsxWlanAPEntry + ".2";
	private final String wlanAPLocation = wlsxWlanAPEntry + ".14";
	// private final String wlanAPMacAddress = wlsxWlanAPEntry + ".1";
	// private final String wlanAPModel = wlsxWlanAPEntry + ".5";
	private final String wlanAPModelName = wlsxWlanAPEntry + ".13";
	private final String wlanAPName = wlsxWlanAPEntry + ".3";
	private final String wlanAPSerialNumber = wlsxWlanAPEntry + ".6";
	/** up(1), down(2) */
	private final String wlanAPStatus = wlsxWlanAPEntry + ".19";

	/*
	 * wlanAPdot11gAntennaGain 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.8
	 * wlsxWlanAPEntry 8 wlanAPNumRadios 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.9
	 * wlsxWlanAPEntry 9 wlanAPEnet1Mode 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.10
	 * wlsxWlanAPEntry 10 wlanAPIpsecMode 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.11
	 * wlsxWlanAPEntry 11 wlanAPUpTime 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.12
	 * wlsxWlanAPEntry 12 wlanAPModelName 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.13
	 * wlsxWlanAPEntry 13 wlanAPLocation 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.14
	 * wlsxWlanAPEntry 14 wlanAPBuilding 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.15
	 * wlsxWlanAPEntry 15 wlanAPFloor 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.16
	 * wlsxWlanAPEntry 16 wlanAPLoc 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.17
	 * wlsxWlanAPEntry 17 wlanAPExternalAntenna
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.18 wlsxWlanAPEntry 18 wlanAPStatus
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.19 wlsxWlanAPEntry 19
	 * wlanAPNumBootstraps 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.20 wlsxWlanAPEntry
	 * 20 wlanAPNumReboots 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.21 wlsxWlanAPEntry
	 * 21 wlanAPUnprovisioned 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.22
	 * wlsxWlanAPEntry 22 wlanAPMonitorMode 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.23
	 * wlsxWlanAPEntry 23 wlanAPFQLNBuilding
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.24 wlsxWlanAPEntry 24 wlanAPFQLNFloor
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.25 wlsxWlanAPEntry 25 wlanAPFQLN
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.26 wlsxWlanAPEntry 26 wlanAPFQLNCampus
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.27 wlsxWlanAPEntry 27 wlanAPLongitude
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.28 wlsxWlanAPEntry 28 wlanAPLatitude
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.29 wlsxWlanAPEntry 29 wlanAPAltitude
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.30 wlsxWlanAPEntry 30 wlanAPMeshRole
	 * 1.3.6.1.4.1.14823.2.2.1.5.2.1.4.1.31 wlsxWlanAPEntry 31
	 */

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		if (containsMoClass(moClassArr, MoAp.MO_CLASS) == false) return Ret.OK;

		List<MoAp> moApList = new ArrayList<MoAp>();
		String oids[] = new String[] { wlanAPIpAddress, wlanAPName, wlanAPModelName, wlanAPSerialNumber,
				wlanAPLocation, wlanAPStatus };
		try {
			List<OidValue>[] varList = snmpwalk(node, oids);
			MoAp ap;
			for (int index = 0; index < varList[0].size(); index++) {
				ap = MoApi.createMo(MoAp.class);
				ap.setFaultThrNo(0);
				ap.setSnmpIndex(varList[0].get(index).getInstance(wlanAPIpAddress));
				ap.setMacAddress(varList[0].get(index).getInstance(wlanAPIpAddress));
				ap.setIpAddress(varList[0].get(index).getValue());
				ap.setMoName(varList[1].get(index).getValue());
				ap.setMoAlias(ap.getMoName());
				ap.setMoType(ap.getMoClass());
				ap.setModelName(varList[2].get(index).getValue());
				ap.setSerialNo(varList[3].get(index).getValue());
				ap.setLinkDescr(varList[4].get(index).getValue());
				ap.setStatusLink(varList[5].get(index).getInt() == 1 ? 1 : 0);
				ap.setManaged(true);

				moApList.add(ap);
			}

			if (moApList.size() > 0) configMo.addMoListDetected(moApList);

		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}
		return new Ret(moApList.size());
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoAp.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return wlanAPIpAddress;
	}

}

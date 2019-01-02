package com.daims.dfc.filter.config.alcatel;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;

import com.daims.dfc.common.mo.MoMemory;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.mib.XYLAN_HEALTH_MIB;

/**
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterAlcatelMem extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -212192704541356379L;
	
	private final XYLAN_HEALTH_MIB MIB = new XYLAN_HEALTH_MIB();


	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		if (containsMoClass(moClassArr, MoMemory.MO_CLASS) == false) return Ret.OK;

		OidValue ovArr[];
		try {
			ovArr = snmpget(node, MIB.healthDeviceMemoryTotal);
		} catch (Exception e) {
			return Ret.OK;
		}

		// 크기가 -1이면 무시합니다. ( 2012.11.06 김종훈 )
		long size = ovArr[0].getLong(-1);
		if (size < 0) return new Ret(0);

		MoMemory memory = MoMemory.make(MoMemory.MEMORY_NAME, MoMemory.MEMORY_TYPE_REAL, ovArr[0].getLong(-1), "");
		configMo.addMoDetected(memory);

		return new Ret(1);
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoMemory.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return MIB.healthDeviceMemoryTotal;
	}

}

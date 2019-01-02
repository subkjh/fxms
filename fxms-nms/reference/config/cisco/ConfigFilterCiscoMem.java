package com.daims.dfc.filter.config.cisco;

import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;

import com.daims.dfc.common.mo.MoMemory;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * 시스코 장비으 메모리 용량을 가져오는 필터
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterCiscoMem extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6867292194385057092L;

	/**
	 * Gauge32 <b> Indicates the number of bytes from the memory pool that are
	 * currently unused on the managed device. Note that the sum of
	 * ciscoMemoryPoolUsed and ciscoMemoryPoolFree is the total amount of memory
	 * in the pool
	 */

	private final String ciscoMemoryPoolFree = ".1.3.6.1.4.1.9.9.48.1.1.1.6";

	/**
	 * Gauge32 <br>
	 * Indicates the number of bytes from the memory pool that are currently in
	 * use by applications on the managed device
	 */
	private final String ciscoMemoryPoolUsed = ".1.3.6.1.4.1.9.9.48.1.1.1.5";

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		List<OidValue>[] valueList;
		try {
			valueList = snmpwalk(node, ciscoMemoryPoolFree, ciscoMemoryPoolUsed);
		} catch (Exception e) {
			return Ret.OK;
		}

		long memTotalBytes = 0;
		if (valueList == null || valueList[0].size() == 0) return Ret.OK;

		configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoMemory.MO_CLASS);

		// free + used
		for (int index = 0; index < valueList[0].size(); index++) {
			memTotalBytes += valueList[0].get(index).getLong(0);
			memTotalBytes += valueList[1].get(index).getLong(0);
		}

		MoMemory memory = MoMemory.make(MoMemory.MEMORY_NAME, MoMemory.MEMORY_TYPE_REAL, memTotalBytes, "");
		configMo.addMoDetected(memory);

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoMemory.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return ciscoMemoryPoolFree;
	}

}

package com.daims.dfc.filter.config.std;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.RC;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpNotFoundOidException;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;
import com.daims.dfc.mib.RMON2_MIB;

/**
 * probeSoftwareRev를 이용하여 SW, FW 버전를 가져오는 필터
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterProbeVer extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4724730426975723291L;

	private final RMON2_MIB OID = new RMON2_MIB();

	public ConfigFilterProbeVer() {
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {
			OidValue ovArr[] = snmpget(node,
					new String[] { OID.probeSoftwareRev, OID.probeHardwareRev });

			node.setVerSw(ovArr[0].getValue());
			node.setVerHw(ovArr[1].getValue());
			ServiceImpl.logger.debug("probeSoftwareRev=" + node.getVerSw() + ", probeHardwareRev=" + node.getVerFw());

			return Ret.OK;

		}
		catch (SnmpTimeoutException e) {
			ServiceImpl.logger.fail(e.getMessage() + " - " + node.getIpAddress());
			return new Ret(RC.c007_timeout, node.getIpAddress());
		}
		catch (SnmpNotFoundOidException e) {
			return new Ret(RC.c013_notFound, e.getMessage());
		}
		catch (Exception e) {
			return new Ret(RC.c999_error, e.getMessage());
		}

	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoNode.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return OID.probeSoftwareRev;
	}

}

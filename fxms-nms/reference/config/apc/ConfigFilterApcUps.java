package com.daims.dfc.filter.config.apc;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mo.MoNodeUps;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

public class ConfigFilterApcUps extends ConfigFilterSnmpNode {
	/**
	 * Snmp를 이용하여 APC 장비 정보(모델명, 배터리 상태, UPS와 통신상태)를 가져옵니다.
	 * 
	 * @author kwak
	 */

	/**
	 * UPS 와 통신상태 The status of agent's communication with UPS. ok(1),noComm(2)
	 */
	private final static String APC_COMM_STATUS = ".1.3.6.1.4.1.318.1.1.1.8.1.0";

	/**
	 * UPS 배터리 상태 The status of the UPS batteries. A batteryLow(3) value
	 * indicates the UPS will be unable to sustain the current load, and its
	 * services will be lost if power is not restored. The amount of run time in
	 * reserve at the time of low battery can be configured by the
	 * upsAdvConfigLowBatteryRunTime. unknown(1),batteryNormal(2),batteryLow(3)
	 */
	private final static String APC_UPS_Battery_Status = ".1.3.6.1.4.1.318.1.1.1.2.1.1.0";

	/**
	 * UPS모델명 The UPS model name (e.g. 'APC Smart-UPS 600').
	 */
	private final static String APC_UPS_Name = ".1.3.6.1.4.1.318.1.1.1.1.1.1.0";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8629692570358453591L;

	public ConfigFilterApcUps() {
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		ServiceImpl.logger.debug(configMo);

		if ((configMo.getNode() instanceof MoNodeUps) == false) return Ret.OK;
		MoNodeUps node = (MoNodeUps) configMo.getNode();
		if (node.isSnmp() == false) return Ret.OK;

		OidValue ovArr[];
		int status;

		try {
			ovArr = snmpget(node, APC_UPS_Name, APC_UPS_Battery_Status, APC_COMM_STATUS);

			node.setUpsName(ovArr[0].getValue());

			status = ovArr[1].getInt();

			if (status == 1) {
				node.setStatusBattery("Unknown");
			}
			else if (status == 2) {
				node.setStatusBattery("Battery normal");
			}
			else if (status == 3) {
				node.setStatusBattery("Battery Low");
			}

			status = ovArr[2].getInt();

			if (status == 1) {
				node.setStatusComm("ok");
			}
			else if (status == 2) {
				node.setStatusComm("noComm");
			}

		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoNodeUps.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return APC_UPS_Name;
	}

}

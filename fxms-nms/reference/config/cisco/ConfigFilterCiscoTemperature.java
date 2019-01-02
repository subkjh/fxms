package com.daims.dfc.filter.config.cisco;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mib.MibCiscoEnvMonObjects;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoSensor;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * CISCO 장비의 온도를 객체로 등록합니다.
 * 
 * @author subkjh
 * @since 2013.01.11
 */
public class ConfigFilterCiscoTemperature extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4656083266210230066L;

	private MibCiscoEnvMonObjects OID;

	public ConfigFilterCiscoTemperature() {
		OID = new MibCiscoEnvMonObjects();
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {
		ServiceImpl.logger.debug(configMo);

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		List<OidValue>[] valueList;
		try {
			valueList = snmpwalk(node, OID.ciscoEnvMonTemperatureStatusDescr, OID.ciscoEnvMonTemperatureThreshold);
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			return Ret.OK;
		}

		configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoSensor.MO_CLASS);

		if (valueList == null || valueList[0].size() == 0) return Ret.OK;

		List<MoSensor> tempList = new ArrayList<MoSensor>();
		MoSensor temp;
		String snmpIndex;

		for (int index = 0; index < valueList[0].size(); index++) {

			snmpIndex = valueList[0].get(index).getInstance(1);

			temp = new MoSensor();
			temp.setManaged(true);
			temp.setMoName(valueList[0].get(index).getValue());
			temp.setMoAlias(temp.getMoName());
			temp.setMoType("TEMPERATURE");
			temp.setSnmpIndex(snmpIndex);
			// temp.setTempThreshold(valueList[1].get(index).getInt());

			tempList.add(temp);
		}

		configMo.addMoListDetected(tempList);

		return new Ret(tempList.size());
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoSensor.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return OID.ciscoEnvMonTemperatureStatusDescr;
	}

}

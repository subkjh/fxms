package com.daims.dfc.filter.config.netbotz;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mib.MibNetbotz;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoSensor;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

public class ConfigFilterNetbotzFms extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3619906845365730517L;

	// public static void main(String[] args) throws Exception {
	//
	// try {
	// DaoFactory.getInstance().addDataBase(new
	// File("deploy/conf/databases.xml"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// FaultApi.api = new FaultApiImpl();
	// FaultApi.api.init();
	// CLogger.logger.setLevel(LEVEL.raw);
	// MoApi.api = new MoApiImpl();
	// ConfApi.api = new ConfApiImpl();
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	// ConfApi.api.snmputil.setLogger(CLogger.logger);
	//
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("203.228.204.4", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	// Netbotz filter = new Netbotz();
	// ConfigMo configMo = new ConfigMo(node);
	// filter.filter(configMo);
	// for (Mo mo : configMo.moList()) {
	// System.out.println(mo);
	// }
	//
	// ConfApi.api.snmputil.close();
	// System.exit(0);
	// }

	private MibNetbotz MIB;

	public ConfigFilterNetbotzFms() {
		MIB = new MibNetbotz();
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		if (containsMoClass(moClassArr, MoSensor.MO_CLASS) == false) return Ret.OK;

		try {
			List<MoSensor> moList = new ArrayList<MoSensor>();

			find(node, moList, new String[] { MIB.tempSensorId, MIB.tempSensorValue, MIB.tempSensorLabel }, "SENSOR_TEMP",
					"numeric");
			find(node, moList, new String[] { MIB.humiSensorId, MIB.humiSensorValue, MIB.humiSensorLabel }, "SENSOR_HUMI",
					"numeric");
			find(node, moList, new String[] { MIB.dewPointSensorId, MIB.dewPointSensorValue, MIB.dewPointSensorLabel },
					"SENSOR_DP", "numeric");
			find(node, moList, new String[] { MIB.audioSensorId, MIB.audioSensorValue, MIB.audioSensorLabel }, "SensorAudio",
					"numeric");
			find(node, moList, new String[] { MIB.airFlowSensorId, MIB.airFlowSensorValue, MIB.airFlowSensorLabel }, "SENSOR_AF",
					"numeric");
			find(node, moList, new String[] { MIB.ampDetectSensorId, MIB.ampDetectSensorValue, MIB.ampDetectSensorLabel },
					"SensorAmpDetect", "numeric");
			findOtherNumericSensor(node, moList);

			find(node, moList, new String[] { MIB.cameraMotionSensorId, MIB.cameraMotionSensorValue, MIB.cameraMotionSensorLabel },
					"SENSOR_CM", "state");
			find(node, moList, new String[] { MIB.doorSwitchSensorId, MIB.doorSwitchSensorValue, MIB.doorSwitchSensorLabel },
					"SENSOR_DS", "state");
			find(node, moList, new String[] { MIB.dryContactSensorId, MIB.dryContactSensorValue, MIB.dryContactSensorLabel },
					"SensorDryContact", "state");

			findOtherStateSensor(node, moList);

			if (moList != null && moList.size() > 0) {
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoSensor.MO_CLASS);
				configMo.addMoListDetected(moList);
				return new Ret(moList.size());
			}
		} catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}
		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoSensor.MO_CLASS };
	}

	private boolean exist(List<MoSensor> moList, String moName) {
		for (Mo mo : moList) {
			if (mo.getMoName().equalsIgnoreCase(moName)) return true;
		}
		return false;
	}

	private void find(SnmpMo node, List<MoSensor> moList, String oidArray[], String moType, String sensorType) {
		List<OidValue>[] valueList;
		try {

			valueList = snmpwalk(node, oidArray);

			for (int index = 0; index < valueList[0].size(); index++) {
				MoSensor sensor = makeMo(valueList, index, oidArray[1], moType, sensorType);
				moList.add(sensor);
			}
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}
	}

	private void findOtherNumericSensor(SnmpMo node, List<MoSensor> moList) {
		List<OidValue>[] valueList;
		try {

			valueList = snmpwalk(node //
					, MIB.otherNumericSensorId //
					, MIB.otherNumericSensorValue //
					, MIB.otherNumericSensorLabel //
					);

			for (int index = 0; index < valueList[0].size(); index++) {
				MoSensor sensor = makeMo(valueList, index, MIB.otherNumericSensorValue, "SENSOR_NUM", "numeric");
				if (exist(moList, sensor.getMoName()) == false) {
					moList.add(sensor);
				}
			}
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}
	}

	private void findOtherStateSensor(SnmpMo node, List<MoSensor> moList) {
		List<OidValue>[] valueList;
		try {

			valueList = snmpwalk(node //
					, MIB.otherStateSensorId //
					, MIB.otherStateSensorValue //
					, MIB.otherStateSensorLabel //
					);

			for (int index = 0; index < valueList[0].size(); index++) {
				MoSensor sensor = makeMo(valueList, index, MIB.otherStateSensorValue, "SENSOR_STATE", "state");

				if (exist(moList, sensor.getMoName()) == false) {
					moList.add(sensor);
				}

			}
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}
	}

	private MoSensor makeMo(List<OidValue>[] valueList, int index, String oid, String moType, String sensorType) {
		MoSensor sensor = new MoSensor();
		sensor.setMoType(moType);
		sensor.setSensorKind(sensorType);
		sensor.setSnmpIndex(valueList[0].get(index).getInstance(1));
		sensor.setMoName(valueList[0].get(index).getValue());
		sensor.setStatusSensor(valueList[1].get(index).getValue());
		sensor.setMoAlias(valueList[2].get(index).getValue());
		sensor.setOidValue(oid);

		sensor.setFaultThrNo(0);

		return sensor;
	}

}

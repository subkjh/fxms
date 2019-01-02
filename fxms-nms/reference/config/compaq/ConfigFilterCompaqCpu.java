package com.daims.dfc.filter.config.compaq;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import subkjh.dao.control.DaoFactory;
import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.api.MoApi;
import com.daims.dfc.common.mo.MoCpu;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * 컴팩 서버의 CPU 정보를 가져오는 필터
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterCompaqCpu extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 70830891583100602L;

	public static void main(String[] args) {
		try {
			DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		MoNode node = MoApi.makeNodeSnmp("10.80.1.134", 161, 1, "public", "", MoNode.class);

		ConfigFilterCompaqCpu f = new ConfigFilterCompaqCpu();
		try {
			f.filter(new ConfigMo(node), null, null);
		}
		catch (TimeoutException e) {
			e.printStackTrace();
		}
		catch (NotFoundException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	protected final String cpqSeCpuDesigner = ".1.3.6.1.4.1.232.1.2.2.1.1.8";
	protected final String cpqSeCpuEntry = ".1.3.6.1.4.1.232.1.2.2.1.1";
	protected final String cpqSeCpuExtSpeed = ".1.3.6.1.4.1.232.1.2.2.1.1.7";
	protected final String cpqSeCpuName = ".1.3.6.1.4.1.232.1.2.2.1.1.3";

	protected final String cpqSeCpuSlot = ".1.3.6.1.4.1.232.1.2.2.1.1.2";
	protected final String cpqSeCpuSocketNumber = ".1.3.6.1.4.1.232.1.2.2.1.1.9";
	/**
	 * This is the internal speed in megahertz of this processor. Zero will be
	 * returned if this value is not available.
	 */
	protected final String cpqSeCpuSpeed = ".1.3.6.1.4.1.232.1.2.2.1.1.4";
	protected final String cpqSeCpuStatus = ".1.3.6.1.4.1.232.1.2.2.1.1.6";
	protected final String cpqSeCpuStep = ".1.3.6.1.4.1.232.1.2.2.1.1.5";
	protected final String cpqSeCpuTable = ".1.3.6.1.4.1.232.1.2.2.1";
	protected final String cpqSeCpuThreshPassed = ".1.3.6.1.4.1.232.1.2.2.1.1.10";

	protected final String cpqSeCpuUnitIndex = ".1.3.6.1.4.1.232.1.2.2.1.1.1";

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		List<OidValue>[] valueList;
		try {
			valueList = snmpwalk(node, cpqSeCpuName, cpqSeCpuSpeed);
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			return Ret.OK;
		}
		MoCpu cpu;

		List<MoCpu> cpuList = new ArrayList<MoCpu>();

		for (int index = 0; index < valueList[0].size(); index++) {

			cpu = new MoCpu(valueList[0].get(index).getValue(), valueList[0].get(index).getInt(), valueList[0].get(
					index).getInstance(1));
			cpuList.add(cpu);

			ServiceImpl.logger.debug(cpu.getMoName());
		}

		if (cpuList != null && cpuList.size() > 0) {

			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoCpu.MO_CLASS);

			configMo.addMoListDetected(cpuList);
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoCpu.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return cpqSeCpuUnitIndex;
	}

}

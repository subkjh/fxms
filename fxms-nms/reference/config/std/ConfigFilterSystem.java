package com.daims.dfc.filter.config.std;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.RC;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;
import subkjh.utils.DateUtil;

import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpNotFoundOidException;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;
import com.daims.dfc.mib.SNMPV2_MIB;

/**
 * MIB SYSTEM 그룹 정보를 이용하여 구성정보를 가져오는 전반부 필터입니다.
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterSystem extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9110940880238409553L;

	private final SNMPV2_MIB MIB = new SNMPV2_MIB();

	// public static void main(String[] args) throws Exception {
	// ConfApi.api = new ConfApiImpl();
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("10.4.15.81", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	// // MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("14.4.44.156", 161, 1,
	// // "public", MoNodeSnmpImpl.class);
	// SystemConfigFilter filter = new SystemConfigFilter();
	// filter.filter(new ConfigMo(node));
	// ConfApi.api.snmputil.close();
	// }

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {
			OidValue ovArr[] = snmpget(node, MIB.sysName, MIB.sysDescr, MIB.sysLocation, MIB.sysObjectID,
					MIB.sysUpTime, MIB.sysServices
			);

			node.setSysName(ovArr[0].getValue());
			node.setSysDescr(ovArr[1].getValue());
			node.setSysLocation(ovArr[2].getValue());
			node.setSysObjectId(ovArr[3].getValue());
			node.setSysUptime(ovArr[4].getLong(0));
			node.setSysUptimeChgUxtime(System.currentTimeMillis() / 1000L);
			node.setSysServices(ovArr[5].getInt(0));

			node.setHstimeSync(DateUtil.getHTime());

			// 32bit reset 되는 문제를 어떻게 해결할 것인가???
			// System.out.println(node.getSysUptime() + ", " +
			// SystemGroup.getTimeTicks(node.getSysUptime()));

			if (node.getMoName() == null) node.setMoName(node.getSysName());

			node.setStatusSnmp(Mo.STATUS_ONLINE);

			return Ret.OK;
		}
		catch (SnmpTimeoutException e) {
			node.setStatusSnmp(Mo.STATUS_OFFLINE);
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
		return null;
	}

	@Override
	protected String getOidToCheck() {
		return null;
	}
}

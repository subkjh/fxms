package com.daims.dfc.filter.config.hp;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mib.MibHp;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoProcess;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;

public class ConfigFilterHpProcess extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3657841829470692176L;

	// public static void main(String[] args) throws Exception {
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("12.4.6.234", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	// ServiceImpl.logger.setLevel(LEVEL.info);
	//
	// FilterConfigHpProcess filter = new FilterConfigHpProcess();
	// ConfigMo configMo = new ConfigMo(node);
	// filter.filter(configMo);
	// System.out.println(configMo.getChildrenInfo());
	// for (Mo mo : configMo.moList(MoProcess.MO_CLASS)) {
	// System.out.println(mo);
	// }
	// // System.out.println(ByteUtil.toObject(bytes));
	// ConfApi.api.snmputil.close();
	//
	// }

	private MibHp hpOid;

	public ConfigFilterHpProcess() {
		hpOid = new MibHp();
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		int faultThrNo = 0;
		String name_path_para[];

		String oids[] = new String[] { hpOid.processCmd, hpOid.processCPU };

		MoProcess moProcess;
		List<MoProcess> moList = new ArrayList<MoProcess>();

		try {
			List<OidValue> listArray[] = snmpwalk(node, oids);

			OID: for (int index = 0; index < listArray[0].size(); index++) {

				if (listArray[0].get(index).isNull()) continue;

				// System.out.println(listArray[0].get(index).getInstance(1) +
				// "\t" + listArray[0].get(index).getValue()
				// + "\t" + listArray[1].get(index).getValue());

				name_path_para = hpOid.splitProcessName_Path_Para(listArray[0].get(index).getValue());

				moProcess = new MoProcess();
				moProcess.setBeanStatus(NotiBean.BEAN_STATUS_UNKNOWN);
				moProcess.setHstimeChg(node.getHstimeChg());
				moProcess.setHstimeReg(0);
				moProcess.setManaged(true);
				moProcess.setMoAlias(name_path_para[0]);
				moProcess.setMoClass(MoProcess.MO_CLASS);
				moProcess.setMoName(listArray[0].get(index).getInstance(1) + "_" + name_path_para[0]);
				moProcess.setMoNo(0);
				moProcess.setMoNoUpper(0);
				moProcess.setMoType(MoProcess.MO_CLASS);
				moProcess.setSnmpIndex(listArray[0].get(index).getInstance(1));
				moProcess.setSwRunName(name_path_para[0]);
				moProcess.setSwRunParameters(name_path_para[2]);
				moProcess.setSwRunPath(name_path_para[1]);
				moProcess.setUserGroupNo(0);
				moProcess.setUserNo(0);
				moProcess.setFaultThrNo(faultThrNo);

				for (MoProcess proc : moList) {
					if (proc.match(moProcess)) {
						proc.addPid(0);
						continue OID;
					}
				}

				moList.add(moProcess);

				try {
					moProcess.setCpuTime(listArray[1].get(index).getLong(0));
				}
				catch (Exception e) {
				}

			}
		}
		catch (SnmpTimeoutException e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}

		configMo.addMoListDetected(moList);

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoProcess.MO_CLASS };
	}

	@Override
	public boolean isSyncFilter() {
		return false;
	}

	@Override
	protected String getOidToCheck() {
		return hpOid.processPID;
	}

}

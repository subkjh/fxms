package com.daims.dfc.filter.config.std;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.CClass;
import subkjh.log.Ret;

import com.daims.dfc.api.MoApi;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoProcess;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;
import com.daims.dfc.mib.HOST_RESOURCES_MIB;

/**
 * 실행중인 어플리케이션을 가져옵니다.
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterHostSwRun extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1049013344088690220L;

	public static void main(String[] args) throws Exception {
		// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
		MoNode node = MoApi.makeNodeSnmp("167.1.21.32", 161, 1, "public", MoNode.class);

		ConfigFilterHostSwRun filter = new ConfigFilterHostSwRun();
		ConfigMo configMo = new ConfigMo(node);
		filter.filter(configMo, null, null);
		for (Mo mo : configMo.getMoListAll()) {
			System.out.println(CClass.toString(mo));
		}

		System.out.println(configMo.getMoListAll().size());
	}

	private final HOST_RESOURCES_MIB MIB = new HOST_RESOURCES_MIB();

	public ConfigFilterHostSwRun() {

	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		List<OidValue> valueList = snmpwalk(node, MIB.hrSWRunIndex);
		OidValue swArr[];
		String oidArray[];
		String index;
		MoProcess mo;
		int pid;
		List<MoProcess> moList = new ArrayList<MoProcess>();

		OID: for (OidValue oid : valueList) {
			index = oid.getInstance(1);

			oidArray = new String[] { MIB.hrSWRunName + "." + index//
			, MIB.hrSWRunPath + "." + index//
			, MIB.hrSWRunParameters + "." + index//
			, MIB.hrSWRunPerfCPU + "." + index//
			, MIB.hrSWRunPerfMem + "." + index//
			};

			try {
				swArr = snmpget(node, oidArray);
			}
			catch (SnmpTimeoutException e) {
				throw e;
			}
			catch (Exception e) {
				continue;
			}

			String runName = swArr[0].getValue();

			pid = Integer.parseInt(index);
			mo = MoApi.createMo(MoProcess.class);
			mo = MoProcess.set(mo, runName, runName, swArr[1].getValue(), swArr[2].getValue(), pid);
			mo.setMoName(MoProcess.makeMoName(mo.getPid(), mo.getSwRunName()));
			mo.setSnmpIndex(index);
			mo.addPid(pid);
			mo.setCpuTime(swArr[3].getLong() * 10000);
			mo.setMemUsed(swArr[4].getLong());

			for (MoProcess proc : moList) {
				if (proc.match(mo)) {
					proc.addPid(pid);
					continue OID;
				}
			}

			moList.add(mo);

		}

		configMo.addMoListDetected(moList);

		return Ret.OK;
	}

	/*
	 * public Ret filter2(ConfigMo configMo, String... moClassArray) throws
	 * TimeoutException, NotFoundException, Exception {
	 * 
	 * MoNodeSnmpImpl node = getSnmpNode(configMo); if (node == null) return
	 * Ret.OK;
	 * 
	 * if (containsMoClass(moClassArray, MoProcess.MO_CLASS) == false) return
	 * Ret.OK;
	 * 
	 * int faultThrNo = 0;
	 * 
	 * String oids[] = new String[] {
	 * HOST_RESOURCES_MIB.hrSWRun.hrSWRunTable.OID_hrSWRunIndex, //
	 * HOST_RESOURCES_MIB.hrSWRun.hrSWRunTable.OID_hrSWRunName, //
	 * HOST_RESOURCES_MIB.hrSWRun.hrSWRunTable.OID_hrSWRunPath, //
	 * HOST_RESOURCES_MIB.hrSWRun.hrSWRunTable.OID_hrSWRunParameters };
	 * 
	 * MoProcess mo; Map<String, MoProcess> map = new HashMap<String,
	 * MoProcess>();
	 * 
	 * try { List<OidValue> valueList[] = getSnmpUtil().snmpwalk(node, oids);
	 * for (int index = 0; index < valueList[0].size(); index++) {
	 * 
	 * mo = new MoProcess(); mo.setBeanStatus(NotiBean.BEAN_STATUS_UNKNOWN);
	 * mo.setFaultThrNo(0); mo.setHstimeChg(node.getHstimeChg());
	 * mo.setHstimeReg(0); mo.setManaged(true);
	 * mo.setMoAlias(valueList[1].get(index).getValue());
	 * mo.setMoClass(MoProcess.MO_CLASS);
	 * mo.setMoName(valueList[0].get(index).getValue() + "_" +
	 * valueList[1].get(index).getValue()); mo.setMoNo(0); mo.setMoNoUpper(0);
	 * mo.setMoType(MoProcess.MO_CLASS);
	 * mo.setSnmpIndex(valueList[0].get(index).getValue());
	 * mo.setSwRunName(valueList[1].get(index).getValue());
	 * mo.setSwRunPath(valueList[2].get(index).getValue());
	 * mo.setSwRunParameters(valueList[3].get(index).getValue());
	 * mo.setUserGroupNo(0); mo.setUserNo(0); mo.setFaultThrNo(faultThrNo);
	 * 
	 * map.put(mo.getSnmpIndex(), mo);
	 * 
	 * } } catch (Exception e) { ServiceImpl.logger.error(e); return new Ret(e);
	 * }
	 * 
	 * oids = new String[] {
	 * HOST_RESOURCES_MIB.hrSWRunPerf.hrSWRunPerfTable.OID_hrSWRunIndex, //
	 * HOST_RESOURCES_MIB.hrSWRunPerf.hrSWRunPerfTable.OID_hrSWRunPerfCPU, //
	 * HOST_RESOURCES_MIB.hrSWRunPerf.hrSWRunPerfTable.OID_hrSWRunPerfMem };
	 * 
	 * try { List<OidValue> valueList[] = getSnmpUtil().snmpwalk(node, oids);
	 * for (int index = 0; index < valueList[0].size(); index++) { mo =
	 * map.get(valueList[0].get(index).getValue());
	 * 
	 * if (mo != null) {
	 * mo.setCpuCentiSeconds(valueList[1].get(index).getLong());
	 * mo.setMemoryAllocated(valueList[2].get(index).getLong());
	 * configMo.addMoDetected(mo); } else { //
	 * System.out.println(objects[0].toString()); } }
	 * 
	 * } catch (Exception e) { ServiceImpl.logger.error(e); return new Ret(e); }
	 * 
	 * return Ret.OK; }
	 */
	@Override
	public String[] getMoClassContains() {
		return new String[] { MoProcess.MO_CLASS };
	}

	@Override
	public boolean isSyncFilter() {
		// 프로세스 목록 조회는 동기화에 사용하지 않으므로 false로 넘깁니다.
		return false;
	}

	@Override
	protected String getOidToCheck() {
		return MIB.hrSWRunIndex;
	}

}

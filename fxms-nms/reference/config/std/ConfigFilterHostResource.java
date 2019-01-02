package com.daims.dfc.filter.config.std;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.CClass;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoChild;
import com.daims.dfc.common.mo.MoCpu;
import com.daims.dfc.common.mo.MoDisk;
import com.daims.dfc.common.mo.MoMemory;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.mib.HOST_RESOURCES_MIB;
import com.daims.dfc.service.conf.ConfApi;

/**
 * HOST-RESOURCE-MIB을 이용하여 스토리지, CPU 내용을 조회합니다.
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterHostResource extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 519115276819610544L;

	public static void main(String[] args) throws Exception {

		MoNode node = ConfApi.makeMoNode("167.1.21.31", 161, 1, "public", 0, null, null, MoNode.class);
		ConfigFilterHostResource filter = new ConfigFilterHostResource();

		ConfigMo configMo = new ConfigMo(node);
		filter.filter(configMo, null, null);
		for (Mo mo : configMo.getMoListAll()) {
			System.out.println(CClass.toString(mo));
		}

	}

	private final HOST_RESOURCES_MIB MIB = new HOST_RESOURCES_MIB();

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {
			OidValue ovArr[] = snmpget(node, MIB.hrSystemUptime);
			node.setSysUptime(ovArr[0].getLong(0));
			node.setSysUptimeChgUxtime(System.currentTimeMillis() / 1000L);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}

		int moCount = 0;

		Ret ret = Ret.OK;
		List<Mo> moList;

		if (containsMoClass(moClassArr, MoCpu.MO_CLASS)) {
			moList = findCpu(node);
			if (moList != null) {
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoCpu.MO_CLASS);
				configMo.addMoListDetected(moList);
				moCount += moList.size();
			}
		}

		if (ret.isOk()) {
			moCount += ret.intValue();
			moList = findStorage(node, containsMoClass(moClassArr, MoDisk.MO_CLASS),
					containsMoClass(moClassArr, MoMemory.MO_CLASS));
			if (moList != null) {
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoDisk.MO_CLASS);
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoMemory.MO_CLASS);
				configMo.addMoListDetected(moList);
				moCount += moList.size();

				configMo.addAttachObject(ConfigMo.ATTACH_OBJECT_KEY__HOST_MIB, true);
			}
		}

		return ret.isOk() ? new Ret(moCount) : ret;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoCpu.MO_CLASS, MoDisk.MO_CLASS, MoMemory.MO_CLASS };
	}

	/**
	 * 하나의 인터페이스의 정보를 수집한다.
	 * 
	 * @param node
	 *            노드
	 * @param snmpIndex
	 *            int 인터페이스 인덱스
	 * @return InterfaceInfo 수집된 인터페이스 정보
	 */
	protected MoCpu getCpu(SnmpMo node, int snmpIndex) throws Exception {
		String dotidx = "." + snmpIndex;
		OidValue ovArr[];
		try {
			ovArr = snmpget(node //
					, MIB.hrDeviceType + dotidx //
					, MIB.hrDeviceDescr + dotidx);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			throw e;
		}

		String hrDeviceType = ovArr[0].getValue();
		String hrDeviceDescr = ovArr[1].getValue();

		if (hrDeviceType.equals(MIB.hrDeviceProcessor)) {
			MoCpu cpu = new MoCpu(hrDeviceDescr, MoCpu.getCpuHz(hrDeviceDescr), snmpIndex);
			return cpu;
		}

		return null;
	}

	@Override
	protected String getOidToCheck() {
		return MIB.hrDeviceIndex;
	}

	/**
	 * 하나의 인터페이스의 정보를 수집한다.
	 * 
	 * @param ifidx
	 *            int 인터페이스 인덱스
	 * @return InterfaceInfo 수집된 인터페이스 정보
	 */
	protected MoChild<?> getStorage(SnmpMo node, int snmpIndex) throws Exception {
		String dotidx = "." + snmpIndex;
		String oids[] = new String[] { //
		MIB.hrStorageType + dotidx //
		, MIB.hrStorageDescr + dotidx //
		, MIB.hrStorageAllocationUnits + dotidx //
		, MIB.hrStorageSize + dotidx //
		, MIB.hrStorageUsed + dotidx //
		};

		OidValue ovArr[];
		try {
			ovArr = snmpget(node, oids);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			throw e;
		}

		if (ovArr == null) return null;

		String hrStorageType, hrStorageDescr;
		long hrStorageAllocationUnits, hrStorageSize, hrStorageUsed;

		hrStorageType = ovArr[0].getValue();

		if (hrStorageType.equals(MIB.hrStorageFixedDisk) //
				|| hrStorageType.equals(MIB.hrStorageNetworkDisk) //
				|| hrStorageType.equals(MIB.hrStorageRamDisk) //
				|| hrStorageType.equals(MIB.hrStorageRam) //
				|| hrStorageType.equals(MIB.hrStorageVirtualMemory)) {

			MoChild<?> storage;

			hrStorageDescr = ovArr[1].getValue();
			hrStorageAllocationUnits = ovArr[2].getInt(0);
			try {
				hrStorageSize = getLong(ovArr[3].getBytes());
			}
			catch (Exception e) {
				hrStorageSize = ovArr[3].getLong(0);
			}

			try {
				hrStorageUsed = getLong(ovArr[4].getBytes());
			}
			catch (Exception e) {
				hrStorageUsed = ovArr[4].getLong(0);
			}

			if (hrStorageSize < 0) hrStorageSize = Integer.MAX_VALUE + (Integer.MAX_VALUE + hrStorageSize);
			if (hrStorageUsed < 0) hrStorageUsed = Integer.MAX_VALUE + (Integer.MAX_VALUE + hrStorageUsed);

			ServiceImpl.logger.debug(node.getIpAddress() + " : " + hrStorageDescr + ", hrStorageSize="
					+ hrStorageSize + ", hrStorageUsed=" + hrStorageUsed);

			if (hrStorageSize <= 0) return null;

			if (hrStorageType.equals(MIB.hrStorageFixedDisk) //
					|| hrStorageType.equals(MIB.hrStorageRamDisk) || hrStorageType.equals(MIB.hrStorageNetworkDisk)) {
				MoDisk disk = new MoDisk();
				disk.setDiskSizeBytes((hrStorageAllocationUnits * hrStorageSize));
				disk.setDiskSizeFreeBytes((hrStorageAllocationUnits * (hrStorageSize - hrStorageUsed)));
				disk.setMoType(getDiskMoType(hrStorageType));
				storage = disk;
			}
			else {
				MoMemory memory = new MoMemory();
				memory.setMemSizeBytes(hrStorageAllocationUnits * hrStorageSize);
				memory.setMemoryType(hrStorageType.equals(MIB.hrStorageRam) ? "Real" : "Virtual");
				storage = memory;
			}

			storage.setMoAlias(hrStorageDescr);
			storage.setMoName(hrStorageDescr);
			storage.setSnmpIndex(snmpIndex + "");
			storage.setManaged(true);

			if (storage instanceof MoDisk) {
				MoDisk moDisk = (MoDisk) storage;
				moDisk.setMoName(MoDisk.getMoNameByHrStorageDescr(storage.getMoName()));
				// 관리대상 최소 크기 이하인 경우는 관리대상을 false로 합니다.
				if (moDisk.getDiskSize() < MoDisk.SIZE_MIN) storage.setManaged(false);
			}

			return storage;
		}

		return null;
	}

	private List<Mo> findCpu(SnmpMo node) {

		List<Integer> list;
		MoCpu mo, mo2;
		List<Mo> moList = new ArrayList<Mo>();
		Map<String, MoCpu> map = new HashMap<String, MoCpu>();

		try {
			list = getHrDeviceIndexList(node);

			for (Integer index : list) {
				mo = getCpu(node, index);
				if (mo == null) continue;
				mo2 = map.get(mo.getMoName());
				if (mo2 != null) {
					mo2.setCountCpu(mo2.getCountCpu() + 1);
					map.put(mo.getMoName(), mo2);
				}
				else {
					map.put(mo.getMoName(), mo);
				}
			}

			for (String key : map.keySet()) {
				mo = map.get(key);
				if (mo.getCountCpu() > 1) {
					mo.setMoName(mo.getMoName() + " * " + mo.getCountCpu());
				}
				moList.add(mo);
			}

			return moList;
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return null;
		}
	}

	private List<Mo> findStorage(SnmpMo node, boolean isDisk, boolean isMemory) {

		int faultThrNoDisk = 0;
		int faultThrNoMem = 0;
		List<Mo> moList = new ArrayList<Mo>();

		List<Integer> list;
		Mo mo;
		try {
			list = getHrStorageIndexList(node);
			for (Integer index : list) {
				mo = getStorage(node, index);
				if (mo != null) {
					if (isDisk && mo instanceof MoDisk) {
						((MoDisk) mo).setFaultThrNo(faultThrNoDisk);
						moList.add(mo);
					}
					else if (isMemory && mo instanceof MoMemory) {
						((MoMemory) mo).setFaultThrNo(faultThrNoMem);
						moList.add(mo);
					}
				}
			}
			return moList;
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return null;
		}
	}

	/**
	 * 
	 * @param target
	 * @return 디바이스 인덱스 목록
	 * @throws Exception
	 */
	private List<Integer> getHrDeviceIndexList(SnmpMo node) throws Exception {

		List<OidValue> valueList = snmpwalk(node, MIB.hrDeviceIndex);
		List<Integer> list = new ArrayList<Integer>();
		for (OidValue value : valueList) {
			if (value.isNull() == false) list.add(value.getInt(0));
		}
		return list;
	}

	/**
	 * 
	 * @param target
	 * @return 스토리지의 SNMP Index 목록
	 * @throws Exception
	 */
	private List<Integer> getHrStorageIndexList(SnmpMo node) throws Exception {
		List<Integer> list = new ArrayList<Integer>();

		List<OidValue> varList = snmpwalk(node, MIB.hrStorageIndex);
		for (OidValue var : varList) {
			if (var.isNull() == false) {
				if (var.getInt(-1) > -1) list.add(var.getInt());
			}
		}

		return list;
	}

	private long getLong(byte bytes[]) {
		if (bytes == null) return 0;

		long value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value = (value << 8) + (bytes[i] & 0xff);
		}
		return value;
	}

	private String getDiskMoType(String hrStorageType) {
		if (hrStorageType.equals(MIB.hrStorageFixedDisk)) return "FixedDisk";
		else if (hrStorageType.equals(MIB.hrStorageRamDisk)) return "RamDisk";
		// else if (hrStorageType.equals(MIB.hrStorageFlashMemory)) return
		// "FlashMemory";
		else if (hrStorageType.equals(MIB.hrStorageNetworkDisk)) return "NetworkDisk";
		return null;
	}

}

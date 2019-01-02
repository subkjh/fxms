package com.daims.dfc.filter.config.netapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import subkjh.dao.control.DaoFactory;
import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.api.MoApi;
import com.daims.dfc.common.mib.MibNetApp;
import com.daims.dfc.common.mo.MoDisk;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * NAS(NETAPP) 장비의 스토리지, CPU 내용을 조회합니다.
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterNetappNas extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5541411851222105450L;

	public static void main(String[] args) {
		try {
			DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		MoNode node = MoApi.makeNodeSnmp("12.4.6.234", 161, 1, "public", "", MoNode.class);

		ConfigFilterNetappNas f = new ConfigFilterNetappNas();
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

	private MibNetApp MIB;

	public ConfigFilterNetappNas() {
		MIB = new MibNetApp();
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = configMo.getNode();
		if (node.isSnmp() == false) return Ret.OK;

		if (containsMoClass(moClassArr, MoDisk.MO_CLASS) == false) return Ret.OK;

		List<OidValue>[] varList;
		try {
			varList = snmpwalk(node //
					, MIB.dfTabledfEntrydfFileSys//
					, MIB.dfType //
					, MIB.dfLowTotalKBytes //
					, MIB.dfHighTotalKBytes //
					, MIB.dfLowAvailKBytes //
					, MIB.dfHighAvailKBytes //
			);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}

		MoDisk disk;
		List<MoDisk> moList = new ArrayList<MoDisk>();

		for (int index = 0; index < varList[0].size(); index++) {

			if (varList[1].get(index).getValue().equals("2") == false) continue;

			disk = MoDisk.make(varList[0].get(index).getValue() //
					, getValue(varList[2].get(index).getInt(0), varList[3].get(index).getInt(1)) //
					, getValue(varList[4].get(index).getInt(0), varList[5].get(index).getInt(1)) //
					, 0 //
					, varList[0].get(index).getInstance(1));

			if (disk.getMoName().indexOf("snapshot") > 0) continue;

			// 잔여공간이 실제 공간보다 크면 무시합니다.
			// if ( disk.getDiskSize() < disk.getDiskSizeFree() ) continue;

			// System.out.println(varArray[0].toString());
			// System.out.println(varArray[1].value + "," +
			// getLong(varArray[1].value.toBytes()));
			// System.out.println(varArray[2].value + "," +
			// getLong(varArray[2].value.toBytes()));
			// System.out.println(varArray[3].value + "," +
			// getLong(varArray[3].value.toBytes()));
			// System.out.println(disk.getMoName());
			// System.out.println(disk.getDiskSize());
			// System.out.println(disk.getDiskSizeFree());
			// System.out.println(((disk.getDiskSize() - disk.getDiskSizeFree())
			// / disk.getDiskSize()));

			ServiceImpl.logger.debug(disk.getMoName() + ", " + disk.getDiskSize() + ", " + disk.getDiskSizeFree());
			moList.add(disk);
		}

		configMo.addMoListDetected(moList);

		return new Ret(moList.size());
	}

	public Ret filter2(ConfigMo configMo, String... moClassArray) {

		MoNode node = configMo.getNode();
		if (node.isSnmp() == false) return Ret.OK;

		if (containsMoClass(moClassArray, MoDisk.MO_CLASS) == false) return Ret.OK;

		List<OidValue>[] varList;
		try {
			varList = snmpwalk(node //
					, MIB.dfTabledfEntrydfFileSys//
					, MIB.dfTabledfEntrydfKBytesTotal // ,
					, MIB.dfTabledfEntrydfKBytesAvail //
					, MIB.dfType //
			);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}

		MoDisk disk;
		List<MoDisk> moList = new ArrayList<MoDisk>();

		for (int index = 0; index < varList[0].size(); index++) {

			disk = new MoDisk();

			if (varList[3].get(index).getValue().equals("2") == false) continue;

			disk.setDiskSizeBytes(getLong(varList[1].get(index).getBytes()) * 1024);
			disk.setDiskSizeFreeBytes(getLong(varList[2].get(index).getBytes()) * 1024);
			disk.setMoAlias(varList[0].get(index).getValue());
			disk.setMoName(varList[0].get(index).getValue());

			disk.setSnmpIndex(varList[0].get(index).getInstance(1));
			disk.setHstimeChg(node.getHstimeChg());
			disk.setManaged(true);

			if (disk.getMoName().indexOf("snapshot") > 0) continue;

			// 잔여공간이 실제 공간보다 크면 무시합니다.
			// if ( disk.getDiskSize() < disk.getDiskSizeFree() ) continue;

			// System.out.println(varArray[0].toString());
			// System.out.println(varArray[1].value + "," +
			// getLong(varArray[1].value.toBytes()));
			// System.out.println(varArray[2].value + "," +
			// getLong(varArray[2].value.toBytes()));
			// System.out.println(varArray[3].value + "," +
			// getLong(varArray[3].value.toBytes()));
			// System.out.println(disk.getMoName());
			// System.out.println(disk.getDiskSize());
			// System.out.println(disk.getDiskSizeFree());
			// System.out.println(((disk.getDiskSize() - disk.getDiskSizeFree())
			// / disk.getDiskSize()));

			ServiceImpl.logger.debug(disk.getMoName() + ", " + disk.getDiskSize() + ", " + disk.getDiskSizeFree());
			moList.add(disk);
		}

		configMo.addMoListDetected(moList);

		return new Ret(moList.size());
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoDisk.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return MIB.dfTabledfEntrydfFileSys;
	}

	private long getLong(byte bytes[]) {
		if (bytes == null) return 0;

		long value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value = (value << 8) + (bytes[i] & 0xff);
		}
		return value;
	}

	private long getValue(long low, long high) {
		long x;

		if (low >= 0) x = high * (4294967295L) + low;
		else x = (high + 1) * (4294967295L) + low;
		return x * 1024;
	}

}

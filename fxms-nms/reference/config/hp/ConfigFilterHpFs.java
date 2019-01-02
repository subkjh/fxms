package com.daims.dfc.filter.config.hp;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mib.MibHp;
import com.daims.dfc.common.mo.MoDisk;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

public class ConfigFilterHpFs extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2103962847265349207L;

	// public static void main(String[] args) throws Exception {
	//
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("12.4.6.234", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	// FilterConfigHp filter = new FilterConfigHp();
	// filter.filter(new ConfigMo(node), MoDisk.MO_CLASS);
	// ConfApi.api.snmputil.close();
	//
	// }

	private MibHp OID;

	public ConfigFilterHpFs() {
		OID = new MibHp();
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		int moCount = 0;
		List<MoDisk> listDisk;

		Ret ret = Ret.OK;

		try {
			listDisk = getDiskList(node);
			configMo.addMoListDetected(listDisk);
			moCount = listDisk.size();
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

		return ret.isOk() ? new Ret(moCount) : ret;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoDisk.MO_CLASS };
	}

	protected List<MoDisk> getDiskList(SnmpMo node) throws Exception {
		String oidArray[] = new String[] { //
		OID.fileSystemBfree, OID.fileSystemBlock, OID.fileSystemBsize, OID.fileSystemDir };

		List<MoDisk> listDisk = new ArrayList<MoDisk>();

		List<OidValue>[] listArray = snmpwalk(node, oidArray);
		long bfree, block, bsize, size;
		String dir;
		String snmpIndex;

		for (int index = 0; index < listArray[0].size(); index++) {

			bfree = listArray[0].get(index).getLong();
			block = listArray[1].get(index).getLong();
			bsize = listArray[2].get(index).getLong();
			dir = listArray[3].get(index).getValue();
			snmpIndex = listArray[0].get(index).getInstance(2);

			ServiceImpl.logger.trace(snmpIndex + " = " + bfree + " = " + block + " = " + bsize + " = " + dir);

			size = block * bsize;
			if (size <= 0) continue;

			MoDisk disk = MoDisk.make(dir, size, bsize * bfree, 0, snmpIndex);
			disk.setManaged(disk.getDiskSize() >= MoDisk.SIZE_MIN);

			if (disk.getDiskSize() >= 0.001D) {
				listDisk.add(disk);
			}
		}

		return listDisk;
	}

	@Override
	protected String getOidToCheck() {
		return OID.fileSystemID1;
	}

}

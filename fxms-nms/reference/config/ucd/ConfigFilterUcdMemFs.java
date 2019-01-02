package com.daims.dfc.filter.config.ucd;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mib.MibNetSnmp;
import com.daims.dfc.common.mo.MoCpu;
import com.daims.dfc.common.mo.MoDisk;
import com.daims.dfc.common.mo.MoMemory;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * UCD-SNMP인 경우의 MEMORY, DISK 구성 찾기<br>
 * disk를 확인하고자 할때는 snmpd.conf 화일에서 <br>
 * 
 * disk /<br>
 * disk /data<br>
 * disk /user<br>
 * 위와 같이 보고자하는 내용을 추가해 주어야 합니다.
 * 
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterUcdMemFs extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8650421947693800513L;

	// public static void main(String[] args) throws Exception {
	// MoApi.api = new MoApiImpl();
	// ConfApi.api = new ConfApiImpl();
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	// CLogger.logger.setLevel(LEVEL.trace);
	// ConfApi.api.snmputil.setLogger(CLogger.logger);
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("14.4.44.251", 161, 1,
	// "msp_read", MoNodeSnmpImpl.class);
	// // MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("172.16.200.240", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	//
	// FilterConfigUcdSnmpMib filter = new FilterConfigUcdSnmpMib();
	//
	// ConfigMo configMo = new ConfigMo(node);
	// filter.filter(configMo);
	// for (Mo mo : configMo.moList()) {
	// System.out.println(CClass.toString(mo));
	// }
	// ConfApi.api.snmputil.close();
	//
	// }

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		Object obj = configMo.getAttachObject(ConfigMo.ATTACH_OBJECT_KEY__HOST_MIB);
		if (obj != null) {
			ServiceImpl.logger.debug("This filter passwd because HOST-MIB processed." + " " + obj);
			return Ret.OK;
		}

		int count = 0;

		if (containsMoClass(moClassArr, MoMemory.MO_CLASS)) {
			MoMemory moMemory = findMemory(node);
			if (moMemory != null) {
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoMemory.MO_CLASS);
				configMo.addMoDetected(moMemory);
				count++;
			}
		}

		if (containsMoClass(moClassArr, MoDisk.MO_CLASS)) {
			List<MoDisk> moDiskList = findDisk(node);
			if (moDiskList != null && moDiskList.size() > 0) {
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoDisk.MO_CLASS);
				configMo.addMoListDetected(moDiskList);
				count += moDiskList.size();
			}
		}

		return new Ret(count);
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoCpu.MO_CLASS, MoDisk.MO_CLASS, MoMemory.MO_CLASS };
	}

	protected MoDisk getDisk(SnmpMo node, int snmpIndex) throws Exception {
		String dotidx = "." + snmpIndex;
		String oids[] = new String[] { //
		MibNetSnmp.dskPath + dotidx //
		, MibNetSnmp.dskTotal + dotidx //
		, MibNetSnmp.dskAvail + dotidx };

		OidValue ovArr[];
		try {
			ovArr = snmpget(node, oids);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			throw e;
		}

		if (ovArr == null) return null;

		String moName = ovArr[0].getValue();
		long sizeKBytes = ovArr[1].getLong(-1);
		long freeKBytes = ovArr[2].getLong(-1);

		ServiceImpl.logger.debug(node.getIpAddress() + "|" + moName + "|" + sizeKBytes + "|" + freeKBytes);

		if (sizeKBytes <= 0) return null;

		MoDisk moDisk = MoDisk.make(moName, sizeKBytes * 1024, freeKBytes * 1024, 0, snmpIndex + "");

		return moDisk;
	}

	@Override
	protected String getOidToCheck() {
		return MibNetSnmp.memTotalReal;
	}

	private List<MoDisk> findDisk(SnmpMo node) throws Exception {
		List<MoDisk> moDiskList = new ArrayList<MoDisk>();
		List<Integer> dskIndexList = getDiskIndexList(node);
		MoDisk moDisk;

		if (dskIndexList != null && dskIndexList.size() > 0) {
			for (int dskIndex : dskIndexList) {
				moDisk = getDisk(node, dskIndex);
				if (moDisk != null) moDiskList.add(moDisk);
			}
		}

		return moDiskList;
	}

	private MoMemory findMemory(SnmpMo node) throws Exception {

		OidValue ovArr[] = snmpget(node, MibNetSnmp.memTotalReal);
		if (ovArr != null && ovArr.length == 1) {
			MoMemory moMemory = MoMemory.make(MoMemory.MEMORY_NAME, MoMemory.MEMORY_TYPE_REAL,
					ovArr[0].getLong(-1) * 1024, "");
			return moMemory;
		}

		return null;

	}

	private List<Integer> getDiskIndexList(SnmpMo node) throws Exception {
		List<Integer> list = new ArrayList<Integer>();

		List<OidValue> varList = snmpwalk(node, MibNetSnmp.dskIndex);
		for (OidValue var : varList) {
			if (var.isNull() == false) {
				if (var.getInt(-1) > -1) list.add(var.getInt());
			}
		}

		return list;
	}
}

package com.daims.dfc.filter.config.unicn;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mo.MoIpPhone;
import com.daims.dfc.common.mo.MoNodePbx;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * OID를 사용하여 장비의 구성정보를 수집합니다.
 * 
 * @author kwak
 * 
 */

public class ConfigFilterUnicnIpt extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6717961551525686235L;
	/** 라이선스 수량 */
	private final static String UNICN_LICENSECOUNT = ".1.3.6.1.4.1.16183.4.2.19.1.1.7";
	/** IP-PBX의 Port */
	private final static String UNICN_PORT = ".1.3.6.1.4.1.16183.4.2.19.1.1.6";
	/** 현재 시스템의 등록된 UA수 */
	private final static String UNICN_SIPNBOFUA = ".1.3.6.1.4.1.16183.4.2.19.1.2.8";

	/** 단말 연결 IP */
	private final static String UNICN_UA_CONTACTIPADDRESS = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.10";

	/** 표시이름 */
	private final static String UNICN_UA_DISPLAYNAME = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.8";
	/** 내선번호 */
	private final static String UNICN_UA_EXTNUMBER = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.6";
	/** 등록 시간 */
	private final static String UNICN_UA_REGISTERTIME = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.9";
	/** 단말 SIP ID */
	private final static String UNICN_UA_SIPID = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.3";
	/** 단말 SIP Password */
	private final static String UNICN_UA_SIPPASSWD = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.4";
	/** IP-PBX Version */
	private final static String UNICN_VERSION = ".1.3.6.1.4.1.16183.4.2.19.1.1.8";

	// public static void main(String[] args) throws Exception {
	//
	// MoApi.api = new MoApiImpl();
	// ConfApi.api = new ConfApiImpl();
	// getSnmpUtil() = SnmpUtil.createSnmpUtil();
	//
	// MoNodePbxImpl node = ConfApi.makeNodeSnmp("211.44.132.133", 161, 1,
	// "HexusCommunity", MoNodePbxImpl.class);
	//
	// FilterConfigIptUnicn filter = new FilterConfigIptUnicn();
	// ConfigMo configMo = new ConfigMo(node);
	//
	// filter.filter(configMo);
	// if (configMo.getSizeChildren() > 0) {
	// for (Mo mo : configMo.moList()) {
	// System.out.println(mo);
	// }
	// }
	//
	// getSnmpUtil().close();
	//
	// System.exit(0);
	// }

	private final String UNICN_LAST_MIB = ".1.3.6.1.4.1.16183.4.2.19.2.1.5";

	public ConfigFilterUnicnIpt() {

	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		ServiceImpl.logger.debug(configMo);

		if ((configMo.getNode() instanceof MoNodePbx) == false) return Ret.OK;

		MoNodePbx node = (MoNodePbx) configMo.getNode();
		if (node.isSnmp() == false) return Ret.OK;

		try {
			configMo.addMoListDetected(collectPbxConf(node));

		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoNodePbx.MO_CLASS, MoIpPhone.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		// 2013.03.06 by subkjh
		// 확인할 OID 없음으로 변경
		return null;

		// return UNICN_UA_SIPID;
	}

	private List<MoIpPhone> collectPbxConf(MoNodePbx node) {
		OidValue ovArr[];
		List<MoIpPhone> phoneList = new ArrayList<MoIpPhone>();
		MoIpPhone ipPhone;

		String srcOid = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.1";
		String changeOid = ".1.3.6.1.4.1.16183.4.2.19.3.1.1.4";

		try {

			ovArr = snmpget(node, UNICN_PORT, UNICN_VERSION, UNICN_LICENSECOUNT, UNICN_SIPNBOFUA);

			node.setPortService(Integer.parseInt(ovArr[0].getValue()));
			node.setVerHw(ovArr[1].getValue());
			node.setDnCountMax(Integer.parseInt(ovArr[2].getValue()));
			node.setPhoneCountMax(Integer.parseInt(ovArr[3].getValue()));

			ovArr = snmpnext(node, UNICN_LAST_MIB);
			if (ovArr[0].getOid() == null) return null;

			String curOid = ovArr[0].getOid().replaceAll(srcOid, changeOid);

			List<OidValue> idxList = new ArrayList<OidValue>();

			ovArr = snmpget(node, curOid);

			if (ovArr[0].getValue() == null) return null;

			do {
				if (ovArr[0].getOid().startsWith(changeOid)) {
					idxList.add(ovArr[0]);
					curOid = ovArr[0].getOid();
				}
				else break;
				ovArr = snmpnext(node, curOid);
			} while (true);

			for (OidValue var : idxList) {
				ipPhone = getPhoneConf(var.getInstance(1), node);
				if (ipPhone != null) phoneList.add(ipPhone);
			}

			return phoneList;

		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			return null;
		}
	}

	private MoIpPhone getPhoneConf(String unicnIdx, MoNodePbx node) throws Exception {
		MoIpPhone ipPhone = new MoIpPhone();
		String dotIdx = "." + unicnIdx;

		OidValue ovArr[] = snmpget(node //
				, UNICN_UA_SIPID + dotIdx //
				, UNICN_UA_SIPPASSWD + dotIdx//
				, UNICN_UA_EXTNUMBER + dotIdx//
				, UNICN_UA_DISPLAYNAME + dotIdx//
				, UNICN_UA_REGISTERTIME + dotIdx//
				, UNICN_UA_CONTACTIPADDRESS + dotIdx);

		ipPhone.setSipId(ovArr[0].getValue());
		ipPhone.setSipPw(ovArr[1].getValue());
		ipPhone.setTelNoExt(ovArr[2].getValue());
		ipPhone.setLinkDescr(ovArr[3].getValue());
		ipPhone.setHstimeReg(timeConvert(ovArr[4].getValue()));
		ipPhone.setIpAddressSignaling(ovArr[5].getValue());

		ipPhone.setMoName(ipPhone.getLinkDescr());
		ipPhone.setMoAlias(ipPhone.getLinkDescr());

		return ipPhone;
	}

	private long timeConvert(String time) {
		return Long.parseLong(time.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "").substring(0, 14));
	}
}

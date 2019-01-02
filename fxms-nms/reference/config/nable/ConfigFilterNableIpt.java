package com.daims.dfc.filter.config.nable;

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

public class ConfigFilterNableIpt extends ConfigFilterSnmpNode {

	/** 시스템 */
	/** IP-PBX 고유한 ID(070대표번호) */
	private final static String NABLE_ID = ".1.3.6.1.4.1.25256.1.2.19.1.1.1.0";
	/** IP-PBX의 Port */
	private final static String NABLE_PORT = ".1.3.6.1.4.1.25256.1.2.19.1.1.6.0";
	/** SIP서버 정보 - 현재 시스템의 등록된 UA수 */
	private final static String NABLE_SIPNBOFUA = ".1.3.6.1.4.1.25256.1.2.19.1.2.8.0";
	/** 단말 연결 IP */
	private final static String NABLE_UA_CONTACTIPADDRESS = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.10";
	/** 표시 이름 */
	private final static String NABLE_UA_DISPLAYNAME = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.8";

	/** 내선번호 */
	private final static String NABLE_UA_EXTNUMBER = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.6";

	// index가져와서 처리 (NablePbxConfig.getPbxConfig)
	/** 단말정보 */
	/** 단말 Index - uaSipId (SIP ID) */
	private final static String NABLE_UA_INDEX = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.1";
	/** 전화번호 */
	private final static String NABLE_UA_NUMBER = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.5";
	/** 단말 Mac Address */
	private final static String NABLE_UA_PHONEMACADDRESS = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.12";
	/** 단말 종류 */
	private final static String NABLE_UA_PHONETYPE = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.11";
	/** 등록 시간 */
	private final static String NABLE_UA_REGISTERTIME = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.9";
	/** SIP ID */
	private final static String NABLE_UA_SIPID = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.3";
	/** SIP Password */
	private final static String NABLE_UA_SIPPASSWD = ".1.3.6.1.4.1.25256.1.2.19.3.1.1.4";
	/** IP-PBX Version */
	private final static String NABLE_VERSION = ".1.3.6.1.4.1.25256.1.2.19.1.1.8.0";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4519567824374563938L;
	/** 라이선스 수량 */
	private final static String VLICENSECOUNT = ".1.3.6.1.4.1.25256.1.2.19.1.1.7.0";

	// public static void main(String[] args) throws Exception {
	//
	// MoApi.api = new MoApiImpl();
	// ConfApi.api = new ConfApiImpl();
	// getSnmpUtil() = SnmpUtil.createSnmpUtil();
	//
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("218.49.113.40", 161, 1,
	// "msp_read", MoNodeSnmpImpl.class);
	//
	// FilterConfigIptNable filter = new FilterConfigIptNable();
	// filter.filter(new ConfigMo(node));
	// getSnmpUtil().close();
	//
	// System.exit(0);
	// }

	public ConfigFilterNableIpt() {

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
		return NABLE_ID;
	}

	private List<MoIpPhone> collectPbxConf(MoNodePbx pbxNode) {
		List<OidValue> varList;
		OidValue ovArr[];
		List<MoIpPhone> phoneList = new ArrayList<MoIpPhone>();
		MoIpPhone ipPhone;

		try {

			ovArr = snmpget(pbxNode, NABLE_PORT, //
					NABLE_VERSION, //
					VLICENSECOUNT, //
					NABLE_SIPNBOFUA);
			pbxNode.setPortService(Integer.parseInt(ovArr[0].getValue()));
			pbxNode.setVerHw(ovArr[1].getValue());
			pbxNode.setDnCountMax(Integer.parseInt(ovArr[2].getValue()));
			pbxNode.setPhoneCountMax(Integer.parseInt(ovArr[3].getValue()));

			varList = snmpwalk(pbxNode, NABLE_UA_INDEX);

			for (OidValue var : varList) {
				ipPhone = getPhoneConf(var.getInstance(1), pbxNode);
				if (ipPhone != null) phoneList.add(ipPhone);
			}

			return phoneList;

		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			return null;
		}
	}

	private MoIpPhone getPhoneConf(String nableIdx, MoNodePbx node) throws Exception {
		MoIpPhone ipPhone = new MoIpPhone();
		String dotIdx = "." + nableIdx;

		OidValue ovArr[] = snmpget(node //
				, NABLE_UA_SIPID + dotIdx //
				, NABLE_UA_SIPPASSWD + dotIdx//
				, NABLE_UA_NUMBER + dotIdx//
				, NABLE_UA_EXTNUMBER + dotIdx//
				, NABLE_UA_DISPLAYNAME + dotIdx//
				, NABLE_UA_REGISTERTIME + dotIdx//
				, NABLE_UA_CONTACTIPADDRESS + dotIdx//
				, NABLE_UA_PHONETYPE + dotIdx//
				, NABLE_UA_PHONEMACADDRESS + dotIdx);

		ipPhone.setSipId(ovArr[0].getValue());
		ipPhone.setSipPw(ovArr[1].getValue());
		ipPhone.setTelNo(ovArr[2].getValue());
		ipPhone.setTelNoExt(ovArr[3].getValue());
		ipPhone.setLinkDescr(ovArr[4].getValue());
		ipPhone.setHstimeReg(Long.parseLong(ovArr[5].getValue()));
		ipPhone.setIpAddressSignaling(ovArr[6].getValue());
		ipPhone.setPhoneType(ovArr[7].getValue());
		ipPhone.setHwId(ovArr[8].getValue());

		return ipPhone;
	}
}

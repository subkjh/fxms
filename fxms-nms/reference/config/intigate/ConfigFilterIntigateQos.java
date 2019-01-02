package com.daims.dfc.filter.config.intigate;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.CClass;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.filter.config.intigate.bean.QosRulePolicyBean;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * OID를 사용하여 장비의 구성정보를 수집합니다.
 * 
 * @author kwak
 * 
 */

public class ConfigFilterIntigateQos extends ConfigFilterSnmpNode {

	/** QOS Index정보 */
	private final static String QOS_RULE_INDEX = ".1.3.6.1.4.1.15589.3.3.6.1.1.1";

	/** QOS MAXINBOUND */
	private final static String QOS_RULE_MAXINBOUND = ".1.3.6.1.4.1.15589.3.3.6.1.1.6.1";
	/** QOS MAXOUTBOUND */
	private final static String QOS_RULE_MAXOUTBOUND = ".1.3.6.1.4.1.15589.3.3.6.1.1.8.1";
	/** QOS MININBOUND */
	private final static String QOS_RULE_MININBOUND = ".1.3.6.1.4.1.15589.3.3.6.1.1.5.1";
	/** QOS MINOUTBOUND */
	private final static String QOS_RULE_MINOUTBOUND = ".1.3.6.1.4.1.15589.3.3.6.1.1.7.1";
	/** QOS Id정보 */
	private final static String QOS_RULE_VPID = ".1.3.6.1.4.1.15589.3.3.6.1.1.2.1";
	/** QOS 명 */
	private final static String QOS_RULE_VPNAME = ".1.3.6.1.4.1.15589.3.3.6.1.1.4.1";
	/**
	 * 
	 */
	private static final long serialVersionUID = -8528175619832290869L;

	// public static void main(String[] args) throws Exception {
	//
	// MoApi.api = new MoApiImpl();
	// ConfApi.api = new ConfApiImpl();
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	//
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("1.255.0.17", 161, 1,
	// "msp_read", MoNodeSnmpImpl.class);
	//
	// FilterConfigQosIntigate filter = new FilterConfigQosIntigate();
	// filter.filter(new ConfigMo(node));
	// ConfApi.api.snmputil.close();
	//
	// System.exit(0);
	// }

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		ServiceImpl.logger.debug(configMo);

		MoNode node = configMo.getNode();
		if (node.isSnmp() == false) return Ret.OK;

		List<OidValue> varList;
		List<QosRulePolicyBean> qosList = new ArrayList<QosRulePolicyBean>();
		QosRulePolicyBean qosInti;

		try {

			varList = snmpwalk(node, QOS_RULE_INDEX);

			for (OidValue var : varList) {
				qosInti = qosIntiGet(var.getInstance(1), node);
				// if (qosInti != null)
				// System.out.println(CClass.toString(qosInti));
				qosList.add(qosInti);
			}
			configMo.addAttachObject("QosRulePolicy", qosList);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return new Ret(e);
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return null;
	}

	@Override
	protected String getOidToCheck() {
		return QOS_RULE_INDEX;
	}

	private QosRulePolicyBean qosIntiGet(String qosIntiIdx, MoNode node) throws Exception {
		QosRulePolicyBean qosInti;
		String dotIdx = "." + qosIntiIdx;

		System.out.println("INDEX > " + qosIntiIdx);
		OidValue ovArr[] = snmpget(node //
				, QOS_RULE_VPID + dotIdx //
				, QOS_RULE_VPNAME + dotIdx//
				, QOS_RULE_MININBOUND + dotIdx//
				, QOS_RULE_MINOUTBOUND + dotIdx//
				, QOS_RULE_MAXINBOUND + dotIdx//
				, QOS_RULE_MAXOUTBOUND + dotIdx);

		qosInti = new QosRulePolicyBean();
		qosInti.setRuleId(ovArr[0].getInt());
		qosInti.setRuleName(ovArr[1].getValue());
		qosInti.setInboundMin(ovArr[2].getInt());
		qosInti.setOutboundMin(ovArr[3].getInt());
		qosInti.setInboundMax(ovArr[4].getInt());
		qosInti.setOutboundMax(ovArr[5].getInt());

		System.out.println(CClass.toString("qosInti " + qosInti));

		return qosInti;
	}
}

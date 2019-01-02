package com.daims.dfc.filter.config.l4;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.dao.control.DaoFactory;
import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.CLogger;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.api.MoApi;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoRip;
import com.daims.dfc.common.mo.MoVip;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.mib.F5_BIGIP_LOCAL_MIB;
import com.daims.dfc.utils.CheckUtil;

public class F5ConfigFilter extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 92378687830689677L;

	public static void main(String[] args) {
		try {
			DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		ServiceImpl.logger.setLevel(CLogger.LEVEL.trace);

		// MoNodeSnmpImpl node = ConfigFilter.makeNodeSnmp("1.226.200.2", 161,
		// 1, "broadntv13", "", MoNodeSnmpImpl.class);
		MoNode node = MoApi.makeNodeSnmp("167.1.21.63", 60261, 1, "broadntv!#", "", MoNode.class);

		ConfigMo configMo = new ConfigMo(node);

		F5ConfigFilter f = new F5ConfigFilter();
		try {
			f.filter(configMo, null, null);
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

		for (Mo mo : configMo.getMoListAll()) {
			System.out.println(mo.getMoClass() + ":" + mo.getMoName());
		}

		System.exit(0);
	}

	private final F5_BIGIP_LOCAL_MIB MIB = new F5_BIGIP_LOCAL_MIB();

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		List<MoVip> vipMoList = new ArrayList<MoVip>();
		List<MoRip> ripMoList = new ArrayList<MoRip>();

		getVipNRip(node, configMo, vipMoList, ripMoList);

		configMo.addMoListDetected(vipMoList);
		configMo.addMoListDetected(ripMoList);

		ServiceImpl.logger.debug(node, vipMoList.size(), ripMoList.size());

		return new Ret("vip=" + vipMoList.size() + ", rip=" + ripMoList.size());
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoVip.MO_CLASS, MoRip.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return MIB.ltmVirtualServName;
	}

	private String getLB(int code) {

		switch (code) {
		case 0:
			return "roundRobin";
		case 1:
			return "ratioMember";
		case 2:
			return "leastConnMember";
		case 3:
			return "observedMember";
		case 4:
			return "predictiveMember";
		case 5:
			return "ratioNodeAddress";
		case 6:
			return "leastConnNodeAddress";
		case 7:
			return "fastestNodeAddress";
		case 8:
			return "observedNodeAddress";
		case 9:
			return "predictiveNodeAddress";
		case 10:
			return "dynamicRatio";
		case 11:
			return "fastestAppResponse";
		case 12:
			return "leastSessions";
		case 13:
			return "dynamicRatioMember";
		case 14:
			return "l3Addr";
		case 15:
			return "weightedLeastConnMember";
		case 16:
			return "weightedLeastConnNodeAddr";
		case 17:
			return "ratioSession";

		default:
			return "unkown";
		}
	}

	private int getStatus(int code) {
		switch (code) {
		case 1:
			return 1;
		case 2:
			return 0;
		case 3:
			return 0;
		default:
			return -1;
		}
	}

	private void getVipNRip(MoNode node, ConfigMo configMo, List<MoVip> vipMoList, List<MoRip> ripMoList) {
		List<String> oids = new ArrayList<String>();

		MoVip vip;
		MoRip rip;
		List<OidValue>[] valueList;

		Map<String, String> vipNameMap = new HashMap<String, String>();
		Map<String, String> poolOidMap = new HashMap<String, String>();
		Map<String, Integer> lbTypeMap = new HashMap<String, Integer>();
		Map<String, String> poolNameMap = new HashMap<String, String>();

		try {

			setPollName(node, lbTypeMap, poolNameMap);

			oids.add(MIB.ltmVirtualServName);
			oids.add(MIB.ltmVirtualServAddr);
			oids.add(MIB.ltmVirtualServPort);
			oids.add(MIB.ltmVirtualServDefaultPool);
			// oids.add(MIB.ltmVsStatusEnabledState);

			valueList = snmpwalk(node, oids.toArray(new String[oids.size()]));

			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoVip.MO_CLASS);
			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoRip.MO_CLASS);

			String lbType = null;
			String ltmPoolStatName = null;

			for (int i = 0; i < valueList[0].size(); i++) {
				String poolDefaultName = valueList[3].get(i).getValue();

				try {
					CheckUtil.checkIp(valueList[1].get(i).getIpAddress());
				}
				catch (Exception e) {
					continue;
				}

				ltmPoolStatName = poolNameMap.get(poolDefaultName);
				lbType = getLB(lbTypeMap.get(ltmPoolStatName));

				vip = new MoVip(0, 0, valueList[3].get(i).getValue(), valueList[1].get(i).getIpAddress(),
						valueList[2].get(i).getInt(), lbType);

				// vip.setStatusVip(getStatus(valueList[4].get(i).getInt()));
				// vip.setUserProperty("ltmPoolStatName", ltmPoolStatName);

				vipMoList.add(vip);

				vipNameMap.put(vip.getMoAlias(), vip.getMoName());
				poolOidMap.put(valueList[3].get(i).getValue(),
						valueList[3].get(i).getInstance(MIB.ltmVirtualServDefaultPool));
			}

			oids.clear();

			oids.add(MIB.ltmPoolMbrStatusPoolName);
			oids.add(MIB.ltmPoolMbrStatusAvailState);
			oids.add(MIB.ltmPoolMbrStatusEnabledState);
			oids.add(MIB.ltmPoolMemberStatAddrType);

			valueList = snmpwalk(node, oids.toArray(new String[oids.size()]));

			String[] ipport = new String[2];
			String poolNameIndex, poolName, addrType;
			if (vipMoList.size() <= 0) return;
			for (int i = 0; i < valueList[0].size(); i++) {

				poolName = valueList[0].get(i).getValue();
				poolNameIndex = valueList[0].get(i).getInstance(MIB.ltmPoolMbrStatusPoolName);
				addrType = valueList[3].get(i).getValue();

				ipport = parseIpNPort(valueList[1].get(i).getOid());
				if (vipNameMap.get(poolName) == null) continue;

				rip = new MoRip();
				MoRip.set(rip, vipNameMap.get(poolName), poolName, ipport[0], Integer.parseInt(ipport[1]));

				rip.setStatusRip(getStatus(valueList[2].get(i).getInt()));
				rip.setUserProperty("poolNameIndex", poolNameIndex);
				rip.setUserProperty("addrTypeIndex", addrType);
				rip.setUserProperty("addrIndex", ipport[0]);
				rip.setUserProperty("portIndex", ipport[1]);

				ripMoList.add(rip);
			}

		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}
	}

	private void setPollName(MoNode node, Map<String, Integer> lbTypeMap, Map<String, String> poolNameMap) {
		List<String> oids = new ArrayList<String>();

		List<OidValue>[] valueList;

		try {
			oids.add(MIB.ltmPoolLbMode);
			oids.add(MIB.ltmPoolName);

			valueList = snmpwalk(node, oids.toArray(new String[oids.size()]));

			for (int i = 0; i < valueList[0].size(); i++) {
				lbTypeMap.put(valueList[0].get(i).getInstance(MIB.ltmPoolLbMode), valueList[0].get(i).getInt());
			}
			for (int i = 0; i < valueList[1].size(); i++) {
				poolNameMap.put(valueList[1].get(i).getValue(), valueList[1].get(i).getInstance(MIB.ltmPoolName));
			}

		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}
	}

	private String[] parseIpNPort(String oid) {
		ServiceImpl.logger.debug("oid=" + oid);
		String[] ss = null;
		ss = oid.split("\\.");
		String port = ss[ss.length - 1];
		String ip = ss[ss.length - 5] + "." + ss[ss.length - 4] + "." + ss[ss.length - 3] + "." + ss[ss.length - 2];
		return new String[] { ip, port };
	}

}
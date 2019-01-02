package com.daims.dfc.filter.config.l4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.daims.dfc.mib.ALTEON_CHEETAH_LAYER4_MIB;

/**
 * 알테온 장비의 L4를 수집하는 필터
 * 
 * @author subkjh
 * 
 */
public class AlteonConfigFilter extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4672318317256305530L;

	public static void main(String[] args) {
		try {
			DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		ServiceImpl.logger.setLevel(CLogger.LEVEL.debug);

		MoNode node = MoApi.makeNodeSnmp("167.1.21.63", 60161, 1, "broadntv!#", "", MoNode.class);

		ConfigMo configMo = new ConfigMo(node);

		AlteonConfigFilter f = new AlteonConfigFilter();
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
			System.out.println(mo.getMoClass() + " : " + mo.getMoName());
		}

		System.exit(0);
	}

	private final ALTEON_CHEETAH_LAYER4_MIB MIB = new ALTEON_CHEETAH_LAYER4_MIB();

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
		return MIB.slbCurCfgVirtServerIndex;
	}

	private List<Integer> bitmap(byte[] values) {
		int serverIndex = 0;
		List<Integer> serverIndexesInGroup = new ArrayList<Integer>();

		serverIndex = 0;
		serverIndexesInGroup.clear();

		for (byte b : values) {
			if (b == 0x00) {
				serverIndex += 8;
				continue;
			}
			else {
				for (int j = 0; j < 8; j++) {
					serverIndex++;
					if ((b & 128) == 128) serverIndexesInGroup.add(serverIndex);

					b = (byte) (b << 1);
				}
			}
		}
		return serverIndexesInGroup;
	}

	private String getLb(int code) {
		switch (code) {
		case 1:
			return "roundRobin";
		case 2:
			return "leastConnections";
		case 3:
			return "minMisses";
		case 4:
			return "hash";
		case 5:
			return "response";
		case 6:
			return "bandwidth";
		case 7:
			return "phash";
		default:
			return "unkown";
		}
	}

	private int getStatus(int code) {
		switch (code) {
		case 2:
			return 1;
		case 3:
			return 0;
		default:
			return -1;
		}
	}

	private void getVipNRip(MoNode node, ConfigMo configMo, List<MoVip> vipMoList, List<MoRip> ripMoList) {

		try {

			List<String> oids = new ArrayList<String>();

			oids.add(MIB.slbCurCfgVirtServerIndex);
			oids.add(MIB.slbCurCfgVirtServerIpAddress);
			oids.add(MIB.slbCurCfgVirtServerState);

			List<OidValue>[] valueList = snmpwalk(node, oids.toArray(new String[oids.size()]));
			List<OidValue> serviceList = null;
			OidValue portArr[];
			OidValue groupArr[];
			OidValue ovRipArr[];
			MoVip vip;
			MoRip rip;
			int vIndex, sIndex, rIndex;

			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoVip.MO_CLASS);
			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoRip.MO_CLASS);

			for (int i = 0; i < valueList[0].size(); i++) {

				vIndex = valueList[0].get(i).getInt();
				serviceList = snmpwalk(node, MIB.slbCurCfgVirtServiceIndex + "." + vIndex);

				for (OidValue value : serviceList) {

					sIndex = value.getInt();
					oids.clear();
					oids.add(MIB.slbCurCfgVirtServiceVirtPort + "." + vIndex + "." + sIndex);
					oids.add(MIB.slbCurCfgVirtServiceRealGroup + "." + vIndex + "." + sIndex);
					oids.add(MIB.slbCurCfgVirtServiceRealPort + "." + vIndex + "." + sIndex);
					portArr = snmpget(node, oids.toArray(new String[oids.size()]));

					oids.clear();

					oids.add(MIB.slbCurCfgGroupMetric + "." + portArr[1].getValue());
					oids.add(MIB.slbCurCfgGroupRealServers + "." + portArr[1].getValue());

					groupArr = snmpget(node, oids.toArray(new String[oids.size()]));

					vip = new MoVip(0, 0, portArr[1].getValue(), valueList[1].get(i).getValue(), portArr[0].getInt(),
							getLb(groupArr[0].getInt()));

					vip.setStatusVip(getStatus(valueList[2].get(i).getInt()));

					vipMoList.add(vip);

					List<Integer> groupIndexList = bitmap(groupArr[1].getBytes());
					oids.clear();
					List<String> realStatusOid = new ArrayList<String>();

					for (Integer groupIndex : groupIndexList) {
						oids.add(MIB.slbCurCfgRealServerIpAddr + "." + groupIndex);
						realStatusOid.add(MIB.slbCurCfgRealServerState + "." + groupIndex);
					}

					OidValue ripStateArr[];

					ovRipArr = snmpget(node, oids.toArray(new String[oids.size()]));
					ripStateArr = snmpget(node, realStatusOid.toArray(new String[oids.size()]));

					for (int j = 0; j < ovRipArr.length; j++) {

						rIndex = Integer.parseInt(ovRipArr[j].getInstance(MIB.slbCurCfgRealServerIpAddr));

						rip = new MoRip();
						MoRip.set(rip, vip.getMoName(), portArr[1].getValue(), ovRipArr[j].getValue(),
								portArr[0].getInt());

						if (exist(ripMoList, rip)) continue;

						rip.setStatusRip(getStatus(ripStateArr[j].getInt()));

						rip.setUserProperties("vServerIndex=" + vIndex + ",vServiceIndex=" + sIndex
								+ ",rServerIndex=" + rIndex);

						ripMoList.add(rip);
					}

					for (OidValue ip : ovRipArr) {
						rIndex = Integer.parseInt(ip.getInstance(MIB.slbCurCfgRealServerIpAddr));
						rip = new MoRip();
						MoRip.set(rip, vip.getMoName(), portArr[1].getValue(), ip.getValue(), portArr[0].getInt());

						if (exist(ripMoList, rip)) continue;

						rip.setUserProperties("vServerIndex=" + vIndex + ",vServiceIndex=" + sIndex
								+ ",rServerIndex=" + rIndex);

						ripMoList.add(rip);
					}
				}
			}
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}
	}

	private boolean exist(List<MoRip> ripMoList, MoRip rip) {
		for (MoRip e : ripMoList) {
			if (e.getMoName().equals(rip.getMoName())) return true;
		}
		return false;
	}

}
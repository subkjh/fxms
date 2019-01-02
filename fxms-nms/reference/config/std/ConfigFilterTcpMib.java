package com.daims.dfc.filter.config.std;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import subkjh.dao.control.DaoFactory;
import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.CLogger;
import subkjh.log.CLogger.LEVEL;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.api.MoApi;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoSocketPort;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.mib.TCP_MIB;
import com.daims.dfc.mib.TCP_MIB.Conn;
import com.daims.dfc.mib.UDP_MIB;
import com.daims.dfc.service.conf.ConfApi;

/**
 * 소켓 포트를 가져오는 ConfigFilter<br>
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterTcpMib extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8990256289383973985L;

	public static void main(String[] args) throws Exception {
		CLogger.logger.setLevel(LEVEL.trace);
		try {
			DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		MoNode node = MoApi.getApi().getMoNodeByIp("167.1.21.31", false);

		ConfigMo configMo = ConfApi.getApi().getConfigMo(node.getMoNo(), null);
		ConfigFilterTcpMib c = new ConfigFilterTcpMib();
		try {
			c.filter(configMo, null, null);
			for (MoSocketPort port : configMo.getMoList(MoSocketPort.class))
				System.out.println(port);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private TCP_MIB MIB;

	public ConfigFilterTcpMib() {
		MIB = new TCP_MIB();
	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {
			List<MoSocketPort> moList = makeTcp(node);
			if (moList != null && moList.size() > 0) {
				configMo.addMoListDetected(moList);
			}
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

		try {
			List<MoSocketPort> moList = makeUdp(node);
			if (moList != null && moList.size() > 0) {
				configMo.addMoListDetected(moList);
			}
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

		// List<OidValue> valueList = getSnmpUtil().snmpwalk(node,
		// ".1.3.6.1.2.1.7.7.1.8");
		// for ( OidValue value : valueList ) {
		// System.out.println(value);
		// }

		return Ret.OK;
	}

	private List<MoSocketPort> makeTcp(MoNode node) throws Exception {

		List<OidValue> valueList = snmpwalk(node, MIB.tcpConnState);

		if (valueList == null) return null;

		List<MoSocketPort> moList = new ArrayList<MoSocketPort>();

		Conn conn;
		int index = MIB.tcpConnState.length() + 1;
		for (OidValue value : valueList) {
			if (value.getInt() == MIB.tcpConnState_listen) {
				conn = MIB.parse(value.getOid().substring(index));

				moList.add(new MoSocketPort(0, 0, "TCP " + conn.localPort, node.getIpAddress(), Integer
						.parseInt(conn.localPort), MoSocketPort.PROTOCOL_TCP, ""));
			}
		}

		return moList;
	}

	private List<MoSocketPort> makeUdp(MoNode node) throws Exception {

		UDP_MIB UDP = new UDP_MIB();

		List<OidValue> valueList = snmpwalk(node, UDP.udpLocalPort);

		if (valueList == null) return null;

		List<MoSocketPort> moList = new ArrayList<MoSocketPort>();

		for (OidValue value : valueList) {
			moList.add(new MoSocketPort(0, 0, "UDP " + value.getInt(), node.getIpAddress(), value.getInt(),
					MoSocketPort.PROTOCOL_UDP, ""));
		}

		return moList;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoSocketPort.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return MIB.tcpConnState;
	}

}

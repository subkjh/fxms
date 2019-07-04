package fxms.nms.fxactor.snmpping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import subkjh.bas.co.log.Logger;

import com.adventnet.snmp.snmp2.SnmpAPI;

import fxms.bas.co.utils.CheckUtil;
import fxms.bas.mo.Mo;
import fxms.bas.po.PsVo;
import fxms.bas.poller.exp.PollingTimeoutException;
import fxms.nms.NmsCodes;
import fxms.nms.co.snmp.SnmpUtil;
import fxms.nms.co.snmp.mib.MibCiscoPingTable;
import fxms.nms.co.snmp.vo.OidValue;
import fxms.nms.mo.NeMo;
import fxms.nms.mo.VpnMo;
import fxms.nms.mo.property.MoSnmppable;

/**
 * VPN PING 수집 방법은 아래와 같다. <br>
 * 
 * 1. SNMP community 문자열을 DB 에서 가져온다. <br>
 * 2. 아래의 순서로 MIB 을 전송한다. ( SET 메소드로 전송한다. )<br>
 * A. 1.3.6.1.4.1.9.9.16.1.1.1.16.x ( 기존에 VPN PING 이 설정되어 있으면 이를 삭제한다. )<br>
 * B. 1.3.6.1.4.1.9.9.16.1.1.1.16.x ( 새로운 VPN PING 설정을 한다. )<br>
 * C. 1.3.6.1.4.1.9.9.16.1.1.1.2.x<br>
 * D. 1.3.6.1.4.1.9.9.16.1.1.1.3.x ( VPN PING 대상 장비를 설정한다. )<br>
 * E. 1.3.6.1.4.1.9.9.16.1.1.1.4.x ( PING 시도 개수를 설정한다. )<br>
 * F. 1.3.6.1.4.1.9.9.16.1.1.1.5.x ( PING 패킷 크기를 설정한다. )<br>
 * G. 1.3.6.1.4.1.9.9.16.1.1.1.6.x ( PING timeout 시간을 설정한다. )<br>
 * H. 1.3.6.1.4.1.9.9.16.1.1.1.7.x<br>
 * I. 1.3.6.1.4.1.9.9.16.1.1.1.8.x<br>
 * J. 1.3.6.1.4.1.9.9.16.1.1.1.15.x ( VPN PING 아이디를 설정한다. )<br>
 * K. 1.3.6.1.4.1.9.9.16.1.1.1.17.x ( VRF 이름을 설정한다. )<br>
 * L. 1.3.6.1.4.1.9.9.16.1.1.1.16.x<br>
 * 3. 1초 간격으로 아래의 MIB 을 전송하여서 VPN PING 결과가 저장되었는지를 확인한다. ( GET 메소드로 전송한다. )<br>
 * A. 1.3.6.1.4.1.9.9.16.1.1.1.14.x<br>
 * 4. VPN PING 결과를 가져오기 위해서 아래의 MIB 을 전송한다. ( GET 메소드로 전송한다. )<br>
 * A. 1.3.6.1.4.1.9.9.16.1.1.1.9.x ( PING 전송 개수 )<br>
 * B. 1.3.6.1.4.1.9.9.16.1.1.1.10.x ( PING 응답 메시지 수신 개수 )<br>
 * C. 1.3.6.1.4.1.9.9.16.1.1.1.11.x ( MIN RTT )<br>
 * D. 1.3.6.1.4.1.9.9.16.1.1.1.12.x ( AVG RTT )<br>
 * E. 1.3.6.1.4.1.9.9.16.1.1.1.13.x ( MAX RTT )<br>
 * 5. 아래의 MIB 을 전송하여서 VPN PING 결과를 장비에서 삭제한다. ( SET 메소드로 전송한다. )<br>
 * A. 1.3.6.1.4.1.9.9.16.1.1.1.16.x ( 기존에 VPN PING 이 설정되어 있으면 이를 삭제한다. )<br>
 * 
 * @author Ahn
 * 
 */
public class SnmpPingVpnActor extends SnmpPingFxActor {

	public static void main(String[] args) {
		SnmpPingVpnActor c = new SnmpPingVpnActor();
		String ss[] = new String[] { "10.0.0.1", "200.101.102.1" };
		for (String ip : ss) {
			byte s[] = c.ipTohex(ip);
			for (byte b : s) {
				System.out.print(" " + String.format("%02x", b));
			}
			System.out.println();
		}
	}

	private MibCiscoPingTable OID = new MibCiscoPingTable();

	private final int PING_PACKET_COUNT = 4;

	private Map<String, VpnMo> serialNoMap = new HashMap<String, VpnMo>();

	/** 1 */
	private final int TRUTHVALUE_TRUE = 1;

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof NeMo) == false) {
			return null;
		}

		NeMo node = (NeMo) mo;

		List<VpnMo> moList = node.getMoConfig().getChildren(VpnMo.class);
		if (moList == null || moList.size() == 0)
			return null;

		serialNoMap.clear();

		List<VpnMo> moListValue = setVpnPing(node, moList, 100, getSnmpUtil());

		// 라우터가 ping을 완료하기까지 대기함.
		Thread.sleep(3000);

		getVpnPingStatus(node, moListValue, getSnmpUtil());

		return makeValue(node, moList);

	}

	/**
	 * 하나의 VPN에 대한 Vrf Ping 테스트합니다.
	 * 
	 * @param node
	 * @param ipAddress
	 * @param vrfName
	 * @return
	 * @throws Exception
	 */
	public int test(NeMo node, String ipAddress, String vrfName, int startIndex, SnmpUtil snmpUtil) throws Exception {

		VpnMo vpn = new VpnMo();
		vpn.setIpAddress(ipAddress);
		vpn.setVrfName(vrfName);

		List<VpnMo> moList = new ArrayList<VpnMo>();
		moList.add(vpn);

		List<VpnMo> moListValue = setVpnPing(node, moList, startIndex, snmpUtil);

		// 라우터가 ping을 완료하기까지 대기함.
		Thread.sleep(3000);

		getVpnPingStatus(node, moListValue, snmpUtil);

		return vpn.getStatusVrfPing();
	}

	private List<VpnMo> getVpnPingStatus(MoSnmppable node, List<VpnMo> moList, SnmpUtil snmpUtil) {

		List<String> oidList = new ArrayList<String>();
		List<OidValue> lastOidList = new ArrayList<OidValue>();
		List<OidValue> varList;
		VpnMo mo;

		try {

			for (String serialNo : serialNoMap.keySet()) {
				oidList.add(OID.ciscoPingCompleted + "." + serialNo);
				lastOidList.add(new OidValue(OID.ciscoPingEntryStatus + "." + serialNo, SnmpAPI.STRING, "6"));
			}

			while (true) {

				varList = snmpUtil.snmpget(node, oidList.toArray(new String[oidList.size()]));
				if (varList == null)
					return null;
				oidList.clear();

				for (OidValue var : varList) {
					if (var.isNull())
						continue;

					if (var.getInt() != TRUTHVALUE_TRUE) {
						oidList.add(var.getOid());
					}
				}
				if (oidList.size() == 0)
					break;

				Thread.sleep(1000);
			}

			oidList.clear();
			varList.clear();

			for (String serialNo : serialNoMap.keySet()) {
				oidList.add(OID.ciscoPingReceivedPackets + "." + serialNo);
				// oidList.add(OID.ciscoPingAvgRtt + "." + serialNo);
				// oidList.add(OID.ciscoPingMaxRtt + "." + serialNo);
				// oidList.add(OID.ciscoPingMinRtt + "." + serialNo);
			}

			varList = snmpUtil.snmpget(node, oidList.toArray(new String[oidList.size()]));

			if (varList == null)
				return null;

			for (OidValue var : varList) {
				if (var.isNull())
					continue;
				mo = serialNoMap.get(var.getInstance(1));
				if (mo != null) {
					if (var.getInt() > 0)
						mo.setStatusVrfPing(1);
					else
						mo.setStatusVrfPing(0);
				}
			}

			snmpUtil.snmpset(node, (OidValue[]) lastOidList.toArray());

			return moList;
		} catch (Exception e) {
			return null;
		}

	}

	private byte[] ipTohex(String ip) {
		StringTokenizer st = new StringTokenizer(ip, ".");
		String str[];
		if (st.countTokens() == 4) {
			str = new String[4];
			while (st.hasMoreTokens()) {
				str[0] = st.nextToken();
				str[1] = st.nextToken();
				str[2] = st.nextToken();
				str[3] = st.nextToken();
			}

			int ai[] = new int[] { Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]),
					Integer.parseInt(str[3]) };
			byte bytes[] = new byte[4];
			for (int i = 0; i < 4; i++) {
				bytes[i] = (byte) ai[i];
				// System.out.println(bytes[i]);
			}

			return bytes;
		}

		return null;
	}

	private List<PsVo> makeValue(NeMo node, List<VpnMo> moList) {

		List<PsVo> valueList = new ArrayList<PsVo>();

		for (VpnMo vpnPing : moList) {
			if (vpnPing.isMngYn()) {

				PsVo value = new PsVo(vpnPing, null, NmsCodes.PsItem.VpnPingStatus, vpnPing.getStatusVrfPing());
				value.setMoInstance(vpnPing.getMoName());
				valueList.add(value);
			}
		}

		return valueList;
	}

	private List<VpnMo> setVpnPing(MoSnmppable node, List<VpnMo> moList, int startIndex, SnmpUtil snmpUtil) {

		List<OidValue> oidList = new ArrayList<OidValue>();

		int i = startIndex;

		for (VpnMo mo : moList) {

			if (CheckUtil.isIpAddress(mo.getIpAddress()) == false)
				continue;

			try {
				oidList.clear();
				oidList.add(new OidValue(OID.ciscoPingEntryStatus + "." + i, SnmpAPI.INTEGER, 6));
				snmpUtil.snmpset(node, oidList.toArray(new OidValue[oidList.size()]));

				oidList.clear();
				oidList.add(new OidValue(OID.ciscoPingEntryStatus + "." + i, SnmpAPI.INTEGER, 5));
				snmpUtil.snmpset(node, oidList.toArray(new OidValue[oidList.size()]));

				oidList.clear();
				oidList.add(new OidValue(OID.ciscoPingProtocol + "." + i, SnmpAPI.INTEGER, 1));
				oidList.add(new OidValue(OID.ciscoPingAddress + "." + i, SnmpAPI.STRING, ipTohex(mo.getIpAddress())));
				oidList.add(new OidValue(OID.ciscoPingPacketCount + "." + i, SnmpAPI.INTEGER, PING_PACKET_COUNT));
				oidList.add(new OidValue(OID.ciscoPingPacketSize + "." + i, SnmpAPI.INTEGER, 64));
				oidList.add(new OidValue(OID.ciscoPingPacketTimeout + "." + i, SnmpAPI.INTEGER, 2000));
				oidList.add(new OidValue(OID.ciscoPingDelay + "." + i, SnmpAPI.INTEGER, 2000));
				oidList.add(new OidValue(OID.ciscoPingEntryOwner + "." + i, SnmpAPI.STRING, "TESTER"));
				oidList.add(new OidValue(OID.ciscoPingVrfName + "." + i, SnmpAPI.STRING, mo.getVrfName()));
				snmpUtil.snmpset(node, oidList.toArray(new OidValue[oidList.size()]));

				oidList.clear();
				oidList.add(new OidValue(OID.ciscoPingEntryStatus + "." + i, SnmpAPI.INTEGER, "1"));

				snmpUtil.snmpset(node, oidList.toArray(new OidValue[oidList.size()]));

				serialNoMap.put(i + "", mo);

				i++;

			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}

		return moList;

	}
}

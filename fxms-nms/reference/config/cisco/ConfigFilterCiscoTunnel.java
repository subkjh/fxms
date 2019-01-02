package com.daims.dfc.filter.config.cisco;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoTunnel;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

public class ConfigFilterCiscoTunnel extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5177424701535312015L;

	public static String byteArrayToHex(byte[] ba) {
		String[] ss = new String[4];

		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
			ss[x] = hex2decimal((hexNumber.substring(hexNumber.length() - 2)));
		}

		String s = ss[0] + "." + ss[1] + "." + ss[2] + "." + ss[3];

		return s;
	}

	public static String hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val + "";
	}

	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	} // byte[] to hex
		// public static void main(String[] args) throws Exception {
		// ConfApi.api = new ConfApiImpl();
		// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
		// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("211.208.236.207", 161, 1,
		// "kiupnms", MoNodeSnmpImpl.class);
		// // MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("14.4.44.156", 161, 1,
		// "public", MoNodeSnmpImpl.class);
		// FilterConfigTunnel filter = new FilterConfigTunnel();
		// filter.filter(new ConfigMo(node));
		//
		// ConfApi.api.snmputil.close();
		// }

	private final String cipSecTunActiveTime = ".1.3.6.1.4.1.9.9.171.1.3.2.1.10";

	private final String cipSecTunIndex = ".1.3.6.1.4.1.9.9.171.1.3.2.1.2";

	private final String cipSecTunLocalAddr = ".1.3.6.1.4.1.9.9.171.1.3.2.1.4";

	private final String cipSecTunRemoteAddr = ".1.3.6.1.4.1.9.9.171.1.3.2.1.5";

	private final String cipSecTunStatus = ".1.3.6.1.4.1.9.9.171.1.3.2.1.51";

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {
			List<MoTunnel> moList = tunnelModule(node);
			if (moList != null && moList.size() > 0) {
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoTunnel.MO_CLASS);
				configMo.addMoListDetected(moList);
				return new Ret(moList.size());
			}
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}

		// 위 처리중 오류가 발생하더라고 다른 작업은 계속 수행을 위해 OK를 리턴합니다.

		return Ret.OK;

	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoTunnel.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return cipSecTunIndex;
	}

	private List<MoTunnel> tunnelModule(SnmpMo node) {
		List<OidValue> valueList;
		OidValue ovArr[];
		List<MoTunnel> moList = new ArrayList<MoTunnel>();
		String dotIdx;
		String snmpIndex;

		try {
			valueList = snmpwalk(node, cipSecTunIndex);
			for (OidValue value : valueList) {
				snmpIndex = value.getInstance(cipSecTunIndex);
				MoTunnel tunnel = new MoTunnel();
				dotIdx = "." + snmpIndex;
				tunnel.setSnmpIndex(snmpIndex);
				tunnel.setTnIndex(Long.parseLong(snmpIndex));

				ovArr = snmpget(node //
						, cipSecTunLocalAddr + dotIdx //
						, cipSecTunRemoteAddr + dotIdx //
						, cipSecTunActiveTime + dotIdx //
						, cipSecTunStatus + dotIdx);

				tunnel.setLocalIp(byteArrayToHex(ovArr[0].getBytes()));
				tunnel.setRemoteIp(byteArrayToHex(ovArr[1].getBytes()));
				tunnel.setActiveTime(Long.parseLong(ovArr[2].getValue()));
				tunnel.setStatusTunnel(Integer.parseInt(ovArr[3].getValue()));
				tunnel.setMoName(tunnel.getLocalIp() + "-" + tunnel.getRemoteIp());
				tunnel.setManaged(true);
				tunnel.setMoType(tunnel.getMoClass());

				moList.add(tunnel);
			}

			return moList;
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

		return null;
	}

}

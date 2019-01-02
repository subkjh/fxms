package com.daims.dfc.filter.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceCfg;
import subkjh.service.services.ServiceImpl;
import subkjh.utils.FileUtil;

import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;

/**
 * 
 * 장비 OS 버전 수집 필터<br>
 * config.xml 설정 방법<br>
 * &lt;filter name="OsVerConfigFilter"
 * javaClass="com.daims.skb.bms.filter.OsVerConfigFilter"&gt;&lt;/filter&gt;
 * 
 * <br>
 * <br>
 * 
 * HOME/deploy/conf/filter/config/OsVerConfigFilter.txt에 다음과 같이 설정합니다.<br>
 * 모델명 수집OID<br>
 * ...<br>
 * 
 * @author subkjh
 * @since 2015.08.06
 */
public class NodeSetFieldConfigFilter extends ConfigFilterSnmpNode {

	public static void main(String[] args) {

		NodeSetFieldConfigFilter filter = new NodeSetFieldConfigFilter();
		try {
			List<String[]> list = filter.getOid("V5616F");

			System.out.println("V5616F");
			if (list != null) {
				for (String[] s : list) {
					System.out.println(Arrays.toString(s));
				}
			}

			list = filter.getOid("S5328");
			System.out.println("S5328");
			if (list != null) {
				for (String[] s : list) {
					System.out.println(Arrays.toString(s));
				}
			}

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private final String PATTERN = "( |\t)+";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1909942171266139739L;

	/** key : 모델명, value : { OID, fieldName } */
	private static Map<String, List<String[]>> oidMap = null;

	// private static final String dsOsVersion =
	// ".1.3.6.1.4.1.6296.9.1.1.1.3.0";
	// private static final String entPhysicalSoftwareRev =
	// ".1.3.6.1.2.1.47.1.1.1.1.10";
	// private static final String PXX = ".1.3.6.1.4.1.7800.2.2.1.3.1.2";

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = configMo.getNode();
		if (node.isSnmp() == false) return Ret.OK;

		try {
			String value;
			List<String[]> oidList = getOid(node.getModelName());
			if (oidList != null) {
				for (String[] of : oidList) {
					value = getSnmpValue(node, of[0]);
					if (value != null) {
						node.set(of[1], value);
					}
				}
			}
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return null;
	}

	private synchronized List<String[]> getOid(String modelName) throws Exception {

		if (oidMap == null) {

			oidMap = new HashMap<String, List<String[]>>();

			String filename = ServiceCfg.getFile(ServiceCfg.getHomeDeployConf(), "filter", "config",
					"NodeSetFieldConfigFilter.txt");
			File file = new File(filename);
			if (file.exists() == false) return null;

			List<String> lineList = FileUtil.getLines(file);
			String no[];
			List<String[]> e;

			for (String line : lineList) {
				if (line == null || line.trim().length() == 0) continue;
				if (line.charAt(0) == '#') continue;
				no = line.split(PATTERN);

				if (no.length >= 3) {
					if (no[1].charAt(0) != '.') no[1] = "." + no[1];
					e = oidMap.get(no[0]);
					if (e == null) {
						e = new ArrayList<String[]>();
						oidMap.put(no[0], e);
					}
					e.add(new String[] { no[1], no[2] });
					ServiceImpl.logger.debug("model oid add : " + no[0] + "." + no[2] + " (" + no[1] + ")");
				}

			}

		}

		return oidMap.get(modelName);
	}

	private String getSnmpValue(MoNode node, String oid) throws Exception {
		String[] ss = oid.split("\\.");
		if (ss[(ss.length - 1)].equals("0")) {
			try {
				OidValue ovArr[] = snmpget(node, new String[] { oid });
				if (ovArr.length > 0) return ovArr[0].getValue();
			}
			catch (SnmpTimeoutException e) {
				throw e;
			}
			catch (Exception e) {
				ServiceImpl.logger.debug("OID 확인 실패 " + e.getMessage(), new Class[0]);
				return null;
			}
		}
		else {
			try {
				List<OidValue> ovList = snmpwalk(node, oid);
				for (OidValue ov : ovList) {
					if (ov != null && ov.getValue() != null && ov.getValue().trim().length() > 0) {
						return ov.getValue().trim();
					}
				}
			}
			catch (SnmpTimeoutException e) {
				throw e;
			}
			catch (Exception e) {
				ServiceImpl.logger.debug("OID 확인 실패 " + e.getMessage(), new Class[0]);
				return null;
			}

		}

		return null;

	}

}
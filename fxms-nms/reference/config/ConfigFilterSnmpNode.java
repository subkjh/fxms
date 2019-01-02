package com.daims.dfc.filter.config;

import java.util.List;

import subkjh.exception.TimeoutException;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.HasModel;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.lib.snmp.FilterValidChecker;
import com.daims.dfc.lib.snmp.SnmpUtil;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpErrorException;
import com.daims.dfc.lib.snmp.exception.SnmpNotFoundOidException;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;

/**
 * SNMP를 이용한 필터들의 공통 기능을 가지고 있는 필터
 * 
 * @author subkjh
 * @since 3.0 : 2013.08.21
 */
public abstract class ConfigFilterSnmpNode extends ConfigFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1156881868284387227L;

	private SnmpUtil snmputil;

	@Override
	public boolean isValid(Mo mo) throws TimeoutException {

		if ((mo instanceof SnmpMo) == false) {
			ServiceImpl.logger.debug("not SnmpNode");
			return false;
		}
		SnmpMo node = (SnmpMo) mo;
		String modelNo = "";
		if (node instanceof HasModel) {
			modelNo = ((HasModel) node).getModelNo() + "";
		}

		try {
			if (isIp(node.getIpAddress()) == false) {
				ServiceImpl.logger.trace("IP(" + node.getIpAddress() + ") NOT IP");
				return false;
			}
		}
		catch (Exception e1) {
			ServiceImpl.logger.trace("IP(" + node.getIpAddress() + ") NOT IP");
			return false;
		}

		String oid = getOidToCheck();
		if (oid == null || oid.trim().length() == 0) return true;

		if (node.isSnmp() == false) {
			ServiceImpl.logger.trace(node + " NOT SnmpMo");
			return true;
		}

		FilterValidChecker oidPool = FilterValidChecker.getInstance();
		try {
			return oidPool.valid(modelNo, oid, getSnmpUtil(), node);
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
			return false;
		}
	}

	/**
	 * 사용할 SnmpUtil를 설정합니다.
	 * 
	 * @param snmputil
	 */
	public void setSnmpUtil(SnmpUtil snmputil) {
		this.snmputil = snmputil;
	}

	/**
	 * 
	 * 해당 필터를 지원 여부를 확인할 OID<br>
	 * NULL이거나 공백이면 무조건 처리하는 것으로 판단합니다.
	 * 
	 * @return 해당 필터를 지원 여부를 확인할 OID
	 */
	protected String getOidToCheck() {
		return getProperty("OID_TO_CHECK", null);
	}

	/**
	 * ConfigMo에서 MoNodeSnmp를 찾아 제공합니다.<br>
	 * MoNodeSnmp가 아니고 MoNodeSnmp이지만 SNMP정보가 정확하지 않으면 즉, SNMP를 수행할 수 없으면 null을
	 * 제공합니다.
	 * 
	 * @param configMo
	 * @return 유효한 MoNodeSnmp
	 */
	protected MoNode getSnmpNode(ConfigMo configMo) {

		ServiceImpl.logger.debug(configMo);

		MoNode node = (MoNode) configMo.getNode();

		if (node.isSnmp() == false) return null;

		return node;
	}

	/**
	 * 사용할 SnmpUtil<br>
	 * 지정되지 않은 경우 ConfApi.api.snmputil를 제공합니다.
	 * 
	 * @return 사용할 SnmpUtil
	 */
	protected SnmpUtil getSnmpUtil() throws Exception {
		return snmputil != null ? snmputil : SnmpUtil.getSnmpUtil("ConfigF");
	}

	/**
	 * IP주소인지 여부
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected boolean isIp(String s) throws Exception {

		if (s == null) return false;
		if (s.trim().length() == 0) return false;

		String ss[] = s.split("\\.");
		if (ss.length != 4) return false;

		for (int i = 0; i < ss.length; i++) {
			if (Integer.parseInt(ss[i]) < 0 || Integer.parseInt(ss[i]) > 255) return false;
		}

		return true;
	}

	protected OidValue[] snmpget(SnmpMo node, String... oidArr) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException, Exception {

		for (int i = 0; i < 2; i++) {
			try {
				List<OidValue> varList = getSnmpUtil().snmpget(node, oidArr);
				if (varList == null) return null;

				return varList.toArray(new OidValue[varList.size()]);
			}
			catch (SnmpTimeoutException e) {
				if (i == 1) throw e;
			}
			catch (SnmpErrorException e) {
				throw e;
			}
			catch (SnmpNotFoundOidException e) {
				throw e;
			}

			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e1) {
			}
		}

		return null;

	}

	protected OidValue[] snmpnext(SnmpMo node, String... oidArr) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException, Exception {

		for (int i = 0; i < 2; i++) {
			try {
				List<OidValue> varList = getSnmpUtil().snmpgetnext(node, oidArr);
				if (varList == null) return null;

				return varList.toArray(new OidValue[varList.size()]);
			}
			catch (SnmpTimeoutException e) {
				if (i == 1) throw e;
			}
			catch (SnmpErrorException e) {
				throw e;
			}
			catch (SnmpNotFoundOidException e) {
				throw e;
			}

			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e1) {
			}
		}

		return null;

	}

	protected List<OidValue>[] snmpwalk(SnmpMo node, String... oidArr) throws SnmpTimeoutException,
			SnmpErrorException, SnmpNotFoundOidException, Exception {

		for (int i = 0; i < 2; i++) {
			try {
				return getSnmpUtil().snmpwalk(node, oidArr);
			}
			catch (SnmpTimeoutException e) {
				if (i == 1) throw e;
			}
			catch (SnmpErrorException e) {
				throw e;
			}
			catch (SnmpNotFoundOidException e) {
				throw e;
			}

			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e1) {
			}
		}

		return null;

	}

	protected List<OidValue> snmpwalk(SnmpMo node, String oid) throws SnmpTimeoutException, SnmpErrorException,
			SnmpNotFoundOidException, Exception {

		for (int i = 0; i < 2; i++) {
			try {
				return getSnmpUtil().snmpwalk(node, oid);
			}
			catch (SnmpTimeoutException e) {
				if (i == 1) throw e;
			}
			catch (SnmpErrorException e) {
				throw e;
			}
			catch (SnmpNotFoundOidException e) {
				throw e;
			}

			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e1) {
			}
		}

		return null;

	}
}

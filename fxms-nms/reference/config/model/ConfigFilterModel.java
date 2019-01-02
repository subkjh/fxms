package com.daims.dfc.filter.config.model;

import java.lang.reflect.Method;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.conf.MoConf;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.filter.config.model.beans.ModelConf;
import com.daims.dfc.filter.config.model.beans.ModelConfA;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpErrorException;
import com.daims.dfc.lib.snmp.exception.SnmpNotFoundOidException;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;
import com.daims.dfc.service.conf.ConfApi;

/**
 * MO 정보를 동적으로 가져오는 필터
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterModel extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4941845615122347928L;

	// public static void main(String[] args) throws Exception {
	//
	// try {
	// DaoFactory.getInstance().addDataBase(new
	// File("deploy/conf/databases.xml"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// CLogger.logger.setLevel(LEVEL.raw);
	// ConfApi.api = new ConfApiImpl();
	// ConfApi.api.init();
	// ConfApi.api.snmputil = SnmpUtil.createSnmpUtil();
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("167.1.21.113", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	//
	// node.setModelNo(1043);
	//
	// DynamicMoSnmpConfigFilter filter = new DynamicMoSnmpConfigFilter();
	// ConfigMo configMo = new ConfigMo(node);
	//
	// filter.filter(configMo);
	//
	// ConfApi.api.snmputil.close();
	//
	// if (configMo.moList(MoInterface.MO_CLASS) != null) {
	// for (Mo mo : configMo.moList(MoInterface.MO_CLASS)) {
	// System.out.println(mo);
	// }
	// }
	//
	// String[] keys = configMo.getAttachObjectMapKey();
	// if (keys != null) {
	// for (String key : keys) {
	// System.out.println(key + "=" + configMo.getAttachObject(key));
	// }
	// }
	// }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Ret filter(ConfigMo configMo,String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		List<ModelConf> moClassList = ConfApi.getApi().getMoClassList(configMo);
		if (moClassList == null || moClassList.size() == 0) {
			ServiceImpl.logger.trace("No dynamic OID");
			return Ret.OK;
		}

		List objList;
		Object obj;
		for (ModelConf moAttr : moClassList) {

			if (ServiceImpl.logger.isRaw()) {
				ServiceImpl.logger.raw(moAttr);
			}

			try {
				obj = moAttr.createObjectBean();
			} catch (Exception e) {
				ServiceImpl.logger.error(e);
				continue;
			}

			objList = find(node, moAttr);
			if (obj instanceof Mo) {
				configMo.addMoListDetected(objList);
			} else if (objList != null) {
				configMo.addAttachObject("MoConf_" + moAttr.getConfClass(), objList);
			}
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return null;
	}

	private List<Object> find(SnmpMo node, ModelConf moAttr) throws SnmpTimeoutException, SnmpNotFoundOidException,
			SnmpErrorException, Exception {

		List<String> indexList = new ArrayList<String>();

		if (moAttr.hasIndex()) {
			List<OidValue> varList = snmpwalk(node, moAttr.getOidIndex());
			if (varList != null && varList.size() > 0) {
				for (int i = 0; i < varList.size(); i++) {
					indexList.add(varList.get(i).getValue());
				}
			}
		} else {
			indexList.add("");
		}

		if (ServiceImpl.logger.isTrace()) {
			ServiceImpl.logger.trace("indexes : " + indexList.toString());
		}

		List<Object> objList = new ArrayList<Object>();
		Object obj;
		for (String oidIndex : indexList) {
			obj = moAttr.createObjectBean();

			if (setMo(node, obj, moAttr, oidIndex)) {

				if (obj instanceof Mo) {
					Mo mo = (Mo) obj;
					if (mo.getMoName() != null && mo.getMoName().trim().length() > 0) {
						if (ServiceImpl.logger.isTrace()) {
							ServiceImpl.logger.trace("add : " + mo.toString());
						}
						objList.add(mo);
					} else {
						if (ServiceImpl.logger.isTrace()) {
							ServiceImpl.logger.trace("ignore : " + mo.toString());
						}
					}
				} else {
					objList.add(obj);
				}
			}
		}

		return objList;
	}

	@SuppressWarnings("rawtypes")
	private void set(Object target, String fieldName, Object value) throws Exception {

		if (ServiceImpl.logger.isRaw()) {
			ServiceImpl.logger.raw(fieldName + "=" + value);
		}

		if (value == null) return;

		String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		Method method = null;
		try {
			Method methods[] = target.getClass().getMethods();
			for (Method m : methods) {
				if (m.getName().equals(methodName) && m.getParameterTypes().length == 1) {
					method = m;
					break;
				}
			}
		} catch (Exception e) {
			throw new Exception(methodName + " : " + e.getMessage());
		}

		if (method == null) {
			if (ServiceImpl.logger.isRaw()) {
				ServiceImpl.logger.raw("not found the method : " + methodName + "(" + value + ")");
			}
			return;
		}

		String str = value == null ? null : value.toString();
		Class type = method.getParameterTypes()[0];

		if (type == String.class && value instanceof Clob) {
			method.invoke(target, value.toString());
		} else if (type == String.class) {
			method.invoke(target, str);
		} else if (type == Integer.TYPE || type == Integer.class) {
			method.invoke(target, (value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(str)));
		} else if (type == Long.TYPE || type == Long.class) {
			method.invoke(target, (value instanceof Number ? ((Number) value).longValue() : Long.parseLong(str)));
		} else if (type == Double.TYPE || type == Double.class) {
			method.invoke(target, (value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(str)));
		} else if (type == Short.TYPE || type == Short.class) {
			method.invoke(target, (value instanceof Number ? ((Number) value).shortValue() : Short.parseShort(str)));
		} else if (type == Float.TYPE || type == Float.class) {
			method.invoke(target, (value instanceof Number ? ((Number) value).floatValue() : Float.parseFloat(str)));
		} else if (type == Byte.TYPE || type == Byte.class) {
			method.invoke(target, (value instanceof Number ? ((Number) value).byteValue() : Byte.parseByte(str)));
		} else if (type == Boolean.TYPE || type == Boolean.class) {
			method.invoke(target, str.toLowerCase().equals("y") || str.toLowerCase().equals("true"));
		} else {
			method.invoke(target, value);
		}
	}

	private boolean setMo(SnmpMo node, Object mo, ModelConf moAttr, String index) {

		List<String> oidList = new ArrayList<String>();

		if (index == null || index.trim().length() == 0) {
			for (ModelConfA moAttrOid : moAttr.getMoOidList()) {
				if (moAttrOid.isSnmpOid()) {
					oidList.add(moAttrOid.getSnmpOid());
				}
			}
		} else {
			for (ModelConfA moAttrOid : moAttr.getMoOidList()) {
				if (moAttrOid.isSnmpOid()) {
					oidList.add(moAttrOid.getSnmpOid() + "." + index);
				}
			}

			if (mo instanceof Mo && index != null && index.trim().length() > 0) {
				((Mo) mo).setUserProperty("snmpIndex", index);
			} else if (mo instanceof MoConf) {
				((MoConf) mo).setConfKey(index);
			}

		}

		// 1. SNMP 내용이 있다면 처리합니다.
		if (oidList.size() > 0) {

			OidValue ovArr[];
			String value;

			try {
				ovArr = snmpget(node, oidList.toArray(new String[oidList.size()]));
			} catch (Exception e) {
				ServiceImpl.logger.fail(moAttr + ":" + e.getMessage());
				return false;
			}

			for (OidValue oidValue : ovArr) {
				for (ModelConfA moAttrOid : moAttr.getMoOidList()) {
					if (moAttrOid.match(oidValue.getOid())) {

						if ("mac".equals(moAttrOid.getSnmpType())) {
							value = oidValue.getMacAddress();
						} else if ("ip".equals(moAttrOid.getSnmpType())) {
							value = oidValue.getIpAddress();
						} else if ("dateAndTime".equals(moAttrOid.getSnmpType())) {
							value = oidValue.getHstimeDateAndTime();
						} else {
							value = oidValue.getValue();
						}

						try {
							set(mo, moAttrOid.getJavaFieldName(), moAttrOid.getValueStr(value));
						} catch (Exception e) {
							ServiceImpl.logger.fail(moAttrOid.getJavaFieldName() + "=" + moAttrOid.getValueStr(value));
							// ServiceImpl.logger.error(e);
						}
						break;
					}
				}
			}

		}

		// 2. SNMP가 아니고 일반텍스트 내용을 처리합니다.
		for (ModelConfA moAttrOid : moAttr.getMoOidList()) {
			if (moAttrOid.isSnmpOid() == false) {
				try {
					set(mo, moAttrOid.getJavaFieldName(), moAttrOid.getSnmpOid());
				} catch (Exception e) {
					ServiceImpl.logger.fail(moAttrOid.getJavaFieldName() + "=" + moAttrOid.getSnmpOid());
				}
			}
		}

		return true;
	}
}

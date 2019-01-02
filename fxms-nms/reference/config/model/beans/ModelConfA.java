package com.daims.dfc.filter.config.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.daims.dfc.common.beans.snmp.DynamicOidEnum;

/**
 * MO 필드와 매칭되는 OID 객체
 * 
 * @author subkjh
 * @since 2013.07.16
 */
public class ModelConfA implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1490206300311882864L;

	/** 모델번호 */
	private int modelNo;
	/** 구성분류 */
	private String confClass;
	/**
	 * 자바필드명<br>
	 * null인 경우 구성으로 사용하지 않음
	 */
	private String javaFieldName;
	/** SNMP OID */
	private String snmpOid;
	/** SNMP 타입 */
	private String snmpType;
	/**
	 * 성능번호<br>
	 * 0이하이면 성능으로 사용하지 않음
	 */
	private int perfNo;
	/** 계산식 */
	private String computeStr;

	private List<DynamicOidEnum> enumList;

	public ModelConfA() {

	}

	public void add(DynamicOidEnum oidEnum) {
		if (enumList == null) {
			enumList = new ArrayList<DynamicOidEnum>();
		}

		enumList.add(oidEnum);
	}

	public String getComputeStr() {
		return computeStr;
	}

	public String getConfClass() {
		return confClass;
	}

	public List<DynamicOidEnum> getEnumList() {
		return enumList;
	}

	public String getJavaFieldName() {
		return javaFieldName;
	}

	public int getModelNo() {
		return modelNo;
	}

	public int getPerfNo() {
		return perfNo;
	}

	public String getSnmpOid() {
		return snmpOid;
	}

	public String getSnmpType() {
		return snmpType;
	}

	/**
	 * SNMP로 얻은 값을 문자열 값으로 변환합니다.<br>
	 * null이거나 일치한 내용이 없다면 입력된 값 그대로 보냅니다.
	 * 
	 * @param valueSnmp
	 * @return 문자열값
	 */
	public String getValueStr(String valueSnmp) {
		if (valueSnmp == null) return valueSnmp;

		if (enumList == null) return valueSnmp;

		for (DynamicOidEnum e : enumList) {
			if (e.getValueOid() != null && e.getValueOid().equals(valueSnmp)) return e.getValueUse();
		}

		return valueSnmp;
	}

	/**
	 * SNMP OID에 입력된 값이 진짜 SNMP OID인지 점검합니다.
	 * 
	 * @return
	 * @since 2013.07.24 by subkjh
	 */
	public boolean isSnmpOid() {

		if (snmpOid == null || snmpOid.trim().length() < 6) return false;

		if (snmpOid.charAt(0) != '.') {
			return false;
		}

		String ss[] = snmpOid.substring(1).split("\\.");
		for (String e : ss) {
			try {
				Long.parseLong(e);
			} catch (Exception ex) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param _oid
	 *            snmpget를 통해 얻는 OID
	 * @return
	 */
	public boolean match(String _oid) {
		return snmpOid.equals(_oid) || _oid.startsWith(snmpOid + ".");

	}

	public void setComputeStr(String computeStr) {
		this.computeStr = computeStr;
	}

	public void setConfClass(String confClass) {
		this.confClass = confClass;
	}

	public void setEnumList(List<DynamicOidEnum> enumList) {
		this.enumList = enumList;
	}

	public void setJavaFieldName(String javaFieldName) {
		this.javaFieldName = javaFieldName;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public void setPerfNo(int perfNo) {
		this.perfNo = perfNo;
	}

	public void setSnmpOid(String snmpOid) {
		this.snmpOid = snmpOid;
	}

	public void setSnmpType(String snmpType) {
		this.snmpType = snmpType;
	}

}

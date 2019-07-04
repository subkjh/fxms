package fxms.nms.co.snmp.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * SNMP OID 정보
 * 
 * @author subkjh
 * 
 */
public class OidDefVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8916916209779361179L;

	/**
	 * SNMPOID<br>
	 * 스칼라값이여야함.
	 */
	private String snmpOid;

	/** OID설명 */
	private String snmpOidDescr;

	/** OID명 */
	private String snmpOidName;

	/** OID 데이터 유형 */
	private int snmpOidType;

	/** key:value, value:명 */
	private Map<String, String> vnMap;

	public OidDefVo() {

	}

	public String getSnmpOid() {
		return snmpOid;
	}

	public String getSnmpOidDescr() {
		return snmpOidDescr;
	}

	public String getSnmpOidName() {
		return snmpOidName;
	}

	public int getSnmpOidType() {
		return snmpOidType;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public String getValue(String value) {
		if (vnMap == null)
			return value;

		String ret = vnMap.get(value);
		if (ret == null)
			return value;

		return ret + "(" + value + ")";
	}

	public Map<String, String> getVnMap() {
		return vnMap;
	}

	/**
	 * SNMP OID에 입력된 값이 진짜 SNMP OID인지 점검합니다.
	 * 
	 * @return
	 * @since 2013.07.24 by subkjh
	 */
	public boolean isSnmpOid() {

		String snmpOid = getSnmpOid();

		if (snmpOid == null || snmpOid.trim().length() < 6)
			return false;

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

	public void setMap(Map<String, String> map) {
		this.vnMap = map;
	}

	public void setSnmpOid(String snmpOid) {
		this.snmpOid = snmpOid;
	}

	public void setSnmpOidDescr(String snmpOidDescr) {
		this.snmpOidDescr = snmpOidDescr;
	}

	public void setSnmpOidName(String snmpOidName) {
		this.snmpOidName = snmpOidName;
	}

	public void setSnmpOidType(int snmpOidType) {
		this.snmpOidType = snmpOidType;
	}

	/**
	 * 값의 구분은 콤마(,), 세미콜론(;), CR으로 구분되며 값과 명칭의 구분은 콜론(:), 같음(=)으로 구분됩니다.<br>
	 * 예) 1 : up,2 : down,3 : testing,4 : unknown,5 : dormant,6 : notPresent,7 :
	 * lowerLayerDown
	 * 
	 * @param values
	 */
	public void setValues(String values) {
		if (values == null || values.trim().length() == 0)
			return;

		if (vnMap == null)
			vnMap = new HashMap<String, String>();

		String valArray[] = values.split(",|\n|;");
		String nv[];
		for (String val : valArray) {
			nv = val.split(":|=");
			vnMap.put(nv[0].trim(), nv[1].trim());
		}
	}

	public void setVnMap(Map<String, String> vnMap) {
		this.vnMap = vnMap;
	}

	@Override
	public String toString() {
		return snmpOid + "=" + snmpOidName;
	}

}

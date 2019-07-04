package fxms.nms.co.snmp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import subkjh.bas.co.lang.Lang;

import com.adventnet.snmp.snmp2.SnmpAPI;

import fxms.bas.po.counter.ValueCounter64;
import fxms.nms.co.snmp.SnmpUtil;

public class OidValue implements Serializable {

	public static final byte COUNTER64 = 70;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8667204918207166527L;

	public static void main(String[] args) {
		// OidValue o = new OidValue();
		// o.oid = ".1.2.3.4.5.6.7.8.9.0.0.1";
		// System.out.println(o.getInstance(".1.2.3.4.5.6.7.8.9"));

		byte bytes[] = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff };
		byte bytes2[] = new byte[] { 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
				(byte) 0xff };
		// BigInteger bi = new BigInteger(bytes);
		BigDecimal bd = new BigDecimal(Long.MAX_VALUE);
		// BigDecimal bd2 = new BigDecimal(Long.MAX_VALUE);

		bd = bd.add(new BigDecimal(Long.MAX_VALUE));
		bd = bd.add(new BigDecimal(1));

		System.out.println(new BigInteger(bytes));
		System.out.println(new BigInteger(bytes2));
		System.out.println(bd);
		System.out.println(bd.toBigInteger());
		System.out.println(bd.toBigInteger().toString(16));
		System.out.println(ValueCounter64.MAX_COUNTER64);
		System.out.println(ValueCounter64.MAX_COUNTER64.toBigInteger().toString(16));
		// System.out.println(bd.toEngineeringString());
		// System.out.println(bd.add(bd2));
		// System.out.println(bi.add(new BigInteger("1")));
		// System.out.println(Double.MAX_VALUE);

	}

	private String oid;
	private byte valueBytes[];
	private String valueString;
	private byte valueType;

	public OidValue() {

	}

	/**
	 * 
	 * @param oid
	 * @param valueType
	 * @param valueBytes
	 */
	public OidValue(String oid, byte valueType, byte valueBytes[]) {
		this.oid = oid;
		this.valueType = valueType;
		this.valueString = new String(valueBytes, SnmpUtil.CHARSET);
		this.valueBytes = valueBytes;
	}

	public OidValue(String oid, byte valueType, Number number) {
		this.oid = oid;
		this.valueType = valueType;
		this.valueString = String.valueOf(number.longValue());

		if (valueString != null) {
			this.valueBytes = valueString.getBytes();
		} else {
			valueBytes = new byte[0];
		}
	}

	public OidValue(String oid, byte valueType, String valueString) {
		this.oid = oid;
		this.valueType = valueType;
		this.valueString = valueString;
		if (valueString != null) {
			this.valueBytes = valueString.getBytes(SnmpUtil.CHARSET);
		} else {
			valueBytes = new byte[0];
		}
	}

	public OidValue(String oid, byte valueType, String valueString, byte valueBytes[]) {
		this.oid = oid;
		this.valueType = valueType;

		if (valueType == SnmpAPI.COUNTER64) {
			this.valueString = new BigInteger(valueBytes).toString();
		} else {
			this.valueString = valueString;
		}
		this.valueBytes = valueBytes;
	}

	public BigInteger getBigInteger() {
		if (valueBytes == null || valueBytes.length == 0)
			return null;
		return new BigInteger(valueBytes);
	}

	public byte[] getBytes() {
		return valueBytes;
	}

	/**
	 * double값을 제공하고 값이 존재하지 않고 기본 값도 입력되지 않은 경우 예외처리합니다.<br>
	 * 
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public double getDouble(double... defaultValue) throws Exception {
		if (valueString == null) {
			if (defaultValue.length > 0)
				return defaultValue[0];
			throw new Exception(Lang.get("수치형 값이 존재하지 않습니다."));
		}

		try {
			return Double.parseDouble(valueString);
		} catch (Exception e) {
			if (defaultValue.length > 0)
				return defaultValue[0];
			throw new Exception(Lang.get("수치형 값이 아닙니다.") + "(" + valueString + ")");
		}
	}

	public String getHstimeDateAndTime() {
		if (valueBytes == null)
			return "0";

		if (valueBytes.length < 8)
			return "0";

		/*
		 * field octets contents range ----- ------ -------- ----- 1 1-2 year
		 * 0..65536 2 3 month 1..12 3 4 day 1..31 4 5 hour 0..23 5 6 minutes
		 * 0..59 6 7 seconds 0..60 (use 60 for leap-second) 7 8 deci-seconds
		 * 0..9 8 9 direction from UTC '+' / '-' 9 10 hours from UTC 0..11 10 11
		 * minutes from UTC 0..59
		 */

		int year = valueBytes[0];
		year <<= 8;
		year |= (valueBytes[1] & 0xff);

		return String.format("%04d%02d%02d%02d%02d%02d", year, valueBytes[2], valueBytes[3], valueBytes[4],
				valueBytes[5], valueBytes[6]);

	}

	/**
	 * OID에서 마지막에서 몇개를 제공합니다.
	 * 
	 * @param size
	 *            인스턴스 갯수
	 * @return 인스턴스
	 */
	public String getInstance(int size) {
		String ss[] = oid.split("\\.");
		String ret = "";
		for (int i = ss.length - 1; i >= 0; i--) {
			ret = ss[i] + ret;
			if (--size <= 0)
				break;
			ret = "." + ret;
		}
		return ret;
	}

	public String getInstance(String oid) {
		return this.oid.replaceFirst(oid + ".", "");
	}

	public int getInt(int... defaultValue) {
		if (valueString == null)
			return defaultValue.length > 0 ? defaultValue[0] : -1;
		try {
			return Integer.parseInt(valueString);
		} catch (Exception e) {
			return defaultValue.length > 0 ? defaultValue[0] : -1;
		}
	}

	public String getIpAddress() {
		if (valueBytes == null)
			return null;

		if (valueBytes.length != 4) {
			String ret = "";
			for (byte b : valueBytes) {
				ret += String.format("%02x", b);
			}
			return ret;
		}
		return String.format("%d.%d.%d.%d", valueBytes[0] & 0xff, valueBytes[1] & 0xff, valueBytes[2] & 0xff,
				valueBytes[3] & 0xff);
	}

	public long getLong(long... defaultValue) {
		if (valueString == null)
			return defaultValue.length > 0 ? defaultValue[0] : -1;
		try {
			return Long.parseLong(valueString);
		} catch (Exception e) {
			return defaultValue.length > 0 ? defaultValue[0] : -1;
		}
	}

	public String getMacAddress() {
		if (valueBytes == null)
			return null;

		if (valueBytes.length != 6) {
			String ret = "";
			for (byte b : valueBytes) {
				ret += String.format("%02x", b);
			}
			return ret;
		}
		return String.format("%02x%02x.%02x%02x.%02x%02x", valueBytes[0], valueBytes[1], valueBytes[2], valueBytes[3],
				valueBytes[4], valueBytes[5]);
	}

	public String getOid() {
		return oid;
	}

	public byte getType() {
		return valueType;
	}

	public String getValue() {
		return valueString;
	}

	/**
	 * 
	 * @return 값이 null인지 여부
	 */
	public boolean isNull() {
		return valueString == null;
	}

	public void setNumber(Number number) {
		this.valueString = String.valueOf(number.longValue());

		if (valueString != null) {
			this.valueBytes = valueString.getBytes();
		} else {
			valueBytes = new byte[0];
		}
	}

	/**
	 * 
	 * @return 값 바이트 길이
	 */
	public int size() {
		return valueBytes == null ? 0 : valueBytes.length;
	}

	@Override
	public String toString() {
		return oid + "|" + valueType + "|" + (valueBytes != null ? valueBytes.length : 0) + "|" + valueString;
	}
}

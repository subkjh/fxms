package fxms.bas.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;

/**
 * 입력한 문자 확인
 * 
 * @author subkjh
 * 
 */
public class CheckUtil {

	/** 키보드 자판 배열 */
	private static final String PASSWORD_SERIES[] = new String[] { "`1234567890-=", "qwertyuiop[]\\", "asdfghjkl;'",
			"zxcvbnm,./", "~!@#$%^&*()_+", "QWERTYUIOP{}|", "ASDFGHJKL:\"", "ZXCVBNM<>?" };

	private static final char SPECIAL_CHAR[] = "~!@#$%^&*()_+`-=[]\\{}|;':\",./<>?".toCharArray();

	/**
	 * 간단한 암호 적용 여부
	 * 
	 * @since 2013.06.18 by subkjh
	 */
	public static boolean isAllowSimplePassword = false;

	public static void checkEMail(String s, int lenMin, int lenMax, String name) throws Exception {

		if (lenMin == 0 && (s == null || s.trim().length() == 0))
			return;

		checkStringRange(s, lenMin, lenMax, name);
		// if (s == null || s.trim().getBytes().length < lenMin ||
		// s.trim().getBytes().length > lenMax) {
		// throw new Exception(name + " " + "입력 범위에 맞지 않습니다." + " : 범위 " +
		// lenMin + "~" + lenMax + "\n" //
		// + "입력된 값 : [" + s + "]");
		// }

		if (s.indexOf('@') <= 0)
			throw new Exception(
					name + " : " + Lang.get("EMail 주소 형식이 아닙니다.") + " - " + Lang.get("입력된 값") + " : [" + s + "]");

	}

	public static void checkFtype(String ftype, String ftypeBase, String varValue) throws Exception {

		boolean isValid = false;

		if (ftypeBase != null && ftypeBase.trim().length() > 0) {
			try {

				if ("N".equalsIgnoreCase(ftype)) {
					String ss[] = ftypeBase.split("~");
					double value = Double.parseDouble(varValue);
					double valueMin, valueMax;
					if (ss.length == 2) {
						valueMin = Double.parseDouble(ss[0]);
						valueMax = Double.parseDouble(ss[1]);
						isValid = value >= valueMin && value <= valueMax;
					}
				} else if ("E".equalsIgnoreCase(ftype)) {
					String ss[] = ftypeBase.split(";|,");
					for (String s : ss) {
						if (varValue.equals(s)) {
							isValid = true;
							break;
						}
					}
				} else {
					isValid = true;
				}
			} catch (Exception e) {
				isValid = false;
			}
		} else {
			isValid = true;
		}

		if (isValid == false)
			throw new Exception(Lang.get("환경변수 값이 잘못되었습니다.") + "(" + ftypeBase + " : " + varValue + ")");

	}

	public static void checkHhmm(String... hhmmArray) throws Exception {
		for (String hhmm : hhmmArray) {
			if (hhmm.compareTo("00:00") < 0 || hhmm.compareTo("24:00") > 0)
				throw new Exception(Lang.get("시간대를 나타내는 값이 아닙니다.") + " (" + hhmm + ")");
		}
	}

	public static void checkIp(String ipAddress) throws Exception {
		if (isIpAddress(ipAddress) == false)
			throw new Exception(Lang.get("It is not an IP address.") + " : " + ipAddress);
	}

	public static void checkIp(String ipAddress, String name) throws Exception {
		if (isIpAddress(ipAddress) == false)
			throw new Exception(Lang.get("It is not an IP address.") + " " + name + ", " + ipAddress);
	}

	public static void checkIpRange(long ipStart, long ipEnd) throws Exception {
		if (ipStart > ipEnd)
			throw new Exception(Lang.get("시작 IP주소가 끝 IP주소 보다 큽니다."));
	}

	/**
	 * 암호 체크
	 * 
	 * @param s
	 * @throws Exception
	 * @since 2013.04.22
	 */
	public static void checkPassword(String s, int maxLen) throws Exception {

		if (s.length() > maxLen) {
			throw new Exception(Lang.get("Password length too large.") + "(" + maxLen + ")");
		}

		// 2013.06.18 by subkjh
		if (isAllowSimplePassword) {
			if (s == null || s.trim().length() == 0) {
				throw new Exception(Lang.get("Even simple passwords must be at least 4 digits long."));
			}
			return;
		}

		String msg = Lang.get("Passwords must meet the following conditions:") + "\n" //
				+ Lang.get("It must be at least 8 characters long and contain at least 1 English letter, number, and special character.") + "\n" //
				+ Lang.get("In the keyboard layout, more than 4 digits cannot be composed in a row.") + "\n" //
				+ Lang.get("The same character cannot occur more than 3 times in a row.");

		if (s == null || s.trim().length() == 0)
			throw new Exception(msg);

		char chs[] = s.toCharArray();
		int countNumeric = 0;
		int countAlpabet = 0;
		int countSpecial = 0;

		// 영문, 숫자, 특수문자 포함 여부

		for (char ch : chs) {
			if (ch >= '0' && ch <= '9')
				countNumeric++;
			if (ch >= 'a' && ch <= 'z')
				countAlpabet++;
			if (ch >= 'A' && ch <= 'Z')
				countAlpabet++;

			for (char e : SPECIAL_CHAR) {
				if (ch == e) {
					countSpecial++;
					break;
				}
			}
		}

		if (countNumeric <= 0)
			throw new Exception(msg);
		if (countAlpabet <= 0)
			throw new Exception(msg);
		if (countSpecial <= 0)
			throw new Exception(msg);

		// 자판 배열
		String s2;
		for (int i = 0; i < s.length() - 4; i++) {
			s2 = s.substring(i, i + 4);

			for (String e : PASSWORD_SERIES) {
				if (e.contains(s2) == true) {
					throw new Exception(msg);
				}
			}
		}

		// 같은 문자 연속성
		for (int i = 0; i <= chs.length - 3; i++) {
			if (chs[i] == chs[i + 1]) {
				if (chs[i] == chs[i + 2]) {
					throw new Exception(msg);
				}
			}
		}

	}

	/**
	 * SNMP OID인지 확인
	 * 
	 * @param s
	 *            SNMP OID
	 * @param lenMin
	 *            최소 길이
	 * @param lenMax
	 *            최대 길이
	 * @param name
	 * @throws Exception
	 */
	public static void checkSnmpOidRange(String s, int lenMin, int lenMax, String name) throws Exception {

		if (lenMin == 0 && (s == null || s.trim().length() == 0))
			return;

		if (s.charAt(0) != '.') {
			throw new Exception(name + " : " + Lang.get("SNMP OID는 점(.)으로 시작해서 숫자만 입력해야 합니다.") + " - "
					+ Lang.get("입력된 값") + " : [" + s + "]");
		}

		if (s.length() < 3) {
			throw new Exception(name + " : " + Lang.get("SNMP OID는 최소 3자리 이상으로 입력해야 합니다.") + " - " + Lang.get("입력된 값")
					+ " : [" + s + "]");
		}

		String ss[] = s.substring(1).split("\\.");
		for (String e : ss) {
			try {
				Long.parseLong(e);
			} catch (Exception ex) {
				throw new Exception(name + " : " + Lang.get("SNMP OID는 점(.)으로 시작해서 숫자만 입력해야 합니다.") + " - "
						+ Lang.get("입력된 값") + " : [" + s + "]");
			}
		}
		checkStringRange(s, lenMin, lenMax, name);

		// if (s == null || s.trim().getBytes().length < lenMin ||
		// s.trim().getBytes().length > lenMax) {
		// throw new Exception(name + " " + "입력 범위에 맞지 않습니다." + " : 범위 " +
		// lenMin + "~" + lenMax + "\n" //
		// + "입력된 값 : [" + s + "]");
		// }
	}

	public static void checkSocketPort(int port) throws Exception {
		if (port <= 0 || port >= 65535)
			throw new Exception(Lang.get("소켓포트 범위를 벗어납니다.") + " (" + port + ")");
	}

	/**
	 * 입력 문자열 검증
	 * 
	 * @param s
	 *            입력된 문자열
	 * @param lenMin
	 *            최소크기
	 * @param msg
	 *            조건에 맞지 않을때 메시지
	 * @throws Exception
	 * @since 2013.02.06 by subkjh
	 */
	public static void checkStrEmpty(String s, int lenMin, String msg) throws Exception {
		if (s == null || s.trim().getBytes().length < lenMin) {
			throw new Exception(msg + " (" + s + ")");
		}
	}

	public static void checkString(String name, String value, boolean okNull, int lenMax) throws Exception {
		if (okNull == false && value == null)
			throw new Exception(Lang.get("null값을 입력할 수 없습니다.") + " (" + name + ")");
		if (value != null && lenMax < value.getBytes().length) {
			throw new Exception(Lang.get("입력된 글자수가 너무 많습니다.") + " (" + name + ", " + Lang.get("최대문자수(한글 한 자당 2문자로 봄)")
					+ " =" + lenMax + ")");
		}
	}

	/**
	 * 문자 검증
	 * 
	 * @param s
	 *            입력된 문자열
	 * @param lenMin
	 *            최소길이
	 * @param lenMax
	 *            최대길이
	 * @param msg
	 *            조건에 맞지 않을때 메시지
	 * @throws Exception
	 * @since 2013.02.06 by subkjh
	 */
	public static void checkStrRange(String s, int lenMin, int lenMax, String name) throws Exception {

		if (lenMin == 0)
			return;

		checkStringRange(s, lenMin, lenMax, name);

	}

	public static void checkTelNo(String s, int lenMin, int lenMax, String name) throws Exception {

		if (lenMin == 0 && (s == null || s.trim().length() == 0))
			return;

		checkStringRange(s, lenMin, lenMax, name);

		for (char ch : s.toCharArray()) {
			if ((ch >= '0' && ch <= '9') || ch == '-')
				continue;

			throw new Exception(name + " : " + Lang.get("전화번호가 아닙니다.") + "\n" //
					+ Lang.get("입력된 값") + " : [" + s + "]");
		}

	}

	public static void checkUrl(String s, int lenMin, int lenMax, String name) throws Exception {

		if (lenMin == 0 && (s == null || s.trim().length() == 0))
			return;

		checkStringRange(s, lenMin, lenMax, name);

		try {
			URL url = new URL(s);
			if (("https".equalsIgnoreCase(url.getProtocol()) || "http".equalsIgnoreCase(url.getProtocol()))
					&& url.getHost() != null && url.getHost().trim().length() > 0)
				return;
		} catch (Exception e) {

		}

		throw new Exception(name + " : " + Lang.get("URL이 아닙니다.") + "\n" //
				+ Lang.get("입력된 값") + " : [" + s + "]");

	}

	public static long getIpNum(String ipAddress) {
		long ipNum = 0;
		try {
			String ss[] = ipAddress.split("\\.");
			ipNum = 0;
			for (int i = 0; i < ss.length; i++) {
				ipNum <<= 8;
				ipNum |= Integer.parseInt(ss[i]);
			}
		} catch (Exception e) {
			return -1;
		}
		return ipNum;
	}

	/**
	 * 
	 * @param s
	 * @return IP주소인 지여부
	 */
	public static boolean isIpAddress(String s) {

		if (s == null)
			return false;
		if (s.trim().length() == 0)
			return false;

		String ss[] = s.split("\\.");
		if (ss.length != 4)
			return false;

		for (int i = 0; i < ss.length; i++) {
			try {
				if (Integer.parseInt(ss[i]) < 0 || Integer.parseInt(ss[i]) > 255)
					return false;
			} catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) {

		try {
			CheckUtil.checkPassword("a4aa2242###", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void checkStringRange(String s, int lenMin, int lenMax, String name) throws Exception {

		if (s == null || s.trim().getBytes().length < lenMin || s.trim().getBytes().length > lenMax) {
			throw new Exception(name + " : " + Lang.get("입력된 글자수가 범위에 맞지 않습니다.")//
					+ " : " + Lang.get("최소") + " " + lenMin + ", " + Lang.get("최대") + " " + lenMax + " ( "
					+ Lang.get("한글 한 자당 2문자로 처리됨") + " )\n" //
					+ Lang.get("입력된 문자") + " : [" + s + "]");
		}

	}

	/**
	 * 문자 크기를 최대문자열수 이내로 조절합니다.
	 * 
	 * @param s
	 *            입력할 문자열
	 * @param lenMax
	 *            최대문자열 수
	 * @return 최대문자열에 맞는 문자열
	 */
	public static String makeString(String s, int lenMax) {
		if (s == null)
			return s;

		byte bytes[];
		try {
			bytes = s.getBytes("euc-kr");

			if (bytes.length > lenMax) {
				bytes = s.getBytes("euc-kr");
				for (int index = bytes.length - 1; index >= 0; index--) {
					if (bytes[index] == ' ' || bytes[index] == '\n') {
						if (index < lenMax) {
							String ret = new String(bytes, 0, index, "euc-kr");
							Logger.logger.debug("[" + s + "] -> [" + ret + "]");
							return ret;
						}
					}
				}
				return new String(bytes, 0, lenMax, "euc-kr");
			} else {
				return s;
			}
		} catch (UnsupportedEncodingException e) {
			return s.substring(0, lenMax);
		}

	}

}

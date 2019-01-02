package com.fxms.ui.bas.vo;

import java.util.HashMap;
import java.util.Map;

public class SnmpPass extends Pass {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3453603949481986302L;


	/** SNMP Read 커뮤니티 */
	private String snmpRead;

	/** SNMP Write 커뮤니티 */
	private String snmpWrite;

	/** SNMP 포트 */
	private int snmpPort = 161;

	/** SNMP Version */
	private int snmpVer = 1;

	/** 인증암호 */
	private String snmpAuthPwd;

	/** 인증알고리즘 */
	private int snmpAuthProtocol;

	private String snmpContextId;

	private String snmpContextName;

	/** 개인암호 */
	private String snmpPrivPwd;

	/** 개인암호프로토콜 */
	private int snmpPrivProtocol;

	/** 사용자명 */
	private String snmpUserName;

	public SnmpPass() {
	}

	/**
	 * 
	 * @param snmpString
	 *            SNMP 문자열
	 * @throws Exception
	 */
	public SnmpPass(String snmpString) throws Exception {
		setSnmpString(snmpString);
	}


	public Map<String, Object> getMap() {

		Map<String, Object> map = new HashMap<String, Object>();

		if (snmpRead != null)
			map.put("snmpRead", snmpRead);
		if (snmpWrite != null)
			map.put("snmpWrite", snmpWrite);
		if (snmpAuthPwd != null)
			map.put("snmpAuthPwd", snmpAuthPwd);
		if (snmpUserName != null)
			map.put("snmpUserName", snmpUserName);
		if (snmpPrivPwd != null)
			map.put("snmpPrivPwd", snmpPrivPwd);
		if (snmpContextName != null)
			map.put("snmpContextName", snmpContextName);
		if (snmpContextId != null)
			map.put("snmpContextId", snmpContextId);

		map.put("snmpPort", snmpPort);
		map.put("snmpAuthProtocol", snmpAuthProtocol);
		map.put("snmpPrivProtocol", snmpPrivProtocol);
		map.put("snmpVer", snmpVer);

		return map;
	}

	/**
	 * 
	 * @return 인증프로토콜
	 */
	public int getSnmpAuthProtocol() {
		return snmpAuthProtocol;
	}

	/**
	 * 
	 * @return 인증암호
	 */
	public String getSnmpAuthPwd() {
		return snmpAuthPwd;
	}

	/**
	 * 
	 * @return 암호화된 인증 암호
	 */
	public String getSnmpAuthPwdEnc() {
		try {
			return getEncrypt3(snmpAuthPwd);
		} catch (Exception e) {
			return snmpAuthPwd;
		}
	}

	public String getSnmpContextId() {
		return snmpContextId;
	}

	public String getSnmpContextName() {
		return snmpContextName;
	}

	public int getSnmpPort() {
		return snmpPort;
	}

	public int getSnmpPrivProtocol() {
		return snmpPrivProtocol;
	}

	public String getSnmpPrivPwd() {
		return snmpPrivPwd;
	}

	/**
	 * 
	 * @return 암호회된 개인 암호
	 */
	public String getSnmpPrivPwdEnc() {
		try {
			return getEncrypt3(snmpPrivPwd);
		} catch (Exception e) {
			return snmpPrivPwd;
		}
	}

	public String getSnmpRead() {
		return snmpRead;
	}

	/**
	 * 
	 * @return 암호화된 읽기 커뮤니티
	 */
	public String getSnmpReadEnc() {
		try {
			return getEncrypt3(snmpRead);
		} catch (Exception e) {
			return snmpRead;
		}
	}

	public String getSnmpString() {
		String s;

		StringBuffer sb = new StringBuffer();
		sb.append("sv=" + snmpVer + ",");
		sb.append("sp=" + snmpPort + ",");

		s = getString(getSnmpReadEnc());
		if (s.length() > 0)
			sb.append("sre=" + s + ",");

		s = getString(getSnmpWriteEnc());
		if (s.length() > 0)
			sb.append("swe=" + s + ",");

		s = getString(getSnmpAuthPwdEnc());
		if (s.length() > 0)
			sb.append("sape=" + s + ",");

		sb.append("sap=" + snmpAuthProtocol + ",");

		s = getString(getSnmpPrivPwdEnc());
		if (s.length() > 0)
			sb.append("sppe=" + s + ",");

		sb.append("spp=" + snmpPrivProtocol + ",");

		s = getString(snmpUserName);
		if (s.length() > 0)
			sb.append("sun=" + s + ",");

		s = getString(snmpContextId);
		if (s.length() > 0)
			sb.append("sci=" + s + ",");

		s = getString(snmpContextName);
		if (s.length() > 0)
			sb.append("scn=" + s);

		return sb.toString();
	}

	public String getSnmpUserName() {
		return snmpUserName;
	}

	public int getSnmpVer() {
		return snmpVer;
	}

	public String getSnmpWrite() {
		return snmpWrite;
	}

	/**
	 * 
	 * @return 암호화된 쓰기 커뮤니티
	 */
	public String getSnmpWriteEnc() {
		try {
			return getEncrypt3(snmpWrite);
		} catch (Exception e) {
			return snmpWrite;
		}
	}


	/**
	 * 
	 * @param snmpAuthProtocol
	 *            SNMP 인증 프로토콜
	 */
	public void setSnmpAuthProtocol(int snmpAuthProtocol) {
		this.snmpAuthProtocol = snmpAuthProtocol;
	}

	/**
	 * 
	 * @param snmpAuthPwd
	 *            SNMP 인증 암호
	 */
	public void setSnmpAuthPwd(String snmpAuthPwd) {
		this.snmpAuthPwd = snmpAuthPwd;
	}

	/**
	 * 
	 * @param snmpAuthPwdEnc
	 *            암호화된 인증 암호
	 */
	public void setSnmpAuthPwdEnc(String snmpAuthPwdEnc) {

		if (snmpAuthPwdEnc == null || snmpAuthPwdEnc.trim().length() == 0)
			return;

		try {
			snmpAuthPwd = getDecrypt3(snmpAuthPwdEnc);
		} catch (Exception e) {
			this.snmpAuthPwd = snmpAuthPwdEnc;
		}
	}

	/**
	 * 
	 * @param snmpContextId
	 *            컨텍스트 ID
	 */
	public void setSnmpContextId(String snmpContextId) {
		this.snmpContextId = snmpContextId;
	}

	/**
	 * 
	 * @param snmpContextName
	 *            컨텍스트 명
	 */
	public void setSnmpContextName(String snmpContextName) {
		this.snmpContextName = snmpContextName;
	}

	/**
	 * 
	 * @param snmpPort
	 *            SNMP 포트
	 */
	public void setSnmpPort(int snmpPort) {
		this.snmpPort = snmpPort;
	}

	/**
	 * 
	 * @param snmpPrivProtocol
	 *            SNMP 사설 프로토콜
	 */
	public void setSnmpPrivProtocol(int snmpPrivProtocol) {
		this.snmpPrivProtocol = snmpPrivProtocol;
	}

	/**
	 * 
	 * @param snmpPrivPwd
	 *            SNMP 사설 암호
	 */
	public void setSnmpPrivPwd(String snmpPrivPwd) {
		this.snmpPrivPwd = snmpPrivPwd;
	}

	/**
	 * 
	 * @param snmpPrivPwdEnc
	 *            암호화된 개인 암호
	 */
	public void setSnmpPrivPwdEnc(String snmpPrivPwdEnc) {

		if (snmpPrivPwdEnc == null || snmpPrivPwdEnc.trim().length() == 0)
			return;

		try {
			snmpPrivPwd = getDecrypt3(snmpPrivPwdEnc);
		} catch (Exception e) {
			this.snmpPrivPwd = snmpPrivPwdEnc;
		}
	}

	/**
	 * 
	 * @param snmpRead
	 *            SNMP 읽기 커뮤니티
	 */
	public void setSnmpRead(String snmpRead) {
		this.snmpRead = snmpRead;
	}

	/**
	 * 
	 * @param snmpReadEnc
	 *            암호화된 읽기 커뮤니티
	 */
	public void setSnmpReadEnc(String snmpReadEnc) {

		if (snmpReadEnc == null || snmpReadEnc.trim().length() == 0)
			return;

		try {
			snmpRead = getDecrypt3(snmpReadEnc);
		} catch (Exception e) {
			this.snmpRead = snmpReadEnc;
		}
	}

	/**
	 * SNMP 정보를 문자열로 설정합니다.<br>
	 * snmp<br>
	 * sv : snmpVer<br>
	 * sp : snmpPort<br>
	 * sre : snmpReadEnc<br>
	 * swe : snmpWriteEnc<br>
	 * sape : snmpAuthPwdEnc<br>
	 * sap : snmpAuthProtocol<br>
	 * sppe : snmpPrivPwdEnc<br>
	 * spp : snmpPrivProtocol<br>
	 * sun : snmpUserName<br>
	 * sci : snmpContextId<br>
	 * scn : snmpContextName<br>
	 * 
	 * @param snmpString
	 *            SNMP정보 문자열
	 * @throws Exception
	 */
	public void setSnmpString(String snmpString) throws Exception {
		if (snmpString == null) {
			return;
		}

		String ss[] = snmpString.split(",|;");
		String nv[];
		String name, value;
		for (String s : ss) {
			if (s != null) {
				nv = s.split("=");
				if (nv.length == 2) {
					name = nv[0];
					value = nv[1];

					if ("sv".equalsIgnoreCase(name))
						snmpVer = Integer.parseInt(value);
					else if ("sp".equalsIgnoreCase(name))
						snmpPort = Integer.parseInt(value);
					else if ("sre".equalsIgnoreCase(name))
						setSnmpReadEnc(value);
					else if ("swe".equalsIgnoreCase(name))
						setSnmpWriteEnc(value);

					else if ("sape".equalsIgnoreCase(name))
						setSnmpAuthPwdEnc(value);
					else if ("sap".equalsIgnoreCase(name))
						snmpAuthProtocol = Integer.parseInt(value);
					else if ("sppe".equalsIgnoreCase(name))
						setSnmpPrivPwdEnc(value);
					else if ("spp".equalsIgnoreCase(name))
						snmpPrivProtocol = Integer.parseInt(value);
					else if ("sun".equalsIgnoreCase(name))
						snmpUserName = value;
					else if ("sci".equalsIgnoreCase(name))
						snmpContextId = value;
					else if ("scn".equalsIgnoreCase(name))
						snmpContextName = value;

				}
			}
		}

	}

	/**
	 * 
	 * @param snmpUserName
	 *            SNMP 유저
	 */
	public void setSnmpUserName(String snmpUserName) {
		this.snmpUserName = snmpUserName;
	}

	/**
	 * 
	 * @param snmpVer
	 *            SNMP VERSION
	 */
	public void setSnmpVer(int snmpVer) {
		this.snmpVer = snmpVer;
	}

	/**
	 * 
	 * @param snmpWrite
	 *            SNMP WRITE 커뮤니티
	 */
	public void setSnmpWrite(String snmpWrite) {
		this.snmpWrite = snmpWrite;
	}

	/**
	 * 
	 * @param snmpWriteEnc
	 *            암호화된 쓰기 커뮤니티
	 */
	public void setSnmpWriteEnc(String snmpWriteEnc) {

		if (snmpWriteEnc == null || snmpWriteEnc.trim().length() == 0)
			return;

		try {
			snmpWrite = getDecrypt3(snmpWriteEnc);
		} catch (Exception e) {
			this.snmpWrite = snmpWriteEnc;
		}
	}


	private String getString(String s) {
		if (s == null)
			return "";
		return s;
	}
}

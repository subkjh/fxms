package fxms.nms.mo.property;

import java.util.HashMap;
import java.util.Map;

import subkjh.bas.co.lang.Lang;
import fxms.bas.co.def.ARGS;
import fxms.bas.mo.node.Pass;

/**
 * TELNET, SSH 정보
 * 
 * @author subkjh
 * 
 */
public class TelnetPass extends Pass {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2937051617433516143L;

	public static final String PROTOCOL_TELNET = "telnet";
	public static final String PROTOCOL_SSH = "ssh";
	public static final String PROTOCOL_RLOGIN = "rlogin";
	public static final String PROTOCOL_NONE = "none";

	public static TelnetPass defTelnet = new TelnetPass(22, PROTOCOL_SSH, "nprism", "nprism001))$");

	public static void main(String[] args) throws Exception {
		TelnetPass p = new TelnetPass();
		System.out.println(p.getMap());
		System.out.println(p.getTelnetString());
	}

	private String telnetProtocol;

	/** Telnet User Password */
	private String telnetPwd;

	/** Telnet User EnPassword */
	private String telnetPwdEn;

	/** Telnet Port */
	private int telnetPort;

	private String telnetPrompt;

	private String telnetPromptEn;

	/** Telnet User Id */
	private String telnetUser;

	/** 계정입력 문자열 */
	private String strLogin;

	/** 암호입력 문자열 */
	private String strPass;

	public TelnetPass() {
	}

	@ARGS(para = { "PORT", "PROTOCOL(telnet|ssh)", "USER-ID", "USER-PASS" })
	public TelnetPass(int port, String protocol, String user, String pass) {
		telnetPort = port;
		telnetProtocol = protocol;
		telnetUser = user;
		telnetPwd = pass;
		telnetPrompt = "$";
	}

	public TelnetPass(String telnetString) throws Exception {
		setTelnetString(telnetString);
	}

	public void checkValid(String ipAddress) throws Exception {

		if (ipAddress == null || ipAddress.trim().length() == 0) throw new Exception(Lang.get("IP주소가 설정되지 않았습니다."));

		if (telnetPort <= 0) throw new Exception(Lang.get("포트 정보가 없습니다."));

		if ((telnetUser == null || telnetUser.trim().length() <= 0) //
				&& (telnetPwd == null || telnetPwd.trim().length() <= 0))
			throw new Exception(Lang.get("로그인 정보가 없습니다."));
	}

	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		if (telnetProtocol != null) map.put("telnetProtocol", telnetProtocol);
		if (telnetProtocol != null) map.put("protocolCli", telnetProtocol);

		if (telnetPwd != null) map.put("telnetPwd", telnetPwd);
		if (telnetPwd != null) map.put("telnetPass", telnetPwd);

		if (telnetPwdEn != null) map.put("telnetPwdEn", telnetPwdEn);

		if (telnetPrompt != null) map.put("telnetPrompt", telnetPrompt);
		if (telnetPromptEn != null) map.put("telnetPromptEn", telnetPromptEn);
		if (telnetUser != null) map.put("telnetUser", telnetUser);

		if (strLogin != null) map.put("strLogin", strLogin);
		if (strPass != null) map.put("strPass", strPass);

		map.put("telnetPort", telnetPort);

		return map;
	}

	public String getStrLogin() {
		return strLogin;
	}

	public String getStrPass() {
		return strPass;
	}

	public int getTelnetPort() {
		return telnetPort;
	}

	public String getTelnetPrompt() {
		return telnetPrompt;
	}

	public String getTelnetPromptEn() {
		return telnetPromptEn;
	}

	public String getTelnetProtocol() {
		return telnetProtocol;
	}

	public String getTelnetPwd() {
		return telnetPwd;
	}

	public String getTelnetPwdEn() {
		return telnetPwdEn;
	}

	/**
	 * 
	 * @return 암호화된 암호
	 */
	public String getTelnetPwdEnc() {
		try {
			return getEncrypt3(telnetPwd);
		}
		catch (Exception e) {
			return telnetPwd;
		}
	}

	/**
	 * 
	 * @return 암호화된 EN 암호
	 */
	public String getTelnetPwdEnEnc() {
		try {
			return getEncrypt3(telnetPwdEn);
		}
		catch (Exception e) {
			return telnetPwdEn;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getTelnetString() {
		String s;
		StringBuffer sb = new StringBuffer();
		sb.append("por=" + telnetPort + ",");

		if (telnetProtocol != null) sb.append("pro=" + telnetProtocol + ",");
		else sb.append("pro=none,");

		s = getString(telnetUser);
		if (s.length() > 0) sb.append("usr=" + s + ",");

		s = getString(getTelnetPwdEnc());
		if (s.length() > 0) sb.append("tpe=" + s + ",");

		s = getString(getTelnetPwdEnEnc());
		if (s.length() > 0) sb.append("tpee=" + s + ",");

		if (telnetPrompt != null) sb.append("p=" + telnetPrompt + ",");
		if (telnetPromptEn != null) sb.append("pe=" + telnetPromptEn + "");

		if (strLogin != null) sb.append("login=" + strLogin + "");
		if (strPass != null) sb.append("pass=" + strPass + "");

		return sb.toString();
	}

	public String getTelnetUser() {
		return telnetUser;
	}

	/**
	 * 
	 * @return 접속 가능 여부
	 */
	public boolean isValid() {
		return telnetProtocol != null && (PROTOCOL_NONE.equals(telnetProtocol) == false) && telnetPort > 0;
	}

	public void setStrLogin(String strLogin) {
		this.strLogin = strLogin;
	}

	public void setStrPass(String strPass) {
		this.strPass = strPass;
	}

	public void setTelnetPort(int telnetPort) {
		this.telnetPort = telnetPort;
	}

	public void setTelnetPrompt(String telnetPrompt) {
		this.telnetPrompt = telnetPrompt;
	}

	public void setTelnetPromptEn(String telnetPromptEn) {
		this.telnetPromptEn = telnetPromptEn;
	}

	public void setTelnetProtocol(String telnetProtocol) {
		this.telnetProtocol = telnetProtocol;
	}

	public void setTelnetPwd(String telnetPwd) {
		this.telnetPwd = telnetPwd;
	}

	public void setTelnetPwdEn(String telnetPwdEn) {
		this.telnetPwdEn = telnetPwdEn;
	}

	/**
	 * 
	 * @param telnetPwdEnc
	 *            암호화된 암호
	 */
	public void setTelnetPwdEnc(String telnetPwdEnc) {

		if (telnetPwdEnc == null || telnetPwdEnc.trim().length() == 0) return;

		try {
			this.telnetPwd = getDecrypt3(telnetPwdEnc);
		}
		catch (Exception e) {
			this.telnetPwd = telnetPwdEnc;
		}
	}

	/**
	 * 
	 * @param telnetPwdEnEnc
	 *            암호화된 EN암호
	 */
	public void setTelnetPwdEnEnc(String telnetPwdEnEnc) {

		if (telnetPwdEnEnc == null || telnetPwdEnEnc.trim().length() == 0) return;

		try {
			this.telnetPwdEn = getDecrypt3(telnetPwdEnEnc);
		}
		catch (Exception e) {
			this.telnetPwdEn = telnetPwdEnEnc;
		}
	}

	public void setTelnetString(String telnetString) throws Exception {

		if (telnetString == null) {
			return;
		}

		String ss[] = telnetString.split(",|;");
		String nv[];
		String name, value;
		for (String s : ss) {
			if (s != null) {
				nv = s.split("=");
				if (nv.length == 2) {
					name = nv[0];
					value = nv[1];

					if ("por".equalsIgnoreCase(name)) telnetPort = Integer.parseInt(value);
					else if ("pro".equalsIgnoreCase(name)) telnetProtocol = value;
					else if ("tpe".equalsIgnoreCase(name)) setTelnetPwdEnc(value);
					else if ("tpee".equalsIgnoreCase(name)) setTelnetPwdEnEnc(value);
					else if ("p".equalsIgnoreCase(name)) telnetPrompt = value;
					else if ("pe".equalsIgnoreCase(name)) telnetPromptEn = value;
					else if ("usr".equalsIgnoreCase(name)) telnetUser = value;
					else if ("login".equalsIgnoreCase(name)) strLogin = value;
					else if ("pass".equalsIgnoreCase(name)) strPass = value;

				}
			}
		}

	}

	public void setTelnetUser(String telnetUser) {
		this.telnetUser = telnetUser;
	}

	@Override
	public String toString() {
		return telnetPort + "|" + telnetProtocol + "|" + telnetPort;
	}

	private String getString(String s) {
		if (s == null) return "";
		return s;
	}

}

package com.fxms.nms.mo;

import fxms.bas.define.ARGS;
import fxms.bas.mo.Mo;

/**
 * URL을 나타내는 MO
 * 
 * @author subkjh
 * 
 */
public class UrlMo extends Mo {

	/** MO분류. SERVICE */
	public static final String MO_CLASS = "URL";

	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";

	/** 1. 정상 */
	public static final byte STATUS_OK = 1;
	/** 2. 접속은 되나 오류페이지 */
	public static final byte STATUS_ERROR = 2;
	/** 3. 접속 안됨 */
	public static final byte STATUS_DOWN = 0;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6731575156953090055L;

	/** 변경일시 */
	private long hstimeChg;

	/** IP 주소 */
	private String ipAddress;

	/** URL 상태 */
	private int statusUrl = STATUS_UNKNOWN;

	/** 접속 URL */
	private String url;

	/** 운용자그룹번호(RO그룹) */
	private int userGroupNo;

	/** 운용자번호(소유자) */
	private int userNo;

	/** 운용자번호(수정) */
	private int userNoChg;

	/** 트래픽 수집 주기(초) */
	private int secPolling;

	/** 성공 문자열 */
	private String strOk;

	/** 오류 문자열 */
	private String strError;

	/** 수집담당서버 */
	private String mgrServer;

	private String method = METHOD_POST;

	public UrlMo() {

	}

	@ARGS(para = { "URL" })
	public UrlMo(String url) {
		this.url = url;
	}

	public long getHstimeChg() {
		return hstimeChg;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getMethod() {
		return method;
	}

	public String getMgrServer() {
		return mgrServer;
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

	public int getSecPolling() {
		return secPolling;
	}

	public int getStatusUrl() {
		return statusUrl;
	}

	public String getStrError() {
		return strError;
	}

	public String getStrOk() {
		return strOk;
	}

	public String getUrl() {
		return url;
	}

	public int getUserGroupNo() {
		return userGroupNo;
	}

	public int getUserNo() {
		return userNo;
	}

	public int getUserNoChg() {
		return userNoChg;
	}

	public void setHstimeChg(long hstimeChg) {
		this.hstimeChg = hstimeChg;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setMgrServer(String mgrServer) {
		this.mgrServer = mgrServer;
	}

	public void setSecPolling(int secPolling) {
		this.secPolling = secPolling;
	}

	public void setStatusUrl(int statusUrl) {
		this.statusUrl = statusUrl;
	}

	public void setStrError(String strError) {
		this.strError = strError;
	}

	public void setStrOk(String strOk) {
		this.strOk = strOk;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUserGroupNo(int userGroupNo) {
		this.userGroupNo = userGroupNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public void setUserNoChg(int userNoChg) {
		this.userNoChg = userNoChg;
	}

}

package fxms.nms.co.snmp.trap.vo;

import java.io.Serializable;

/**
 * 받은 트랩 로드 ( variable binds )
 * 
 * @author subkjh
 * 
 */
public class TrapLogDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6435910849487951287L;

	/** 트랩 수신 서버 주소 * */
	private String ipAddrRecv;
	/** 수신 일련 번호 */
	private long seqnoRecv;
	/** 인덱스 */
	private String varIndex;
	/** OID */
	private String varOid;
	private String varOidName;
	/** 값 종류 */
	private int varType;
	/** 값 */
	private String varValue;
	private String varValueName;

	public TrapLogDetail() {

	}

	public TrapLogDetail(String ipAddrRecv, long seqnoRecv, String varIndex, String varOid, String varOidName, String varValue,
			String varValueName, int varType) {
		this.ipAddrRecv = ipAddrRecv;
		this.seqnoRecv = seqnoRecv;
		this.varIndex = varIndex;
		this.varOid = varOid;
		this.varOidName = varOidName;
		this.varValue = varValue;
		this.varValueName = varValueName;
		this.varType = varType;
	}

	public String getIpAddrRecv() {
		return ipAddrRecv;
	}

	public long getSeqnoRecv() {
		return seqnoRecv;
	}

	public String getVarIndex() {
		return varIndex;
	}

	public String getVarOid() {
		return varOid;
	}

	public String getVarOidName() {
		return varOidName;
	}

	public int getVarType() {
		return varType;
	}

	public String getVarValue() {
		return varValue;
	}

	public String getVarValueName() {
		return varValueName;
	}

	public void setIpAddrRecv(String ipAddrRecv) {
		this.ipAddrRecv = ipAddrRecv;
	}

	public void setSeqnoRecv(long seqnoRecv) {
		this.seqnoRecv = seqnoRecv;
	}

	public void setVarIndex(String varIndex) {
		this.varIndex = varIndex;
	}

	public void setVarOid(String varOid) {
		this.varOid = varOid;
	}

	public void setVarOidName(String varOidName) {
		this.varOidName = varOidName;
	}

	public void setVarType(int varType) {
		this.varType = varType;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

	public void setVarValueName(String varValueName) {
		this.varValueName = varValueName;
	}
}

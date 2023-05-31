package fxms.bas.mo;

/**
 * 노드 MO
 * 
 * @author subkjh
 *
 */
public class NodeMo extends FxMo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8896028668174474848L;

	public static final String MO_CLASS = "NODE";

	/** 노드IP주소 */
	private String nodeIpAddr;

	/** 통신방식 */
	private String commProtc;

	/** 통신포트번호 */
	private Integer commPortNo = 0;

	/** 설치위치메모 */
	private String inloMemo;

	/** 연락처명 */
	private String cntcNm;

	/** 연락처전화번호 */
	private String cntcTelNo;

	/** 설치일자 */
	private Integer instYmd;

	/** FX서버주소 */
	private String fxsvrIpAddr;

	public NodeMo() {
		setMoClass(MO_CLASS);
	}

	/**
	 * 노드IP주소
	 * 
	 * @return 노드IP주소
	 */
	public String getNodeIpAddr() {
		return nodeIpAddr;
	}

	/**
	 * 노드IP주소
	 * 
	 * @param nodeIpAddr 노드IP주소
	 */
	public void setNodeIpAddr(String nodeIpAddr) {
		this.nodeIpAddr = nodeIpAddr;
	}

	/**
	 * 통신방식
	 * 
	 * @return 통신방식
	 */
	public String getCommProtc() {
		return commProtc;
	}

	/**
	 * 통신방식
	 * 
	 * @param commProtc 통신방식
	 */
	public void setCommProtc(String commProtc) {
		this.commProtc = commProtc;
	}

	/**
	 * 통신포트번호
	 * 
	 * @return 통신포트번호
	 */
	public Integer getCommPortNo() {
		return commPortNo;
	}

	/**
	 * 통신포트번호
	 * 
	 * @param commPortNo 통신포트번호
	 */
	public void setCommPortNo(Integer commPortNo) {
		this.commPortNo = commPortNo;
	}

	/**
	 * 설치위치메모
	 * 
	 * @return 설치위치메모
	 */
	public String getInloMemo() {
		return inloMemo;
	}

	/**
	 * 설치위치메모
	 * 
	 * @param inloMemo 설치위치메모
	 */
	public void setInloMemo(String inloMemo) {
		this.inloMemo = inloMemo;
	}

	/**
	 * 연락처명
	 * 
	 * @return 연락처명
	 */
	public String getCntcNm() {
		return cntcNm;
	}

	/**
	 * 연락처명
	 * 
	 * @param cntcNm 연락처명
	 */
	public void setCntcNm(String cntcNm) {
		this.cntcNm = cntcNm;
	}

	/**
	 * 연락처전화번호
	 * 
	 * @return 연락처전화번호
	 */
	public String getCntcTelNo() {
		return cntcTelNo;
	}

	/**
	 * 연락처전화번호
	 * 
	 * @param cntcTelNo 연락처전화번호
	 */
	public void setCntcTelNo(String cntcTelNo) {
		this.cntcTelNo = cntcTelNo;
	}

	/**
	 * 설치일자
	 * 
	 * @return 설치일자
	 */
	public Integer getInstYmd() {
		return instYmd;
	}

	/**
	 * 설치일자
	 * 
	 * @param instYmd 설치일자
	 */
	public void setInstYmd(Integer instYmd) {
		this.instYmd = instYmd;
	}

	/**
	 * FX서버주소
	 * 
	 * @return FX서버주소
	 */
	public String getFxsvrIpAddr() {
		return fxsvrIpAddr;
	}

	/**
	 * FX서버주소
	 * 
	 * @param fxsvrIpAddr FX서버주소
	 */
	public void setFxsvrIpAddr(String fxsvrIpAddr) {
		this.fxsvrIpAddr = fxsvrIpAddr;
	}

}

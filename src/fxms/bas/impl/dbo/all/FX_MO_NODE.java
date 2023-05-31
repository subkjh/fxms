package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.04.13 13:50
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_MO_NODE", comment = "MO노드테이블")
@FxIndex(name = "FX_MO_NODE__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FX_MO_NODE__FK1", type = INDEX_TYPE.FK, columns = { "MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FX_MO_NODE extends FX_MO implements Serializable {

	public FX_MO_NODE() {
	}

	@FxColumn(name = "NODE_IP_ADDR", size = 39, comment = "노드IP주소")
	private String nodeIpAddr;

	@FxColumn(name = "COMM_PROTC", size = 50, nullable = true, comment = "통신방식")
	private String commProtc;

	@FxColumn(name = "COMM_PORT_NO", size = 5, nullable = true, comment = "통신포트번호", defValue = "0")
	private Integer commPortNo = 0;

	@FxColumn(name = "INLO_MEMO", size = 200, nullable = true, comment = "설치위치메모")
	private String inloMemo;

	@FxColumn(name = "CNTC_NM", size = 100, nullable = true, comment = "연락처명")
	private String cntcNm;

	@FxColumn(name = "CNTC_TEL_NO", size = 100, nullable = true, comment = "연락처전화번호")
	private String cntcTelNo;

	@FxColumn(name = "INST_YMD", size = 8, nullable = true, comment = "설치일자")
	private Integer instYmd;

	@FxColumn(name = "FXSVR_IP_ADDR", size = 39, nullable = true, comment = "FX서버주소")
	private String fxsvrIpAddr;

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

package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.05.20 09:51
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_USER_WORK_HST", comment = "사용자작업이력테이블")
@FxIndex(name = "FX_UR_LOG__PK", type = INDEX_TYPE.PK, columns = { "OP_SEQ_NO" })
@FxIndex(name = "FX_UR_LOG__KEY_SES", type = INDEX_TYPE.KEY, columns = { "SESSION_ID" })
@FxIndex(name = "FX_UR_LOG__KEY_OP", type = INDEX_TYPE.KEY, columns = { "OP_ID" })
public class FX_UR_USER_WORK_HST implements Serializable {

	public FX_UR_USER_WORK_HST() {
	}

	public static final String FX_SEQ_OPSEQNO = "FX_SEQ_OPSEQNO";
	@FxColumn(name = "OP_SEQ_NO", size = 19, comment = "작업일련번호", sequence = "FX_SEQ_OPSEQNO")
	private long opSeqNo;

	@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호")
	private int userNo;

	@FxColumn(name = "USER_NAME", size = 50, nullable = true, comment = "사용자명")
	private String userName;

	@FxColumn(name = "SESSION_ID", size = 50, comment = "세션ID")
	private String sessionId;

	@FxColumn(name = "OP_ID", size = 30, comment = "기능ID")
	private String opId;

	@FxColumn(name = "OP_NAME", size = 100, nullable = true, comment = "기능명")
	private String opName;

	@FxColumn(name = "OP_OBJ_TYPE", size = 20, nullable = true, comment = "작업객체종류")
	private String opObjType;

	@FxColumn(name = "OP_OBJ_NO", size = 100, nullable = true, comment = "작업객체관리번호")
	private String opObjNo;

	@FxColumn(name = "OP_OBJ_NAME", size = 100, nullable = true, comment = "작업객체명")
	private String opObjName;

	@FxColumn(name = "RST_NO", size = 9, nullable = true, comment = "결과번호")
	private int rstNo;

	@FxColumn(name = "RST_CONT", size = 1000, nullable = true, comment = "결과내용")
	private String rstCont;

	@FxColumn(name = "STRT_DTM", size = 14, nullable = true, comment = "시작일시")
	private long strtDtm;

	@FxColumn(name = "END_DTM", size = 14, nullable = true, comment = "종료일시")
	private long endDtm;

	@FxColumn(name = "IN_PARA", size = 1000, nullable = true, comment = "입력인수")
	private String inPara;

	@FxColumn(name = "OUT_RET", size = 1000, nullable = true, comment = "출력결과")
	private String outRet;

	/**
	 * 작업일련번호
	 * 
	 * @return 작업일련번호
	 */
	public long getOpSeqNo() {
		return opSeqNo;
	}

	/**
	 * 작업일련번호
	 * 
	 * @param opSeqNo 작업일련번호
	 */
	public void setOpSeqNo(long opSeqNo) {
		this.opSeqNo = opSeqNo;
	}

	/**
	 * 사용자번호
	 * 
	 * @return 사용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 사용자번호
	 * 
	 * @param userNo 사용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	/**
	 * 사용자명
	 * 
	 * @return 사용자명
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 사용자명
	 * 
	 * @param userName 사용자명
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 세션ID
	 * 
	 * @return 세션ID
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * 세션ID
	 * 
	 * @param sessionId 세션ID
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 기능ID
	 * 
	 * @return 기능ID
	 */
	public String getOpId() {
		return opId;
	}

	/**
	 * 기능ID
	 * 
	 * @param opId 기능ID
	 */
	public void setOpId(String opId) {
		this.opId = opId;
	}

	/**
	 * 기능명
	 * 
	 * @return 기능명
	 */
	public String getOpName() {
		return opName;
	}

	/**
	 * 기능명
	 * 
	 * @param opName 기능명
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * 작업객체종류
	 * 
	 * @return 작업객체종류
	 */
	public String getOpObjType() {
		return opObjType;
	}

	/**
	 * 작업객체종류
	 * 
	 * @param opObjType 작업객체종류
	 */
	public void setOpObjType(String opObjType) {
		this.opObjType = opObjType;
	}

	/**
	 * 작업객체관리번호
	 * 
	 * @return 작업객체관리번호
	 */
	public String getOpObjNo() {
		return opObjNo;
	}

	/**
	 * 작업객체관리번호
	 * 
	 * @param opObjNo 작업객체관리번호
	 */
	public void setOpObjNo(String opObjNo) {
		this.opObjNo = opObjNo;
	}

	/**
	 * 작업객체명
	 * 
	 * @return 작업객체명
	 */
	public String getOpObjName() {
		return opObjName;
	}

	/**
	 * 작업객체명
	 * 
	 * @param opObjName 작업객체명
	 */
	public void setOpObjName(String opObjName) {
		this.opObjName = opObjName;
	}

	/**
	 * 결과번호
	 * 
	 * @return 결과번호
	 */
	public int getRstNo() {
		return rstNo;
	}

	/**
	 * 결과번호
	 * 
	 * @param rstNo 결과번호
	 */
	public void setRstNo(int rstNo) {
		this.rstNo = rstNo;
	}

	/**
	 * 결과내용
	 * 
	 * @return 결과내용
	 */
	public String getRstCont() {
		return rstCont;
	}

	/**
	 * 결과내용
	 * 
	 * @param rstCont 결과내용
	 */
	public void setRstCont(String rstCont) {
		this.rstCont = rstCont;
	}

	/**
	 * 시작일시
	 * 
	 * @return 시작일시
	 */
	public long getStrtDtm() {
		return strtDtm;
	}

	/**
	 * 시작일시
	 * 
	 * @param strtDtm 시작일시
	 */
	public void setStrtDtm(long strtDtm) {
		this.strtDtm = strtDtm;
	}

	/**
	 * 종료일시
	 * 
	 * @return 종료일시
	 */
	public long getEndDtm() {
		return endDtm;
	}

	/**
	 * 종료일시
	 * 
	 * @param endDtm 종료일시
	 */
	public void setEndDtm(long endDtm) {
		this.endDtm = endDtm;
	}

	/**
	 * 입력인수
	 * 
	 * @return 입력인수
	 */
	public String getInPara() {
		return inPara;
	}

	/**
	 * 입력인수
	 * 
	 * @param inPara 입력인수
	 */
	public void setInPara(String inPara) {
		this.inPara = inPara;
	}

	/**
	 * 출력결과
	 * 
	 * @return 출력결과
	 */
	public String getOutRet() {
		return outRet;
	}

	/**
	 * 출력결과
	 * 
	 * @param outRet 출력결과
	 */
	public void setOutRet(String outRet) {
		this.outRet = outRet;
	}
}

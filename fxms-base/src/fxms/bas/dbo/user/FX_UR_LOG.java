package fxms.bas.dbo.user;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.01.23 10:31
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_LOG", comment = "운용자(작업이력)테이블")
@FxIndex(name = "FX_UR_LOG__PK", type = INDEX_TYPE.PK, columns = { "OP_SEQNO" })
@FxIndex(name = "FX_UR_LOG__KEY_SES", type = INDEX_TYPE.KEY, columns = { "SESSION_ID" })
@FxIndex(name = "FX_UR_LOG__KEY_OP", type = INDEX_TYPE.KEY, columns = { "OP_NO" })
public class FX_UR_LOG implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8194118700624367615L;

	public FX_UR_LOG() {
	}

	public static final String FX_SEQ_OPSEQNO = "FX_SEQ_OPSEQNO";
	@FxColumn(name = "OP_SEQNO", size = 19, comment = "작업일련번호", sequence = "FX_SEQ_OPSEQNO")
	private Number opSeqno;

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호")
	private Number userNo;

	@FxColumn(name = "USER_NAME", size = 50, nullable = true, comment = "운용자명")
	private String userName;

	@FxColumn(name = "SESSION_ID", size = 50, comment = "세션ID")
	private String sessionId;

	@FxColumn(name = "OP_NO", size = 9, nullable = true, comment = "운용번호")
	private Number opNo;

	@FxColumn(name = "OP_NAME", size = 100, nullable = true, comment = "기능명")
	private String opName;

	@FxColumn(name = "OP_OBJ_TYPE", size = 20, nullable = true, comment = "작업객체종류")
	private String opObjType;

	@FxColumn(name = "OP_OBJ_NO", size = 100, nullable = true, comment = "작업객체관리번호")
	private String opObjNo;

	@FxColumn(name = "OP_OBJ_NAME", size = 100, nullable = true, comment = "작업객체명")
	private String opObjName;

	@FxColumn(name = "RET_NO", size = 9, nullable = true, comment = "결과번호")
	private Number retNo;

	@FxColumn(name = "RET_MSG", size = 1000, nullable = true, comment = "결과내용")
	private String retMsg;

	@FxColumn(name = "SRT_DATE", size = 14, nullable = true, comment = "시작일시")
	private Number srtDate;

	@FxColumn(name = "END_DATE", size = 14, nullable = true, comment = "종료일시")
	private Number endDate;

	@FxColumn(name = "IN_PARA", size = 1000, nullable = true, comment = "입력인수")
	private String inPara;

	@FxColumn(name = "OUT_RET", size = 1000, nullable = true, comment = "출력결과")
	private String outRet;

	/**
	 * 작업일련번호
	 * 
	 * @return 작업일련번호
	 */
	public Number getOpSeqno() {
		return opSeqno;
	}

	/**
	 * 작업일련번호
	 * 
	 * @param opSeqno
	 *            작업일련번호
	 */
	public void setOpSeqno(Number opSeqno) {
		this.opSeqno = opSeqno;
	}

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public Number getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(Number userNo) {
		this.userNo = userNo;
	}

	/**
	 * 운용자명
	 * 
	 * @return 운용자명
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 운용자명
	 * 
	 * @param userName
	 *            운용자명
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
	 * @param sessionId
	 *            세션ID
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 운용번호
	 * 
	 * @return 운용번호
	 */
	public Number getOpNo() {
		return opNo;
	}

	/**
	 * 운용번호
	 * 
	 * @param opNo
	 *            운용번호
	 */
	public void setOpNo(Number opNo) {
		this.opNo = opNo;
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
	 * @param opName
	 *            기능명
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
	 * @param opObjType
	 *            작업객체종류
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
	 * @param opObjNo
	 *            작업객체관리번호
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
	 * @param opObjName
	 *            작업객체명
	 */
	public void setOpObjName(String opObjName) {
		this.opObjName = opObjName;
	}

	/**
	 * 결과번호
	 * 
	 * @return 결과번호
	 */
	public Number getRetNo() {
		return retNo;
	}

	/**
	 * 결과번호
	 * 
	 * @param retNo
	 *            결과번호
	 */
	public void setRetNo(Number retNo) {
		this.retNo = retNo;
	}

	/**
	 * 결과내용
	 * 
	 * @return 결과내용
	 */
	public String getRetMsg() {
		return retMsg;
	}

	/**
	 * 결과내용
	 * 
	 * @param retMsg
	 *            결과내용
	 */
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	/**
	 * 시작일시
	 * 
	 * @return 시작일시
	 */
	public Number getSrtDate() {
		return srtDate;
	}

	/**
	 * 시작일시
	 * 
	 * @param srtDate
	 *            시작일시
	 */
	public void setSrtDate(Number srtDate) {
		this.srtDate = srtDate;
	}

	/**
	 * 종료일시
	 * 
	 * @return 종료일시
	 */
	public Number getEndDate() {
		return endDate;
	}

	/**
	 * 종료일시
	 * 
	 * @param endDate
	 *            종료일시
	 */
	public void setEndDate(Number endDate) {
		this.endDate = endDate;
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
	 * @param inPara
	 *            입력인수
	 */
	public void setInPara(String inPara) {
		if (inPara.length() > 500) {
			this.inPara = inPara.substring(0, 500);
		} else {
			this.inPara = inPara;
		}
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
	 * @param outRet
	 *            출력결과
	 */
	public void setOutRet(String outRet) {
		this.outRet = outRet;
	}
}

package fxms.rule.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.01.26 16:58
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_BR_RULE_RUN_HST", comment = "룰실행이력테이블")
@FxIndex(name = "FX_BR_RULE_RUN_HST__PK", type = INDEX_TYPE.PK, columns = { "BR_RUN_NO" })
@FxIndex(name = "FX_BR_RULE_RUN_HST__KEY1", type = INDEX_TYPE.KEY, columns = { "BR_RULE_NO" })
@FxIndex(name = "FX_BR_RULE_RUN_HST__KEY2", type = INDEX_TYPE.KEY, columns = { "RUN_STRT_DTM" })
@FxIndex(name = "FX_BR_RULE_RUN_HST__FK1", type = INDEX_TYPE.FK, columns = {
		"BR_RULE_NO" }, fkTable = "FX_BR_RULE", fkColumn = "BR_RULE_NO")
public class FX_BR_RULE_RUN_HST implements Serializable {

	public FX_BR_RULE_RUN_HST() {
	}

	public static final String FX_SEQ_BRRUNNO = "FX_SEQ_BRRUNNO";
	@FxColumn(name = "BR_RUN_NO", size = 14, comment = "비즈니스룰실행번호", sequence = "FX_SEQ_BRRUNNO")
	private Long brRunNo;

	@FxColumn(name = "RULE_NAME", size = 100, comment = "룰명")
	private String ruleName;

	@FxColumn(name = "RUN_STRT_DTM", size = 14, comment = "실행시작일시")
	private Long runStrtDtm;

	@FxColumn(name = "RUN_FNSH_REQ_DTM", size = 14, nullable = true, comment = "실행종료요청일시", defValue = "0")
	private Long runFnshReqDtm = 0L;

	@FxColumn(name = "RUN_FNSH_DTM", size = 14, nullable = true, comment = "실행종료일시")
	private Long runFnshDtm;

	@FxColumn(name = "TOT_ACT_CNT", size = 5, nullable = true, comment = "전체행위건수")
	private Integer totActCnt;

	@FxColumn(name = "RUN_ACT_CNT", size = 5, nullable = true, comment = "실행행위건수")
	private Integer runActCnt;

	@FxColumn(name = "ERR_ACT_CNT", size = 5, nullable = true, comment = "오류행위건수")
	private Integer errActCnt;

	@FxColumn(name = "RST_TEXT", size = 1000, nullable = true, comment = "결과문구")
	private String rstText;

	@FxColumn(name = "BR_RULE_NO", size = 9, comment = "비즈니스룰번호")
	private Integer brRuleNo;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Long regDtm;

	/**
	 * 비즈니스룰실행번호
	 * 
	 * @return 비즈니스룰실행번호
	 */
	public Long getBrRunNo() {
		return brRunNo;
	}

	/**
	 * 비즈니스룰실행번호
	 * 
	 * @param brRunNo 비즈니스룰실행번호
	 */
	public void setBrRunNo(Long brRunNo) {
		this.brRunNo = brRunNo;
	}

	/**
	 * 룰명
	 * 
	 * @return 룰명
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * 룰명
	 * 
	 * @param ruleName 룰명
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * 실행시작일시
	 * 
	 * @return 실행시작일시
	 */
	public Long getRunStrtDtm() {
		return runStrtDtm;
	}

	/**
	 * 실행시작일시
	 * 
	 * @param runStrtDtm 실행시작일시
	 */
	public void setRunStrtDtm(Long runStrtDtm) {
		this.runStrtDtm = runStrtDtm;
	}

	/**
	 * 실행종료요청일시
	 * 
	 * @return 실행종료요청일시
	 */
	public Long getRunFnshReqDtm() {
		return runFnshReqDtm;
	}

	/**
	 * 실행종료요청일시
	 * 
	 * @param runFnshReqDtm 실행종료요청일시
	 */
	public void setRunFnshReqDtm(Long runFnshReqDtm) {
		this.runFnshReqDtm = runFnshReqDtm;
	}

	/**
	 * 실행종료일시
	 * 
	 * @return 실행종료일시
	 */
	public Long getRunFnshDtm() {
		return runFnshDtm;
	}

	/**
	 * 실행종료일시
	 * 
	 * @param runFnshDtm 실행종료일시
	 */
	public void setRunFnshDtm(Long runFnshDtm) {
		this.runFnshDtm = runFnshDtm;
	}

	/**
	 * 전체행위건수
	 * 
	 * @return 전체행위건수
	 */
	public Integer getTotActCnt() {
		return totActCnt;
	}

	/**
	 * 전체행위건수
	 * 
	 * @param totActCnt 전체행위건수
	 */
	public void setTotActCnt(Integer totActCnt) {
		this.totActCnt = totActCnt;
	}

	/**
	 * 실행행위건수
	 * 
	 * @return 실행행위건수
	 */
	public Integer getRunActCnt() {
		return runActCnt;
	}

	/**
	 * 실행행위건수
	 * 
	 * @param runActCnt 실행행위건수
	 */
	public void setRunActCnt(Integer runActCnt) {
		this.runActCnt = runActCnt;
	}

	/**
	 * 오류행위건수
	 * 
	 * @return 오류행위건수
	 */
	public Integer getErrActCnt() {
		return errActCnt;
	}

	/**
	 * 오류행위건수
	 * 
	 * @param errActCnt 오류행위건수
	 */
	public void setErrActCnt(Integer errActCnt) {
		this.errActCnt = errActCnt;
	}

	/**
	 * 결과문구
	 * 
	 * @return 결과문구
	 */
	public String getRstText() {
		return rstText;
	}

	/**
	 * 결과문구
	 * 
	 * @param rstText 결과문구
	 */
	public void setRstText(String rstText) {
		this.rstText = rstText;
	}

	/**
	 * 비즈니스룰번호
	 * 
	 * @return 비즈니스룰번호
	 */
	public Integer getBrRuleNo() {
		return brRuleNo;
	}

	/**
	 * 비즈니스룰번호
	 * 
	 * @param brRuleNo 비즈니스룰번호
	 */
	public void setBrRuleNo(Integer brRuleNo) {
		this.brRuleNo = brRuleNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public Integer getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(Integer regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(Long regDtm) {
		this.regDtm = regDtm;
	}
}

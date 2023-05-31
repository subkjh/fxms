package fxms.rule.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * 룰실행 중지 요청 확인용
 * 
 * @since 2023.01.26 16:58
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_BR_RULE_RUN_HST", comment = "룰실행이력테이블")
@FxIndex(name = "FX_BR_RULE_RUN_HST__PK", type = INDEX_TYPE.PK, columns = { "BR_RUN_NO" })
public class SelectRunToStopDbo {

	public SelectRunToStopDbo() {
	}

	@FxColumn(name = "BR_RUN_NO", size = 14, comment = "비즈니스룰실행번호", sequence = "FX_SEQ_BRRUNNO")
	private Long brRunNo;

	@FxColumn(name = "RUN_FNSH_REQ_DTM", size = 14, nullable = true, comment = "실행종료요청일시", defValue = "0")
	private Long runFnshReqDtm = 0L;

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
}

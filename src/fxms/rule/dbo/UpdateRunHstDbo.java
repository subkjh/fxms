package fxms.rule.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * 룰 실행 완료 기록용
 * 
 * @since 2023.01.26 16:58
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_BR_RULE_RUN_HST", comment = "룰실행이력테이블")
@FxIndex(name = "FX_BR_RULE_RUN_HST__PK", type = INDEX_TYPE.PK, columns = { "BR_RUN_NO" })
public class UpdateRunHstDbo {

	public UpdateRunHstDbo() {
	}

	@FxColumn(name = "BR_RUN_NO", size = 14, comment = "비즈니스룰실행번호")
	private Long brRunNo;

	@FxColumn(name = "RUN_FNSH_DTM", size = 14, nullable = true, comment = "실행종료일시")
	private Long runFnshDtm;

	@FxColumn(name = "RUN_ACT_CNT", size = 5, nullable = true, comment = "실행행위건수")
	private Integer runActCnt;

	@FxColumn(name = "ERR_ACT_CNT", size = 5, nullable = true, comment = "오류행위건수")
	private Integer errActCnt;

	@FxColumn(name = "RST_TEXT", size = 1000, nullable = true, comment = "결과문구")
	private String rstText;

}

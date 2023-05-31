package fxms.bas.impl.dbo;

import java.util.Calendar;

import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

@FxTable(name = "FX_UR_USER", comment = "사용자테이블")
@FxIndex(name = "FX_UR_USER__PK", type = INDEX_TYPE.PK, columns = { "USER_NO" })
public class UpdateUserDateDbo {

	@FxColumn(name = "USER_NO", size = 9, comment = "사용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private boolean useYn = false;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	@FxColumn(name = "USE_END_DATE", size = 8, nullable = true, comment = "사용종료일자")
	private int useEndDate;

	@FxColumn(name = "STR_DATE", size = 8, comment = "사용종료일자(1m|3m|6m|12m)", operator = COLUMN_OP.nothing)
	private String strDate;

	public UpdateUserDateDbo() {
	}

	public void makeDate() {

		int amount = 1;
		try {
			amount = Integer.parseInt(strDate.replaceAll("m", ""));
		} catch (Exception e) {
		}
		useEndDate = DateUtil.getYmd(DateUtil.addTime(System.currentTimeMillis(), Calendar.MONTH, amount));
		useYn = true;
		strDate = null;
	}

}

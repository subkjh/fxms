package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.04.03 14:07
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_TIME", comment = "운용자시간테이블")
@FxIndex(name = "FX_UR_TIME__PK", type = INDEX_TYPE.PK, columns = { "USER_NO", "DATA_TYPE" })
public class FX_UR_TIME {

	public FX_UR_TIME() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호", defValue = "0")
	private int userNo = 0;

	@FxColumn(name = "DATA_TYPE", size = 20, comment = "객체분류")
	private String dataType;

	@FxColumn(name = "DONE_DATE", size = 14, comment = "완료일시")
	private long doneDate;

	@FxColumn(name = "DATA_COUNT", size = 9, nullable = true, comment = "처리건수")
	private int dataCount;

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	/**
	 * 객체분류
	 * 
	 * @return 객체분류
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * 객체분류
	 * 
	 * @param dataType
	 *            객체분류
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * 완료일시
	 * 
	 * @return 완료일시
	 */
	public long getDoneDate() {
		return doneDate;
	}

	/**
	 * 완료일시
	 * 
	 * @param doneDate
	 *            완료일시
	 */
	public void setDoneDate(long doneDate) {
		this.doneDate = doneDate;
	}

	/**
	 * 처리건수
	 * 
	 * @return 처리건수
	 */
	public int getDataCount() {
		return dataCount;
	}

	/**
	 * 처리건수
	 * 
	 * @param dataCount
	 *            처리건수
	 */
	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
}

package fxms.bas.impl.co;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_UR_USER", comment = "운용자테이블")
@FxIndex(name = "FX_UR_USER__PK", type = INDEX_TYPE.PK, columns = { "USER_NO" })
public class DeleteUserDbo {

	public DeleteUserDbo() {
	}

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호", sequence = "FX_SEQ_USERNO")
	private int userNo;

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

}
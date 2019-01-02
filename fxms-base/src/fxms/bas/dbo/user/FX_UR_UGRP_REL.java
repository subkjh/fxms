package fxms.bas.dbo.user;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.06.12 10:11
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_UGRP_REL", comment = "운용자그룹상속관계테이블")
@FxIndex(name = "FX_UR_UGRP_REL__PK", type = INDEX_TYPE.PK, columns = { "UGRP_NO", "REL_UGRP_NO" })
public class FX_UR_UGRP_REL {

	public FX_UR_UGRP_REL() {
	}

	@FxColumn(name = "UGRP_NO", size = 9, comment = "운용자그룹번호")
	private int ugrpNo;

	@FxColumn(name = "REL_UGRP_NO", size = 9, comment = "관계운용자그룹번호")
	private int relUgrpNo;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시")
	private long regDate;

	/**
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	/**
	 * 관계운용자그룹번호
	 * 
	 * @return 관계운용자그룹번호
	 */
	public int getRelUgrpNo() {
		return relUgrpNo;
	}

	/**
	 * 관계운용자그룹번호
	 * 
	 * @param relUgrpNo
	 *            관계운용자그룹번호
	 */
	public void setRelUgrpNo(int relUgrpNo) {
		this.relUgrpNo = relUgrpNo;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}
}

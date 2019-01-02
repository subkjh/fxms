package com.fxms.nms.dbo;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.06.28 10:34
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FN_NET_ITEM", comment = "네트워크항목테이블")
@FxIndex(name = "FN_NET_ITEM__PK", type = INDEX_TYPE.PK, columns = { "NET_NO", "ITEM_SEQNO" })
public class FN_NET_ITEM {

	public FN_NET_ITEM() {
	}

	@FxColumn(name = "NET_NO", size = 9, comment = "네트워크번호")
	private int netNo;

	@FxColumn(name = "ITEM_SEQNO", size = 9, comment = "구성일련번호")
	private int itemSeqno;

	@FxColumn(name = "START_NE_MO_NO", size = 19, comment = "시작NE관리번호")
	private long startNeMoNo;

	@FxColumn(name = "START_IF_MO_NO", size = 19, nullable = true, comment = "시작포트관리번호")
	private long startIfMoNo;

	@FxColumn(name = "END_NE_MO_NO", size = 19, comment = "끝NE관리번호")
	private long endNeMoNo;

	@FxColumn(name = "END_IF_MO_NO", size = 19, nullable = true, comment = "끝포트관리번호")
	private long endIfMoNo;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	/**
	 * 네트워크번호
	 * 
	 * @return 네트워크번호
	 */
	public int getNetNo() {
		return netNo;
	}

	/**
	 * 네트워크번호
	 * 
	 * @param netNo
	 *            네트워크번호
	 */
	public void setNetNo(int netNo) {
		this.netNo = netNo;
	}

	/**
	 * 구성일련번호
	 * 
	 * @return 구성일련번호
	 */
	public int getItemSeqno() {
		return itemSeqno;
	}

	/**
	 * 구성일련번호
	 * 
	 * @param itemSeqno
	 *            구성일련번호
	 */
	public void setItemSeqno(int itemSeqno) {
		this.itemSeqno = itemSeqno;
	}

	/**
	 * 시작NE관리번호
	 * 
	 * @return 시작NE관리번호
	 */
	public long getStartNeMoNo() {
		return startNeMoNo;
	}

	/**
	 * 시작NE관리번호
	 * 
	 * @param startNeMoNo
	 *            시작NE관리번호
	 */
	public void setStartNeMoNo(long startNeMoNo) {
		this.startNeMoNo = startNeMoNo;
	}

	/**
	 * 시작포트관리번호
	 * 
	 * @return 시작포트관리번호
	 */
	public long getStartIfMoNo() {
		return startIfMoNo;
	}

	/**
	 * 시작포트관리번호
	 * 
	 * @param startIfMoNo
	 *            시작포트관리번호
	 */
	public void setStartIfMoNo(long startIfMoNo) {
		this.startIfMoNo = startIfMoNo;
	}

	/**
	 * 끝NE관리번호
	 * 
	 * @return 끝NE관리번호
	 */
	public long getEndNeMoNo() {
		return endNeMoNo;
	}

	/**
	 * 끝NE관리번호
	 * 
	 * @param endNeMoNo
	 *            끝NE관리번호
	 */
	public void setEndNeMoNo(long endNeMoNo) {
		this.endNeMoNo = endNeMoNo;
	}

	/**
	 * 끝포트관리번호
	 * 
	 * @return 끝포트관리번호
	 */
	public long getEndIfMoNo() {
		return endIfMoNo;
	}

	/**
	 * 끝포트관리번호
	 * 
	 * @param endIfMoNo
	 *            끝포트관리번호
	 */
	public void setEndIfMoNo(long endIfMoNo) {
		this.endIfMoNo = endIfMoNo;
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

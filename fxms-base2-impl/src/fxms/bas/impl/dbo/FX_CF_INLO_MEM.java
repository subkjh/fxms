package fxms.bas.impl.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CF_INLO_MEM", comment = "설치위치구성원테이블")
@FxIndex(name = "FX_CF_INLO_CHLD__PK", type = INDEX_TYPE.PK, columns = { "INLO_NO", "MEM_INLO_NO" })
@FxIndex(name = "FX_CF_INLO_CHLD__KEY1", type = INDEX_TYPE.KEY, columns = { "INLO_NO" })
@FxIndex(name = "FX_CF_INLO_CHLD__KEY2", type = INDEX_TYPE.KEY, columns = { "MEM_INLO_NO" })
public class FX_CF_INLO_MEM implements Serializable {

	public FX_CF_INLO_MEM() {
	}

	@FxColumn(name = "INLO_NO", size = 9, comment = "위치번호")
	private int inloNo;

	@FxColumn(name = "MEM_INLO_NO", size = 9, comment = "위치번호(하위)")
	private int memInloNo;

	@FxColumn(name = "MEM_DEPTH", size = 5, nullable = true, comment = "상속깊이", defValue = "0")
	private int memDepth = 0;

	@FxColumn(name = "INLO_TYPE", size = 30, comment = "설치위치종류(코드집)")
	private String inloType;
	
	public String getInloType() {
		return inloType;
	}

	public void setInloType(String inloType) {
		this.inloType = inloType;
	}

	/**
	 * 위치번호
	 * 
	 * @return 위치번호
	 */
	public int getInloNo() {
		return inloNo;
	}

	/**
	 * 위치번호
	 * 
	 * @param inloNo
	 *            위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * 위치번호(하위)
	 * 
	 * @return 위치번호(하위)
	 */
	public int getMemInloNo() {
		return memInloNo;
	}

	/**
	 * 위치번호(하위)
	 * 
	 * @param memInloNo
	 *            위치번호(하위)
	 */
	public void setMemInloNo(int memInloNo) {
		this.memInloNo = memInloNo;
	}

	/**
	 * 상속깊이
	 * 
	 * @return 상속깊이
	 */
	public int getMemDepth() {
		return memDepth;
	}

	/**
	 * 상속깊이
	 * 
	 * @param memDepth
	 *            상속깊이
	 */
	public void setMemDepth(int memDepth) {
		this.memDepth = memDepth;
	}
}

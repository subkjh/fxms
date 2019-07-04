package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.01.30 16:23	
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_SEQ_TAB", comment = "시퀀스테이블")
@FxIndex(name = "FX_SEQ_TAB__PK", type = INDEX_TYPE.PK, columns = { "SEQ_NAME" })
public class FX_SEQ_TAB  {

	public FX_SEQ_TAB() {
	}

	@FxColumn(name = "SEQ_NAME", size = 20, comment = "시퀀스명")
	private String seqName;

	@FxColumn(name = "INC_BY", size = 5, comment = "증가")
	private Number incBy;

	@FxColumn(name = "VALUE_MIN", size = 19, comment = "최소값")
	private Number valueMin;

	@FxColumn(name = "VALUE_MAX", size = 19, comment = "최대값")
	private Number valueMax;

	@FxColumn(name = "VALUE_NEXT", size = 19, comment = "다음값")
	private Number valueNext;

	@FxColumn(name = "IS_CYCLE", size = 1, comment = "순환여부")
	private String cycle;

	/**
	 * 시퀀스명
	 * 
	 * @return 시퀀스명
	 */
	public String getSeqName() {
		return seqName;
	}

	/**
	 * 시퀀스명
	 * 
	 * @param seqName
	 *            시퀀스명
	 */
	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	/**
	 * 증가
	 * 
	 * @return 증가
	 */
	public Number getIncBy() {
		return incBy;
	}

	/**
	 * 증가
	 * 
	 * @param incBy
	 *            증가
	 */
	public void setIncBy(Number incBy) {
		this.incBy = incBy;
	}

	/**
	 * 최소값
	 * 
	 * @return 최소값
	 */
	public Number getValueMin() {
		return valueMin;
	}

	/**
	 * 최소값
	 * 
	 * @param valueMin
	 *            최소값
	 */
	public void setValueMin(Number valueMin) {
		this.valueMin = valueMin;
	}

	/**
	 * 최대값
	 * 
	 * @return 최대값
	 */
	public Number getValueMax() {
		return valueMax;
	}

	/**
	 * 최대값
	 * 
	 * @param valueMax
	 *            최대값
	 */
	public void setValueMax(Number valueMax) {
		this.valueMax = valueMax;
	}

	/**
	 * 다음값
	 * 
	 * @return 다음값
	 */
	public Number getValueNext() {
		return valueNext;
	}

	/**
	 * 다음값
	 * 
	 * @param valueNext
	 *            다음값
	 */
	public void setValueNext(Number valueNext) {
		this.valueNext = valueNext;
	}

	/**
	 * 순환여부
	 * 
	 * @return 순환여부
	 */
	public String isCycle() {
		return cycle;
	}

	/**
	 * 순환여부
	 * 
	 * @param cycle
	 *            순환여부
	 */
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
}

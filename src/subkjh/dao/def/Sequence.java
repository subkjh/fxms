package subkjh.dao.def;

/**
 * Sequence 내용
 * 
 * @author subkjh
 * @since 2013.10.02
 * 
 */
public class Sequence {

	/** 시퀀스명 */
	private String sequenceName;
	/** 증가수 */
	private long incBy;
	/** 최대값 */
	private long valueMax;
	/** 최소값 */
	private long valueMin;
	/** 순환여부 */
	private boolean cycle;
	/** 다음값 */
	private long valueNext;

	/** 캐쉬된 값 */
	private long valueMaxCache;

	public Sequence() {

	}

	public long getIncBy() {
		return incBy;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public long getValueMax() {
		return valueMax;
	}

	public long getValueMaxCache() {
		return valueMaxCache;
	}

	public long getValueMin() {
		return valueMin;
	}

	public long getValueNext() {
		return valueNext;
	}

	public boolean isCycle() {
		return cycle;
	}

	public void setCycle(boolean cycle) {
		this.cycle = cycle;
	}

	public void setIncBy(long incBy) {
		this.incBy = incBy;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public void setValueMax(long valueMax) {
		this.valueMax = valueMax;
	}

	public void setValueMaxCache(long valueMaxCache) {
		this.valueMaxCache = valueMaxCache;
	}

	public void setValueMin(long valueMin) {
		this.valueMin = valueMin;
	}

	public void setValueNext(long valueNext) {
		this.valueNext = valueNext;
	}

	@Override
	public String toString() {
		return sequenceName + "|" + valueMin + "/" + valueMax + "=" + valueNext;
	}

	public String getXml() {

		return "<seqno cycle=\"" + cycle + "\" hstime=\"20140328134942\" id=\"" + sequenceName + "\" max=\"" + valueMax
				+ "\" next=\"" + valueMin + "\" start=\"" + valueMin + "\"/>";
	}

}

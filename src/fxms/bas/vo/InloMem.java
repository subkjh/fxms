package fxms.bas.vo;

public class InloMem {

	private int inloNo;
	private int lowerInloNo;
	private int lowerDepth;

	public InloMem(int inloNo, int lowerInloNo, int depth) {
		this.inloNo = inloNo;
		this.lowerInloNo = lowerInloNo;
		lowerDepth = depth;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(inloNo);
		sb.append(",").append(lowerInloNo);
		sb.append(",").append(lowerDepth);

		return sb.toString();
	}

	public int getInloNo() {
		return inloNo;
	}

	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public int getLowerInloNo() {
		return lowerInloNo;
	}

	public void setLowerInloNo(int lowerInloNo) {
		this.lowerInloNo = lowerInloNo;
	}

	public int getLowerDepth() {
		return lowerDepth;
	}

	public void setLowerDepth(int lowerDepth) {
		this.lowerDepth = lowerDepth;
	}

}

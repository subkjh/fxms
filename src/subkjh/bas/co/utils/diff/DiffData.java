package subkjh.bas.co.utils.diff;

public class DiffData {

	/** 이전 데이터 */
	private Object bfData;

	/** 이후 데이터 */
	private Object afData;

	private String name;

	public DiffData(String name, Object bfData, Object afData) {
		this.name = name;
		this.bfData = bfData;
		this.afData = afData;
	}

	public Object getAfData() {
		return afData;
	}

	public Object getBfData() {
		return bfData;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(name);
		sb.append("(");
		sb.append(bfData);
		sb.append(" -> ");
		sb.append(afData);
		sb.append(")");

		return sb.toString();
	}

}

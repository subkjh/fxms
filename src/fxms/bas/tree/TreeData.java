package fxms.bas.tree;

public class TreeData {

	private final String dataId;
	private final String dataText;
	private final String dataType;
	private final String dataClass;

	public TreeData(Object dataId, String dataText, String dataType, String dataClass) {
		this.dataId = dataId.toString();
		this.dataText = dataText;
		this.dataType = dataType;
		this.dataClass = dataClass;
	}

	public String getDataId() {
		return dataId;
	}

	public String getDataText() {
		return dataText;
	}

	public String getDataType() {
		return dataType;
	}

	public String getDataClass() {
		return dataClass;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(dataId).append(")").append(dataText).append("(").append(dataType).append(")");
		return sb.toString();
	}

}

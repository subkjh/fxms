package subkjh.bas.dao.model;

/**
 * 조회용 SQL<br>
 * 
 * @author subkjh
 * 
 */
public class SqlSelectBean extends SqlBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3975307391697830768L;
	/**
	 * 결과를 담는 클래스
	 */
	private ResultBean resultMap;

	public SqlSelectBean(String qid, ResultBean resultMap) {
		super(qid, null);
		this.resultMap = resultMap;
	}

	public SqlSelectBean(String qid, ResultBean resultMap, String database) {
		super(qid, database);
		this.resultMap = resultMap;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			if (obj instanceof SqlSelectBean) {
				SqlSelectBean target = (SqlSelectBean) obj;
				try {
					return target.resultMap.equals(resultMap);
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	/**
	 * @return the resultMap
	 */
	public ResultBean getResultMap() {
		return resultMap;
	}

	@Override
	public String getXml() {
		String tag = "select";
		StringBuffer ret = new StringBuffer();

		ret.append("<" + tag + " id=\"" + getQid() + "\"");
		ret.append(" resultMap=\"" + resultMap.getResultId() + "\"");
		ret.append(">\n");

		for (SqlElement e : getChildren()) {
			ret.append("\t<![CDATA[\n");
			ret.append(addPrevString(e.getSql(), "\t"));
			ret.append("\t]]>\n");
		}

		if (isIncludeDescr) {
			ret.append("\t<descr>\n");
			ret.append("\t<![CDATA[\n");
			ret.append(addPrevString(getDescr(), "\t"));
			ret.append("\t]]>\n");
			ret.append("\t</descr>\n");
		}

		ret.append("</" + tag + ">\n");
		return ret.toString();
	}

	/**
	 * @param resultMap
	 *            the resultMap to set
	 */
	public void setResultMap(ResultBean resultMap) {
		this.resultMap = resultMap;
	}

	@Override
	public String toString() {
		return super.toString() + "|" + (resultMap == null ? "null" : resultMap.getResultId());
	}

	@Override
	protected String getXmlTag() {
		return "select";
	}

}

package subkjh.dao.model;

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
	private DaoResult result;

	public SqlSelectBean(String qid, DaoResult resultMap) {
		super(qid, null);
		this.result = resultMap;
	}

	public SqlSelectBean(String qid, DaoResult resultMap, String database) {
		super(qid, database);
		this.result = resultMap;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			if (obj instanceof SqlSelectBean) {
				SqlSelectBean target = (SqlSelectBean) obj;
				try {
					return target.result.equals(result);
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	/**
	 * @return the resultMap
	 */
	public DaoResult getResultMap() {
		return result;
	}

	@Override
	public String getXml() {
		String tag = "select";
		StringBuffer ret = new StringBuffer();

		ret.append("<" + tag + " id=\"" + getQid() + "\"");
		ret.append(" resultMap=\"" + result.rID + "\"");
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
	public void setResultMap(DaoResult resultMap) {
		this.result = resultMap;
	}

	@Override
	public String toString() {
		return super.toString() + "|" + (result == null ? "null" : result.rID);
	}

	@Override
	protected String getXmlTag() {
		return "select";
	}

}

package subkjh.bas.dao.model;

/**
 * 데이터베이스로부터 자료를 읽어 들일 때 메소드 또는 필드에 어떻게 값을 채울지에 대한 내용을 담고 있다.
 * 
 * @author subkjh
 * @since 2009-10-28
 * 
 */
public class ResultMappingBean {

	private String column;

	private String javaField;

	private boolean javaMethod;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResultMappingBean) {
			ResultMappingBean target = (ResultMappingBean) obj;
			try {
				if (target.column.equals(column) == false) return false;
				if (target.javaField.equals(javaField) == false) return false;
			}
			catch (Exception e) {
			}

			return true;
		}
		else {
			return false;
		}
	}

	public ResultMappingBean(String javaField, String column) {

		if (javaField.indexOf(")") > 0) {
			this.javaField = javaField.replaceAll("\\(", "");
			this.javaField = this.javaField.replaceAll("\\)", "");
			javaMethod = true;
		}
		else {
			this.javaField = javaField;
			javaMethod = false;
		}

		this.column = column;

	}

	/**
	 * 테이블의 컬럼 명을 제공합니다.
	 * 
	 * @return 테이블의 컬럼 명
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * 자바에 설정한 멤버명을 제공합니다.
	 * 
	 * @return 자바 멤버 명
	 */
	public String getJavaField() {
		return javaField;
	}

	/**
	 * 자바 필드가 메소드인지 변수인지를 제공합니다.
	 * 
	 * @return true이면 메소드 그렇치 않으면 필드
	 */
	public boolean isJavaMethod() {
		return javaMethod;
	}

	public String getXml() {
		return "<result attr=\"" + javaField + (javaMethod ? "()" : "") + "\" field=\"" + column + "\" />\n";
	}

}

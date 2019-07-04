package subkjh.bas.dao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SELECT 문의 결과를 담는 클래스
 * 
 * @author subkjh
 * @since 2009-10-28
 * 
 */
public class ResultBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9211188043328846125L;

	public static void main(String[] args) {
		ResultBean a = new ResultBean("TEST", "aaa.adf.eee");
		a.add(new ResultMappingBean("setAa()", "AA"));
		a.add(new ResultMappingBean("setCodeName()", "CODE_NAME"));
		a.add(new ResultMappingBean("stest", "STEST"));
		System.out.println(a.getXml());

	}

	private Class<?> javaClass;
	private String javaClassName;
	/** 필드처리 내용 */
	private List<ResultMappingBean> mappingList;
	private List<String> includeIdList;
	/** ID */
	private String resultId;

	/**
	 * 
	 * @param rid
	 *            Result ID
	 * @param javaClassName
	 *            값을 넣은 자바 클래스 명
	 */
	public ResultBean(String rid, String javaClassName) {
		this.resultId = rid;
		this.javaClassName = javaClassName;
	}

	/**
	 * 목록을 추가한다.
	 * 
	 * @param map
	 */
	public void add(List<ResultMappingBean> map) {
		if (map != null) {
			if (mappingList == null) {
				mappingList = new ArrayList<ResultMappingBean>();
			}
			for (ResultMappingBean e : map) {
				if (mappingList.contains(e) == false) mappingList.add(e);
			}
		}
	}

	/**
	 * 테이블 컬럼과 자바 클래스 메소드와의 매핑 정보를 추가합니다.
	 * 
	 * @param entry
	 *            추가할 내역
	 */
	public void add(ResultMappingBean... entry) {
		if (mappingList == null) {
			mappingList = new ArrayList<ResultMappingBean>();
		}

		for (ResultMappingBean e : entry)
			mappingList.add(e);
	}

	public void addIncludeId(String id) {

		if (includeIdList == null) {
			includeIdList = new ArrayList<String>();
		}

		includeIdList.add(id);
	}

	/**
	 * 이 객체의 유효한지 판단합니다.
	 * 
	 * @throws Exception
	 *             유효하지 않은 경우 예외 발생합니다.
	 */
	public void checkValid() throws Exception {

		if (resultId == null && resultId.length() == 0) {
			throw new Exception("Not defined the ResultMap ID");
		}

		try {
			setJavaClass(Class.forName(javaClassName));
		}
		catch (ClassNotFoundException e) {
			throw e;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ResultBean) {
			ResultBean target = (ResultBean) obj;
			try {
				if (resultId.equals(target.resultId) == false) return false;
				if (javaClassName.equals(target.javaClassName) == false) return false;
				if (mappingList != null && target.mappingList != null) {
					if (mappingList.size() != target.mappingList.size()) return false;
					for (int index = 0, size = mappingList.size(); index < size; index++) {
						if (mappingList.get(index).equals(target.mappingList.get(index)) == false) return false;
					}
				}
				return true;
			}
			catch (Exception e) {
				return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * 필드 매핑 정보를 넘긴다.
	 * 
	 * @return 필드 매핑 정보
	 */
	public List<ResultMappingBean> getFields() {
		return mappingList;
	}

	public List<String> getIncludeIdList() {
		return includeIdList;
	}

	/**
	 * 결과를 담을 자바 클래스를 제공합니다.
	 * 
	 * @return 자바 클래스
	 */
	public Class<?> getJavaClass() {
		if (javaClass == null) {
			try {
				javaClass = Class.forName(javaClassName);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return javaClass;
	}

	/**
	 * 컬럼과 필드간 매핑 정보를 제공합니다.
	 * 
	 * @return 매핑 정보
	 */
	public List<ResultMappingBean> getMappingFields() {
		return mappingList;
	}

	/**
	 * ID를 제공합니다.
	 * 
	 * @return ID
	 */
	public String getResultId() {
		return resultId;
	}

	/**
	 * ResultBean에 대한 쿼리 XML 내용을 제공합니다.
	 * 
	 * @return 쿼리 XML
	 */
	public String getXml() {

		String ret = "<resultMap id=\"" + resultId + "\" javaClass=\"" + javaClassName + "\">\n";

		if (mappingList != null) {
			for (ResultMappingBean field : mappingList) {
				ret += "\t" + field.getXml();
			}
		}

		ret += "</resultMap>\n";

		return ret;
	}

	/**
	 * 결과를 담을 자바 클래스를 설정합니다.
	 * 
	 * @param javaClass
	 *            결과용 자바 클래스
	 */
	public void setJavaClass(Class<?> javaClass) {
		this.javaClass = javaClass;
	}

	@Override
	public String toString() {
		String ret = getClass().getSimpleName() + "(" + resultId + "," + javaClass + ")";

		if (mappingList != null) {
			ret += "\n\t";
			for (ResultMappingBean field : mappingList) {
				ret += "(" + field.getColumn() + "-" + field.getJavaField() + "),";
			}
		}

		return ret;
	}
}

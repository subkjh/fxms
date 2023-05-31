package subkjh.dao.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import subkjh.dao.util.DaoUtil;

/**
 * SELECT 문의 결과를 담는 클래스
 * 
 * @author subkjh
 * @since 2009-10-28
 * 
 */
public class DaoResult {

	/**
	 * Map으로 데이터를 받을 때 키을 어떤 형식으로 할지 결정한다.
	 * 
	 * @author subkjh
	 *
	 */
	public enum ResultKeyCase {
		upper {
			public String getKey(String s) {
				return s == null ? s : s.toUpperCase();
			}
		},

		lower {
			public String getKey(String s) {
				return s == null ? s : s.toLowerCase();
			}
		},
		java {
			public String getKey(String s) {
				return s == null ? s : DaoUtil.getJavaFieldName(s, false);
			}
		};

		public static ResultKeyCase getKeyCase(String name) {
			for (ResultKeyCase key : ResultKeyCase.values()) {
				if (key.name().equalsIgnoreCase(name)) {
					return key;
				}
			}
			return upper;
		}

		private ResultKeyCase() {

		}

		public String getKey(String s) {
			return s;
		}

	}

	public static void main(String[] args) throws Exception {
		DaoResult a = new DaoResult("TEST", "aaa.adf.eee");
		a.add(new RetMappVo("setAa()", "AA"));
		a.add(new RetMappVo("setCodeName()", "CODE_NAME"));
		a.add(new RetMappVo("stest", "STEST"));
		System.out.println(a.getXml());
	}

	/** 필드처리 내용 */
	private List<RetMappVo> mappingList;
	private List<String> includeIdList;
	/** ID */
	public final String rID; // result ID
	public final ResultKeyCase keyCase; // map을 사용할때 키 유형
	public final Class<?> javaClass; // 데이터 객체

	/**
	 * 
	 * @param rid           Result ID
	 * @param javaClassName 값을 넣은 자바 클래스 명
	 */
	public DaoResult(String rid, String javaClassName) throws Exception {
		this(rid, javaClassName, ResultKeyCase.upper.name());
	}

	public DaoResult(String rid, String javaClassName, String keyCase) throws Exception {
		this.rID = rid;
		this.keyCase = ResultKeyCase.getKeyCase(keyCase);
		this.javaClass = Class.forName(javaClassName);
	}

	/**
	 * 목록을 추가한다.
	 * 
	 * @param map
	 */
	public void add(List<RetMappVo> map) {
		if (map != null) {
			if (mappingList == null) {
				mappingList = new ArrayList<RetMappVo>();
			}
			for (RetMappVo e : map) {
				if (mappingList.contains(e) == false)
					mappingList.add(e);
			}
		}
	}

	/**
	 * 테이블 컬럼과 자바 클래스 메소드와의 매핑 정보를 추가합니다.
	 * 
	 * @param entry 추가할 내역
	 */
	public void add(RetMappVo... entry) {
		if (mappingList == null) {
			mappingList = new ArrayList<RetMappVo>();
		}

		for (RetMappVo e : entry)
			mappingList.add(e);
	}

	public void addIncludeId(String id) {

		if (includeIdList == null) {
			includeIdList = new ArrayList<String>();
		}

		includeIdList.add(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DaoResult) {
			DaoResult target = (DaoResult) obj;
			try {
				if (rID.equals(target.rID) == false)
					return false;
				if (javaClass != target.javaClass)
					return false;
				if (mappingList != null && target.mappingList != null) {
					if (mappingList.size() != target.mappingList.size())
						return false;
					for (int index = 0, size = mappingList.size(); index < size; index++) {
						if (mappingList.get(index).equals(target.mappingList.get(index)) == false)
							return false;
					}
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 필드 매핑 정보를 넘긴다.
	 * 
	 * @return 필드 매핑 정보
	 */
	public List<RetMappVo> getFields() {
		return mappingList;
	}

	public List<String> getIncludeIdList() {
		return includeIdList;
	}

	/**
	 * 컬럼과 필드간 매핑 정보를 제공합니다.
	 * 
	 * @return 매핑 정보
	 */
	public List<RetMappVo> getMapp() {
		return mappingList;
	}

	/**
	 * ResultBean에 대한 쿼리 XML 내용을 제공합니다.
	 * 
	 * @return 쿼리 XML
	 */
	public String getXml() {

		String ret = "<resultMap id=\"" + rID + "\" javaClass=\"" + javaClass.getName() + "\">\n";

		if (mappingList != null) {
			for (RetMappVo field : mappingList) {
				ret += "\t" + field.getXml();
			}
		}

		ret += "</resultMap>\n";

		return ret;
	}

	public boolean isMap() {
		return Map.class.isAssignableFrom(this.javaClass);
	}

	@Override
	public String toString() {
		String ret = getClass().getSimpleName() + "(" + rID + "," + javaClass + ")";

		if (mappingList != null) {
			ret += "\n\t";
			for (RetMappVo field : mappingList) {
				ret += "(" + field.getColumn() + "-" + field.getJavaField() + "),";
			}
		}

		return ret;
	}

}

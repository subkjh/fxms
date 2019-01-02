package fxms.module.usertree.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.04.04 17:10
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_TREE_ATTR", comment = "사용자트리속성MO테이블")
@FxIndex(name = "FX_UR_TREE_ATTR__PK", type = INDEX_TYPE.PK, columns = { "TREE_NO", "JAVA_FIELD_NAME" })
public class FX_UR_TREE_ATTR {

	@FxColumn(name = "TREE_NO", size = 9, comment = "폴더관리번호")
	private int treeNo;

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호", defValue = "0")
	private int userNo = 0;

	@FxColumn(name = "JAVA_FIELD_NAME", size = 50, nullable = true, comment = "객체자바필드명")
	private String javaFieldName;

	@FxColumn(name = "STRING_METHOD", size = 100, nullable = true, comment = "String 비교 메소드")
	private String stringMethod;

	@FxColumn(name = "STRING_VALUE", size = 100, nullable = true, comment = "String 메소드 파라메터")
	private String stringValue;

	public FX_UR_TREE_ATTR() {
	}

	/**
	 * 객체자바필드명
	 * 
	 * @return 객체자바필드명
	 */
	public String getJavaFieldName() {
		return javaFieldName;
	}

	/**
	 * String 비교 메소드
	 * 
	 * @return String 비교 메소드
	 */
	public String getStringMethod() {
		return stringMethod;
	}

	/**
	 * String 메소드 파라메터
	 * 
	 * @return String 메소드 파라메터
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * 폴더관리번호
	 * 
	 * @return 폴더관리번호
	 */
	public int getTreeNo() {
		return treeNo;
	}

	public int getUserNo() {
		return userNo;
	}

	/**
	 * 객체자바필드명
	 * 
	 * @param javaFieldName
	 *            객체자바필드명
	 */
	public void setJavaFieldName(String javaFieldName) {
		this.javaFieldName = javaFieldName;
	}

	/**
	 * String 비교 메소드
	 * 
	 * @param stringMethod
	 *            String 비교 메소드
	 */
	public void setStringMethod(String stringMethod) {
		this.stringMethod = stringMethod;
	}

	/**
	 * String 메소드 파라메터
	 * 
	 * @param stringValue
	 *            String 메소드 파라메터
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * 폴더관리번호
	 * 
	 * @param treeNo
	 *            폴더관리번호
	 */
	public void setTreeNo(int treeNo) {
		this.treeNo = treeNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

}

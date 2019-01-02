package fxms.module.usertree.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.04.04 17:03
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UR_TREE", comment = "사용자트리테이블")
@FxIndex(name = "FX_UR_TREE__PK", type = INDEX_TYPE.PK, columns = { "TREE_NO" })
public class FX_UR_TREE implements Serializable {

	public static final String FX_SEQ_TREENO = "FX_SEQ_TREENO";

	/**
	 * 
	 */
	private static final long serialVersionUID = -6331959017243027608L;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일자")
	private long chgDate;

	@FxColumn(name = "ETC_MO_TREE_NAME", size = 30, comment = "기타MO트리명")
	private String etcMoTreeName;

	@FxColumn(name = "INLO_TYPE", size = 30, comment = "설치위치종류")
	private String inloType;

	@FxColumn(name = "ORDER_BY", size = 9, nullable = true, comment = "정렬순서", defValue = "0")
	private int orderBy = 0;

	@FxColumn(name = "TARGET_MO_CLASS", size = 20, comment = "대상MO분류 ")
	private String targetMoClass;

	@FxColumn(name = "TREE_NAME", size = 50, comment = "폴더명")
	private String treeName;

	@FxColumn(name = "TREE_DESCR", size = 50, comment = "트리설명")
	private String treeDescr;

	@FxColumn(name = "TREE_NO", size = 9, comment = "폴더관리번호", sequence = "FX_SEQ_TREENO")
	private int treeNo;

	@FxColumn(name = "UPPER_TREE_NO", size = 9, comment = "상위트리번호")
	private int upperTreeNo;

	@FxColumn(name = "USER_NO", size = 9, comment = "운용자번호", defValue = "0")
	private int userNo = 0;

	public FX_UR_TREE() {
	}

	/**
	 * 수정일자
	 * 
	 * @return 수정일자
	 */
	public long getChgDate() {
		return chgDate;
	}

	public String getEtcMoTreeName() {
		return etcMoTreeName;
	}

	public String getInloType() {
		return inloType;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public int getOrderBy() {
		return orderBy;
	}

	/**
	 * 대상MO분류
	 * 
	 * @return 대상MO분류
	 */
	public String getTargetMoClass() {
		return targetMoClass;
	}

	public String getTreeDescr() {
		return treeDescr;
	}

	/**
	 * 폴더명
	 * 
	 * @return 폴더명
	 */
	public String getTreeName() {
		return treeName;
	}

	/**
	 * 폴더관리번호
	 * 
	 * @return 폴더관리번호
	 */
	public int getTreeNo() {
		return treeNo;
	}

	public int getUpperTreeNo() {
		return upperTreeNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public int getUserNo() {
		return userNo;
	}

	/**
	 * 수정일자
	 * 
	 * @param chgDate
	 *            수정일자
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	public void setEtcMoTreeName(String etcMoTreeName) {
		this.etcMoTreeName = etcMoTreeName;
	}

	public void setInloType(String inloType) {
		this.inloType = inloType;
	}

	/**
	 * 정렬순서
	 * 
	 * @param orderBy
	 *            정렬순서
	 */
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 대상MO분류
	 * 
	 * @param targetMoClass
	 *            대상MO분류
	 */
	public void setTargetMoClass(String targetMoClass) {
		this.targetMoClass = targetMoClass;
	}

	public void setTreeDescr(String treeDescr) {
		this.treeDescr = treeDescr;
	}

	/**
	 * 폴더명
	 * 
	 * @param treeName
	 *            폴더명
	 */
	public void setTreeName(String treeName) {
		this.treeName = treeName;
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

	public void setUpperTreeNo(int upperTreeNo) {
		this.upperTreeNo = upperTreeNo;
	}

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

}

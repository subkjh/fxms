package fxms.bas.impl.dbo;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.05.31 09:58
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CD_OP_MENU", comment = "기능메뉴테이블")
@FxIndex(name = "FX_CD_OP_MENU__PK", type = INDEX_TYPE.PK, columns = { "OP_NO", "MENU_OP_NO" })
@FxIndex(name = "FX_CD_OP_MENU__FK", type = INDEX_TYPE.FK, columns = {
		"OP_NO" }, fkTable = "FX_CD_OP", fkColumn = "OP_NO")
public class FX_CD_OP_MENU {

	@FxColumn(name = "OP_NO", size = 9, comment = "기능번호")
	private int opNo;

	@FxColumn(name = "MENU_OP_NO", size = 9, comment = "메뉴기능번호")
	private int menuOpNo;

	@FxColumn(name = "MENU_INDEX", size = 9, comment = "메뉴순서")
	private int menuIndex;

	@FxColumn(name = "MENU_DESCR", size = 100, nullable = true, comment = "메뉴설명")
	private String menuDescr;

	@FxColumn(name = "MENU_EN_ATTR", size = 100, nullable = true, comment = "메뉴활성화조건")
	private String menuEnAttr;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private boolean useYn = true;

	public FX_CD_OP_MENU() {
	}

	/**
	 * 메뉴설명
	 * 
	 * @return 메뉴설명
	 */
	public String getMenuDescr() {
		return menuDescr;
	}

	public String getMenuEnAttr() {
		return menuEnAttr;
	}

	/**
	 * 메뉴순서
	 * 
	 * @return 메뉴순서
	 */
	public int getMenuIndex() {
		return menuIndex;
	}

	/**
	 * 메뉴기능번호
	 * 
	 * @return 메뉴기능번호
	 */
	public int getMenuOpNo() {
		return menuOpNo;
	}

	/**
	 * 기능번호
	 * 
	 * @return 기능번호
	 */
	public int getOpNo() {
		return opNo;
	}

	public boolean isUseYn() {
		return useYn;
	}

	/**
	 * 메뉴설명
	 * 
	 * @param menuDescr
	 *            메뉴설명
	 */
	public void setMenuDescr(String menuDescr) {
		this.menuDescr = menuDescr;
	}

	public void setMenuEnAttr(String menuEnAttr) {
		this.menuEnAttr = menuEnAttr;
	}

	/**
	 * 메뉴순서
	 * 
	 * @param menuIndex
	 *            메뉴순서
	 */
	public void setMenuIndex(int menuIndex) {
		this.menuIndex = menuIndex;
	}

	/**
	 * 메뉴기능번호
	 * 
	 * @param menuOpNo
	 *            메뉴기능번호
	 */
	public void setMenuOpNo(int menuOpNo) {
		this.menuOpNo = menuOpNo;
	}

	/**
	 * 기능번호
	 * 
	 * @param opNo
	 *            기능번호
	 */
	public void setOpNo(int opNo) {
		this.opNo = opNo;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}
}

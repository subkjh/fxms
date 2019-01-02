package fxms.bas.dbo.diag;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.02.27 14:27
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_DI_NODE", comment = "다이아그램NODE테이블")
@FxIndex(name = "FX_DI_NODE__PK", type = INDEX_TYPE.PK, columns = { "DIAG_NO", "DIAG_NODE_NO" })
public class FX_DI_NODE {

	public FX_DI_NODE() {
	}

	@FxColumn(name = "DIAG_NO", size = 9, comment = "챠트명")
	private Integer diagNo;

	@FxColumn(name = "DIAG_NODE_NO", size = 9, comment = "다이아그램노드번호")
	private Integer diagNodeNo;

	@FxColumn(name = "DIAG_NODE_TYPE", size = 10, comment = "다이아그램노드유형")
	private String diagNodeType;

	@FxColumn(name = "DIAG_NODE_X", size = 9, comment = "다이아그램노드X좌표")
	private Integer diagNodeX;

	@FxColumn(name = "DIAG_NODE_Y", size = 9, comment = "다이아그램노드Y좌표")
	private Integer diagNodeY;

	@FxColumn(name = "DIAG_NODE_WIDTH", size = 9, comment = "다이아그램노드넓이")
	private Integer diagNodeWidth;

	@FxColumn(name = "DIAG_NODE_HEIGHT", size = 9, comment = "다이아그램노드높이")
	private Integer diagNodeHeight;

	/**
	 * 챠트명
	 * 
	 * @return 챠트명
	 */
	public Integer getDiagNo() {
		return diagNo;
	}

	/**
	 * 챠트명
	 * 
	 * @param diagNo
	 *            챠트명
	 */
	public void setDiagNo(Integer diagNo) {
		this.diagNo = diagNo;
	}

	/**
	 * 다이아그램노드번호
	 * 
	 * @return 다이아그램노드번호
	 */
	public Integer getDiagNodeNo() {
		return diagNodeNo;
	}

	/**
	 * 다이아그램노드번호
	 * 
	 * @param diagNodeNo
	 *            다이아그램노드번호
	 */
	public void setDiagNodeNo(Integer diagNodeNo) {
		this.diagNodeNo = diagNodeNo;
	}

	/**
	 * 다이아그램노드유형
	 * 
	 * @return 다이아그램노드유형
	 */
	public String getDiagNodeType() {
		return diagNodeType;
	}

	/**
	 * 다이아그램노드유형
	 * 
	 * @param diagNodeType
	 *            다이아그램노드유형
	 */
	public void setDiagNodeType(String diagNodeType) {
		this.diagNodeType = diagNodeType;
	}

	/**
	 * 다이아그램노드X좌표
	 * 
	 * @return 다이아그램노드X좌표
	 */
	public Integer getDiagNodeX() {
		return diagNodeX;
	}

	/**
	 * 다이아그램노드X좌표
	 * 
	 * @param diagNodeX
	 *            다이아그램노드X좌표
	 */
	public void setDiagNodeX(Integer diagNodeX) {
		this.diagNodeX = diagNodeX;
	}

	/**
	 * 다이아그램노드Y좌표
	 * 
	 * @return 다이아그램노드Y좌표
	 */
	public Integer getDiagNodeY() {
		return diagNodeY;
	}

	/**
	 * 다이아그램노드Y좌표
	 * 
	 * @param diagNodeY
	 *            다이아그램노드Y좌표
	 */
	public void setDiagNodeY(Integer diagNodeY) {
		this.diagNodeY = diagNodeY;
	}

	/**
	 * 다이아그램노드넓이
	 * 
	 * @return 다이아그램노드넓이
	 */
	public Integer getDiagNodeWidth() {
		return diagNodeWidth;
	}

	/**
	 * 다이아그램노드넓이
	 * 
	 * @param diagNodeWidth
	 *            다이아그램노드넓이
	 */
	public void setDiagNodeWidth(Integer diagNodeWidth) {
		this.diagNodeWidth = diagNodeWidth;
	}

	/**
	 * 다이아그램노드높이
	 * 
	 * @return 다이아그램노드높이
	 */
	public Integer getDiagNodeHeight() {
		return diagNodeHeight;
	}

	/**
	 * 다이아그램노드높이
	 * 
	 * @param diagNodeHeight
	 *            다이아그램노드높이
	 */
	public void setDiagNodeHeight(Integer diagNodeHeight) {
		this.diagNodeHeight = diagNodeHeight;
	}

}

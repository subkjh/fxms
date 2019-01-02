package fxms.bas.dbo.diag;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.02.27 14:27
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_DI_LINE", comment = "다이아그램라인테이블")
@FxIndex(name = "FX_DI_LINE__PK", type = INDEX_TYPE.PK, columns = { "DIAG_NO", "DIAG_NODE_NO" })
public class FX_DI_LINE implements Serializable {

	public FX_DI_LINE() {
	}

	@FxColumn(name = "DIAG_NO", size = 9, comment = "챠트명")
	private Integer diagNo;

	@FxColumn(name = "DIAG_NODE_NO", size = 9, comment = "다이아그램노드번호")
	private Integer diagNodeNo;

	@FxColumn(name = "LINK_DIAG_NODE_NO1", size = 9, comment = "연결다이아그램노드번호1")
	private Integer linkDiagNodeNo1;

	@FxColumn(name = "LINK_DIAG_NODE_NO2", size = 9, comment = "연결다이아그램노드번호2")
	private Integer linkDiagNodeNo2;

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
	 * 연결다이아그램노드번호1
	 * 
	 * @return 연결다이아그램노드번호1
	 */
	public Integer getLinkDiagNodeNo1() {
		return linkDiagNodeNo1;
	}

	/**
	 * 연결다이아그램노드번호1
	 * 
	 * @param linkDiagNodeNo1
	 *            연결다이아그램노드번호1
	 */
	public void setLinkDiagNodeNo1(Integer linkDiagNodeNo1) {
		this.linkDiagNodeNo1 = linkDiagNodeNo1;
	}

	/**
	 * 연결다이아그램노드번호2
	 * 
	 * @return 연결다이아그램노드번호2
	 */
	public Integer getLinkDiagNodeNo2() {
		return linkDiagNodeNo2;
	}

	/**
	 * 연결다이아그램노드번호2
	 * 
	 * @param linkDiagNodeNo2
	 *            연결다이아그램노드번호2
	 */
	public void setLinkDiagNodeNo2(Integer linkDiagNodeNo2) {
		this.linkDiagNodeNo2 = linkDiagNodeNo2;
	}

}

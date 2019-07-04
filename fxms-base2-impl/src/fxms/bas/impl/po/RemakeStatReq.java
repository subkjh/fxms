package fxms.bas.impl.po;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * 성능을 다시 만들어 줄 것을 요청하는 내용
 * 
 * @author subkjh
 * 
 */
@FxTable(name = "FX_PS_STREQ_CUR", comment = "통계생성요청테이블")
@FxIndex(name = "FX_PS_STAT_REQ__PK", type = INDEX_TYPE.PK, columns = { "MAKE_REQ_NO" })
public class RemakeStatReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6159914238055807437L;

	@FxColumn(name = "MAKE_REQ_NO", size = 19, comment = "생성요청번호")
	private long makeReqNo;

	@FxColumn(name = "PS_TABLE", size = 15, comment = "테이블명")
	private String psTable;

	@FxColumn(name = "PS_TYPE", size = 10, comment = "자료종류")
	private String psType;

	@FxColumn(name = "PS_DATE", size = 14, comment = "생성할 일시")
	private long psDate;

	public RemakeStatReq() {
	}

	public RemakeStatReq(String psTable, String psType, long psDate) {
		this.psTable = psTable;
		this.psType = psType;
		this.psDate = psDate;
	}

	public String getKey() {
		return getPsTable() + "." + getPsType() + "." + getPsDate();
	}

	public long getMakeReqNo() {
		return makeReqNo;
	}

	public long getPsDate() {
		return psDate;
	}

	public String getPsTable() {
		return psTable;
	}

	public String getPsType() {
		return psType;
	}

	public void setMakeReqNo(long makeReqNo) {
		this.makeReqNo = makeReqNo;
	}

	public void setPsDate(long psDate) {
		this.psDate = psDate;
	}

	public void setPsTable(String psTable) {
		this.psTable = psTable;
	}

	public void setPsType(String psType) {
		this.psType = psType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("PS(");
		sb.append("TABLE(" + psTable + ")");
		sb.append("TYPE(" + psType + ")");
		sb.append("DATE(" + psDate + ")");
		sb.append(")");

		return sb.toString();
	}

}

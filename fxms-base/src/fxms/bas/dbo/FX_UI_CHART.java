package fxms.bas.dbo;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.02.19 12:59
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_UI_CHART", comment = "챠트테이블")
@FxIndex(name = "FX_UI_CHART__PK", type = INDEX_TYPE.PK, columns = { "CHART_ID" })
public class FX_UI_CHART {

	@FxColumn(name = "CHART_NAME", size = 50, comment = "챠트명")
	private String chartName;

	@FxColumn(name = "CHART_ID", size = 50, comment = "챠트ID")
	private String chartId;

	@FxColumn(name = "MO_CLASS", size = 20, comment = "MO분류 ")
	private String moClass;

	@FxColumn(name = "MO_TYPE", size = 30, comment = "MO종류")
	private String moType;

	@FxColumn(name = "PS_CODES", size = 105, comment = "상태값번호")
	private String psCodes;

	@FxColumn(name = "PS_TYPE", size = 10, comment = "자료종류")
	private String psType;

	@FxColumn(name = "DATA_RANGE", size = 10, comment = "데이터범위")
	private String dataRange;

	@FxColumn(name = "CHART_JAVA_CLASS", size = 10, comment = "챠트자바클래스")
	private String chartJavaClass;

	@FxColumn(name = "SEQ_BY", size = 9, nullable = true, comment = "정렬순서", defValue = "0")
	private Integer seqBy = 0;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운영자번호", defValue = "0")
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private Long chgDate;

	public FX_UI_CHART() {
	}

	public String getChartId() {
		return chartId;
	}

	/**
	 * 챠트명
	 * 
	 * @return 챠트명
	 */
	public String getChartName() {
		return chartName;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public Long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public Integer getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 데이터범위
	 * 
	 * @return 데이터범위
	 */
	public String getDataRange() {
		return dataRange;
	}

	/**
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * MO종류
	 * 
	 * @return MO종류
	 */
	public String getMoType() {
		return moType;
	}

	/**
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCodes() {
		return psCodes;
	}

	/**
	 * 자료종류
	 * 
	 * @return 자료종류
	 */
	public String getPsType() {
		return psType;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Long getRegDate() {
		return regDate;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public Integer getRegUserNo() {
		return regUserNo;
	}

	public Integer getSeqBy() {
		return seqBy;
	}

	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	/**
	 * 챠트명
	 * 
	 * @param chartName
	 *            챠트명
	 */
	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(Long chgDate) {
		this.chgDate = chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(Integer chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 데이터범위
	 * 
	 * @param dataRange
	 *            데이터범위
	 */
	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}

	/**
	 * MO분류
	 * 
	 * @param moClass
	 *            MO분류
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	/**
	 * MO종류
	 * 
	 * @param moType
	 *            MO종류
	 */
	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 상태값번호
	 * 
	 * @param psCodes
	 *            상태값번호
	 */
	public void setPsCodes(String psCodes) {
		this.psCodes = psCodes;
	}

	/**
	 * 자료종류
	 * 
	 * @param psType
	 *            자료종류
	 */
	public void setPsType(String psType) {
		this.psType = psType;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(Long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(Integer regUserNo) {
		this.regUserNo = regUserNo;
	}

	public void setSeqBy(Integer seqBy) {
		this.seqBy = seqBy;
	}

	public String getChartJavaClass() {
		return chartJavaClass;
	}

	public void setChartJavaClass(String chartJavaClass) {
		this.chartJavaClass = chartJavaClass;
	}

	
}

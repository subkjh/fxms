package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.06.22 10:26
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CO_WEATHER", comment = "공통날씨테이블")
@FxIndex(name = "FX_CO_WEATHER__PK", type = INDEX_TYPE.PK, columns = { "AREA_NUM", "YMD_HH", "FCST_DATA_YN" })
public class FX_CO_WEATHER {

	public FX_CO_WEATHER() {
	}

	@FxColumn(name = "AREA_NUM", size = 10, comment = "지역번호")
	private String areaNum;

	@FxColumn(name = "YMD_HH", size = 10, comment = "년월일시간")
	private Long ymdHh;

	@FxColumn(name = "FCST_DATA_YN", size = 1, comment = "예보데이터여부", defValue = "Y")
	private String fcstDataYn = "Y";

	@FxColumn(name = "TEMP_VAL", size = 8, nullable = true, comment = "온도값")
	private Float tempVal;

	@FxColumn(name = "HUMI_VAL", size = 8, nullable = true, comment = "습도값")
	private Float humiVal;

	@FxColumn(name = "WEAT_CD", size = 3, nullable = true, comment = "날씨코드")
	private String weatCd;

	@FxColumn(name = "PREC_VAL", size = 8, nullable = true, comment = "강수량값")
	private Float precVal;

	@FxColumn(name = "WNDSP_VAL", size = 8, nullable = true, comment = "풍속값")
	private Float wndspVal;

	@FxColumn(name = "DUST_VAL", size = 8, nullable = true, comment = "미세먼지값")
	private Float dustVal;

	@FxColumn(name = "UDUST_VAL", size = 8, nullable = true, comment = "초미세먼지값")
	private Float udustVal;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private Long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;

	/**
	 * @return 지역번호
	 */
	public String getAreaNum() {
		return areaNum;
	}

	/**
	 * @param areaNum 지역번호
	 */
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}

	/**
	 * @return 년월일시간
	 */
	public Long getYmdHh() {
		return ymdHh;
	}

	/**
	 * @param ymdHh 년월일시간
	 */
	public void setYmdHh(Long ymdHh) {
		this.ymdHh = ymdHh;
	}

	/**
	 * @return 예보데이터여부
	 */
	public String isFcstDataYn() {
		return fcstDataYn;
	}

	/**
	 * @param fcstDataYn 예보데이터여부
	 */
	public void setFcstDataYn(String fcstDataYn) {
		this.fcstDataYn = fcstDataYn;
	}

	/**
	 * @return 온도값
	 */
	public Float getTempVal() {
		return tempVal;
	}

	/**
	 * @param tempVal 온도값
	 */
	public void setTempVal(Float tempVal) {
		this.tempVal = tempVal;
	}

	/**
	 * @return 습도값
	 */
	public Float getHumiVal() {
		return humiVal;
	}

	/**
	 * @param humiVal 습도값
	 */
	public void setHumiVal(Float humiVal) {
		this.humiVal = humiVal;
	}

	/**
	 * @return 날씨코드
	 */
	public String getWeatCd() {
		return weatCd;
	}

	/**
	 * @param weatCd 날씨코드
	 */
	public void setWeatCd(String weatCd) {
		this.weatCd = weatCd;
	}

	/**
	 * @return 강수량값
	 */
	public Float getPrecVal() {
		return precVal;
	}

	/**
	 * @param precVal 강수량값
	 */
	public void setPrecVal(Float precVal) {
		this.precVal = precVal;
	}

	/**
	 * @return 풍속값
	 */
	public Float getWndspVal() {
		return wndspVal;
	}

	/**
	 * @param wndspVal 풍속값
	 */
	public void setWndspVal(Float wndspVal) {
		this.wndspVal = wndspVal;
	}

	/**
	 * @return 미세먼지값
	 */
	public Float getDustVal() {
		return dustVal;
	}

	/**
	 * @param dustVal 미세먼지값
	 */
	public void setDustVal(Float dustVal) {
		this.dustVal = dustVal;
	}

	/**
	 * @return 초미세먼지값
	 */
	public Float getUdustVal() {
		return udustVal;
	}

	/**
	 * @param udustVal 초미세먼지값
	 */
	public void setUdustVal(Float udustVal) {
		this.udustVal = udustVal;
	}

	/**
	 * @return 등록사용자번호
	 */
	public Integer getRegUserNo() {
		return regUserNo;
	}

	/**
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(Integer regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * @return 등록일시
	 */
	public Long getRegDtm() {
		return regDtm;
	}

	/**
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(Long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * @return 수정사용자번호
	 */
	public Integer getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(Integer chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * @return 수정일시
	 */
	public Long getChgDtm() {
		return chgDtm;
	}

	/**
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(Long chgDtm) {
		this.chgDtm = chgDtm;
	}
}

package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.12.19 16:09
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CO_DONG", comment = "공통법정동테이블")
@FxIndex(name = "FX_CO_DONG__PK", type = INDEX_TYPE.PK, columns = { "AREA_NUM" })
@FxIndex(name = "FX_CO_DONG__KEY_UPPER", type = INDEX_TYPE.KEY, columns = { "UPPER_AREA_NUM" })
public class FX_CO_DONG {

	public FX_CO_DONG() {
	}

	@FxColumn(name = "AREA_NUM", size = 10, comment = "지역번호")
	private String areaNum;

	@FxColumn(name = "AREA_NAME", size = 20, comment = "지역명")
	private String areaName;

	@FxColumn(name = "AREA_ALL_NAME", size = 100, comment = "지역전체명")
	private String areaAllName;

	@FxColumn(name = "AREA_CL_CD", size = 10, comment = "지역구분코드")
	private String areaClCd;

	@FxColumn(name = "UPPER_AREA_NUM", size = 10, comment = "상위지역번호")
	private String upperAreaNum;

	@FxColumn(name = "USE_YN", size = 1, comment = "사용여부", defValue = "Y")
	private boolean useYn = true;

	@FxColumn(name = "LAT", size = 15, nullable = true, comment = "위도", defValue = "-1")
	private double lat = -1D;

	@FxColumn(name = "LNG", size = 15, nullable = true, comment = "경도", defValue = "-1")
	private double lng = -1D;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록사용자번호", defValue = "0")
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 지역번호
	 * 
	 * @return 지역번호
	 */
	public String getAreaNum() {
		return areaNum;
	}

	/**
	 * 지역번호
	 * 
	 * @param areaNum 지역번호
	 */
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}

	/**
	 * 지역명
	 * 
	 * @return 지역명
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 지역명
	 * 
	 * @param areaName 지역명
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 지역전체명
	 * 
	 * @return 지역전체명
	 */
	public String getAreaAllName() {
		return areaAllName;
	}

	/**
	 * 지역전체명
	 * 
	 * @param areaAllName 지역전체명
	 */
	public void setAreaAllName(String areaAllName) {
		this.areaAllName = areaAllName;
	}

	/**
	 * 지역구분코드
	 * 
	 * @return 지역구분코드
	 */
	public String getAreaClCd() {
		return areaClCd;
	}

	/**
	 * 지역구분코드
	 * 
	 * @param areaClCd 지역구분코드
	 */
	public void setAreaClCd(String areaClCd) {
		this.areaClCd = areaClCd;
	}

	/**
	 * 상위지역번호
	 * 
	 * @return 상위지역번호
	 */
	public String getUpperAreaNum() {
		return upperAreaNum;
	}

	/**
	 * 상위지역번호
	 * 
	 * @param upperAreaNum 상위지역번호
	 */
	public void setUpperAreaNum(String upperAreaNum) {
		this.upperAreaNum = upperAreaNum;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public boolean isUseYn() {
		return useYn;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn 사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	/**
	 * 위도
	 * 
	 * @return 위도
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * 위도
	 * 
	 * @param lat 위도
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * 경도
	 * 
	 * @return 경도
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * 경도
	 * 
	 * @param lng 경도
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(long chgDtm) {
		this.chgDtm = chgDtm;
	}
}

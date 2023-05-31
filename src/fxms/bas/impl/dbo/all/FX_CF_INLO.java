package fxms.bas.impl.dbo.all;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.05.08 17:14
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_CF_INLO", comment = "설치위치테이블")
@FxIndex(name = "FX_CF_INLO__PK", type = INDEX_TYPE.PK, columns = { "INLO_NO" })
@FxIndex(name = "FX_CF_INLO__KEY1", type = INDEX_TYPE.KEY, columns = { "DEL_YN" })
@FxIndex(name = "FX_CF_INLO__KEY2", type = INDEX_TYPE.KEY, columns = { "UPPER_INLO_NO" })
public class FX_CF_INLO {

	public FX_CF_INLO() {
	}

	public static final String FX_SEQ_INLONO = "FX_SEQ_INLONO";
	@FxColumn(name = "INLO_NO", size = 9, comment = "설치위치번호", sequence = "FX_SEQ_INLONO")
	private Integer inloNo;

	@FxColumn(name = "UPPER_INLO_NO", size = 9, comment = "상위설치위치번호", defValue = "0")
	private Integer upperInloNo = 0;

	@FxColumn(name = "OWNER_INLO_NO", size = 9, comment = "소유설치위치번호", defValue = "0")
	private Integer ownerInloNo = 0;

	@FxColumn(name = "INLO_NAME", size = 100, comment = "설치위치명")
	private String inloName;

	@FxColumn(name = "INLO_ALL_NAME", size = 2000, nullable = true, comment = "설치위치전체명")
	private String inloAllName;

	@FxColumn(name = "INLO_DESC", size = 200, nullable = true, comment = "설치위치설명")
	private String inloDesc;

	@FxColumn(name = "INLO_CL_CD", size = 30, comment = "설치위치분류코드", defValue = "NONE")
	private String inloClCd = "NONE";

	@FxColumn(name = "INLO_TYPE_CD", size = 30, comment = "설치위치유형코드", defValue = "NONE")
	private String inloTypeCd = "NONE";

	@FxColumn(name = "INLO_LEVEL_CD", size = 10, comment = "설치위치등급코드", defValue = "NONE")
	private String inloLevelCd = "NONE";

	@FxColumn(name = "LTD", size = 15, nullable = true, comment = "위도", defValue = "-1")
	private Double ltd = -1D;

	@FxColumn(name = "LND", size = 15, nullable = true, comment = "경도", defValue = "-1")
	private Double lnd = -1D;

	@FxColumn(name = "AREA_NUM", size = 10, nullable = true, comment = "지역번호")
	private String areaNum;

	@FxColumn(name = "ZIP_NO", size = 10, nullable = true, comment = "우편번호")
	private String zipNo;

	@FxColumn(name = "ADDR", size = 200, nullable = true, comment = "주소")
	private String addr;

	@FxColumn(name = "CNTCR_NAME", size = 50, nullable = true, comment = "연락자명")
	private String cntcrName;

	@FxColumn(name = "CNTCR_EMAIL", size = 50, nullable = true, comment = "연락자이메일")
	private String cntcrEmail;

	@FxColumn(name = "CNTCT_MEMO", size = 100, nullable = true, comment = "연락자메모")
	private String cntctMemo;

	@FxColumn(name = "TEL_NUM", size = 30, nullable = true, comment = "전화번호")
	private String telNum;

	@FxColumn(name = "FAX_NUM", size = 30, nullable = true, comment = "팩스번호")
	private String faxNum;

	@FxColumn(name = "INLO_TID", size = 50, nullable = true, comment = "설치위치TID")
	private String inloTid;

	@FxColumn(name = "INLO_URL", size = 200, nullable = true, comment = "설치위치URL")
	private String inloUrl;

	@FxColumn(name = "MNG_DIV", size = 20, nullable = true, comment = "관리구분")
	private String mngDiv;

	@FxColumn(name = "ETC1", size = 100, nullable = true, comment = "기타1")
	private String etc1;

	@FxColumn(name = "ETC2", size = 100, nullable = true, comment = "기타2")
	private String etc2;

	@FxColumn(name = "ETC3", size = 100, nullable = true, comment = "기타3")
	private String etc3;

	@FxColumn(name = "ETC4", size = 100, nullable = true, comment = "기타4")
	private String etc4;

	@FxColumn(name = "ETC5", size = 100, nullable = true, comment = "기타5")
	private String etc5;

	@FxColumn(name = "DEL_YN", size = 1, nullable = true, comment = "삭제여부", defValue = "N")
	private String delYn = "N";

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0")
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시")
	private Long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;

	/**
	 * @return 설치위치번호
	 */
	public Integer getInloNo() {
		return inloNo;
	}

	/**
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(Integer inloNo) {
		this.inloNo = inloNo;
	}

	/**
	 * @return 상위설치위치번호
	 */
	public Integer getUpperInloNo() {
		return upperInloNo;
	}

	/**
	 * @param upperInloNo 상위설치위치번호
	 */
	public void setUpperInloNo(Integer upperInloNo) {
		this.upperInloNo = upperInloNo;
	}

	/**
	 * @return 소유설치위치번호
	 */
	public Integer getOwnerInloNo() {
		return ownerInloNo;
	}

	/**
	 * @param ownerInloNo 소유설치위치번호
	 */
	public void setOwnerInloNo(Integer ownerInloNo) {
		this.ownerInloNo = ownerInloNo;
	}

	/**
	 * @return 설치위치명
	 */
	public String getInloName() {
		return inloName;
	}

	/**
	 * @param inloName 설치위치명
	 */
	public void setInloName(String inloName) {
		this.inloName = inloName;
	}

	/**
	 * @return 설치위치전체명
	 */
	public String getInloAllName() {
		return inloAllName;
	}

	/**
	 * @param inloAllName 설치위치전체명
	 */
	public void setInloAllName(String inloAllName) {
		this.inloAllName = inloAllName;
	}

	/**
	 * @return 설치위치설명
	 */
	public String getInloDesc() {
		return inloDesc;
	}

	/**
	 * @param inloDesc 설치위치설명
	 */
	public void setInloDesc(String inloDesc) {
		this.inloDesc = inloDesc;
	}

	/**
	 * @return 설치위치분류코드
	 */
	public String getInloClCd() {
		return inloClCd;
	}

	/**
	 * @param inloClCd 설치위치분류코드
	 */
	public void setInloClCd(String inloClCd) {
		this.inloClCd = inloClCd;
	}

	/**
	 * @return 설치위치유형코드
	 */
	public String getInloTypeCd() {
		return inloTypeCd;
	}

	/**
	 * @param inloTypeCd 설치위치유형코드
	 */
	public void setInloTypeCd(String inloTypeCd) {
		this.inloTypeCd = inloTypeCd;
	}

	/**
	 * @return 설치위치등급코드
	 */
	public String getInloLevelCd() {
		return inloLevelCd;
	}

	/**
	 * @param inloLevelCd 설치위치등급코드
	 */
	public void setInloLevelCd(String inloLevelCd) {
		this.inloLevelCd = inloLevelCd;
	}

	/**
	 * @return 위도
	 */
	public Double getLtd() {
		return ltd;
	}

	/**
	 * @param ltd 위도
	 */
	public void setLtd(Double ltd) {
		this.ltd = ltd;
	}

	/**
	 * @return 경도
	 */
	public Double getLnd() {
		return lnd;
	}

	/**
	 * @param lnd 경도
	 */
	public void setLnd(Double lnd) {
		this.lnd = lnd;
	}

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
	 * @return 우편번호
	 */
	public String getZipNo() {
		return zipNo;
	}

	/**
	 * @param zipNo 우편번호
	 */
	public void setZipNo(String zipNo) {
		this.zipNo = zipNo;
	}

	/**
	 * @return 주소
	 */
	public String getAddr() {
		return addr;
	}

	/**
	 * @param addr 주소
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}

	/**
	 * @return 연락자명
	 */
	public String getCntcrName() {
		return cntcrName;
	}

	/**
	 * @param cntcrName 연락자명
	 */
	public void setCntcrName(String cntcrName) {
		this.cntcrName = cntcrName;
	}

	/**
	 * @return 연락자이메일
	 */
	public String getCntcrEmail() {
		return cntcrEmail;
	}

	/**
	 * @param cntcrEmail 연락자이메일
	 */
	public void setCntcrEmail(String cntcrEmail) {
		this.cntcrEmail = cntcrEmail;
	}

	/**
	 * @return 연락자메모
	 */
	public String getCntctMemo() {
		return cntctMemo;
	}

	/**
	 * @param cntctMemo 연락자메모
	 */
	public void setCntctMemo(String cntctMemo) {
		this.cntctMemo = cntctMemo;
	}

	/**
	 * @return 전화번호
	 */
	public String getTelNum() {
		return telNum;
	}

	/**
	 * @param telNum 전화번호
	 */
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	/**
	 * @return 팩스번호
	 */
	public String getFaxNum() {
		return faxNum;
	}

	/**
	 * @param faxNum 팩스번호
	 */
	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}

	/**
	 * @return 설치위치TID
	 */
	public String getInloTid() {
		return inloTid;
	}

	/**
	 * @param inloTid 설치위치TID
	 */
	public void setInloTid(String inloTid) {
		this.inloTid = inloTid;
	}

	/**
	 * @return 설치위치URL
	 */
	public String getInloUrl() {
		return inloUrl;
	}

	/**
	 * @param inloUrl 설치위치URL
	 */
	public void setInloUrl(String inloUrl) {
		this.inloUrl = inloUrl;
	}

	/**
	 * @return 관리구분
	 */
	public String getMngDiv() {
		return mngDiv;
	}

	/**
	 * @param mngDiv 관리구분
	 */
	public void setMngDiv(String mngDiv) {
		this.mngDiv = mngDiv;
	}

	/**
	 * @return 기타1
	 */
	public String getEtc1() {
		return etc1;
	}

	/**
	 * @param etc1 기타1
	 */
	public void setEtc1(String etc1) {
		this.etc1 = etc1;
	}

	/**
	 * @return 기타2
	 */
	public String getEtc2() {
		return etc2;
	}

	/**
	 * @param etc2 기타2
	 */
	public void setEtc2(String etc2) {
		this.etc2 = etc2;
	}

	/**
	 * @return 기타3
	 */
	public String getEtc3() {
		return etc3;
	}

	/**
	 * @param etc3 기타3
	 */
	public void setEtc3(String etc3) {
		this.etc3 = etc3;
	}

	/**
	 * @return 기타4
	 */
	public String getEtc4() {
		return etc4;
	}

	/**
	 * @param etc4 기타4
	 */
	public void setEtc4(String etc4) {
		this.etc4 = etc4;
	}

	/**
	 * @return 기타5
	 */
	public String getEtc5() {
		return etc5;
	}

	/**
	 * @param etc5 기타5
	 */
	public void setEtc5(String etc5) {
		this.etc5 = etc5;
	}

	/**
	 * @return 삭제여부
	 */
	public String isDelYn() {
		return delYn;
	}

	/**
	 * @param delYn 삭제여부
	 */
	public void setDelYn(String delYn) {
		this.delYn = delYn;
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

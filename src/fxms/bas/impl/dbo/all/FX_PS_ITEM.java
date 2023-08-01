package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.04.12 13:06
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_PS_ITEM", comment = "성능항목테이블")
@FxIndex(name = "FX_PS_ITEM__PK", type = INDEX_TYPE.PK, columns = { "PS_ID" })
@FxIndex(name = "FX_PS_ITEM__KEY1", type = INDEX_TYPE.KEY, columns = { "PS_TBL" })
public class FX_PS_ITEM {

	public FX_PS_ITEM() {
	}

	@FxColumn(name = "PS_ID", size = 50, comment = "성능ID")
	private String psId;

	@FxColumn(name = "PS_NAME", size = 100, comment = "상태값명")
	private String psName;

	@FxColumn(name = "PS_TBL", size = 15, nullable = true, comment = "성능테이블")
	private String psTbl;

	@FxColumn(name = "PS_COL", size = 20, nullable = true, comment = "성능컬럼")
	private String psCol;

	@FxColumn(name = "PS_FMT", size = 10, nullable = true, comment = "성능형식", defValue = "20,2")
	private String psFmt = "20,2";

	@FxColumn(name = "PS_UNIT", size = 10, nullable = true, comment = "성능단위")
	private String psUnit;

	@FxColumn(name = "PS_GRP", size = 30, nullable = true, comment = "성능그룹")
	private String psGrp;

	@FxColumn(name = "CACU_FMLA", size = 100, nullable = true, comment = "계산식")
	private String cacuFmla;

	@FxColumn(name = "PS_DESC", size = 1000, nullable = true, comment = "성능설명")
	private String psDesc;

	@FxColumn(name = "PS_MEMO", size = 1000, nullable = true, comment = "성능메모")
	private String psMemo;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private String useYn = "Y";

	@FxColumn(name = "CNTL_YN", size = 1, nullable = true, comment = "제어여부", defValue = "N")
	private String cntlYn = "N";

	@FxColumn(name = "INSTANCE_EXIST_YN", size = 1, nullable = true, comment = "인스턴스존재여부", defValue = "N")
	private String instanceExistYn = "N";

	@FxColumn(name = "UPD_ADPT", size = 200, nullable = true, comment = "업데이트아답터")
	private String updAdpt;

	@FxColumn(name = "MO_UPD_TBL", size = 50, nullable = true, comment = "MO수정테이블")
	private String moUpdTbl;

	@FxColumn(name = "MO_UPD_COL", size = 50, nullable = true, comment = "MO수정컬럼")
	private String moUpdCol;

	@FxColumn(name = "MO_UPD_DTM_COL", size = 50, nullable = true, comment = "MO수정일시컬럼")
	private String moUpdDtmCol;

	@FxColumn(name = "NULL_VAL", size = 19, nullable = true, comment = "NULL값")
	private Double nullVal;

	@FxColumn(name = "MIN_VAL", size = 19, nullable = true, comment = "최소값")
	private Double minVal;

	@FxColumn(name = "MAX_VAL", size = 19, nullable = true, comment = "최대값")
	private Double maxVal;

	@FxColumn(name = "DFT_VAL", size = 19, nullable = true, comment = "디폴트값")
	private Double dftVal;

	@FxColumn(name = "PS_SCALE", size = 10, nullable = true, comment = "성능비율", defValue = "1")
	private Double psScale = 1D;

	@FxColumn(name = "SORT_SEQ", size = 9, nullable = true, comment = "정렬순서", defValue = "99999")
	private Integer sortSeq = 99999;

	@FxColumn(name = "PS_ID_2ND", size = 50, nullable = true, comment = "성능ID2차")
	private String psId2nd;

	@FxColumn(name = "MO_CLASS", size = 20, nullable = true, comment = "수집MO클래스")
	private String moClass;

	@FxColumn(name = "MO_TYPE", size = 30, nullable = true, comment = "수집MO유형")
	private String moType;

	@FxColumn(name = "PS_VAL_TYPE", size = 2, nullable = true, comment = "성능값유형", defValue = "MV")
	private String psValType = "MV";

	@FxColumn(name = "PS_VAL_CD_JSON", size = 2000, nullable = true, comment = "성능값코드JSON")
	private String psValCdJson;

	@FxColumn(name = "STAT_FUNC_IDS", size = 200, nullable = true, comment = "통계함수ID목록", defValue = "AVG,MIN,MAX,SUM")
	private String statFuncIds = "AVG,MIN,MAX,SUM";

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, comment = "등록사용자번호", defValue = "0")
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, operator = COLUMN_OP.insert, comment = "등록일시")
	private Long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, comment = "수정일시")
	private Long chgDtm;

	/**
	 * 성능ID
	 * 
	 * @return 성능ID
	 */
	public String getPsId() {
		return psId;
	}

	/**
	 * 성능ID
	 * 
	 * @param psId 성능ID
	 */
	public void setPsId(String psId) {
		this.psId = psId;
	}

	/**
	 * 상태값명
	 * 
	 * @return 상태값명
	 */
	public String getPsName() {
		return psName;
	}

	/**
	 * 상태값명
	 * 
	 * @param psName 상태값명
	 */
	public void setPsName(String psName) {
		this.psName = psName;
	}

	/**
	 * 성능테이블
	 * 
	 * @return 성능테이블
	 */
	public String getPsTbl() {
		return psTbl;
	}

	/**
	 * 성능테이블
	 * 
	 * @param psTbl 성능테이블
	 */
	public void setPsTbl(String psTbl) {
		this.psTbl = psTbl;
	}

	/**
	 * 성능컬럼
	 * 
	 * @return 성능컬럼
	 */
	public String getPsCol() {
		return psCol;
	}

	/**
	 * 성능컬럼
	 * 
	 * @param psCol 성능컬럼
	 */
	public void setPsCol(String psCol) {
		this.psCol = psCol;
	}

	/**
	 * 성능형식
	 * 
	 * @return 성능형식
	 */
	public String getPsFmt() {
		return psFmt;
	}

	/**
	 * 성능형식
	 * 
	 * @param psFmt 성능형식
	 */
	public void setPsFmt(String psFmt) {
		this.psFmt = psFmt;
	}

	/**
	 * 성능단위
	 * 
	 * @return 성능단위
	 */
	public String getPsUnit() {
		return psUnit;
	}

	/**
	 * 성능단위
	 * 
	 * @param psUnit 성능단위
	 */
	public void setPsUnit(String psUnit) {
		this.psUnit = psUnit;
	}

	/**
	 * 성능그룹
	 * 
	 * @return 성능그룹
	 */
	public String getPsGrp() {
		return psGrp;
	}

	/**
	 * 성능그룹
	 * 
	 * @param psGrp 성능그룹
	 */
	public void setPsGrp(String psGrp) {
		this.psGrp = psGrp;
	}

	/**
	 * 계산식
	 * 
	 * @return 계산식
	 */
	public String getCacuFmla() {
		return cacuFmla;
	}

	/**
	 * 계산식
	 * 
	 * @param cacuFmla 계산식
	 */
	public void setCacuFmla(String cacuFmla) {
		this.cacuFmla = cacuFmla;
	}

	/**
	 * 성능설명
	 * 
	 * @return 성능설명
	 */
	public String getPsDesc() {
		return psDesc;
	}

	/**
	 * 성능설명
	 * 
	 * @param psDesc 성능설명
	 */
	public void setPsDesc(String psDesc) {
		this.psDesc = psDesc;
	}

	/**
	 * 성능메모
	 * 
	 * @return 성능메모
	 */
	public String getPsMemo() {
		return psMemo;
	}

	/**
	 * 성능메모
	 * 
	 * @param psMemo 성능메모
	 */
	public void setPsMemo(String psMemo) {
		this.psMemo = psMemo;
	}

	/**
	 * 사용여부
	 * 
	 * @return 사용여부
	 */
	public String isUseYn() {
		return useYn;
	}

	/**
	 * 사용여부
	 * 
	 * @param useYn 사용여부
	 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/**
	 * 제어여부
	 * 
	 * @return 제어여부
	 */
	public String isCntlYn() {
		return cntlYn;
	}

	/**
	 * 제어여부
	 * 
	 * @param cntlYn 제어여부
	 */
	public void setCntlYn(String cntlYn) {
		this.cntlYn = cntlYn;
	}

	/**
	 * 인스턴스존재여부
	 * 
	 * @return 인스턴스존재여부
	 */
	public String isInstanceExistYn() {
		return instanceExistYn;
	}

	/**
	 * 인스턴스존재여부
	 * 
	 * @param instanceExistYn 인스턴스존재여부
	 */
	public void setInstanceExistYn(String instanceExistYn) {
		this.instanceExistYn = instanceExistYn;
	}

	/**
	 * 업데이트아답터
	 * 
	 * @return 업데이트아답터
	 */
	public String getUpdAdpt() {
		return updAdpt;
	}

	/**
	 * 업데이트아답터
	 * 
	 * @param updAdpt 업데이트아답터
	 */
	public void setUpdAdpt(String updAdpt) {
		this.updAdpt = updAdpt;
	}

	/**
	 * MO수정테이블
	 * 
	 * @return MO수정테이블
	 */
	public String getMoUpdTbl() {
		return moUpdTbl;
	}

	/**
	 * MO수정테이블
	 * 
	 * @param moUpdTbl MO수정테이블
	 */
	public void setMoUpdTbl(String moUpdTbl) {
		this.moUpdTbl = moUpdTbl;
	}

	/**
	 * MO수정컬럼
	 * 
	 * @return MO수정컬럼
	 */
	public String getMoUpdCol() {
		return moUpdCol;
	}

	/**
	 * MO수정컬럼
	 * 
	 * @param moUpdCol MO수정컬럼
	 */
	public void setMoUpdCol(String moUpdCol) {
		this.moUpdCol = moUpdCol;
	}

	/**
	 * MO수정일시컬럼
	 * 
	 * @return MO수정일시컬럼
	 */
	public String getMoUpdDtmCol() {
		return moUpdDtmCol;
	}

	/**
	 * MO수정일시컬럼
	 * 
	 * @param moUpdDtmCol MO수정일시컬럼
	 */
	public void setMoUpdDtmCol(String moUpdDtmCol) {
		this.moUpdDtmCol = moUpdDtmCol;
	}

	/**
	 * NULL값
	 * 
	 * @return NULL값
	 */
	public Double getNullVal() {
		return nullVal;
	}

	/**
	 * NULL값
	 * 
	 * @param nullVal NULL값
	 */
	public void setNullVal(Double nullVal) {
		this.nullVal = nullVal;
	}

	/**
	 * 최소값
	 * 
	 * @return 최소값
	 */
	public Double getMinVal() {
		return minVal;
	}

	/**
	 * 최소값
	 * 
	 * @param minVal 최소값
	 */
	public void setMinVal(Double minVal) {
		this.minVal = minVal;
	}

	/**
	 * 최대값
	 * 
	 * @return 최대값
	 */
	public Double getMaxVal() {
		return maxVal;
	}

	/**
	 * 최대값
	 * 
	 * @param maxVal 최대값
	 */
	public void setMaxVal(Double maxVal) {
		this.maxVal = maxVal;
	}

	/**
	 * 디폴트값
	 * 
	 * @return 디폴트값
	 */
	public Double getDftVal() {
		return dftVal;
	}

	/**
	 * 디폴트값
	 * 
	 * @param dftVal 디폴트값
	 */
	public void setDftVal(Double dftVal) {
		this.dftVal = dftVal;
	}

	/**
	 * 성능비율
	 * 
	 * @return 성능비율
	 */
	public Double getPsScale() {
		return psScale;
	}

	/**
	 * 성능비율
	 * 
	 * @param psScale 성능비율
	 */
	public void setPsScale(Double psScale) {
		this.psScale = psScale;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public Integer getSortSeq() {
		return sortSeq;
	}

	/**
	 * 정렬순서
	 * 
	 * @param sortSeq 정렬순서
	 */
	public void setSortSeq(Integer sortSeq) {
		this.sortSeq = sortSeq;
	}

	/**
	 * 성능ID2차
	 * 
	 * @return 성능ID2차
	 */
	public String getPsId2nd() {
		return psId2nd;
	}

	/**
	 * 성능ID2차
	 * 
	 * @param psId2nd 성능ID2차
	 */
	public void setPsId2nd(String psId2nd) {
		this.psId2nd = psId2nd;
	}

	/**
	 * 수집MO클래스
	 * 
	 * @return 수집MO클래스
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * 수집MO클래스
	 * 
	 * @param moClass 수집MO클래스
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	/**
	 * 수집MO유형
	 * 
	 * @return 수집MO유형
	 */
	public String getMoType() {
		return moType;
	}

	/**
	 * 수집MO유형
	 * 
	 * @param moType 수집MO유형
	 */
	public void setMoType(String moType) {
		this.moType = moType;
	}

	/**
	 * 성능값유형
	 * 
	 * @return 성능값유형
	 */
	public String getPsValType() {
		return psValType;
	}

	/**
	 * 성능값유형
	 * 
	 * @param psValType 성능값유형
	 */
	public void setPsValType(String psValType) {
		this.psValType = psValType;
	}

	/**
	 * 성능값코드JSON
	 * 
	 * @return 성능값코드JSON
	 */
	public String getPsValCdJson() {
		return psValCdJson;
	}

	/**
	 * 성능값코드JSON
	 * 
	 * @param psValCdJson 성능값코드JSON
	 */
	public void setPsValCdJson(String psValCdJson) {
		this.psValCdJson = psValCdJson;
	}

	/**
	 * 통계함수ID목록
	 * 
	 * @return 통계함수ID목록
	 */
	public String getStatFuncIds() {
		return statFuncIds;
	}

	/**
	 * 통계함수ID목록
	 * 
	 * @param statFuncIds 통계함수ID목록
	 */
	public void setStatFuncIds(String statFuncIds) {
		this.statFuncIds = statFuncIds;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @return 등록사용자번호
	 */
	public Integer getRegUserNo() {
		return regUserNo;
	}

	/**
	 * 등록사용자번호
	 * 
	 * @param regUserNo 등록사용자번호
	 */
	public void setRegUserNo(Integer regUserNo) {
		this.regUserNo = regUserNo;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public Long getRegDtm() {
		return regDtm;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDtm 등록일시
	 */
	public void setRegDtm(Long regDtm) {
		this.regDtm = regDtm;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @return 수정사용자번호
	 */
	public Integer getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 수정사용자번호
	 * 
	 * @param chgUserNo 수정사용자번호
	 */
	public void setChgUserNo(Integer chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public Long getChgDtm() {
		return chgDtm;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDtm 수정일시
	 */
	public void setChgDtm(Long chgDtm) {
		this.chgDtm = chgDtm;
	}
}

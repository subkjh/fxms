package fxms.bas.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.22 15:57
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_PS_ITEM", comment = "성능항목테이블")
@FxIndex(name = "FX_PS_ITEM__PK", type = INDEX_TYPE.PK, columns = { "PS_CODE" })
@FxIndex(name = "FX_PS_ITEM__UK_DB", type = INDEX_TYPE.UK, columns = { "PS_TABLE", "PS_COLUMN" })
public class FX_PS_ITEM implements Serializable {

	public FX_PS_ITEM() {
	}

	@FxColumn(name = "PS_CODE", size = 20, comment = "상태값번호")
	private String psCode;

	@FxColumn(name = "PS_NAME", size = 50, comment = "상태값명")
	private String psName;

	@FxColumn(name = "PS_TABLE", size = 15, nullable = true, comment = "데이터베이스테이블명")
	private String psTable;

	@FxColumn(name = "PS_COLUMN", size = 20, nullable = true, comment = "데이터베이스컬럼명")
	private String psColumn;

	@FxColumn(name = "PS_FORMAT", size = 10, nullable = true, comment = "상태값포맷", defValue = "’20,2’")
	private String psFormat = "20,2";

	@FxColumn(name = "PS_FLAG", size = 20, nullable = true, comment = "성능플래그")
	private String psFlag;

	@FxColumn(name = "PS_UNIT", size = 10, nullable = true, comment = "성능단위")
	private String psUnit;

	@FxColumn(name = "PS_GROUP", size = 30, nullable = true, comment = "성능그룹")
	private String psGroup;

	@FxColumn(name = "COMPUTE_FORMULA", size = 100, nullable = true, comment = "계산식")
	private String computeFormula;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "‘Y’")
	private boolean useYn = true;

	@FxColumn(name = "INSTANCE_YN", size = 1, nullable = true, comment = "인스턴스존재여부", defValue = "‘Y’")
	private boolean instanceYn = true;

	@FxColumn(name = "UPDATE_FILTER", size = 200, nullable = true, comment = "업데이트필터")
	private String updateFilter;

	@FxColumn(name = "READ_FUNC", size = 50, nullable = true, comment = "읽기함수", defValue = "'MAX'")
	private String readFunc = "MAX";

	@FxColumn(name = "MO_TABLE", size = 50, nullable = true, comment = "MO테이블")
	private String moTable;

	@FxColumn(name = "MO_COLUMN", size = 50, nullable = true, comment = "MO컬럼")
	private String moColumn;

	@FxColumn(name = "MO_DATE_COLUMN", size = 50, nullable = true, comment = "MO컬럼일시")
	private String moDateColumn;

	@FxColumn(name = "STAT_FUNCS", size = 1024, nullable = true, comment = "통계함수", defValue = "'MAX'")
	private String statFuncs = "MAX";

	@FxColumn(name = "STAT_DATE", size = 1024, nullable = true, comment = "통계시간")
	private String statDate;

	@FxColumn(name = "VAL_NULL", size = 20, nullable = true, comment = "NULL의미값", defValue = "-1")
	private double valNull = -1;

	@FxColumn(name = "VAL_MAX", size = 100, nullable = true, comment = "최대값")
	private String valMax;
	
	@FxColumn(name = "VAL_MIN", size = 100, nullable = true, comment = "최소값")
	private String valMin;

	@FxColumn(name = "VAL_DEF", size = 20, nullable = true, comment = "화면설정기본값", defValue = "0")
	private double valDef = 0;

	@FxColumn(name = "SEQ_BY", size = 9, nullable = true, comment = "정렬순서", defValue = "99999")
	private int seqBy = 99999;

	@FxColumn(name = "PS_CODE_2ND", size = 20, nullable = true, comment = "2차상태값번호")
	private String psCode2nd;
	
	@FxColumn(name = "MO_CLASS", size = 20, nullable = true, comment = "수집MO분류")
	private String moClass;

	@FxColumn(name = "MO_TYPE", size = 50, nullable = true, comment = "수집MO종류")
	private String moType;
	
	/**
	 * 상태값번호
	 * 
	 * @return 상태값번호
	 */
	public String getPsCode() {
		return psCode;
	}

	/**
	 * 상태값번호
	 * 
	 * @param psCode
	 *            상태값번호
	 */
	public void setPsCode(String psCode) {
		this.psCode = psCode;
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
	 * @param psName
	 *            상태값명
	 */
	public void setPsName(String psName) {
		this.psName = psName;
	}

	/**
	 * 데이터베이스테이블명
	 * 
	 * @return 데이터베이스테이블명
	 */
	public String getPsTable() {
		return psTable;
	}

	/**
	 * 데이터베이스테이블명
	 * 
	 * @param psTable
	 *            데이터베이스테이블명
	 */
	public void setPsTable(String psTable) {
		this.psTable = psTable;
	}

	/**
	 * 데이터베이스컬럼명
	 * 
	 * @return 데이터베이스컬럼명
	 */
	public String getPsColumn() {
		return psColumn;
	}

	/**
	 * 데이터베이스컬럼명
	 * 
	 * @param psColumn
	 *            데이터베이스컬럼명
	 */
	public void setPsColumn(String psColumn) {
		this.psColumn = psColumn;
	}

	/**
	 * 상태값포맷
	 * 
	 * @return 상태값포맷
	 */
	public String getPsFormat() {
		return psFormat;
	}

	/**
	 * 상태값포맷
	 * 
	 * @param psFormat
	 *            상태값포맷
	 */
	public void setPsFormat(String psFormat) {
		this.psFormat = psFormat;
	}

	/**
	 * 성능플래그
	 * 
	 * @return 성능플래그
	 */
	public String getPsFlag() {
		return psFlag;
	}

	/**
	 * 성능플래그
	 * 
	 * @param psFlag
	 *            성능플래그
	 */
	public void setPsFlag(String psFlag) {
		this.psFlag = psFlag;
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
	 * @param psUnit
	 *            성능단위
	 */
	public void setPsUnit(String psUnit) {
		this.psUnit = psUnit;
	}

	/**
	 * 계산식
	 * 
	 * @return 계산식
	 */
	public String getComputeFormula() {
		return computeFormula;
	}

	/**
	 * 계산식
	 * 
	 * @param computeFormula
	 *            계산식
	 */
	public void setComputeFormula(String computeFormula) {
		this.computeFormula = computeFormula;
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
	 * @param useYn
	 *            사용여부
	 */
	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	/**
	 * 인스턴스존재여부
	 * 
	 * @return 인스턴스존재여부
	 */
	public boolean isInstanceYn() {
		return instanceYn;
	}

	/**
	 * 인스턴스존재여부
	 * 
	 * @param instanceYn
	 *            인스턴스존재여부
	 */
	public void setInstanceYn(boolean instanceYn) {
		this.instanceYn = instanceYn;
	}

	/**
	 * 업데이트필터
	 * 
	 * @return 업데이트필터
	 */
	public String getUpdateFilter() {
		return updateFilter;
	}

	/**
	 * 업데이트필터
	 * 
	 * @param updateFilter
	 *            업데이트필터
	 */
	public void setUpdateFilter(String updateFilter) {
		this.updateFilter = updateFilter;
	}

	/**
	 * 읽기함수
	 * 
	 * @return 읽기함수
	 */
	public String getReadFunc() {
		return readFunc;
	}

	/**
	 * 읽기함수
	 * 
	 * @param readFunc
	 *            읽기함수
	 */
	public void setReadFunc(String readFunc) {
		this.readFunc = readFunc;
	}

	/**
	 * MO테이블
	 * 
	 * @return MO테이블
	 */
	public String getMoTable() {
		return moTable;
	}

	/**
	 * MO테이블
	 * 
	 * @param moTable
	 *            MO테이블
	 */
	public void setMoTable(String moTable) {
		this.moTable = moTable;
	}

	/**
	 * MO컬럼
	 * 
	 * @return MO컬럼
	 */
	public String getMoColumn() {
		return moColumn;
	}

	/**
	 * MO컬럼
	 * 
	 * @param moColumn
	 *            MO컬럼
	 */
	public void setMoColumn(String moColumn) {
		this.moColumn = moColumn;
	}

	/**
	 * MO컬럼일시
	 * 
	 * @return MO컬럼일시
	 */
	public String getMoDateColumn() {
		return moDateColumn;
	}

	/**
	 * MO컬럼일시
	 * 
	 * @param moDateColumn
	 *            MO컬럼일시
	 */
	public void setMoDateColumn(String moDateColumn) {
		this.moDateColumn = moDateColumn;
	}

	/**
	 * 통계함수
	 * 
	 * @return 통계함수
	 */
	public String getStatFuncs() {
		return statFuncs;
	}

	/**
	 * 통계함수
	 * 
	 * @param statFuncs
	 *            통계함수
	 */
	public void setStatFuncs(String statFuncs) {
		this.statFuncs = statFuncs;
	}

	/**
	 * 통계시간
	 * 
	 * @return 통계시간
	 */
	public String getStatDate() {
		return statDate;
	}

	/**
	 * 통계시간
	 * 
	 * @param statDate
	 *            통계시간
	 */
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	/**
	 * NULL의미값
	 * 
	 * @return NULL의미값
	 */
	public double getValNull() {
		return valNull;
	}

	/**
	 * NULL의미값
	 * 
	 * @param valNull
	 *            NULL의미값
	 */
	public void setValNull(double valNull) {
		this.valNull = valNull;
	}

	/**
	 * 최대값
	 * 
	 * @return 최대값
	 */
	public String getValMax() {
		return valMax;
	}

	/**
	 * 최대값
	 * 
	 * @param valMax
	 *            최대값
	 */
	public void setValMax(String valMax) {
		this.valMax = valMax;
	}

	/**
	 * 화면설정기본값
	 * 
	 * @return 화면설정기본값
	 */
	public double getValDef() {
		return valDef;
	}

	/**
	 * 화면설정기본값
	 * 
	 * @param valDef
	 *            화면설정기본값
	 */
	public void setValDef(double valDef) {
		this.valDef = valDef;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public int getSeqBy() {
		return seqBy;
	}

	/**
	 * 정렬순서
	 * 
	 * @param seqBy
	 *            정렬순서
	 */
	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

	/**
	 * 2차상태값번호
	 * 
	 * @return 2차상태값번호
	 */
	public String getPsCode2nd() {
		return psCode2nd;
	}

	/**
	 * 2차상태값번호
	 * 
	 * @param psCode2nd
	 *            2차상태값번호
	 */
	public void setPsCode2nd(String psCode2nd) {
		this.psCode2nd = psCode2nd;
	}

	public String getPsGroup() {
		return psGroup;
	}

	public void setPsGroup(String psGroup) {
		this.psGroup = psGroup;
	}

	public String getValMin() {
		return valMin;
	}

	public void setValMin(String valMin) {
		this.valMin = valMin;
	}

	public String getMoClass() {
		return moClass;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	public String getMoType() {
		return moType;
	}

	public void setMoType(String moType) {
		this.moType = moType;
	}
	
	

	
}

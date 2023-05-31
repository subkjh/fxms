package fxms.bas.impl.dbo.all;

import java.io.Serializable;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2023.03.27 14:28
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AL_CFG", comment = "경보임계(설정)테이블")
@FxIndex(name = "FX_AL_CFG__PK", type = INDEX_TYPE.PK, columns = { "ALARM_CFG_NO" })
@FxIndex(name = "FX_AL_CFG__UK", type = INDEX_TYPE.UK, columns = { "ALARM_CFG_NAME" })
public class FX_AL_CFG implements Serializable {

	public FX_AL_CFG() {
	}

	public static final String FX_SEQ_ALARMCFGNO = "FX_SEQ_ALARMCFGNO";
	@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "경보조건번호", sequence = "FX_SEQ_ALARMCFGNO")
	private Integer alarmCfgNo;

	@FxColumn(name = "ALARM_CFG_NAME", size = 50, comment = "경보조건명")
	private String alarmCfgName;

	@FxColumn(name = "ALARM_CFG_DESC", size = 100, nullable = true, comment = "경보조건설명")
	private String alarmCfgDesc;

	@FxColumn(name = "MO_CLASS", size = 20, nullable = true, comment = "MO클래스", defValue = "MO")
	private String moClass = "MO";

	@FxColumn(name = "MO_TYPE", size = 30, nullable = true, comment = "수집MO유형")
	private String moType;

	@FxColumn(name = "BAS_ALARM_CFG_YN", size = 1, nullable = true, comment = "기본경보조건여부", defValue = "N")
	private String basAlarmCfgYn = "N";

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private String useYn = "Y";

	@FxColumn(name = "INLO_NO", size = 9, nullable = true, comment = "설치위치번호", defValue = "0")
	private Integer inloNo = 0;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0", operator=COLUMN_OP.insert)
	private Integer regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시", operator=COLUMN_OP.insert)
	private Long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private Integer chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private Long chgDtm;

	/**
	 * 경보조건번호
	 * 
	 * @return 경보조건번호
	 */
	public Integer getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 경보조건번호
	 * 
	 * @param alarmCfgNo 경보조건번호
	 */
	public void setAlarmCfgNo(Integer alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 경보조건명
	 * 
	 * @return 경보조건명
	 */
	public String getAlarmCfgName() {
		return alarmCfgName;
	}

	/**
	 * 경보조건명
	 * 
	 * @param alarmCfgName 경보조건명
	 */
	public void setAlarmCfgName(String alarmCfgName) {
		this.alarmCfgName = alarmCfgName;
	}

	/**
	 * 경보조건설명
	 * 
	 * @return 경보조건설명
	 */
	public String getAlarmCfgDesc() {
		return alarmCfgDesc;
	}

	/**
	 * 경보조건설명
	 * 
	 * @param alarmCfgDesc 경보조건설명
	 */
	public void setAlarmCfgDesc(String alarmCfgDesc) {
		this.alarmCfgDesc = alarmCfgDesc;
	}

	/**
	 * MO클래스
	 * 
	 * @return MO클래스
	 */
	public String getMoClass() {
		return moClass;
	}

	/**
	 * MO클래스
	 * 
	 * @param moClass MO클래스
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
	 * 기본경보조건여부
	 * 
	 * @return 기본경보조건여부
	 */
	public String isBasAlarmCfgYn() {
		return basAlarmCfgYn;
	}

	/**
	 * 기본경보조건여부
	 * 
	 * @param basAlarmCfgYn 기본경보조건여부
	 */
	public void setBasAlarmCfgYn(String basAlarmCfgYn) {
		this.basAlarmCfgYn = basAlarmCfgYn;
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
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public Integer getInloNo() {
		return inloNo;
	}

	/**
	 * 설치위치번호
	 * 
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(Integer inloNo) {
		this.inloNo = inloNo;
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

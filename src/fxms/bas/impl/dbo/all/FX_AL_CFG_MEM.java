package fxms.bas.impl.dbo.all;

import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * @since 2022.08.26 09:49
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AL_CFG_MEM", comment = "경보임계(설정항목)테이블")
@FxIndex(name = "FX_AL_CFG_MEM__PK", type = INDEX_TYPE.PK, columns = { "ALARM_CFG_NO", "ALCD_NO" })
@FxIndex(name = "FX_AL_CFG_MEM__FK_CFG", type = INDEX_TYPE.FK, columns = {
		"ALARM_CFG_NO" }, fkTable = "FX_AL_CFG", fkColumn = "ALARM_CFG_NO")
@FxIndex(name = "FX_AL_CFG_MEM__FK_ALCD", type = INDEX_TYPE.FK, columns = {
		"ALCD_NO" }, fkTable = "FX_AL_CD", fkColumn = "ALCD_NO")
public class FX_AL_CFG_MEM {

	public FX_AL_CFG_MEM() {
	}

	@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "경보조건번호")
	private int alarmCfgNo;

	@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드")
	private int alcdNo;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "Y")
	private boolean useYn = true;

	@FxColumn(name = "AL_CRI_CMPR_VAL", size = 20, nullable = true, comment = "심각알람비교값")
	private Double alCriCmprVal;

	@FxColumn(name = "AL_MAJ_CMPR_VAL", size = 20, nullable = true, comment = "경고알람비교값")
	private Double alMajCmprVal;

	@FxColumn(name = "AL_MIN_CMPR_VAL", size = 20, nullable = true, comment = "관심알람비교값")
	private Double alMinCmprVal;

	@FxColumn(name = "AL_WAR_CMPR_VAL", size = 20, nullable = true, comment = "관심알람비교값")
	private Double alWarCmprVal;

	@FxColumn(name = "REPT_TIMES", size = 2, nullable = true, comment = "반복횟수", defValue = "1")
	private int reptTimes = 1;

	@FxColumn(name = "PRE_CMPR_PS_ID", size = 20, nullable = true, comment = "선행비교성능ID")
	private String preCmprPsId;

	@FxColumn(name = "PRE_CMPR_CD", size = 2, nullable = true, comment = "선행비교코드")
	private String preCmprCd;

	@FxColumn(name = "PRE_CMPR_VAL", size = 20, nullable = true, comment = "선행비교값")
	private Double preCmprVal;

	@FxColumn(name = "FPACT_CD", size = 10, nullable = true, comment = "후속조치코드")
	private String fpactCd;

	@FxColumn(name = "REG_MEMO", size = 400, nullable = true, comment = "등록메모")
	private String regMemo;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록사용자번호", defValue = "0", operator=COLUMN_OP.insert)
	private int regUserNo = 0;

	@FxColumn(name = "REG_DTM", size = 14, nullable = true, comment = "등록일시", operator=COLUMN_OP.insert)
	private long regDtm;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정사용자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DTM", size = 14, nullable = true, comment = "수정일시")
	private long chgDtm;

	/**
	 * 경보조건번호
	 * 
	 * @return 경보조건번호
	 */
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 경보조건번호
	 * 
	 * @param alarmCfgNo 경보조건번호
	 */
	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 경보코드
	 * 
	 * @return 경보코드
	 */
	public int getAlcdNo() {
		return alcdNo;
	}

	/**
	 * 경보코드
	 * 
	 * @param alcdNo 경보코드
	 */
	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
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
	 * 심각알람비교값
	 * 
	 * @return 심각알람비교값
	 */
	public Double getAlCriCmprVal() {
		return alCriCmprVal;
	}

	/**
	 * 심각알람비교값
	 * 
	 * @param alCriCmprVal 심각알람비교값
	 */
	public void setAlCriCmprVal(Double alCriCmprVal) {
		this.alCriCmprVal = alCriCmprVal;
	}

	/**
	 * 경고알람비교값
	 * 
	 * @return 경고알람비교값
	 */
	public Double getAlMajCmprVal() {
		return alMajCmprVal;
	}

	/**
	 * 경고알람비교값
	 * 
	 * @param alMajCmprVal 경고알람비교값
	 */
	public void setAlMajCmprVal(Double alMajCmprVal) {
		this.alMajCmprVal = alMajCmprVal;
	}

	/**
	 * 관심알람비교값
	 * 
	 * @return 관심알람비교값
	 */
	public Double getAlMinCmprVal() {
		return alMinCmprVal;
	}

	/**
	 * 관심알람비교값
	 * 
	 * @param alMinCmprVal 관심알람비교값
	 */
	public void setAlMinCmprVal(Double alMinCmprVal) {
		this.alMinCmprVal = alMinCmprVal;
	}

	/**
	 * 관심알람비교값
	 * 
	 * @return 관심알람비교값
	 */
	public Double getAlWarCmprVal() {
		return alWarCmprVal;
	}

	/**
	 * 관심알람비교값
	 * 
	 * @param alWarCmprVal 관심알람비교값
	 */
	public void setAlWarCmprVal(Double alWarCmprVal) {
		this.alWarCmprVal = alWarCmprVal;
	}

	/**
	 * 반복횟수
	 * 
	 * @return 반복횟수
	 */
	public int getReptTimes() {
		return reptTimes;
	}

	/**
	 * 반복횟수
	 * 
	 * @param reptTimes 반복횟수
	 */
	public void setReptTimes(int reptTimes) {
		this.reptTimes = reptTimes;
	}

	/**
	 * 선행비교성능ID
	 * 
	 * @return 선행비교성능ID
	 */
	public String getPreCmprPsId() {
		return preCmprPsId;
	}

	/**
	 * 선행비교성능ID
	 * 
	 * @param preCmprPsId 선행비교성능ID
	 */
	public void setPreCmprPsId(String preCmprPsId) {
		this.preCmprPsId = preCmprPsId;
	}

	/**
	 * 선행비교코드
	 * 
	 * @return 선행비교코드
	 */
	public String getPreCmprCd() {
		return preCmprCd;
	}

	/**
	 * 선행비교코드
	 * 
	 * @param preCmprCd 선행비교코드
	 */
	public void setPreCmprCd(String preCmprCd) {
		this.preCmprCd = preCmprCd;
	}

	/**
	 * 선행비교값
	 * 
	 * @return 선행비교값
	 */
	public Double getPreCmprVal() {
		return preCmprVal;
	}

	/**
	 * 선행비교값
	 * 
	 * @param preCmprVal 선행비교값
	 */
	public void setPreCmprVal(Double preCmprVal) {
		this.preCmprVal = preCmprVal;
	}

	/**
	 * 후속조치코드
	 * 
	 * @return 후속조치코드
	 */
	public String getFpactCd() {
		return fpactCd;
	}

	/**
	 * 후속조치코드
	 * 
	 * @param fpactCd 후속조치코드
	 */
	public void setFpactCd(String fpactCd) {
		this.fpactCd = fpactCd;
	}

	/**
	 * 등록메모
	 * 
	 * @return 등록메모
	 */
	public String getRegMemo() {
		return regMemo;
	}

	/**
	 * 등록메모
	 * 
	 * @param regMemo 등록메모
	 */
	public void setRegMemo(String regMemo) {
		this.regMemo = regMemo;
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

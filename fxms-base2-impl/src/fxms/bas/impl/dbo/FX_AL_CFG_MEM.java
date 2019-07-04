package fxms.bas.impl.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.20 09:56
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AL_CFG_MEM", comment = "경보임계(설정항목)테이블")
@FxIndex(name = "FX_AL_CFG_MEM__PK", type = INDEX_TYPE.PK, columns = { "ALARM_CFG_NO", "VERIFIER_VALUE", "ALCD_NO", "ALARM_LEVEL" })
@FxIndex(name = "FX_AL_CFG_MEM__FK", type = INDEX_TYPE.FK, columns = {
		"ALARM_CFG_NO" }, fkTable = "FX_AL_CFG", fkColumn = "ALARM_CFG_NO")
public class FX_AL_CFG_MEM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2763824802153147663L;

	@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "경보조건번호")
	private int alarmCfgNo;

	@FxColumn(name = "VERIFIER_JAVA_CLASS", size = 100, nullable = true, comment = "검증자자바클래스")
	private String verifierJavaClass;

	@FxColumn(name = "VERIFIER_VALUE", size = 10, nullable = true, comment = "검증자값")
	private String verifierValue = "all";

	@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드")
	private int alcdNo;

	@FxColumn(name = "USE_YN", size = 1, nullable = true, comment = "사용여부", defValue = "'Y'")
	private boolean useYn = true;

	@FxColumn(name = "COMPARE_VAL", size = 20, nullable = true, comment = "비교값")
	private double compareVal;
	

	@FxColumn(name = "REPEAT_TIMES", size = 2, nullable = true, comment = "연속일치회수", defValue = "1")
	private int repeatTimes = 1;

	@FxColumn(name = "ALARM_LEVEL", size = 2, comment = "경보등급")
	private int alarmLevel;


	@FxColumn(name = "TREAT_NAME", size = 100, nullable = true, comment = "경보조치코드명")
	private String treatName;

	public FX_AL_CFG_MEM() {
	}

	/**
	 * 경보조건번호
	 * 
	 * @return 경보조건번호
	 */
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 경보등급
	 * 
	 * @return 경보등급
	 */
	public int getAlarmLevel() {
		return alarmLevel;
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
	 * 비교값
	 * 
	 * @return 비교값
	 */
	public double getCompareVal() {
		return compareVal;
	}

	/**
	 * 연속일치회수
	 * 
	 * @return 연속일치회수
	 */
	public int getRepeatTimes() {
		return repeatTimes;
	}

	/**
	 * 경보조치코드명
	 * 
	 * @return 경보조치코드명
	 */
	public String getTreatName() {
		return treatName;
	}

	public String getVerifierJavaClass() {
		return verifierJavaClass;
	}

	public String getVerifierValue() {
		return verifierValue;
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
	 * 경보조건번호
	 * 
	 * @param alarmCfgNo
	 *            경보조건번호
	 */
	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 경보등급
	 * 
	 * @param alarmLevel
	 *            경보등급
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 경보코드
	 * 
	 * @param alcdNo
	 *            경보코드
	 */
	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	/**
	 * 비교값
	 * 
	 * @param compareVal
	 *            비교값
	 */
	public void setCompareVal(double compareVal) {
		this.compareVal = compareVal;
	}

	/**
	 * 연속일치회수
	 * 
	 * @param repeatTimes
	 *            연속일치회수
	 */
	public void setRepeatTimes(int repeatTimes) {
		this.repeatTimes = repeatTimes;
	}

	/**
	 * 경보조치코드명
	 * 
	 * @param treatName
	 *            경보조치코드명
	 */
	public void setTreatName(String treatName) {
		this.treatName = treatName;
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

	public void setVerifierJavaClass(String verifierJavaClass) {
		this.verifierJavaClass = verifierJavaClass;
	}

	public void setVerifierValue(String verifierValue) {
		this.verifierValue = verifierValue;
	}
}

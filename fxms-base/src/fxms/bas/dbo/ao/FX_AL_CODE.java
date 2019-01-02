package fxms.bas.dbo.ao;

import java.io.Serializable;

import subkjh.bas.dao.define.COLUMN_OP;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2017.06.16 15:13
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_AL_CODE", comment = "경보코드(경보)테이블")
@FxIndex(name = "FX_AL_CODE__PK", type = INDEX_TYPE.PK, columns = { "ALCD_NO" })
@FxIndex(name = "FX_AL_CODE__UK_NAME", type = INDEX_TYPE.UK, columns = { "ALCD_NAME" })
public class FX_AL_CODE implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2290684657117754802L;

	@FxColumn(name = "ALCD_NO", size = 9, comment = "경보코드번호")
	private int alcdNo;

	@FxColumn(name = "ALCD_NAME", size = 200, comment = "경보코드명")
	private String alcdName;

	@FxColumn(name = "ALCD_ANAME", size = 200, comment = "경보코드표시명")
	private String alcdAname;

	@FxColumn(name = "ALCD_DESCR", size = 200, nullable = true, comment = "경보설명")
	private String alcdDescr;

	@FxColumn(name = "ALCD_FLAG", size = 10, nullable = true, comment = "경보플래그")
	private String alcdFlag;

	@FxColumn(name = "ALARM_MSG", size = 500, nullable = true, comment = "경보메시지")
	private String alarmMsg;

	@FxColumn(name = "TARGET_MO_CLASS", size = 50, comment = "경보분류")
	private String targetMoClass;

	@FxColumn(name = "AUTO_CLEAR_SEC", size = 9, nullable = true, comment = "자동해제시간(초) ")
	private int autoClearSec;

	@FxColumn(name = "ALARM_LEVEL", size = 1, nullable = true, comment = "기본경보등급")
	private int alarmLevel;

	@FxColumn(name = "COMPARE_CODE", size = 2, nullable = true, comment = "비교조건")
	private String compareCode;

	@FxColumn(name = "PS_CODE", size = 10, nullable = true, comment = "성능상태코드")
	private String psCode;

	@FxColumn(name = "SERVICE_ALARM_YN", size = 1, nullable = true, comment = "서비스경보여부")
	private boolean serviceAlarmYn;

	@FxColumn(name = "TREAT_NAME", size = 100, nullable = true, comment = "경보조치명")
	private String treatName;

	@FxColumn(name = "REG_USER_NO", size = 9, operator = COLUMN_OP.insert, nullable = true, comment = "등록운용자번호")
	private int regUserNo;

	@FxColumn(name = "REG_DATE", size = 14, operator = COLUMN_OP.insert, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시", defValue = "0")
	private long chgDate = 0;

	public FX_AL_CODE() {
	}

	/**
	 * 기본경보등급
	 * 
	 * @return 기본경보등급
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * 경보메시지
	 * 
	 * @return 경보메시지
	 */
	public String getAlarmMsg() {
		return alarmMsg;
	}

	public String getAlcdAname() {
		return alcdAname;
	}

	/**
	 * 경보설명
	 * 
	 * @return 경보설명
	 */
	public String getAlcdDescr() {
		return alcdDescr;
	}

	/**
	 * 경보플래그
	 * 
	 * @return 경보플래그
	 */
	public String getAlcdFlag() {
		return alcdFlag;
	}

	/**
	 * 경보코드명
	 * 
	 * @return 경보코드명
	 */
	public String getAlcdName() {
		return alcdName;
	}

	/**
	 * 경보코드번호
	 * 
	 * @return 경보코드번호
	 */
	public int getAlcdNo() {
		return alcdNo;
	}

	/**
	 * 자동해제시간(초)
	 * 
	 * @return 자동해제시간(초)
	 */
	public int getAutoClearSec() {
		return autoClearSec;
	}

	/**
	 * 수정일시
	 * 
	 * @return 수정일시
	 */
	public long getChgDate() {
		return chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @return 수정운영자번호
	 */
	public int getChgUserNo() {
		return chgUserNo;
	}

	/**
	 * 비교조건
	 * 
	 * @return 비교조건
	 */
	public String getCompareCode() {
		return compareCode;
	}

	/**
	 * 성능상태코드
	 * 
	 * @return 성능상태코드
	 */
	public String getPsCode() {
		return psCode;
	}

	/**
	 * 등록일시
	 * 
	 * @return 등록일시
	 */
	public long getRegDate() {
		return regDate;
	}

	/**
	 * 등록운용자번호
	 * 
	 * @return 등록운용자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	public String getTargetMoClass() {
		return targetMoClass;
	}

	public String getTreatName() {
		return treatName;
	}

	public boolean isServiceAlarmYn() {
		return serviceAlarmYn;
	}

	/**
	 * 기본경보등급
	 * 
	 * @param alarmLevel
	 *            기본경보등급
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 경보메시지
	 * 
	 * @param alarmMsg
	 *            경보메시지
	 */
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public void setAlcdAname(String alcdAname) {
		this.alcdAname = alcdAname;
	}

	/**
	 * 경보설명
	 * 
	 * @param alcdDescr
	 *            경보설명
	 */
	public void setAlcdDescr(String alcdDescr) {
		this.alcdDescr = alcdDescr;
	}

	/**
	 * 경보플래그
	 * 
	 * @param alcdFlag
	 *            경보플래그
	 */
	public void setAlcdFlag(String alcdFlag) {
		this.alcdFlag = alcdFlag;
	}

	/**
	 * 경보코드명
	 * 
	 * @param alcdName
	 *            경보코드명
	 */
	public void setAlcdName(String alcdName) {
		this.alcdName = alcdName;
	}

	/**
	 * 경보코드번호
	 * 
	 * @param alcdNo
	 *            경보코드번호
	 */
	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	/**
	 * 자동해제시간(초)
	 * 
	 * @param autoClearSec
	 *            자동해제시간(초)
	 */
	public void setAutoClearSec(int autoClearSec) {
		this.autoClearSec = autoClearSec;
	}

	/**
	 * 수정일시
	 * 
	 * @param chgDate
	 *            수정일시
	 */
	public void setChgDate(long chgDate) {
		this.chgDate = chgDate;
	}

	/**
	 * 수정운영자번호
	 * 
	 * @param chgUserNo
	 *            수정운영자번호
	 */
	public void setChgUserNo(int chgUserNo) {
		this.chgUserNo = chgUserNo;
	}

	/**
	 * 비교조건
	 * 
	 * @param compareCode
	 *            비교조건
	 */
	public void setCompareCode(String compareCode) {
		this.compareCode = compareCode;
	}

	/**
	 * 성능상태코드
	 * 
	 * @param psCode
	 *            성능상태코드
	 */
	public void setPsCode(String psCode) {
		this.psCode = psCode;
	}

	/**
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 등록운용자번호
	 * 
	 * @param regUserNo
	 *            등록운용자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}

	public void setServiceAlarmYn(boolean serviceAlarmYn) {
		this.serviceAlarmYn = serviceAlarmYn;
	}

	public void setTargetMoClass(String targetMoClass) {
		this.targetMoClass = targetMoClass;
	}

	public void setTreatName(String treatName) {
		this.treatName = treatName;
	}

}

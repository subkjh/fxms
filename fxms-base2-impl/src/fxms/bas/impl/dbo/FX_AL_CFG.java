package fxms.bas.impl.dbo;

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

@FxTable(name = "FX_AL_CFG", comment = "경보임계(설정)테이블")
@FxIndex(name = "FX_ALARM_THR_PK", type = INDEX_TYPE.PK, columns = { "ALARM_CFG_NO" })
@FxIndex(name = "FX_ALARM_THR_NAME__UK", type = INDEX_TYPE.UK, columns = { "ALARM_CFG_NAME" })
public class FX_AL_CFG implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5377369221719250764L;

	public static final String FX_SEQ_ALARMCFGNO = "FX_SEQ_ALARMCFGNO";

	@FxColumn(name = "ALARM_CFG_NO", size = 9, comment = "경보조건번호", sequence = "FX_SEQ_AMARLCFGNO")
	private int alarmCfgNo;

	@FxColumn(name = "ALARM_CFG_NAME", size = 50, nullable = true, comment = "경보조건명")
	private String alarmCfgName;

	@FxColumn(name = "ALARM_CFG_DESCR", size = 100, nullable = true, comment = "경보조건설명")
	private String alarmCfgDescr;

	@FxColumn(name = "MO_CLASS", size = 20, nullable = true, comment = "MO분류")
	private String moClass;

	@FxColumn(name = "BASIC_CFG_YN", size = 1, nullable = true, comment = "MO분류")
	private boolean basicCfgYn = false;

	@FxColumn(name = "REG_USER_NO", size = 9, nullable = true, comment = "등록운영자번호", defValue = "0", operator=COLUMN_OP.insert)
	private int regUserNo = 0;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시", operator=COLUMN_OP.insert)
	private long regDate;

	@FxColumn(name = "CHG_USER_NO", size = 9, nullable = true, comment = "수정운영자번호", defValue = "0")
	private int chgUserNo = 0;

	@FxColumn(name = "CHG_DATE", size = 14, nullable = true, comment = "수정일시")
	private long chgDate;

	public FX_AL_CFG() {
	}

	/**
	 * 경보조건설명
	 * 
	 * @return 경보조건설명
	 */
	public String getAlarmCfgDescr() {
		return alarmCfgDescr;
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
	 * 경보조건번호
	 * 
	 * @return 경보조건번호
	 */
	public int getAlarmCfgNo() {
		return alarmCfgNo;
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
	 * MO분류
	 * 
	 * @return MO분류
	 */
	public String getMoClass() {
		return moClass;
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
	 * 등록운영자번호
	 * 
	 * @return 등록운영자번호
	 */
	public int getRegUserNo() {
		return regUserNo;
	}

	public boolean isBasicCfgYn() {
		return basicCfgYn;
	}

	/**
	 * 경보조건설명
	 * 
	 * @param alarmCfgEscr
	 *            경보조건설명
	 */
	public void setAlarmCfgEscr(String alarmCfgDescr) {
		this.alarmCfgDescr = alarmCfgDescr;
	}

	/**
	 * 경보조건명
	 * 
	 * @param alarmCfgName
	 *            경보조건명
	 */
	public void setAlarmCfgName(String alarmCfgName) {
		this.alarmCfgName = alarmCfgName;
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

	public void setBasicCfgYn(boolean basicCfgYn) {
		this.basicCfgYn = basicCfgYn;
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
	 * MO분류
	 * 
	 * @param moClass
	 *            MO분류
	 */
	public void setMoClass(String moClass) {
		this.moClass = moClass;
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
	 * 등록운영자번호
	 * 
	 * @param regUserNo
	 *            등록운영자번호
	 */
	public void setRegUserNo(int regUserNo) {
		this.regUserNo = regUserNo;
	}
}

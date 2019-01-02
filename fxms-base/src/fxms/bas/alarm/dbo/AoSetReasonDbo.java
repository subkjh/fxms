package fxms.bas.alarm.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_AL_HST", comment = "경보(이력)테이블")
@FxIndex(name = "FX_AL_HST__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO" })
public class AoSetReasonDbo implements Serializable, AlarmTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8228205120091848961L;

	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호", sequence = "FX_SEQ_ALARMNO")
	private long alarmNo;

	@FxColumn(name = "REASON_REG_DATE", size = 14, nullable = true, comment = "경보원인등록일시")
	private long reasonRegDate;

	@FxColumn(name = "REASON_REG_USER_NO", size = 9, nullable = true, comment = "경보원인등록운용자번호")
	private int reasonRegUserNo;

	@FxColumn(name = "REASON_NO", size = 9, nullable = true, comment = "경보원인번호", defValue = "0")
	private int reasonNo = 0;

	@FxColumn(name = "REASON_NAME", size = 100, nullable = true, comment = "경보원인명")
	private String reasonName;

	@FxColumn(name = "REASON_MEMO", size = 200, nullable = true, comment = "경보원인메모")
	private String reasonMemo;

	public AoSetReasonDbo() {
	}

	/**
	 * 경보발생번호
	 * 
	 * @return 경보발생번호
	 */
	public long getAlarmNo() {
		return alarmNo;
	}

	/**
	 * 경보원인메모
	 * 
	 * @return 경보원인메모
	 */
	public String getReasonMemo() {
		return reasonMemo;
	}

	/**
	 * 경보원인명
	 * 
	 * @return 경보원인명
	 */
	public String getReasonName() {
		return reasonName;
	}

	/**
	 * 경보원인번호
	 * 
	 * @return 경보원인번호
	 */
	public int getReasonNo() {
		return reasonNo;
	}

	/**
	 * 경보원인등록일시
	 * 
	 * @return 경보원인등록일시
	 */
	public long getReasonRegDate() {
		return reasonRegDate;
	}

	/**
	 * 경보원인등록운용자번호
	 * 
	 * @return 경보원인등록운용자번호
	 */
	public int getReasonRegUserNo() {
		return reasonRegUserNo;
	}

	/**
	 * 경보발생번호
	 * 
	 * @param alarmNo
	 *            경보발생번호
	 */
	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	/**
	 * 경보원인메모
	 * 
	 * @param reasonMemo
	 *            경보원인메모
	 */
	public void setReasonMemo(String reasonMemo) {
		this.reasonMemo = reasonMemo;
	}

	/**
	 * 경보원인명
	 * 
	 * @param reasonName
	 *            경보원인명
	 */
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	/**
	 * 경보원인번호
	 * 
	 * @param reasonNo
	 *            경보원인번호
	 */
	public void setReasonNo(int reasonNo) {
		this.reasonNo = reasonNo;
	}

	/**
	 * 경보원인등록일시
	 * 
	 * @param reasonRegDate
	 *            경보원인등록일시
	 */
	public void setReasonRegDate(long reasonRegDate) {
		this.reasonRegDate = reasonRegDate;
	}

	/**
	 * 경보원인등록운용자번호
	 * 
	 * @param reasonRegUserNo
	 *            경보원인등록운용자번호
	 */
	public void setReasonRegUserNo(int reasonRegUserNo) {
		this.reasonRegUserNo = reasonRegUserNo;
	}

}
package fxms.nms.dbo;

import java.io.Serializable;

import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.07.03 16:50
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FN_NET_AL_HST", comment = "네트워크경보이력")
@FxIndex(name = "FN_NET_AL_HST__PK", type = INDEX_TYPE.PK, columns = { "ALARM_NO", "NET_NO" })
public class FN_NET_AL_HST implements Serializable {

	public FN_NET_AL_HST() {
	}

	@FxColumn(name = "ALARM_NO", size = 19, comment = "경보발생번호")
	private long alarmNo;

	@FxColumn(name = "NET_NO", size = 9, comment = "네트워크번호")
	private int netNo;

	@FxColumn(name = "NET_NAME", size = 100, comment = "네트워크명")
	private String netName;

	@FxColumn(name = "NE_MO_NO", size = 19, comment = "NE관리번호")
	private long neMoNo;

	@FxColumn(name = "IF_MO_NO", size = 19, nullable = true, comment = "포트관리번호")
	private long ifMoNo;

	@FxColumn(name = "REG_DATE", size = 14, nullable = true, comment = "등록일시")
	private long regDate;

	@FxColumn(name = "CLEAR_DATE", size = 14, nullable = true, comment = "해제일시")
	private long clearDate;

	/**
	 * 경보발생번호
	 * 
	 * @return 경보발생번호
	 */
	public long getAlarmNo() {
		return alarmNo;
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
	 * 네트워크번호
	 * 
	 * @return 네트워크번호
	 */
	public int getNetNo() {
		return netNo;
	}

	/**
	 * 네트워크번호
	 * 
	 * @param netNo
	 *            네트워크번호
	 */
	public void setNetNo(int netNo) {
		this.netNo = netNo;
	}

	/**
	 * 네트워크명
	 * 
	 * @return 네트워크명
	 */
	public String getNetName() {
		return netName;
	}

	/**
	 * 네트워크명
	 * 
	 * @param netName
	 *            네트워크명
	 */
	public void setNetName(String netName) {
		this.netName = netName;
	}

	/**
	 * NE관리번호
	 * 
	 * @return NE관리번호
	 */
	public long getNeMoNo() {
		return neMoNo;
	}

	/**
	 * NE관리번호
	 * 
	 * @param neMoNo
	 *            NE관리번호
	 */
	public void setNeMoNo(long neMoNo) {
		this.neMoNo = neMoNo;
	}

	/**
	 * 포트관리번호
	 * 
	 * @return 포트관리번호
	 */
	public long getIfMoNo() {
		return ifMoNo;
	}

	/**
	 * 포트관리번호
	 * 
	 * @param ifMoNo
	 *            포트관리번호
	 */
	public void setIfMoNo(long ifMoNo) {
		this.ifMoNo = ifMoNo;
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
	 * 등록일시
	 * 
	 * @param regDate
	 *            등록일시
	 */
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}

	/**
	 * 해제일시
	 * 
	 * @return 해제일시
	 */
	public long getClearDate() {
		return clearDate;
	}

	/**
	 * 해제일시
	 * 
	 * @param clearDate
	 *            해제일시
	 */
	public void setClearDate(long clearDate) {
		this.clearDate = clearDate;
	}
}

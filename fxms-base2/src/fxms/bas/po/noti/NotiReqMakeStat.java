package fxms.bas.po.noti;

import fxms.bas.co.noti.FxEventImpl;

/**
 * 통계 생성 요청 노티
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class NotiReqMakeStat extends FxEventImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5422521539319674617L;

	private String psTable;
	private String psType;
	private long psDate;

	public long mstime;

	public NotiReqMakeStat(String psTable, String psType, long psDate) {

		this.setEventType("ps");

		this.psDate = psDate;
		this.psTable = psTable;
		this.psType = psType;
	}

	public NotiReqMakeStat() {
		this.setEventType("ps");
	}

	public long getPsDate() {
		return psDate;
	}

	public String getPsTable() {
		return psTable;
	}

	public String getPsType() {
		return psType;
	}

	public void setPsDate(long psDate) {
		this.psDate = psDate;
	}

	public void setPsTable(String psTable) {
		this.psTable = psTable;
	}

	public void setPsType(String psType) {
		this.psType = psType;
	}

	public String getKey() {
		return psTable + "." + psType + "." + psDate;
	}

}

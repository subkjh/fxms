package fxms.bas.vo;

import fxms.bas.event.FxEventImpl;
import fxms.bas.mo.Moable;

public class Alarm extends FxEventImpl implements Moable, Alarmable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5625836235625053347L;

	private long alarmNo;

	private String alarmKey;

	private int alarmLevel;

	private String alarmMsg;

	private int alcdNo;

	private long occurDtm;

	private long rlseDtm;

	private long moNo;

	private String moName;

	private String moInstance;

	private long upperMoNo = -1L;

	private String upperMoName;

	private int inloNo;

	private int alarmCfgNo;

	private String moClass;

	private String moType;

	public Alarm() {

	}

	@Override
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 알람키
	 *
	 * @return 알람키
	 */
	public String getAlarmKey() {
		return alarmKey;
	}

	/**
	 * 알람등급
	 *
	 * @return 알람등급
	 */
	public int getAlarmLevel() {
		return alarmLevel;
	}

	/**
	 * 알람메세지
	 *
	 * @return 알람메세지
	 */
	public String getAlarmMsg() {
		return alarmMsg;
	}

	/**
	 * 알람번호
	 *
	 * @return 알람번호
	 */
	@Override
	public long getAlarmNo() {
		return alarmNo;
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
	 * 설치위치번호
	 *
	 * @return 설치위치번호
	 */
	@Override
	public int getInloNo() {
		return inloNo;
	}

	@Override
	public String getMoClass() {
		return moClass;
	}

	/**
	 * MO인스턴스
	 *
	 * @return MO인스턴스
	 */
	public String getMoInstance() {
		return moInstance;
	}

	/**
	 * MO명
	 *
	 * @return MO명
	 */
	@Override
	public String getMoName() {
		return moName;
	}

	/**
	 * MO번호
	 *
	 * @return MO번호
	 */
	@Override
	public long getMoNo() {
		return moNo;
	}

	@Override
	public String getMoType() {
		return moType;
	}

	/**
	 * 발생일시
	 *
	 * @return 발생일시
	 */
	public long getOccurDtm() {
		return occurDtm;
	}

	/**
	 * 해제일시
	 *
	 * @return 해제일시
	 */
	public long getRlseDtm() {
		return rlseDtm;
	}

	/**
	 * 상위MO명
	 *
	 * @return 상위MO명
	 */
	public String getUpperMoName() {
		return upperMoName;
	}

	/**
	 * 상위MO번호
	 *
	 * @return 상위MO번호
	 */
	@Override
	public long getUpperMoNo() {
		return upperMoNo;
	}

	public boolean isCleared() {
		return rlseDtm > 0;
	}

	public void setAlarmCfgNo(int alarmCfgNo) {
		this.alarmCfgNo = alarmCfgNo;
	}

	/**
	 * 알람키
	 *
	 * @param alarmKey 알람키
	 */
	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	/**
	 * 알람등급
	 *
	 * @param alarmLevel 알람등급
	 */
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	/**
	 * 알람메세지
	 *
	 * @param alarmMsg 알람메세지
	 */
	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	/**
	 * 알람번호
	 *
	 * @param alarmNo 알람번호
	 */
	@Override
	public void setAlarmNo(long alarmNo) {
		this.alarmNo = alarmNo;
	}

	/**
	 * 경보코드번호
	 *
	 * @param alcdNo 경보코드번호
	 */
	public void setAlcdNo(int alcdNo) {
		this.alcdNo = alcdNo;
	}

	/**
	 * 설치위치번호
	 *
	 * @param inloNo 설치위치번호
	 */
	public void setInloNo(int inloNo) {
		this.inloNo = inloNo;
	}

	public void setMo(Moable mo) {
		this.moNo = mo.getMoNo();
		this.upperMoNo = mo.getUpperMoNo();
		this.moClass = mo.getMoClass();
		this.moType = mo.getMoType();
		this.moName = mo.getMoName();
		this.inloNo = mo.getInloNo();
	}

	/**
	 * MO인스턴스
	 *
	 * @param moInstance MO인스턴스
	 */
	public void setMoInstance(String moInstance) {
		this.moInstance = moInstance;
	}

	/**
	 * MO번호
	 *
	 * @param moNo MO번호
	 */
	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	/**
	 * 발생일시
	 *
	 * @param occurDtm 발생일시
	 */
	public void setOccurDtm(long occurDtm) {
		this.occurDtm = occurDtm;
	}

	/**
	 * 해제일시
	 *
	 * @param rlseDtm 해제일시
	 */
	public void setRlseDtm(long rlseDtm) {
		this.rlseDtm = rlseDtm;
	}

	/**
	 * 상위MO명
	 *
	 * @param upperMoName 상위MO명
	 */
	public void setUpperMoName(String upperMoName) {
		this.upperMoName = upperMoName;
	}

	/**
	 * 상위MO번호
	 *
	 * @param upperMoNo 상위MO번호
	 */
	public void setUpperMoNo(long upperMoNo) {
		this.upperMoNo = upperMoNo;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getNo()).append(")");
		sb.append(getClass().getSimpleName()).append("(");
		sb.append(getAlarmNo());
		sb.append(",").append(getAlarmKey());
		sb.append(",").append(getAlcdNo());
		sb.append(",").append(getAlarmLevel());
		sb.append(",mo=").append(getMoNo());
		sb.append(")");
		sb.append(isCleared() ? " released" : " occured");

		return sb.toString();
	}

}

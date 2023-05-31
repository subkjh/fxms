package fxms.bas.mo;

import fxms.bas.event.FxEvent;

/**
 * 관리대상(MO)을 나타내는지 여부
 * 
 * @author SUBKJH-DEV
 *
 */
public interface Moable extends FxEvent {

	/**
	 * 
	 * @return 관리대상 번호
	 */
	public long getMoNo();

	/**
	 * 
	 * @return 관리대상의 상위관리대상 번호
	 */
	public long getUpperMoNo();

	/**
	 * 알람조건번호
	 * 
	 * @return 알람조건번호
	 */
	public int getAlarmCfgNo();

	/**
	 * 설치위치번호
	 * 
	 * @return 설치위치번호
	 */
	public int getInloNo();

	/**
	 * MO클래스
	 * 
	 * @return MO클래스
	 */
	public String getMoClass();

	/**
	 * MO명
	 * 
	 * @return MO명
	 */
	public String getMoName();
	

	/**
	 * MO유형
	 *
	 * @return MO유형
	 */
	public String getMoType();

}

package fxms.bas.co;

public interface AmHstVo {

	/**
	 * 관리그룹번호
	 * 
	 * @return 관리그룹번호
	 */
	public int getAmGroupNo();

	/**
	 * 관리그룹번호
	 * 
	 * @param amGroupNo
	 *            관리그룹번호
	 */
	public void setAmGroupNo(int amGroupNo);

	/**
	 * 운용자번호
	 * 
	 * @return 운용자번호
	 */
	public long getUserNo();

	/**
	 * 운용자번호
	 * 
	 * @param userNo
	 *            운용자번호
	 */
	public void setUserNo(long userNo);

	/**
	 * 사용자명
	 * 
	 * @return 사용자명
	 */
	public String getAmName();

	/**
	 * 사용자명
	 * 
	 * @param amName
	 *            사용자명
	 */
	public void setAmName(String amName);

	/**
	 * MO번호
	 * 
	 * @return MO번호
	 */
	public long getMoNo();

	/**
	 * MO번호
	 * 
	 * @param moNo
	 *            MO번호
	 */
	public void setMoNo(long moNo);

	/**
	 * 경보발생번호
	 * 
	 * @return 경보발생번호
	 */
	public long getAlarmNo();

	/**
	 * 경보발생번호
	 * 
	 * @param alarmNo
	 *            경보발생번호
	 */
	public void setAlarmNo(long alarmNo);

	/**
	 * 경보조치코드명
	 * 
	 * @return 경보조치코드명
	 */
	public String getTreatName();

	/**
	 * 경보조치코드명
	 * 
	 * @param treatName
	 *            경보조치코드명
	 */
	public void setTreatName(String treatName);

	/**
	 * 경보코드번호
	 * 
	 * @return 경보코드번호
	 */
	public int getAlcdNo();

	/**
	 * 경보코드번호
	 * 
	 * @param alcdNo
	 *            경보코드번호
	 */
	public void setAlcdNo(int alcdNo);

	/**
	 * 해제여부
	 * 
	 * @return 해제여부
	 */
	public boolean isClearYn();

	/**
	 * 해제여부
	 * 
	 * @param clearYn
	 *            해제여부
	 */
	public void setClearYn(boolean clearYn);

	/**
	 * 오류여부
	 * 
	 * @param errYn
	 *            오류여부
	 */
	public void setErrYn(boolean errYn);

	/**
	 * 오류메시지
	 * 
	 * @param errMsg
	 *            오류메시지
	 */
	public void setErrMsg(String errMsg);

}

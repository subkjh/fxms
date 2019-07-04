package fxms.bas.co.vo;

public interface IsSync {

	/**
	 * 동기화일시
	 * 
	 * @return 동기화일시
	 */
	public long getSyncDate();

	/**
	 * 동기화일시
	 *
	 * @param syncDate
	 *            동기화일시
	 */
	public void setSyncDate(long syncDate);

	public int getSyncUserNo();

	public void setSyncUserNo(int userNo);
	
}

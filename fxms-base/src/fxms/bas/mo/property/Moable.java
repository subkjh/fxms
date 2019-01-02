package fxms.bas.mo.property;

/**
 * 관리대상(MO)을 나타내는지 여부
 * 
 * @author SUBKJH-DEV
 *
 */
public interface Moable {

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

}

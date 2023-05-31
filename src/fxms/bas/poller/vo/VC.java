package fxms.bas.poller.vo;

/**
 * Value Convertor
 * 
 * @author subkjh
 *
 */
public abstract class VC {

	/**
	 * 최대값과 최소값을 이용하여 단위를 구합니다.<br>
	 * 
	 * @param max
	 *            최대값
	 * @param min
	 *            최수값
	 * @return 단위
	 */
	public abstract String convertUnit(Number max, Number min);

	/**
	 * 단위를 변경하여 값을 변환합니다.
	 * 
	 * @param value
	 *            변환할 값
	 * @return 변환된 값
	 */
	public abstract Number convert(Number value);

	/**
	 * <pre>
	 * 입력되는 단위변환된 값을 소수 2자리에서 사사오입한 내용
	 * </pre>
	 * 
	 * @param value
	 *            변환할 값
	 * @return 소주 2자리까지의 값
	 */
	public String toString(Number value) {
		// 소수 2자리에서 사사오임
		long n = (long) ((value.doubleValue() + .005D) * 100L);
		return String.format("%.2f", (n / 100D));
	}

	/**
	 * 
	 * <pre>
	 * 원시값을 단위 변환하여 소주점 2자리로 만들어 제공합니다.
	 * </pre>
	 * 
	 * @param value
	 * @return
	 */
	public String toStringConvert(Number value) {

		Number valNew = convert(value);
		// 소수 2자리에서 사사오임
		long n = (long) ((valNew.doubleValue() + .005D) * 100L);
		return String.format("%.2f", (n / 100D));

	}

}

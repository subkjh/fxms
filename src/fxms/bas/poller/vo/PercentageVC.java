package fxms.bas.poller.vo;

/**
 * 
 * <pre>
 * 
 * 프로젝트명 : IX정산데이터 생성시스템 (dfc3.2)
 * 패키지명 : com.daims.dfc.convertor
 * 파일명 : PercentageVC.java
 * 용도 :
 * </pre>
 * 
 * @author subkjh
 * @date 2016. 2. 1.
 * 
 */
public class PercentageVC extends VC {

	public static void main(String[] args) {
		PercentageVC c = new PercentageVC();
		String unit = "";
		// unit = c.convertUnit(1000000000000L, 0);
		System.out.println(c.convert(10.123) + " " + unit);
		System.out.println(c.toStringConvert(293.4789) + " " + unit);
		System.out.println(c.toStringConvert(293.4749) + " " + unit);
		System.out.println(c.toStringConvert(4.61537594) + " " + unit);
	}

	@Override
	public Number convert(Number value) {
		return value;
	}

	@Override
	public String toString(Number value) {
		// 소수 1자리에서 사사오임
		long n = (long) ((value.doubleValue() + .05D) * 10L);
		return String.format("%.1f", (n / 10D));
	}

	@Override
	public String toStringConvert(Number value) {

		Number valNew = convert(value);
		// 소수 2자리에서 사사오임
		long n = (long) ((valNew.doubleValue() + .05D) * 10L);
		return String.format("%.1f", (n / 10D));

	}

	@Override
	public String convertUnit(Number max, Number min) {
		return "";
	}
}

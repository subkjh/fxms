package fxms.bas.poller.vo;
/**
 * 1024 단위의 변환기
 * 
 * @author subkjh
 * 
 */
public class U1024VC extends VC {
	
	public static final int Ki = 1;
	public static final int Mi = 2;
	public static final int Gi = 3;
	public static final int Ti = 4;
	public static final int Pi = 5;

	private static final String UNIT_STRING[] = new String[] { "", "Ki", "Mi", "Gi", "Ti", "Pi" };
	private static final long UNIT[] = new long[] { 1, 1024L, 1024L * 1024L, 1024L * 1024L * 1024L,
			1024L * 1024L * 1024L * 1024L, 1024L * 1024L * 1024L * 1024L * 1024L };

	protected int unitIndex = 0;

	public static void main(String[] args) {
		U1024VC c = new U1024VC();
		String unit = "";
		 unit = c.convertUnit(1000000000000L, 0);
		System.out.println(c.convert(1000000000000L) + " " + unit);
		System.out.println(c.toString(293.4789) + " " + unit);
		System.out.println(c.toString(293.4749) + " " + unit);
		
		System.out.println(U1024VC.vc(1000000000000L));
	}

	@Override
	public String convertUnit(Number max, Number min) {
		double v = max.doubleValue();
		for (int i = 0; i < UNIT.length; i++) {
			if (v / UNIT[i] <= 1024D) {
				unitIndex = i;
				return UNIT_STRING[unitIndex];
			}
		}

		unitIndex = UNIT.length - 1;

		return UNIT_STRING[unitIndex];
	}

	@Override
	public Number convert(Number value) {
		long n = (long) ((value.doubleValue() * 100L) / UNIT[unitIndex]);
		return n / 100D;
	}
	
	public static String vc(Number value)
	{
		U1024VC vc = new U1024VC();
		String unit = vc.convertUnit(value, 0);
		return vc.convert(value) + unit;
	}

}

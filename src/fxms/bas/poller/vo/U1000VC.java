package fxms.bas.poller.vo;

public class U1000VC extends VC {

	private static final String UNIT_STRING[] = new String[] { "", "K", "M", "G", "T", "P" };
	private static final long UNIT[] = new long[] { 1, 1000L, 1000L * 1000L, 1000L * 1000L * 1000L,
			1000L * 1000L * 1000L * 1000L, 1000L * 1000L * 1000L * 1000L * 1000L };

	private int unitIndex = 0;

	public static void main(String[] args) {
		U1000VC c = new U1000VC();
		String unit = "";
		 unit = c.convertUnit(1000000000000L, 0);
		System.out.println(c.convert(1000000000000L) + " " + unit);
		System.out.println(c.toString(293.4789) + " " + unit);
		System.out.println(c.toString(293.4749) + " " + unit);
		System.out.println(c.toStringConvert(1000000000000L) + " " + unit);
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
	
}

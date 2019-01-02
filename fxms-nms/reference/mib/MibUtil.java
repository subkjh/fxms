package com.daims.dfc.mib;

public class MibUtil {

	private static final int in_hundredths_of_a_second = 100;

	/**
	 * 
	 * @param timeTicks
	 * @return 일days 시:분 초
	 */
	public static String getTimeTicks ( long timeTicks ) {
		long n = timeTicks;
		long day, hour, minutes, seconds;

		day = (n / (86400 * in_hundredths_of_a_second));
		n = n % (86400 * in_hundredths_of_a_second);

		hour = (n / (3600 * in_hundredths_of_a_second));
		n = n % (3600 * in_hundredths_of_a_second);

		minutes = (n / (60 * in_hundredths_of_a_second));
		n = n % (60 * in_hundredths_of_a_second);

		seconds = (n / (in_hundredths_of_a_second));

		return (day > 0 ? day + "days " : "") //
				+ hour + ":" + minutes + " " + seconds;
	}
}

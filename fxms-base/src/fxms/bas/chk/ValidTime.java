package fxms.bas.chk;

import java.io.Serializable;
import java.util.Calendar;

public class ValidTime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8509553181422198245L;
	/** 1234567 */
	public static final String DAYS_ALL = "1234567";
	/** 23:59 */
	public static final String HHMM_END = "23:59";
	/** 00:00 */
	public static final String HHMM_START = "00:00";

	public static boolean validDays(String s) {
		if (s == null || s.length() == 0)
			return true;
		for (char ch : s.toCharArray()) {
			if (ch < '1' || ch > '7')
				return false;
		}
		return true;
	}

	/** 수신요일(1:일요일~7:토요일). 23456이면 월~금까지 의미함 */
	private String validDays;

	/** 종료시분. hh:mm */
	private String validHhmmEnd;

	/** 시작시분. hh:mm */
	private String validHhmmStart;

	public ValidTime() {
		validDays = DAYS_ALL;
		validHhmmStart = HHMM_START;
		validHhmmEnd = HHMM_END;
	}

	public ValidTime(String days, String hhmmStart, String hhmmEnd) {
		validDays = days;
		validHhmmStart = (hhmmStart == null || hhmmStart.trim().length() != 5 ? HHMM_START : hhmmStart);
		validHhmmEnd = (hhmmEnd == null || hhmmEnd.trim().length() != 5 ? HHMM_END : hhmmEnd);
	}

	/**
	 * 
	 * @param hstime
	 *            20130222143300 형식의 값으로 합니다.
	 * @return
	 * @throws Exception
	 */
	public boolean onTime(long hstime) {

		try {
			Calendar c = getCalendar(hstime);
			if (containsDay(c) == false)
				return false;

			String hhmm = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

			if (validHhmmStart.compareTo(validHhmmEnd) > 0) {
				return hhmm.compareTo(validHhmmStart) >= 0 || hhmm.compareTo(validHhmmEnd) <= 0;
			} else {
				return hhmm.compareTo(validHhmmStart) >= 0 && hhmm.compareTo(validHhmmEnd) <= 0;
			}
		} catch (Exception e) {
			return false;
		}

	}

	public String getValidDays() {
		return validDays;
	}

	public String getValidHhmmEnd() {
		return validHhmmEnd;
	}

	public String getValidHhmmStart() {
		return validHhmmStart;
	}

	public void setValidDays(String validDays) {
		this.validDays = validDays;
	}

	public void setValidHhmmEnd(String hhmmEnd) {
		if (hhmmEnd == null || hhmmEnd.trim().length() != 5) {
			validHhmmEnd = HHMM_END;
		} else {
			validHhmmEnd = hhmmEnd;
		}
	}

	public void setValidHhmmStart(String hhmmStart) {
		if (hhmmStart == null || hhmmStart.trim().length() != 5) {
			validHhmmStart = HHMM_START;
		} else {
			validHhmmStart = hhmmStart;
		}
	}

	@Override
	public String toString() {
		return validDays + "|" + validHhmmStart + "~" + validHhmmStart;
	}

	private boolean containsDay(Calendar c) {
		int day = c.get(Calendar.DAY_OF_WEEK);
		if (validDays == null)
			return false;
		for (char ch : validDays.toCharArray()) {
			if ((ch - '0') == day)
				return true;
		}
		return false;
	}

	private Calendar getCalendar(long hstime) throws Exception {
		String htime = hstime + "";
		if (htime.trim().length() != 14)
			throw new Exception("Time is invalid [" + htime + "]");

		Calendar c = Calendar.getInstance();

		int year = Integer.parseInt(htime.substring(0, 4));
		int month = Integer.parseInt(htime.substring(4, 6));
		int day = Integer.parseInt(htime.substring(6, 8));

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);

		int hh = Integer.parseInt(htime.substring(8, 10));
		int mm = Integer.parseInt(htime.substring(10, 12));
		int ss = Integer.parseInt(htime.substring(12, 14));
		c.set(Calendar.HOUR_OF_DAY, hh);
		c.set(Calendar.MINUTE, mm);
		c.set(Calendar.SECOND, ss);

		c.set(Calendar.MILLISECOND, 0);

		return c;
	}

}
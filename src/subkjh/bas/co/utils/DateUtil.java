package subkjh.bas.co.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import subkjh.bas.co.lang.Lang;

/**
 * 협정 세계시(UTC)<br>
 * 협정 세계시(UTC)는 1972년 1월 1일부터 시행된 국제 표준시이다. "UTC"는 보통 "Universal Time Code"나
 * "Universal Time Convention"의 약어로 사용되기도 하는데,<br>
 * 이는 틀린 것이라 한다.<br>
 * 실제로 국제 전기 통신 연합은 통일된 약자를 원했지만,<br>
 * 영어권의 사람들과 프랑스어권의 사람들은 각각 자신의 언어로 된 약자를 사용하길 원했다.<br>
 * 영어권은 CUT(Coordinated Universal Time)을, 프랑스어권은 TUC(Temps Universel Coordonne)를
 * 제안했으며,<br>
 * 결국 두 언어 모두 C, T, U로 구성되어 있는 것에 착안해 UTC로 약어를 결정하기로 했다.<br>
 * UTC는 그리니치 평균시(GMT)<br>
 * 
 * 
 * 날짜의 표기<br>
 * ISO 8601에는 날짜의 표기에 그레고리력을 따른다.<br>
 * 연월일 표기법 : YYYYMMDD (기본 형식)<br>
 * 연과 연중 일수 표기법 : YYYYDDD (기본 형식)<br>
 * 연과 주의 주중 일수 표기법: YYYYWwwD (기본 형식)<br>
 * 시간의 표기 : hhmmss (기본 형식)<br>
 * 시간대의 표기 : 시간대를 표기할 때에는 Z또는 +/- 기호를 사용한다.<br>
 * 1981-02-22T09:00Z 또는 19810222T0900Z : UTC 시간대에서의 1981년 2월 22일 오전 9시<br>
 * 
 * @author subkjh
 *
 */
public class DateUtil {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HHmmss");
	private static final SimpleDateFormat HH = new SimpleDateFormat("HH");
	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat YYYYMM = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//	private static final SimpleDateFormat TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public static long addTime(long mstime, int field, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mstime);
		c.add(field, amount);
		return c.getTimeInMillis();
	}

	public static String checkDate(Object date) throws Exception {
		try {
			String s = date.toString();
			if (s.length() == 8) {
				Date d = new SimpleDateFormat("yyyyMMdd").parse(date.toString());
				return YYYYMMDD.format(new Date(d.getTime()));
			}
		} catch (Exception e) {
		}

		throw new Exception(Lang.get("not date type") + "(yyyyMMdd) : " + date);
	}

	/**
	 * 
	 * @return 요일<br>
	 *         Calendar.SUNDAY(1), ... Calendar.SATURDAY(7)
	 */
	public static int getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static synchronized long getDtm() {
		return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
	}

	public static synchronized long getDtm(long mstime) {
		return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(mstime)));
	}

	public static synchronized long getDtm(String mstime) {
		return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(Long.valueOf(mstime))));
	}

	public static synchronized String getDtmStr() {
		return YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
	}

	public static synchronized String getDtmStr(long mstime) {
		return YYYYMMDDHHMMSS.format(new Date(mstime));
	}

	public static synchronized long getHH(long mstime) {
		return Long.parseLong(HH.format(new Date(mstime)));
	}

	public static long getHstime(String hstime) {
		long mstime = toMstime(hstime);
		return toHstime(mstime);
	}

	public static synchronized int getTime() {
		return Integer.parseInt(HHMMSS.format(new Date(System.currentTimeMillis())));
	}

	public static synchronized int getTime(long mstime) {
		return Integer.parseInt(HHMMSS.format(new Date(mstime)));
	}

	public static synchronized int getYmd() {
		return Integer.parseInt(YYYYMMDD.format(new Date(System.currentTimeMillis())));
	}

	public static synchronized int getYmd(long mstime) {
		return Integer.parseInt(YYYYMMDD.format(new Date(mstime)));
	}

	public static synchronized String getYmdStr() {
		return YYYYMMDD.format(new Date(System.currentTimeMillis()));
	}

	public static synchronized String getYmdStr(long mstime) {
		return YYYYMMDD.format(new Date(mstime));
	}

	public static synchronized int getYyyymm(long mstime) {
		return Integer.parseInt(YYYYMM.format(new Date(mstime)));
	}

	public static void main(String[] args) {
		long hstime = DateUtil.getDtm();
		System.out.println(DateUtil.getYmd(DateUtil.addTime(System.currentTimeMillis(), Calendar.MONTH, -1)));

		System.out.println(hstime);
		System.out.println(DateUtil.toMstime(hstime));
		System.out.println(DateUtil.toMstime(hstime + 3));
		System.out.println(DateUtil.getHstime("2023020212"));

		String s = "2022-09-29 14:39:35.832";

		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(s);
			System.out.println(toHstime(date1.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			DateUtil.checkDate("123123");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println(DateUtil.checkDate("20239223"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		long mstime = System.currentTimeMillis() - 86400000L;
		String timezone = DateUtil.toTimeZone(mstime);

		System.out.println(DateUtil.toHstime(mstime));
		System.out.println(timezone);
		System.out.println(mstime);

		try {
			System.out.println(DateUtil.toMstimeTimeZone(timezone));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static synchronized long toHstime(long mstime) {
		return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(mstime)));
	}

	/**
	 * 
	 * @param hstime
	 * @return
	 */
	public static long toMstime(long hstime) {
		return toMstime(String.valueOf(hstime));
	}

	/**
	 * 
	 * @param hstime
	 * @return
	 */
	public static long toMstime(String hstime) {

		if (hstime == null || hstime.length() < 8)
			return 0;

		Calendar c = Calendar.getInstance();

		int year = Integer.parseInt(hstime.substring(0, 4));
		int month = Integer.parseInt(hstime.substring(4, 6));
		int day = Integer.parseInt(hstime.substring(6, 8));

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		if (hstime.length() >= 10) {
			int hh = Integer.parseInt(hstime.substring(8, 10));
			c.set(Calendar.HOUR_OF_DAY, hh);
		}
		if (hstime.length() >= 12) {
			int mm = Integer.parseInt(hstime.substring(10, 12));
			c.set(Calendar.MINUTE, mm);
		}
		if (hstime.length() >= 14) {
			int ss = Integer.parseInt(hstime.substring(12, 14));
			c.set(Calendar.SECOND, ss);
		}

		return c.getTimeInMillis();

	}

	/**
	 * 
	 * @param date '2023-05-29T13:08:42.042Z'
	 * @return
	 * @throws ParseException
	 */
	public static long toMstimeTimeZone(String date) throws ParseException {
		return TIME.parse(date).getTime();
	}

	/**
	 * 
	 * @param mstime
	 * @return '2023-05-29T13:08:42.042Z'
	 */
	public static String toTimeZone(long mstime) {
		return TIME.format(new Date(mstime));
	}
}

package fxms.bas.co.def;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Performent and State Value Statistic Type
 * 
 * @author subkjh
 *
 */
public enum PS_TYPE {
	/**
	 * 일일 데이터
	 */
	DAY1(false, 86400L) {

		@Override
		public String getTag() {
			return "1D";
		}

		@Override
		public long getHstimeEnd(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101240000L;
			return Long.parseLong(s.substring(0, 8) + "240000");
		}

		@Override
		public long getHstimeStart(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101000000L;
			return Long.parseLong(s.substring(0, 8) + "000000");
		}

		@Override
		public long getMstimeGroupStart(long mstime) {

			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public long getMstimeNext(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.DAY_OF_YEAR, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeNextGroup(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.YEAR, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public String getNameGroupByHstime(long hstime) {
			return (hstime + "").substring(0, 4);
		}

	},

	/**
	 * 1시간 데이터
	 */
	HOUR1(true, 3600L) {

		@Override
		public String getTag() {
			return "1H";
		}

		@Override
		public long getHstimeEnd(long hstime) {
			long hstimeNew = getHstimeByMstime(getMstimeNext(getMstimeByHstime(getHstimeStart(hstime)), 1) - 1L);
			return ((hstimeNew / 10000)) * 10000 + 6000;
		}

		@Override
		public long getHstimeStart(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101000000L;
			return Long.parseLong(s.substring(0, 10) + "0000");
		}

		@Override
		public long getMstimeGroupStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public long getMstimeNext(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.HOUR_OF_DAY, amount * 1);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeNextGroup(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.MONTH, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public String getNameGroupByHstime(long hstime) {
			return (hstime + "").substring(0, 6);
		}

	},

	/**
	 * 2시간 데이터
	 */
	HOUR2(true, 7200L) {

		@Override
		public String getTag() {
			return "2H";
		}

		@Override
		public long getHstimeEnd(long hstime) {
			long hstimeNew = getHstimeByMstime(getMstimeNext(getMstimeByHstime(getHstimeStart(hstime)), 1) - 1L);
			return ((hstimeNew / 10000)) * 10000 + 6000;
		}

		@Override
		public long getHstimeStart(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101000000L;
			int hour = Integer.parseInt(s.substring(8, 10));
			if (hour % 2 == 1)
				hour--;
			return Long.parseLong(s.substring(0, 8) + String.format("%02d", hour) + "0000");
		}

		@Override
		public long getMstimeGroupStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public long getMstimeNext(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.HOUR_OF_DAY, amount * 2);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeNextGroup(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.MONTH, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			int hour = c.get(Calendar.HOUR);
			if (hour % 2 == 1) {
				c.set(Calendar.HOUR, hour - 1);
			}
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public String getNameGroupByHstime(long hstime) {
			return (hstime + "").substring(0, 6);
		}

	},

	/**
	 * 5분 데이터
	 */
	MIN1(false, 60L) {

		@Override
		public String getTag() {
			return "1M";
		}

		@Override
		public long getHstimeEnd(long hstime) {
			long hstimeNew = getHstimeByMstime(getMstimeNext(getMstimeByHstime(getHstimeStart(hstime)), 1) - 1L);
			return ((hstimeNew / 100)) * 100 + 60;
		}

		@Override
		public long getHstimeStart(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101000000L;
			return Long.parseLong(s.substring(0, 12) + "00");
		}

		@Override
		public long getMstimeGroupStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public long getMstimeNext(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.MINUTE, amount * 1);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeNextGroup(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.DAY_OF_YEAR, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeStart(long mstime) {
			long stime = mstime / 60000L;
			stime *= 60000L;
			return stime;
		}

		@Override
		public String getNameGroupByHstime(long hstime) {
			return (hstime + "").substring(0, 8);
		}
	},
	
	/**
	 * 5분 데이터
	 */
	RAW(false, 60L) {

		@Override
		public String getTag() {
			return "RAW";
		}

		@Override
		public long getHstimeEnd(long hstime) {
			long hstimeNew = getHstimeByMstime(getMstimeNext(getMstimeByHstime(getHstimeStart(hstime)), 1) - 1L);
			return ((hstimeNew / 100)) * 100 + 60;
		}

		@Override
		public long getHstimeStart(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101000000L;
			return Long.parseLong(s.substring(0, 12) + "00");
		}

		@Override
		public long getMstimeGroupStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public long getMstimeNext(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.MINUTE, amount * 1);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeNextGroup(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.DAY_OF_YEAR, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeStart(long mstime) {
			long stime = mstime / 60000L;
			stime *= 60000L;
			return stime;
		}

		@Override
		public String getNameGroupByHstime(long hstime) {
			return (hstime + "").substring(0, 8);
		}
	},

	/**
	 * 30분 데이터
	 */
	MIN30(true, 1800L) {

		@Override
		public String getTag() {
			return "30M";
		}

		@Override
		public long getHstimeEnd(long hstime) {
			long hstimeNew = getHstimeByMstime(getMstimeNext(getMstimeByHstime(getHstimeStart(hstime)), 1) - 1L);
			return ((hstimeNew / 100)) * 100 + 60;
		}

		@Override
		public long getHstimeStart(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101000000L;
			int min = Integer.parseInt(s.substring(10, 12));
			min = (min < 30 ? 0 : 30);
			return Long.parseLong(s.substring(0, 10) + String.format("%02d", min) + "00");
		}

		@Override
		public long getMstimeGroupStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public long getMstimeNext(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.MINUTE, amount * 30);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeNextGroup(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.WEEK_OF_YEAR, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeStart(long mstime) {
			long stime = mstime / 1800000L;
			stime *= 1800000L;
			return stime;
		}

		@Override
		public String getNameGroupByHstime(long hstime) {
			long mstime = getMstimeByHstime(hstime);
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);

			int dayOfYear = c.get(Calendar.DAY_OF_YEAR);
			int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
			int year = c.get(Calendar.YEAR);

			if (dayOfYear > 300 && weekOfYear == 1) {

				if (PS_TYPE.isSeparateLastWeek) {
					// 1월 1일이 중간에 있을 때 53와 1주로 분리할 때
					// 2008년 12월 29일(월) --> 200853
					// 2009년 1월 1일(목) --> 200901
					c.setTimeInMillis(mstime - 86400000L * 7);
					weekOfYear = c.get(Calendar.WEEK_OF_YEAR) + 1;
				} else {
					// 1월 1일과 같은 주에 있는 12월 일 때 년도 년도 변경
					// 2008년 12월 29일(월) --> 200901
					year++;
				}
			}

			return String.format("%04d%02d", year, weekOfYear);
		}

	},

	/**
	 * 5분 데이터
	 */
	MIN5(false, 300L) {

		@Override
		public String getTag() {
			return "5M";
		}

		@Override
		public long getHstimeEnd(long hstime) {
			long hstimeNew = getHstimeByMstime(getMstimeNext(getMstimeByHstime(getHstimeStart(hstime)), 1) - 1L);
			return ((hstimeNew / 100)) * 100 + 60;
		}

		@Override
		public long getHstimeStart(long hstime) {
			String s = hstime + "";
			if (s.length() != 14)
				return 20000101000000L;

			int min = Integer.parseInt(s.substring(10, 12));
			min = (min / 5) * 5;
			return Long.parseLong(s.substring(0, 10) + String.format("%02d", min) + "00");
		}

		@Override
		public long getMstimeGroupStart(long mstime) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTimeInMillis();
		}

		@Override
		public long getMstimeNext(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.MINUTE, amount * 5);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeNextGroup(long mstime, int amount) {
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mstime);
			c.add(Calendar.DAY_OF_YEAR, amount);
			return getMstimeStart(c.getTimeInMillis());
		}

		@Override
		public long getMstimeStart(long mstime) {
			long stime = mstime / 300000L;
			stime *= 300000L;
			return stime;
		}

		@Override
		public String getNameGroupByHstime(long hstime) {
			return (hstime + "").substring(0, 8);
		}
	};

	/**
	 * 1월 1일이 중간에 있을 때 53와 1주로 분리할 때<br>
	 * 2008년 12월 29일(월) --> 200853<br>
	 * 2009년 1월 1일(목) --> 200901<br>
	 * 2014.01.06 by subkjh : true --> false 변경 : xxx_30M_nnnn53 -->
	 * xxx_3M_(nnnn+1)01로 옮겨야 함.
	 */
	public static boolean isSeparateLastWeek = true;

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static synchronized String getHstime(long mstime) {
		return YMD.format(new Date(mstime));
	}
	public static synchronized String getHstime() {
		return YMD.format(new Date(System.currentTimeMillis()));
	}
	/**
	 * 
	 * @param mstime
	 * @return yyyymmddhhmiss 형태의 일시
	 */
	public static synchronized long getHstimeByMstime(long mstime) {
		return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(mstime)));
	}

	public static PS_TYPE getPsType(String name) {
		for (PS_TYPE mrtgCode : PS_TYPE.values()) {
			if (mrtgCode.name().equalsIgnoreCase(name))
				return mrtgCode;
		}
		
		for (PS_TYPE mrtgCode : PS_TYPE.values()) {
			if (mrtgCode.getTag().equalsIgnoreCase(name))
				return mrtgCode;
		}
		return null;
	}

	public static long getMstimeByHstime(long hstime) {
		return getMstimeByHstime(hstime + "");
	}

	public static long getMstimeByHstime(String hstime) {

		if (hstime == null || hstime.length() < 8)
			return 0;

		Calendar c = Calendar.getInstance();

		int year = Integer.parseInt(hstime.substring(0, 4));
		int month = Integer.parseInt(hstime.substring(4, 6));
		int day = Integer.parseInt(hstime.substring(6, 8));

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);

		if (hstime.length() == 14) {
			int hh = Integer.parseInt(hstime.substring(8, 10));
			int mm = Integer.parseInt(hstime.substring(10, 12));
			int ss = Integer.parseInt(hstime.substring(12, 14));
			c.set(Calendar.HOUR_OF_DAY, hh);
			c.set(Calendar.MINUTE, mm);
			c.set(Calendar.SECOND, ss);
			c.set(Calendar.MILLISECOND, 0);
		} else {
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

		}

		return c.getTimeInMillis();

	}

	public static void main(String[] args) {
		long mstime = PS_TYPE.getMstimeByHstime("20141231");
		long start = DAY1.getMstimeGroupStart(mstime);
		long end = DAY1.getMstimeGroupEnd(mstime);

		System.out.println(PS_TYPE.getHstimeByMstime(start));
		System.out.println(PS_TYPE.getHstimeByMstime(end));

	}

	/** */
	private boolean postTime;

	/** 자료 간격 초 */
	private long secGap;

	private PS_TYPE(boolean postTime, long secGap) {
		this.postTime = postTime;
		this.secGap = secGap;
	}

	/**
	 * 
	 * @param hstime
	 * @return 종료 시간
	 */
	public abstract long getHstimeEnd(long hstime);

	/**
	 * 
	 * @param hstime
	 * @return 다음 시간
	 */
	public long getHstimeNext(long hstime, int amount) {
		return getHstimeByMstime(getMstimeNext(getMstimeByHstime(hstime + ""), amount));
	}

	public long getHstimeNextGroup(long hstime, int amount) {
		return getHstimeByMstime(getMstimeNextGroup(getMstimeByHstime(hstime + ""), amount));
	}

	/**
	 * 한 주기(단위)의 시작 일시
	 * 
	 * @param hstime
	 * @return 시작 시간
	 */
	public abstract long getHstimeStart(long hstime);

	/**
	 * 입력된 시간의 범위내 끝 시간을 구합니다.<br>
	 * 5분 간격인 경우 3분 20초가 입력되면 4분 59초 999을 제공합니다.
	 * 
	 * @param mstime
	 * @return 입력된 시간의 범위내 끝 시간
	 */
	public long getMstimeEnd(long mstime) {
		return getMstimeStart(mstime) + getSecGap() * 1000L - 1L;
	}

	/**
	 * 한 그룹의 종료일시
	 * 
	 * @param mstime
	 * @return 한 그룹의 종료일시
	 */
	public long getMstimeGroupEnd(long mstime) {
		long newTime = getMstimeGroupStart(mstime);
		newTime = getMstimeNextGroup(newTime, 1);
		return newTime - 1L;
	}

	/**
	 * 한 그룹의 시작일시
	 * 
	 * @param mstime
	 * @return 한 그룹의 시작일시
	 */
	public abstract long getMstimeGroupStart(long mstime);

	/**
	 * count 만큼의 내용을 계산된 시간을 넘긴다.
	 * 
	 * @param mstime
	 * @param amount
	 * @return amount 만큼의 내용을 계산된 시간
	 */
	public abstract long getMstimeNext(long mstime, int amount);

	/**
	 * 
	 * @param mstime
	 * @param amount
	 * @return 그룹 단위로 계산된 시간
	 */
	public abstract long getMstimeNextGroup(long mstime, int amount);

	/**
	 * 한 주기(단위)의 시작 일시
	 * 
	 * @param mstime
	 * @return 시작 시간
	 */
	public abstract long getMstimeStart(long mstime);

	/**
	 * 
	 * 
	 * @param hstime
	 * @return 해당 시간이 포함되는 그룹명
	 */
	public abstract String getNameGroupByHstime(long hstime);

	public abstract String getTag();

	/**
	 * 
	 * @param mstime
	 * @return 해당 시간이 포함되는 그룹명
	 */
	public String getNameGroupByMstime(long mstime) {
		long hstime = PS_TYPE.getHstimeByMstime(mstime);
		return getNameGroupByHstime(hstime);
	}

	public long getSecGap() {
		return secGap;
	}

	/**
	 * @return the postTime
	 */
	public boolean isPostTime() {
		return postTime;
	}
	
	public String getTableTag(long psDate) {
		return "_" + getTag() + "_" + getNameGroupByHstime(psDate);
	}
}

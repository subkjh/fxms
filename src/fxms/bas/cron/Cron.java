package fxms.bas.cron;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * 주기적으로 작업할 내용을 정의한다.
 * 
 * @author subkjh
 *
 */
public class Cron implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7553943136033795438L;

	private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat TEST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static final String EVERY_MINUTES = "* * * * *";
	public static final String CYCLE_5_MINUTES = "period 300";
	public static final String CYCLE_1_SECOND = "period 1";
	public static final String CYCLE_10_SECONDS = "period 10";
	public static final String CYCLE_30_SECONDS = "period 30";

	public static String getDate(long mstime) {
		return FMT.format(new Date(mstime));
	}

	public static void main(String[] args) throws Exception {
		Cron cron = new Cron();
		cron.setSchedule("* * * * *");
		System.out.println(cron.getCron());
		System.out.println(cron.toString());
		System.out.println("--------------");

		Cron cron1 = new Cron();
		cron1.setSchedule("2016-04-25 16:37:11");
		System.out.println(cron1.getCron());
		System.out.println(cron1.toString());
		System.out.println("--------------");

		Cron cron2 = new Cron();
		cron2.setSchedule("period 5");
		System.out.println(cron2.getCron());
		System.out.println(cron2.toString());
		System.out.println("--------------");

		Cron cron3 = new Cron();
		cron3.setSchedule(CYCLE_1_SECOND);
		System.out.println(cron3.getCron());
		System.out.println(cron3.toString());
		System.out.println("--------------");

		while (true) {
			long mstime = System.currentTimeMillis();
			if (cron3.isOnTime(mstime)) {
				System.out.println(TEST.format(new Date(mstime)) + " 3");
			}
			Thread.yield();
		}
	}

	/** 마지막 확인 시간 */
	private long lastMstime;
	/** 처리 주기 일 */
	private boolean[] runDays;
	/** size 24, index 0~23까지 의미 있음 */
	private boolean[] runHours;
	/** size 60, index 0~59까지 의미 있음 */
	private boolean[] runMinutes;
	/** size 12, index 0~11까지 의미 있음 */
	private boolean[] runMonths;
	/** 특정 일시에 처리할 때의 시간 */
	private Calendar runOntime;
	/** 실행주기 */
	private long secCycle = -1;
	/** size 7, index 0=sunday, 1=monday, ... 6=saturday */
	private boolean[] runWeeks;

	public Cron() {
	}

	/**
	 * 일정을 넘긴다.
	 *
	 * @return 알아보기 쉬운 일정
	 */
	public String getCron() {

		if (secCycle > 0)
			return "period " + secCycle;

		if (runOntime != null) {
			return FMT.format(runOntime.getTime());
		}

		StringBuffer sb = new StringBuffer();
		sb.append(makeStr(runMinutes, 0));
		sb.append(" ");
		sb.append(makeStr(runHours, 0));
		sb.append(" ");
		sb.append(makeStr(runDays, 0));
		sb.append(" ");
		sb.append(makeStr(runMonths, 0));
		sb.append(" ");
		sb.append(makeStr(runWeeks, 0));

		return sb.toString();
	}

	@Override
	public String toString() {

		if (secCycle > 0)
			return "period " + secCycle;

		if (runOntime != null) {
			return "TIME(" + FMT.format(runOntime.getTime()) + ")";
		}

		StringBuffer sb = new StringBuffer();
		if (lastMstime > 0) {
			sb.append("LAST(" + FMT.format(new Date(lastMstime)) + ")");
		}
		sb.append("MI(" + makeStr(runMinutes, 0) + ")");
		sb.append("HH(" + makeStr(runHours, 0) + ")");
		sb.append("DD(" + makeStr(runDays, 0) + ")");
		sb.append("MM(" + makeStr(runMonths, 0) + ")");
		sb.append("WW(" + makeStr(runWeeks, 0) + ")");

		return sb.toString();
	}

	/**
	 * 입력된 시간이 CRON이 시작할 시간인지 확인하다.
	 * 
	 * @param mstime
	 * @return
	 */
	public boolean isOnTime(long mstime) {

		boolean ret = false;

		if (secCycle > 0) {
			if (lastMstime == 0) {
				lastMstime = mstime / (secCycle * 1000L);
				lastMstime *= (secCycle * 1000L);
			}
			if (mstime >= secCycle * 1000L + lastMstime) {
				lastMstime = mstime / 1000L;
				lastMstime *= 1000L;
				return true;
			}
		} else {

			if (lastMstime == 0) {
				lastMstime = mstime / (60000L);
				lastMstime *= 60000L;
			}

			if (mstime < lastMstime + 60000L)
				return false;

			Calendar nowTime = Calendar.getInstance();
			nowTime.setTimeInMillis(mstime);

			if (runOntime != null) {

				ret = runOntime.get(Calendar.YEAR) == nowTime.get(Calendar.YEAR) //
						&& runOntime.get(Calendar.MONTH) == nowTime.get(Calendar.MONTH) //
						&& runOntime.get(Calendar.DAY_OF_MONTH) == nowTime.get(Calendar.DAY_OF_MONTH) //
						&& runOntime.get(Calendar.HOUR_OF_DAY) == nowTime.get(Calendar.HOUR_OF_DAY) //
						&& runOntime.get(Calendar.MINUTE) == nowTime.get(Calendar.MINUTE); //

			} else {

				ret = runMinutes[nowTime.get(Calendar.MINUTE)] //
						&& runHours[nowTime.get(Calendar.HOUR_OF_DAY)] //
						&& runDays[nowTime.get(Calendar.DAY_OF_MONTH) - 1] //
						&& runMonths[nowTime.get(Calendar.MONTH)] //
						&& runWeeks[nowTime.get(Calendar.DAY_OF_WEEK) - 1];
			}

			if (ret) {
				lastMstime = mstime / 60000L;
				lastMstime *= 60000L;
			}

		}
		return ret;
	}

	/**
	 * 분 시 일 월 주 [초]<br>
	 * 또는 yyyy-mm-dd hh:mi[:ss] <br>
	 * 또는 period 분<br>
	 *
	 * 초 : 0 ~ 59<br>
	 * 분 : 0 ~ 59<br>
	 * 시 : 0 ~ 23<br>
	 * 일 : 1 ~ 31<br>
	 * 월 : 1 ~ 12<br>
	 * 주 : 0 ~ 6 ( 0=일요일, 1=월요일 )<br>
	 *
	 *
	 * @param line
	 */
	public void setSchedule(String schedule) throws Exception {

		try {
			if (schedule.indexOf("period") >= 0) {
				secCycle = Integer.parseInt(schedule.replaceAll("period", "").trim());
			} else if (schedule.lastIndexOf(":") > 0) {
				if (schedule.length() == 16)
					schedule += ":00";

				runOntime = Calendar.getInstance();
				try {
					runOntime.setTime(FMT.parse(schedule));
				} catch (Exception e) {
					runOntime = null;
					throw e;
				}
			} else {

				runOntime = null;

				String ss[] = schedule.split(" ");
				if (ss.length < 5 || ss.length > 6)
					throw new Exception("WORD IS NOT 6");

				runMinutes = new boolean[60];
				runHours = new boolean[24];
				runDays = new boolean[31];
				runMonths = new boolean[12];
				runWeeks = new boolean[7];

				Arrays.fill(runMinutes, false);
				Arrays.fill(runHours, false);
				Arrays.fill(runDays, false);
				Arrays.fill(runMonths, false);
				Arrays.fill(runWeeks, false);

				parse(runMinutes, ss[0], 0);
				parse(runHours, ss[1], 0);
				parse(runDays, ss[2], 1);
				parse(runMonths, ss[3], 1);
				parse(runWeeks, ss[4], 0);

			}
		} catch (Exception e) {
			throw new Exception("CRON(" + schedule + ") " + e.getMessage());
		}

	}

	private boolean isAll(boolean tag[]) {
		for (boolean element : tag)
			if (!element)
				return false;
		return true;
	}

	private String makeStr(boolean val[], int startIndex) {

		if ((val == null) || isAll(val))
			return "*";

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < val.length; i++) {
			if (val[i]) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(startIndex + i);
			}
		}

		return sb.toString();
	}

	private void parse(boolean[] period, String format, int first_value) throws Exception {
		if (format.equals("*")) {
			Arrays.fill(period, true);
		} else {
			String ss[] = format.split(",");
			for (String s : ss) {
				period[Integer.parseInt(s) - first_value] = true;
			}
		}
	}

}

package fxms.bas.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import fxms.bas.exp.DateRangeOverException;
import fxms.bas.fxo.FxmsUtil;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.utils.DateUtil;

/**
 * 성능 통계 종류를 나타낸다.
 * 
 * @author subkjh
 *
 */
public class PsKind {
	
	public static final String PSKIND_RAW = "RAW";
	public static final String PSKIND_5M = "5M";
	public static final String PSKIND_15M = "15M";
	public static final String PSKIND_1H = "1H";
	public static final String PSKIND_1D = "1D";


	public enum TBL_PART_UNIT_CD {
		Daily("D", Calendar.DAY_OF_YEAR), Monthly("M", Calendar.MONTH), None("N", Calendar.DAY_OF_YEAR),
		Weekly("W", Calendar.WEEK_OF_YEAR), Yearly("Y", Calendar.YEAR);

		public static TBL_PART_UNIT_CD get(String cd) {
			for (TBL_PART_UNIT_CD e : TBL_PART_UNIT_CD.values()) {
				if (e.name().equalsIgnoreCase(cd)) {
					return e;
				}
			}

			for (TBL_PART_UNIT_CD e : TBL_PART_UNIT_CD.values()) {
				if (e.cd.equalsIgnoreCase(cd)) {
					return e;
				}
			}
			return None;
		}

		private int calendarType;

		private String cd;

		private TBL_PART_UNIT_CD(String cd, int calendarType) {
			this.cd = cd;
			this.calendarType = calendarType;
		}

		public int getCalendarType() {
			return calendarType;
		}

	}

	class Range {
		String unit;
		int value = -1;

		Range(String s) {
			try {
				char ch;
				StringBuffer num = new StringBuffer();
				for (int i = 0; i < s.length(); i++) {
					ch = s.charAt(i);
					if (ch >= '0' && ch <= '9') {
						num.append(ch);
					} else {
						this.unit = s.substring(i).trim().toLowerCase();
						this.value = Integer.parseInt(num.toString());
						return;
					}
				}

			} catch (Exception e) {

			}
		}
	}

	private final static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	public static void main(String[] args) {
		PsKind psKind = new PsKind("1", "2", 3, "15 Minutes");
		System.out.println(FxmsUtil.toJson(psKind));
	}

	private final long DAY = 24 * 60 * 60 * 1000L;
	private final long HOUR = 1 * 60 * 60 * 1000L;
	
	private final int calendarField;
	private final int calendarFieldValue;
	private final String dataRange; // 데이터범위
	private final int dataStoreDays;
	private final String interval; // 주기
	private final long intervalSeconds; // 주기(초)
	private String psDataSrc; // 입력데이터종류
	private final String psDataTag; // 성능데이터구분자
	private final String psKindName; // 성능종류명
	private Integer tblPartStoreCnt = 5; // 테이블분리보관건수
	private TBL_PART_UNIT_CD tblPartUnitCd; // 테이블분리단위코드

	/**
	 * 
	 * @param psKindName    숫자+(S|M|D|W|M|Y)로 구성된 명칭으로 입력한다.
	 * @param psDataTag
	 * @param dataStoreDays
	 * @param dataRange
	 */
	public PsKind(String psKindName, String psDataTag, int dataStoreDays, String dataRange) {
		this.psKindName = psKindName;
		this.psDataTag = psDataTag;
		this.dataStoreDays = dataStoreDays;
		this.dataRange = dataRange;

		Range data = new Range(this.dataRange);

		if (data.value > 0) {

			this.calendarFieldValue = data.value;

			if (data.unit.startsWith("second")) {
				this.calendarField = Calendar.SECOND;
				this.intervalSeconds = this.calendarFieldValue * 1;
			} else if (data.unit.startsWith("minute")) {
				this.calendarField = Calendar.MINUTE;
				this.intervalSeconds = this.calendarFieldValue * 60;
			} else if (data.unit.startsWith("hour")) {
				this.calendarField = Calendar.HOUR_OF_DAY;
				this.intervalSeconds = this.calendarFieldValue * 3600;
			} else if (data.unit.startsWith("day")) {
				this.calendarField = Calendar.DAY_OF_MONTH;
				this.intervalSeconds = this.calendarFieldValue * 86400;
			} else if (data.unit.startsWith("month")) {
				this.calendarField = Calendar.MONTH;
				this.intervalSeconds = -1;
			} else if (data.unit.startsWith("year")) {
				this.calendarField = Calendar.YEAR;
				this.intervalSeconds = -1;
			} else {
				this.calendarField = -1;
				this.intervalSeconds = -1;
			}

			this.interval = new StringBuffer().append(this.calendarFieldValue).append(data.unit.charAt(0)).toString();

		} else {
			this.calendarField = -1;
			this.calendarFieldValue = -1;
			this.intervalSeconds = -1;
			this.interval = "";
		}
	}

	public void checkDateRange(long startDtm, long endDtm) throws DateRangeOverException {

		long startMs = DateUtil.toMstime(startDtm);
		long endMs = DateUtil.toMstime(endDtm);

		if (this.isRaw()) {
			// 원천 데이터는 하루만 조회 가능
			if (startMs + DAY > endMs) {
				throw new DateRangeOverException(
						this.psKindName + ":" + startDtm + "~" + endDtm + " : " + Lang.get("raw datas per day only."));
			}
		}

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(startMs);
		c.set(this.calendarField, this.calendarFieldValue * 1000); // 1000은 데이터건수

		// System.out.println();

		if (c.getTimeInMillis() < endMs) {
			throw new DateRangeOverException(
					this.psKindName + ":" + startDtm + "~" + endDtm + "," + DateUtil.toHstime(c.getTimeInMillis())
							+ " : " + Lang.get("The range of data retrieval is too wide."));
		}
	}

	public int getCalendarField() {
		return calendarField;
	}

	public int getCalendarFieldValue() {
		return calendarFieldValue;
	}

	public int getDataStoreDays() {
		return dataStoreDays;
	}

	public String getExpiredDate() {
		long mstime = System.currentTimeMillis() - (this.dataStoreDays * 86400000L);
		mstime -= 86400000L;
		return YYYYMMDD.format(new Date(mstime));
	}

	/**
	 * 
	 * @param hstime
	 * @return 종료 시간
	 */
	public long getHstimeEnd(long hstime) {
		return toHstime(getMstimeEnd(toMstime(hstime)));
	}

	/**
	 * 
	 * @param hstime
	 * @return 다음 시간
	 */
	public long getHstimeNext(long hstime, int amount) {
		return toHstime(getMstimeNext(toMstime(hstime), amount));
	}

	/**
	 * 한 그룹의 시작일시
	 * 
	 * @param mstime
	 * @return 한 그룹의 시작일시 public long getMstimeGroupStart(long mstime) { Calendar c
	 *         = Calendar.getInstance(); c.setTimeInMillis(mstime);
	 *         c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0);
	 *         c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0);
	 * 
	 *         if (tblPartUnitCd == TBL_PART_UNIT_CD.Yearly) { // 년 단위
	 *         c.set(Calendar.MONTH, 0); c.set(Calendar.DAY_OF_MONTH, 1); } else if
	 *         (tblPartUnitCd == TBL_PART_UNIT_CD.Monthly) { // 월 단위
	 *         c.set(Calendar.DAY_OF_MONTH, 1); } else if (tblPartUnitCd ==
	 *         TBL_PART_UNIT_CD.Weekly) { // 주 단위 c.set(Calendar.DAY_OF_WEEK,
	 *         Calendar.SUNDAY); } else { // 일 단위 // 위에서 하여 처리 내역 없음 }
	 * 
	 *         return c.getTimeInMillis(); }
	 */

	public long getHstimeNextGroup(long hstime, int amount) {
		return toHstime(getMstimeNextGroup(toMstime(hstime), amount));
	}

	/**
	 * 한 주기(단위)의 시작 일시
	 * 
	 * @param hstime
	 * @return 시작 시간
	 */
	public long getHstimeStart(long hstime) {
		return toHstime(getMstimeStart(toMstime(hstime)));

	}

	/**
	 * 한 그룹의 종료일시
	 * 
	 * @param mstime
	 * @return 한 그룹의 종료일시
	 */
	/*
	 * public long getMstimeGroupEnd(long mstime) { long newTime =
	 * getMstimeGroupStart(mstime); newTime = getMstimeNextGroup(newTime, 1); return
	 * newTime - 1L; }
	 */

	public String getInterval() {
		return interval;
	}

	public long getIntervalSeconds() {
		return intervalSeconds;
	}

	/**
	 * 입력된 시간의 범위내 끝 시간을 구합니다.<br>
	 * 5분 간격인 경우 3분 20초가 입력되면 4분 59초 999을 제공합니다.
	 * 
	 * @param mstime
	 * @return 입력된 시간의 범위내 끝 시간
	 */
	public long getMstimeEnd(long mstime) {
		long stime = getMstimeStart(mstime);
		return this.getMstimeNext(stime, 1) - 1000L;
	}

	/**
	 * count 만큼의 내용을 계산된 시간을 넘긴다.
	 * 
	 * @param mstime
	 * @param amount
	 * @return amount 만큼의 내용을 계산된 시간
	 */
	public long getMstimeNext(long mstime, int amount) {

		// long stime = getMstimeStart(mstime);

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mstime);
		if (amount != 0)
			c.add(this.calendarField, this.calendarFieldValue * amount);
		return c.getTimeInMillis();
	}

	/**
	 * 한 주기(단위)의 시작 일시
	 * 
	 * @param mstime
	 * @return 시작 시간
	 */
	public long getMstimeStart(long mstime) {

		long time = mstime + (TimeZone.getDefault().getRawOffset());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mstime);
		c.set(Calendar.MILLISECOND, 500);

		if (this.calendarField == Calendar.YEAR) {
			c.set(Calendar.MONTH, 0); // 0이면 1월
			c.set(Calendar.DAY_OF_YEAR, 1); // 1이면 1일
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
		} else if (this.calendarField == Calendar.MONTH) {
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
		} else if (this.calendarField == Calendar.DAY_OF_MONTH) {
			long stime = time / (this.calendarFieldValue * DAY);
			stime *= (this.calendarFieldValue * DAY);
			return stime - TimeZone.getDefault().getRawOffset();
		} else if (this.calendarField == Calendar.HOUR_OF_DAY) {
			long stime = time / (this.calendarFieldValue * HOUR);
			stime *= (this.calendarFieldValue * HOUR);
			return stime - TimeZone.getDefault().getRawOffset();
		} else if (this.calendarField == Calendar.MINUTE) {
			long stime = time / (this.calendarFieldValue * 60000L);
			stime *= (this.calendarFieldValue * 60000L);
			return stime - TimeZone.getDefault().getRawOffset();
		} else if (this.calendarField == Calendar.SECOND) {
			long stime = time / (this.calendarFieldValue * 1000L);
			stime *= (this.calendarFieldValue * 1000L);
			return stime - TimeZone.getDefault().getRawOffset();
		}

		return c.getTimeInMillis();

	}

	/**
	 * 
	 * 
	 * @param hstime
	 * @return 해당 시간이 포함되는 그룹명
	 */
	public String getNameGroupByHstime(long hstime) {
		int size = 8;
		if (tblPartUnitCd == TBL_PART_UNIT_CD.Yearly) {
			// 년 단위
			size = 4;
		} else if (tblPartUnitCd == TBL_PART_UNIT_CD.Monthly) {
			// 월 단위
			size = 6;
		} else if (tblPartUnitCd == TBL_PART_UNIT_CD.Weekly) {
			// 주 단위
			size = 6;
		} else {
			// 일 단위
			// 위에서 하여 처리 내역 없음
		}

		return (hstime + "").substring(0, size);
	}

	/**
	 * 
	 * @param mstime
	 * @return 해당 시간이 포함되는 그룹명
	 */
	public String getNameGroupByMstime(long mstime) {
		long hstime = toHstime(mstime);
		return getNameGroupByHstime(hstime);
	}

	public String getPsDataSrc() {
		return psDataSrc;
	}

	public String getPsKindName() {
		return psKindName;
	}

	/**
	 * 테이블명 조회<br>
	 * 테이블을 분리하면 일시를 이용하여 테이블명을 가져오고<br>
	 * 분리하지 않으면 일시에 상관없이<br>
	 * 
	 * @param psItem 성능항목
	 * @param hstime 일시
	 * @return
	 */
	public String getTableName(PsItem psItem, long hstime) {
		return getTableName(psItem.getPsTable(), hstime);
	}

	/**
	 * 
	 * @param psTable
	 * @param dtm
	 * @return
	 */
	public String getTableName(String psTable, long hstime) {
		if (isPartition()) {
			return psTable + getTableTag(hstime);
		} else {
			return psTable + "_" + getPsDataTag();
		}
	}

	public Integer getTblPartStoreCnt() {
		return tblPartStoreCnt;
	}

	/**
	 * 파티션테이블을 사용하는지 여부
	 * 
	 * @return 파티션테이블을 사용하는지 여부
	 */
	public boolean isPartition() {
		return this.tblPartUnitCd != TBL_PART_UNIT_CD.None;
	}

	/**
	 * 원천 테이블인지 여부
	 * 
	 * @return 원천 테이블인지 여부
	 */
	public boolean isRaw() {
		return this.calendarFieldValue < 0;
	}

	public void setPsDataSrc(String psDataSrc) {
		this.psDataSrc = psDataSrc;
	}

	public void setTblPartStoreCnt(Integer tblPartStoreCnt) {
		this.tblPartStoreCnt = tblPartStoreCnt;
	}

	public void setTblPartUnitCd(TBL_PART_UNIT_CD tblPartUnitCd) {
		this.tblPartUnitCd = tblPartUnitCd;
	}

	public void testPrint() {

		if (this.isRaw())
			return;

		// System.out.println(TimeZone.getDefault());
		// System.out.println(TimeZone.getDefault().getRawOffset());
		// System.out.println(TimeZone.getDefault().getRawOffset() / 3600000L);

		long a = toHstime(System.currentTimeMillis());
		long startHstime = this.getHstimeStart(a);

		for (int i = 0; i < 50; i++) {
			long aa = getHstimeNext(startHstime, i);
			System.out.println(startHstime + "," + aa + "\t" + getPsKindName() + " " + getHstimeStart(aa) + " ~ "
					+ getHstimeEnd(aa) + " : " + getNameGroupByHstime(aa) + "\t" + this.calendarField + ","
					+ this.calendarFieldValue);
		}
	}

	/**
	 * 
	 * @param mstime
	 * @return yyyymmddhhmiss 형태의 일시
	 */
	public synchronized long toHstime(long mstime) {
		return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(mstime)));
	}

	public long toMstime(long hstime) {
		return toMstime(String.valueOf(hstime));
	}

	public long toMstime(String hstime) {

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

	@Override
	public String toString() {
		return psKindName + "," + psDataTag + "," + dataRange + "," + tblPartUnitCd;
	}

	/**
	 * 
	 * @param mstime
	 * @param amount
	 * @return 그룹 단위로 계산된 시간
	 */
	private long getMstimeNextGroup(long mstime, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mstime);
		c.add(this.tblPartUnitCd.getCalendarType(), amount);
		return getMstimeStart(c.getTimeInMillis());
	}

	private String getPsDataTag() {
		return psDataTag;
	}

	private String getTableTag(long hstime) {
		return "_" + getPsDataTag() + "_" + getNameGroupByHstime(hstime);
	}

}

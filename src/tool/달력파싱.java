package tool;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.CoCode.DOW_CD;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.FileUtil;

/**
 * 달력 만들기<br>
 * https://astro.kasi.re.kr/life/post/calendarData<br>
 * 위 사이트에서 해당 파일 다운로드<br>
 * 
 * @author subkjh
 *
 */

public class 달력파싱 {
	class DateData {
		String date;
		String msg;
		DOW_CD dowCd;
		int seanCd;
		boolean isHoliday;

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(date).append("\t");
			sb.append(dowCd).append("\t");
			sb.append(isHoliday ? "Y" : "N").append("\t\t");
			sb.append(seanCd).append("\t");
			sb.append(msg == null ? "" : msg);
			return sb.toString();
		}
	}

	public 달력파싱(int year, File file) {
		this.year = year;
		this.file = file;
	}

	private File file;
	private int year;
	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private Map<String, DateData> map = new HashMap<String, DateData>();

	public static void main(String[] args) {
		달력파싱 c;

		c = new 달력파싱(2022, new File("datas/setup/2022년 달력자료.txt"));
		c.makeDate();
		c.parse();
		c.print();
		c = new 달력파싱(2023, new File("datas/setup/2023 달력자료.txt"));
		c.makeDate();
		c.parse();
		c.print();
		c = new 달력파싱(2024, new File("datas/setup/2024 달력자료.txt"));
		c.makeDate();
		c.parse();
		c.print();
	}

	private void parse() {
		List<String> lineList;
		try {
			lineList = FileUtil.getLines(file, Charset.forName("utf-8"));
			String data;
			for (int i = 0; i < lineList.size(); i++) {
				data = lineList.get(i);

				if (data.startsWith("24기")) {
					i = parse2(lineList, i, 3, 2, 5);
				} else if (data.startsWith("기타 명절")) {
					i = parse2(lineList, i, 1, 0, 3);
				} else if (data.startsWith("기념일")) {
					i = parse2(lineList, i, 2, 1, 4);
				} else if (data.startsWith("국경일")) {
					i = 국경일(lineList, i, 2, 1, 4);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param lineList
	 * @param index
	 * @param monthIdx 뒤에서 시작한 월에 대한 순서
	 * @param dayIdx   뒤에서 시작한 일자에 대한 순서
	 * @param size
	 * @return
	 */
	private int parse2(List<String> lineList, int index, int monthIdx, int dayIdx, int size) {
		String data;
		String ss[];
		String date;
		int i = index;
		for (; i < lineList.size(); i++) {
			data = lineList.get(i);
			if (data.startsWith("======"))
				break;
			ss = data.split("[ ]+");
			if (ss.length >= size) {
				date = makeDate(ss[ss.length - (monthIdx + 1)], ss[ss.length - (dayIdx + 1)]);
				String msg = "";
				for (int n = 0; n < ss.length - (monthIdx + 1); n++) {
					msg += ss[n];
				}
				setDate(date, msg, null);
			}

		}

		return i;
	}

	private void setDate(String date, String msg, Boolean isHoliday) {
		DateData v = map.get(date);
		if (v != null) {
			if (v.msg == null) {
				v.msg = msg;
			} else {
				v.msg += "," + msg;
			}
			if (isHoliday != null) {
				v.isHoliday = isHoliday;
			}

		}
	}

	private void print() {
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		for (String date : list) {
			System.out.println(map.get(date));
		}
	}

	private String makeDate(String month, String day) {
		StringBuffer sb = new StringBuffer();
		sb.append(year);
		if (month.length() == 1)
			sb.append("0");
		sb.append(month);
		if (day.length() == 1)
			sb.append("0");
		sb.append(day);
		return sb.toString();
	}

	/**
	 * SEAN_CD 1 봄, 가을철<br>
	 * SEAN_CD 2 여름철<br>
	 * SEAN_CD 3 겨울철<br>
	 * 
	 * @param year
	 */
	private void makeDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, 1);
		DateData v;
		while (true) {

			v = new DateData();
			v.date = YYYYMMDD.format(new Date(c.getTimeInMillis()));
			v.dowCd = DOW_CD.getDOW_CD(c.get(Calendar.DAY_OF_WEEK));
			v.isHoliday = DOW_CD.SUN == v.dowCd;

			if (v.date.compareTo(year + "0401") < 0) {
				v.seanCd = 3;
			} else if (v.date.compareTo(year + "0701") < 0) {
				v.seanCd = 1;
			} else if (v.date.compareTo(year + "0901") < 0) {
				v.seanCd = 2;
			} else if (v.date.compareTo(year + "1201") < 0) {
				v.seanCd = 1;
			} else {
				v.seanCd = 3;

			}

			map.put(v.date, v);

			c.add(Calendar.DAY_OF_YEAR, 1);
			if (c.get(Calendar.YEAR) != year) {
				break;
			}

		}
	}

	private int 국경일(List<String> lineList, int index, int monthIdx, int dayIdx, int size) {
		String data;
		String ss[];
		String date;
		int i = index;
		int pos;
		DateData v;
		String msg;
		for (; i < lineList.size(); i++) {
			data = lineList.get(i);
			if (data.startsWith("======"))
				break;
			pos = data.indexOf("   ");
			if (pos > 0) {
				msg = data.substring(0, pos);
//				System.out.println(data.substring(0, pos) + "\t\t\t\t" + data.substring(pos).trim());
				date = data.substring(pos).trim();
				date = date.replaceAll("월", "월 ");
				date = date.replaceAll("일", "일 ");
				date = date.replaceAll("~", "");

				ss = date.split("[ ]+");
				if (ss.length <= 3) {
					date = makeDate(ss[0].replaceAll("월", ""), ss[1].replaceAll("일", ""));
					setDate(date, msg.replaceAll("\\*", ""), msg.indexOf("**") > 0 ? false : true);
				} else {
					String sDate = makeDate(ss[0].replaceAll("월", ""), ss[1].replaceAll("일", ""));
					String eDate = makeDate(ss[2].replaceAll("월", ""), ss[3].replaceAll("일", ""));
					long sMs = DateUtil.toMstime(sDate);
					long eMs = DateUtil.toMstime(eDate);
					for (long ms = sMs; ms <= eMs; ms += 86400000) {
						date = YYYYMMDD.format(new Date(ms));
						setDate(date, msg.replaceAll("\\*", ""), msg.indexOf("**") > 0 ? false : true);
					}
				}
			}
		}

		return i;
	}

}

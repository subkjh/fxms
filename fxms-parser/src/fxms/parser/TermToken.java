package fxms.parser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TermToken extends AttrToken {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	public static void setTime(List<Token> tokenList, Map<String, Object> map) {

		TermToken termToken = null;
		boolean desc = false;

		for (Token token : tokenList) {
			if (termToken == null && token instanceof TermToken) {
				termToken = (TermToken) token;
			} else if (token instanceof OrderToken) {
				if (((OrderToken) token).desc()) {
					desc = true;
				}
			}
		}

		if (termToken == null) {
			return;
		}

		int num = 0;

		if (termToken.getValue() == null) {
			num = Integer.valueOf(toNumber(termToken.getType()));
		} else {
			num = Integer.valueOf(toNumber(termToken.getValue()));
		}
		
		if ( desc ) {
			num *= -1;
		}
		
		String unit = termToken.getType();

		if (unit.indexOf("yesterday") >= 0) {
			long today = getToday(num);
			String endDate = YYYYMMDDHHMMSS.format(new Date(getToday(num+1) - 1));
			String startDate = YYYYMMDDHHMMSS.format(new Date(today));
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			return;
		}

		if (unit.indexOf("today") >= 0) {
			long today = getToday(0);
			String endDate = YYYYMMDDHHMMSS.format(new Date(today + 86400000L - 1));
			String startDate = YYYYMMDDHHMMSS.format(new Date(today));
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			return;
		}

		if (unit.indexOf("month") >= 0) {
			long today = getMonth(num);
			String endDate = YYYYMMDDHHMMSS.format(new Date(getMonth(num + 1) - 1));
			String startDate = YYYYMMDDHHMMSS.format(new Date(today));
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			return;
		}

		if (unit.indexOf("minute") >= 0) {
			num *= 60;
		} else if (unit.indexOf("hour") >= 0) {
			num *= 3600;
		} else if (unit.indexOf("day") >= 0) {
			num *= 86400;
		}

		long time = num * 1000L;

		String ret = YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis() + time));
		map.put("startDate", ret);
		map.put("endDate", TermToken.nowDate());

	}

	private static long getToday(int amount) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DAY_OF_YEAR, amount);

		return c.getTimeInMillis();
	}

	private static long getMonth(int amount) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, amount);
		return c.getTimeInMillis();
	}

	public static String nowDate() {
		return YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
	}

	public TermToken(String id, String text, String type) {
		super(id, text, type);
	}

	@Override
	public boolean match(String s) {

		String newS = removeNumber(s);

		if (super.match(newS)) {
			return true;
		}

		return false;
	}

}

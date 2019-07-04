package fxms.nms.co.syslog;

import java.util.Calendar;

import fxms.bas.co.utils.CheckUtil;
import fxms.nms.co.syslog.vo.SyslogVo;

/**
 * 화일에 들어있는 SYSLOG을 파싱하여 일시와 IP주소, 로그내용을 파생합니다.
 * 
 * @author subkjh
 * 
 */
public class SyslogParser {

	private int indexIp = 0;

	public SyslogParser() {

	}

	public int getIndexIp() {
		return indexIp;
	}

	/**
	 * split로 자른 값들은 배열에 넣어야 하기 때문에 ss[]을 생성 아래에서는 " " 으로만 나눠주기 때문에 " " 이 들어간
	 * syslog만 가능하다. 따라서 장비별이나 여러가지 syslog 방식을 추가.
	 * 
	 */
	public SyslogVo parse(String str) throws Exception {

		SyslogVo bean = new SyslogVo();

		// System.out.println(" str.length()" + str.length());
		String ss[] = str.substring(16).split(" ");
		String ip = ss[getIndexIp()];

		try {
			CheckUtil.checkIp(ip);
			bean.setIpAddress(ip);
		} catch (Exception e) {
			bean.setHostname(ip);
		}

		bean.setMsg(str);
		bean.setMsTime(getTime(str));

		return bean;
	}

	public void setIndexIp(int indexIp) {
		this.indexIp = indexIp;
	}

	/**
	 * @method getTime
	 * @method 시간을 가져온다. substring(start, end-1)을 월, 날짜, 시간, 분, 초 순서로 가져온다.
	 * @param String
	 * 
	 * @return c.getTimeInMillis() (Calendar 의 시각치를 밀리 세컨드로 돌려줍니다.)
	 */
	private long getTime(String str) {
		String ss[] = new String[] { str.substring(0, 3), str.substring(4, 6), str.substring(7, 9), str.substring(10, 12),
				str.substring(13, 15) };
		// 0~2, 4~5, 7~8, 10~11, 13~14 만큼 자른다.
		Calendar c = Calendar.getInstance();
		// 클래스 메서드로써 내부적으로 새로운 인스턴스를 생성하여 Calendar 레퍼런스를 반환한다
		c.set(Calendar.MONTH, getMonth(ss[0]));
		// 0 ~ 11까지 나온다.
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ss[1].trim()));
		// 해당하는 달에서의 날짜
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ss[2]));
		// 24시간 기준의 시간
		c.set(Calendar.MINUTE, Integer.parseInt(ss[3]));
		c.set(Calendar.SECOND, Integer.parseInt(ss[4]));

		return c.getTimeInMillis();
	}

	/**
	 * @method getMonth
	 * @method Month값을 가져온다. Month는 0이 기준이기 때문에 해당 월에서 -1 을 해준값이다. Jan 1월 일 경우
	 *         0, Feb 2월일 경우 1, Mar 3월일 경우 2, Apr 4월일 경우 3..
	 * @param String
	 *            s
	 * 
	 * @return
	 */
	//
	protected int getMonth(String s) {
		return s.equals("Jan") || s.equals("1월") ? 0 : //
				s.equals("Feb") || s.equals("2월") ? 1 : //
						s.equals("Mar") || s.equals("3월") ? 2 : //
								s.equals("Apr") || s.equals("4월") ? 3 : //
										s.equals("May") || s.equals("5월") ? 4 : //
												s.equals("Jun") || s.equals("6월") ? 5 : //
														s.equals("Jul") || s.equals("7월") ? 6 : //
																s.equals("Aug") || s.equals("8월") ? 7 : //
																		s.equals("Sep") || s.equals("9월") ? 8 : //
																				s.equals("Oct") || s.equals("10월") ? 9 : //
																						s.equals("Nov") || s.equals("11월") ? 10
																								: 11;
	}

}

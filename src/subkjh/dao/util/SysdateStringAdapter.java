package subkjh.dao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * insert/update문에서 sysdate로 되어 있는 내용을 현재시간으로 변경한다.
 * 
 * @author subkjh
 *
 */
public class SysdateStringAdapter implements StringAdapter {

	private String sysdate;

	public SysdateStringAdapter() {
		SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
		sysdate = YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));

	}

	@Override
	public String convert(String s) {

		if (s.equalsIgnoreCase("sysdate")) {
			return sysdate;
		}

		return s;
	}

}

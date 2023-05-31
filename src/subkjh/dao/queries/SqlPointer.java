package subkjh.dao.queries;

import java.io.InputStream;

import subkjh.bas.co.log.Logger;

/**
 * 쿼리를 정의한 XML이 위치하는 곳을 나타내는 클래스
 * 
 * @author subkjh
 * 
 */
public class SqlPointer {

	/**
	 * 
	 * @param name
	 * @return
	 */
	public InputStream getResource(String filename) {

		String urlname = getUrl(filename);
		if (urlname == null)
			return null;

		InputStream inputStream = SqlPointer.class.getResourceAsStream(urlname);

		if (inputStream != null) {
			Logger.logger.trace("URL : file={},class={}", filename,
					SqlPointer.class.getPackage().getName() + "." + urlname);
		}
		return inputStream;
	}

	public String getUrl(String filename) {

		int pos = filename.indexOf("sql");
		if (pos < 0)
			return null;

		String urlname = filename.substring(pos + 4);
		urlname = urlname.replaceAll("\\\\", "/");

		return urlname;
	}
}

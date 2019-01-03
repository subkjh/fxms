package fxms.module.restapi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fxms.bas.exception.NotFoundException;
import subkjh.bas.log.Logger;

/**
 * REST API를 위한 fx 핸들러
 * 
 * @author subkjh@naver.com(김종훈)
 *
 */
public abstract class FxHttpHandler implements HttpHandler {

	private static long WORK_SEQNO = 10000000;

	private synchronized long getNextSeqno() {
		return ++WORK_SEQNO;
	}

	public FxHttpHandler() {

	}

	@Override
	public void handle(HttpExchange he) throws IOException {

		long seqno = getNextSeqno();

		String curName = Thread.currentThread().getName();

		Thread.currentThread().setName("HTTP-" + seqno);

		Logger.logger
				.debug(he.getRemoteAddress().getHostString() + ", " + he.getRequestURI().getPath() + ", handle...");

		try {

			String method = he.getRequestMethod();

			Set<Entry<String, List<String>>> header;
			String body;

			header = he.getRequestHeaders().entrySet();

			if ("GET".equalsIgnoreCase(method)) {
				URI requestedUri = he.getRequestURI();
				body = requestedUri.getRawQuery();
			} else {
				InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
				BufferedReader br = new BufferedReader(isr);
				body = br.readLine();
				br.close();
				isr.close();
			}

			Logger.logger.info("HEAD\n" + header + "\nBODY\n" + body);

			try {
				byte bytes[] = onProcess(he.getRemoteAddress(), header, body);
				he.sendResponseHeaders(200, bytes.length);
				OutputStream os = he.getResponseBody();
				os.write(bytes);
				os.close();

				if (bytes.length <= 1024) {
					Logger.logger.info("WRITE\n" + new String(bytes));
				} else {
					Logger.logger.info("WRITE\nsize=" + bytes.length);

				}

			} catch (NotFoundException | FileNotFoundException e) {
				String msg = e.getMessage();
				if (msg == null) {
					msg = "예외 발생 (" + e.getClass().getName() + ")";
				}

				Logger.logger.fail("ERROR : " + msg);

				he.sendResponseHeaders(404, msg.length());
				OutputStream os = he.getResponseBody();
				os.write(msg.getBytes());
				os.close();
			} catch (Exception e) {
				String msg = e.getMessage();
				if (msg == null) {
					msg = "예외 발생 (" + e.getClass().getName() + ")";
				}

				Logger.logger.fail("ERROR : " + msg);

				he.sendResponseHeaders(500, msg.length());
				OutputStream os = he.getResponseBody();
				os.write(msg.getBytes());
				os.close();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Thread.currentThread().setName(curName);
		}

	}

	/**
	 * 요청 내용을 처리하는 메소드
	 * 
	 * @param client
	 *            요청 클라이언트
	 * @param header
	 *            헤더
	 * @param body
	 *            바디
	 * @return 결과
	 * @throws Exception
	 */
	protected abstract byte[] onProcess(InetSocketAddress client, Set<Entry<String, List<String>>> header, String body)
			throws Exception;

	@SuppressWarnings("unchecked")
	protected void parseBody(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);

					} else if (obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}

}

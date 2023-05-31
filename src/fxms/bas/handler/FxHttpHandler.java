package fxms.bas.handler;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fxms.bas.exp.NotFoundException;
import subkjh.bas.co.log.Logger;

/**
 * Http 핸들러
 * 
 * @author subkjh
 *
 */
public abstract class FxHttpHandler implements HttpHandler {

	private static long WORK_SEQNO = 10000000;
	public static final String CHARSET = "UTF-8";

	public FxHttpHandler() {
	}

	@Override
	public void handle(HttpExchange he) throws IOException {

		long seqno = getNextSeqno();

		Thread th = new Thread() {
			public void run() {
				try {
					onHandling(he);
				} catch (IOException e) {
					Logger.logger.error(e);
				}
			}
		};
		th.setName("HTTP-" + seqno);
		th.start();

	}

	protected String getMappingUrl(HttpExchange he) {
		String paths[] = he.getRequestURI().getPath().substring(1).split("/");
		String mapping = paths.length > 1 ? paths[paths.length - 1] : null;
		return mapping;
	}

	protected boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 요청 내용을 처리하는 메소드
	 * 
	 * @param he
	 * @param client
	 * @param mapping
	 * @param body
	 * @return
	 * @throws Exception
	 */
	protected abstract byte[] onProcess(HttpExchange he, InetSocketAddress client, String mapping, String body)
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

	private synchronized long getNextSeqno() {
		return ++WORK_SEQNO;
	}

	private void onHandling(HttpExchange he) throws IOException {

		String mapping = getMappingUrl(he);

		Logger.logger.debug(
				he.getRemoteAddress().getHostString() + ", " + he.getRequestURI().getPath() + ", handle..." + mapping);

		String retMsg = null;

		try {

			String method = he.getRequestMethod();
			Headers header = he.getRequestHeaders();
			String body;

			if ("GET".equalsIgnoreCase(method)) {
				URI requestedUri = he.getRequestURI();
				body = requestedUri.getRawQuery();

				// json 형식으로 강제 변환함.
				Map<String, Object> map = new HashMap<String, Object>();
				parseBody(body, map);

				if (map.size() > 0) {
					Gson son = new Gson();
					body = son.toJson(map);
				} else {
					body = "";
				}

			} else {
				InputStreamReader isr = new InputStreamReader(he.getRequestBody(), CHARSET);
				BufferedReader br = new BufferedReader(isr);
				StringBuffer tmp = new StringBuffer();
				String line;
				while ( true ) {
					line = br.readLine();
					if ( line == null) break;
					tmp.append(line).append("\n");
				}
				body = tmp.toString();	
				br.close();
				isr.close();
			}

			body = body == null ? "" : body.trim();

			Logger.logger.info("\n*** INPUT ***\n--- HEAD ---\n{}\n--- BODY ---\n{}\n------------", header.entrySet(),
					body);

			try {

				byte bytes[] = onProcess(he, he.getRemoteAddress(), mapping, body);

				// 데이터가 있으면
				he.getResponseHeaders().add("Content-Type", "charset=" + CHARSET);
				he.sendResponseHeaders(200, bytes.length);

				OutputStream os = he.getResponseBody();
				os.write(bytes);
				os.close();

				retMsg = "size=" + bytes.length;

				Logger.logger.info("\n*** OUTPUT ***\n--- HEAD ---\n{}\n--- BODY ---\n{}\n------------",
						he.getResponseHeaders().entrySet(),
						(bytes.length > 1000 ? "size=" + bytes.length : new String(bytes, CHARSET)));

			} catch (NotFoundException | FileNotFoundException e) {

				Logger.logger.error(e);

				String msg = e.getMessage();
				if (msg == null) {
					msg = "예외 발생 (" + e.getClass().getName() + ")";
				}

				retMsg = "error=" + msg;

				Logger.logger.fail("ERROR : " + msg);

				he.sendResponseHeaders(404, msg.length());
				OutputStream os = he.getResponseBody();
				os.write(msg.getBytes(CHARSET));
				os.close();

			} catch (Exception e) {

				Logger.logger.error(e);

				String msg = e.getMessage();
				if (msg == null) {
					msg = "예외 발생 (" + e.getClass().getName() + ")";
				}

				retMsg = "error=" + msg;
				Logger.logger.fail("ERROR : " + msg);

				he.sendResponseHeaders(500, msg.length());
				OutputStream os = he.getResponseBody();
				os.write(msg.getBytes(CHARSET));
				os.close();
			}

		} catch (Exception e) {

			String msg = e.getMessage();
			if (msg == null) {
				msg = "예외 발생 (" + e.getClass().getName() + ")";
			}
			retMsg = "error=" + msg;

			Logger.logger.error(e);
			throw e;

		} finally {

			Logger.logger.debug("{}:{}/  {}  result {}", he.getRemoteAddress().getHostString(),
					he.getRequestURI().getPath(), mapping, retMsg);
		}

	}
}

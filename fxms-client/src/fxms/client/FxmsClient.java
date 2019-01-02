package fxms.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.LongSerializationPolicy;

import fxms.client.log.Logger;

public class FxmsClient {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	public static <T> List<T> convert(Class<T> classOf, List<Map<String, Object>> list) {

		if (list == null) {
			return new ArrayList<T>();
		}

		List<T> ret = new ArrayList<T>();
		T data;
		try {
			for (Map<String, Object> map : list) {
				data = classOf.newInstance();
				ObjectUtil.toObject(map, data);
				ret.add(data);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}

	/**
	 * 눈으로 확인할 수 있는 시간 형식으로 넘긴다.
	 * 
	 * @return yyyyMMddHHmmss의 값
	 */
	public static synchronized long getDate(long mstime) {
		if (mstime <= 0) {
			return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())));
		} else {
			return Long.parseLong(YYYYMMDDHHMMSS.format(new Date(mstime)));
		}
	}

	private String host;
	private int port;
	private String sessionId;
	private long seqno;
	protected final Gson gson;
	private String userId;
	private String password;
	private Map<String, Object> userMap;

	public FxmsClient() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gsonBuilder.create();
	}

	/**
	 * 
	 * @param host
	 *            접속서버
	 * @param port
	 *            접속포트
	 */
	public FxmsClient(String host, int port) {
		this.host = host;
		this.port = port;

		Logger.logger.info("host={} port={}", host, port);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		gson = gsonBuilder.create();
	}

	public final String getSessionId() {
		return sessionId;
	}

	public Map<String, Object> getUserMap() {
		if (userMap == null) {
			userMap = new HashMap<String, Object>();
		}
		return userMap;

	}

	/**
	 * 
	 * @param userId
	 *            로그인 ID
	 * @param password
	 *            암호
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> login(String userId, String password) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("user-id", userId);
		para.put("password", password);
		String result = post(getUrl("login"), gson.toJson(para));

		para = gson.fromJson(result, Map.class);

		if ("N".equalsIgnoreCase(para.get("is-ok") + "")) {
			throw new Exception("로그인 실패");
		}

		this.userId = userId;
		this.password = password;

		userMap = (Map<String, Object>) para.get("result");

		sessionId = userMap.get("sessionId") + "";
		seqno = Double.valueOf(para.get("next-seqno") + "").longValue();

		return userMap;
	}

	public <T> List<T> selectList(String uri, String method, Map<String, Object> para, Class<T> classOfT) {
		List<Map<String, Object>> list;
		try {
			list = testRetList(uri, method, para);
			return convert(classOfT, list);
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	public void setServer(String host, int port) {
		this.host = host;
		this.port = port;

		Logger.logger.info("host={} port={}", host, port);
	}

	/**
	 * 
	 * @param uri
	 *            context로 mo, ao, ps
	 * @param method
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> testRetList(String uri, String method, Map<String, Object> parameters)
			throws Exception {

		Map<String, Object> pdu = getReq();
		pdu.put("method", method);
		pdu.put("parameters", parameters);
		String request = gson.toJson(pdu);
		String result = post(getUrl(uri), request);

		try {
			Map<String, Object> res = parseResult(result);
			return (List<Map<String, Object>>) res.get("result");
		} catch (ReloginException e) {
			try {
				this.login(userId, password);
				pdu = getReq();
				pdu.put("method", method);
				pdu.put("parameters", parameters);
				request = gson.toJson(pdu);
				result = post(getUrl(uri), request);
				Map<String, Object> res = parseResult(result);
				return (List<Map<String, Object>>) res.get("result");
			} catch (Exception e2) {
				throw e2;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 
	 * @param uri
	 * @param method
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> testRetObj(String uri, String method, Map<String, Object> parameters) throws Exception {

		Map<String, Object> pdu = getReq();
		pdu.put("method", method);
		if (parameters != null) {
			pdu.put("parameters", parameters);
		}
		String request = gson.toJson(pdu);

		String result = post(getUrl(uri), request);

		try {
			Map<String, Object> res = parseResult(result);

			if (res.get("result") instanceof List) {
				Map<String, Object> ret = new HashMap<String, Object>();
				ret.put("list", res.get("result"));
				return ret;
			} else {
				return (Map<String, Object>) res.get("result");
			}
		} catch (ReloginException e) {
			try {
				this.login(userId, password);

				pdu = getReq();
				pdu.put("method", method);
				if (parameters != null) {
					pdu.put("parameters", parameters);
				}
				request = gson.toJson(pdu);

				result = post(getUrl(uri), request);
				Map<String, Object> res = parseResult(result);
				return (Map<String, Object>) res.get("result");
			} catch (Exception e2) {
				throw e2;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private int getLength(Object obj) {
		if (obj == null) {
			return 0;
		}

		try {
			return obj.toString().getBytes("euc-kr").length;
		} catch (UnsupportedEncodingException e) {
			return obj.toString().length();
		}
	}

	private Map<String, Object> getReq() {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("session-id", sessionId);
		para.put("seqno", seqno);

		return para;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> parseResult(String result) throws ReloginException, Exception {

		Map<String, Object> para = new HashMap<String, Object>();

		para = gson.fromJson(result, Map.class);

		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(result);

		String isOk = para.get("is-ok") + "";

		if ("N".equals(isOk)) {
			Logger.logger.fail("JsonParser output: {}", jsonElement);
		} else {
			Logger.logger.trace("JsonParser output: {}", jsonElement);
		}

		seqno = Double.valueOf(para.get("next-seqno") + "").longValue();

		if (seqno == -1 && userId != null) {
			throw new ReloginException();
		}

		if ("N".equals(isOk)) {
			throw new Exception("errmsg=" + para.get("errmsg"));
		}

		return para;
	}

	protected String getUrl(String path) {
		return "http://" + host + ":" + port + "/" + path;
	}

	protected final String post(String site, String input) throws Exception {
		try {

			Logger.logger.debug("site={} sent={}", site, input);

			URL url = new URL(site);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Data-Type", "json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			// if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			// throw new RuntimeException("Failed : HTTP error code : " +
			// conn.getResponseCode());
			// }

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuffer sb = new StringBuffer();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			conn.disconnect();

			Logger.logger.debug("site={} received={}", site, sb.toString());

			return sb.toString();

		} catch (Exception e) {

			throw e;
		}

	}

	public final long download(String filename, File toFile) throws Exception {
		try {

			if ( toFile.getParentFile().exists() == false ) {
				toFile.getParentFile().mkdirs();
			}
			
			Logger.logger.debug("filename={}", filename);

			URL url = new URL(getUrl("download"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Data-Type", "json");

			StringBuffer sb = new StringBuffer();
			sb.append("{ method:\"download\", file-name:\"");
			sb.append(filename);
			sb.append("\" }");

			OutputStream os = conn.getOutputStream();
			os.write(sb.toString().getBytes());
			os.flush();

			InputStream inStream = conn.getInputStream();
			FileOutputStream outStream = null;
			int bytesRead;
			byte[] buffer = new byte[2048];

			try {
				outStream = new FileOutputStream(toFile, false);
				while ((bytesRead = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (outStream != null)
					try {
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}

			inStream.close();
			conn.disconnect();

			Logger.logger.debug("file={},size={}", toFile.getName(), toFile.length());
			
			return toFile.length();

		} catch (Exception e) {

			throw e;
		}

	}

	public final List<Map<String, Object>> ls(String folder) throws Exception {
		try {

			Logger.logger.debug("folder={}", folder);

			URL url = new URL(getUrl("download"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Data-Type", "json");

			StringBuffer sb = new StringBuffer();
			sb.append("{ method:\"list\", folder:\"");
			sb.append(folder);
			sb.append("\" }");

			OutputStream os = conn.getOutputStream();
			os.write(sb.toString().getBytes());
			os.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			sb = new StringBuffer();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			conn.disconnect();

			return (List) new Gson().fromJson(sb.toString(), ArrayList.class);

		} catch (Exception e) {

			throw e;
		}

	}

	protected void print(List<Map<String, Object>> mapList) {
		if (mapList == null || mapList.size() == 0)
			return;

		Map<String, Object> map = mapList.get(0);
		String nameArr[] = map.keySet().toArray(new String[map.size()]);
		Arrays.sort(nameArr);

		int max[] = new int[nameArr.length];

		for (int i = 0; i < max.length; i++) {
			max[i] = nameArr[i].length();
		}

		for (Map<String, Object> e : mapList) {
			for (int i = 0; i < nameArr.length; i++) {
				max[i] = Math.max(max[i], (getLength(e.get(nameArr[i]))));
			}
		}

		StringBuffer line = new StringBuffer();
		line.append("+-");
		for (int i = 0; i < max.length; i++) {
			for (int n = 0; n < max[i]; n++)
				line.append("-");
			line.append("-+");
			if (i < max.length - 1)
				line.append("-");
		}

		System.out.println(line);
		System.out.print("| ");
		for (int i = 0; i < nameArr.length; i++) {
			System.out.format("%-" + max[i] + "s", nameArr[i]);
			if (i < nameArr.length)
				System.out.print(" | ");
		}
		System.out.println("\n" + line);

		for (Map<String, Object> e : mapList) {
			System.out.print("| ");
			for (int i = 0; i < nameArr.length; i++) {
				System.out.format("%-" + max[i] + "s", e.get(nameArr[i]));
				if (i < nameArr.length)
					System.out.print(" | ");
			}
			System.out.println();
		}

		System.out.println(line);

		System.out.println("  " + mapList.size() + " rows");
		System.out.println(line);
	}
}

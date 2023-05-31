package fxms.bas.ws.handler.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.FxResponse;
import subkjh.bas.co.log.Logger;

public class FxHttpClient {

	private static FxHttpClient fxmsClient;

	public static FxHttpClient getFxmsClient() throws Exception {
		if (fxmsClient == null) {
			fxmsClient = new FxHttpClient("localhost", 10005);
			fxmsClient.login("subkjh", "1111");
		}
		return fxmsClient;
	}

	public static void main(String[] args) {
		FxHttpClient c = new FxHttpClient("localhost", 10005);

		FxResponse response;
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("alarmCfgNo", 10001);

		try {
			c.login("subkjh", "1111");

			response = c.call("login/update-token", para);
			System.out.println(response);

			response = c.call("alarmcfg/select-alarm-cfg-mem-list", para);
			System.out.println(response);

			response = c.call("ps/select-ps-item-list", para);
			System.out.println(response);
			
			para.clear();
			para.put("moNo", 1000187);
			para.put("psId", "E01V5");
			response = c.call("ps/select-ps-value-min-max-avg-list", para);
			System.out.println(response);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, Object> makaPara(Object... objects) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		int index = 0;
		while (index + 1 < objects.length) {
			parameters.put(objects[index] + "", objects[index + 1]);
			index += 2;
		}
		return parameters;
	}

	private String host;

	private int port;

	private String jwt;

	/**
	 * 
	 * @param host 접속서버
	 * @param port 접속포트
	 */
	public FxHttpClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	protected String getUrl(String path) {
		return "http://" + host + ":" + port + "/" + path;
	}

	protected String makeJson(Object para) {
		return FxmsUtil.toJson(para);
	}

	protected Map<String, Object> makeMap(String para) {
		return FxmsUtil.toMapFromJson(para);
	}

	protected final FxResponse post(String site, String input) throws Exception {

		try {

			// 사이트 연결
			URL url = new URL(site);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Data-Type", "json");
			
			// 토큰 추가
			if (this.jwt != null)
				conn.setRequestProperty("Authorization", "Bearer " + this.jwt);

			// 요청 내역 보내기
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			// if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			// throw new RuntimeException("Failed : HTTP error code : " +
			// conn.getResponseCode());
			// }

//			Response response = new Response();
//			String val = conn.getHeaderField("Authorization");
//			if (val != null) {
//				response.jwt = val.replaceFirst("Bearer ", "");
//			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuffer sb = new StringBuffer();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			conn.disconnect();

			Logger.logger.info("url={}, para={}, response={}", site, input, sb.toString());

			return new FxResponse(this.makeMap(sb.toString()));

		} catch (Exception e) {

			Logger.logger.fail("url={}, para={}\n{}", site, input, e.getMessage());

			throw e;
		}

	}

	/**
	 * 
	 * @param uri        context로 mo, ao, ps
	 * @param method
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public FxResponse call(String uri, Map<String, Object> parameters) throws Exception {

		try {

			String request = makeJson(parameters);
			return post(getUrl(uri), request);
		} catch (Exception e) {
			throw e;
		}

	}

	public final long download(String filename, File toFile) throws Exception {
		try {

			if (toFile.getParentFile().exists() == false) {
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

	public void login(String userId, String password) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userId", userId);
		para.put("userPwd", password);

		FxResponse response = post(getUrl("login"), makeJson(para));
		System.out.println(FxmsUtil.toJson(response));
		this.jwt = String.valueOf(response.get("jwt"));
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}

package subkjh.bas.net.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class UrlClientPost {

	public static void main(String[] args) throws Exception {
		String site;

		UrlClientPost t = new UrlClientPost();

		// site = "http://localhost:8080/fxms-rest-api/bio/fxapi/callProcess";
//		site = "http://192.168.0.199:9080/rpc?target=itms&method=dev.device.get_wan_tab&body=";
		site = "http://219.248.49.66:7777/rpc?method=dev.device.get_wan_tab&body=";
//		site = "http://192.168.0.199:9080/rpc?target=itms&method=qi_stb.raw.detail&body=";
		
//		String body = "{\"key\": \"T102||_tab__tab__tab__tab_|1508725800|239.192.69.236:49220|OVERALL|CRIT\", \"page\": 1, \"mode\": \"by_channel\", \"good\": 0, \"warn\": 0, \"crit\": 1, \"net\": \"\", \"method\": \"qi_stb.raw.detail\", \"x-osk-token\": \"\", \"x-osk-call\": \"1\"}";
		Gson son = new Gson();
		// {"ip":"211.58.70.233","vendor":"mmc-ap","dev_mac":"085DDD9AAA55","dev_model":"Giga
		// AP(머큐리
		// RUSH-337AC)","net_type":"LanE","vail":false,"limit":20,"page":1,"method":"dev.device.get_wan_tab","no_session":false}:
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("ip", "211.58.70.233");
		para.put("vendor", "mmc-ap");
		para.put("dev_mac", "085DDD9AAA55");
		para.put("dev_model", "Giga AP(머큐리 RUSH-337AC)");
		para.put("net_type", "LanE");
		para.put("vail", false);
		para.put("limit", 20);
		para.put("page", 1);
		para.put("method", "dev.device.get_wan_tab");
		para.put("no_session", false);

		System.out.println(son.toJson(para));
//		System.out.println(body);

		System.out.println("received : " + t.post(site, son.toJson(para)));
//		System.out.println("received : " + t.post(site, body));
	}

	public String post(String site, String input) throws Exception {
		try {

			URL url = new URL(site);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Data-Type", "json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

//			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuffer sb = new StringBuffer();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			conn.disconnect();

			return sb.toString();

		} catch (Exception e) {

			throw e;
		}

	}

}

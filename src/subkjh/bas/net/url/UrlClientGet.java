package subkjh.bas.net.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class UrlClientGet {

	public static final int TIMEOUT_SEC = 30;

	public static void main(String[] args) {

		UrlClientGet c = new UrlClientGet();
//		try {
//			c.get("http://125.7.128.54:2580/q?start=2017/10/20-16:56:55&end=2017/10/20-16:57:55&m=sum:rc01.co2.ppm%7Bid=2000%7D&ascii", 10);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		site = "http://localhost:8080/fxms-rest-api/bio/fxapi/callProcess";
		String site = "http://10.0.1.11:10005/login?user-id=SYSTEM&password=SYSTEM";
		Gson son = new Gson();
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

		System.out.println(site + son.toJson(para));

		try {
			System.out.println(c.get(site, 30));
//			c.get(site + son.toJson(para), 30);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String get(String site, int timeout) throws Exception {
		try {

			URL url = new URL(site);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(timeout * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			StringBuffer sb = new StringBuffer();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			conn.disconnect();

			return sb.toString();

		} catch (Exception e) {
			throw e;
		}

	}

	public String get(String site) throws Exception {
		return get(site, TIMEOUT_SEC);
	}

}

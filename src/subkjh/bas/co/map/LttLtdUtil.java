package subkjh.bas.co.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import subkjh.bas.co.log.Logger;

/**
 * 위경도 조회하는 유틸리티
 * 
 * @author subkjh(김종훈)
 *
 */
public class LttLtdUtil {

	public static void main(String[] args) {
		try {
//			Map<String, Object> map = new LttLtdUtil().callNaver("인천 연수구 송도문화로84번길 24");
//			{address=인천 남동구 구월동 505, ltt=37.4459576, ltd=126.7158608}
//			{address=인천 연수구 송도문화로84번길 24, ltt=37.3742871, ltd=126.6586552}

			Map<String, Object> map = new LttLtdUtil().callTMap("인천 연수구 송도문화로84번길 24");
//			address=인천 남동구 구월동 505, ltt=37.445003, ltd=126.709453}
//			{address=인천 연수구 송도문화로84번길 24, ltt=37.374312, ltd=126.660485}
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> callGoogle(String address) throws Exception {

		String s = URLEncoder.encode(address, "utf-8");
		// String site =
		// "https://maps.googleapis.com/maps/api/geocode/json?address=" + s
		// + "&key=AIzaSyAMVLJi3YvEW3TaqdLja0fQrc-f3KB29ro";

		String site = "https://maps.googleapis.com/maps/api/geocode/json?address=" + s
				+ "&key=AIzaSyBxtoi9pxMn5FcZnTk3wMlcx8TZp3ZSzY8";

		URL url = new URL(site);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Data-Type", "json");

		// OutputStream os = conn.getOutputStream();
		// // os.write(input.getBytes());
		// os.flush();

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

		Map<String, Object> map = new Gson().fromJson(sb.toString(), HashMap.class);

		Logger.logger.debug(map.toString());

		String status = (String) map.get("status");

		if ("ok".equalsIgnoreCase(status)) {
			try {
				List<Map<?, ?>> results = (List) map.get("results");
				if (results != null && results.size() > 0) {
					Map<?, ?> geometry = (Map) results.get(0).get("geometry");
					Map<?, ?> location = (Map) geometry.get("location");

					Object lat = location.get("lat");
					Object lng = location.get("lng");

					Map<String, Object> ret = new HashMap<String, Object>();

					ret.put("address", address);
					ret.put("ltt", Double.valueOf(lat.toString()));
					ret.put("ltd", Double.valueOf(lng.toString()));

					return ret;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if ("REQUEST_DENIED".equalsIgnoreCase(status)) {
			throw new Exception("Check API Configuration by subkjh");
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> callNaver(String address) throws IOException {

		String s = URLEncoder.encode(address, "utf-8");
		String site = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + s;

		URL url = new URL(site);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Data-Type", "json");

		conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "sk0kz94mq2");
		conn.setRequestProperty("X-NCP-APIGW-API-KEY", "yKfKv1iMVxIh9bO1f2KWDPZLgyTEdoflnmjPIzBD");

		// OutputStream os = conn.getOutputStream();
		// // os.write(input.getBytes());
		// os.flush();

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

		System.out.println(address + ":" + sb.toString());

		Map<String, Object> map = new Gson().fromJson(sb.toString(), HashMap.class);

		Logger.logger.debug(map.toString());

		String status = (String) map.get("status");
		//
		if ("ok".equalsIgnoreCase(status)) {
			try {

				List<Map<?, ?>> addresses = (List) map.get("addresses");
				if (addresses != null && addresses.size() > 0) {

					Object lat = addresses.get(0).get("y");
					Object lng = addresses.get(0).get("x");

					Map<String, Object> ret = new HashMap<String, Object>();

					ret.put("address", address);
					ret.put("ltt", Double.valueOf(lat.toString()));
					ret.put("ltd", Double.valueOf(lng.toString()));

					return ret;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

		return null;
	}

	private final String TMAP_KEY = "3bcf8d86-b82a-422b-83c2-a020d6a0d78d";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> callTMap(String address) throws IOException {

		String s = URLEncoder.encode(address, "utf-8");
		String site = "https://apis.openapi.sk.com/tmap/geo/fullAddrGeo?version=1&appKey=" + TMAP_KEY + "&fullAddr="
				+ s;

		URL url = new URL(site);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Data-Type", "json");

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		StringBuffer sb = new StringBuffer();
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}

		conn.disconnect();

		Map<String, Object> map = new Gson().fromJson(sb.toString(), HashMap.class);

		Logger.logger.debug(map.toString());

		map = (Map<String, Object>) map.get("coordinateInfo");
		//
		if (map != null) {
			try {

				List<Map<?, ?>> addresses = (List) map.get("coordinate");
				if (addresses != null && addresses.size() > 0) {

					Object lat = addresses.get(0).get("lat");
					Object lng = addresses.get(0).get("lon");

					if (lat == null || lat.toString().length() == 0) {
						lat = addresses.get(0).get("newLat");
						lng = addresses.get(0).get("newLon");
					}

					Map<String, Object> ret = new HashMap<String, Object>();

					ret.put("address", address);
					ret.put("ltt", Double.valueOf(lat.toString()));
					ret.put("ltd", Double.valueOf(lng.toString()));

					return ret;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

		return null;
	}

}

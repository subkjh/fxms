package com.fxms.nms.fxactor.ping.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class UrlGet {
	public static void main(String[] args) throws Exception {
		String site;

		UrlGet t = new UrlGet();

		site = "http://localhost:8080/fxms-rest-api/bio/fxapi/callProcess";	
		
		Gson son = new Gson();
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("className", "aaa");
		para.put("paraMeter", 111);
		
		
		System.out.println(t.post(site, son.toJson(para)));
	}

	

	public String post(String site, String input) throws Exception {
		try {

			URL url = new URL(site);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

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

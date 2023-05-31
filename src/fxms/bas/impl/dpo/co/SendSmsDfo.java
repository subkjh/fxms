package fxms.bas.impl.dpo.co;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import fxms.bas.api.FxApi;
import fxms.bas.exp.AttrNotFoundException;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.var.SelectVarDfo;
import fxms.bas.impl.dpo.var.SetVarDfo;
import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 *
 */
public class SendSmsDfo implements FxDfo<Void, Boolean> {

	private final String ID = "bizppurio.id";
	private final String PWD = "bizppurio.password";
	private final String TOKEN_URL = "token.url";
	private final String SEND_URL = "send.url";
	private final String REF_KEY = "ref.key";
	private final String FROM_TEL = "send.tel.num";

	void setVar() {

		SetVarDfo var = new SetVarDfo();
		Map<String, Object> datas = new HashMap<>();
		datas.put("varGrpName", "SMS");
		try {
			datas.put("varVal", "thingspire2");
			var.setVar(ID, datas);

			datas.put("varVal", "thingspire1507!");
			var.setVar(PWD, datas);

			datas.put("varVal", "https://dev-api.bizppurio.com/v1/token");
			datas.put("varVal", "https://api.bizppurio.com/v1/token");
			var.setVar(TOKEN_URL, datas);

			datas.put("varVal", "https://dev-api.bizppurio.com/v3/message");
			datas.put("varVal", "https://api.bizppurio.com/v3/message");
			var.setVar(SEND_URL, datas);

			datas.put("varVal", "thingspire");
			var.setVar(REF_KEY, datas);

			datas.put("varVal", "02-6265-1945");
			var.setVar(FROM_TEL, datas);

		} catch (Exception e1) {
		}
	}

	public static void main(String[] args) {

		SendSmsDfo dfo = new SendSmsDfo();
//		dfo.setVar();

		try {
			dfo.sendSms("010-4728-9295", "test 메시지");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Boolean call(FxFact fact, Void data) throws Exception {
		String telNo = fact.getString("telNo");
		String message = fact.getString("message");
		return sendSms(telNo, message);
	}

	private String get(Map<String, String> map, String name) throws AttrNotFoundException {
		String ret = map.get(name);
		if (ret == null) {
			throw new AttrNotFoundException(name);
		}
		return ret;
	}

	public boolean sendSms(String telNo, String message) throws Exception {

		Map<String, String> map = new SelectVarDfo().selectVarMap(FxApi.makePara("varGrpName", "SMS"));
		String id = get(map, ID);
		String pwd = get(map, PWD);
		String tokenUrl = get(map, TOKEN_URL);
		String sendUrl = get(map, SEND_URL);
		String refKey = get(map, REF_KEY);
		String fromTel = get(map, FROM_TEL);

		Data data = new Data(id, refKey, fromTel);
		data.to = telNo;
		data.addMessage(message);

//		System.out.println(id + "," + pwd + "," + tokenUrl);
//		System.out.println(FxmsUtil.toJson(data));

		return sendSms(id, pwd, tokenUrl, sendUrl, data);
	}

	private boolean sendSms(String id, String pwd, String tokenUrl, String sendUrl, Data data) throws Exception {

		/** 비즈뿌리오 id, password를 base64로 인코딩 **/
		String encodedString = (Base64.getEncoder().withoutPadding().encodeToString(id.getBytes()))
				+ (Base64.getEncoder().withoutPadding().encodeToString(pwd.getBytes()));
		StringBuilder endcodedBuffer = new StringBuilder(encodedString);
		endcodedBuffer.insert(15, 6);
		System.out.println("buf : " + endcodedBuffer);

		String token = null;
		StringBuilder tokenResult = new StringBuilder();
		StringBuilder messageResult = new StringBuilder();
		URL messageUrl = null;
		try {
			/** SSL 인증서 무시 : 비즈뿌리오 API 운영을 접속하는 경우 해당 코드 필요 없음 **/
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			/** 토큰 발급 url **/
			/** 토큰 발급 헤더 설정 **/
			HttpsURLConnection tokenUrlConn = (HttpsURLConnection) new URL(tokenUrl).openConnection();
			tokenUrlConn.setRequestMethod("POST");
			tokenUrlConn.addRequestProperty("Content-Type", "application/json");
			tokenUrlConn.addRequestProperty("Accept-Charset", "UTF-8");
			tokenUrlConn.addRequestProperty("Authorization", "Basic " + endcodedBuffer);
			tokenUrlConn.setDoInput(true); /** InputStream으로 서버로부터 응답을 받겠다는 옵션. **/
			tokenUrlConn.setDoOutput(true); /** OutputStream으로 POST 데이터를 넘겨주겠다는 옵션. **/
			tokenUrlConn.setUseCaches(false);
			tokenUrlConn.setConnectTimeout(15000);

			/** Response **/
			BufferedReader buf = new BufferedReader(new InputStreamReader(tokenUrlConn.getInputStream(), "UTF-8"));
			while ((token = buf.readLine()) != null) {
				tokenResult.append(token);
			}
			tokenUrlConn.disconnect();
			Logger.logger.debug(" Response : {}", tokenResult.toString());

			/** JSON 데이터 파싱 & 추출 **/
			Map<String, Object> obj = FxmsUtil.toMapFromJson(tokenResult.toString());
			Logger.logger.info("access token : {}", obj.get("accesstoken")); // 성공
			String tokenValue = obj.get("accesstoken").toString();

			/** 메세지 전송 url **/
			messageUrl = new URL(sendUrl);
			/** messageSend 헤더 설정 **/
			HttpsURLConnection messageSend = (HttpsURLConnection) messageUrl.openConnection();
			messageSend.setRequestMethod("POST");
			messageSend.addRequestProperty("Content-Type", "application/json");
			messageSend.addRequestProperty("Accept-Charset", "UTF-8");
			messageSend.addRequestProperty("Authorization", "Bearer " + tokenValue);
			messageSend.setDoInput(true);
			messageSend.setDoOutput(true);
			messageSend.setUseCaches(false);
			messageSend.setConnectTimeout(15000);

			/** Request **/
			OutputStream os = messageSend.getOutputStream();
			/**
			 * bizppurio 계정ID, 발신번호, 수신번호. ※발신번호는 bizppurio 계정에 등록된 번호만 사용할 수 있음
			 **/

//			String sms = String.format(
//					"{\"account\":\"%s\",\"refkey\":\"%s\"," + "\"type\":\"sms\",\"from\":\"%s\",\"to\":\"%s\","
//							+ "\"content\":{\"sms\":{\"message\":\"[ %s ]\"}}}",
//					id, refKey, sendTelNum, telNo, message);

			String sms = FxmsUtil.toJson(data);
			System.out.println(sms);

			os.write(sms.getBytes("UTF-8"));
			os.flush();

			/** Response **/
			BufferedReader in = new BufferedReader(new InputStreamReader(messageSend.getInputStream(), "UTF-8"));
			String response;
			while ((response = in.readLine()) != null) {
				messageResult.append(response);
			}
			messageSend.disconnect();
			Logger.logger.info("messageSend Response : {}", messageResult.toString());

			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	class Data {
		final String account;
		final String refKey;
		final String type = "sms";
		final String from;
		String to;
		Map<String, Object> content = new HashMap<>();

		public Data(String account, String refKey, String from) {
			this.account = account;
			this.refKey = refKey;
			this.from = from;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		void addMessage(String message) {
			Object val = content.get("sms");
			if (val == null) {
				List<String> list = new ArrayList<>();
				content.put("sms", list);
				list.add(message);
			} else if (val instanceof List) {
				((List) val).add(message);
			}
		}
	}
}
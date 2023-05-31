package fxms.rule.action.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * 
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "URL호출", descr = "URL을 호출하여 결과를 payload에 추가한다.")
public class UrlAction extends FxRuleActionImpl {

	@FxAttr(text = "호출 URL", description = "호출할 URL을 입력한다.<br>예: http://www.thingspire.com")
	private String url;

	@FxAttr(text = "호출 방식", description = "GET, POST중 하나를 입력한다.")
	private String method;

	@FxAttr(text = "호출 인자", description = "호출에 사용할 인자를 입력한다.", required = false)
	private String para;

	@FxAttr(text = "변수명", description = "결과를 넣을 변수를 입력한다.")
	private String var;

	@FxAttr(text = "응답번호변수", description = "응답번호를 넣을 변수를 입력한다.", required = false)
	private String responseno;

	public UrlAction(Map<String, Object> map) throws Exception {
		super(map);

		if (var == null) {
			var = "url.body";
		}

		if (responseno == null) {
			responseno = "url.responseno";
		}
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		String site;
		if ("get".equalsIgnoreCase(method)) {
			site = url + (para != null ? "?" + para : "");
		} else {
			site = url;
		}

		URL url = new URL(site);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5 * 1000);
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Data-Type", "json");

		if ("get".equalsIgnoreCase(method)) {
			conn.setRequestMethod("GET");
		} else {
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			OutputStream os = conn.getOutputStream();
			if (para != null)
				os.write(para.getBytes());
			os.flush();
		}

		// 응답코드 넣기
		fact.addPayload(responseno, conn.getResponseCode());

		// 오류이면 무시
		if (conn.getResponseCode() != 200) {
//			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			return;
		}

		// 결과 읽어오기
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringBuffer sb = new StringBuffer();
		String output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
		conn.disconnect();

		// 결과 payload에 넣기
		fact.addPayload(var, sb.toString());

	}

}
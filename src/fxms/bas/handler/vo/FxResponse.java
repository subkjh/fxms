package fxms.bas.handler.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;

public class FxResponse {

	public enum HttpStatusCode {

		OK(200, "성공"),

		BadRequest(400, "요청 형식 오류(잘못된 파라미터 입력)"),

		Unauthorized(401, "미승인 서비스(Access Token or key 가 없음)"),

		Forbidden(403, "허용되지 않는 Access Token 사용(기간이 만료되었거나 알수 없는 Token)"),

		NotFound(404, "해당 리소스가 존재하지 않음"),

		InternalServerError(500, "서버 내부 에러")

		;

		private int code;

		private HttpStatusCode(int code, String message) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("aaaaaaa", "대한민국");
		map.put("list", new ArrayList<>());
		map.put("map2", map2);
		FxResponse a = new FxResponse(HttpStatusCode.OK, null, map);

		String json = FxmsUtil.toJson(a);

		System.out.println(json);

		FxResponse b = FxmsUtil.toObject(json, FxResponse.class);

		System.out.println(FxmsUtil.toJson(b));
		Map<String, Object> retMap = (Map<String, Object>) b.getDatas();
		System.out.println(retMap.get("list").getClass().getName());
		System.out.println(retMap.get("map2").getClass().getName());
	}

	/**
	 * 1xx (정보): 요청을 받았으며 프로세스를 계속한다 <br>
	 * 2xx (성공): 요청을 성공적으로 받았으며 인식했고 수용하였다<br>
	 * 3xx (리다이렉션): 요청 완료를 위해 추가 작업 조치가 필요하다<br>
	 * 4xx (클라이언트 오류): 요청의 문법이 잘못되었거나 요청을 처리할 수 없다<br>
	 * 5xx (서버 오류): 서버가 명백히 유효한 요청에 대해 충족을 실패했다<br>
	 */
	private final int code;

	private final String message;

	private final Object datas;

	private final long timeMillis;
	

	public FxResponse(HttpStatusCode status, String message, Object datas) {
		this.code = status.getCode();
		this.message = message;
		this.datas = datas;
		this.timeMillis = System.currentTimeMillis();
	}

	public FxResponse(Map<String, Object> response) throws Exception {
		this.code = ((Number) response.get("code")).intValue();
		this.message = response.get("message") == null ? null : response.get("message").toString();
		this.datas = response.get("datas");
		this.timeMillis = System.currentTimeMillis();
	}

	@SuppressWarnings("rawtypes")
	public Object get(String name) {
		if (datas instanceof Map) {
			return ((Map) datas).get(name);
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public Object getDatas() {
		return datas;
	}

	public String getMessage() {
		return message;
	}

	public long getTimeMillis() {
		return timeMillis;
	}

	@Override
	public String toString() {
		return FxmsUtil.toJson(this);
	}
}

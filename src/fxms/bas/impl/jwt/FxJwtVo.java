package fxms.bas.impl.jwt;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxCfg;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import subkjh.bas.co.lang.Lang;

/**
 *
 * HEADER:ALGORITHM & TOKEN TYPE<br>
 * {<br>
 * "alg": "HS512"<br>
 * }<br>
 * <br>
 * 
 * PAYLOAD:DATA<br>
 * {<br>
 * "sub": "subkjh",<br>
 * "auth": "ROLE_ADMIN",<br>
 * "exp": 1672219352,<br>
 * "userNo": 2,<br>
 * "userId": "subkjh",<br>
 * "userName": "김종훈",<br>
 * "inloNo": 1,<br>
 * "ugrpNo": 2,<br>
 * "userMail": "subkjh@naver.com"<br>
 * }<br>
 * 
 * VERIFY SIGNATURE<br>
 * MACSHA512( base64UrlEncode(header) + "." + base64UrlEncode(payload),
 * 
 * c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK
 * 
 * )
 * 
 * 
 * @author subkjh
 *
 */

public class FxJwtVo {

	public static void main(String[] args) {
		String jwtSecret = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";

		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJramgiLCJhdXRoIjoiUk9MRV9BRE1JTiIsImV4cCI6MTY3MjIxOTM1MiwidXNlck5vIjoyLCJ1c2VySWQiOiJzdWJramgiLCJ1c2VyTmFtZSI6Iuq5gOyihe2biCIsImlubG9ObyI6MSwidWdycE5vIjoyLCJ1c2VyTWFpbCI6InN1YmtqaEBuYXZlci5jb20ifQ.Z_v0Yz48DlXmI69o_A1Y-Lu60n6rVvDXCFABhmFS5hDB3aSeFErWghbO9XxsVoz8bmWJILrEPnFKq9jNqBxeXA";
		FxJwtVo vo;
		try {
			vo = new FxJwtVo(token);
			System.out.println(vo.getPayload());
			System.out.println(System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String jwtSecret;
	private String token;
	private Map<String, Object> payloadMap;

	public String getSessionId() {
		Object value = payloadMap.get("sessionId");
		return value == null ? null : value.toString();
	}

	public String getHostName() {
		Object value = payloadMap.get("hostName");
		return value == null ? null : value.toString();
	}
	
	public String getUserId() {
		Object value = payloadMap.get("userId");
		return value == null ? null : value.toString();
	}

	public int getUserNo() {
		Object value = payloadMap.get("userNo");
		try {
			return value == null ? -1
					: value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
		} catch (Exception e) {
			return -1;
		}
	}

	public Map<String, Object> getPayload() {
		return payloadMap;
	}

	public FxJwtVo(String token) throws Exception {

		this.token = token;
		this.jwtSecret = FxCfg.get(FX_PARA.jwtSecret, (String) null);

		check();

	}

	@SuppressWarnings("unchecked")
	public void check() throws Exception {

		try {
			if (jwtSecret != null) {
				Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
			}

			String[] chunks = token.split("\\.");
			Base64.Decoder decoder = Base64.getDecoder();
			String payload = new String(decoder.decode(chunks[1]));
			ObjectMapper mapper = new ObjectMapper();

			try {
				payloadMap = mapper.readValue(payload, Map.class);
			} catch (Exception e) {
				throw new Exception(Lang.get("Error during PAYLOAD analysis"));
			}

		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			throw new Exception(Lang.get("Invalid JWT signature."));
		} catch (ExpiredJwtException e) {
			throw new Exception(Lang.get("An expired JWT token."));
		} catch (UnsupportedJwtException e) {
			throw new Exception(Lang.get("JWT token not supported."));
		} catch (IllegalArgumentException e) {
			throw new Exception(Lang.get("JWT token is invalid."));
		} catch (Exception e) {
			throw e;
		}
	}

	public String getToken() {
		return this.token;
	}
}

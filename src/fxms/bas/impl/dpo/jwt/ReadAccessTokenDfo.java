package fxms.bas.impl.dpo.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import subkjh.bas.co.lang.Lang;

/**
 * Java Web Token(JWT)은 클라이언트 측에서 발급받은 토큰을 서버에서 검증하여 인증하는 방식입니다.<br>
 * Refresh Token은 만료된 Access Token을 갱신하는 데 사용됩니다.<br>
 * 
 * Refresh Token을 사용하는 방법은 다음과 같습니다.<br>
 * 
 * 1. 클라이언트가 인증을 받아 Access Token과 Refresh Token을 발급받습니다.<br>
 * 2. Access Token이 만료되면 클라이언트는 서버에 Refresh Token을 보내 갱신된 Access Token을
 * 요청합니다.<br>
 * 3. 서버는 Refresh Token을 검증하고, 유효한 경우 새로운 Access Token을 발급해 클라이언트에게 응답합니다.<br>
 * 4. 클라이언트는 새로운 Access Token을 사용하여 요청을 다시 보냅니다.<br>
 * 
 * @author subkjh
 *
 */
public class ReadAccessTokenDfo implements FxDfo<String, Map<String, Object>> {

	public static void main(String[] args) {
		ReadAccessTokenDfo dfo = new ReadAccessTokenDfo();
		try {
			Map<String, Object> ret = dfo.read(
					"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmeG1zIiwiZXhwIjoxNjg0MjM2Mjk5LCJhdXRoIjoiUk9MRV9VU0VSIiwidXNlck5vIjoxMDQyLCJ1c2VySWQiOiJ0ZXN0IiwidXNlck5hbWUiOiJ0ZXN0IiwiaW5sb05vIjowLCJ1Z3JwTm8iOjEwMDAsInVzZXJNYWlsIjoidGVzdCIsInNlc3Npb25JZCI6IkZYTVMxMzY0MzAwNjAwMDAxIiwiaG9zdG5hbWUiOiJlYzItMy0zNS0yMzAtMzIuYXAtbm9ydGhlYXN0LTIuY29tcHV0ZS5hbWF6b25hd3MuY29tIn0.chqE2FPPeQ-6iQc7RNRpvqBeSIS9NzAnkMaZ-MnWv8zSqGyqhB6vRuGO8f-IqHfqec5KDAMVazt22fu3aNfg6A");
			System.out.println(FxmsUtil.toJson(ret));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> call(FxFact fact, String accessToken) throws Exception {
		return read(accessToken);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> read(String accessToken) throws Exception {
		
		if ( accessToken == null) {
			throw new Exception(Lang.get("An error occurred while parsing the token."));
		}

		String[] check = accessToken.split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		String payload = new String(decoder.decode(check[1]));

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(payload, Map.class);
		} catch (Exception e) {
			throw new Exception(Lang.get("An error occurred while parsing the token."));
		}
	}
}

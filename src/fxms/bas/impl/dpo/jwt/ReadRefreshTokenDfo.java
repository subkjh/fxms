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
import io.jsonwebtoken.security.SignatureException;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.utils.DateUtil;

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
public class ReadRefreshTokenDfo implements FxDfo<String, Map<String, Object>> {

	public static void main(String[] args) {
		String refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2ODQyMzI4ODMsInNlc3Npb25JZCI6IkZYTVMxMzY0MzAwNzUwMDAxIn0.odhqAjRdbbQSTj4TIL_JKqLJvyUqK0gFra5Q2dG6GjMPTcBfgc46RU0Q44wPreZwUNeRBURl4GGNII09k1HNBQ";
		ReadRefreshTokenDfo dfo = new ReadRefreshTokenDfo();
		try {
			Map<String, Object> map = dfo.read(refreshToken);

			System.out.println(FxmsUtil.toJson(map));
			System.out.println(DateUtil.getDtm(((Number) map.get("exp")).longValue() * 1000L));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> call(FxFact fact, String refreshToken) throws Exception {
		return read(refreshToken);
	}

//	private Key getRefreshTokenSecretKey() {
//		byte[] keyBytes = Decoders.BASE64.decode(MakeRefreshTokenDfo.REFRESH_TOKEN_SECRET_KEY);
//		Key key = Keys.hmacShaKeyFor(keyBytes);
//		return key;
//	}
//
//	public Map<String, Object> checkToken(String refreshToken) throws Exception {
//		
//		try {
//			Jwts.parserBuilder().setSigningKey(getRefreshTokenSecretKey()).build().parseClaimsJws(refreshToken);
//
//		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
//				| IllegalArgumentException ex) {
//			throw ex;
//		}
//
//		return read(refreshToken);
//
//	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> read(String refreshToken) throws Exception {

		String[] check = refreshToken.split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		String payload = new String(decoder.decode(check[1]));

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(payload, Map.class);
		} catch (Exception e) {
			throw new Exception(Lang.get("Error during PAYLOAD analysis"));
		}
	}
}

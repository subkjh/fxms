package fxms.bas.impl.dpo.jwt;

import java.security.Key;
import java.util.Date;

import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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
public class MakeRefreshTokenDfo implements FxDfo<SessionVo, String> {

	private final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일
	public static final String REFRESH_TOKEN_SECRET_KEY = "UVcGqWDFKL0g3YXJFdTmeZSnCSDNymmiw8tsCSc0o1zwmXGmZWHMxEU34KF/mZb10oqhTTsvpBHJEI+1lvd3Dw==";

	@Override
	public String call(FxFact fact, SessionVo session) throws Exception {

		String token = generateRefreshToken(session);

		return token;
	}

	private Key getRefreshTokenSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(REFRESH_TOKEN_SECRET_KEY);
//		byte[] keyBytes = Decoders.BASE64URL.decode(REFRESH_TOKEN_SECRET_KEY);
		Key key = Keys.hmacShaKeyFor(keyBytes);
		return key;
	}

	public String generateRefreshToken(SessionVo session) {
		return Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME)) //
				.claim("sessionId", session.getSessionId()) //
				.signWith(getRefreshTokenSecretKey(), SignatureAlgorithm.HS512).compact();
	}

}

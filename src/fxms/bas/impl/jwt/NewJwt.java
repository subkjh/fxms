package fxms.bas.impl.jwt;

import java.security.Key;
import java.util.Date;

import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Java Web Token(JWT)은 클라이언트 측에서 발급받은 토큰을 서버에서 검증하여 인증하는 방식입니다. Refresh Token은
 * 만료된 Access Token을 갱신하는 데 사용됩니다.<br>
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
public class NewJwt {
	private static final String AUTHORITIES_KEY = "auth";
	private static final String BEARER_TYPE = "bearer";
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2; // 60분
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
	private String jwtSecret;
	private long nowTime;

	public static void main(String[] args) {
		new NewJwt();
	}

	public NewJwt() {
		this.nowTime = (new Date()).getTime();
	}

	public String makeAccessToken(SessionVo user) {

		this.jwtSecret = FxCfg.get(FX_PARA.jwtSecret, (String) null);

		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		// Access Token 생성
		Date accessTokenExpiresIn = new Date(this.nowTime + ACCESS_TOKEN_EXPIRE_TIME);
		String accessToken = Jwts.builder().setSubject("fxms") // payload "sub": "name"
				.setExpiration(accessTokenExpiresIn) // payload "exp": 1516239022 (예시)
				.claim("auth", user.getAuthority()).claim("userNo", user.getUserNo()).claim("userId", user.getUserId())
				.claim("userName", user.getUserName()).claim("inloNo", user.getInloNo())
				.claim("ugrpNo", user.getUgrpNo()).claim("userMail", user.getUserId())
				.claim("sessionId", user.getSessionId()).claim("hostname", user.getHostname())
				.signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512"
				.compact();

		return accessToken;
	}

	public String makeRefreshToken() {

		this.jwtSecret = FxCfg.get(FX_PARA.jwtSecret, (String) null);

		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		// Refresh Token 생성
		String refreshToken = Jwts.builder().setExpiration(new Date(this.nowTime + REFRESH_TOKEN_EXPIRE_TIME))
				.signWith(key, SignatureAlgorithm.HS512).compact();

		return refreshToken;
	}

}

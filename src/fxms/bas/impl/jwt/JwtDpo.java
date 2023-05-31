package fxms.bas.impl.jwt;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import fxms.bas.handler.vo.SessionVo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

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
public class JwtDpo {

	private static final String jwtSecret = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";
	private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2; // 60분
//	private final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일
	private final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 5;

	public static void main(String[] args) {

		System.out.println(jwtSecret.length());
		System.out.println(generateSecretKey());

		JwtDpo dpo = new JwtDpo();

		String refreshToken = dpo.generateRefreshToken();
		System.out.println(refreshToken.length());
		try {
			// Thread.sleep(10000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dpo.validateRefreshToken(refreshToken));
	}

	private Key getRefreshTokenSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		Key key = Keys.hmacShaKeyFor(keyBytes);
		return key;
	}

	public String generateRefreshToken() {
		return Jwts.builder().setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
				.signWith(getRefreshTokenSecretKey(), SignatureAlgorithm.HS512).compact();
	}

	public boolean validateRefreshToken(String refreshToken) {
		try {
			Jwts.parserBuilder().setSigningKey(getRefreshTokenSecretKey()).build().parseClaimsJws(refreshToken);
			return true;
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public String generateAccessToken(SessionVo user) {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		// Access Token 생성
		Date accessTokenExpiresIn = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);
		String accessToken = Jwts.builder().setSubject("fxms") // payload "sub": "name"
				.setExpiration(accessTokenExpiresIn) // payload "exp": 1516239022 (예시)
				.claim("auth", user.getAuthority()) //
				.claim("userNo", user.getUserNo()) //
				.claim("userId", user.getUserId()) //
				.claim("userName", user.getUserName()) //
				.claim("inloNo", user.getInloNo()) //
				.claim("ugrpNo", user.getUgrpNo()) //
				.claim("userMail", user.getUserId()) //
				.claim("sessionId", user.getSessionId()) //
				.claim("hostname", user.getHostname()) //
				.signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512"
				.compact();

		return accessToken;
	}

	/**
	 * REFRESH_TOKEN_SECRET_KEY는 안전한 임의의 문자열을 사용하여 생성합니다. 이 비밀키는 서버 측에서만 알고 있어야 하며,
	 * 클라이언트와 공유되지 않도록 해야 합니다.<br>
	 * 위 코드에서는 SecureRandom 클래스를 사용하여 64바이트의 임의의 바이트 배열을 생성하고, Base64 인코딩을 사용하여 문자열로
	 * 변환합니다.<br>
	 * 이렇게 생성된 문자열은 REFRESH_TOKEN_SECRET_KEY로 사용할 수 있습니다.
	 * 
	 * @return
	 */
	public static String generateSecretKey() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] secretKeyBytes = new byte[64];
		secureRandom.nextBytes(secretKeyBytes);
		return Base64.getEncoder().encodeToString(secretKeyBytes);
	}
}

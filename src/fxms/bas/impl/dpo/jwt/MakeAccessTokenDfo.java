package fxms.bas.impl.dpo.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import subkjh.bas.co.user.User.USER_TYPE_CD;

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
public class MakeAccessTokenDfo implements FxDfo<SessionVo, String> {

	public static void main(String[] args) {
		SessionVo session = new SessionVo("AAA", 1, "홍길동", "test", USER_TYPE_CD.Operator, 0, 0);

		MakeAccessTokenDfo dpo = new MakeAccessTokenDfo();
		ReadAccessTokenDfo dpo2 = new ReadAccessTokenDfo();
		try {
			String token = dpo.makeAccessToken(session);
			System.out.println(token);
			Map<String, Object> payload = dpo2.read(token);
			System.out.println(FxmsUtil.toJson(payload));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2; // 2시간
	private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일
	public static final String ACCESS_TOKEN_SECRET_KEY = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK";

	@Override
	public String call(FxFact fact, SessionVo session) throws Exception {

		String token = makeAccessToken(session);

		return token;
	}

	public String makeAccessToken(SessionVo user) {

//		byte[] keyBytes = Decoders.BASE64URL.decode(ACCESS_TOKEN_SECRET_KEY);
		byte[] keyBytes = Decoders.BASE64.decode(ACCESS_TOKEN_SECRET_KEY);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		user.setAccessTokenExpiresIn(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);

		Date accessTokenExpiresIn = new Date(user.getAccessTokenExpiresIn());

		// Access Token 생성
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
}

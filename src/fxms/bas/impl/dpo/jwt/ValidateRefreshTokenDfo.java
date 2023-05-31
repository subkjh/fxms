package fxms.bas.impl.dpo.jwt;

import java.security.Key;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
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
public class ValidateRefreshTokenDfo implements FxDfo<String, Boolean> {

	@Override
	public Boolean call(FxFact fact, String refreshToken) throws Exception {

		boolean bRet = validateRefreshToken(refreshToken);

		return bRet;
	}

	private Key getRefreshTokenSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(MakeRefreshTokenDfo.REFRESH_TOKEN_SECRET_KEY);
		Key key = Keys.hmacShaKeyFor(keyBytes);
		return key;
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

}

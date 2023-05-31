package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.user.User;
import subkjh.bas.co.user.exception.UserNotFoundException;
import subkjh.dao.ClassDaoEx;

/**
 * 
 * @author subkjh
 *
 */
public class UserSelectDfo implements FxDfo<String, User> {

	public static void main(String[] args) {
		UserSelectDfo dfo = new UserSelectDfo();
		try {
			dfo.select(111, "kkk");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public User call(FxFact fact, String userId) throws Exception {
		return select(null, userId);
	}

	public User select(Integer userNo, String userId) throws Exception {

		Map<String, Object> para = FxApi.makePara("useYn", "Y");
		if (userNo != null) {
			para.put("userNo", userNo);
		}
		if (userId != null) {
			para.put("userId", userId);
		}

		FX_UR_USER user = ClassDaoEx.SelectData(FX_UR_USER.class, para);

		if (user == null) {
			throw new UserNotFoundException(userId);
		}

		return new User(user.getUserId(), user.getUserNo(), user.getUserName(), user.getUgrpNo(), user.getUserTypeCd(),
				user.getInloNo());
	}

	public User select(String userId) throws Exception {
		return select(null, userId);
	}
}
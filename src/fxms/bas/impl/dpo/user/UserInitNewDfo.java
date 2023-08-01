package fxms.bas.impl.dpo.user;

import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.user.User;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

/**
 * 운용자의 암호를 변경하는 함수
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserInitNewDfo implements FxDfo<FX_UR_USER, FX_UR_USER> {

	@Override
	public FX_UR_USER call(FxFact fact, FX_UR_USER user) throws Exception {
		return initUser(user);
	}

	public FX_UR_USER initUser(FX_UR_USER user) throws Exception {

		int userNo = ClassDaoEx.GetNextVal(FX_UR_USER.FX_SEQ_USERNO, Integer.class);
		FxTableMaker.initRegChg(userNo, user);
		user.setUserNo(userNo);
		user.setUserPwd(User.encodingPassword(user.getUserPwd()));
		return user;

	}
}

package fxms.bas.impl.dpo.user;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.user.User;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.bas.co.user.exception.UserNotFoundException;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class LoginDfo implements FxDfo<Void, SessionVo> {

	@Override
	public SessionVo call(FxFact fact, Void data) throws Exception {

		String userId = fact.getString("userId");
		String password = fact.getString("password");
		String host = fact.getString("host");
		String media = fact.getString("media");

		return login(userId, password, host, media);
	}

	private static long sessionId = 0;

	private static synchronized String getNextSessionId() {
		if (sessionId == 0) {
			sessionId = (System.currentTimeMillis() / 12345) * 10000;
		}

		++sessionId;

		return "FXMS" + sessionId;
	}

	public SessionVo login(String userId, String password, String ipaddr, String media)
			throws UserNotFoundException, Exception {

		String sessionId = getNextSessionId();

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userId", userId);

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			FX_UR_USER user = tran.selectOne(FX_UR_USER.class, para);

			if (user == null) {
				throw new UserNotFoundException(userId);
			}

			if (user.isUseYn() == false) {
				throw new UserNotFoundException("ID is disabled");
			}

			if (user.getUserPwd().equals(User.encodingPassword(password)) == false) {
				throw new UserNotFoundException("password incorrect");
			}

			int ymd = DateUtil.getYmd(System.currentTimeMillis());
			if ((ymd >= user.getUseStrtDate() && ymd <= user.getUseEndDate()) == false) {
				throw new UserNotFoundException(userId + " use expired");
			}

			SessionVo session = new SessionVo(sessionId, user.getUserNo(), user.getUserId(), user.getUserName(),
					USER_TYPE_CD.get(user.getUserTypeCd()), user.getUgrpNo(), user.getInloNo());
			session.setAuthority(user.getAuthority());
			session.setHostname(ipaddr);
			return session;

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

	}
}

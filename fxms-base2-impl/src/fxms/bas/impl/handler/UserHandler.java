package fxms.bas.impl.handler;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.api.FxApi;
import fxms.bas.co.FxConfDao;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.co.DeleteUserDbo;
import fxms.bas.impl.co.ProcessNewUserDbo;
import fxms.bas.impl.co.UserGroupGetDbo;
import fxms.bas.impl.co.UserSimpleGetDbo;
import fxms.bas.impl.dbo.FX_UI_BASIC;
import fxms.bas.impl.dbo.FX_UI_GROUP;
import fxms.bas.impl.dbo.FX_UI_PROPERTY;
import fxms.bas.impl.dbo.FX_UR_NEW;
import fxms.bas.impl.dbo.FX_UR_PROPERTY;
import fxms.bas.impl.dbo.FX_UR_UGRP;
import fxms.bas.impl.dbo.FX_UR_UGRP_REL;
import fxms.bas.impl.dbo.FX_UR_USER;
import fxms.bas.impl.handler.func.UserPwdChange;
import fxms.bas.impl.handler.func.UserUiSet;
import fxms.module.restapi.CommHandler;
import fxms.module.restapi.vo.SessionVo;

public class UserHandler extends CommHandler {

	public static void main(String[] args) {
		try {
			System.out.println(User.encodingPassword("guest"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object addUser(SessionVo session, Map<String, Object> parameters) throws Exception {
		FX_UR_USER user = new FX_UR_USER();
		ObjectUtil.toObject(parameters, user);
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			addUser(tran, user, session.getUserNo());
			tran.commit();

			return user;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object addUserGroup(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_UR_UGRP ugrp = new FX_UR_UGRP();
		ugrp.setUgrpDescr(getString(parameters, "userGroupDescr"));
		ugrp.setUgrpName(getString(parameters, "userGroupName"));
		ugrp.setReservedYn(false);
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			int ugrpNo = tran.getNextVal(FX_UR_UGRP.FX_SEQ_UGRPNO, Integer.class);
			ugrp.setRegDate(FxApi.getDate(0));
			ugrp.setRegUserNo(session.getUserNo());
			ugrp.setChgDate(ugrp.getRegDate());
			ugrp.setChgUserNo(ugrp.getRegUserNo());
			ugrp.setUgrpNo(ugrpNo);

			tran.insertOfClass(FX_UR_UGRP.class, ugrp);

			FX_UR_UGRP_REL rel = new FX_UR_UGRP_REL();
			rel.setRegDate(ugrp.getRegDate());
			rel.setRegUserNo(ugrp.getRegUserNo());
			rel.setRelUgrpNo(User.USER_GROUP_ALL);
			rel.setUgrpNo(ugrp.getUgrpNo());
			tran.insertOfClass(FX_UR_UGRP_REL.class, rel);

			tran.commit();
			return ugrp;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object applyNewUser(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_UR_NEW user = new FX_UR_NEW();
		ObjectUtil.toObject(parameters, user);

		user.setProcessState("P");
		user.setRegDate(FxApi.getDate());

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.insertOfClass(FX_UR_NEW.class, user);
			tran.commit();
			return user;
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object changePwd(SessionVo session, Map<String, Object> parameters) throws Exception {

		String oldPasswd = getString(parameters, "oldPasswd");
		String newPasswd = getString(parameters, "newPasswd");
		String newPasswdChk = getString(parameters, "newPasswdChk");

		return new UserPwdChange().change(session.getUserId(), oldPasswd, newPasswd, newPasswdChk);

	}

	public Object deleteUser(SessionVo session, Map<String, Object> parameters) throws Exception {

		DeleteUserDbo user = new DeleteUserDbo();
		ObjectUtil.toObject(parameters, user);
		FxConfDao.getDao().deleteOfObject(user);
		return user;
	}

	public Object deleteUserGroup(SessionVo session, Map<String, Object> parameters) throws Exception {

		int userGroupNo = getInt(parameters, "userGroupNo");

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> wherePara = new HashMap<String, Object>();
			wherePara.put("ugrpNo", userGroupNo);

			List<FX_UR_USER> userList = tran.select(FX_UR_USER.class, wherePara);
			if (userList.size() > 0) {
				throw new Exception("운용자그룹에 속한 운용자가 존재하여 삭제할 수 없습니다. 운용자를 먼저 삭제해야 합니다.");
			}

			tran.deleteOfClass(FX_UR_UGRP_REL.class, null, wherePara);
			tran.deleteOfClass(FX_UR_UGRP.class, null, wherePara);
			tran.commit();

			return wherePara;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 현재 로그인 상태인 운용자 수를 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getLoginedUserCount(SessionVo session, Map<String, Object> parameters) throws Exception {

		DbTrans tran = getTran();

		try {
			tran.start();
			int cnt = tran.selectQidInt("SELECT_COUNT_LOGIN_USERS", null, 0);
			Map<String, Integer> ret = new HashMap<String, Integer>();
			ret.put("CNT", cnt);
			return ret;
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object getUser(SessionVo session, Map<String, Object> parameters) throws Exception {
		String userId = getString(parameters, "userId");
		return FxConfDao.getDao().selectOne(FX_UR_USER.class, FxConfDao.makePara("userId", userId));
	}

	public Object getUserGroupList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(UserGroupGetDbo.class, null);
	}

	public Object getUserList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(UserSimpleGetDbo.class, null);
	}

	public Object getUserProperties(SessionVo session, Map<String, Object> parameters) throws Exception {

		parameters.clear();
		parameters.put("userNo", session.getUserNo());

		List<FX_UR_PROPERTY> list = FxConfDao.getDao().select(FX_UR_PROPERTY.class, parameters);
		Map<String, Object> ret = new HashMap<String, Object>();
		for (FX_UR_PROPERTY e : list) {
			ret.put(e.getPropertyName(), e.getPropertyValue());
		}

		return ret;
	}

	public Object getUserUi(SessionVo session, Map<String, Object> parameters) throws Exception {

		parameters.clear();
		parameters.put("userNo", session.getUserNo());

		Map<String, Object> ret = new HashMap<String, Object>();
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			List<FX_UI_GROUP> groupList = tran.select(FX_UI_GROUP.class, parameters);

			// 자신의 화면이 없으면 공용화면을 제공한다.
			if (groupList.size() == 0) {
				parameters.put("userNo", 0);
				groupList = tran.select(FX_UI_GROUP.class, parameters);
			}

			List<FX_UI_BASIC> basicList = tran.select(FX_UI_BASIC.class, parameters);
			ret.put("property-list", tran.select(FX_UI_PROPERTY.class, parameters));
			ret.put("basic-list", basicList);
			ret.put("group-list", groupList);
			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 신규 운용자가 요청한 내용을 승인/거절을 처리한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object processNewUser(SessionVo session, Map<String, Object> parameters) throws Exception {

		ProcessNewUserDbo dbo = new ProcessNewUserDbo();
		ObjectUtil.toObject(parameters, dbo);
		dbo.setProcessUserNo(session.getUserNo());
		dbo.setProcessDate(FxApi.getDate());

		FX_UR_USER user = new FX_UR_USER();
		ObjectUtil.toObject(parameters, user);
		user.setUserPasswd(user.getUserId() + "_fxms12#$");

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			tran.updateOfClass(dbo.getClass(), dbo, null);

			// 승인된 경우 운용자를 추가한다.
			if ("A".equals(dbo.getProcessState())) {
				addUser(tran, user, session.getUserNo());
			}

			tran.commit();

			return dbo;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object setUserProperty(SessionVo session, Map<String, Object> parameters) throws Exception {

		List<FX_UR_PROPERTY> list = new ArrayList<FX_UR_PROPERTY>();
		Object value;
		FX_UR_PROPERTY property;
		long chgDate = FxApi.getDate(System.currentTimeMillis());

		for (String key : parameters.keySet()) {
			value = parameters.get(key);
			if (value != null) {
				property = new FX_UR_PROPERTY();
				property.setChgDate(chgDate);
				property.setPropertyName(key);
				property.setPropertyValue(String.valueOf(value));
				property.setUserNo(session.getUserNo());
				list.add(property);
			}
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			for (FX_UR_PROPERTY p : list) {
				tran.deleteOfClass(FX_UR_PROPERTY.class, p, null);
				tran.insertOfClass(FX_UR_PROPERTY.class, p);
			}

			tran.commit();

			return list;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object setUserUi(SessionVo session, Map<String, Object> parameters) throws Exception {
		new UserUiSet(session, parameters);
		return getUserUi(session, parameters);
	}

	public Object updateUser(SessionVo session, Map<String, Object> parameters) throws Exception {

		parameters.put("chgDate", FxApi.getDate(0));
		parameters.put("chgUserNo", session.getUserNo());

		FxConfDao.getDao().updateOfClass(FX_UR_USER.class, parameters);

		return parameters;

	}

	public Object updateUserGroup(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_UR_UGRP ugrp = new FX_UR_UGRP();
		ugrp.setUgrpDescr(getString(parameters, "userGroupDescr"));
		ugrp.setUgrpName(getString(parameters, "userGroupName"));
		ugrp.setUgrpNo(getInt(parameters, "userGroupNo"));
		ugrp.setReservedYn(false);
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			ugrp.setChgDate(FxApi.getDate(0));
			ugrp.setChgUserNo(session.getUserNo());

			tran.updateOfClass(FX_UR_UGRP.class, ugrp, null);

			tran.commit();
			return ugrp;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	private void addUser(FxDaoExecutor tran, FX_UR_USER user, int regUserNo) throws Exception {

		int userNo = tran.getNextVal(FX_UR_USER.FX_SEQ_USERNO, Integer.class);

		user.setUserNo(userNo);
		user.setRegUserNo(regUserNo);
		user.setRegDate(FxApi.getDate(0));
		user.setChgDate(user.getRegDate());
		user.setChgUserNo(user.getRegUserNo());
		user.setUserPasswd(User.encodingPassword(user.getUserPasswd()));

		tran.insertOfClass(FX_UR_USER.class, user);

		tran.commit();

	}

	private DbTrans getTran() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createDbTrans(BasCfg.getHomeDeployConfSql("user-handler.xml"));
	}

}

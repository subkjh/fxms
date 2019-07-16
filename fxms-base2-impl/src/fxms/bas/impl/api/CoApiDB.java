package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.CoApi;
import fxms.bas.api.FxApi;
import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.co.AmUserVo;
import fxms.bas.co.FxConfDao;
import fxms.bas.co.OpCode;
import fxms.bas.co.vo.FxVar;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.co.LogoutDbo;
import fxms.bas.impl.co.UserDao;
import fxms.bas.impl.co.UserLogDbo;
import fxms.bas.impl.co.VarDbo;
import fxms.bas.impl.dbo.FX_AM_GROUP;
import fxms.bas.impl.dbo.FX_AM_GROUP_MO;
import fxms.bas.impl.dbo.FX_AM_GROUP_USER;
import fxms.bas.impl.dbo.FX_AM_HST;
import fxms.bas.impl.dbo.FX_CD_OP;
import fxms.bas.impl.dbo.FX_UR_TIME;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.user.UserAlog;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;

public class CoApiDB extends CoApi {

	@Override
	public String getState(LOG_LEVEL arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SessionVo doLogin(String userId, String password, String ipaddr) throws Exception {
		SessionVo user = null;

		try {
			user = new UserDao().login(userId, password, ipaddr);
			Logger.logger.info("{} {} {} {}", userId, password, ipaddr, user.getSessionId());
			user.setLastOpTime(System.currentTimeMillis());
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

		return user;
	}

	@Override
	protected FxVar doSelectVar(String varName) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("varName", varName);

			VarDbo var = tran.selectOne(VarDbo.class, para);
			return new FxVar(var.getVarName(), var.getVarValue());
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void doUpdateVarValue(String varName, Object varValue) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			VarDbo dbo = new VarDbo();
			dbo.setVarName(varName);
			dbo.setVarValue(String.valueOf(varValue));
			dbo.setChgDate(FxApi.getDate(0));
			dbo.setChgUserNo(User.USER_NO_SYSTEM);

			tran.updateOfClass(VarDbo.class, dbo, null);

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	protected List<FxVar> doSelectVarAll() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			List<VarDbo> list = tran.select(VarDbo.class, null);
			List<FxVar> ret = new ArrayList<FxVar>();
			for (VarDbo e : list) {
				ret.add(new FxVar(e.getVarName(), e.getVarValue()));
			}
			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void doLogout(String sessionId) throws Exception {
		LogoutDbo dbo = new LogoutDbo();
		dbo.setLogoutDate(FxApi.getDate());
		dbo.setLogStatusCode(UserAlog.TYPE_LOGOUT);
		dbo.setSessionId(sessionId);

		try {
			FxConfDao.getDao().update(dbo, null);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@Override
	protected void reload() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected Map<Long, List<AmGroupVo>> loadAmGroup() throws Exception {
		try {

			FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
			try {

				tran.start();

				Map<Long, List<AmGroupVo>> moAmMap = new HashMap<Long, List<AmGroupVo>>();

				Map<String, Object> para = new HashMap<String, Object>();
				para.put("enableYn", "Y");
				List<FX_AM_GROUP> list = tran.select(FX_AM_GROUP.class, para);
				if (list.size() > 0) {

					List<AmGroupVo> groupList = new ArrayList<AmGroupVo>();
					for (FX_AM_GROUP e : list) {
						groupList.add(new AmGroupVo(e.getAmGroupNo()));
					}

					List<FX_AM_GROUP_USER> userList = tran.select(FX_AM_GROUP_USER.class, para);
					List<FX_AM_GROUP_MO> moList = tran.select(FX_AM_GROUP_MO.class, para);

					for (FX_AM_GROUP_USER user : userList) {
						for (AmGroupVo group : groupList) {
							if (user.getAmGroupNo() == group.getAmGroupNo()) {
								group.getAmList().add(new AmUserVo(user.getUserNo(), user.getAmName(), user.getAmMail(),
										user.getAmTelno()));
								break;
							}
						}
					}

					List<AmGroupVo> entry;

					for (FX_AM_GROUP_MO mo : moList) {
						entry = moAmMap.get(mo.getMoNo());
						if (entry == null) {
							entry = new ArrayList<AmGroupVo>();
							moAmMap.put(mo.getMoNo(), entry);
						}

						for (AmGroupVo group : groupList) {
							if (mo.getAmGroupNo() == group.getAmGroupNo()) {
								entry.add(group);
								break;
							}
						}
					}
				}

				return moAmMap;

			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			} finally {
				tran.stop();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public void logAmHst(List<AmHstVo> list) throws Exception {
		FxConfDao.getDao().insertList(FX_AM_HST.class, list);
	}

	@Override
	public void logUserOp(int userNo, String sessionId, OpCode opcode, String inPara, String outRet, int retNo,
			String retMsg, long mstimeStart) {

		UserLogDbo log = new UserLogDbo();

		log.setEndDate(FxApi.getDate(0));
		log.setInPara(inPara);
		log.setOpNo(opcode.getOpNo());
		log.setOpSeqno(0);
		log.setOutRet(outRet);
		log.setRetMsg(retMsg);
		log.setRetNo(retNo);
		log.setSessionId(sessionId);
		log.setSrtDate(FxApi.getDate(mstimeStart));
		log.setUserNo(userNo);

		FxDaoExecutor tran;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		} catch (Exception e) {
			Logger.logger.error(e);
			return;
		}

		try {
			tran.start();
			long opSeqno = tran.getNextVal("FX_SEQ_OPSEQNO", Long.class);
			log.setOpSeqno(opSeqno);
			tran.insertOfClass(UserLogDbo.class, log);

			if (opcode != null && opcode.getDataType() != null) {
				FX_UR_TIME data = new FX_UR_TIME();
				data.setDataCount(0);
				data.setDataType(opcode.getDataType());
				data.setDoneDate(log.getSrtDate().longValue());
				data.setUserNo(log.getUserNo().intValue());
				tran.updateOfClass(FX_UR_TIME.class, data, null);
			}

			tran.commit();
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
		} finally {
			tran.stop();
		}

	}

	@Override
	public void logUserAccess() {

	}

	@Override
	protected List<OpCode> doSelectOpCode() throws Exception {
		List<FX_CD_OP> list = FxConfDao.getDao().select(FX_CD_OP.class, null);
		List<OpCode> ret = new ArrayList<OpCode>();
		for (FX_CD_OP a : list) {
			ret.add(new OpCode(a.getOpNo(), a.getOpName(), a.getUpperOpNo(), a.getUgrpNo(), a.getDataType()));
		}
		return ret;
	}
}

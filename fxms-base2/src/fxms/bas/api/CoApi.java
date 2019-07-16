package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.co.OpCode;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.co.vo.FxVar;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.module.restapi.vo.SessionMgr;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;

public abstract class CoApi extends FxApi {

	public static CoApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static CoApi getApi() {
		if (api != null)
			return api;

		api = makeApi(CoApi.class);

		return api;
	}

	/** 환경변수 목록 */
	private Map<String, Object> varMap = new HashMap<String, Object>();
	private Map<Integer, OpCode> codeMap;
	private Map<String, OpCode> nameMap;
	private Map<Long, List<AmGroupVo>> moAmMap = new HashMap<Long, List<AmGroupVo>>();

	/** 로그인 정보 */
	private SessionMgr sessionMgr = new SessionMgr();

	public Map<Long, List<AmGroupVo>> getMoAmMap() {
		return moAmMap;
	}

	public OpCode getOpCode(int opNo) {
		if (codeMap == null) {
			loadOpCode();
		}

		return codeMap == null ? null : codeMap.get(opNo);
	}

	public OpCode getOpCode(String name) {

		if (nameMap == null) {
			loadOpCode();
		}

		return nameMap == null ? null : nameMap.get(name);
	}

	public SessionMgr getSessionMgr() {
		return sessionMgr;
	}

	/**
	 * 
	 * @param varName
	 * @param valueDefault
	 * @return
	 */
	public int getVarValue(String varName, int valueDefault) {

		String value = null;

		try {
			value = getVarValue(varName);
			if (value == null) {
				return valueDefault;
			}
			return Integer.parseInt(value);

		} catch (Exception e) {
			Logger.logger.fail("var={}, value={}", varName, value);
			return valueDefault;
		}
	}

	/**
	 *
	 * @param varName
	 * @param valueDefault
	 * @return
	 */
	public long getVarValue(String varName, long valueDefault) {

		try {
			String value;

			value = getVarValue(varName);
			if (value == null)
				return valueDefault;

			return Long.parseLong(value);
		} catch (Exception e) {
			Logger.logger.error(e);
			return valueDefault;
		}
	}

	/**
	 * 
	 * @param varName
	 * @param valueDefault
	 * @return
	 */
	public String getVarValue(String varName, String valueDefault) {
		try {
			String value = getVarValue(varName);
			if (value == null)
				return valueDefault;
			return value;
		} catch (Exception e) {
			Logger.logger.error(e);
			return valueDefault;
		}
	}

	public boolean isAccesable(User user, String name) {

		OpCode opcode = getOpCode(name);
		if (opcode == null) {
			return true;
		}

		if (opcode.getUserGroupNo() == User.USER_GROUP_ALL) {
			return true;
		}

		if (opcode.getUserGroupNo() == User.USER_GROUP_EMPTY) {
			return false;
		}

		return user.getUserGroupNo() == opcode.getUserGroupNo();

	}

	public abstract void logAmHst(List<AmHstVo> list) throws Exception;

	/**
	 * 
	 * @param userId
	 * @param password
	 * @param ipaddr
	 * @return
	 * @throws Exception
	 */
	public SessionVo login(String userId, String password, String ipaddr) throws Exception {

		SessionVo vo = doLogin(userId, password, ipaddr);

		sessionMgr.putNew(vo, ipaddr);

		return vo;

	}

	/**
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	public void logout(String sessionId) throws Exception {
		doLogout(sessionId);
		sessionMgr.remove(sessionId);
	}

	public abstract void logUserAccess();

	public abstract void logUserOp(int userNo, String sessionId, OpCode opcode, String inPara, String outRet, int retNo,
			String retMsg, long mstimeStart);

	public void setVarValue(String varName, Object varValue, boolean broadcast) throws Exception {

		Logger.logger.debug("var={}, val={}, broadcast={}", varName, varValue, broadcast);

		try {

			doUpdateVarValue(varName, varValue);

			varMap.remove(varName);

			// 다시 적재 합니다.
			getVarValue(varName);

			if (broadcast && FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_VAR));
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	protected abstract SessionVo doLogin(String userId, String password, String ipaddr) throws Exception;

	protected abstract void doLogout(String sessionId) throws Exception;

	protected abstract List<OpCode> doSelectOpCode() throws Exception;

	/**
	 * 환경변수 내역을 제공합니다.
	 * 
	 * @param varName 환경변수명
	 * @return 환경변수
	 * @throws Exception
	 */
	protected abstract FxVar doSelectVar(String varName) throws Exception;

	/**
	 * 환경변수를 저장소에서 조회합니다.
	 * 
	 * @return 환경변수 목록
	 * @throws Exception
	 */
	protected abstract List<FxVar> doSelectVarAll() throws Exception;

	/**
	 * 환경변수의 값을 설정합니다.<br>
	 * 
	 * @param varName  변수명
	 * @param varValue 변수값
	 * 
	 * @throws Exception
	 */
	protected abstract void doUpdateVarValue(String varName, Object varValue) throws Exception;

	@Override
	protected void initApi() throws Exception {
		moAmMap = loadAmGroup();

		try {
			StringBuffer sb = new StringBuffer();
			List<FxVar> varList = doSelectVarAll();
			for (FxVar var : varList) {
				sb.append(Logger.makeSubString(var.getName(), var.getValue()));
				varMap.put(var.getName(), var.getValue());
			}
			Logger.logger.info(Logger.makeString("variables", null, sb.toString()));

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	protected abstract Map<Long, List<AmGroupVo>> loadAmGroup() throws Exception;

	/**
	 * 
	 * @param varName
	 * @return
	 * @throws Exception
	 */
	private synchronized String getVarValue(String varName) throws Exception {

		if (varMap.size() == 0) {
			List<FxVar> varList = doSelectVarAll();
			for (FxVar var : varList) {
				varMap.put(var.getName(), var.getValue());
			}
		}

		Object value = varMap.get(varName);

		if (value == null) {
			try {
				FxVar var = doSelectVar(varName);
				String valueStr = var == null ? null : var.getValue();
				if (valueStr != null) {
					varMap.put(varName, valueStr);
				} else {
					Logger.logger.fail("VAR-NAME(" + varName + ") NOT DEFINED");
				}
				return valueStr;
			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		}
		return value.toString();
	}

	private void loadOpCode() {

		try {

			List<OpCode> opList = doSelectOpCode();

			Map<Integer, OpCode> codeMap = new HashMap<Integer, OpCode>();
			Map<String, OpCode> nameMap = new HashMap<String, OpCode>();

			for (OpCode op : opList) {
				codeMap.put(op.getOpNo(), op);
				nameMap.put(op.getOpName(), op);
			}

			this.codeMap = codeMap;
			this.nameMap = nameMap;

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}
}

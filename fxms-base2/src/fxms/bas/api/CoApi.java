package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.co.OpCode;
import fxms.module.restapi.vo.SessionVo;

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

	private Map<Integer, OpCode> codeMap;
	private Map<String, OpCode> nameMap;
	private Map<Long, List<AmGroupVo>> moAmMap = new HashMap<Long, List<AmGroupVo>>();

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

	public abstract SessionVo login(String userId, String password, String ipaddr) throws Exception;

	public abstract void logout(String sessionId) throws Exception;

	public abstract void logUserAccess();

	public abstract void logUserOp(int userNo, String sessionId, OpCode opcode, String inPara, String outRet, int retNo,
			String retMsg, long mstimeStart);

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

	protected abstract List<OpCode> doSelectOpCode() throws Exception;

	@Override
	protected void initApi() throws Exception {
		moAmMap = loadAmGroup();
	}

	protected abstract Map<Long, List<AmGroupVo>> loadAmGroup() throws Exception;
}

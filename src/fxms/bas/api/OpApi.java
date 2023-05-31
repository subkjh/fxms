package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.exp.NotFoundException;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.OpCode;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 운영 코드 관리 API
 * 
 * @author subkjh
 *
 */
public abstract class OpApi extends FxApi {

	public static OpApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static OpApi getApi() {
		if (api != null)
			return api;

		api = makeApi(OpApi.class);

		return api;
	}

	private final Map<String, OpCode> opMap = new HashMap<String, OpCode>();

	/**
	 * 운용코드 정보를 로딩한다.
	 */
	private void loadOpCode() {

		try {

			List<OpCode> opList = doSelectOpCodes(null);

			Map<String, OpCode> opMap = new HashMap<String, OpCode>();

			for (OpCode op : opList) {
				opMap.put(op.getOpId(), op);
			}

			synchronized (this.opMap) {
				this.opMap.clear();
				this.opMap.putAll(opMap);
			}

			save2file("op.code.list", opList);

			Logger.logger.info(Logger.makeString("LOADED OP-ID", opMap.size()));

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract List<OpCode> doSelectOpCodes(Map<String, Object> para) throws Exception;

	protected abstract boolean doUpdateOpId(int userNo, String opId, boolean use) throws NotFoundException, Exception;

	@Override
	public void onCreated() throws Exception {
		loadOpCode();
	}

	/**
	 * 
	 * @param opId
	 * @param use
	 */
	public boolean setOpId(int userNo, String opId, boolean use) {
		try {

			doUpdateOpId(userNo, opId, use);

			if (use == false) {
				synchronized (this.opMap) {
					this.opMap.remove(opId);
				}
			} else {
				List<OpCode> list = doSelectOpCodes(FxApi.makePara("opId", opId));
				if (list.size() == 1) {
					synchronized (this.opMap) {
						this.opMap.put(opId, list.get(0));
					}
				}
			}

			return true;
		} catch (Exception e) {
			Logger.logger.error(e);
			return false;
		}
	}

	@Override
	public void reload(Enum<?> type) throws Exception {
		if (type == ReloadType.All || type == ReloadType.Op) {
			loadOpCode();
		}
	}

	/**
	 * 운용항목 조회
	 * 
	 * @param opId
	 * @return
	 */
	public OpCode getOpCode(String opId) {
		synchronized (this.opMap) {
			return this.opMap.get(opId);
		}
	}

	/**
	 * 
	 * @param userGroupNo 사용자그룹번호
	 * @param opId        기능ID
	 * @return
	 */
	public boolean isAccesable(int userGroupNo, String opId) {

		OpCode opcode = getOpCode(opId);

		// 관리하지 않으면 무조건 사용 가능함.
		if (opcode == null) {
			return true;
		}

		return opcode.isAcceesable(userGroupNo);
	}

	@Override
	public String getState(LOG_LEVEL arg0) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		synchronized (this.opMap) {
			sb.append(Logger.makeString("op.size", this.opMap.size()));
		}
		return sb.toString();
	}

}

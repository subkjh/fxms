package fxms.bas.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.dbo.cd.OpCode;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;

public class OpCodePool {

	private static OpCodePool pool;

	public static OpCodePool getPool() {

		if (pool == null) {
			pool = new OpCodePool();
		}
		return pool;
	}

	private OpCodePool() {

	}

	private Map<Integer, OpCode> codeMap;
	private Map<String, OpCode> nameMap;

	private void load() {

		FxDaoExecutor tran = null;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		} catch (Exception e) {
			Logger.logger.error(e);
			return;
		}
		try {
			tran.start();

			List<OpCode> opList = tran.select(OpCode.class, null);

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
		} finally {
			tran.stop();
		}
	}

	public boolean isAccesable(User user, String name) {
		
		OpCode opcode = getOpCode(name);
		if (opcode == null) {
			return true;
		}
		
		if ( opcode.getUgrpNo() == User.USER_GROUP_ALL ) {
			return true;
		}
		
		if ( opcode.getUgrpNo() == User.USER_GROUP_EMPTY ) {
			return false;
		}
		
		return user.getUserGroupNo() == opcode.getUgrpNo();

	}

	public OpCode getOpCode(String name) {
		
		if (nameMap == null) {
			load();
		}

		return nameMap == null ? null : nameMap.get(name);
	}

	public OpCode getOpCode(int opNo) {
		if (codeMap == null) {
			load();
		}

		return codeMap == null ? null : codeMap.get(opNo);
	}

}

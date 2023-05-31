package fxms.bas.impl.dpo.op;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CO_OP;
import fxms.bas.impl.dbo.all.FX_UR_UGRP_OP;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.OpCode;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 기능 코드를 조회한다.
 * 
 * @author subkjh
 *
 */
public class SelectOpDfo implements FxDfo<Map<String, Object>, List<OpCode>> {

	@Override
	public List<OpCode> call(FxFact fact, Map<String, Object> data) throws Exception {
		return selectOpCode(data);
	}

	public List<OpCode> selectOpCode(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		if (para == null) {
			para = new HashMap<>();
		}
		para.put("useYn", "Y");

		Map<String, OpCode> opMap = new HashMap<String, OpCode>();
		OpCode opcode;

		try {
			tran.start();

			List<FX_CO_OP> opList = tran.select(FX_CO_OP.class, para);

			for (FX_CO_OP op : opList) {
				opcode = new OpCode(op.getOpId(), op.getOpName(), op.getDataType());
				opMap.put(op.getOpId(), opcode);
				opcode.addUserGroupNo(op.getUgrpNo());
			}

			// 사용자그룹에 지정된 기능항목을 추가한다.
			List<FX_UR_UGRP_OP> uopList = tran.select(FX_UR_UGRP_OP.class, para);
			for (FX_UR_UGRP_OP uop : uopList) {
				opcode = opMap.get(uop.getOpId());
				if (opcode != null) {
					opcode.addUserGroupNo(uop.getUgrpNo());
				}
			}
			return new ArrayList<OpCode>(opMap.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	public OpCode selectOpCode(String opId) throws Exception {
		List<OpCode> list = selectOpCode(FxApi.makePara("opId", opId));
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		throw new NotFoundException("opId", opId);
	}
}

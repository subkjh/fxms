package fxms.bas.impl.dpo.user;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_UR_UGRP_OP;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.op.SelectOpDfo;
import fxms.bas.vo.OpCode;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.Ugrp;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 사용자그룹 권한 설정
 * 
 * @author subkjh
 *
 */
public class UgrpOpSetDpo implements FxDpo<Ugrp, Boolean> {

	public static void main(String[] args) {
		UgrpOpSetDpo dfo = new UgrpOpSetDpo();
		try {
			dfo.setOp(1060, "TEST111", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean set(Ugrp ugrp, OpCode opCode, boolean useYn) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_UR_UGRP_OP op = tran.selectOne(FX_UR_UGRP_OP.class,
					FxApi.makePara("ugrpNo", ugrp.getUgrpNo(), "opId", opCode.getOpId()));
			if (useYn == false) {
				if (op == null) {
					return true;
				} else {
					tran.deleteOfClass(FX_UR_UGRP_OP.class,
							FxApi.makePara("ugrpNo", op.getUgrpNo(), "opId", op.getOpId()));
				}
			} else {
				if (op != null) {
					return true;
				} else {
					FX_UR_UGRP_OP data = new FX_UR_UGRP_OP();
					data.setOpId(opCode.getOpId());
					data.setUgrpNo(ugrp.getUgrpNo());
					data.setRegUserNo(0);
					data.setRegDtm(DateUtil.getDtm());
					tran.insertOfClass(FX_UR_UGRP_OP.class, ugrp);
				}
			}

			tran.commit();

			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	public Boolean run(FxFact fact, Ugrp data) throws Exception {
		OpCode opCode = fact.getObject(OpCode.class, "opCode");
		boolean useYn = fact.isTrue("useYn");
		return set(data, opCode, useYn);
	}

	public boolean setOp(int ugrpNo, String opId, boolean useYn) throws Exception {

		Ugrp ugrp = new UgrpSelectDfo().select(ugrpNo);

		OpCode opCode = new SelectOpDfo().selectOpCode(opId);

		return set(ugrp, opCode, useYn);

	}
}
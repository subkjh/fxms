package fxms.bas.impl.dpo.op;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CO_OP;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 기능 코드 사용 여부 설정
 * 
 * @author subkjh
 *
 */
public class SetUseOpDfo implements FxDfo<Void, Boolean> {

	@Override
	public Boolean call(FxFact fact, Void data) throws Exception {
		String opId = fact.getString("opId");
		Boolean use = fact.isTrue("use");
		return updateOpId(fact.getUserNo(), opId, use);
	}

	public boolean updateOpId(int userNo, String opId, boolean use) throws NotFoundException, Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("opId", opId);

			List<FX_CO_OP> opList = tran.selectDatas(FX_CO_OP.class, para);
			if (opList.size() == 0) {
				throw new NotFoundException("opId", opId);
			}

			FX_CO_OP op = opList.get(0);
			op.setUseYn(use);
			op.setChgDtm(DateUtil.getDtm());
			op.setChgUserNo(userNo);

			tran.updateOfClass(op.getClass(), op);
			tran.commit();

			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}
}

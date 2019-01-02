package fxms.module.restapi.handler.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.dao.FxConfDao;
import fxms.bas.dbo.cd.FX_CD_OP;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;

/**
 * 기능에 대한 접근 권한을 변경하는 함수
 * 
 * @author SUBKJH-DEV
 *
 */
public class OpCodeUpdate {

	public OpCodeUpdate(int opNo, int ugrpNo, boolean includeSub) throws Exception {
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("opNo", opNo);
		para.put("ugrpNo", ugrpNo);

		if (includeSub == false) {
			FxConfDao.getDao().updateOfClass(FX_CD_OP.class, para);
			return;
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			tran.updateOfClass(FX_CD_OP.class, para);

			List<FX_CD_OP> opList = tran.select(FX_CD_OP.class, null);

			updateSubOp(opList, opNo, tran, para);

			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	private void updateSubOp(List<FX_CD_OP> opList, int opNo, FxDaoExecutor tran, Map<String, Object> para)
			throws Exception {

		List<Integer> list = new ArrayList<Integer>();

		for (FX_CD_OP op : opList) {
			if (op.getUpperOpNo() == opNo) {
				list.add(op.getOpNo());
			}
		}

		if (list.size() > 0) {
			para.put("opNo", list);
			tran.updateOfClass(FX_CD_OP.class, para);
			for (int no : list) {
				updateSubOp(opList, no, tran, para);
			}
		}

	}
}

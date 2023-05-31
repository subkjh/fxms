package fxms.bas.impl.dpo.mo;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.co.CoCode.MO_WORK_TYPE_CD;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.DeleteMoDbo;
import fxms.bas.impl.dbo.DeleteMoSubDbo;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dbo.all.FX_MX_WORK_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

public class SetDelFlagMoDfo implements FxDfo<Mo, Mo> {

	@Override
	public Mo call(FxFact fact, Mo data) throws Exception {

		int userNo = fact.getUserNo();

		deleteMo(data, userNo);

		return data;
	}

	public void deleteMo(Mo toDelMo, int userNo) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();

			// 입력된 MO찾기
			para.put("moNo", toDelMo.getMoNo());
			FX_MO mo = tran.selectOne(FX_MO.class, para);
			if (mo == null) {
				throw new MoNotFoundException(toDelMo.getMoNo());
			}

			// 2023.01 깨비쫑
			// 삭제로직에서 DEL_YN = 'Y'로 변경한다.
			// 하위는 그대로 놔둔다.

			// 자식 삭제 플래그
			DeleteMoSubDbo subDbo = new DeleteMoSubDbo();
			subDbo.setUpperMoNo(mo.getMoNo());
			FxTableMaker.initRegChg(userNo, subDbo);
			tran.updateOfClass(DeleteMoSubDbo.class, subDbo);

			// 자신 삭제 플래그
			DeleteMoDbo dbo = new DeleteMoDbo();
			dbo.setMoNo(mo.getMoNo());
			FxTableMaker.initRegChg(userNo, dbo);
			tran.updateOfClass(DeleteMoDbo.class, dbo);

			long workHstNo = tran.getNextVal(FX_MX_WORK_HST.FX_SEQ_WORKHSTNO, Long.class);
			tran.insertOfClass(FX_MX_WORK_HST.class,
					MoDpo.makeHstMo(workHstNo, mo, MO_WORK_TYPE_CD.Delete, userNo, ""));

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

}

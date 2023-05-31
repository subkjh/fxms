package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_PS_STAT_KIND;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 
 * @author subkjh
 *
 */
public class PsKindSelectDfo implements FxDfo<Void, List<PsKind>> {

	@Override
	public List<PsKind> call(FxFact fact, Void data) throws Exception {
		return selectPsKinds();
	}

	public List<PsKind> selectPsKinds() throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			List<FX_PS_STAT_KIND> list = tran.select(FX_PS_STAT_KIND.class, FxApi.makePara("USE_YN", "Y"));
			List<PsKind> kindList = new ArrayList<PsKind>();
			PsKind kind;
			for (FX_PS_STAT_KIND row : list) {
				kind = new PsKind(row.getPsDataName(), row.getPsDataTag(), row.getDataStoreDays(), row.getDataRange());
				kind.setTblPartStoreCnt(row.getTblPartStoreCnt());
				kind.setTblPartUnitCd(PsKind.TBL_PART_UNIT_CD.get(row.getTblPartUnitCd()));
				kind.setPsDataSrc(row.getPsDataSrc());
				kindList.add(kind);
			}

			return kindList;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}

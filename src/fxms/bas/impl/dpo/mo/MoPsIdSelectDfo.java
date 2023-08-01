package fxms.bas.impl.dpo.mo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_MAPP_PS;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.mapp.MappMoPs;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 관리대상 + 수집ID = 맵핑ID 데이터를 조회한다.
 * 
 * @author subkjh
 *
 */
public class MoPsIdSelectDfo implements FxDfo<String, List<MappMoPs>> {

	@Override
	public List<MappMoPs> call(FxFact fact, String mngDiv) throws Exception {
		return selectMappMoPs(mngDiv);
	}

	public List<MappMoPs> selectMappMoPs(String mngDiv) throws Exception {
		return selectMappMoPs(FxApi.makePara("mngDiv", mngDiv));
	}

	public List<MappMoPs> selectMappMoPs(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();
			List<MappMoPs> ret = new ArrayList<>();
			List<FX_MAPP_PS> list = tran.selectDatas(FX_MAPP_PS.class, para);
			for (FX_MAPP_PS ps : list) {
				ret.add(new MappMoPs(ps.getMoNo(), ps.getPsId(), ps.getPsName(), ps.getMappId()));
			}
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}

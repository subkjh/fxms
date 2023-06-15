package fxms.bas.impl.dpo.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dao.ValueApiQid;
import fxms.bas.impl.dbo.all.FX_V_ACUR;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 수집 데이터값이 특정 테이블에 기록해야 할 때 변경된 부분이 있으면 업데이트 한다.
 * 
 * @author subkjh
 *
 */
public class UpdateCurValueDfo implements FxDfo<PsVoList, Boolean> {

	@Override
	public Boolean call(FxFact fact, PsVoList voList) throws Exception {
		return updateCur(voList);
	}

	/**
	 * 
	 * @param voList
	 * @throws Exception
	 */
	public Boolean updateCur(PsVoList voList) throws Exception {

		ValueApiQid QID = new ValueApiQid();
		FX_V_ACUR v;
		long dtm = DateUtil.getDtm(voList.getMstime());
		List<FX_V_ACUR> updateList = new ArrayList<FX_V_ACUR>();

		Logger.logger.debug("owner={}, date={}, size={}", voList.getOwner(), dtm, voList.size());

		for (PsVo value : voList) {
			v = new FX_V_ACUR();
			v.setCurCollDtm(dtm);
			v.setCurCollVal(Math.round(value.getValue().doubleValue() * 100) / 100D);
			v.setMoInstance(value.getMoInstance() == null ? "*" : value.getMoInstance());
			v.setMoNo(value.getMo().getMoNo());
			v.setPsId(value.getPsItem().getPsId());
			updateList.add(v);
		}

		if (updateList.size() > 0) {
			QidDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
					.createQidDao(BasCfg.getHome(ValueApiQid.QUERY_XML_FILE));
			try {
				tran.start();
				tran.execute(QID.INSERT_FX_V_ACUR, updateList);
				tran.commit();
			} catch (Exception e) {
				for (FX_V_ACUR data : updateList) {
					try {
						tran.execute(QID.INSERT_FX_V_ACUR, data);
					} catch (Exception e2) {
						Logger.logger.fail("moNo={}, psId={}", data.getMoNo(), data.getPsId());
					}
				}
				tran.commit();
			} finally {
				tran.stop();
			}
		}

		return true;
	}

}
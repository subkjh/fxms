package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.PsItemNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_PS_ITEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 
 * @author subkjh
 *
 */
public class PsItemSelectDfo implements FxDfo<Void, List<PsItem>> {

	@Override
	public List<PsItem> call(FxFact fact, Void data) throws Exception {
		return null;
	}

	public PsItem selectPsItem(String psId) throws PsItemNotFoundException, Exception {

		List<PsItem> list = selectPsItems(FxApi.makePara("psId", psId));
		if (list.size() == 1)
			return list.get(0);

		throw new PsItemNotFoundException(psId);
	}

	public List<PsItem> selectPsItems(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, PsItem> map = new HashMap<String, PsItem>();

			PsItem item;
			List<FX_PS_ITEM> itemList = tran.select(FX_PS_ITEM.class, para);

			// 목록 만들기
			for (FX_PS_ITEM o : itemList) {
				item = new PsItem(o.getPsId(), o.getPsName(), o.getPsTbl(), o.getPsCol(), o.getPsFmt(), o.getPsUnit(),
						o.getStatFuncIds(), o.getPsScale(), o.getMoClass(), o.getMoType(), o.getPsGrp(),
						o.getPsValType(), o.getPsMemo());

				item.setValue(o.getNullVal(), o.getMinVal(), o.getMaxVal(), o.getDftVal());
				item.setMoUpdateCol(o.getMoUpdTbl(), o.getMoUpdCol(), o.getMoUpdDtmCol());
				item.setUse("Y".equalsIgnoreCase(o.isUseYn()));

				map.put(item.getPsId(), item);
			}

			return new ArrayList<PsItem>(map.values());

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}

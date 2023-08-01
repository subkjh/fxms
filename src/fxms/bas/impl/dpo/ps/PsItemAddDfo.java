package fxms.bas.impl.dpo.ps;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_PS_ITEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

/**
 * 
 * @author subkjh
 *
 */
public class PsItemAddDfo implements FxDfo<Void, Boolean> {

	@Override
	public Boolean call(FxFact fact, Void data) throws Exception {
		return null;
	}

	public Boolean addPsItem(int userNo, String psId, String psName, Map<String, Object> para) throws Exception {

		FX_PS_ITEM item = FxTableMaker.toObject(para, FX_PS_ITEM.class, false);
		FxTableMaker.initRegChg(userNo, item);

		item.setPsId(psId);
		item.setPsName(psName);

		if (FxApi.isEmpty(item.getPsTbl())) {
			item.setPsTbl("FX_V_" + psId.toUpperCase());
		}
		if (FxApi.isEmpty(item.getPsTbl())) {
			item.setPsCol(psId.toUpperCase());
		}

		ClassDaoEx.InsertOfClass(FX_PS_ITEM.class, item);
		return true;

	}

}

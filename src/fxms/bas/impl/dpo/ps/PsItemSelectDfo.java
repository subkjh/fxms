package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.PsItemNotFoundException;
import fxms.bas.impl.dbo.all.FX_PS_ITEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import subkjh.dao.ClassDaoEx;

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
		
		List<FX_PS_ITEM> itemList = ClassDaoEx.SelectDatas(FX_PS_ITEM.class, para);
		Map<String, PsItem> map = new HashMap<String, PsItem>();

		PsItem item;

		// 목록 만들기
		for (FX_PS_ITEM o : itemList) {
			item = new PsItem(o.getPsId(), o.getPsName(), o.getPsTbl(), o.getPsCol(), o.getPsFmt(), o.getPsUnit(),
					o.getStatFuncIds(), o.getPsScale(), o.getMoClass(), o.getMoType(), o.getPsGrp(), o.getPsValType(),
					o.getPsMemo());

			item.setValue(o.getNullVal(), o.getMinVal(), o.getMaxVal(), o.getDftVal());
			item.setMoUpdateCol(o.getMoUpdTbl(), o.getMoUpdCol(), o.getMoUpdDtmCol());
			item.setUse("Y".equalsIgnoreCase(o.isUseYn()));

			map.put(item.getPsId(), item);
		}

		return new ArrayList<PsItem>(map.values());

	}

}

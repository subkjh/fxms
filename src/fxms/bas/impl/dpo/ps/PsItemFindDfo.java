package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;

/**
 * 수집데이터에서 성능항목을 추출한다.
 * 
 * @author subkjh
 *
 */
public class PsItemFindDfo extends PsDpo implements FxDfo<PsVoList, List<PsItem>> {

	@Override
	public List<PsItem> call(FxFact fact, PsVoList datas) throws Exception {
		return find(datas);
	}

	public List<PsItem> find(PsVoList datas) {
		Map<String, PsItem> map = new HashMap<String, PsItem>();
		for (PsVo e : datas) {
			map.put(e.getPsItem().getPsId(), e.getPsItem());
		}
		return new ArrayList<PsItem>(map.values());
	}

}

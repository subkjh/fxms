package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.vo.PsValGroup;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;

/**
 * PsVoList 데이터를 이용하여 PsValGroup 목록을 생성한다.
 * 
 * @author subkjh
 *
 */
public class PsValMakeGroupDfo extends PsDpo implements FxDfo<PsVoList, List<PsValGroup>> {

	public static void main(String[] args) {

		MoApi.api = new MoApiDfo();

		PsValMakeGroupDfo dfo = new PsValMakeGroupDfo();
		PsVoList datas = new PsVoList("test", System.currentTimeMillis(), null);

		try {
			datas.add(1, MoApi.getApi().getMo(1000), PsApi.getApi().getPsItem("MoStatus"), null);
			System.out.println(FxmsUtil.toJson(dfo.make(datas)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PsValGroup> call(FxFact fact, PsVoList datas) throws Exception {
		return make(datas);
	}

	/**
	 * 
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public List<PsValGroup> make(PsVoList datas) throws Exception {

		Map<String, PsValGroup> map = new HashMap<>();
		PsValGroup grp;

		for (PsVo val : datas) {
			grp = map.get(val.getPsItem().getPsTable());
			if (grp == null) {
				grp = new PsValGroup(val.getPsItem().getPsTable(), datas.getMstime());
				map.put(val.getPsItem().getPsTable(), grp);
			}

			grp.add(val.getMo().getMoNo(), val.getPsItem().getPsColumn(), val.getValue());

		}
		return new ArrayList<PsValGroup>(map.values());
	}

}

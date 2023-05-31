package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.InloApi;
import fxms.bas.impl.dpo.inlo.InloAddDpo;
import fxms.bas.impl.dpo.inlo.RemoveInloDpo;
import fxms.bas.impl.dpo.inlo.SelectInloListDfo;
import fxms.bas.impl.dpo.inlo.SelectMappInloDfo;
import fxms.bas.impl.dpo.inlo.UpdateInloDpo;
import fxms.bas.vo.Inlo;

/**
 * 저장소 작업이 완료된 MoApi
 * 
 * @author subkjh
 *
 */
public class InloApiDB extends InloApi {

	@Override
	public int addInlo(int userNo, Map<String, Object> para) throws Exception {
		return new InloAddDpo().addInlo(userNo, para);
	}

	@Override
	public List<Inlo> selectInlos(Map<String, Object> para) throws Exception {
		return new SelectInloListDfo().selectInlo(para);
	}

	@Override
	public boolean updateInlo(int userNo, int inloNo, Map<String, Object> para) throws Exception {
		return new UpdateInloDpo().updateInlo(userNo, inloNo, para);
	}

	@Override
	public Map<String, Integer> getMappInloAll(String mngDiv) throws Exception {
		return new SelectMappInloDfo().selectMappInlo(mngDiv);
	}

	@Override
	public boolean removeInlo(int inloNo, String inloName) throws Exception {
		return new RemoveInloDpo().removeInlo(inloNo, inloName);
	}
}

package fxms.bas.impl.dpo.inlo;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;

/**
 * 설치위치를 기록하는 DPO
 * 
 * @author subkjh
 *
 */
public class UpdateInloDfo implements FxDfo<Map<String, Object>, Boolean> {

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {

		int userNo = fact.getUserNo();
		int inloNo = fact.getInt("inloNo");

		return updateInlo(userNo, inloNo, data);
	}

	/**
	 * 
	 * @param userNo
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public boolean updateInlo(int userNo, int inloNo, Map<String, Object> datas) throws Exception {
		ClassDaoEx.open().setOfClass(FX_CF_INLO.class, FxApi.makePara("inloNo", inloNo), datas).close();
		return true;
	}

}

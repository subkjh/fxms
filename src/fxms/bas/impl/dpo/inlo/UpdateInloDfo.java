package fxms.bas.impl.dpo.inlo;

import java.util.Map;

import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

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

		FX_CF_INLO old = ClassDaoEx.SelectData(FX_CF_INLO.class, ClassDaoEx.makePara("inloNo", inloNo));

		if (old != null && datas != null && datas.size() > 0) {

			ObjectUtil.toObject(datas, old);

			FxTableMaker.initRegChg(userNo, old);

			// 데이터 수정
			ClassDaoEx.open().updateOfClass(FX_CF_INLO.class, old).close();

			return true;
		}
		return false;
	}

}

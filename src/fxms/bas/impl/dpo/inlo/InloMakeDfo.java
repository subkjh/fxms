package fxms.bas.impl.dpo.inlo;

import java.util.Map;

import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Inlo;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

/**
 * 설치위치 등록
 * 
 * @author subkjh
 *
 */
public class InloMakeDfo implements FxDfo<Map<String, Object>, FX_CF_INLO> {

	@Override
	public FX_CF_INLO call(FxFact fact, Map<String, Object> data) throws Exception {
		int userNo = fact.getUserNo();
		Inlo upper = fact.getObject(Inlo.class, "upper");
		return make(userNo, upper, data);
	}

	/**
	 * 
	 * @param userNo 등록자번호
	 * @param upper  상위위치
	 * @param para   등록할 위치정보
	 * @return
	 * @throws Exception
	 */
	public FX_CF_INLO make(int userNo, Inlo upper, Map<String, Object> para) throws Exception {

		FX_CF_INLO item = new FX_CF_INLO();
		ObjectUtil.toObject(para, item);

		if (upper != null && upper.getInloNo() > 0) {
			item.setInloAllName(InloDpo.getAllName(upper.getInloAllName(), item.getInloName()));
		} else {
			item.setInloAllName(item.getInloName());
		}

		FxTableMaker.initRegChg(userNo, item);

		item.setInloNo(ClassDaoEx.GetNextVal(FX_CF_INLO.FX_SEQ_INLONO, Integer.class));

		return item;
	}

}

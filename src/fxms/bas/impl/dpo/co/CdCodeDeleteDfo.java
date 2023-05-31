package fxms.bas.impl.dpo.co;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.impl.dbo.all.FX_CO_CD;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.CdCodeDto;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 코드 목록 조회
 * 
 * @author subkjh
 *
 */
public class CdCodeDeleteDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) throws Exception {
		CdCodeDeleteDfo dfo = new CdCodeDeleteDfo();
		System.out.println(dfo.call(null, FxApi.makePara("cdClass", "TEST", "cdCode", "111")));
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {
		CdCodeDto dto = FxAttrApi.toObject(data, CdCodeDto.class);
		return delete(dto);
	}

	public boolean delete(CdCodeDto dto) throws Exception {
		Map<String, Object> para = ObjectUtil.toMap(dto);
		para.put("useYn", "N");
		int cnt = ClassDaoEx.open().updateOfClass(FX_CO_CD.class, para).close().getProcessedCount();
		return cnt == 1;
	}
}
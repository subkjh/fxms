package fxms.bas.impl.dpo.co;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.CdClNotFoundException;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.impl.dbo.all.FX_CO_CD;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.CdCodeDto;
import subkjh.dao.ClassDaoEx;

/**
 * 코드 목록 조회
 * 
 * @author subkjh
 *
 */
public class CdCodeUpdateDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) throws Exception {
		CdCodeUpdateDfo dfo = new CdCodeUpdateDfo();
		System.out.println(
				dfo.call(null, FxApi.makePara("cdClass", "TEST", "cdCode", "111", "cdName", "AAA", "cdDesc", "TEST")));
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {
		CdCodeDto dto = FxAttrApi.toObject(data, CdCodeDto.class);
		return udpate(dto, data);
	}

	public boolean udpate(CdCodeDto dto, Map<String, Object> data) throws Exception {
			int cnt = ClassDaoEx.open().setOfClass(FX_CO_CD.class, dto, data).close().getProcessedCount();
		return cnt == 1;
	}
}
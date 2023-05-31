package fxms.bas.impl.dpo.co;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.CdClNotFoundException;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.impl.dbo.all.FX_CO_CD;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.CdCodeAddDto;
import subkjh.dao.ClassDaoEx;

/**
 * 코드 목록 조회
 * 
 * @author subkjh
 *
 */
public class CdCodeInsertDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) throws Exception {
		CdCodeInsertDfo dfo = new CdCodeInsertDfo();
		System.out.println(
				dfo.call(null, FxApi.makePara("cdClass", "TEST", "cdCode", "111", "cdName", "AAA", "cdDesc", "TEST")));
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {
		CdCodeAddDto dto = FxAttrApi.toObject(data, CdCodeAddDto.class);
		return insert(dto);
	}

	public boolean insert(CdCodeAddDto dto) throws Exception {

		if (new CdCodeSelectDfo().selectCl(dto.getCdClass()) == null) {
			throw new CdClNotFoundException(dto.getCdClass());
		}

		int cnt = ClassDaoEx.open().insertOfClass(FX_CO_CD.class, dto).close().getProcessedCount();
		return cnt == 1;
	}
}
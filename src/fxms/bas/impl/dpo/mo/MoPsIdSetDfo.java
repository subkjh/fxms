package fxms.bas.impl.dpo.mo;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.co.DATA_STATUS;
import fxms.bas.impl.dbo.all.FX_MAPP_PS;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.mapp.MappData;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 관리대상 + 수집ID = 맵핑ID 데이터를 조회한다.
 * 
 * @author subkjh
 *
 */
public class MoPsIdSetDfo implements FxDfo<FX_MAPP_PS, DATA_STATUS> {

	@Override
	public DATA_STATUS call(FxFact fact, FX_MAPP_PS data) throws Exception {
		return set(data.getMngDiv(), data.getMappData(), ObjectUtil.toMap(data));
	}

	public DATA_STATUS set(int userNo, MappData mappData, long moNo, String moName, String psId, String psName)
			throws Exception {

		Map<String, Object> datas = FxApi.makePara("moNo", moNo, "moName", moName, "psId", psId, "psName", psName);
		datas.put("mappDescr", mappData.getMappDescr());
		datas.put("mappId", mappData.getMappId().toString());

		return set(mappData.getMngDiv(), mappData.getMappData(), datas);

	}

	public DATA_STATUS set(String mngDiv, String mappData, Map<String, Object> datas) throws Exception {

		datas.put("mngDiv", mngDiv);
		datas.put("mappData", mappData);

		ClassDaoEx dao = ClassDaoEx.open().setOfClass(FX_MAPP_PS.class //
				, FxApi.makePara("mngDiv", mngDiv, "mappData", mappData) //
				, datas).close();

		return dao.getDataStatus();

	}
}

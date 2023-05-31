package fxms.bas.impl.dpo.ao;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.impl.dbo.all.FX_AL_CFG_MEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.AlCfgMemDto;
import subkjh.dao.ClassDaoEx;

public class AlCfgMemUpdateDfo implements FxDfo<Map<String, Object>, Boolean> {
	
	public static void main(String[] args) throws Exception {
		AlCfgMemUpdateDfo dfo = new AlCfgMemUpdateDfo();
		dfo.call(null, FxApi.makePara("alarmCfgNo", 10002, "alcdNo", 23011, "alMinCmprVal", 5));
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {
		AlCfgMemDto dto = FxAttrApi.toObject(data, AlCfgMemDto.class);
		return update(dto, data);
	}

	public boolean update(AlCfgMemDto dto, Map<String, Object> data) throws Exception {
		ClassDaoEx.open().setOfClass(FX_AL_CFG_MEM.class, dto, data).close();
		return true;
	}
}

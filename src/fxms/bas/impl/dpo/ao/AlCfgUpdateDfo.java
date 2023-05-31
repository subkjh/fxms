package fxms.bas.impl.dpo.ao;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.impl.dbo.all.FX_AL_CFG;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.AlCfgDto;
import subkjh.dao.ClassDaoEx;

/**
 * 알람 발생 조건 복사
 * 
 * @author subkjh
 *
 */
public class AlCfgUpdateDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) throws Exception {
		AlCfgUpdateDfo dfo = new AlCfgUpdateDfo();
		dfo.call(null, FxApi.makePara("alarmCfgNo", 10000, "alarmCfgName", "이름변경"));
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {
		AlCfgDto dto = FxAttrApi.toObject(data, AlCfgDto.class);
		return udpate(dto, data);
	}

	public Boolean udpate(AlCfgDto dto, Map<String, Object> data) throws Exception {
		ClassDaoEx.open().setOfClass(FX_AL_CFG.class, dto, data).close();
		return true;
	}
}

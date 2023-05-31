package fxms.bas.impl.dpo.ao;

import java.util.Map;

import fxms.bas.impl.dbo.all.FX_AL_CFG_MEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.AlCfgMemAddDto;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

public class AlCfgMemInsertDfo implements FxDfo<Map<String, Object>, FX_AL_CFG_MEM> {

	@Override
	public FX_AL_CFG_MEM call(FxFact fact, Map<String, Object> data) throws Exception {
		AlCfgMemAddDto dto = ObjectUtil.toObject(data, AlCfgMemAddDto.class);
		return insert(dto);
	}

	public FX_AL_CFG_MEM insert(AlCfgMemAddDto dto) throws Exception {

		FX_AL_CFG_MEM obj = ObjectUtil.toObject(dto, FX_AL_CFG_MEM.class);

		FxTableMaker.initRegChg(0, obj);

		ClassDaoEx.open().insertOfClass(FX_AL_CFG_MEM.class, obj).close();

		return obj;
	}
}

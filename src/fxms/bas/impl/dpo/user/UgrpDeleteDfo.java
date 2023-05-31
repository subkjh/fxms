package fxms.bas.impl.dpo.user;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_UR_UGRP;
import fxms.bas.impl.dbo.all.FX_UR_UGRP_OP;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.UgrpDto;
import subkjh.bas.co.lang.Lang;
import subkjh.dao.ClassDaoEx;

public class UgrpDeleteDfo implements FxDfo<UgrpDto, Boolean> {

	public static void main(String[] args) {
		UgrpDeleteDfo dfo = new UgrpDeleteDfo();
		try {
			dfo.delete(1060, "TEST111");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean call(FxFact fact, UgrpDto data) throws Exception {
		return delete(data.getUgrpNo(), data.getUgrpName());
	}

	public boolean delete(int ugrpNo, String ugrpName) throws Exception {

		ClassDaoEx dao = ClassDaoEx.open();

		FX_UR_UGRP ugrp = dao.selectData(FX_UR_UGRP.class, ClassDaoEx.makePara("ugrpNo", ugrpNo, "ugrpName", ugrpName));
		if (ugrp == null) {
			dao.close();
			throw new Exception(Lang.get("This user group does not exist.", ugrpNo, ugrpName));
		}

		int count = dao.selectDataCount(FX_UR_USER.class, FxApi.makePara("ugrpNo", ugrp.getUgrpNo()));
		if (count > 0) {
			dao.close();
			throw new Exception(Lang.get("A member belonging to the user group exists and cannot be deleted."));
		}

		dao.deleteOfClass(FX_UR_UGRP_OP.class, FxApi.makePara("ugrpNo", ugrpNo)) //
				.deleteOfClass(FX_UR_UGRP.class, FxApi.makePara("ugrpNo", ugrpNo, "ugrpName", ugrpName))//
				.close();

		return true;
	}
}
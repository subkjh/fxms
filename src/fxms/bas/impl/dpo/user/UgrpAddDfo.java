package fxms.bas.impl.dpo.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CO_OP;
import fxms.bas.impl.dbo.all.FX_UR_UGRP;
import fxms.bas.impl.dbo.all.FX_UR_UGRP_OP;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

public class UgrpAddDfo implements FxDfo<Map<String, Object>, Integer> {

	public static void main(String[] args) {
		UgrpAddDfo dfo = new UgrpAddDfo();
		try {
			dfo.addUgrp("TEST1122", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer call(FxFact fact, Map<String, Object> data) throws Exception {
		String ugrpName = fact.getString("ugrpName");
		return addUgrp(ugrpName, data);
	}

	public Integer addUgrp(String ugrpName, Map<String, Object> datas) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_UR_UGRP ugrp = tran.selectOne(FX_UR_UGRP.class, FxApi.makePara("ugrpName", ugrpName));
			if (ugrp != null) {
				throw new Exception(Lang.get("A user group with the same name already exists.", ugrpName));
			}

			int ugrpNo = tran.getNextVal(FX_UR_UGRP.FX_SEQ_UGRPNO, Integer.class);

			ugrp = new FX_UR_UGRP();
			ObjectUtil.toObject(datas, ugrp);
			ugrp.setUgrpName(ugrpName);
			ugrp.setUgrpNo(ugrpNo);
			ugrp.setDelblYn(true);
			ugrp.setUiDispYn(true);
			FxTableMaker.initRegChg(0, ugrp);

			tran.insertOfClass(FX_UR_UGRP.class, ugrp);

			long regDtm = DateUtil.getDtm();
			List<FX_CO_OP> opList = tran.select(FX_CO_OP.class, null);
			List<FX_UR_UGRP_OP> uopList = new ArrayList<FX_UR_UGRP_OP>();
			FX_UR_UGRP_OP uop;

			for (FX_CO_OP op : opList) {

				// 사용중이고 모두에게 적용 가능하면 그룹에 추가해 준다.
				if (op.isUseYn() && op.getUgrpNo() == User.USER_GROUP_ALL) {

					uop = new FX_UR_UGRP_OP();
					uop.setOpId(op.getOpId());
					uop.setRegDtm(regDtm);
					uop.setRegMemo("그룹등록");
					uop.setRegUserNo(User.USER_NO_SYSTEM);
					uop.setUgrpNo(ugrpNo);
					uopList.add(uop);

				}
			}

			if (uopList.size() > 0) {
				tran.insertOfClass(FX_UR_UGRP_OP.class, uopList);
			}

			tran.commit();

			return ugrp.getUgrpNo();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}
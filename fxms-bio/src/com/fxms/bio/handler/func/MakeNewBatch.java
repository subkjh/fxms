package com.fxms.bio.handler.func;

import com.fxms.bio.dbo.FB_BATCH;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.module.restapi.handler.func.HandlerFunc;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class MakeNewBatch extends HandlerFunc {

	public static void main(String[] args) {
		MakeNewBatch c = new MakeNewBatch();

		try {
			Object obj = c.handle(1, 200120, "L180723003", 1040188, "Hematococcus");
			System.out.println(ObjectUtil.toMap(obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object handle(int userNo, int inloNo, String batchId, long pbrMoNo, String prdcCode) throws Exception {

		String seedCode = prdcCode;
		
		FB_BATCH data = new FB_BATCH();
		initFX_COMMON(userNo, data);
		data.setBatchId(batchId);
		data.setBatchSrtDate(FxApi.getDate());
		data.setCompanyInloNo(inloNo);
		data.setPrdcCode(prdcCode);
		data.setSeedCode(seedCode);
		data.setCurNDays(0);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.insert(data);
			tran.commit();
			return data;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}
package com.fxms.bio.handler.func;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.bio.dbo.FB_BATCH;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class MakeNewBatchId {
	
	public static void main(String[] args) {
		MakeNewBatchId c = new MakeNewBatchId();
		
		try {
			Object obj = c.handle(200120);
			System.out.println(ObjectUtil.toMap(obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object handle(int inloNo) throws Exception {

		SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyMMdd");
		String batchId = "L" + YYYYMMDD.format(new Date(System.currentTimeMillis()));

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		FB_BATCH newBatch;
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("batchId like ", batchId + "%");

		try {
			tran.start();
			List<FB_BATCH> batchList = tran.select(FB_BATCH.class, para);
//			for (FB_BATCH batch : batchList) {
//				if (batch.getCurNDays() == -1) {
//					return batch;
//				}
//			}

			newBatch = makeBatch(inloNo, batchId + String.format("%03d", batchList.size() + 1));
			
//			tran.insert(newBatch);
			tran.commit();
			
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("batchId", newBatch.getBatchId());
			return ret;
			
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	private FB_BATCH makeBatch(int inloNo, String batchId) {
		FB_BATCH batch = new FB_BATCH();

		batch.setBatchId(batchId);
		batch.setCompanyInloNo(inloNo);
		batch.setCurNDays(-1);
		batch.setPrdcCode("x");
		batch.setSeedCode("x");

		return batch;
	}
}

package com.fxms.bio.handler.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.bio.dbo.FB_BATCH;
import com.fxms.bio.dbo.FB_BATCH_DATA;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.module.restapi.handler.func.HandlerFunc;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class AddBatchData extends HandlerFunc {

	public static void main(String[] args) {
		AddBatchData c = new AddBatchData();

		try {
			Map<String, Object> para = new HashMap<String, Object>();

			para.put("biomass", Math.random() * 1000);
			para.put("yyyymmdd", FxApi.getYmd(System.currentTimeMillis()));
			para.put("n", Math.random() * 1000);
			para.put("p", Math.random() * 1000);
			para.put("endYn", "N");

			Object obj = c.handle(1, "L180723003", "20180723", 1040188, "Hematococcus", para);
			System.out.println(ObjectUtil.toMap(obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object handle(int userNo, String batchId, String yyyymmdd, long pbrMoNo, String prdcCode,
			Map<String, Object> parameters) throws Exception {

		FB_BATCH_DATA data = makeData(userNo, parameters, FB_BATCH_DATA.class);
		data.setBatchId(batchId);
		data.setPbrMoNo(pbrMoNo);
		data.setYyyymmdd(yyyymmdd);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("batchId", data.getBatchId());

		int days = 0;

		try {
			tran.start();
			List<FB_BATCH_DATA> dataList;

			FB_BATCH batch = tran.selectOne(FB_BATCH.class, para);

			para.put("batchId", data.getBatchId());
			para.put("yyyymmdd", data.getYyyymmdd());

			dataList = tran.select(FB_BATCH_DATA.class, para);
			if (dataList.size() == 1) {
				FB_BATCH_DATA oldData = dataList.get(0);
				data.setNDays(oldData.getNDays());
				tran.update(data, null);
			} else {
				dataList = tran.select(FB_BATCH_DATA.class, para);
				for (FB_BATCH_DATA e : dataList) {
					if (days < e.getNDays()) {
						days = e.getNDays();
					}
				}

				data.setNDays(days + 1);
				
				tran.insert(data);

			}

			if (data.isEndYn()) {
				batch.setEndYn(true);
				batch.setBatchEndDate(FxApi.getDate());
			} else {
				batch.setCurNDays(data.getNDays());
			}

			batch.setChgDate(FxApi.getDate(0));
			batch.setChgUserNo(userNo);
			tran.update(batch, null);


			tran.commit();

			return batch;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

}

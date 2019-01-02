package com.fxms.bio.handler.func;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.bio.dbo.FB_BATCH;
import com.fxms.bio.dbo.FB_BATCH_DATA;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class GetBatchData {

	public static void main(String[] args) {
		GetBatchData c = new GetBatchData();

		try {
			List<String> batchIdList = new ArrayList<String>();
			batchIdList.add("L180723001");
			batchIdList.add("L180723002");
			batchIdList.add("L180723003");

			Object obj = c.handle(batchIdList);
			System.out.println(ObjectUtil.toMap(obj));

			System.out.println("\n\n\n\n");

			int inloNo = 200120;
			long startDate = 20180701000000L;
			long endDate = 20180731235959L;
			String prdcCode = "Hematococcus";
			long pbrMoNo = 1040188;

			obj = c.handle(inloNo, startDate, endDate, prdcCode, pbrMoNo);
			System.out.println(ObjectUtil.toMap(obj));

			obj = c.handle(inloNo);
			System.out.println(ObjectUtil.toMap(obj));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, List<FB_BATCH_DATA>> handle(int inloNo, long startDate, long endDate, String prdcCode,
			long pbrMoNo) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("companyInloNo", inloNo);
		para.put("batchSrtDate >= ", startDate);
		para.put("batchSrtDate <= ", endDate);
		if (prdcCode != null) {
			para.put("prdcCode", prdcCode);
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			List<FB_BATCH> batchList = tran.select(FB_BATCH.class, para);

			para.clear();
			List<String> batchIdList = new ArrayList<String>();
			for (FB_BATCH batch : batchList) {
				batchIdList.add(batch.getBatchId());
			}
			if (pbrMoNo > 0) {
				para.put("pbrMoNo", pbrMoNo);
			}
			para.put("batchId", batchIdList);

			Map<String, List<FB_BATCH_DATA>> ret = new HashMap<String, List<FB_BATCH_DATA>>();
			List<FB_BATCH_DATA> entry;

			List<FB_BATCH_DATA> dataList = tran.select(FB_BATCH_DATA.class, para);
			for (FB_BATCH_DATA data : dataList) {
				entry = ret.get(data.getBatchId());
				if (entry == null) {
					entry = new ArrayList<FB_BATCH_DATA>();
					ret.put(data.getBatchId(), entry);
				}
				entry.add(data);
			}

			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	public Map<String, List<FB_BATCH_DATA>> handle(List<String> batchIdList) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("batchId", batchIdList);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			Map<String, List<FB_BATCH_DATA>> ret = new HashMap<String, List<FB_BATCH_DATA>>();
			List<FB_BATCH_DATA> entry;

			List<FB_BATCH_DATA> dataList = tran.select(FB_BATCH_DATA.class, para);
			for (FB_BATCH_DATA data : dataList) {
				entry = ret.get(data.getBatchId());
				if (entry == null) {
					entry = new ArrayList<FB_BATCH_DATA>();
					ret.put(data.getBatchId(), entry);
				}
				entry.add(data);
			}

			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	public FB_BATCH_DATA handle(int inloNo) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("companyInloNo", inloNo);
		para.put("chgDate in", "select max(CHG_DATE) from FB_BATCH where COMPANY_INLO_NO = " + inloNo);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			List<FB_BATCH> batchList = tran.select(FB_BATCH.class, para);
			if (batchList.size() > 0) {
				para.clear();
				para.put("batchId", batchList.get(0).getBatchId());
				List<FB_BATCH_DATA> dataList = tran.select(FB_BATCH_DATA.class, para);
				if (dataList.size() > 0) {
					dataList.sort(new Comparator<FB_BATCH_DATA>() {
						@Override
						public int compare(FB_BATCH_DATA o1, FB_BATCH_DATA o2) {
							return o1.getNDays() - o2.getNDays();
						}
					});
					return dataList.get(dataList.size() - 1);
				}
			}

			return null;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}
}

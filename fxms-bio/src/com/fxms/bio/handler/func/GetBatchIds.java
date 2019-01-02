package com.fxms.bio.handler.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.bio.dbo.FB_BATCH;

import fxms.bas.api.MoApi;
import fxms.bas.dao.FxConfDao;
import fxms.bas.mo.attr.MoLocation;
import subkjh.bas.utils.ObjectUtil;

public class GetBatchIds {

	public static void main(String[] args) {
		GetBatchIds c = new GetBatchIds();

		try {
			Object obj = c.handle(200120, 20180723000000L, "Hematococcus");
			System.out.println(ObjectUtil.toMap(obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Object handle(int inloNo, long startDate, String prdcCode) throws Exception {

		MoLocation location = MoApi.getApi().getMoLocation(inloNo, "COMPANY");
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("companyInloNo", location.getInloNo());
		para.put("batchSrtDate >=", startDate);
		para.put("prdcCode", prdcCode);

		List<FB_BATCH> batchList = FxConfDao.getDao().select(FB_BATCH.class, para);
		List<String> batchIdList = new ArrayList<String>();
		for (FB_BATCH batch : batchList) {
			batchIdList.add(batch.getBatchId());
		}

		
		System.out.println(batchIdList);
		
		return batchIdList;
	}
}

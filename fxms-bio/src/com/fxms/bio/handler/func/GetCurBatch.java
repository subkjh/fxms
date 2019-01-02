package com.fxms.bio.handler.func;

import java.util.HashMap;
import java.util.Map;

import com.fxms.bio.dbo.FB_BATCH;

import fxms.bas.api.MoApi;
import fxms.bas.dao.FxConfDao;
import fxms.bas.mo.attr.MoLocation;
import subkjh.bas.utils.ObjectUtil;

public class GetCurBatch {
	public static void main(String[] args) {
		GetCurBatch c = new GetCurBatch();

		try {
			Object obj = c.handle(200090);
			System.out.println(ObjectUtil.toMap(obj));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object handle(int inloNo) throws Exception {

		MoLocation location = MoApi.getApi().getMoLocation(inloNo, "COMPANY");
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("companyInloNo", location.getInloNo());
		para.put("endYn", "N");

		return FxConfDao.getDao().select(FB_BATCH.class, para);
	}
}

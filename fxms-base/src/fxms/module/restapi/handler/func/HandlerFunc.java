package fxms.module.restapi.handler.func;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.dbo.FX_COMMON;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.utils.ObjectUtil;

public class HandlerFunc {

	protected <T> T makeData(SessionVo session, Map<String, Object> map, Class<T> classOfT) throws Exception {

		T data = classOfT.newInstance();

		ObjectUtil.toObject(map, data);

		if (data instanceof FX_COMMON) {
			initFX_COMMON(session, (FX_COMMON) data);
		}

		return data;
	}
	
	protected <T> T makeData(int userNo, Map<String, Object> map, Class<T> classOfT) throws Exception {

		T data = classOfT.newInstance();

		ObjectUtil.toObject(map, data);

		if (data instanceof FX_COMMON) {
			initFX_COMMON(userNo, (FX_COMMON) data);
		}

		return data;
	}

	protected void initFX_COMMON(SessionVo session, FX_COMMON item) {

		item.setRegDate(FxApi.getDate(0));
		item.setRegUserNo(session.getUserNo());
		item.setChgDate(FxApi.getDate(0));
		item.setChgUserNo(session.getUserNo());
	}

	protected void initFX_COMMON(int userNo, FX_COMMON item) {

		item.setRegDate(FxApi.getDate(0));
		item.setRegUserNo(userNo);
		item.setChgDate(FxApi.getDate(0));
		item.setChgUserNo(userNo);
	}

}

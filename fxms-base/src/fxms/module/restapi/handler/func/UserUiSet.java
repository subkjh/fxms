package fxms.module.restapi.handler.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.dbo.ui.FX_UI_BASIC;
import fxms.bas.dbo.ui.FX_UI_GROUP;
import fxms.bas.dbo.ui.FX_UI_PROPERTY;
import fxms.bas.fxo.FxCfg;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

/**
 * 운용자의 화면 구성을 설정하는 함수
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserUiSet {

	@SuppressWarnings("unchecked")
	public UserUiSet(SessionVo session, Map<String, Object> parameters) throws Exception {

		Map<String, Object> groupMap = (Map<String, Object>) parameters.get("group");
		List<Map<String, Object>> uiMapList = (List<Map<String, Object>>) parameters.get("basic-list");
		List<Map<String, Object>> propertyMapList = (List<Map<String, Object>>) parameters.get("property-list");

		FX_UI_BASIC ui;
		List<FX_UI_BASIC> uiList = new ArrayList<FX_UI_BASIC>();
		List<FX_UI_PROPERTY> propertyList = new ArrayList<FX_UI_PROPERTY>();

		FX_UI_GROUP uiGroup = new FX_UI_GROUP();
		ObjectUtil.toObject(groupMap, uiGroup);
		uiGroup.setChgDate(FxApi.getDate(0));
		uiGroup.setUserNo(session.getUserNo());

		for (Map<String, Object> uiMap : uiMapList) {
			ui = new FX_UI_BASIC();
			ObjectUtil.toObject(uiMap, ui);
			ui.setUserNo(session.getUserNo());
			ui.setUiGroupNo(uiGroup.getUiGroupNo());
			uiList.add(ui);
		}

		if (propertyMapList != null && propertyMapList.size() > 0) {
			FX_UI_PROPERTY property;
			for (Map<String, Object> map : propertyMapList) {
				property = new FX_UI_PROPERTY();
				ObjectUtil.toObject(map, property);
				if (property.getPropertyValue() != null && property.getPropertyValue().length() > 0) {
					propertyList.add(property);
					property.setUserNo(session.getUserNo());
					property.setUiGroupNo(uiGroup.getUiGroupNo());
				}
			}
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			Map<String, Object> deletePara = new HashMap<String, Object>();
			deletePara.put("userNo", session.getUserNo());
			deletePara.put("uiGroupNo", uiGroup.getUiGroupNo());

			tran.deleteOfClass(FX_UI_PROPERTY.class, deletePara);
			tran.deleteOfClass(FX_UI_BASIC.class, deletePara);
			tran.deleteOfClass(FX_UI_GROUP.class, deletePara);

			tran.insert(uiGroup);
			tran.insertOfClass(FX_UI_BASIC.class, uiList);
			tran.insertOfClass(FX_UI_PROPERTY.class, propertyList);
			
			tran.commit();
			
		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}
}

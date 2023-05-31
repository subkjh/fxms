package fxms.bas.impl.handler.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_UI_GRP;
import fxms.bas.impl.dbo.all.FX_UI_GRP_ITEM;
import fxms.bas.impl.dbo.all.FX_UI_GRP_ITEM_ATTR;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

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

		FX_UI_GRP_ITEM ui;
		List<FX_UI_GRP_ITEM> uiList = new ArrayList<FX_UI_GRP_ITEM>();
		List<FX_UI_GRP_ITEM_ATTR> propertyList = new ArrayList<FX_UI_GRP_ITEM_ATTR>();

		FX_UI_GRP uiGroup = new FX_UI_GRP();
		ObjectUtil.toObject(groupMap, uiGroup);
		uiGroup.setChgDtm(DateUtil.getDtm());
		uiGroup.setUserNo(session.getUserNo());

		for (Map<String, Object> uiMap : uiMapList) {
			ui = new FX_UI_GRP_ITEM();
			ObjectUtil.toObject(uiMap, ui);
			ui.setUserNo(session.getUserNo());
			ui.setUiGrpNo(uiGroup.getUiGrpNo());
			uiList.add(ui);
		}

		if (propertyMapList != null && propertyMapList.size() > 0) {
			FX_UI_GRP_ITEM_ATTR property;
			for (Map<String, Object> map : propertyMapList) {
				property = new FX_UI_GRP_ITEM_ATTR();
				ObjectUtil.toObject(map, property);
				if (property.getAttrVal() != null && property.getAttrVal().length() > 0) {
					propertyList.add(property);
					property.setUserNo(session.getUserNo());
					property.setUiGrpNo(uiGroup.getUiGrpNo());
				}
			}
		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			Map<String, Object> deletePara = new HashMap<String, Object>();
			deletePara.put("userNo", session.getUserNo());
			deletePara.put("uiGroupNo", uiGroup.getUiGrpNo());

			tran.deleteOfClass(FX_UI_GRP_ITEM_ATTR.class, deletePara);
			tran.deleteOfClass(FX_UI_GRP_ITEM.class, deletePara);
			tran.deleteOfClass(FX_UI_GRP.class, deletePara);

			tran.insertOfClass(FX_UI_GRP.class, uiGroup);
			tran.insertOfClass(FX_UI_GRP_ITEM.class, uiList);
			tran.insertOfClass(FX_UI_GRP_ITEM_ATTR.class, propertyList);

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

package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.UiDashbHandlerQid;
import fxms.bas.impl.dbo.all.FX_UI_DASHB;
import fxms.bas.impl.dbo.all.FX_UI_DASHB_ITEM;
import fxms.bas.impl.dbo.all.FX_UI_DASHB_ITEM_ATTR;
import fxms.bas.impl.handler.dto.uidashb.DeleteUiDashbItemAttrPara;
import fxms.bas.impl.handler.dto.uidashb.DeleteUiDashbItemPara;
import fxms.bas.impl.handler.dto.uidashb.SelectDashbInfo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.def.FxDaoCallBefore;

/**
 * 대시보드 관련 핸들러
 * 
 * @author subkjh
 *
 */
public class UiDashbHandler extends BaseHandler {

	public static void main(String[] args) throws Exception {
		UiDashbHandler handler = new UiDashbHandler();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dashbNo", 1);
		parameters.put("uiNo", 2);
		SessionVo session = new SessionVo("AAA", 1, "test", "test", USER_TYPE_CD.Operator, 0, 0);
		SelectDashbInfo item = handler.convert(session, parameters, SelectDashbInfo.class, true);
		System.out.println(handler.selectDashbInfo(session, item));
	}

	public Object add(SessionVo session, FX_UI_DASHB data) throws Exception {
		this.insert(data, new FxDaoCallBefore<FX_UI_DASHB>() {
			@Override
			public void onCall(ClassDao tran, FX_UI_DASHB data) throws Exception {

				int dashbNo = tran.getNextVal(FX_UI_DASHB.FX_SEQ_DASHBNO, Integer.class);

				data.setDashbNo(dashbNo);

			}
		});

		return data;
	}

	public Object addItem(SessionVo session, FX_UI_DASHB_ITEM data) throws Exception {
		insert(data, null);
		return data;
	}

	public Object addItemAttr(SessionVo session, FX_UI_DASHB_ITEM_ATTR data) throws Exception {
		insert(data, null);
		return data;
	}

	public Object delete(SessionVo session, FX_UI_DASHB data) throws Exception {
		this.delete(data, null);
		return data;
	}

	public Object deleteItem(SessionVo session, DeleteUiDashbItemPara data) throws Exception {
		delete(data, new FxDaoCallBefore<DeleteUiDashbItemPara>() {
			@Override
			public void onCall(ClassDao tran, DeleteUiDashbItemPara data) throws Exception {
				tran.deleteOfObject(DeleteUiDashbItemAttrPara.class, data);
			}
		});
		return data;
	}

	public Object selectDashbGridList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return selectListQid("select-dashb-grid-list", parameters);
	}

	public Object selectDashbInfo(SessionVo session, SelectDashbInfo data) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();

		Map<String, Object> para = FxApi.makePara("dashbNo", data.getDashbNo());
		List<FX_UI_DASHB> infoList = ClassDaoEx.SelectDatas(FX_UI_DASHB.class, para, FX_UI_DASHB.class);
		if (infoList.size() > 0) {
			ret.put("bashb", infoList.get(0));
			ret.put("items", ClassDaoEx.SelectDatas(FX_UI_DASHB_ITEM.class, para, FX_UI_DASHB_ITEM.class));
			ret.put("attrs", ClassDaoEx.SelectDatas(FX_UI_DASHB_ITEM_ATTR.class, para, FX_UI_DASHB_ITEM_ATTR.class));
		}

		return ret;
	}

	public Object update(SessionVo session, FX_UI_DASHB data) throws Exception {
		this.update(data, null);
		return data;
	}

	public Object updateItem(SessionVo session, FX_UI_DASHB_ITEM data) throws Exception {
		update(data, null);
		return data;
	}

	public Object updateItemAttr(SessionVo session, FX_UI_DASHB_ITEM_ATTR data) throws Exception {
		update(data, null);
		return data;
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(UiDashbHandlerQid.QUERY_XML_FILE));
	}
}

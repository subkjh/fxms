package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.UiHandlerQid;
import fxms.bas.impl.dbo.all.FX_UI_GRP;
import fxms.bas.impl.dbo.all.FX_UI_GRP_ITEM;
import fxms.bas.impl.dbo.all.FX_UI_GRP_ITEM_ATTR;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

public class UiHandler extends BaseHandler {

	public static void main(String[] args) {
		try {

			Map<String, Object> para = new HashMap<String, Object>();
			UiHandler handler = new UiHandler();
			SessionVo session = new SessionVo("AAA", 1, "test", "test", USER_TYPE_CD.Operator, 0, 0);
			handler.selectMyUiGroupList(session, para);
			handler.selectMyUiItemList(session, para);
			handler.selectMyUiAttrList(session, para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object deleteMyUiAttr(SessionVo session, FX_UI_GRP_ITEM_ATTR item) throws Exception {
		delete(item, null);
		return item;
	}

	public Object deleteMyUiGroup(SessionVo session, FX_UI_GRP item) throws Exception {
		delete(item, null);
		return item;
	}

	public Object deleteMyUiItem(SessionVo session, FX_UI_GRP_ITEM item) throws Exception {
		delete(item, null);
		return item;
	}

	/**
	 * 나의 화면 구성 속성 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object insertMyUiAttr(SessionVo session, FX_UI_GRP_ITEM_ATTR item) throws Exception {
		insert(item, null);
		return item;
	}

	/**
	 * 나의 화면 그룹 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object insertMyUiGroup(SessionVo session, FX_UI_GRP_ITEM item) throws Exception {
		insert(item, null);
		return item;
	}

	/**
	 * 나의 화면 구성 내역 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object insertMyUiItem(SessionVo session, FX_UI_GRP_ITEM item) throws Exception {
		insert(item, null);
		return item;
	}

	/**
	 * 사용자 메뉴 목록을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectMyMenuList(SessionVo session, Map<String, Object> parameters) throws Exception {

		Map<String, Object> wherePara = FxApi.makePara("userNo", session.getUserNo());

		return selectListQid("select-my-menu-list", makePara4Ownership(session, wherePara));

	}

	/**
	 * 사용자 기능 목록을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectMyOpList(SessionVo session, Map<String, Object> parameters) throws Exception {

		Map<String, Object> wherePara = FxApi.makePara("userNo", session.getUserNo());

		return selectListQid("select-my-op-list", makePara4Ownership(session, wherePara));

	}

	/**
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectMyUiAllList(SessionVo session, Map<String, Object> parameters) throws Exception {

		Map<String, Object> wherePara = FxApi.makePara("userNo", session.getUserNo());

		Map<String, Object> ret = new HashMap<String, Object>();
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			List<FX_UI_GRP> groupList = tran.select(FX_UI_GRP.class, wherePara);

			// 자신의 화면이 없으면 공용화면을 제공한다.
			if (groupList.size() == 0) {
				wherePara.put("userNo", 0);
				groupList = tran.select(FX_UI_GRP.class, wherePara);
			}

			List<FX_UI_GRP_ITEM> itemList = tran.select(FX_UI_GRP_ITEM.class, wherePara);
			ret.put("attr-list", tran.select(FX_UI_GRP_ITEM_ATTR.class, wherePara));
			ret.put("item-list", itemList);
			ret.put("group-list", groupList);

			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 나의 화면 구성 속성 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectMyUiAttrList(SessionVo session, Map<String, Object> parameters) throws Exception {
		Map<String, Object> wherePara = FxApi.makePara("userNo", session.getUserNo());
		return ClassDaoEx.SelectDatas(FX_UI_GRP_ITEM_ATTR.class, wherePara, FX_UI_GRP_ITEM_ATTR.class);
	}

	/**
	 * 나의 화면 그룹 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectMyUiGroupList(SessionVo session, Map<String, Object> parameters) throws Exception {
		Map<String, Object> wherePara = FxApi.makePara("userNo", session.getUserNo());
		return ClassDaoEx.SelectDatas(FX_UI_GRP.class, wherePara, FX_UI_GRP.class);
	}

	/**
	 * 나의 화면 구성 내역 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectMyUiItemList(SessionVo session, Map<String, Object> parameters) throws Exception {
		Map<String, Object> wherePara = FxApi.makePara("userNo", session.getUserNo());
		return ClassDaoEx.SelectDatas(FX_UI_GRP_ITEM.class, wherePara, FX_UI_GRP_ITEM.class);
	}

	public Object updateMyUiAttr(SessionVo session, FX_UI_GRP_ITEM_ATTR item) throws Exception {
		update(item, null);
		return item;
	}

	public Object updateMyUiGroup(SessionVo session, FX_UI_GRP item) throws Exception {
		update(item, null);
		return item;
	}

	public Object updateMyUiItem(SessionVo session, FX_UI_GRP_ITEM item) throws Exception {
		update(item, null);
		return item;
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(UiHandlerQid.QUERY_XML_FILE));
	}
}

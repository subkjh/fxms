package fxms.module.usertree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoOwnership;
import fxms.module.usertree.vo.UserTreeAttrVo;
import fxms.module.usertree.vo.UserTreeMoVo;
import fxms.module.usertree.vo.UserTreeVo;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;
import subkjh.bas.user.User.USER_TYPE;

/**
 * 사용자 개인 트리를 조회한다.
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserTreeItemSelector {

	public static void main(String[] args) {
		try {
			User user = new User();
			user.setUserNo(1);
			user.setUserType(USER_TYPE.admin.getCode());
			new UserTreeItemSelector().getUserTree(user, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 사용자 트리를 조회한다.
	 * 
	 * @param userNo
	 *            사용자 ID
	 * @param treeNo
	 *            트리번호
	 * @return 트리 맵. key = no
	 * @throws Exception
	 */
	public Map<Integer, UserTreeVo> getUserTree(User user, int treeNo) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			Map<Integer, UserTreeVo> map = selectUserTreeList(tran, treeNo);

			for (UserTreeVo rootTree : map.values()) {

				try {
					List<UserTreeMoVo> moList = selectTreeMo(tran, user, rootTree);
					new UserTreeMaker(rootTree, moList);

					Logger.logger.debug("tree-no={},mo-count={}", rootTree.getTreeNo(), moList.size());

				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

			return map;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 루트 트리가 가지는 대상 목옥을 조회한다.
	 * 
	 * @param tran
	 * @param rootTree
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<UserTreeMoVo> selectTreeMo(FxDaoExecutor tran, User user, UserTreeVo rootTree) throws Exception {

		Mo targetMo = MoApi.getApi().getMoClass(rootTree.getTargetMoClass()).newInstance();

		Map<String, Object> para = new HashMap<String, Object>();

		List<UserTreeMoVo> list = new ArrayList<UserTreeMoVo>();

		para.clear();
		for (UserTreeAttrVo attr : rootTree.getAttrList()) {
			attr.addWhereParameter(para);
		}

		List<Mo> moList = (List<Mo>) tran.select(targetMo.getClass(), para);
		for (Mo mo : moList) {
			if (user.getUserType() == USER_TYPE.admin.getCode()) {
				list.add(new UserTreeMoVo(mo));
			} else if (mo instanceof MoOwnership) {
				if (MoApi.getApi().isMemberLocation(((MoOwnership) mo).getInloNo(), user.getMngInloNo())) {
					list.add(new UserTreeMoVo(mo));
				}
			} else {
				list.add(new UserTreeMoVo(mo));
			}
		}

		return list;

	}

	/**
	 * 트리 정보과 이 트리의 하위 트리 정보를 조회한다.
	 * 
	 * @param treeNo
	 * @return
	 * @throws Exception
	 */
	private Map<Integer, UserTreeVo> selectUserTreeList(FxDaoExecutor tran, int treeNo) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();

		UserTreeVo tree, upperTree;
		List<UserTreeVo> treeList;

		if (treeNo > 0) {
			para.put("treeNo", treeNo);
			treeList = (List<UserTreeVo>) tran.select(UserTreeVo.class, para);
			para.remove("treeNo");
			para.put("upperTreeNo", treeNo);
			List<UserTreeVo> children = (List<UserTreeVo>) tran.select(UserTreeVo.class, para);
			treeList.addAll(children);
		} else {
			treeList = (List<UserTreeVo>) tran.select(UserTreeVo.class, para);
		}

		Map<Integer, UserTreeVo> tmpTreeMap = new HashMap<Integer, UserTreeVo>();
		Map<Integer, UserTreeVo> treeMap = new HashMap<Integer, UserTreeVo>();
		for (UserTreeVo e : treeList) {
			tmpTreeMap.put(e.getTreeNo(), e);
		}

		List<UserTreeAttrVo> attrList = (List<UserTreeAttrVo>) tran.select(UserTreeAttrVo.class, para);

		for (UserTreeAttrVo attr : attrList) {
			tree = tmpTreeMap.get(attr.getTreeNo());
			if (tree != null) {
				tree.getAttrList().add(attr);
			}
		}

		for (UserTreeVo e : treeList) {
			if (e.getUpperTreeNo() == 0) {
				treeMap.put(e.getTreeNo(), e);
			} else {
				upperTree = tmpTreeMap.get(e.getUpperTreeNo());
				if (upperTree == null) {
					treeMap.put(e.getTreeNo(), e);
				} else {
					upperTree.getChildren().add(e);
				}
			}

		}

		return treeMap;

	}

}

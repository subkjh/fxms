package fxms.module.usertree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLocation;
import fxms.bas.mo.property.MoOwnership;
import fxms.module.usertree.vo.UserTreeAttrVo;
import fxms.module.usertree.vo.UserTreeLocationVo;
import fxms.module.usertree.vo.UserTreeMoVo;
import fxms.module.usertree.vo.UserTreeVo;
import subkjh.bas.utils.ObjectUtil;

/**
 * root 트리에 포함된 MO를 하위 트리에 분배한다.
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserTreeMaker {

	private List<UserTreeMoVo> moList;
	private Class<?> classOfMo;

	/**
	 * 
	 * @param rootTree
	 *            root 트리
	 * @param moList
	 *            root가 가지고 있는 모든 MO
	 */
	public UserTreeMaker(UserTreeVo rootTree, List<UserTreeMoVo> moList) {

		this.moList = moList;

		classOfMo = MoApi.getApi().getMoClass(rootTree.getTargetMoClass());

		List<UserTreeAttrVo> upperAttrList = new ArrayList<UserTreeAttrVo>();

		try {

			if (rootTree.getChildren().size() == 0) {
				
				// 하위 트리가 없으면 root에게 모두 붙인다.
				for (UserTreeMoVo mo : moList) {
					if (mo.getParentList().size() == 0) {
						rootTree.getMoList().add(mo.getMo());
					}
				}
				
				return;
				
			} else {
				for (UserTreeVo childTree : rootTree.getChildren()) {
					setMoList(upperAttrList, childTree);
				}
			}

			// 다른곳에 표현되지 않은 MO를 root에 보인다.
			for (UserTreeMoVo mo : moList) {
				if (mo.getParentList().size() == 0) {
					rootTree.getMoList().add(mo.getMo());
				}
			}

//			print("", rootTree);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setMoList(List<UserTreeAttrVo> upperAttrList, UserTreeVo tree) {

		if (tree.getChildren().size() > 0) {
			// children이 존재하면 mo를 가지지 않는다.

			upperAttrList.addAll(tree.getAttrList());
			for (UserTreeVo child : tree.getChildren()) {
				setMoList(upperAttrList, child);
			}

		} else {

			Map<Long, UserTreeMoVo> containsMoMap = null;

			for (UserTreeAttrVo attr : upperAttrList) {
				containsMoMap = getMoMap(attr, containsMoMap);
				if (containsMoMap.size() == 0) {
					return;
				}
			}

			for (UserTreeAttrVo attr : tree.getAttrList()) {
				containsMoMap = getMoMap(attr, containsMoMap);
				if (containsMoMap.size() == 0) {
					return;
				}
			}

			// System.out.println(">>>> : " + containsEquipMap.size());

			if (tree.hasLocation() && MoOwnership.class.isAssignableFrom(classOfMo)) {
				// location이 존재하면 그 밑에 붙인다.
				MoOwnership loc;
				MoLocation location;
				UserTreeLocationVo treeLocation;

				for (UserTreeMoVo item : containsMoMap.values()) {
					loc = (MoOwnership) item.getMo();
					location = MoApi.getApi().getMoLocation(loc.getInloNo(), tree.getInloType());
					if (location != null) {
						treeLocation = tree.getLocationMap().get(location.getInloNo());
						if (treeLocation == null) {
							treeLocation = new UserTreeLocationVo(location);
							tree.getLocationMap().put(treeLocation.getInloNo(), treeLocation);
						}
						item.getParentList().add(treeLocation);
						treeLocation.getMoList().add(item.getMo());
					}
				}

			} else {

				for (UserTreeMoVo item : containsMoMap.values()) {
					item.getParentList().add(tree);
					tree.getMoList().add(item.getMo());
				}
			}
		}

	}

	private Map<Long, UserTreeMoVo> getMoMap(UserTreeAttrVo attr, Map<Long, UserTreeMoVo> containsMoMap) {

		List<UserTreeMoVo> retList = new ArrayList<UserTreeMoVo>();

		for (UserTreeMoVo mo : moList) {
			if (attr.match(mo.getMo())) {
				retList.add(mo);
			}
		}

		Map<Long, UserTreeMoVo> retMap = new HashMap<Long, UserTreeMoVo>();

		if (containsMoMap == null) {
			for (UserTreeMoVo vo : retList) {
				retMap.put(vo.getMo().getMoNo(), vo);
			}
			return retMap;
		} else {
			for (UserTreeMoVo vo : retList) {
				if (containsMoMap.get(vo.getMo().getMoNo()) != null) {
					retMap.put(vo.getMo().getMoNo(), vo);
				}
			}

			return retMap;
		}

	}

	void print(String tag, UserTreeVo tree) {

		System.out.println(tag + tree.getTreeName() + " (" + tree.getMoCountAll() + ")");
		// if (tree.getChildren().size() > 0) {
		// for (UserTreeVo child : tree.getChildren()) {
		// print(tag + "\t", child);
		// }
		// return;
		// }

		for (UserTreeAttrVo attr : tree.getAttrList()) {
			System.out.println(tag + "\tattr : " + attr);
		}

		tree.getMoList().sort(new Comparator<Mo>() {
			@Override
			public int compare(Mo arg0, Mo arg1) {
				return (int) (arg0.getMoNo() - arg1.getMoNo());
			}
		});

		for (UserTreeLocationVo loc : tree.getLocationMap().values()) {
			System.out.println(tag + "\tlocation : " + loc.getInloFname());
			for (Mo equip : loc.getMoList()) {
				System.out.println(tag + "\t\tequip : " + ObjectUtil.toMap(equip));
			}
		}

		for (Mo equip : tree.getMoList()) {
			System.out.println(tag + "\tequip : " + ObjectUtil.toMap(equip));
		}

		for (UserTreeVo child : tree.getChildren()) {
			print(tag + "\t", child);
		}
	}
}

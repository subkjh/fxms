package fxms.module.usertree.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.mo.Mo;
import fxms.module.usertree.dbo.FX_UR_TREE;

/**
 * 트리 ITEM VO
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserTreeVo extends FX_UR_TREE {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5062694655530834978L;

	private List<UserTreeAttrVo> attrList;
	private List<UserTreeVo> children;
	private List<Mo> moList;
	private Map<Integer, UserTreeLocationVo> locationMap;

	/**
	 * 트리의 속성 목록을 조회한다.
	 * 
	 * @return 트리 속성 목록
	 */
	public List<UserTreeAttrVo> getAttrList() {
		if (attrList == null) {
			attrList = new ArrayList<UserTreeAttrVo>();
		}

		return attrList;
	}

	/**
	 * 하위 트리를 조회한다.
	 * 
	 * @return 하위 트리 목록
	 */
	public List<UserTreeVo> getChildren() {
		if (children == null) {
			children = new ArrayList<UserTreeVo>();
		}

		return children;
	}

	/**
	 * 트리에 설치위치가 자동 추가되는 설치 위치 맵을 조회한다.
	 * 
	 * @return 설치위치 맵. key = no
	 */
	public Map<Integer, UserTreeLocationVo> getLocationMap() {
		if (locationMap == null) {
			locationMap = new HashMap<Integer, UserTreeLocationVo>();
		}

		return locationMap;
	}

	/**
	 * 
	 * @return 하위 트리를 포함하여 존재하는 모든 MO 수
	 */
	public int getMoCountAll() {
		if (getChildren().size() == 0) {
			return getMoList().size();
		}

		return getMoCountAll(getChildren());
	}

	/**
	 * tree에 붙을 MO 목록을 조회한다.
	 * 
	 * @return MO 목록
	 */
	public List<Mo> getMoList() {
		if (moList == null) {
			moList = new ArrayList<Mo>();
		}

		return moList;
	}

	/**
	 * 
	 * @return 설치 위치 포함 여부
	 */
	public boolean hasLocation() {
		return getInloType() != null && getInloType().length() > 0;
	}

	@Override
	public String toString() {
		return getUpperTreeNo() + " - " + getTreeNo();
	}

	private int getMoCountAll(List<UserTreeVo> childern) {
		int size = 0;

		for (UserTreeVo tree : childern) {
			if (tree.getChildren().size() == 0) {
				size += tree.getMoList().size();
			} else {
				size += getMoCountAll(tree.getChildren());
			}
		}

		return size;

	}
}

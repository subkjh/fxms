package fxms.bas.mo.child;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.mo.Mo;

/**
 * 노드의 자식 정보를 가지고 있는 객체
 * 
 * @author subkjh
 *
 */
public class MoConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 50761360114949545L;

	private Map<String, List<Mo>> children;

	private Mo parent;

	/** 수정된 MO 목록 */
	private List<Mo> moListChg = null;

	public MoConfig(Mo parent) {
		this.parent = parent;
	}

	public void addMo(Mo mo, boolean isDetected) {

		if (children == null) {
			children = new HashMap<String, List<Mo>>();
		}

		List<Mo> list = children.get(mo.getMoClass());

		if (list == null) {
			list = new ArrayList<Mo>();
			children.put(mo.getMoClass(), list);
		}

		if (isDetected) {

			mo.setUpperMoNo(parent.getMoNo());

			Mo moOld = find(mo);

			if (moOld != null && moOld.getMoNo() > 0) {
				mo.setStatus(FxEvent.STATUS.changed);
				removeChild(moOld);
				if (moListChg == null) {
					moListChg = new ArrayList<Mo>();
				}
				moListChg.add(moOld);
			} else {
				mo.setStatus(FxEvent.STATUS.added);
			}

		}

		list.add(mo);

	}

	public void addMo(MoConfig children, boolean detected) {

		if (children == null) {
			return;
		}

		for (Mo mo : children.getMoListAll()) {
			addMo(mo, detected);
		}
	}

	/**
	 * 탐색된 MO 목록을 추가 또는 수정합니다.
	 * 
	 * @param moList
	 *            탐색된 MO 목록
	 * @return 처리된 수량
	 */
	public int addMoListDetected(List<? extends Mo> moList) {
		if (moList == null || moList.size() == 0)
			return 0;

		renameMoNameIfDup(moList);

		for (Mo mo : moList)
			addMo(mo, true);

		return moList.size();
	}

	/**
	 * 기존 하위 목록에 존재하는지 찾아서 제공합니다.
	 * 
	 * @param mo
	 *            찾을 새로운 MO
	 * @return 찾은 기존 MO
	 */
	public Mo find(Mo mo) {
		if (children == null)
			return null;
		List<Mo> list = children.get(mo.getMoClass());

		if (list == null || list.size() == 0)
			return null;

		for (Mo e : list) {
			if (e.equalMo(mo))
				return e;
		}
		return null;
	}

	public int sizeAll() {
		int count = 0;

		if (children != null) {
			for (List<Mo> list : children.values()) {
				count += (list == null ? 0 : list.size());
			}
		}
		return count;
	}

	public String getChildrenString() {
		StringBuffer sb = new StringBuffer();
		sb.append("parent = " + parent);
		if (children != null) {
			for (String moClass : children.keySet()) {
				sb.append("\n" + moClass + "\n");
				for (Mo mo : children.get(moClass)) {
					sb.append("  " + mo + "\n");
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param moClass
	 *            MO_CLASS
	 * @return MO_CLASS에 해당되는 MO 수
	 */
	public int size(String moClass) {
		if (children == null || children.size() == 0)
			return 0;

		List<Mo> list = children.get(moClass);
		return list == null ? 0 : list.size();
	}

	public Mo getChild(String moClass, String moName) {
		List<? extends Mo> moList = getChildren(moClass);
		if (moList != null) {
			for (Mo mo : moList) {
				if (mo.getMoName().equals(moName))
					return mo;
			}
		}
		return null;
	}

	public List<String> getMoClassList() {
		List<String> moClassList = new ArrayList<String>();

		if (children != null) {
			for (String moClass : children.keySet()) {
				moClassList.add(moClass);
			}
		}

		return moClassList;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getChildren(Class<T> classOfT) {
		if (children == null)
			return null;
		try {
			Field field = classOfT.getField("MO_CLASS");
			String moClass = field.get(null).toString();
			return (List<T>) children.get(moClass);
		} catch (Exception e) {
		}

		return null;
	}

	public List<? extends Mo> getChildren(String moClass) {
		return children == null ? null : children.get(moClass);
	}

	public List<Mo> getMoListAll() {
		List<Mo> allList = new ArrayList<Mo>();

		if (children != null) {
			for (List<Mo> list : children.values()) {
				allList.addAll(list);
			}
		}

		return allList;
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();

		if (children != null) {
			for (String moClass : children.keySet()) {
				sb.append(moClass);
				sb.append("\n");
				for (Mo mo : children.get(moClass)) {
					sb.append("  ");
					sb.append(Logger.fill(mo.getMoName(), 50, '.') + mo.getStatus());
					sb.append("\n");
				}
			}

		}

		return sb.toString();
	}

	public Mo getParent() {
		return parent;
	}

	public Mo getChild(long moNo) {
		for (List<Mo> list : children.values()) {
			for (Mo mo : list) {
				if (mo.getMoNo() == moNo) {
					return mo;
				}
			}
		}

		return null;
	}

	public Mo removeChild(Mo child) {
		List<Mo> list = children.get(child.getMoClass());
		for (int index = list.size() - 1; index >= 0; index--) {
			if (list.get(index).getMoNo() == child.getMoNo()) {
				return list.remove(index);
			}
		}

		return null;
	}

	public void setStatusAllChildren(FxEvent.STATUS _beanStatusOld, FxEvent.STATUS _beanStatusNew, String _moClass) {

		if (children == null)
			return;

		boolean toChange = false;

		List<Mo> moList;
		if (_moClass == null) {
			for (String key : children.keySet()) {
				moList = children.get(key);
				if (moList != null) {
					for (Mo mo : moList) {
						toChange = _beanStatusOld == null || (mo.getStatus() == _beanStatusOld);
						if (toChange)
							mo.setStatus(_beanStatusNew);
					}
				}
			}
		} else {
			moList = children.get(_moClass);
			if (moList != null) {
				for (Mo mo : moList) {
					toChange = _beanStatusOld == null || (mo.getStatus() == _beanStatusOld);
					if (toChange)
						mo.setStatus(_beanStatusNew);
				}
			}
		}
	}

	public String toString() {
		return "SIZE(" + sizeAll() + ")";
	}

	/**
	 * 명칭이 중복된 경우 index를 붙여서 제공합니다.
	 * 
	 * @param map
	 * @param name
	 * @param index
	 * @return
	 */
	private String getName(Map<String, ?> map, String nameOrg, int index) {
		String name = nameOrg + " (" + index + ")";
		Object value = map.get(name);
		if (value == null)
			return name;
		return getName(map, nameOrg, index + 1);
	}

	/**
	 * 중복된 MO_NAME에 대해서 MO_NAME (2) 형식으로 rename합니다.
	 * 
	 * @param moList
	 */
	private void renameMoNameIfDup(List<? extends Mo> moList) {
		Map<String, String> moNameMap = new HashMap<String, String>();
		for (Mo mo : moList) {
			if (moNameMap.get(mo.getMoName()) == null) {
				moNameMap.put(mo.getMoName(), mo.getMoName());
			} else {
				mo.setMoName(getName(moNameMap, mo.getMoName(), 2));
				moNameMap.put(mo.getMoName(), mo.getMoName());
			}
		}
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("parent", parent);
		map.put("mo-class-list", getMoClassList());
		for (String moClass : getMoClassList()) {
			map.put(moClass + "-list", this.getChildren(moClass));
		}
		return map;
	}	
	

}

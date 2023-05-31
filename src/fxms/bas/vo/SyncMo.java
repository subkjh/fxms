package fxms.bas.vo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.event.FxEvent;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;

/**
 * 노드의 자식 정보를 가지고 있는 객체
 *
 * @author subkjh
 *
 */
public class SyncMo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 50761360114949545L;

	/**
	 * MO분류 기준 자식 목록
	 */
	private final Map<String, List<Mo>> childMap;

	private final Mo upper;

	/**
	 *
	 * @param upper
	 */
	public SyncMo(Mo upper) {
		this.upper = upper;
		this.childMap = new HashMap<>();
	}

	/**
	 * MO키를 이용하여 동일한 관리대상을 찾는다.
	 *
	 * @param moClass
	 * @param moKey
	 * @return
	 */
	private Mo find(String moClass, String moKey) {

		List<Mo> list = childMap.get(moClass);

		if (list == null || list.size() == 0)
			return null;

		for (Mo e : list) {
			if (moKey.equals(Mo.getMoKey(e))) {
				return e;
			}
		}
		return null;
	}

	private List<? extends Mo> getChildren(String moClass) {
		return childMap.get(moClass);
	}

	/**
	 * 명칭이 중복된 경우 index를 붙여서 제공합니다.
	 *
	 * @param map
	 * @param serviceName
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
	 * 목록에서 제거한다.
	 *
	 * @param child
	 * @return
	 */
	private Mo removeChild(Mo child) {

		List<Mo> list = childMap.get(child.getMoClass());

		for (int index = list.size() - 1; index >= 0; index--) {
			if (list.get(index).getMoNo() == child.getMoNo()) {
				return list.remove(index);
			}
		}

		return null;
	}

	/**
	 * 중복된 MO_NAME에 대해서 MO_NAME (2) 형식으로 rename합니다.
	 *
	 * @param moList
	 */
	private void renameMoNameIfDup(List<? extends Mo> moList) {
		Map<String, String> moNameMap = new HashMap<>();
		for (Mo mo : moList) {
			if (moNameMap.get(mo.getMoName()) == null) {
				moNameMap.put(mo.getMoName(), mo.getMoName());
			} else {
				mo.setMoName(getName(moNameMap, mo.getMoName(), 2));
				moNameMap.put(mo.getMoName(), mo.getMoName());
			}
		}
	}

	/**
	 * 탐색된 관리대상 추가
	 *
	 * @param mo
	 */
	public void addDetectedMo(Mo mo) {

		mo.setUpperMoNo(upper.getMoNo());

		// 기존 MO 찾기
		Mo moOld = find(mo.getMoClass(), Mo.getMoKey(mo));

		if (moOld != null && moOld.getMoNo() > 0) {

			mo.setMoNo(moOld.getMoNo());
			mo.setStatus(FxEvent.STATUS.changed);

			Mo removeMo = removeChild(moOld);
			Logger.logger.trace("remove old mo {}", removeMo);

		} else {

			mo.setStatus(FxEvent.STATUS.added);

		}

		// 목록에 추가
		addMo(mo);

	}

	/**
	 * 탐색된 MO 목록을 추가 또는 수정합니다.
	 *
	 * @param moList 탐색된 MO 목록
	 * @return 처리된 수량
	 */
	public int addDetectedMoList(List<? extends Mo> moList) {

		if (moList == null || moList.size() == 0)
			return 0;

		// 중복 정리
		renameMoNameIfDup(moList);

		// 탐색한 내용 추가함.
		for (Mo mo : moList) {
			addDetectedMo(mo);
		}

		return moList.size();
	}

	/**
	 * 관리대상 추가
	 *
	 * @param mo
	 */
	public void addMo(Mo mo) {

		List<Mo> list = childMap.get(mo.getMoClass());

		if (list == null) {
			list = new ArrayList<>();
			childMap.put(mo.getMoClass(), list);
		}

		list.add(mo);

	}

	public Mo getChild(long moNo) {
		for (List<Mo> list : childMap.values()) {
			for (Mo mo : list) {
				if (mo.getMoNo() == moNo) {
					return mo;
				}
			}
		}

		return null;
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

	@SuppressWarnings("unchecked")
	public <T> List<T> getChildren(Class<T> classOfT) {

		try {
			Field field = classOfT.getField("MO_CLASS");
			String moClass = field.get(null).toString();
			return (List<T>) childMap.get(moClass);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();

		for (String moClass : childMap.keySet()) {
			sb.append(moClass);
			sb.append("\n");
			for (Mo mo : childMap.get(moClass)) {
				sb.append("  ");
				sb.append(
						Logger.fill(mo.getMoNo() + "," + mo.getMoName() + "," + mo.getClass().getSimpleName(), 50, '.')
								+ mo.getStatus());
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	/**
	 *
	 * @return MO분류 목록
	 *
	 */
	private List<String> getMoClassList() {
		List<String> moClassList = new ArrayList<>();

		for (String moClass : childMap.keySet()) {
			moClassList.add(moClass);
		}

		return moClassList;
	}

	public List<Mo> getMoListAll() {

		List<Mo> allList = new ArrayList<>();

		for (List<Mo> list : childMap.values()) {
			allList.addAll(list);
		}

		return allList;
	}

	/**
	 * 관리대상의 상태를 변경한다.
	 * 
	 * @param _beanStatusOld
	 * @param _beanStatusNew
	 * @param _moClass
	 */
	public void setStatusAllChildren(FxEvent.STATUS _beanStatusOld, FxEvent.STATUS _beanStatusNew, String _moClass) {

		boolean toChange = false;

		List<Mo> moList;
		if (_moClass == null) {
			for (String key : childMap.keySet()) {
				moList = childMap.get(key);
				if (moList != null) {
					for (Mo mo : moList) {
						toChange = _beanStatusOld == null || (mo.getStatus() == _beanStatusOld);
						if (toChange)
							mo.setStatus(_beanStatusNew);
					}
				}
			}
		} else {
			moList = childMap.get(_moClass);
			if (moList != null) {
				for (Mo mo : moList) {
					toChange = _beanStatusOld == null || (mo.getStatus() == _beanStatusOld);
					if (toChange)
						mo.setStatus(_beanStatusNew);
				}
			}
		}
	}

	/**
	 * 하위 관리대상 건수를 제공한다.
	 *
	 * @return
	 */
	public int sizeAll() {
		int count = 0;

		for (List<Mo> list : childMap.values()) {
			count += (list == null ? 0 : list.size());
		}

		return count;
	}

	public String toLogString() {
		StringBuffer sb = new StringBuffer();
		for (String key : childMap.keySet()) {
			List<Mo> moList = childMap.get(key);
			if (moList != null) {
				if (sb.length() > 0)
					sb.append(",");
				sb.append(key).append("=").append(moList.size());
			}
		}

		return sb.toString();
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();

		map.put("upper", this);
		map.put("mo-class-list", getMoClassList());
		for (String moClass : getMoClassList()) {
			map.put(moClass + "-list", this.getChildren(moClass));
		}
		return map;
	}

	@Override
	public String toString() {
		return "SIZE(" + sizeAll() + ")";
	}

	public Mo getUpper() {
		return upper;
	}

}

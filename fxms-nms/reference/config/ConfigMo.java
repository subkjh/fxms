package com.daims.dfc.filter.config;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.service.notification.beans.NotiBean;
import subkjh.service.notification.beans.StatusCount;

import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoChild;
import com.daims.dfc.common.mo.MoImpl;
import com.daims.dfc.common.mo.MoNode;

/**
 * 구성MO<br>
 * 
 * 장비로부터 얻은 구성정보를 담는 클래스<br>
 * 
 * @author subkjh
 * 
 */
public class ConfigMo implements Serializable {

	/** CDP */
	public static final String ATTACH_OBJECT_KEY__LINK_CDP = "LINK_CDP";
	/** CDP */
	public static final String ATTACH_OBJECT_KEY__LINK_BRIDGE = "LINK_BRIDGE";
	/** HOST-MIB */
	public static final String ATTACH_OBJECT_KEY__HOST_MIB = "HOST-MIB";

	/**
	 * 
	 */
	private static final long serialVersionUID = 286963787850684130L;

	/** 추가할 내용 */
	private Map<String, Object> attachObjectMap;

	/** 하위 MO. key : MO_CLASS */
	private Map<String, List<Mo>> moMap;

	/** 노드 */
	private MoNode node;

	/** 수정된 MO 목록 */
	private List<Mo> moListChg = null;

	public ConfigMo() {

	}

	/**
	 * 
	 * @param node
	 *            노드
	 */
	public ConfigMo(MoNode node) {
		this.node = node;
	}

	/**
	 * 동기화 과정에서 찾은 내용을 추가합니다.
	 * 
	 * @param key
	 * @param attachObject
	 */
	public void addAttachObject(String key, Object attachObject) {
		if (attachObjectMap == null) attachObjectMap = new HashMap<String, Object>();
		attachObjectMap.put(key, attachObject);
	}

	/**
	 * 하위 MO 추가
	 * 
	 * @param mo
	 */
	public void addMo(Mo mo) {

		if (moMap == null) {
			moMap = new HashMap<String, List<Mo>>();
		}

		List<Mo> moList = moMap.get(mo.getMoClass());
		if (moList == null) {
			moList = new ArrayList<Mo>();
			moMap.put(mo.getMoClass(), moList);
		}

		moList.add(mo);
	}

	/**
	 * 탐색된 관리대상을 추가합니다.
	 * 
	 * @param moNew
	 *            탐색된 관리대상
	 * @return 이전 관리대상
	 */
	public Mo addMoDetected(Mo moNew) {

		if (node.getMoNo() > 0) moNew.setMoNoUpper(node.getMoNo());

		if (moNew instanceof MoImpl) {
			// 하위 장비의 등록은 장비의 sync한 시간으로 합니다.
			((MoImpl) moNew).setHstimeReg(node.getHstimeSync());
			((MoImpl) moNew).setHstimeSync(node.getHstimeSync());
		}

		if (moNew instanceof MoChild) {
			((MoChild<?>) moNew).setHstimeChg(node.getHstimeChg());
		}

		Mo moOld = find(moNew);

		if (moOld != null && moOld.getMoNo() > 0) {
			moNew.setUserChgAttr(moOld);
			moNew.setBeanStatus(Mo.CHANGE);
			removeMo(moOld);
			if (moListChg == null) {
				moListChg = new ArrayList<Mo>();
			}
			moListChg.add(moOld);
		}
		else {
			moNew.setBeanStatus(Mo.ADD);
		}

		addMo(moNew);

		return moOld;
	}

	/**
	 * 탐색된 MO 목록을 추가 또는 수정합니다.
	 * 
	 * @param moList
	 *            탐색된 MO 목록
	 * @return 처리된 수량
	 */
	public int addMoListDetected(List<? extends Mo> moList) {
		if (moList == null || moList.size() == 0) return 0;

		renameMoNameIfDup(moList);

		for (Mo mo : moList)
			addMoDetected(mo);

		return moList.size();
	}

	public void copy(ConfigMo configMo) {
		this.node = configMo.node;
		this.moMap = configMo.moMap;
		this.attachObjectMap = configMo.attachObjectMap;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConfigMo) {
			ConfigMo target = (ConfigMo) obj;
			return target.getNode().equals(node);
		}
		return super.equals(obj);
	}

	/**
	 * 부가정보를 조회합니다.
	 * 
	 * @param key
	 * @return
	 */
	public Object getAttachObject(String key) {
		return attachObjectMap == null ? null : attachObjectMap.get(key);
	}

	// /**
	// * 하위 MO를 제거합니다.
	// *
	// * @param moClass
	// * @param beanStatus
	// * @return
	// */
	// public int deleteMo4Status(String moClass, byte beanStatus) {
	// if (moMap == null) return 0;
	// List<? extends Mo> list = moMap.get(moClass);
	// int count = 0;
	// if (list != null) {
	// for (int i = list.size() - 1; i >= 0; i--)
	// if (list.get(i).getBeanStatus() == beanStatus) {
	// count++;
	// list.remove(i);
	// }
	// }
	//
	// return count;
	// }

	/**
	 * 
	 * @return
	 * @since 2013.07.17 by subkjh
	 */
	public String[] getAttachObjectMapKey() {
		return attachObjectMap == null ? null : attachObjectMap.keySet().toArray(new String[attachObjectMap.size()]);
	}

	/**
	 * 기존 하위 목록에 존재하는지 찾아서 제공합니다.
	 * 
	 * @param mo
	 *            찾을 새로운 MO
	 * @return 찾은 기존 MO
	 */
	public Mo find(Mo mo) {
		if (moMap == null) return null;
		List<Mo> list = moMap.get(mo.getMoClass());

		if (list == null || list.size() == 0) return null;

		for (Mo e : list) {
			if (e.match(mo)) return e;
		}
		return null;
	}

	/**
	 * 하위 MO를 조회합니다.
	 * 
	 * @param moClass
	 *            MO CLASS
	 * @param name
	 *            MO 명
	 * @return MO 검색된 MO
	 */
	public Mo getMo(String moClass, String name) {

		if (moMap == null) return null;
		List<Mo> list = moMap.get(moClass);

		if (list == null || list.size() == 0) return null;

		for (Mo mo : list) {
			if (mo.getMoName().equals(name)) return mo;
		}
		return null;
	}

	/**
	 * 수정되어 백업된 MO를 찾아 제공합니다.
	 * 
	 * @param moNo
	 *            찾을 MO번호
	 * @return 수정되어질 백업된 MO
	 */
	public Mo getMo4Old(long moNo) {
		if (moListChg == null) return null;
		for (Mo mo : moListChg) {
			if (mo.getMoNo() == moNo) return mo;
		}
		return null;
	}

	/**
	 * 구성된 하위MO에서 MO분류가 일치하고 필드명과 값이 같은 MO를 찾아 제공합니다.<br>
	 * 
	 * @param moClass
	 *            MO분류
	 * @param fieldName
	 *            필드명
	 * @param value
	 *            비교할 값
	 * @return 찾은 MO<br>
	 *         못 찾을 경우 null을 제공합니다.
	 */
	public Mo getMo(String moClass, String fieldName, Object value) {
		if (moMap == null) return null;
		List<Mo> list = moMap.get(moClass);

		if (list == null || list.size() == 0) return null;

		Object obj;
		for (Mo mo : list) {
			try {
				obj = mo.get(fieldName);
			}
			catch (Exception e) {
				continue;
			}

			if (obj.equals(value)) return mo;
		}
		return null;
	}

	/**
	 * MO번호의 MO를 찾아 제공합니다.
	 * 
	 * @param moNo
	 *            MO번호
	 * @return 찾은 MO
	 */
	public Mo getMo4No(long moNo) {

		if (node != null && node.getMoNo() == moNo) return node;

		if (moMap == null) return null;

		for (List<Mo> moList : moMap.values()) {
			if (moList != null) {
				for (Mo mo : moList) {
					if (mo.getMoNo() == moNo) return mo;
				}
			}
		}
		return null;
	}

	/**
	 * 입력된 MO_CLASS에서 SNMP_INDEX가 같은 MO를 찾습니다.
	 * 
	 * @param moClass
	 * @param snmpIndex
	 * @return
	 */
	public Mo getMo4SnmpIndex(String moClass, String snmpIndex) {
		if (getMoMap() == null) return null;
		List<Mo> list = getMoMap().get(moClass);
		if (list == null || list.size() == 0) return null;
		for (Mo mo : list) {
			if (mo instanceof MoChild) {
				MoChild<?> _mo = (MoChild<?>) mo;
				if (_mo.getSnmpIndex() != null && _mo.getSnmpIndex().equals(snmpIndex)) return mo;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return 하위 MO의 MO_CLASS 목록
	 */
	public List<String> getMoClassList() {
		List<String> list = new ArrayList<String>();
		if (moMap == null) return list;
		for (String key : moMap.keySet()) {
			list.add(key);
		}
		return list;
	}

	/**
	 * MO상태에 해당되는 MO의 수를 계산합니다.
	 * 
	 * @param beanStatus
	 *            MO상태
	 * @return MO상태에 해당되는 MO 수
	 */
	public StatusCount getMoCount() {

		StatusCount bean = new StatusCount();

		if (moMap == null) return bean;

		List<Mo> entry;
		for (String key : moMap.keySet()) {
			entry = moMap.get(key);
			if (entry != null) {
				for (Mo mo : entry) {
					bean.add(mo.getBeanStatus(), 1);
				}
			}
		}

		return bean;
	}

	/**
	 * 노드의 하위 MO중에서 moClass에 해당되는 MO의 수
	 * 
	 * @param moClass
	 * @return MO 수
	 */
	public int getMoCount4Class(String moClass) {
		List<? extends Mo> list = getMoList4Class(moClass);
		return list == null ? 0 : list.size();
	}

	/**
	 * 
	 * @return 전체 하위 MO 수
	 */
	public int getMoCountAll() {
		if (moMap == null) return 0;
		int size = 0;
		for (List<?> list : moMap.values())
			size += list.size();
		return size;
	}

	/**
	 * 상태에 해당되는 하위 MO을 제공합니다.
	 * 
	 * @param beanStatus
	 *            상태
	 * @return 상태에 해당되는 전체 하위 MO 목록
	 */
	public List<Mo> getMoList(byte beanStatus) {
		if (moMap == null) return null;

		List<Mo> ret = new ArrayList<Mo>();
		List<Mo> entry;
		for (String key : moMap.keySet()) {
			entry = moMap.get(key);
			if (entry != null) {
				for (Mo mo : entry) {
					if (mo.getBeanStatus() == beanStatus) {
						ret.add(mo);
					}
				}
			}
		}

		return ret;
	}

	/**
	 * 노드의 하위 MO중에서 moClass에 해당되는 MO 목록<br>
	 * 없을 경우 null
	 * 
	 * @param classOfT
	 *            조회할 클래스<br>
	 *            MO_CLASS 필드가 정의되어 있어야 합니다.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getMoList(Class<T> classOfT) {
		if (moMap == null) return null;
		try {
			Field field = classOfT.getField("MO_CLASS");
			String moClass = field.get(null).toString();
			return (List<T>) moMap.get(moClass);
		}
		catch (Exception e) {
		}

		return null;
	}

	/**
	 * 노드의 하위 MO중에서 moClass에 해당되는 MO 목록<br>
	 * 없을 경우 null
	 * 
	 * @param moClass
	 * @return MO목록
	 */
	public List<Mo> getMoList4Class(String moClass) {
		if (moMap == null) return null;
		return moMap.get(moClass);
	}

	/**
	 * 입력된 MO_CLASS와 객체 상태가 일치되는 하위 MO 목록
	 * 
	 * @param moClass
	 * @param beanStatus
	 * @return
	 */
	public List<? extends Mo> getMoList4ClassStatus(String moClass, byte beanStatus) {
		if (moMap == null) return null;
		List<? extends Mo> list = moMap.get(moClass);
		List<Mo> retList = new ArrayList<Mo>();
		if (list != null) {
			for (Mo mo : list)
				if (mo.getBeanStatus() == beanStatus) retList.add(mo);
		}

		return retList;
	}

	/**
	 * 자식 관리대상에 변경이 있는지 확인합니다.
	 * 
	 * @return 변동사항 발생이면 true
	 */
	public boolean isChangedChildren() {
		List<Mo> moList = getMoListAll();
		if (moList == null || moList.size() == 0) return false;

		for (Mo mo : moList) {
			if (mo.getBeanStatus() == Mo.ADD || mo.getBeanStatus() == Mo.DELETE || mo.getBeanStatus() == Mo.CHANGE)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @return 전체 하위 MO 목록
	 */
	public List<Mo> getMoListAll() {
		if (moMap == null) return null;

		List<Mo> ret = new ArrayList<Mo>();
		List<Mo> entry;
		for (String key : moMap.keySet()) {
			entry = moMap.get(key);
			if (entry != null) ret.addAll(entry);
		}

		return ret;
	}

	public List<Mo> getMoListChg() {
		return moListChg;
	}

	/**
	 * MO_CLASS가 키인 MO 맵
	 * 
	 * @return MO맵
	 */
	public Map<String, List<Mo>> getMoMap() {
		return moMap;
	}

	/**
	 * 
	 * @return 노드
	 */
	public MoNode getNode() {
		return node;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * MoConf에 해당되는 내용이 존재하는지 여부를 확인합니다.
	 * 
	 * @return 존재여부
	 */
	public boolean hasMoConf() {
		String keys[] = getAttachObjectMapKey();
		if (keys == null || keys.length == 0) return false;

		// MoConf 존재여부 확인
		boolean isExist = false;
		for (String key : keys) {
			if (key.startsWith("MoConf_") == false) continue;
			isExist = true;
			break;
		}
		return isExist;
	}

	/**
	 * @return 동기화 가능 여부
	 */
	public boolean isSyncable() {
		return node != null && node.isManaged() && node.isConfigSync();
	}

	/**
	 * 목록에서 입력된 내용을 삭제합니다.
	 * 
	 * @param child
	 *            삭제할 MO
	 * @return 삭제된 MO
	 */
	public Mo removeMo(Mo child) {
		List<Mo> list = moMap.get(child.getMoClass());
		for (int index = list.size() - 1; index >= 0; index--) {
			if (list.get(index).getMoNo() == child.getMoNo()) {
				return list.remove(index);
			}
		}

		return null;
	}

	/**
	 * 
	 * @param node
	 */
	public void setNode(MoNode node) {
		this.node = node;
	}

	/**
	 * 모든 MO의 상태를 설정합니다.
	 * 
	 * @param _beanStatusOld
	 *            이전상태<br>
	 *            null이면 이전상태 고려하지 않지만 값이 있으면 그 상태의 경우만 변경함.
	 * @param _beanStatusNew
	 *            변경할 상태
	 * @param _moClass
	 *            MO_CLASS<br>
	 *            null이면 전체를 대상으로 값이 있으면 해당 MO들만 처리합니다.
	 * @since 2013.01.29 by subkjh
	 */
	public void setStatusAllChildren(Byte _beanStatusOld, byte _beanStatusNew, String _moClass) {

		if (moMap == null) return;

		boolean toChange = false;

		List<Mo> moList;
		if (_moClass == null) {
			for (String key : moMap.keySet()) {
				moList = moMap.get(key);
				if (moList != null) {
					for (Mo mo : moList) {
						toChange = _beanStatusOld == null || (mo.getBeanStatus() == _beanStatusOld);
						if (toChange) mo.setBeanStatus(_beanStatusNew);
					}
				}
			}
		}
		else {
			moList = moMap.get(_moClass);
			if (moList != null) {
				for (Mo mo : moList) {
					toChange = _beanStatusOld == null || (mo.getBeanStatus() == _beanStatusOld);
					if (toChange) mo.setBeanStatus(_beanStatusNew);
				}
			}
		}
	}

	@Override
	public String toString() {
		return (node == null ? "" : node.toString()) + toStringChildren();
	}

	/**
	 * 
	 * @return 하위MO 조건별 수
	 */
	public String toStringChildren() {

		if (moMap == null) return "No Children";

		int count[] = new int[4];
		StringBuffer sb = new StringBuffer();

		List<Mo> list;
		for (String key : moMap.keySet()) {
			list = moMap.get(key);

			for (int index = 0; index < count.length; index++)
				count[index] = 0;

			for (Mo mo : list) {
				if (mo.getBeanStatus() == NotiBean.BEAN_STATUS_ADD) count[0]++;
				else if (mo.getBeanStatus() == NotiBean.BEAN_STATUS_CHANGE) count[1]++;
				else if (mo.getBeanStatus() == NotiBean.BEAN_STATUS_DELETE) count[2]++;
				else if (mo.getBeanStatus() == NotiBean.BEAN_STATUS_NOTHING) count[3]++;
			}

			sb.append("\nMO-CLASS(" + key + ")");
			sb.append("TCUDN(");
			sb.append(list.size() + "," + count[0] + "," + count[1] + "," + count[2] + "," + count[3] + ")");
		}

		return sb.toString();
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
		if (value == null) return name;
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
			}
			else {
				mo.setMoName(getName(moNameMap, mo.getMoName(), 2));
				moNameMap.put(mo.getMoName(), mo.getMoName());
			}
		}
	}
}

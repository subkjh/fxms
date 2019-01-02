package fxms.bas.mo.attr;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.mo.Mo;

/**
 * 관리대상 적재 클래스
 * 
 * @author SUBKJH-DEV
 *
 * @param <MO>
 */
public abstract class MoLoader<MO extends Mo> {

	private String moClass;
	private Map<String, Object> para;
	private Map<String, MO> moMap;

	public MoLoader(String moClass, Map<String, Object> para) {
		this.moClass = moClass;
		this.para = para;
		moMap = new HashMap<String, MO>();
	}

//	public MoLoader(String moClass, Object... parameters) {
//		this.moClass = moClass;
//		para = new HashMap<String, Object>();
//
//		for (int i = 0; i < parameters.length; i += 2) {
//			if (i + 1 < parameters.length) {
//				para.put(parameters[i].toString(), parameters[i + 1]);
//			}
//		}
//		moMap = new HashMap<String, MO>();
//	}

	@SuppressWarnings("unchecked")
	public void addMo(Mo mo) {
		moMap.put(getKey((MO) mo), (MO) mo);
	}

	public abstract String getKey(MO mo);

	public MO getMo(String key) {
		return moMap.get(key);
	}

	public String getMoClass() {
		return moClass;
	}

	public Collection<MO> getMoList() {
		return moMap.values();
	}

	public Map<String, Object> getPara() {
		return para;
	}

	public void putPara(String name, Object value) {
		if (para == null) {
			para = new HashMap<String, Object>();
		}

		para.put(name, value);
	}

	public void setMoList(List<MO> moList) {

		moMap.clear();

		String key;
		for (MO mo : moList) {
			key = getKey(mo);
			moMap.put(key, mo);
		}
	}

	public int size() {
		return moMap.size();
	}

}

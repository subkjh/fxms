package com.daims.dfc.filter.config.model.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 동적구성 정의 객체
 * 
 * @author subkjh
 * 
 */
public class ModelConf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -197342639185512050L;

	private int modelNo;
	private String confClass;
	private String javaClassName;
	private String oidIndex;

	private List<ModelConfA> moOidList;

	public void add(ModelConfA moOid) {
		if (moOidList == null) {
			moOidList = new ArrayList<ModelConfA>();
		}

		moOidList.add(moOid);
	}

	public Object createObjectBean() throws Exception {
		return Class.forName(javaClassName).newInstance();
	}

	public String getConfClass() {
		return confClass;
	}

	public String getJavaClassName() {
		return javaClassName;
	}

	public int getModelNo() {
		return modelNo;
	}

	public List<ModelConfA> getMoOidList() {
		return moOidList;
	}

	public String getOidIndex() {
		return oidIndex;
	}

	public boolean hasIndex() {
		return oidIndex != null && oidIndex.trim().length() > 0;
	}

	public void setConfClass(String confClass) {
		this.confClass = confClass;
	}

	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	public void setMoOidList(List<ModelConfA> moOidList) {
		this.moOidList = moOidList;
	}

	public void setOidIndex(String oidIndex) {
		this.oidIndex = oidIndex;
	}

}

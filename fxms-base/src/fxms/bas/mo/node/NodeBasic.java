package fxms.bas.mo.node;

import java.io.Serializable;

import fxms.bas.mo.attr.Model;

/**
 * 간단한 노드 정보
 * 
 * @author subkjh
 * 
 */
public class NodeBasic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5466861365651201096L;

	/** 장비모델 */
	private Model model;

	/** 기본정보를 조회한 서버 주소 */
	private String pollingOwner;

	/** SysDescr */
	private String sysDescr;

	/** sysLocation */
	private String sysLocation;

	/** SysName */
	private String sysName;

	/** SysObjectID */
	private String sysObjectId;

	public Model getModel() {
		return model;
	}

	public String getPollingOwner() {
		return pollingOwner;
	}

	public String getSysDescr() {
		return sysDescr;
	}

	public String getSysLocation() {
		return sysLocation;
	}

	public String getSysName() {
		return sysName;
	}

	public String getSysObjectId() {
		return sysObjectId;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setPollingOwner(String pollingOwner) {
		this.pollingOwner = pollingOwner;
	}

	public void setSysDescr(String sysDescr) {
		this.sysDescr = sysDescr;
	}

	public void setSysLocation(String sysLocation) {
		this.sysLocation = sysLocation;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public void setSysObjectId(String sysObjectId) {
		this.sysObjectId = sysObjectId;
	}

	@Override
	public String toString() {
		return "SYS-OBJECT-ID(" + sysObjectId + ")SYS-NAME(" + sysName + ")SYS-DESCR(" + sysDescr + ")";
	}

}

package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;

public class PsValues implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6393723671639156825L;

	/** 관리대상 */
	private final long moNo;
	private final Mo mo;
	/** 성능번호 */
	private final PsItemSimple psItem;
	private final List<PsValue> values;

	public PsValues(Mo mo, PsItem psItem) {
		this.mo = mo;
		this.moNo = mo.getMoNo();
		this.psItem = new PsItemSimple(psItem);
		this.values = new ArrayList<PsValue>();
	}

	public String getKey() {
		return moNo + ":" + psItem.getPsId();
	}

	public Mo getMo() {
		return mo;
	}

	public long getMoNo() {
		return moNo;
	}

	public PsItemSimple getPsItem() {
		return psItem;
	}

	public List<Number> getValueOnly() {
		List<Number> ret = new ArrayList<Number>();
		for (PsValue o : this.values) {
			ret.add(o.getValue());
		}
		return ret;
	}

	public List<PsValue> getValues() {
		return values;
	}

}

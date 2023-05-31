package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.ValueApi.StatFunction;

/**
 * 
 * @author subkjh
 *
 */
public class PsValuesGroupBy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7365722344667925869L;

	private final String statFunc;
	private final PsItemSimple psItem;
	private final List<PsValue> values;

	public String getKey() {
		return this.psItem.getPsId() + ":" + statFunc;
	}

	public PsValuesGroupBy(PsItem psItem, StatFunction stat) {
		this.values = new ArrayList<>();
		this.psItem = new PsItemSimple(psItem);
		this.statFunc = stat.name();
	}

	public String getStatFunc() {
		return statFunc;
	}

	public PsItemSimple getPsItem() {
		return psItem;
	}

	public List<PsValue> getValues() {
		return values;
	}

	public int size() {
		return this.values.size();
	}

}

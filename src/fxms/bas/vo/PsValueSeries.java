package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 *
 */
public class PsValueSeries implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9214599549314090985L;

	private long moNo;

	private Mo mo;

	private String psDataCd;

	private String psStatFuncArray[];

	private PsItem psItem;

	/** 일시, 값1, 값2 ... 형식 */
	private List<Number[]> valueList;

	public PsValueSeries() {
		valueList = new ArrayList<Number[]>();
	}

	public void add(long time, Number[] value) {

		Number entry[] = new Number[value.length + 1];
		entry[0] = time;
		for (int i = 0; i < value.length; i++) {
			entry[i + 1] = value[i];
		}

		valueList.add(entry);
	}

	public String getDebug() {
		StringBuffer sb = new StringBuffer();
		sb.append(Logger.fill('.', 20, String.valueOf(moNo)));
		sb.append("\n");
		sb.append(Logger.fill('.', 20, psItem.getPsName()));
		sb.append("\n");

		sb.append("date ");
		for (String func : psStatFuncArray) {
			sb.append(func).append(" ");
		}
		sb.append("\n");

		Number entry[];
		for (int i = 0; i < size(); i++) {
			entry = valueList.get(i);
			for (Number value : entry) {
				sb.append(value).append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public long getMoNo() {
		return moNo;
	}

	public String getPsDataCd() {
		return psDataCd;
	}

	public List<Number[]> getValueList() {
		return valueList;
	}

	public void setMoNo(long moNo) {
		this.moNo = moNo;
	}

	public void setPsDataCd(String psDataCd) {
		this.psDataCd = psDataCd;
	}

	public int size() {
		return valueList.size();
	}

	public String[] getPsStatFuncArray() {
		return psStatFuncArray;
	}

	public void setPsStatFuncArray(String[] psStatFuncArray) {
		this.psStatFuncArray = psStatFuncArray;
	}

	public PsItem getPsItem() {
		return psItem;
	}

	public void setPsItem(PsItem psItem) {
		this.psItem = psItem;
	}

	public Mo getMo() {
		return mo;
	}

	public void setMo(Mo mo) {
		this.mo = mo;
	}

}

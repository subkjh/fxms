package fxms.bas.pso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Mo;
import fxms.bas.noti.FxEvent;

/**
 * 수집된 내용을 가지고 있는 객체
 * 
 * @author subkjh
 * @since 2013.05.25
 */
public class VoList extends ArrayList<PsVo> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1845632882337709356L;

	/** 수집시전의 시간 */
	private long mstime;

	/** 저장후 보낼 노티 */
	private FxEvent sign;

	/** 수집한 주체 */
	private String owner;

	public VoList clone() {
		VoList ret = new VoList(owner, mstime);
		ret.sign = sign;
		ret.addAll(this);
		return ret;
	}

	public VoList(String owner, long mstime) {
		this.owner = owner;
		this.mstime = mstime;
	}

	public VoList(String owner, long mstime, List<PsVo> valueList, FxEvent sign) {
		this.owner = owner;
		this.mstime = mstime;
		this.sign = sign;
		this.addAll(valueList);
	}

	/**
	 * 수집된 값을 추가합니다.
	 * 
	 * @param value
	 */
	public void add(List<PsVo> value) {
		if (value != null && value.size() > 0) {
			addAll(value);
		}
	}

	public PsVo add(Mo mo, String psCode, Number value) {
		
		if (mo == null || psCode == null || value == null)
			return null;
		
		PsVo val = new PsVo(mo, null, psCode, value);
		
		add(val);
		
		return val;
	}
	
	public PsVo add(Mo mo, Enum<?> psCode, Number value) {
		
		if (mo == null || psCode == null || value == null)
			return null;
		
		PsVo val = new PsVo(mo, null, psCode.name(), value);
		
		add(val);
		
		return val;
	}

	public PsVo add(Mo mo, String instance, String psCode, Number value) {
		if (mo == null || psCode == null || value == null)
			return null;

		PsVo val = new PsVo(mo, instance, psCode, value);
		add(val);
		return val;
	}
	
	public PsVo add(Mo mo, String instance, Enum<?> psCode, Number value) {
		if (mo == null || psCode == null || value == null)
			return null;

		PsVo val = new PsVo(mo, instance, psCode.name(), value);
		add(val);
		return val;
	}

	public long getMstime() {
		return mstime;
	}

	public String getOwner() {
		return owner;
	}

	public FxEvent getSign() {
		return sign;
	}

	public void setMstime(long mstime) {
		this.mstime = mstime;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setSign(FxEvent sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();

		sb.append("OWNER(" + owner + ")");
		sb.append("MSTIME(" + mstime + ")");
		sb.append("SIZE(" + size() + ")");
		if (sign != null)
			sb.append("SIGNAL(" + sign + ")");

		return sb.toString();
	}

}

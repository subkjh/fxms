package fxms.bas.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fxms.bas.event.FxEvent;
import fxms.bas.mo.Mo;

/**
 * 수집된 원천 데이터 목록
 * 
 * @author subkjh
 * @since 2013.05.25
 */
public class PsVoRawList extends ArrayList<PsVoRaw> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -163312024739423203L;

	/** 수집 시간 */
	private long mstime;

	/** 저장후 보낼 노티 */
	private FxEvent sign;

	/** 수집한 주체 */
	private String owner;

	public PsVoRawList(String owner, long mstime) {
		this.owner = owner;
		this.mstime = mstime;
	}

	public PsVoRawList(String owner, long mstime, List<PsVoRaw> valueList, FxEvent sign) {
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
	public void add(List<PsVoRaw> value) {
		if (value != null && value.size() > 0) {
			addAll(value);
		}
	}

	public PsVoRaw add(Mo mo, String instance, Enum<?> psId, Number value) {
		if (mo == null || psId == null || value == null)
			return null;

		PsVoRaw val = new PsVoRaw(mo, psId, value, instance);
		add(val);
		return val;
	}

	public PsVoRaw add(long moNo, String psId, Number value) {
		if (psId == null || value == null)
			return null;

		PsVoRaw val = new PsVoRaw(moNo, psId, value);
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

		SimpleDateFormat FMT = new SimpleDateFormat("yyyyMMddHHmmss");

		StringBuffer sb = new StringBuffer();

		sb.append(owner).append(",").append(mstime).append(":").append(FMT.format(new Date(mstime)));
		sb.append(",datas=").append(size());

		return sb.toString();
	}

}

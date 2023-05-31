package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.event.FxEvent;
import fxms.bas.mo.Moable;
import subkjh.bas.co.utils.DateUtil;

/**
 * 유효한 PsVoRaw 목록
 * 
 * @author subkjh
 * @since 2013.05.25
 */
public class PsVoList extends ArrayList<PsVo> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6755167051814758245L;

	/** 수집 시간 */
	private final long mstime;
	private final long hstime;

	/** 저장후 보낼 노티 */
	private final FxEvent sign;

	/** 수집한 주체 */
	private final String owner;

	public PsVoList(String owner, long mstime, List<PsVo> valueList) {
		this.owner = owner;
		this.mstime = mstime;
		this.hstime = DateUtil.getDtm(mstime);
		this.sign = null;
		if (valueList != null) {
			this.addAll(valueList);
		}
	}

	public PsVoList(PsVoRawList raw) {
		this.owner = raw.getOwner();
		this.mstime = raw.getMstime();
		this.hstime = DateUtil.getDtm(mstime);
		this.sign = raw.getSign();
	}

	public void add(Number value, Moable mo, PsItem psItem, String moInstance) {
		this.add(new PsVo(value, mo, psItem, moInstance));
	}

	public long getMstime() {
		return mstime;
	}

	public long getHstime() {
		return hstime;
	}

	public FxEvent getSign() {
		return sign;
	}

	public String getOwner() {
		return owner;
	}

}

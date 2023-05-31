package fxms.bas.mo;

import java.io.Serializable;

import fxms.bas.event.FxEventImpl;
import fxms.bas.fxo.FxObject;

/**
 * Managed Object
 *
 * @author subkjh
 *
 */
public class MoImpl extends FxEventImpl implements FxObject, Cloneable, Serializable, Moable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6674720480671960265L;

	/** MO번호 */
	private final long moNo;

	/** MO명 */
	private final String moName;

	private final String moClass;

	private final String moType;

	private final long upperMoNo;

	private final int alarmCfgNo;

	private final int inloNo;

	public MoImpl(Moable mo) {
		this(mo, STATUS.raw);
	}

	public MoImpl(Moable mo, STATUS status) {

		super("mo", status);

		this.moNo = mo.getMoNo();
		this.moName = mo.getMoName();
		this.moClass = mo.getMoClass();
		this.moType = mo.getMoType();
		this.upperMoNo = mo.getUpperMoNo();
		this.alarmCfgNo = mo.getAlarmCfgNo();
		this.inloNo = mo.getInloNo();
	}

	@Override
	public long getMoNo() {
		return moNo;
	}

	@Override
	public String getMoName() {
		return moName;
	}

	@Override
	public String getMoClass() {
		return moClass;
	}

	@Override
	public long getUpperMoNo() {
		return upperMoNo;
	}

	@Override
	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	public String getMoType() {
		return moType;
	}

	@Override
	public int getInloNo() {
		return inloNo;
	}

}
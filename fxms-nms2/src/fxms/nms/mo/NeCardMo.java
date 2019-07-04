package fxms.nms.mo;

import fxms.bas.impl.mo.FxMo;
import fxms.bas.mo.property.MoNeedManager;

public class NeCardMo extends FxMo implements MoNeedManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5542356707091446383L;
	/**
	 * MO분류. INTERFACE
	 */
	public static final String MO_CLASS = "CARD";

	@Override
	public long getManagerMoNo() {
		return getUpperMoNo();
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

	private String cardId;
	private String cardDescr;
	private String cardModelName;
	private String cardType;
	private int portCount;
	private String serialNo;
	private int upperSlotNo = -1;
	private int slotNo = -1;
	private String chassisId;
	private String verFw;
	private String verSw;
	private String snmpIndex;

	public String getVerSw() {
		return verSw;
	}

	public void setVerSw(String verSw) {
		this.verSw = verSw;
	}

	public String getSnmpIndex() {
		return snmpIndex;
	}

	public void setSnmpIndex(String snmpIndex) {
		this.snmpIndex = snmpIndex;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardDescr() {
		return cardDescr;
	}

	public void setCardDescr(String cardDescr) {
		this.cardDescr = cardDescr;
	}

	public String getCardModelName() {
		return cardModelName;
	}

	public void setCardModelName(String cardModelName) {
		this.cardModelName = cardModelName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getPortCount() {
		return portCount;
	}

	public void setPortCount(int portCount) {
		this.portCount = portCount;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public int getUpperSlotNo() {
		return upperSlotNo;
	}

	public void setUpperSlotNo(int upperSlotNo) {
		this.upperSlotNo = upperSlotNo;
	}

	public int getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(int slotNo) {
		this.slotNo = slotNo;
	}

	public String getChassisId() {
		return chassisId;
	}

	public void setChassisId(String chassisId) {
		this.chassisId = chassisId;
	}

	public String getVerFw() {
		return verFw;
	}

	public void setVerFw(String verFw) {
		this.verFw = verFw;
	}

}

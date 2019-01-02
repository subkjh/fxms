package com.fxms.nms.dbo;

import java.io.Serializable;

import fxms.bas.mo.Mo;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

/**
 * @since 2018.03.27 16:50
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FN_MO_CARD", comment = "장비카드테이블")
@FxIndex(name = "FN_MO_CARD__PK", type = INDEX_TYPE.PK, columns = { "MO_NO" })
@FxIndex(name = "FN_MO_CARD__FK_MO", type = INDEX_TYPE.FK, columns = { "MO_NO" }, fkTable = "FX_MO", fkColumn = "MO_NO")
public class FN_MO_CARD extends Mo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7495606776310936335L;

	@FxColumn(name = "CARD_ID", size = 50, nullable = true, comment = "카드ID")
	private String cardId;

	@FxColumn(name = "CARD_DESCR", size = 400, nullable = true, comment = "카드설명")
	private String cardDescr;

	@FxColumn(name = "CARD_MODEL_NAME", size = 100, nullable = true, comment = "카드모델설명")
	private String cardModelName;

	@FxColumn(name = "CARD_TYPE", size = 100, nullable = true, comment = "모델카드")
	private String cardType;

	@FxColumn(name = "PORT_COUNT", size = 9, nullable = true, comment = "포트수")
	private int portCount;

	@FxColumn(name = "SERIAL_NO", size = 100, nullable = true, comment = "시리얼번호")
	private String serialNo;

	@FxColumn(name = "UPPER_SLOT_NO", size = 9, nullable = true, comment = "상위슬롯번호", defValue = "-1")
	private int upperSlotNo = -1;

	@FxColumn(name = "SLOT_NO", size = 9, nullable = true, comment = "슬롯번호", defValue = "-1")
	private int slotNo = -1;

	@FxColumn(name = "CHASSIS_ID", size = 50, nullable = true, comment = "샤시스ID")
	private String chassisId;

	@FxColumn(name = "VER_FW", size = 100, nullable = true, comment = "FW버전")
	private String verFw;

	public String getChassisId() {
		return chassisId;
	}

	public void setChassisId(String chassisId) {
		this.chassisId = chassisId;
	}

	@FxColumn(name = "VER_SW", size = 100, nullable = true, comment = "SW버전")
	private String verSw;

	@FxColumn(name = "SNMP_INDEX", size = 50, nullable = true, comment = "SNMP 인덱스")
	private String snmpIndex;

	public FN_MO_CARD() {
	}

	/**
	 * 카드설명
	 * 
	 * @return 카드설명
	 */
	public String getCardDescr() {
		return cardDescr;
	}

	/**
	 * 카드ID
	 * 
	 * @return 카드ID
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * 카드모델설명
	 * 
	 * @return 카드모델설명
	 */
	public String getCardModelName() {
		return cardModelName;
	}

	/**
	 * 모델카드
	 * 
	 * @return 모델카드
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * 포트수
	 * 
	 * @return 포트수
	 */
	public int getPortCount() {
		return portCount;
	}

	/**
	 * 시리얼번호
	 * 
	 * @return 시리얼번호
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * 슬롯번호
	 * 
	 * @return 슬롯번호
	 */
	public int getSlotNo() {
		return slotNo;
	}

	/**
	 * SNMP 인덱스
	 * 
	 * @return SNMP 인덱스
	 */
	public String getSnmpIndex() {
		return snmpIndex;
	}

	public int getUpperSlotNo() {
		return upperSlotNo;
	}

	/**
	 * FW버전
	 * 
	 * @return FW버전
	 */
	public String getVerFw() {
		return verFw;
	}

	/**
	 * SW버전
	 * 
	 * @return SW버전
	 */
	public String getVerSw() {
		return verSw;
	}

	/**
	 * 카드설명
	 * 
	 * @param cardDescr
	 *            카드설명
	 */
	public void setCardDescr(String cardDescr) {
		this.cardDescr = cardDescr;
	}

	/**
	 * 카드ID
	 * 
	 * @param cardId
	 *            카드ID
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * 카드모델설명
	 * 
	 * @param cardModelName
	 *            카드모델설명
	 */
	public void setCardModelName(String cardModelName) {
		this.cardModelName = cardModelName;
	}

	/**
	 * 모델카드
	 * 
	 * @param cardType
	 *            모델카드
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * 포트수
	 * 
	 * @param portCount
	 *            포트수
	 */
	public void setPortCount(int portCount) {
		this.portCount = portCount;
	}

	/**
	 * 시리얼번호
	 * 
	 * @param serialNo
	 *            시리얼번호
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * 슬롯번호
	 * 
	 * @param slotNo
	 *            슬롯번호
	 */
	public void setSlotNo(int slotNo) {
		this.slotNo = slotNo;
	}

	/**
	 * SNMP 인덱스
	 * 
	 * @param snmpIndex
	 *            SNMP 인덱스
	 */
	public void setSnmpIndex(String snmpIndex) {
		this.snmpIndex = snmpIndex;
	}

	public void setUpperSlotNo(int upperSlotNo) {
		this.upperSlotNo = upperSlotNo;
	}

	/**
	 * FW버전
	 * 
	 * @param verFw
	 *            FW버전
	 */
	public void setVerFw(String verFw) {
		this.verFw = verFw;
	}

	/**
	 * SW버전
	 * 
	 * @param verSw
	 *            SW버전
	 */
	public void setVerSw(String verSw) {
		this.verSw = verSw;
	}
}

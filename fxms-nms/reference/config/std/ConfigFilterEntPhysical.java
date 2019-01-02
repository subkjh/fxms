package com.daims.dfc.filter.config.std;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.RC;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.MoCard;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.common.mo.MoSensor;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpNotFoundOidException;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;
import com.daims.dfc.mib.ENTITY_MIB;

/**
 * 모듈 구성정보를 수집합니다.<br>
 * 단 시리얼번호가 있는 모듈만을 수집합니다.<br>
 * IS_ONLY_SERIALNO : 시리얼번호가 있는 모듈만 가져오기<br>
 * ENT_PHYSICAL_CLASS : IS_ONLY_SERIALNO=false로하고 원하는 분류를 지정하여 가져오기<br>
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterEntPhysical extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1751786874823963249L;

	/**
	 * IS_ONLY_SERIALNO는 시리얼번호가 있는 모듈만 취하고, 이 파라메터가 false이면 ENT_PHYSICAL_CLASS를
	 * 참조함.
	 */
	private final String IS_ONLY_SERIALNO = "IS_ONLY_SERIALNO";

	// /** para.ENT_PHYSICAL_CLASS는 가져올 모듈을 나타냄. 여려개인 경우 콤마로 구분함 */
	// private final String ENT_PHYSICAL_CLASS = "ENT_PHYSICAL_CLASS";

	private final ENTITY_MIB OID = new ENTITY_MIB();

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		// index
		List<MoCard> cardList;
		try {
			if (getProperty(IS_ONLY_SERIALNO, false)) {
				cardList = getCardIndexList(node);
			} else {
				cardList = getCardIndexListWithPhysicalClass(node);
			}
		} catch (SnmpTimeoutException e) {
			ServiceImpl.logger.fail(e.getMessage());
			return Ret.OK;
		} catch (SnmpNotFoundOidException e) {
			ServiceImpl.logger.fail(e.getMessage());
			return Ret.OK;
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			return Ret.OK;
		}

		OidValue ovArr[];

		if (cardList != null && cardList.size() > 0) {

			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoCard.MO_CLASS);

			for (MoCard mo : cardList) {

				try {
					ovArr = snmpget(node//
							, OID.entPhysicalDescr + "." + mo.getSnmpIndex()//
							, OID.entPhysicalName + "." + mo.getSnmpIndex()//
							, OID.entPhysicalModelName + "." + mo.getSnmpIndex()//
							, OID.entPhysicalHardwareRev + "." + mo.getSnmpIndex()//
							, OID.entPhysicalFirmwareRev + "." + mo.getSnmpIndex()//
							, OID.entPhysicalSoftwareRev + "." + mo.getSnmpIndex()//
							, OID.entPhysicalContainedIn + "." + mo.getSnmpIndex() //
							, OID.entPhysicalClass + "." + mo.getSnmpIndex() //
							, OID.entPhysicalSerialNum + "." + mo.getSnmpIndex() //
							, OID.entPhysicalParentRelPos + "." + mo.getSnmpIndex() //
					);
				} catch (SnmpTimeoutException e) {
					return new Ret(RC.c007_timeout, e.getMessage());
				} catch (SnmpNotFoundOidException e) {
					mo.setBeanStatus(NotiBean.BEAN_STATUS_NOTHING);
					continue;
				} catch (Exception e) {
					mo.setBeanStatus(NotiBean.BEAN_STATUS_NOTHING);
					continue;
				}

				mo.setCardDescr(ovArr[0].getValue());
				mo.setMoName(ovArr[1].getValue());
				mo.setCardModelName(ovArr[2].getValue());
				mo.setVerFw(ovArr[3].getValue());
				mo.setVerFw(ovArr[4].getValue());
				mo.setVerSw(ovArr[5].getValue());
				mo.setSlotNoUpper(ovArr[6].getInt());
				mo.setCardType(OID.getPhysicalClass(ovArr[7].getInt()));
				// mo.setCardType(valueList.get(7).getInt() + "");
				mo.setSerialNo(ovArr[8].getValue());
				mo.setMoType(mo.getCardType());
				mo.setSlotNo(Integer.parseInt(mo.getSnmpIndex()));
				mo.setCardId(mo.getSnmpIndex());
				
				if ( mo.getCardModelName() == null || mo.getCardModelName().length() == 0) {
					mo.setCardModelName(mo.getCardDescr());
				}

				// 2013.08.19 by subkjh : added
				if (mo.getMoName() == null || mo.getMoName().trim().length() == 0) {
					mo.setMoName("Unknown");
				}

			}

			CardMgr mgr = new CardMgr(configMo, cardList);
			mgr.make();

			List<MoSensor> sensorList = makeSensor(cardList);
			if (sensorList != null && sensorList.size() > 0) configMo.addMoListDetected(sensorList);

			configMo.addMoListDetected(cardList);

			return new Ret(cardList.size());
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoCard.MO_CLASS, MoSensor.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return OID.entPhysicalDescr;
	}

	/**
	 * 시리얼번호가 있는것을 대상으로 함
	 * 
	 * @param node
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws Exception
	 */
	private List<MoCard> getCardIndexList(SnmpMo node) throws SnmpTimeoutException, SnmpNotFoundOidException, Exception {

		List<OidValue> varList = snmpwalk(node, OID.entPhysicalSerialNum);

		List<MoCard> moList = new ArrayList<MoCard>();
		MoCard mo;
		String serialNo;

		for (OidValue var : varList) {

			serialNo = var.getValue();
			if (serialNo != null && serialNo.trim().length() > 0) {
				mo = new MoCard();
				mo.setSnmpIndex(var.getInstance(1));
				mo.setSerialNo(serialNo);
				moList.add(mo);
			}
		}

		return moList;

	}

	/**
	 * 원하는 PhysicalClass만 수집하기 위해 필요한 index를 가져옴.
	 * 
	 * @param node
	 * @return
	 * @throws SnmpTimeoutException
	 * @throws SnmpNotFoundOidException
	 * @throws Exception
	 * @since 2013.08.20 by subkjh
	 */
	private List<MoCard> getCardIndexListWithPhysicalClass(SnmpMo node) throws SnmpTimeoutException, SnmpNotFoundOidException,
			Exception {

		List<OidValue> varList = snmpwalk(node, OID.entPhysicalClass);

		List<MoCard> moList = new ArrayList<MoCard>();
		MoCard mo;
		String value;

		for (OidValue var : varList) {
			value = var.getValue();
			if (value != null && value.trim().length() > 0) {
				mo = new MoCard();
				mo.setSnmpIndex(var.getInstance(1));
				moList.add(mo);
			}
		}

		return moList;

	}

	private List<MoSensor> makeSensor(List<MoCard> moList) {

		List<MoSensor> list = new ArrayList<MoSensor>();
		MoSensor sensor;
		for (MoCard mo : moList) {
			if ("sensor".equalsIgnoreCase(mo.getCardType())) {
				sensor = new MoSensor();
				sensor.setManaged(true);
				sensor.setMoAlias(mo.getCardDescr() == null ? mo.getCardModelName() : mo.getCardDescr());
				sensor.setMoName(sensor.getMoAlias());
				sensor.setSnmpIndex(mo.getSnmpIndex());
				sensor.setOidValue("");
				sensor.setMoType(sensor.getMoName().toLowerCase().indexOf("temperature") >= 0 ? "TEMP" : "SENSOR");
				sensor.setSensorKind("numeric");
				list.add(sensor);
			}
		}

		return list;

	}
}

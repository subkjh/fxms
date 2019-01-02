package com.fxms.nms.fxactor.conf.snmp;

import java.util.ArrayList;
import java.util.List;

import com.fxms.nms.fxactor.conf.snmp.std.CardMgr;
import com.fxms.nms.mo.NeCardMo;
import com.fxms.nms.mo.NeMo;
import com.fxms.nms.snmp.beans.OidValue;
import com.fxms.nms.snmp.exception.SnmpNotFoundOidException;
import com.fxms.nms.snmp.exception.SnmpTimeoutException;
import com.fxms.nms.snmp.mib.ENTITY_MIB;

import fxms.bas.exception.FxTimeoutException;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.noti.FxEvent;
import subkjh.bas.log.Logger;

/**
 * 모듈 구성정보를 수집합니다.<br>
 * 단 시리얼번호가 있는 모듈만을 수집합니다.<br>
 * IS_ONLY_SERIALNO : 시리얼번호가 있는 모듈만 가져오기<br>
 * ENT_PHYSICAL_CLASS : IS_ONLY_SERIALNO=false로하고 원하는 분류를 지정하여 가져오기<br>
 * 
 * @author subkjh
 * 
 */
public class ConfEntPhysicalActor extends ConfSnmpFxActor {

	// /** para.ENT_PHYSICAL_CLASS는 가져올 모듈을 나타냄. 여려개인 경우 콤마로 구분함 */
	// private final String ENT_PHYSICAL_CLASS = "ENT_PHYSICAL_CLASS";

	private final ENTITY_MIB OID = new ENTITY_MIB();

	@Override
	public void getConfigChildren(MoConfig configMo, String... moClasses) throws FxTimeoutException, Exception {

		NeMo node = (NeMo) configMo.getParent();

		// index
		List<NeCardMo> cardList;
		try {
			String noSerialIgnore = getParaStr("no-serial-ignore");

			if ("true".equalsIgnoreCase(noSerialIgnore)) {
				cardList = getCardIndexListWithPhysicalClass(node);
			} else {
				cardList = getCardIndexList(node);
			}
		} catch (SnmpTimeoutException e) {
			Logger.logger.fail(e.getMessage());
			return;
		} catch (SnmpNotFoundOidException e) {
			Logger.logger.fail(e.getMessage());
			return;
		} catch (Exception e) {
			Logger.logger.fail(e.getMessage());
			return;
		}

		OidValue ovArr[];

		if (cardList != null && cardList.size() > 0) {

			configMo.setStatusAllChildren(FxEvent.STATUS.raw, FxEvent.STATUS.deleted, NeCardMo.MO_CLASS);

			for (NeCardMo mo : cardList) {

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
					throw e;
				} catch (SnmpNotFoundOidException e) {
					mo.setStatus(FxEvent.STATUS.raw);
					continue;
				} catch (Exception e) {
					mo.setStatus(FxEvent.STATUS.raw);
					continue;
				}

				mo.setCardDescr(ovArr[0].getValue());
				mo.setMoName(ovArr[1].getValue());
				mo.setCardModelName(ovArr[2].getValue());
				mo.setVerFw(ovArr[3].getValue());
				mo.setVerFw(ovArr[4].getValue());
				mo.setVerSw(ovArr[5].getValue());
				mo.setCardType(OID.getPhysicalClass(ovArr[7].getInt()));
				// mo.setCardType(valueList.get(7).getInt() + "");
				mo.setSerialNo(ovArr[8].getValue());
				mo.setMoType(mo.getCardType());
				mo.setSlotNo(Integer.parseInt(mo.getSnmpIndex()));
				mo.setUpperSlotNo(ovArr[6].getInt());
				mo.setCardId(mo.getSnmpIndex());

				if (mo.getCardModelName() == null || mo.getCardModelName().length() == 0) {
					mo.setCardModelName(mo.getCardDescr());
				}

				// 2013.08.19 by subkjh : added
				if (mo.getMoName() == null || mo.getMoName().trim().length() == 0) {
					mo.setMoName("Unknown");
				}

			}

			new CardMgr().make(configMo, cardList);
			
			// List<MoSensor> sensorList = makeSensor(cardList);
			// if (sensorList != null && sensorList.size() > 0)
			// configMo.addMoListDetected(sensorList);

			configMo.addMoListDetected(cardList);

		}
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
	private List<NeCardMo> getCardIndexList(NeMo node) throws SnmpTimeoutException, SnmpNotFoundOidException, Exception {

		List<OidValue> varList = snmpwalk(node, OID.entPhysicalSerialNum);

		List<NeCardMo> moList = new ArrayList<NeCardMo>();
		NeCardMo mo;
		String serialNo;

		for (OidValue var : varList) {

			serialNo = var.getValue();
			if (serialNo != null && serialNo.trim().length() > 0) {
				mo = new NeCardMo();
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
	private List<NeCardMo> getCardIndexListWithPhysicalClass(NeMo node)
			throws SnmpTimeoutException, SnmpNotFoundOidException, Exception {

		List<OidValue> varList = snmpwalk(node, OID.entPhysicalClass);

		List<NeCardMo> moList = new ArrayList<NeCardMo>();
		NeCardMo mo;
		String value;

		for (OidValue var : varList) {
			value = var.getValue();
			if (value != null && value.trim().length() > 0) {
				mo = new NeCardMo();
				mo.setSnmpIndex(var.getInstance(1));
				moList.add(mo);
			}
		}

		return moList;

	}

	// private List<MoSensor> makeSensor(List<CardMo> moList) {
	//
	// List<MoSensor> list = new ArrayList<MoSensor>();
	// MoSensor sensor;
	// for (CardMo mo : moList) {
	// if ("sensor".equalsIgnoreCase(mo.getCardType())) {
	// sensor = new MoSensor();
	// sensor.setManaged(true);
	// sensor.setMoAlias(mo.getCardDescr() == null ? mo.getCardModelName() :
	// mo.getCardDescr());
	// sensor.setMoName(sensor.getMoAlias());
	// sensor.setSnmpIndex(mo.getSnmpIndex());
	// sensor.setOidValue("");
	// sensor.setMoType(sensor.getMoName().toLowerCase().indexOf("temperature") >= 0
	// ? "TEMP" : "SENSOR");
	// sensor.setSensorKind("numeric");
	// list.add(sensor);
	// }
	// }
	//
	// return list;
	//
	// }
}

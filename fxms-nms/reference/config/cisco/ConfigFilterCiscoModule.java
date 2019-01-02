package com.daims.dfc.filter.config.cisco;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mib.MibCiscoStack;
import com.daims.dfc.common.mo.Mo;
import com.daims.dfc.common.mo.MoCard;
import com.daims.dfc.common.mo.MoInterface;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * 시스코 장비의 모듈과 인터페이스가 실장된 카드 번호를 가져오는 필터<br>
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterCiscoModule extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5015554730592783993L;

	private MibCiscoStack OID = new MibCiscoStack();

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {

			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoCard.MO_CLASS);
			configMo.addMoListDetected(cardModule(node));

			getInterfaceCiscoModuleIndex(configMo);

		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

		// 위 처리중 오류가 발생하더라고 다른 작업은 계속 수행을 위해 OK를 리턴합니다.

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoCard.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return OID.portIfIndex;
	}

	private List<MoCard> cardModule(SnmpMo node) {

		MoCard moCard;
		List<OidValue> varList;
		List<MoCard> moList = new ArrayList<MoCard>();

		try {
			varList = snmpwalk(node, OID.moduleIndex);

			for (OidValue var : varList) {
				moCard = cardModuleGet(var.getInstance(1), node);
				if (moCard != null) moList.add(moCard);
			}

			return moList;
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
			return null;
		}

	}

	/**
	 * 모듈 내역을 가져옵니다.
	 * 
	 * @param modIdx
	 * @param target
	 * @return
	 * @throws Exception
	 */
	private MoCard cardModuleGet(String modIdx, SnmpMo node) throws Exception {
		MoCard card = new MoCard();
		String dotIdx = "." + modIdx;

		card.setMoType("Module");
		card.setMoName("Module");
		card.setCardId(modIdx);
		card.setSnmpIndex(modIdx);
		card.setCardType("MODULE");

		OidValue ovArr[] = snmpget(node //

				, OID.moduleModel + dotIdx //
				, OID.moduleName + dotIdx//
				, OID.moduleSerialNumberString + dotIdx//
				, OID.moduleFwVersion + dotIdx//
				, OID.moduleSwVersion + dotIdx//
				, OID.moduleNumPorts + dotIdx);

		card.setCardModelName(ovArr[0].getValue());
		card.setMoName(ovArr[1].getValue());
		card.setSerialNo(ovArr[2].getValue());
		card.setVerFw(ovArr[3].getValue());
		card.setVerSw(ovArr[4].getValue());
		card.setCountPort(ovArr[5].getInt(0));

		if (card.getMoName().length() == 0) card.setMoName("MODULE" + card.getSlotNo());

		card.setMoAlias(card.getMoName());

		return card;
	}

	/**
	 * 인터페이스에 대한 카드 정보를 설정합니다.
	 * 
	 * @param snmpTarget
	 * @param node
	 */
	private void getInterfaceCiscoModuleIndex(ConfigMo configMo) {
		try {
			List<OidValue> varList = snmpwalk((SnmpMo) configMo.getNode(), OID.portIfIndex);
			Mo mo;
			String instance;
			String portModuleIndex;
			for (OidValue var : varList) {
				mo = configMo.getMo4SnmpIndex(MoInterface.MO_CLASS, var.getValue());
				if (mo instanceof MoInterface) {
					instance = var.getInstance(2);
					portModuleIndex = instance.split("\\.")[0];
					((MoInterface) mo).setCardId(portModuleIndex);
				}
			}
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

	}

}

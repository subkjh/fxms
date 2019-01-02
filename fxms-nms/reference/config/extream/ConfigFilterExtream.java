package com.daims.dfc.filter.config.extream;

import java.util.ArrayList;
import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.beans.node.SnmpMo;
import com.daims.dfc.common.mo.MoCard;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;

/**
 * Extream 장비의 모듈 정보를 가져오는 필터
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterExtream extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7747614783482592428L;

	public String EXTREAM_MODULE_INDEX = ".1.3.6.1.4.1.1916.1.1.2.2.1.1";
	public String EXTREAM_MODULE_MODEL = ".1.3.6.1.4.1.1916.1.1.2.2.1.3";
	public String EXTREAM_MODULE_NAME = ".1.3.6.1.4.1.1916.1.1.2.2.1.2";
	public String EXTREAM_MODULE_SERIAL_NO = ".1.3.6.1.4.1.1916.1.1.2.2.1.6";

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException, NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {
			List<MoCard> moList = cardModule(node);
			configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoCard.MO_CLASS);
			configMo.addMoListDetected(moList);

		} catch (Exception e) {
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
		return EXTREAM_MODULE_INDEX;
	}

	private List<MoCard> cardModule(SnmpMo node) {
		List<OidValue> valueList;
		OidValue ovArr[];
		MoCard card;
		List<MoCard> moList = new ArrayList<MoCard>();
		String dotIdx;
		String snmpIndex;

		try {
			valueList = snmpwalk(node, EXTREAM_MODULE_INDEX);
			for (OidValue value : valueList) {
				snmpIndex = value.getInstance(EXTREAM_MODULE_INDEX);
				card = new MoCard();
				dotIdx = "." + snmpIndex;

				card.setCardId(snmpIndex);
				card.setSnmpIndex(snmpIndex);
				card.setSerialNo(snmpIndex);
				card.setCardType("MODULE");

				ovArr = snmpget(node //
						, EXTREAM_MODULE_MODEL + dotIdx //
						, EXTREAM_MODULE_NAME + dotIdx //
						, EXTREAM_MODULE_SERIAL_NO + dotIdx);

				card.setCardModelName(ovArr[0].getValue());
				card.setMoName(ovArr[1].getValue());
				card.setSerialNo(ovArr[2].getValue());

				if (card.getMoName().length() == 0) card.setMoName("MODULE" + card.getSlotNo());

				moList.add(card);
			}

			return moList;
		} catch (Exception e) {
			ServiceImpl.logger.fail(e.getMessage());
		}

		return null;
	}
}

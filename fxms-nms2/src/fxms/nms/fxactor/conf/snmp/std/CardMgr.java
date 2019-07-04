package fxms.nms.fxactor.conf.snmp.std;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;
import fxms.bas.mo.child.MoConfig;
import fxms.nms.mo.NeCardMo;
import fxms.nms.mo.NeIfMo;

/**
 * 장비 실장 카드 정보를 정리합니다.
 * 
 * @author subkjh
 * 
 */
public class CardMgr {

	private List<NeCardMo> cardList;

	/**
	 * 
	 * @param configMo
	 *            구성 노드
	 * @param cardList
	 *            장비로부터 가져온 실장 정보
	 */
	public CardMgr() {

	}

	/**
	 * 재 정의함.
	 */
	public void make(MoConfig configMo, List<NeCardMo> cardList) {

		this.cardList = cardList;

		NeCardMo chassis = getChassis();
		if (chassis == null)
			return;

		chassis.setChassisId("Chassis");

		StringBuffer sb = null;
		if (Logger.logger.isTrace()) {
			sb = new StringBuffer();
			sb.append(chassis.getCardModelName() + ", " + chassis.getCardDescr());
		}

		List<NeCardMo> children = new ArrayList<NeCardMo>();

		setSlotNumber(chassis);

		findChildModule(chassis, children);

		List<NeCardMo> portList = new ArrayList<NeCardMo>();

		for (NeCardMo card : children) {

			// System.out.println(card.getCardId() + ", " + card.getCardDescr());

			portList.clear();

			findChildPort(card, portList);

			for (NeCardMo port : portList) {
				port.setCardId(card.getCardId());
			}

			card.setPortCount(portList.size());
			if (Logger.logger.isTrace()) {
				sb.append("\n  " + card.getChassisId() + " : " + card.getCardModelName());
				if (card.getPortCount() > 0) {
					for (NeCardMo port : portList) {
						sb.append("\n    " + port.getChassisId() + " : " + port.getCardDescr());
					}
				}
			}
		}

		if (Logger.logger.isTrace()) {
			Logger.logger.trace(sb.toString());
		}

		// 포트의 실장ID를 지정합니다.
		for (NeCardMo card : cardList) {
			if (card.getCardType().equalsIgnoreCase("port")) {
				setChassisId4Port(configMo, card);
			}
		}

		// 컨테이너는 실장도에서 사용하지 않습니다.
		for (NeCardMo card : cardList) {
			if (card.getCardType().equalsIgnoreCase("container")) {
				card.setChassisId(null);
			}
		}

	}

	private void setSlotNumber(NeCardMo parent) {

		int index = 0;

		for (NeCardMo card : cardList) {
			if (card.getUpperSlotNo() == parent.getSlotNo()) {
				if (card.getCardType().equals("container")) {
					card.setChassisId("Slot" + index++);
				}
			}
		}
	}

	private void findChildModule(NeCardMo parent, List<NeCardMo> children) {

		for (NeCardMo card : cardList) {
			if (card.getUpperSlotNo() == parent.getSlotNo()) {
				if (card.getCardType().equals("container")) {
					findChildModule(card, children);
				} else {
					card.setChassisId(parent.getChassisId());
					children.add(card);
				}
			}
		}
	}

	private void findChildPort(NeCardMo parent, List<NeCardMo> children) {

		for (NeCardMo card : cardList) {
			if (card.getUpperSlotNo() == parent.getSlotNo()) {
				if (card.getCardType().equals("port")) {
					children.add(card);
					makeChassisId4Port(card);
				} else {
					findChildPort(card, children);
				}
			}
		}
	}

	private NeCardMo getChassis() {
		for (NeCardMo card : cardList) {
			if (card.getCardType().equals("chassis"))
				return card;
		}
		return null;
	}

	private void makeChassisId4Port(NeCardMo card) {

		card.setChassisId(card.getSnmpIndex());
		try {
			String ss[] = card.getCardDescr().split("/");
			if (ss.length == 3) {
				card.setChassisId("Port" + ss[2]);
			}
		} catch (Exception e) {
		}
	}

	private void setChassisId4Port(MoConfig configMo, NeCardMo portInCard) {
		List<NeIfMo> portList = configMo.getChildren(NeIfMo.class);
		if (portList == null)
			return;

		// for (IfMo mo : portList) {
		// if (mo.getIfIndex() == ifIndex) {
		// mo.setCardId(slotNoUpper + "");
		// return;
		// }
		// }

		for (NeIfMo mo : portList) {

			if (mo.getIfDescr().equals(portInCard.getCardDescr())) {

				System.out.println(mo.getIfDescr() + " | " + portInCard.getCardDescr() + "=" + portInCard.getCardId()
						+ " . " + portInCard.getChassisId());

				mo.setCardId(portInCard.getCardId() + "");
				mo.setChassisId(portInCard.getChassisId());
				return;
			}
		}
	}
}

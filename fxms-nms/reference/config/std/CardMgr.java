package com.daims.dfc.filter.config.std;

import java.util.ArrayList;
import java.util.List;

import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mo.MoCard;
import com.daims.dfc.common.mo.MoInterface;
import com.daims.dfc.filter.config.ConfigMo;

/**
 * 장비 실장 카드 정보를 정리합니다.
 * 
 * @author subkjh
 * 
 */
public class CardMgr {

	private List<MoCard> cardList;
	private ConfigMo configMo;

	/**
	 * 
	 * @param configMo
	 *            구성 노드
	 * @param cardList
	 *            장비로부터 가져온 실장 정보
	 */
	public CardMgr(ConfigMo configMo, List<MoCard> cardList) {
		this.configMo = configMo;
		this.cardList = cardList;
	}

	/**
	 * 재 정의함.
	 */
	public void make() {

		MoCard chassis = getChassis();
		if (chassis == null) return;

		chassis.setChassisId("Chassis");

		StringBuffer sb = null;
		if (ServiceImpl.logger.isTrace()) {
			sb = new StringBuffer();
			sb.append(chassis.getCardModelName() + ", " + chassis.getCardDescr());
		}

		List<MoCard> children = new ArrayList<MoCard>();
		
		setSlotNumber(chassis);
		
		findChildModule(chassis, children);

		List<MoCard> portList = new ArrayList<MoCard>();
		
		for (MoCard card : children) {
			
			//System.out.println(card.getCardId() + ", " + card.getCardDescr());

			portList.clear();

			findChildPort(card, portList);
			
			for (MoCard port : portList) {
				port.setCardId(card.getCardId());
			}

			card.setCountPort(portList.size());
			if (ServiceImpl.logger.isTrace()) {
				sb.append("\n  " + card.getChassisId() + " : " + card.getCardModelName());
				if (card.getCountPort() > 0) {
					for (MoCard port : portList) {
						sb.append("\n    " + port.getChassisId() + " : " + port.getCardDescr());
					}
				}
			}
		}

		if (ServiceImpl.logger.isTrace()) {
			ServiceImpl.logger.trace(sb.toString());
		}

		// 포트의 실장ID를 지정합니다.
		for (MoCard card : cardList) {
			if (card.getCardType().equalsIgnoreCase("port")) {
				setChassisId4Port(configMo, card);
			}
		}

		// 컨테이너는 실장도에서 사용하지 않습니다.
		for (MoCard card : cardList) {
			if (card.getCardType().equalsIgnoreCase("container")) {
				card.setChassisId(null);
			}
		}

	}

	private void setSlotNumber(MoCard parent) {

		int index = 0;

		for (MoCard card : cardList) {
			if (card.getSlotNoUpper() == parent.getSlotNo()) {
				if (card.getCardType().equals("container")) {
					card.setChassisId("Slot" + index++);
				}
			}
		}
	}

	private void findChildModule(MoCard parent, List<MoCard> children) {

		for (MoCard card : cardList) {
			if (card.getSlotNoUpper() == parent.getSlotNo()) {
				if (card.getCardType().equals("container")) {
					findChildModule(card, children);
				} else {
					card.setChassisId(parent.getChassisId());
					children.add(card);
				}
			}
		}
	}

	private void findChildPort(MoCard parent, List<MoCard> children) {

		for (MoCard card : cardList) {
			if (card.getSlotNoUpper() == parent.getSlotNo()) {
				if (card.getCardType().equals("port")) {
					children.add(card);
					makeChassisId4Port(card);
				} else {
					findChildPort(card, children);
				}
			}
		}
	}

	private MoCard getChassis() {
		for (MoCard card : cardList) {
			if (card.getCardType().equals("chassis")) return card;
		}
		return null;
	}

	private void makeChassisId4Port(MoCard card) {
		
		card.setChassisId(card.getSnmpIndex());
		try {
			String ss[] = card.getCardDescr().split("/");
			if (ss.length == 3) {
				card.setChassisId("Port" + ss[2]);
			}
		} catch (Exception e) {
		}
	}

	private void setChassisId4Port(ConfigMo configMo, MoCard card) {
		List<MoInterface> portList = configMo.getMoList(MoInterface.class);
		if (portList == null) return;

		// for (MoInterface mo : portList) {
		// if (mo.getIfIndex() == ifIndex) {
		// mo.setCardId(slotNoUpper + "");
		// return;
		// }
		// }

		for (MoInterface mo : portList) {
			
			if (mo.getIfDescr().equals(card.getCardDescr())) {
				
				System.out.println(mo.getIfDescr() + " | " +card.getCardDescr() + "=" + card.getCardId() + " . " + card.getChassisId());
				
				mo.setCardId(card.getCardId() + "");
				mo.setChassisId(card.getChassisId());
				return;
			}
		}
	}
}

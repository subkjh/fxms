package com.daims.dfc.filter.config.std;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import subkjh.dao.control.DaoFactory;
import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.CLogger;
import subkjh.log.CLogger.LEVEL;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.utils.FileUtil;

import com.daims.dfc.api.MoApi;
import com.daims.dfc.common.mo.MoCard;
import com.daims.dfc.common.mo.MoInterface;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilter;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.mib.IF_MIB;
import com.daims.dfc.service.conf.ConfApi;

public class ConfigFilter4File extends ConfigFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6821736160634389650L;

	public static void main ( String [ ] args ) throws Exception {
		CLogger.logger.setLevel(LEVEL.trace);
		try {
			DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 10.100.201.1 - 1002642 10.100.201.2 - 1004079 10.100.201.13 - 1005963
		 * 10.100.201.3 - 1004847 10.100.201.4 - 1005018 10.100.201.9 - 1005188
		 * 10.100.201.10 - 1005275 10.100.201.5 - 1005362 10.100.201.6 - 1005524
		 * 10.100.201.7 - 1005674 10.100.201.8 - 1005819 10.100.201.13 - 1005963
		 * 10.100.201.14 - 1006046
		 */

		String ips[] = new String [ ] { "10.100.201.1", "10.100.201.2", "10.100.201.3", "10.100.201.4", "10.100.201.5",
				"10.100.201.6", "10.100.201.7", "10.100.201.8", "10.100.201.9", "10.100.201.10", "10.100.201.13", "10.100.201.14",
				"10.100.201.113" };

		for (String ip : ips) {
			MoNode node = MoApi.getApi().getMoNodeByIp(ip, false);
			if (node == null) continue;

			ConfigMo configMo = ConfApi.getApi().getConfigMo(node.getMoNo(), null);
			ConfigFilter4File c = new ConfigFilter4File();
			try {
				c.filter(configMo, null, null);
				MoApi.getApi().updateNode(configMo, true, null, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Ret filter ( ConfigMo configMo, String moClassArr[], String moName ) throws TimeoutException, NotFoundException,
			Exception {

		configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoCard.MO_CLASS);
		configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoInterface.MO_CLASS);

		List<String> lineList = FileUtil.getLines(new File("datas/" + configMo.getNode().getIpAddress() + ".txt"));

		List<MoInterface> portList = parseInterface(lineList);
		configMo.addMoListDetected(portList);

		List<MoCard> cardList = parseCard(configMo, lineList);
		configMo.addMoListDetected(cardList);

		// for (MoInterface port : portList) {
		// System.out.println(port.getMoClass() + ", " + port.getMoName() + "="
		// + port.getChassisId());
		// }

		return Ret.OK;
	}

	private List<MoCard> parseCard ( ConfigMo configMo, List<String> lineList ) {
		List<MoCard> cardList = new ArrayList<MoCard>();
		String ss[];
		MoCard card;
		int count = 0;
		for (String line : lineList) {
			if (line.startsWith("***")) {
				count++;
				if (count > 1) break;
				continue;
			}
			if (line.startsWith("[")) continue;

			ss = line.split("\\|");
			card = new MoCard();
			card.setCardId(ss[0]);
			card.setCardDescr(ss[1]);
			card.setCardModelName(ss[2]);
			if (card.getCardModelName() == null || card.getCardModelName().length() == 0) {
				card.setCardModelName(card.getCardDescr());
			}
			card.setCardType(ss[3]);
			card.setCountPort(0);
			card.setSlotNo(Integer.parseInt(card.getCardId()));
			card.setSerialNo(ss[5]);
			card.setVerFw(ss[6]);
			card.setVerSw(ss[7]);
			card.setSnmpIndex(ss[9]);
			card.setSlotNoUpper(Integer.parseInt(ss[10]));
			cardList.add(card);

			card.setMoName(card.getCardId());
			card.setMoName(card.getSlotNoUpper() + "");

			// System.out.println(card);
		}

		CardMgr mgr = new CardMgr(configMo, cardList);
		mgr.make();

		return cardList;
	}

	private List<MoInterface> parseInterface ( List<String> lineList ) {
		List<MoInterface> portList = new ArrayList<MoInterface>();
		String ss[];
		MoInterface port;
		boolean start = false;
		for (String line : lineList) {
			if (line.indexOf("인터페이스정보 start") < 0 && start == false) {
				// count++;
				// if (count > 1) break;
				continue;
			} else {

				if (line.startsWith("****")) {
					if (start == true) break;
					else start = true;
					continue;
				}
				if (line.startsWith("[")) continue;

			}
			// if (line.startsWith("[")) continue;

			ss = line.split("\\|");
			// System.out.println("count : " + count);
			// [IF_INDEX|IF_NAME|IF_ALIAS|IF_DESCR|IF_SPEED|IF_TYPE|IF_STATUS_ADMIN|IF_STATUS_OPER|IF_NETMASK|BW_CODE|BW_CODE_QOS|IP_ADDRESS|IS_BIT64|MAC_ADDRESS|CARD_ID]
			// 1|GigabitEthernet0/0/1|GigabitEthernet0/0/1
			// Interface|GigabitEthernet0/0/1|1000000000|6|2null|1G|1G|null|false|000f.e207.f2e0|352
			// System.out.println(line);
			port = new MoInterface();

			port.setIfIndex(Integer.parseInt(ss[0]));
			port.setIfName(ss[1]);
			port.setIfAlias(ss[2]);
			port.setIfDescr(ss[3]);
			port.setIfSpeed(Long.parseLong(ss[4]));
			port.setIfType(Integer.parseInt(ss[5]));
			// port.setIfStatusAdmin(Integer.parseInt(ss[6]));
			port.setIfStatusAdmin(IF_MIB.ifAdminStatus_up);
			if (ss[6].length() > 1) {
				port.setIfStatusOper(Integer.parseInt("" + ss[6].charAt(0)));
				port.setIfNetmask(ss[6].substring(1));
				port.setBwCode(ss[7]);
				port.setBwCodeQos(ss[8]);
				port.setIpAddress(ss[9]);
				port.setBit64("true".equals(ss[10]));
				port.setMacAddress(ss[11]);
				port.setCardId(ss[12]);
			} else {
				port.setIfStatusOper(Integer.parseInt(ss[6]));
				port.setIfNetmask(ss[7]);
				port.setBwCode(ss[8]);
				port.setBwCodeQos(ss[9]);
				port.setIpAddress(ss[10]);
				port.setBit64("true".equals(ss[11]));
				port.setMacAddress(ss[12]);
				port.setCardId(ss[13]);
			}
			port.setMoName(port.getIfName());
			portList.add(port);
			// System.out.println("portlist count : " + portList.size());
		}

		return portList;
	}

	@Override
	public String [ ] getMoClassContains ( ) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<MoCard> parseCardTnms ( ConfigMo configMo, List<String> lineList ) {
		List<MoCard> cardList = new ArrayList<MoCard>();
		String ss[];
		MoCard card;
		int count = 0;
		for (String line : lineList) {
			ss = line.split("\t");
			if (ss.equals(configMo.getNode().getMoName()) == false) continue;

			card = new MoCard();
			card.setCardId(ss[1]);
			card.setCardDescr(ss[2]);
			card.setCardModelName(ss[2]);
			card.setCardType(ss[2]);
			card.setCountPort(0);
			card.setChassisId(ss[1]);
			cardList.add(card);

			card.setMoName(card.getCardId());

			// System.out.println(card);
		}

		return cardList;
	}
}

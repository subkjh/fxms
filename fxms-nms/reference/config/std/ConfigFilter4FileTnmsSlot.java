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
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilter;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.service.conf.ConfApi;

public class ConfigFilter4FileTnmsSlot extends ConfigFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6821736160634389650L;

	public static void main(String[] args) throws Exception {
		CLogger.logger.setLevel(LEVEL.debug);
		try {
			DaoFactory.getInstance().addDataBase(new File("deploy/conf/databases.xml"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 10.100.201.1 - 1002642 10.100.201.2 - 1004079 10.100.201.13 - 1005963
		 * 10.100.201.3 - 1004847 10.100.201.4 - 1005018 10.100.201.9 - 1005188
		 * 10.100.201.10 - 1005275 10.100.201.5 - 1005362 10.100.201.6 - 1005524
		 * 10.100.201.7 - 1005674 10.100.201.8 - 1005819 10.100.201.13 - 1005963
		 * 10.100.201.14 - 1006046
		 */

		String ips[] = new String[] { "20-3-3-6390", "B-102-1-6390", "20-3-1-6390", "20-2-1-6390" };

		for (String ip : ips) {
			MoNode node = MoApi.getApi().getMoNodeByMoName(ip, false);
			if (node == null) continue;

			ConfigMo configMo = ConfApi.getApi().getConfigMo(node.getMoNo(), new String[] { MoCard.MO_CLASS });
			ConfigFilter4FileTnmsSlot c = new ConfigFilter4FileTnmsSlot();
			try {
				c.filter(configMo, null, null);
				MoApi.getApi().updateNode(configMo, true, null, true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE, MoCard.MO_CLASS);

		List<String> lineList = FileUtil.getLines(new File("datas/tnms_slot.txt"));

		List<MoCard> cardList = parseCardTnms(configMo, lineList);
		configMo.addMoListDetected(cardList);

		// for (MoInterface port : portList) {
		// System.out.println(port.getMoClass() + ", " + port.getMoName() + "="
		// + port.getChassisId());
		// }

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<MoCard> parseCardTnms(ConfigMo configMo, List<String> lineList) {
		List<MoCard> cardList = new ArrayList<MoCard>();
		String ss[];
		MoCard card;

		for (String line : lineList) {
			ss = line.split("\t");
			if (ss[0].equals(configMo.getNode().getMoName()) == false) continue;

			card = new MoCard();
			card.setCardId(ss[1]);
			card.setCardDescr(ss[2]);
			card.setCardModelName(ss[2]);
			card.setCardType(ss[2]);
			card.setCountPort(0);
			card.setChassisId(ss[1]);

			card.setMoName(card.getCardId());

			cardList.add(card);

			System.out.println(configMo.getNode().getMoName() + "|" + card.getCardId());
		}

		return cardList;
	}
}

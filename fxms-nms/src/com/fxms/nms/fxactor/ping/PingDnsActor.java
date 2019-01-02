package com.fxms.nms.fxactor.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fxms.nms.NmsCodes;
import com.fxms.nms.mo.DnsMo;

import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class PingDnsActor extends PingFxActor {

	public PingDnsActor() {
	}

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof DnsMo) == false) {
			return null;
		}

		DnsMo node = (DnsMo) mo;

		long queryTime = -1;

		try {

			String command = "dig @" + node.getServer() + " " + node.getDomain();
			Logger.logger.debug(command);

			Process proc = Runtime.getRuntime().exec(command);

			StringBuffer cmdResult = new StringBuffer();
			InputStream eis = proc.getInputStream();
			InputStreamReader eisr = new InputStreamReader(eis);
			BufferedReader ebr = new BufferedReader(eisr);

			String line = null;
			while ((line = ebr.readLine()) != null) {

				if (line == null || eis.available() < 0)
					break;

				if (line != null && line.length() > 0) {
					cmdResult.append(line + "\n");
				}
			}

			Logger.logger.trace(cmdResult.toString());

			// if (cmdResult.toString().contains("ANSWER SECTION")) {
			//
			// }

			Pattern p = Pattern.compile(".+Query\\s+time:\\s+(\\d+)\\s+(.+)$", Pattern.MULTILINE);
			Matcher m = p.matcher(cmdResult.toString());

			while (m.find()) {
				queryTime = Long.parseLong(m.group(1));
				String unit = m.group(2);

				if (!unit.equals("msec")) {
					if (unit.equals("sec"))
						queryTime = queryTime * 1000;
					else {
						Logger.logger.error("쿼리타임 응답 단위가 msec가 아닙니다. 체크 요망 [" + node.toString() + "] queryTime["
								+ queryTime + " " + unit + "]");
					}
				}
			}

			FxServiceImpl.setError(getClass().getSimpleName(), null);
		} catch (IOException e) {
			FxServiceImpl.setError(getClass().getSimpleName(), e.getMessage());
			Logger.logger.error(e);
			queryTime = -1;
		}

		Logger.logger.debug("query time", node.getServer(), node.getDomain(), queryTime);

		int status = queryTime >= 0 ? Mo.STATUS_ON : Mo.STATUS_OFF;

		List<PsVo> valueList = new ArrayList<PsVo>();

		String perfServer = FxCfg.getCfg().getIpAddress();

		PsVo valueCollected = new PsVo(node, null, NmsCodes.PsItem.DnsStatus, status);
		valueCollected.setMoInstance(perfServer);
		valueList.add(valueCollected);

		valueCollected = new PsVo(node, null, NmsCodes.PsItem.DnsQueryTime, -1);
		valueCollected.setMoInstance(perfServer);
		valueList.add(valueCollected);

		return valueList;
	}

}

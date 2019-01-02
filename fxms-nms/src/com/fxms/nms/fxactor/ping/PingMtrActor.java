package com.fxms.nms.fxactor.ping;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fxms.nms.NmsCodes;
import com.fxms.nms.fxactor.ping.data.Mtr;
import com.fxms.nms.fxactor.ping.data.MtrPingResult;
import com.fxms.nms.mo.pmo.IpPmo;

import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.Mo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class PingMtrActor extends PingFxActor {

	public PingMtrActor() {

	}

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof IpPmo) == false) {
			return null;
		}

		IpPmo node = (IpPmo) mo;

		String cmd = getCommandMtr() + " " + node.getIpAddress();

		if (Logger.logger.isTrace()) {
			Logger.logger.trace(cmd);
		}

		StringBuffer cmdResult;
		String serverIp;
		cmdResult = new StringBuffer();
		serverIp = FxCfg.getCfg().getIpAddress();

		Runtime runtime = Runtime.getRuntime();
		Process pingProc = null;
		InputStream eis = null;
		InputStreamReader eisr = null;
		BufferedReader ebr = null;
		String line = null;

		try {

			pingProc = runtime.exec(cmd);

			eis = pingProc.getInputStream();
			eisr = new InputStreamReader(eis);
			ebr = new BufferedReader(eisr);

			while (true) {

				line = ebr.readLine();

				if (line == null || eis.available() < 0)
					break;

				if (line != null && line.length() > 0) {
					cmdResult.append(line + "\n");

					if (line.indexOf(node.getIpAddress()) >= 0)
						break;
				}

			}

		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			try {
				if (eis != null)
					eis.close();
			} catch (Exception e) {
			}

			try {
				if (eisr != null)
					eisr.close();
			} catch (Exception e) {
			}

			try {
				if (ebr != null)
					ebr.close();
			} catch (Exception e) {
			}

			if (pingProc != null) {
				pingProc.destroy();
				pingProc = null;
			}
		}

		writeLog(node, cmdResult);

		MtrPingResult result = new MtrPingResult(node.getIpAddress(), cmdResult.toString());

		List<PsVo> valueList = make(node, result.getMtr(), serverIp);

		return valueList;
	}

	/**
	 * 로그 기록<br>
	 * 결과를 별도 기록을 원할 경우 이 곳에서 처리합니다.
	 * 
	 * @param node
	 * @param cmdResult
	 */
	protected void writeLog(IpPmo node, StringBuffer cmdResult) {
		Logger.logger.trace(cmdResult.toString());
	}

	private String getCommandMtr() {
		Object cmd = getPara("mtrCommand");
		if (cmd == null || cmd.toString().trim().length() == 0) {
			return "mtr -r -n  -c 3";
		} else
			return cmd.toString();
	}

	private List<PsVo> make(IpPmo node, Mtr mtr, String serverIp) {

		if (mtr == null)
			return null;

		List<PsVo> valueList = new ArrayList<PsVo>();
		PsVo value;
		String instance = serverIp;

		value = new PsVo(node, instance, NmsCodes.PsItem.NeMtrAvg, mtr.avg);
		valueList.add(value);

		value = new PsVo(node, instance, NmsCodes.PsItem.NeMtrBest, mtr.best);
		valueList.add(value);

		value = new PsVo(node, instance, NmsCodes.PsItem.NeMtrWrst, mtr.wrst);
		valueList.add(value);

		value = new PsVo(node, instance, NmsCodes.PsItem.NeMtrLoss, mtr.loss);
		valueList.add(value);

		return valueList;

	}

}

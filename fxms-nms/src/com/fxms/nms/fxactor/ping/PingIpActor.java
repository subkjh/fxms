package com.fxms.nms.fxactor.ping;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.nms.NmsCodes;
import com.fxms.nms.fxactor.ping.data.FPingRet;
import com.fxms.nms.mo.NeIfMo;
import com.fxms.nms.mo.IpMo;
import com.fxms.nms.mo.NeMo;
import com.fxms.nms.mo.pmo.IpPmo;
import com.fxms.nms.mo.pmo.IpsPmo;

import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import subkjh.bas.lang.Lang;
import subkjh.bas.log.Logger;

public class PingIpActor extends PingFxActor {

	/**
	 * 
	 * @param cmd
	 * @param ipList
	 * @return
	 */
	public List<FPingRet> fping(String cmd, List<String> ipList) {

		StringBuffer fpingCommand = new StringBuffer();

		fpingCommand.append(cmd);
		for (String ip : ipList)
			fpingCommand.append(" " + ip);

		if (isWindows())
			fpingCommand.append(" -i -w 10 -n 2");

		List<FPingRet> resultList = new ArrayList<FPingRet>();
		Map<String, FPingRet> reulstMap = new HashMap<String, FPingRet>();
		FPingRet fpingResultOld;

		if (Logger.logger.isTrace()) {
			Logger.logger.trace(fpingCommand.toString());
		}

		Runtime runtime = Runtime.getRuntime();
		Process pingProc = null;
		FPingRet result;
		InputStream eis = null;
		InputStreamReader eisr = null;
		BufferedReader ebr = null;
		String line = null;

		try {

			pingProc = runtime.exec(fpingCommand.toString());

			if (isWindows())
				eis = pingProc.getInputStream();
			else
				eis = pingProc.getErrorStream();

			eisr = new InputStreamReader(eis);
			ebr = new BufferedReader(eisr);

			while (true) {

				line = ebr.readLine();

				if (line == null && eis.available() == 0)
					break;

				Logger.logger.trace(line);

				if (line.contains("run by root") || line.contains("cannot"))
					throw new Exception(line);

				try {
					result = parseResult(line);
				} catch (Exception e) {
					Logger.logger.error(e);
					continue;
				}

				if (result != null) {
					fpingResultOld = reulstMap.get(result.getIpAddress());
					if (fpingResultOld == null) {
						reulstMap.put(result.getIpAddress(), result);
					} else {
						fpingResultOld.count(result.isOnline() ? false : true);
					}
				}
			}

			FxServiceImpl.setError(getClass().getSimpleName(), null);

		} catch (Exception e) {
			Logger.logger.error(e);
			FxServiceImpl.setError(getClass().getSimpleName(), e.getMessage());
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

		for (FPingRet fpingResult : reulstMap.values()) {
			resultList.add(fpingResult);
		}

		Logger.logger.trace("finished");

		return resultList;
	}

	/**
	 * 
	 * @param cmd
	 * @param ip
	 * @return ping 결과
	 */
	public FPingRet fping(String cmd, String ip) {
		List<String> ipList = new ArrayList<String>();

		ipList.add(ip);

		List<FPingRet> resultList = fping(cmd, ipList);
		if (resultList != null && resultList.size() > 0)
			return resultList.get(0);
		return null;
	}

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof IpsPmo) == false) {
			return null;
		}

		IpsPmo node = (IpsPmo) mo;
		
		ping(1, node.getIpList());

		List<IpPmo> nodeRetryList;

		int retry = getRetry();

		for (int index = 2; index <= retry; index++) {

			nodeRetryList = getPingNodeToRetry(node.getIpList());

			if (nodeRetryList == null || nodeRetryList.size() == 0)
				break;

			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}

			ping(index, nodeRetryList);

		}

		String perfServer = null;
		perfServer = FxCfg.getCfg().getIpAddress();
		List<PsVo> valueList = new ArrayList<PsVo>();

		for (IpPmo ipMo : node.getIpList()) {

			if (ipMo.getStatusIcmp() == Mo.STATUS_UNKNOWN)
				continue;

			if (ipMo.getMoClass().startsWith(NeMo.MO_CLASS)) {
				valueList.add(new PsVo(ipMo, perfServer, NmsCodes.PsItem.NeIcmpStatus, ipMo.getStatusIcmp()));
				if (ipMo.getStatusIcmp() == Mo.STATUS_ON) {
					valueList.add(new PsVo(ipMo, perfServer, NmsCodes.PsItem.NeIcmpEchoTime, ipMo.getRtt()));
				}
			} else if (ipMo.getMoClass().startsWith(IpMo.MO_CLASS)) {
				valueList.add(new PsVo(ipMo, perfServer, NmsCodes.PsItem.IpIcmpStatus, ipMo.getStatusIcmp()));
			} else if (ipMo.getMoClass().startsWith(NeIfMo.MO_CLASS)) {
				valueList.add(new PsVo(ipMo, perfServer, NmsCodes.PsItem.IfIcmpStatus, ipMo.getStatusIcmp()));
			}

		}

		return valueList;
	}

	public FPingRet parseResult(String str) throws Exception {
		FPingRet ret;
		if (isWindows())
			ret = parseResultWindows(str);
		else
			ret = parseResultUnix(str);

		if (ret != null) {
			if (Logger.logger.isTrace()) {
				Logger.logger.trace(str, ret.getIpAddress(), ret.isOnline());
			}
		}

		return ret;
	}

	/**
	 * 
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public FPingRet ping(String ip) throws Exception {

		List<String> ipList = new ArrayList<String>();
		ipList.add(ip);

		String cmd;

		try {
			cmd = getCommandFping();
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

		if (Logger.logger.isTrace()) {
			Logger.logger.trace(cmd);
		}

		List<FPingRet> resultList = fping(cmd, ipList);
		if (resultList != null && resultList.size() > 0)
			return resultList.get(0);

		return null;
	}

	private String getCommandFping() throws Exception {

		Object v = getPara("cmdFPing");
		if (v == null)
			throw new Exception("'COMMAND_FPING' PARAMETER NOT DEFINED");

		String command = v.toString();

		if (command.toCharArray()[0] == '.') {
			command = FxCfg.getHomeDeploy() + File.separator + "binext" + command.substring(1);
		}

		try {
			File f = new File(command.substring(0, command.indexOf(" ")));
			if (f.exists() == false) {
				throw new Exception(Lang.get("FILE({}) NOT FOUND", f.getPath()));

			}
		} catch (Exception e) {
		}

		return command;
	}

	private List<IpPmo> getPingNodeToRetry(List<IpPmo> nodeList) {
		List<IpPmo> retryNodeList = new ArrayList<IpPmo>();
		for (IpPmo node : nodeList) {
			if (node.getStatusIcmp() != Mo.STATUS_ON) {
				if (node.getStatusIcmp() == Mo.STATUS_UNKNOWN) {
					Logger.logger.trace(node.getIpAddress() + ":" + node);
				}
				retryNodeList.add(node);
			}
		}

		return retryNodeList;
	}

	/**
	 * 
	 * @return off인 경우 재시도 횟수
	 */
	private int getRetry() {

		Object v = getPara("retry");
		if (v != null) {
			try {
				return Integer.parseInt(v.toString());
			} catch (Exception e) {
			}
		}

		return 2;

	}

	private boolean isWindows() {
		String os = System.getProperty("os.name", "");
		return os.toLowerCase().indexOf("window") >= 0;
	}

	/**
	 * 응답 여부를 판단하고 ipList에서 제거한다.
	 * 
	 * @param str
	 */
	private FPingRet parseResultUnix(String str) throws Exception {
		/*
		 * 
		 * [root@Package fping-2.4b2_to]# fping -c 2 167.1.21.3 167.1.21.5 167.1.21.6
		 * 167.1.21.112 167.1.21.3 : [0], 84 bytes, 0.06 ms (0.06 avg, 0% loss)
		 * 167.1.21.5 : [0], 84 bytes, 0.25 ms (0.25 avg, 0% loss) 167.1.21.6 : [0], 84
		 * bytes, 0.21 ms (0.21 avg, 0% loss) 167.1.21.3 : [1], 84 bytes, 0.05 ms (0.05
		 * avg, 0% loss) 167.1.21.5 : [1], 84 bytes, 0.22 ms (0.23 avg, 0% loss)
		 * 167.1.21.6 : [1], 84 bytes, 0.20 ms (0.20 avg, 0% loss)
		 * 
		 * 167.1.21.3 : xmt/rcv/%loss = 2/2/0%, min/avg/max = 0.05/0.05/0.06 167.1.21.5
		 * : xmt/rcv/%loss = 2/2/0%, min/avg/max = 0.22/0.23/0.25 167.1.21.6 :
		 * xmt/rcv/%loss = 2/2/0%, min/avg/max = 0.20/0.20/0.21 167.1.21.112 :
		 * xmt/rcv/%loss = 2/0/100%
		 */

		str = str.trim();

		float rtt;

		if (str.length() <= 0)
			return null;
		if (str.indexOf(":") < 0)
			return null;
		if (str.indexOf("xmt") < 0)
			return null;

		String ip = str.substring(0, str.indexOf(":"));
		ip = ip.trim();

		FPingRet result = new FPingRet(ip);

		int rttIdx = str.indexOf("max");

		if (rttIdx < 0) {
			result.setRtt(-1);
			result.count(true);
		} else {
			String rttStr = str.substring(str.indexOf("max ="), str.length());
			rttStr = rttStr.substring(rttStr.indexOf("/") + 1, rttStr.length());
			rttStr = rttStr.substring(0, rttStr.indexOf("/"));

			rtt = Float.parseFloat(rttStr);
			if (rtt < 0) {
				Logger.logger.debug(str);
			}

			result.setRtt(rtt);
			result.count(false);
		}

		return result;
	}

	private FPingRet parseResultWindows(String str) throws Exception {

		/*
		 * Fast pinger version 2.20 (c) Wouter Dhondt (http://www.kwakkelflap.com)
		 * 
		 * Pinging multiple hosts with 32 bytes of data every 1000 ms:
		 * 
		 * Reply[1] from 172.16.200.248: bytes=32 time=274.5 ms TTL=244 Reply[2] from
		 * 172.16.200.249: bytes=32 time=332.4 ms TTL=244 Reply[3] from 172.16.200.250:
		 * bytes=32 time=349.2 ms TTL=244 Reply[4] from 167.1.21.3: bytes=32 time=0.4 ms
		 * TTL=255 167.1.21.20: request timed out 167.1.21.21: request timed out
		 * Reply[7] from 167.1.21.112: bytes=32 time=0.1 ms TTL=128 167.1.21.23: request
		 * timed out 167.1.21.25: request timed out Reply[10] from 167.1.21.4: bytes=32
		 * time=1.4 ms TTL=60 Reply[11] from 167.1.21.5: bytes=32 time=0.2 ms TTL=255
		 * 
		 * Ping statistics for multiple hosts: Packets: Sent = 11, Received = 7, Lost =
		 * 4 (36% loss) Approximate round trip times in milli-seconds: Minimum = 0.1 ms,
		 * Maximum = 349.2 ms, Average = 136.9 ms
		 */

		int pos2 = 0;
		int pos;
		String ip = null;
		if (str == null)
			return null;
		FPingRet result = null;

		pos = str.indexOf("timed out");
		if (pos >= 0) {
			ip = str.split(":")[0];
			result = new FPingRet(ip);
			result.setRtt(-1);
			result.count(true);
		} else {
			pos = str.indexOf("from ");
			if (pos > 0) {
				pos2 = str.indexOf(":");
				ip = str.substring(pos + 5, pos2);

				result = new FPingRet(ip);

				pos = str.indexOf("time=");
				if (pos > 0) {
					result.setRtt(Float.parseFloat(str.substring(pos + 5, str.indexOf("ms")).trim()));
					result.count(false);
				} else {
					result.setRtt(0);
					result.count(false);
				}
			}
		}
		return result;
	}

	private void ping(int index, List<IpPmo> _nodeList) throws Exception {

		if (_nodeList.size() == 0)
			return;

		List<String> ipList = new ArrayList<String>();
		for (IpPmo node : _nodeList) {
			if (ipList.contains(node.getIpAddress()) == false) {
				ipList.add(node.getIpAddress());
			}
		}

		if (Logger.logger.isTrace()) {
			Logger.logger.trace("try#" + index + " : node=" + _nodeList.size() + ",ip=" + ipList.size());
		}

		String cmd;

		try {
			cmd = getCommandFping();
		} catch (Exception e) {
			Logger.logger.error(e);
			return;
		}

		if (Logger.logger.isTrace()) {
			Logger.logger.trace(cmd);
		}

		List<FPingRet> resultList = fping(cmd, ipList);
		if (resultList != null) {
			for (FPingRet result : resultList) {

				for (IpPmo node : _nodeList) {
					if (result.getIpAddress().equals(node.getIpAddress())) {
						if (result.getRttMs() >= 0)
							node.setRtt(result.getRttMs());
						node.setStatusIcmp(result.isOnline() ? Mo.STATUS_ON : Mo.STATUS_OFF);
					}
				}
			}
		}

	}
}

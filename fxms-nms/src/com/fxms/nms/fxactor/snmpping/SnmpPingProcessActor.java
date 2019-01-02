package com.fxms.nms.fxactor.snmpping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.nms.NmsCodes;
import com.fxms.nms.mo.NeMo;
import com.fxms.nms.mo.ProcessMo;
import com.fxms.nms.snmp.SnmpUtil.EXIST_RET;
import com.fxms.nms.snmp.beans.OidValue;
import com.fxms.nms.snmp.mib.HOST_RESOURCES_MIB;
import com.fxms.nms.snmp.mib.HpMIB;

import fxms.bas.api.FxApi;
import fxms.bas.mo.Mo;
import fxms.bas.poller.exception.PollingTimeoutException;
import fxms.bas.pso.PsVo;
import fxms.bas.pso.counter.CounterMgr;
import subkjh.bas.log.Logger;

/**
 * ProcessService의 기능이 이곳으로 이관하는 방안
 * 
 * @author subkjh
 * @since 2013.04.12
 */
public class SnmpPingProcessActor extends SnmpPingFxActor {

	class Proc {
		String pid;
		String name;
		String path;
		String para;
	}

	class Res {
		String pid;
		long cpu = -1;
		long mem = -1;
	}

	private short mibType;
	private final HOST_RESOURCES_MIB MIB = new HOST_RESOURCES_MIB();

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof NeMo) == false) {
			return null;
		}

		NeMo node = (NeMo) mo;

		List<ProcessMo> moList = node.getMoConfig().getChildren(ProcessMo.class);
		if (moList == null || moList.size() == 0)
			return null;

		for (ProcessMo process : moList) {
			process.initPolling();
		}

		EXIST_RET ret;

		String oidArray[] = new String[] { MIB.hrDeviceIndex, new HpMIB().processPID };

		for (mibType = 0; mibType < oidArray.length; mibType++) {

			if (node.oidNotExist(oidArray[mibType]) == false) {

				ret = getSnmpUtil().exist(node, oidArray[mibType]);

				if (ret == EXIST_RET.exist) {
					switch (mibType) {
					case 0:
						checkHostMibAll(node, moList);
						checkHostMibCpuMem(node, moList);
						break;
					case 1:
						checkHp(node, moList);
						checkHpCpuMem(node, moList);
						break;
					default:
						break;
					}
					break;
				} else if (ret == EXIST_RET.notfound) {
					node.getOidNotExistList().add(oidArray[mibType]);
				}
			}
		}

		return makeValue(pollMsdate, node, moList);

	}

	private void checkHostMibAll(NeMo node, List<ProcessMo> moList) throws Exception {

		Collection<Proc> procList = snmpgetProc(node);
		if (procList.size() == 0)
			return;

		PROC: for (Proc p : procList) {
			for (ProcessMo process : moList) {
				if (process.match(p.name, p.path, p.para)) {
					process.addPid(Integer.parseInt(p.pid));
					if (Logger.debug) {
						System.out
								.println(process.getMoNo() + "," + p.pid + "," + p.name + "," + p.path + "," + p.para);
					}
					continue PROC;
				}
			}

		}

	}

	private void checkHostMibCpuMem(NeMo node, List<ProcessMo> moList) throws Exception {

		int pid;
		ProcessMo process;

		Collection<Res> resList = snmpgetRes(node);
		for (Res res : resList) {

			pid = Integer.parseInt(res.pid);

			process = find(moList, pid);

			if (process != null) {
				if (Logger.debug) {
					System.out.println(process.getMoNo() + "," + pid + "," + res.cpu + "," + res.mem);
				}

				if (res.cpu >= 0) {
					if (process.getCpuTime() < 0) {
						process.setCpuTime(res.cpu);
					} else {
						process.setCpuTime(process.getCpuTime() + res.cpu);
					}
				}

				if (res.mem >= 0) {
					if (process.getMemUsed() < 0) {
						process.setMemUsed(res.mem);
					} else {
						process.setMemUsed(process.getMemUsed() + res.mem);
					}
				}

			}
		}

	}

	private void checkHp(NeMo node, List<ProcessMo> moList) {

		HpMIB hpOid = new HpMIB();
		String name_path_para[];
		String name, path, para;
		int pid;

		try {
			List<OidValue> varList = getSnmpUtil().snmpwalk(node, hpOid.processCmd);

			if (varList == null || varList.size() == 0)
				return;

			GET: for (OidValue var : varList) {

				name_path_para = hpOid.splitProcessName_Path_Para(var.getValue());

				pid = Integer.parseInt(var.getInstance(1));
				name = name_path_para[0];
				path = name_path_para[1];
				para = name_path_para[2];

				for (ProcessMo process : moList) {
					if (process.match(name, path, para)) {
						process.addPid(pid);
						continue GET;
					}
				}
			}

		} catch (Exception e) {
			Logger.logger.fail(e.getMessage());
		}

	}

	private void checkHpCpuMem(NeMo node, List<ProcessMo> moList) {
		HpMIB hpOid = new HpMIB();
		List<String> oidList = new ArrayList<String>();

		for (ProcessMo process : moList) {
			if (process.getPid() > 0) {
				oidList.add(hpOid.processCPU + "." + process.getPid());
			}
		}

		if (oidList.size() == 0)
			return;

		try {
			int pid;
			List<OidValue> varList = getSnmpUtil().snmpwalk(node, hpOid.processCPU);
			ProcessMo process;
			long cputime;
			if (varList != null && varList.size() > 0) {

				for (OidValue var : varList) {
					pid = Integer.parseInt(var.getInstance(1));
					process = find(moList, pid);
					if (process != null) {
						cputime = var.getLong(0);
						if (process.getCpuTime() < 0) {
							process.setCpuTime(cputime);
						} else {
							process.setCpuTime(process.getCpuTime() + cputime);
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.logger.fail(e.getMessage());
		}
	}

	private ProcessMo find(List<ProcessMo> moList, int pid) {
		for (ProcessMo proc : moList) {
			if (proc.containsPid(pid))
				return proc;
		}
		return null;
	}

	private List<PsVo> makeValue(long pollMsdate, NeMo node, List<ProcessMo> moList) {

		List<PsVo> valueList = new ArrayList<PsVo>();
		String instance;
		int cntOn = 0, cntOff = 0;

		for (ProcessMo process : moList) {

			if (process.isMngYn() == false)
				continue;

			if (process.getStatusRun() == ProcessMo.STATUS_RUN_RESTART
					|| process.getStatusRun() == ProcessMo.STATUS_RUN_SOME_CHANGE) {
				instance = FxApi.getDate(pollMsdate) + "";
			} else {
				instance = null;
			}

			if (Logger.logger.isTrace()) {
				Logger.logger.trace(process.toString() + ", pid=" + process.getPidList() + ", run="
						+ process.getStatusRun() + ", cpu=" + process.getCpuTime() + ", mem=" + process.getMemUsed()
						+ " instance=" + instance);
			}

			if (process.getStatusRun() != ProcessMo.STATUS_RUN_OFF) {
				if (process.getCpuTime() >= 0) {
					valueList.add(new PsVo(process, null, NmsCodes.PsItem.ProcessCpuTime, process.getCpuTime()));
				}
				if (process.getMemUsed() >= 0) {
					valueList.add(new PsVo(process, null, NmsCodes.PsItem.ProcessMemUsed, process.getMemUsed()));
				}
				valueList.add(new PsVo(process, null, NmsCodes.PsItem.ProcessCount, process.getCountProc()));
				cntOn++;
			} else {
				cntOff++;
			}

			valueList.add(new PsVo(process, null, NmsCodes.PsItem.ProcessStatus, process.getStatusRun()));

		}

		valueList.add(new PsVo(node, null, NmsCodes.PsItem.ProcessOnRate, (cntOn * 100) / (cntOn + cntOff)));

		return valueList;
	}

	private Collection<Proc> snmpgetProc(NeMo node) throws Exception {

		Map<String, Proc> map = new HashMap<String, Proc>();

		String pid;
		Proc proc;

		List<OidValue> valueList = getSnmpUtil().snmpwalk(node, MIB.hrSWRunName);
		if (valueList != null) {
			for (OidValue v : valueList) {
				pid = v.getInstance(1);
				proc = new Proc();
				proc.pid = pid;
				proc.name = v.getValue();
				map.put(pid, proc);
			}
		}

		valueList = getSnmpUtil().snmpwalk(node, MIB.hrSWRunPath);
		if (valueList != null) {
			for (OidValue v : valueList) {
				pid = v.getInstance(1);
				proc = map.get(pid);
				if (proc != null) {
					proc.path = v.getValue();
				}
			}
		}

		valueList = getSnmpUtil().snmpwalk(node, MIB.hrSWRunParameters);
		if (valueList != null) {
			for (OidValue v : valueList) {
				pid = v.getInstance(1);
				proc = map.get(pid);
				if (proc != null) {
					proc.para = v.getValue();
				}
			}
		}

		return map.values();

	}

	private Collection<Res> snmpgetRes(NeMo node) throws Exception {

		Map<String, Res> map = new HashMap<String, Res>();
		long mstime = System.currentTimeMillis();

		String pid;
		Res res;

		List<OidValue> valueList = getSnmpUtil().snmpwalk(node, MIB.hrSWRunPerfCPU);
		if (valueList != null) {
			for (OidValue v : valueList) {
				pid = v.getInstance(1);
				res = new Res();
				res.pid = pid;
				res.cpu = v.getLong(0);

				try {
					// pid별 초당 CPU 사용 시간을 계산 합니다.
					res.cpu = CounterMgr.getMgr().getValueCounter(node.getMoNo() + "." + pid + ".cpu", mstime,
							res.cpu * 10000);
				} catch (Exception e) {
					res.cpu = -1;
				}
				map.put(pid, res);
			}
		}

		valueList = getSnmpUtil().snmpwalk(node, MIB.hrSWRunPerfMem);
		if (valueList != null) {
			for (OidValue v : valueList) {
				pid = v.getInstance(1);
				res = map.get(pid);
				if (res != null) {
					res.mem = v.getLong(0);
				}
			}
		}

		return map.values();

	}

}

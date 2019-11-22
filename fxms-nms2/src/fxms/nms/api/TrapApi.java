package fxms.nms.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.ao.AoCode;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.api.CoApi;
import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.noti.NotiSender;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.co.thread.BatchSaver;
import fxms.bas.co.vo.FileVo;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.HasIp;
import fxms.nms.co.cd.NmsCode;
import fxms.nms.co.snmp.trap.TrapNode;
import fxms.nms.co.snmp.trap.adapter.TrapAdapter;
import fxms.nms.co.snmp.trap.vo.TrapEventLog;
import fxms.nms.co.snmp.trap.vo.TrapEventLogList;
import fxms.nms.co.snmp.trap.vo.TrapPattern;
import fxms.nms.co.snmp.trap.vo.TrapVo;
import fxms.nms.mo.NeIfMo;
import fxms.nms.mo.property.MoSnmppable;
import fxms.nms.mo.property.SnmpPass;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;

/**
 * TrapService API
 * 
 * @author subkjh
 * 
 */
public abstract class TrapApi extends FxApi {

	/**
	 * TrapApi
	 */
	public static TrapApi api;

	private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static TrapApi getApi() {
		if (api != null)
			return api;

		api = makeApi(TrapApi.class);

		api.reload();

		return api;
	}

	public static void main(String[] args) {

		String s = ".1.3.6.1.2.1.2.2.1.1";
		System.out.println(s.replaceFirst(".1.3.6.1.2.1.2.2.1.1", ""));

		String oid = ".1.4.6.12.3.4.3.0";
		int endIndex = oid.lastIndexOf(".");
		if (endIndex > 0) {
			System.out.println(oid.substring(0, endIndex));
			System.out.println(oid.substring(endIndex));
		}
	}

	public static <T extends MoSnmppable> T makeNodeSnmp(String ipAddress, Class<T> classOfT) {
		T nodeT;
		try {
			nodeT = classOfT.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}

		nodeT.setIpAddress(ipAddress);
		nodeT.setSnmpPass(SnmpPass.defSnmp);

		// nodeT.setTelnetPass(true, TelnetPass.defTelnet);

		return nodeT;
	}

	private boolean broadcastTrap;
	private boolean allowSameEvent; /* 동일한 경보에 대해서 한 번만 발생할 것인지 계속발생시킬것인지 */
	private boolean writeToDatabase; /* trap을 DB에 기록할지 여부 */
	private long trapSeqno;
	private List<TrapPattern> patternList = null;
	private Map<Long, Alarm> trapAlarmMap; /* 트랩 일련번호에 대한 경보를 가지고 있습니다. */
	private List<TrapAdapter> adapterList; /* 필터 목록 */
	private NotiSender trapSender;
	private BatchSaver<TrapEventLog> saver;

	private List<TrapNode> nodeList;
	private Map<String, TrapNode> ipMap;/* key : IP주소 */

	/**
	 * 
	 */
	public TrapApi() {
		adapterList = new ArrayList<TrapAdapter>();
		trapAlarmMap = new HashMap<Long, Alarm>();
		trapSeqno = 0;
		ipMap = new HashMap<String, TrapNode>();
	}

	/**
	 * 
	 * @param syslogList
	 */
	public void broadcastLog(List<TrapEventLog> logList) {

		if (broadcastTrap == false)
			return;

		TrapEventLogList eventLogList = new TrapEventLogList();
		eventLogList.setStatus(FxEvent.STATUS.added);
		eventLogList.addAll(logList);

		if (trapSender != null) {
			trapSender.put(eventLogList);
		} else {
			FxServiceImpl.fxService.send(eventLogList);
		}

	}

	/**
	 * 보관 기관이 지난 자료를 삭제합니다.
	 * 
	 * @throws Exception
	 */
	public void deleteLogExpired() {
		try {
			doDeleteLogExpired();
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	/**
	 * 
	 * @return 필터목록
	 */
	public List<TrapAdapter> getAdapterList() {
		return adapterList;
	}

	public Alarm getAlarmTrapSeqno(long seqno) {
		return trapAlarmMap.get(seqno);
	}

	public Mo getMoByMac(String macAddr) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("macAddress", macAddr);
		return MoApi.getApi().getMo(para);
	}

	@Override
	public String getName() {
		return "API-TRAP";
	}

	public Mo getNeIf(long neMoNo, int ifIndex) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("upperMoNo", neMoNo);
		para.put("ifIndex", ifIndex);
		para.put("moClass", NeIfMo.MO_CLASS);
		return MoApi.getApi().getMo(para);
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append("PATTERN-SIZE(" + patternList.size() + ")");
		sb.append("ADAPTER-SIZE(" + adapterList.size() + ")");
		sb.append("NODE-SIZE(" + nodeList.size() + ")");

		return sb.toString();
	}

	/**
	 * 노드에 해당되는 트랩경보조건목록을 제공합니다.
	 * 
	 * @param node
	 * @return 트랩경보조건목록
	 */
	public List<TrapPattern> getThrByNode(TrapNode node) {

		List<TrapPattern> retList = new ArrayList<TrapPattern>();
		for (TrapPattern th : patternList) {
			if (th.match(node)) {
				retList.add(th);
			}
		}

		return retList;
	}

	/**
	 * TRAP 화일을 제공합니다.
	 * 
	 * @param yyyymmdd  받은일자
	 * @param ipaddress IP주소
	 * @return 화일내용
	 * @throws FileNotFoundException
	 * @throws Exception
	 * @since 2013.02.19 by subkjh
	 */
	public FileVo getTrapFile(String yyyymmdd, String ipaddress) throws FileNotFoundException, Exception {

		String filename = BasCfg.getFile(BasCfg.getHomeDatas(), "trap", yyyymmdd + "", ipaddress + ".log");
		File file = new File(filename);
		if (file.exists() == false) {
			filename = BasCfg.getFile(BasCfg.getHomeDatas(), "trap", yyyymmdd + "", ipaddress + ".zip");
			file = new File(filename);
		}

		if (file.exists() == false)
			throw new FileNotFoundException(filename);

		return new FileVo(file);
	}

	/**
	 * 트랩에 해당되는 노드를 찾습니다.
	 * 
	 * @param trapEvent
	 * @return
	 */
	public TrapNode getTrapNode(TrapVo vo) {
		return ipMap.get(vo.getIpAddress());
	}

	/**
	 * 
	 * @param ipAddress
	 * @return IP주소에 해당되는 장비
	 */
	public TrapNode getTrapNodeByIpaddress(String ipAddress) {
		return ipMap.get(ipAddress);
	}

	/**
	 * 
	 * @return
	 */
	public synchronized long getTrapSeqnoNext() {
		trapSeqno++;
		return trapSeqno;
	}

	/**
	 * 트랩로그를 기록합니다.<br>
	 * 저장소에 기록이 안 될 경우 미전송 백업 폴더에 기록합니다.
	 * 
	 * @param trapLog
	 */
	public void insertLog(TrapEventLog trapLog) {

		Logger.logger.debug("{}", trapLog);

		if (saver != null) {
			saver.put(trapLog);
		}

	}

	/**
	 * 같은 경보에 대해서 한 번만 이벤트 발생여부<br>
	 * 
	 * @return true이면 한번만.
	 */
	public boolean isOnceSameTrap() {
		return allowSameEvent;
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {
		if (noti instanceof ReloadSignal) {
			ReloadSignal r = (ReloadSignal) noti;
			if (r.contains(ReloadSignal.RELOAD_TYPE_ALL)) {
				reload();
			} else if (r.contains(ReloadSignal.RELOAD_TYPE_MO)) {
				loadTrapNode();
			} else if (r.contains("TRAP-ALARM-CFG")) {
				loadTrapPattern();
			}
		} else if (noti instanceof Mo) {

			loadTrapNode();

			// 등록되지 않은 장비로 부터의 SNMP TRAP 발생 경보가 존재하면 해제합니다.
			if (noti instanceof HasIp) {
				TrapNode node = TrapApi.api.getTrapNodeByIpaddress(((HasIp) noti).getIpAddress());
				if (node instanceof Mo) {
					EventApi.getApi().checkClear((Mo) node, "unknown-ne-trap", AoCode.ClearReason.Auto);
				}
			}

		}

	}

	public Alarm removeAlarmTrapSeqno(long seqno) {
		return trapAlarmMap.remove(seqno);
	}

	public void sendEventInvalidNode(TrapNode node) {
		if (node instanceof Mo) {
			EventApi.getApi().check((Mo) node, null, NmsCode.AlarmCode.NOT_SET_TRAP, null, null);
		}
	}

	/**
	 * 
	 * @param ipAddress
	 */
	public void sendEventUnknownNode(String ipAddress) {
		EventApi.getApi().check(null, ipAddress, NmsCode.AlarmCode.UNKNOWN_TRAP, "ip:" + ipAddress, null);
	}

	private String trapFolder;

	/**
	 * 받은 TRAP을 화일에 기록합니다.
	 * 
	 * @param trapLog
	 */
	public synchronized void write2File(TrapVo vo) {

		if (trapFolder == null) {
			return;
		}

		File folder = new File(trapFolder);
		if (folder.exists() == false) {
			folder.mkdirs();
		}

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(vo.getMstimeRecv());
		String dateString = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		String filename = BasCfg.getFile(trapFolder, dateString, vo.getIpAddress() + ".log");

		String msTimeString;
		msTimeString = HHMMSS.format(new Date(vo.getMstimeRecv()));
		msTimeString = msTimeString + "." + String.format("%03d", (int) (vo.getMstimeRecv() % 1000));

		try {
			fileWriter = new FileWriter(new File(filename), true);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(msTimeString + " " + vo.getFileLine());
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			try {
				if (bufferedWriter != null)
					bufferedWriter.close();
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

	}

	protected abstract void doDeleteLogExpired() throws Exception;

	/**
	 * 트랩 로그 기록
	 * 
	 * @param logList
	 * @return 처리결과
	 */
	protected abstract void doInsertLog(List<TrapEventLog> logList) throws Exception;

	/**
	 * 
	 * @return 관리대상 노드 목록
	 */
	protected abstract List<TrapNode> doSelectTrapNode() throws Exception;

	/**
	 * 트랩 임계를 저장소로 부터 가져옵니다.
	 * 
	 * @return 임계목록
	 */
	protected abstract List<TrapPattern> doSelectTrapPattern() throws Exception;

	@Override
	protected void initApi() throws Exception {

		broadcastTrap = getFxPara().getBoolean("broadcastTrap", false);
		writeToDatabase = getFxPara().getBoolean("writeToDatabase", false);
		allowSameEvent = getFxPara().getBoolean("allowSameEvent", false);
		trapFolder = getFxPara().getString("trap-file-folder");

		if (trapFolder == null) {
			trapFolder = FxCfg.getHome() + File.separator + "datas" + File.separator + "syslog";
		} else if (trapFolder.charAt(0) != '/') {
			trapFolder = BasCfg.getHome() + File.separator + trapFolder;
		}

		if (writeToDatabase) {
			saver = new BatchSaver<TrapEventLog>("TrapSaver" //
					, getFxPara().getInt("insert-batch-size", 30) //
					, trapFolder + File.separator + "NotSend") {

				@Override
				public void doInsert(List<TrapEventLog> logList) throws Exception {
					try {
						doInsertLog(logList);
					} catch (Exception e) {
						throw e;
					}
				}

			};
			saver.start();
		}

		adapterList = FxActorParser.getParser().getActorList(TrapAdapter.class);
	}

	@Override
	protected void reload() {
		loadTrapPattern();
		loadTrapNode();
	}

	private void loadTrapNode() {

		try {
			List<TrapNode> nodeList = doSelectTrapNode();

			Map<String, TrapNode> ipMap = new HashMap<String, TrapNode>();

			for (TrapNode node : nodeList) {
				if (node.getIpAddress() != null) {
					ipMap.put(node.getIpAddress(), node);
				}
			}

			this.nodeList = nodeList;
			this.ipMap = ipMap;

			Logger.logger.debug("count-mo={}", nodeList.size());

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	private void loadTrapPattern() {

		try {
			List<TrapPattern> list = doSelectTrapPattern();

			patternList = list;

			Logger.logger.debug("pattern-size={}", patternList.size());

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void zipTrapFiles() throws Exception {

		if (trapFolder == null) {
			return;
		}

		File folder = new File(trapFolder);

		if (folder.exists() == false) {
			return;
		}

		int zipCnt = 0, delCnt = 0;
		int ret;
		int trapLogFileTermDays = getFxPara().getInt(NmsCode.Var.KEEP_DAYS_LOG_TRAP, getDays2KeepFile());
		int zipAfterDays = getFxPara().getInt(NmsCode.Var.TRAP_MAKE_ZIP_AFTER_DAYS, getDays2Zip());
		boolean zipOneFile = getFxPara().getBoolean("zipOneFile", true);

		String delYmd = FxApi.getYmd(System.currentTimeMillis() - (86400000 * trapLogFileTermDays)) + "";
		String zipYmd = FxApi.getYmd(System.currentTimeMillis() - (86400000 * zipAfterDays)) + "";

		Logger.logger.info("paramters : folder={}, del={}, zip={}", folder.getPath(), delYmd, zipYmd);

		// 보관 기간이 지난 자료 삭제
		if (trapLogFileTermDays > 0) {
			delCnt = 0;
			for (File e : folder.listFiles()) {
				if (e.getName().compareTo(delYmd) < 0) {
					ret = FileUtil.delete(e);
					if (ret > 0) {
						Logger.logger.info("delete={}", e.getPath());
						delCnt += ret;
					} else {
						Logger.logger.fail("cannot delete={}", e.getPath());
					}
				}
			}
		}

		// log -> zip
		if (zipAfterDays > 0) {
			zipCnt = 0;
			File dst;
			for (File e : folder.listFiles()) {
				if (e.getName().compareTo(zipYmd) < 0) {
					if (zipOneFile) {
						dst = new File(e.getPath() + ".zip");
						FileUtil.zip(e, dst.getPath());
						FileUtil.delete(e);
						Logger.logger.info("zip {}->{}, delete={}", e.getPath(), dst.getPath(), e.getPath());
						zipCnt++;
					} else {
						for (File e1 : e.listFiles()) {
							if (e1.getName().endsWith("zip") == false) {
								dst = new File(e1.getPath() + ".zip");
								FileUtil.zipFile(e1, dst);
								zipCnt++;
								e1.delete();
								Logger.logger.info("zip {}->{}, delete={}", e1.getPath(), dst.getPath(), e1.getPath());
							}
						}
					}
				}
			}
		}

		StringBuffer sb = new StringBuffer();
		sb.append(Logger.makeString("SYSLOG-ZIP", "zip=" + zipCnt + ",del=" + delCnt));
		sb.append(Logger.makeSubString("folder", folder.getPath()));
		sb.append(Logger.makeSubString("to-del-date", delYmd));
		sb.append(Logger.makeSubString("to-zip-date", zipYmd));
		Logger.logger.info(sb.toString());
	}

	/**
	 * 
	 * @return SyslogVo 보관 일
	 */
	private int getDays2KeepFile() {
		int days = CoApi.getApi().getVarValue(NmsCode.Var.KEEP_DAYS_LOG_TRAP, -1);
		if (days <= 0) {
			days = 30;
			try {
				CoApi.getApi().setVarValue(NmsCode.Var.KEEP_DAYS_LOG_TRAP, days, false);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
		return days;
	}

	/**
	 * 
	 * @return ZIP으로 압축할 경과 일자
	 */
	private int getDays2Zip() {
		int days = CoApi.getApi().getVarValue(NmsCode.Var.TRAP_MAKE_ZIP_AFTER_DAYS, -1);
		if (days <= 0) {
			days = 7;
			try {
				CoApi.getApi().setVarValue(NmsCode.Var.TRAP_MAKE_ZIP_AFTER_DAYS, days, false);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
		return days;
	}
}

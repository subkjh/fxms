package fxms.nms.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.ao.AoCode;
import fxms.bas.api.CoApi;
import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
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
import fxms.nms.co.syslog.adapter.SyslogAdapter;
import fxms.nms.co.syslog.mo.SyslogMo;
import fxms.nms.co.syslog.mo.SyslogNode;
import fxms.nms.co.syslog.vo.SyslogEventLog;
import fxms.nms.co.syslog.vo.SyslogEventLogList;
import fxms.nms.co.syslog.vo.SyslogParsingResultVo;
import fxms.nms.co.syslog.vo.SyslogPattern;
import fxms.nms.co.syslog.vo.SyslogVo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;

/**
 * SyslogService에서 사용하는 API
 * 
 * @author subkjh
 * 
 */
public abstract class SyslogApi extends FxApi {

	/**
	 * SyslogApi
	 */
	public static SyslogApi api;

	private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 사용할 API를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static SyslogApi getApi() {

		if (api != null)
			return api;

		api = makeApi(SyslogApi.class);

		try {
			api.reload();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	/** 수신된 내용을 기록할지 여부 */
	private boolean saveSyslog = false;
	/** 받은 SYSLOG에 대해서 브로드캐스팅 할지 여부 */
	private boolean broadcastSyslog = false;
	/** FilterSyslog 목록 */
	private List<SyslogAdapter> adapterList;
	/** Syslog 관리 장비 목록. key=ip주소 */
	private Map<String, SyslogNode> nodeMapByIp;
	/** Syslog 관리 장비 목록. key=명칭 */
	private Map<String, SyslogNode> nodeMapByName;
	/** 큐 내에 처리할 내용이 아래 이상인 경우 이벤트를 발생합니다. */
	private int sizeMaxInQueueForEvent = 100;
	/** 동일한 경보에 대해서 한 번만 발생할 것인지 계속발생시킬것인지 */
	private boolean allowSameEvent;
	private NotiSender syslogSender;
	private BatchSaver<SyslogEventLog> saver;
	private List<SyslogPattern> patternList = null;
	private List<SyslogNode> nodeList = null;
	private String syslogFolder;

	public SyslogApi() {
		nodeMapByIp = new HashMap<String, SyslogNode>();
		nodeMapByName = new HashMap<String, SyslogNode>();
	}

	public void broadcastLog(List<SyslogEventLog> logList) {

		if (broadcastSyslog == false)
			return;

		SyslogEventLogList eventLogList = new SyslogEventLogList();
		eventLogList.setStatus(FxEvent.STATUS.added);
		eventLogList.addAll(logList);

		if (syslogSender != null) {
			syslogSender.put(eventLogList);
		} else {
			FxServiceImpl.fxService.send(eventLogList);
		}

	}

	public void checkQueueSize(String name, int size) {

		if (size > sizeMaxInQueueForEvent) {
			EventApi.getApi().check(null, name, NmsCode.AlarmCode.SYSLOG_QUEUE_JAM, null, null);
		} else {
			EventApi.getApi().checkClear(null, name, NmsCode.AlarmCode.SYSLOG_QUEUE_JAM, AoCode.ClearReason.Auto);
		}

	}

	public void deleteLogExpired() {
		try {
			doDeleteLogExpired();
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	public List<SyslogAdapter> getAdapterList() {
		return adapterList;
	}

	/**
	 * 
	 * @return HOME/datas/syslog
	 */
	public String getFolderSyslog() {
		return BasCfg.getFolder(BasCfg.getHomeDatas(), "syslog");
	}

	/**
	 * 수신한 SYSLOG의 MO를 조회한다.
	 * 
	 * @param vo
	 * @return
	 */
	public SyslogNode getNode(SyslogVo vo) {
		SyslogNode node = null;

		if (vo.getIpAddress() != null) {
			node = getNodeByIpAddress(vo.getIpAddress());
		}

		if (node == null && vo.getHostname() != null) {
			node = nodeMapByName.get(vo.getHostname());
		}

		return node;
	}

	/**
	 * IP주소에 해당되는 관리대상을 제공합니다.
	 * 
	 * @param ipAddress
	 * @return Syslog장비
	 */
	public SyslogNode getNodeByIpAddress(String ipAddress) {
		return nodeMapByIp.get(ipAddress);
	}

	/**
	 * 노드에 적용할 임계 조건을 검색합니다.
	 * 
	 * @param node 노드
	 * @return 적용할 임계 조건<br>
	 *         해당 사항이 없을 경우 size=0인 리스트 제공
	 */
	public List<SyslogPattern> getPatternList(SyslogNode node) {

		List<SyslogPattern> ret = new ArrayList<SyslogPattern>();

		for (SyslogPattern th : patternList) {
			if (th.equalModel(node)) {
				ret.add(th);
			}
		}

		return ret;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append("ADAPTER-SIZE(" + adapterList.size() + ")");
		sb.append("NODE-SIZE(" + nodeList.size() + ")");
		sb.append("PATTERN-SIZE(" + patternList.size() + ")");

		return sb.toString();
	}

	/**
	 * 
	 * @param yyyymmdd
	 * @param ipaddress
	 * @return
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public FileVo getSyslogFile(int yyyymmdd, String ipaddress) throws FileNotFoundException, Exception {

		String filename = BasCfg.getFile(BasCfg.getHomeDatas(), "syslog", yyyymmdd + "", ipaddress + ".log");
		File file = new File(filename);
		if (file.exists() == false) {
			filename = BasCfg.getFile(BasCfg.getHomeDatas(), "syslog", yyyymmdd + "", ipaddress + ".zip");
			file = new File(filename);
		}

		if (file.exists() == false)
			throw new FileNotFoundException(filename);

		return new FileVo(file);
	}

	/**
	 * 
	 * @param yyyymmdd
	 * @param ipAddress
	 * @return IP주소에 해당되는 장비의 SYSLOG 화일명
	 */
	public String getSyslogFilename(String yyyymmdd, String ipAddress) {
		return BasCfg.getFile(BasCfg.getHomeDatas(), "syslog", yyyymmdd, ipAddress + ".log");
	}

	public String getSyslogFolder() {
		return syslogFolder;
	}

	public void insertLog(SyslogEventLog event) {
		if (saver != null) {
			saver.put(event);
		}
	}

	/**
	 * 같은 경보에 대해서 한 번만 이벤트 발생여부<br>
	 * 
	 * @return true이면 한번만.
	 */
	public boolean isAllowSameEvent() {
		return allowSameEvent;
	}

	/**
	 * 
	 * @return 모든 SYSLOG를 EVENT화 여부
	 */
	public boolean isSaveSyslog() {
		return saveSyslog;
	}

	/**
	 * SYSLOG_EVENT를 생성합니다.
	 * 
	 * @param mo
	 * @param node
	 * @param raw        실제 SYSLOG데이터
	 * @param alarmLevel
	 * @param alarmCode
	 * @param alarmName
	 * @param alarmNo
	 * @return
	 */
	public SyslogEventLog makeSyslogEvent(Mo mo, SyslogNode syslognode, SyslogVo raw, int alarmLevel, int alarmCode,
			String alarmName, long alarmNo) {

		SyslogMo node = null;

		if (syslognode instanceof SyslogMo) {
			node = (SyslogMo) syslognode;
		} else {
			return null;
		}

		SyslogEventLog item = new SyslogEventLog();

		item.setAlarmCode(alarmCode);
		item.setAlarmName(alarmName);
		item.setAlarmLevel(alarmLevel);
		item.setSyslogPriority(raw.getSeverity());
		item.setSyslogFacility(raw.getFacility());
		item.setHstimeRecv(FxApi.getDate(raw.getMsTime()));
		item.setLogMsg(raw.getMsg());

		if (node != null && node instanceof SyslogMo) {
			SyslogMo syslogMo = (SyslogMo) node;
			item.setIpAddress(node.getIpAddress());
			item.setModelNo(syslogMo.getModelNo());
			item.setModelName(syslogMo.getModelName());
		}
		item.setAlarmNo(alarmNo);

		if (mo != null && node != null && mo.getMoNo() != node.getMoNo()) {
			item.setMoName(node.getMoName() + " (" + mo.getMoName() + ")");
			item.setMoNo(mo.getMoNo());
		} else if (mo != null) {
			item.setMoName(mo.getMoName());
			item.setMoNo(mo.getMoNo());
		}

		return item;
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		if (noti instanceof ReloadSignal) {
			ReloadSignal r = (ReloadSignal) noti;
			if (r.contains(NmsCode.EventType.SYSLOG_THR)) {
				loadSyslogPattern();
			} else if (r.contains(ReloadSignal.RELOAD_TYPE_MO)) {
				loadSyslogNode();
			} else if (r.contains(ReloadSignal.RELOAD_TYPE_ALL)) {
				loadSyslogPattern();
				loadSyslogNode();
			}
		} else if (noti instanceof Mo) {

			loadSyslogNode();

			// 등록되지 않은 장비로 부터의 SNMP TRAP 발생 경보가 존재하면 해제합니다.
			if (noti instanceof HasIp) {
				SyslogNode node = getNodeByIpAddress(((HasIp) noti).getIpAddress());
				if (node != null)
					EventApi.getApi().checkClear(null, node.getIpAddress(), NmsCode.AlarmCode.UNKNOWN_SYSLOG,
							AoCode.ClearReason.Auto);
			}

		}

	}

	public SyslogParsingResultVo parse(SyslogNode node, SyslogVo vo) throws Exception {

		Logger.logger.trace("{} {}", node, vo);

		List<SyslogPattern> thrList = getPatternList(node);
		if (thrList == null || thrList.size() == 0) {
			return null;
		}

		SyslogPattern.LogStatus logStatus = null;
		SyslogPattern pattern = null;

		String instance = null;

		for (int i = 0, size = thrList.size(); i < size; i++) {
			pattern = thrList.get(i);
			logStatus = pattern.check(vo.getMsg());
			if (logStatus != SyslogPattern.LogStatus.nothing) {
				instance = pattern.getInstance(vo.getMsg());
				if (instance == null && isAllowSameEvent() == false) {
					instance = "No." + System.currentTimeMillis();
				}
				break;
			} else {
				pattern = null;
			}
		}

		if (pattern == null) {
			return null;
		}

		instance = SyslogApi.getApi().remakeInstance(instance);

		SyslogParsingResultVo ret = new SyslogParsingResultVo();
		ret.setInstance(instance);
		ret.setPattern(pattern);
		ret.setStatus(logStatus);

		return ret;
	}

	/**
	 * 등록되지 않은 장비로부터 올라오는 SYSLOG를 처리하는 메소드
	 * 
	 * @param vo
	 */
	public void processUnknownLog(SyslogVo vo) {

		if (saveSyslog) {

			SyslogEventLog syslogEvent = makeSyslogEvent(null, null, vo, //
					// 경보등급
					0, //
						// 경보코드
					0, //
						// 경보명
					"", //
					// 경보번호
					0);

			if (syslogEvent == null) {
				return;
			}

			syslogEvent.setMoInstance("");
			syslogEvent.setIpAddress(vo.getIpAddress());

			insertLog(syslogEvent);
		}

		EventApi.getApi().check(null, vo.getIpAddress(), NmsCode.AlarmCode.UNKNOWN_SYSLOG, "ip:" + vo.getIpAddress(),
				null);

	}

	/**
	 * SYSLOG패턴에서 검출된 인스턴스를 재 정의하는 메소드
	 * 
	 * @param instance 패턴으로 검출된 인스턴스
	 * @return 재정의된 인스턴스
	 */
	public String remakeInstance(String instance) {
		return instance;
	}

//	public void sendEventInvalidNode(SyslogNode node) {
//		EventApi.getApi().check(node, null, NmsCode.AlarmCode.NOT_SET_SYSLOG, null, null);
//	}

	public void setSyslogFolder(String syslogFolder) {
		this.syslogFolder = syslogFolder;
	}

	/**
	 * SYSLOG 내용을 파일로 기록한다.
	 * 
	 * @param node 노드
	 * @param vo   메시지
	 */
	public void writeSyslog2File(SyslogVo vo) {

		if (syslogFolder == null) {
			return;
		}

		File folder = new File(syslogFolder);
		if (folder.exists() == false) {
			folder.mkdirs();
		}

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		Calendar cal = Calendar.getInstance();
		String dateString = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		String filename = BasCfg.getFile(syslogFolder, dateString, vo.getIpAddress() + ".log");

		String msTimeString;
		msTimeString = HHMMSS.format(new Date(vo.getMsTime()));
		msTimeString = msTimeString + "." + String.format("%03d", (int) (vo.getMsTime() % 1000));

		try {
			fileWriter = new FileWriter(new File(filename), true);
			bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(msTimeString + " " + vo.getMsg());
			bufferedWriter.newLine();

			bufferedWriter.flush();
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			try {
				bufferedWriter.close();
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 파일로 기록된 내용을 압축한다.
	 */
	public void zipSyslogFiles() throws Exception {

		if (syslogFolder == null) {
			return;
		}

		File folder = new File(syslogFolder);

		int delCnt = 0, zipCnt = 0;
		int ret;
		int trapLogFileTermDays = getFxPara().getInt(NmsCode.Var.KEEP_DAYS_LOG_SYSLOG, getDays2KeepFile()); // 파일 보관 기간
		int zipAfterDays = getFxPara().getInt(NmsCode.Var.SYSLOG_MAKE_ZIP_AFTER_DAYS, getDays2Zip()); // ZIP으로 적용할 시간
		boolean zipOneFile = getFxPara().getBoolean("zipOneFile", true);

		String delYmd = FxApi.getYmd(System.currentTimeMillis() - (86400000 * trapLogFileTermDays)) + "";
		String zipYmd = FxApi.getYmd(System.currentTimeMillis() - (86400000 * zipAfterDays)) + "";

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
	 * 보관 기관이 지난 자료를 삭제합니다.
	 * 
	 * @throws Exception
	 */
	protected abstract void doDeleteLogExpired() throws Exception;

	/**
	 * 경보가 존재하는 SYSLOG를 기록합니다.
	 * 
	 * @param syslogEvent
	 * @return
	 */
	protected abstract void doInsertLog(List<SyslogEventLog> logList) throws Exception;

	/**
	 * 
	 * @return SYSLOG 관리대상 장비 목록
	 */
	protected abstract List<SyslogNode> doSelectSyslogNode() throws Exception;

	/**
	 * 
	 * @return SYSLOG 경보 조건 목록
	 */
	protected abstract List<SyslogPattern> doSelectSyslogPattern() throws Exception;

	@Override
	protected void initApi() throws Exception {

		saveSyslog = getFxPara().getBoolean("saveSyslog", true);
		broadcastSyslog = getFxPara().getBoolean("broadcastSyslog", false);
		sizeMaxInQueueForEvent = getFxPara().getInt("sizeMaxInQueueForEvent", 100);
		allowSameEvent = getFxPara().getBoolean("allowSameEvent", false);

		syslogFolder = getFxPara().getString("syslog-file-folder");
		if (syslogFolder == null) {
			syslogFolder = FxCfg.getHome() + File.separator + "datas" + File.separator + "syslog";
		} else if (syslogFolder.charAt(0) != '/') {
			syslogFolder = BasCfg.getHome() + File.separator + syslogFolder;
		}

		if (saveSyslog) {
			saver = new BatchSaver<SyslogEventLog>("SyslogSaver" //
					, getFxPara().getInt("insert-batch-size", 30) //
					, syslogFolder + File.separator + "NotSend") {

				@Override
				public void doInsert(List<SyslogEventLog> logList) throws Exception {
					try {
						doInsertLog(logList);
					} catch (Exception e) {
						throw e;
					}
				}

			};
			saver.start();
		}

		adapterList = FxActorParser.getParser().getActorList(SyslogAdapter.class);

	}

	@Override
	protected void reload() {
		loadSyslogPattern();
		loadSyslogNode();
	}

	/**
	 * 
	 * @return SyslogVo 보관 일
	 */
	private int getDays2KeepFile() {
		int days = CoApi.getApi().getVarValue(NmsCode.Var.KEEP_DAYS_LOG_SYSLOG, -1);
		if (days <= 0) {
			days = 30;
			try {
				CoApi.getApi().setVarValue(NmsCode.Var.KEEP_DAYS_LOG_SYSLOG, days, false);
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
		int days = CoApi.getApi().getVarValue(NmsCode.Var.SYSLOG_MAKE_ZIP_AFTER_DAYS, -1);
		if (days <= 0) {
			days = 7;
			try {
				CoApi.getApi().setVarValue(NmsCode.Var.SYSLOG_MAKE_ZIP_AFTER_DAYS, days, false);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}
		return days;
	}

	private void loadSyslogNode() {
		try {
			List<SyslogNode> list = doSelectSyslogNode();

			Map<String, SyslogNode> ipMap = new HashMap<String, SyslogNode>();
			Map<String, SyslogNode> nmMap = new HashMap<String, SyslogNode>();

			for (SyslogNode node : list) {
				ipMap.put(node.getIpAddress(), node);
				if (node.getNodeName() != null) {
					nmMap.put(node.getNodeName(), node);
				}
			}

			nodeMapByName = nmMap;
			nodeMapByIp = ipMap;
			nodeList = list;

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	private void loadSyslogPattern() {
		try {
			List<SyslogPattern> tmpList = doSelectSyslogPattern();

			Collections.sort(tmpList, new Comparator<SyslogPattern>() {
				@Override
				public int compare(SyslogPattern o1, SyslogPattern o2) {
					if (o1.getOrderBy() == o2.getOrderBy()) {
						return o1.getAlarmLevel() - o2.getAlarmLevel();
					}
					return o1.getOrderBy() - o2.getOrderBy();
				}
			});

			patternList = tmpList;

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

}

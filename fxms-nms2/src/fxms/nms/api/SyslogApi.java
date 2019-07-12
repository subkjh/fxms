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
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.HasIp;
import fxms.nms.co.cd.NmsCode;
import fxms.nms.co.syslog.actor.SyslogActor;
import fxms.nms.co.syslog.mo.SyslogNode;
import fxms.nms.co.syslog.vo.SyslogEventLog;
import fxms.nms.co.syslog.vo.SyslogEventLogList;
import fxms.nms.co.syslog.vo.SyslogThr;
import fxms.nms.co.syslog.vo.SyslogVo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

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
	/** 등록되지 않은 IP에서 올라온 SYSLOG를 기록할지 여부 */
	private boolean acceptNotRegIp = false;
	/** FilterSyslog 목록 */
	private List<SyslogActor> actorList;
	/** Syslog 관리 장비 목록. key=ip주소 */
	private Map<String, SyslogNode> nodeMapByIp;
	/** Syslog 관리 장비 목록. key=명칭 */
	private Map<String, SyslogNode> nodeMapByName;
	/** hostname으로 찾기 위한 Map */
	private Map<String, SyslogNode> nodeMapBySysName;
	/** 큐 내에 처리할 내용이 아래 이상인 경우 이벤트를 발생합니다. */
	private int sizeMaxInQueueForEvent = 100;
	/** SyslogVo 기록 여부 */
	private boolean writeToFile = false;
	/** 동일한 경보에 대해서 한 번만 발생할 것인지 계속발생시킬것인지 */
	private boolean allowSameEvent;
	private NotiSender syslogSender;
	private BatchSaver<SyslogEventLog> saver;
	private List<SyslogThr> thrList = null;
	private List<SyslogNode> nodeList = null;

	private String syslogFile;

	public SyslogApi() {
		nodeMapByIp = new HashMap<String, SyslogNode>();
		nodeMapByName = new HashMap<String, SyslogNode>();
		nodeMapBySysName = new HashMap<String, SyslogNode>();
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

	public List<SyslogActor> getActorList() {
		return actorList;
	}

	/**
	 * 
	 * @return SyslogVo 보관 일
	 */
	public int getDays2KeepFile() {
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
	public int getDays2Zip() {
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

		if (node == null && vo.getHostname() != null) {
			node = nodeMapBySysName.get(vo.getHostname());
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

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getName());
		sb.append("ACTOR-SIZE(" + actorList.size() + ")");
		sb.append("NODE-SIZE(" + nodeList.size() + ")");
		sb.append("SYSLOG-THR-SIZE(" + thrList.size() + ")");

		return sb.toString();
	}

	public String getSyslogFile() {
		return syslogFile;
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

	/**
	 * 노드에 적용할 임계 조건을 검색합니다.
	 * 
	 * @param node 노드
	 * @return 적용할 임계 조건<br>
	 *         해당 사항이 없을 경우 size=0인 리스트 제공
	 */
	public List<SyslogThr> getThresholdList(SyslogNode node) {

		List<SyslogThr> ret = new ArrayList<SyslogThr>();

		for (SyslogThr th : thrList) {
			if (th.getModelNo() == 0 || th.getModelNo() == node.getModelNo()) {
				ret.add(th);
			}
		}

		return ret;
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
	public SyslogEventLog makeSyslogEvent(Mo mo, SyslogNode node, SyslogVo raw, int alarmLevel, int alarmCode,
			String alarmName, long alarmNo) {
		SyslogEventLog item = new SyslogEventLog();

		item.setAlarmCode(alarmCode);
		item.setAlarmName(alarmName);
		item.setAlarmLevel(alarmLevel);
		item.setSyslogPriority(raw.getSeverity());
		item.setSyslogFacility(raw.getFacility());
		item.setHstimeRecv(FxApi.getDate(raw.getMsTime()));
		item.setLogMsg(raw.getMsg());

		if (node != null) {
			item.setIpAddress(node.getIpAddress());
			item.setModelNo(node.getModelNo());
			item.setModelName(node.getModelName());
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
				loadSyslogThr();
			} else if (r.contains(ReloadSignal.RELOAD_TYPE_MO)) {
				loadSyslogNode();
			} else if (r.contains(ReloadSignal.RELOAD_TYPE_ALL)) {
				loadSyslogThr();
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

	public void sendEventInvalidNode(SyslogNode node) {
		EventApi.getApi().check(node, null, NmsCode.AlarmCode.NOT_SET_SYSLOG, null, null);
	}

	/**
	 * SYSLOG 내용을 파일로 기록한다.
	 * 
	 * @param node 노드
	 * @param vo   메시지
	 */
	public void writeSyslog2File(SyslogNode node, SyslogVo vo) {

		if (writeToFile == false) {
			return;
		}

		if (node == null && acceptNotRegIp == false) {
			return;
		}

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		Calendar cal = Calendar.getInstance();
		String dateString = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
		String filename = BasCfg.getFile(BasCfg.getHomeDatas(), "syslog", dateString, vo.getIpAddress() + ".log");

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
	protected abstract List<SyslogThr> doSelectSyslogThr() throws Exception;

	@Override
	protected void initApi() throws Exception {

		saveSyslog = getFxPara().getBoolean("saveSyslog", true);
		broadcastSyslog = getFxPara().getBoolean("broadcastSyslog", false);
		acceptNotRegIp = getFxPara().getBoolean("acceptNotRegIp", false);
		sizeMaxInQueueForEvent = getFxPara().getInt("sizeMaxInQueueForEvent", 100);
		writeToFile = getFxPara().getBoolean("writeToFile", false);
		allowSameEvent = getFxPara().getBoolean("allowSameEvent", false);

		syslogFile = getFxPara().getString("syslog-file-folder");
		if (syslogFile.charAt(0) != '/')
			syslogFile = BasCfg.getHome() + File.separator + syslogFile;

		if (saveSyslog) {
			saver = new BatchSaver<SyslogEventLog>("SyslogSaver" //
					, getFxPara().getInt("insert-batch-size", 30) //
					, syslogFile + File.separator + "NotSend") {

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

		actorList = FxActorParser.getParser().getActorList(SyslogActor.class);

	}

	@Override
	protected void reload() {
		loadSyslogThr();
		loadSyslogNode();
	}

	private void loadSyslogNode() {
		try {
			List<SyslogNode> list = doSelectSyslogNode();

			Map<String, SyslogNode> ipMap = new HashMap<String, SyslogNode>();
			Map<String, SyslogNode> nmMap = new HashMap<String, SyslogNode>();
			Map<String, SyslogNode> sysMap = new HashMap<String, SyslogNode>();

			for (SyslogNode node : list) {
				ipMap.put(node.getIpAddress(), node);
				nmMap.put(node.getMoName(), node);
				if (node.getSysName() != null && node.getSysName().trim().length() > 0) {
					sysMap.put(node.getSysName(), node);
				}
			}

			nodeMapByName = nmMap;
			nodeMapByIp = ipMap;
			nodeMapBySysName = sysMap;
			nodeList = list;

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	private void loadSyslogThr() {
		try {
			List<SyslogThr> tmpList = doSelectSyslogThr();

			Collections.sort(tmpList, new Comparator<SyslogThr>() {
				@Override
				public int compare(SyslogThr o1, SyslogThr o2) {
					if (o1.getOrderBy() == o2.getOrderBy()) {
						return o1.getAlarmLevel() - o2.getAlarmLevel();
					}
					return o1.getOrderBy() - o2.getOrderBy();
				}
			});

			thrList = tmpList;

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}
}

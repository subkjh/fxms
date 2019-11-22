package fxms.nms.co.syslog.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.co.noti.FxEventImpl;
import fxms.nms.mo.property.Modelable;

/**
 * SYSLOG에 대해서 경보를 발생시키는 조건
 * 
 * @author 김종훈
 * 
 */
public abstract class SyslogPattern extends FxEventImpl implements Modelable {

	/**
	 * * 등록 패턴 경보 발생 로직<br>
	 * Trigger ifDesc 경보 해제 <br>
	 * <br>
	 * O X X X 무조건 중복 발생<br>
	 * O O X X 무조건 중복 발생<br>
	 * O O O X 무조건 중복 발생<br>
	 * O X O X 무조건 중복 발생<br>
	 * O X O O 무조건 중복 발생, 해제 경우 동일 경보 전부 해제<br>
	 * O O O O 1개 발생, 따라서 1개 해제<br>
	 * 
	 * @author subkjh
	 * 
	 */
	public enum LogStatus {

		/** 모두 해제 */
		// 경보 해제
		clearAll,

		/** 하나만 해제 */
		clearOne,

		/** 해당 사항 없음 */
		nothing,

		/** 이력에는 남기지 않음 */
		notTrigger,

		/** 무조건 발생 */
		occur,

		/** 경보가 존재하지 않으면 발생 */
		occurIfNotExist;
	}

	private static final String PATTERN = ";|&|\\|";

	/**
	 * 
	 */
	private static final long serialVersionUID = -5570708961043409779L;

	// private static final SimpleDateFormat YYYYMMDDHHMMSS = new
	// SimpleDateFormat("yyyyMMddHHmmss");

	/** 경보코드 */
	private int alarmCode;
	/** 경보코드(실제 사용할 */
	private int alarmCodeUse;
	/** 경보그룹 */
	private String alarmGroup;
	/** 경보등급 */
	private int alarmLevel;
	private String alarmName;
	private String clrPatternStr;
	/** 해제이벤트패턴 */
	protected String clrPatternStrArray[];
	/** 경보여부 */
	private boolean fault;
	protected boolean isClrAnd;
	protected boolean isOcrAnd;
	private String ocrPatternStr;
	/** 발생이벤트패턴 */
	protected String ocrPatternStrArray[];
	/** 적용순서 */
	private int orderBy;
	/** 자동해제시간(초) - 0이거나 작으면 경우 자동 해제하지 않습니다. */
	private int secRelease;
	/** 수정일시(최종) */
	private long hstimeChg;
	/** 키를 찾을 기준점 */
	private String instanceBaseStr;
	/** 기준점 기준 시작 위치 (항목수) */
	private int instanceStart;
	/** 항목수 */
	private int instanceLength;
	/** 사용여부 */
	private boolean enable;
	/** 경보조치번호. 이 조건으로 이벤트가 발생되면 처리할 내용 */
	private String treatName;

	public SyslogPattern() {
		ocrPatternStrArray = null;
		clrPatternStrArray = null;
		enable = true;
	}

	/**
	 * PK
	 * 
	 * @param alarmCode 경보코드
	 */
	public SyslogPattern(int alarmCode) {
		this();
		this.alarmCode = alarmCode;
	}

	public SyslogPattern(int alarmCode, String alarmName, int alarmLevel, String triggerStr, String ocrPatternStr,
			String clrPatternStr, int secRelease) {

		setAlarmCode(alarmCode);
		setAlarmGroup("syslog");
		setAlarmLevel(alarmLevel);
		setAlarmName(alarmName);
		setClrPatternStr(clrPatternStr);
		if (triggerStr != null && triggerStr.trim().length() > 0) {
			setOcrPatternStr(triggerStr);
			setFault(false);
		} else {
			setOcrPatternStr(ocrPatternStr);
			setFault(true);
		}

		setSecRelease(secRelease);
		setEnable(true);
	}

	/**
	 * UK
	 * 
	 * @param alarmName 경보명
	 */
	public SyslogPattern(String alarmName) {
		this();
		this.alarmName = alarmName;
	}

	/**
	 * 문자열을 검사하여 상태를 제공합니다.
	 * 
	 * @param syslog 비교 문자열
	 * @return 상태
	 */
	public LogStatus check(String syslog) {

		// targetString 을 체크하여 Logstaus 나옴

		if (isMatch(syslog, clrPatternStrArray, isClrAnd)) {
			return LogStatus.clearAll;
		}

		// isDefined(ifDescrPatternStr)하여 참이면 LogStatus.clearOne, 아니면
		// LogStatus.clearAll
		// ifDescrPatternStr가 있으면 clearOne, 없으면 clearAll이다.

		// targetStr과 ocrPatternStr[]을 비교하여 ocrPatternStr[]에 targetStr가 있을
		// 경우 아래 return 실행
		if (isMatch(syslog, ocrPatternStrArray, isOcrAnd)) {
			if (isFault()) {
				return LogStatus.occur;
			} else {
				return LogStatus.notTrigger;
			}
		}

		return LogStatus.nothing;
	}

	public int getAlarmCode() {
		return alarmCode;
	}

	public int getAlarmCodeUse() {
		return alarmCodeUse;
	}

	public String getAlarmGroup() {
		return alarmGroup;
	}

	public int getAlarmLevel() {
		return alarmLevel;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public String getClrPatternStr() {
		return clrPatternStr;
	}

	public long getHstimeChg() {
		return hstimeChg;
	}

	/**
	 * 
	 * @param syslog
	 * @return 키 제공
	 */
	public String getInstance(String syslog) {

		if (instanceBaseStr == null || instanceBaseStr.trim().length() == 0 || instanceLength == 0)
			return null;

		int pos = syslog.indexOf(instanceBaseStr);
		if (pos < 0)
			return null;

		try {
			String instance = "";
			List<String> itemList;
			if (instanceStart < 0) {
				itemList = split(syslog.substring(0, pos));
				for (int i = 0; i < instanceLength; i++) {
					instance += itemList.get(itemList.size() + instanceStart + i) + " ";
				}
			} else {
				itemList = split(syslog.substring(pos));
				for (int i = 0; i < instanceLength; i++) {
					instance += itemList.get(instanceStart + i) + " ";
				}
			}
			return instance.trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getInstanceBaseStr() {
		return instanceBaseStr;
	}

	public int getInstanceLength() {
		return instanceLength;
	}

	public int getInstanceStart() {
		return instanceStart;
	}

	public String getOcrPatternStr() {
		return ocrPatternStr;
	}

	public int getOrderBy() {
		return orderBy;
	}

	public int getSecRelease() {
		return secRelease;
	}

	public String getTreatName() {
		return treatName;
	}

	public boolean isEnable() {
		return enable;
	}

	public boolean isFault() {
		return fault;
	}

	/**
	 * @method isMatch
	 * @method baseStr[]의 문자열을 순차적으로 targetStr과 비교한다.
	 * @param triggerStrArray
	 * 
	 * @return
	 */
	public boolean isMatch(String targetStr, String baseStr[], boolean isAnd) {

		if (baseStr == null || baseStr.length == 0)
			return false;

		if (isAnd) {
			for (String str : baseStr) {
				if (targetStr.indexOf(str) < 0)
					return false;
			}
			return true;
		} else {
			for (String str : baseStr) {
				if (targetStr.indexOf(str) >= 0)
					return true;
			}
		}
		return false;
	}

	public void setAlarmCode(int alarmCode) {
		this.alarmCode = alarmCode;
	}

	public void setAlarmCodeUse(int alarmCodeUse) {
		this.alarmCodeUse = alarmCodeUse;
	}

	public void setAlarmGroup(String alarmGroup) {
		this.alarmGroup = alarmGroup;
	}

	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public void setClrPatternStr(String _clrPatternStr) {
		if (_clrPatternStr == null)
			_clrPatternStr = "";

		clrPatternStr = _clrPatternStr.trim();

		if (clrPatternStr != null && clrPatternStr.length() > 0) {
			this.clrPatternStrArray = clrPatternStr.split(PATTERN);
			isClrAnd = clrPatternStr.indexOf('&') > 0;
		}
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setFault(boolean fault) {
		this.fault = fault;
	}

	public void setHstimeChg(long hstimeChg) {
		this.hstimeChg = hstimeChg;
	}

	public void setInstanceBaseStr(String instanceBaseStr) {
		this.instanceBaseStr = instanceBaseStr;
	}

	public void setInstanceLength(int instanceLength) {
		this.instanceLength = instanceLength;
	}

	public void setInstanceStart(int instanceStart) {
		this.instanceStart = instanceStart;
	}

	public void setOcrPatternStr(String _ocrPatternStr) {

		if (_ocrPatternStr == null)
			_ocrPatternStr = "";

		ocrPatternStr = _ocrPatternStr.trim();

		if (ocrPatternStr != null && ocrPatternStr.length() > 0) {
			ocrPatternStrArray = ocrPatternStr.split(PATTERN);
			isOcrAnd = ocrPatternStr.indexOf('&') > 0;
		}
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public void setSecRelease(int secRelease) {
		this.secRelease = secRelease;
	}

	public void setTreatName(String treatName) {
		this.treatName = treatName;
	}

	@Override
	public String toString() {
		return "SYSLOG-PATTERN(code=" + alarmCode + ",name=" + alarmName + ")";
	}

	/**
	 * @method isClearDefined
	 * @method clrPatternStr의 값이 null이 아니거나 길이가 0이 아닐경우 true를 반환한다.
	 * @param
	 * 
	 * @return
	 */
	// clrPatternStr 일 경우 true
	protected boolean isClearDefined() {
		return clrPatternStrArray != null && clrPatternStrArray.length > 0 && clrPatternStrArray[0].trim().length() > 0;
	}

	private List<String> split(String s) {
		List<String> strList = new ArrayList<String>();
		String item = "";
		char chArray[] = s.toCharArray();
		char ch;
		char separatorCh[] = new char[] { ' ', '\t', '\n', '\r' };

		AAA: for (int i = 0; i < chArray.length; i++) {
			ch = chArray[i];
			for (char c : separatorCh) {
				if (c == ch) {
					if (item.length() > 0)
						strList.add(item);
					item = "";
					continue AAA;
				}
			}

			item += ch;
		}

		if (item.length() > 0)
			strList.add(item);

		return strList;
	}

}

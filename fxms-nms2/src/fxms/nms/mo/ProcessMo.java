package fxms.nms.mo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.impl.mo.FxMo;
import fxms.bas.mo.Mo;

public class ProcessMo extends FxMo {

	/** MO분류. PROCESS */
	public static final String MO_CLASS = "PROCESS";

	/** 0 */
	public static final byte STATUS_RUN_OFF = 0;
	/** 1 */
	public static final byte STATUS_RUN_ON = 1;
	/** 2 */
	public static final byte STATUS_RUN_RESTART = 2;
	/** -1 */
	public static final byte STATUS_RUN_UNKNOWN = -1;
	/** 3 */
	public static final byte STATUS_RUN_SOME_CHANGE = 3;

	/** */
	private static final long serialVersionUID = -1605910616518180037L;

	public static String makeMoName(int pid, String swRunName) {
		return pid + ":" + swRunName;
	}

	/**
	 * 
	 * @param mo
	 *            채울 관리대상
	 * @param name
	 *            관리대상명
	 * @param swRunName
	 *            실행명
	 * @param swRunPath
	 *            경로명
	 * @param swRunParameters
	 *            인수내역
	 * @param pid
	 *            프로세스 ID
	 * @return 채워진 관리대상
	 */
	public static ProcessMo set(ProcessMo mo, String name, String swRunName, String swRunPath, String swRunParameters,
			int pid) {

		mo.setAlarmCfgNo(0);
		mo.setMngYn(true);
		mo.setMoAname(name);

		// MO명을 키로 합니다.
		mo.setMoName(makeMoName(pid, swRunName));

		mo.setSwRunName(swRunName);
		mo.setSwRunParameters(swRunParameters);
		mo.setSwRunPath(swRunPath);
		mo.setPid(pid);
		mo.setStatusRun(ProcessMo.STATUS_UNKNOWN);

		return mo;
	}

	/** 명령어(상태확인) */
	private String cmdCheck;
	/** 명령어(실행) */
	private String cmdRun;
	/** 명령어(종료) */
	private String cmdStop;
	/** PID */
	private int pid = -1;
	/** 상태 */
	@SuppressWarnings("unused")
	private int statusRun;
	/** 실행화일명 */
	private String swRunName;
	/** 실행파라메터 */
	private String swRunParameters;
	/** 실행경로 */
	private String swRunPath;
	/** 운용자그룹번호(RO그룹) */
	private int userGroupNo;
	/** 운용자번호(소유자) */
	private int userNo;

	/** CPU 점유 시간 Micro Seconds */
	private long cpuTime = -1;
	/** 할당된 메모리(KBytes) */
	private long memUsed = -1;

	// /** 동일 프로세스 수 */
	// private int countProc = 1;

	private List<Integer> pidList;
	private int pidArr[];

	public ProcessMo() {
		pidArr = null;
	}

	public void addPid(int pid) {
		if (pidList == null) {
			pidList = new ArrayList<Integer>();
		}

		pidList.add(pid);
	}

	public boolean containsPid(int pid) {
		return pidList == null ? false : pidList.contains(pid);
	}

	public String getCmdCheck() {
		return cmdCheck;
	}

	public String getCmdRun() {
		return cmdRun;
	}

	public String getCmdStop() {
		return cmdStop;
	}

	public int getCountProc() {
		return pidList == null ? 1 : pidList.size();
	}

	public long getCpuTime() {
		return cpuTime;
	}

	public long getMemUsed() {
		return memUsed;
	}

	@Override
	public String getMoClass() {
		return MO_CLASS;
	}

	public int getPid() {
		return pid;
	}

	public List<Integer> getPidList() {
		return pidList;
	}

	public int getStatusRun() {

		if (pidList == null || pidList.size() == 0)
			return STATUS_RUN_OFF;
		if (pidArr == null)
			return STATUS_RUN_ON;

		int diffCount = 0;
		if (pidArr.length == pidList.size()) {
			for (int i = 0; i < pidArr.length; i++) {
				if (pidArr[i] != pidList.get(i))
					diffCount++;
			}
		}

		if (diffCount > 0) {
			return pidArr.length == diffCount ? STATUS_RUN_RESTART : STATUS_RUN_SOME_CHANGE;
		}

		return STATUS_RUN_ON;
	}

	public String getSwRunName() {
		return swRunName;
	}

	public String getSwRunParameters() {
		return swRunParameters;
	}

	public String getSwRunPath() {
		return swRunPath;
	}

	public int getUserGroupNo() {
		return userGroupNo;
	}

	public int getUserNo() {
		return userNo;
	}

	public void initPolling() {
		if (pidList != null) {
			pidArr = new int[pidList.size()];
			int index = 0;
			for (Integer pid : pidList) {
				pidArr[index++] = pid;
			}

			pidList.clear();
		}

		cpuTime = -1;
		memUsed = -1;
	}

	/**
	 * 현재, 이전 PID가 모두 존재하고 다른 경우에 한하여 true를 제공합니다.
	 * 
	 * @return 재시작여부
	 */
	public boolean isDiffPid(int pidNow) {
		return pid > 0 && pidNow > 0 && pidNow != pid;
	}

	@Override
	public boolean equalMo(Mo mo) {
		if (mo instanceof ProcessMo) {
			ProcessMo proc = (ProcessMo) mo;
			return matchStr(swRunName, proc.swRunName) //
					&& matchStr(swRunPath, proc.swRunPath)//
					&& matchStr(swRunParameters, proc.swRunParameters);
		} else {
			return super.equalMo(mo);
		}
	}

	/**
	 * 매칭여부를 판단합니다.
	 * 
	 * @param _swRunPath
	 *            경로
	 * @param _swRunParameters
	 *            파라메터
	 * @return 매칭여부
	 */
	public boolean match(String _swRunPath, String _swRunParameters) {
		return matchStr(swRunPath, _swRunPath) && matchStr(swRunParameters, _swRunParameters);
	}

	/**
	 * 매칭여부를 판단합니다.
	 * 
	 * @param _swRunName
	 *            화일명
	 * @param _swRunPath
	 *            경로
	 * @param _swRunParameters
	 *            파라메터
	 * @return 매칭여부
	 */
	public boolean match(String _swRunName, String _swRunPath, String _swRunParameters) {
		return matchStr(swRunName, _swRunName) //
				&& matchStr(swRunPath, _swRunPath)//
				&& matchStr(swRunParameters, _swRunParameters);
	}

	public void setCmdCheck(String cmdCheck) {
		this.cmdCheck = cmdCheck;
	}

	public void setCmdRun(String cmdRun) {
		this.cmdRun = cmdRun;
	}

	public void setCmdStop(String cmdStop) {
		this.cmdStop = cmdStop;
	}

	/**
	 * 
	 * @param cpuTime
	 *            CPU 점유 시간 Micro Seconds
	 */
	public void setCpuTime(long cpuTime) {
		this.cpuTime = cpuTime;
	}

	/**
	 * 
	 * @param memUsed
	 *            할당된 메모리(KBytes)
	 */
	public void setMemUsed(long memUsed) {
		this.memUsed = memUsed;
	}

	/**
	 * 새로운 PID를 설정합니다.
	 * 
	 * @param pid
	 */
	public void setPid(int pid) {
		this.pid = pid;
	}

	public void setStatusRun(int statusRun) {
		this.statusRun = statusRun;
	}

	public void setSwRunName(String swRunName) {
		this.swRunName = swRunName;
	}

	public void setSwRunParameters(String swRunParameters) {
		this.swRunParameters = swRunParameters;
	}

	public void setSwRunPath(String swRunPath) {
		this.swRunPath = swRunPath;
	}

	public void setUserGroupNo(int userGroupNo) {
		this.userGroupNo = userGroupNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	private boolean matchStr(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		if (s1 == null || s2 == null)
			return false;
		return s1.trim().equals(s2.trim());
	}

}
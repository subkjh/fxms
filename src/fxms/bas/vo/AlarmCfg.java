package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.mo.Moable;

public class AlarmCfg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8393109399326673206L;

	/** 알람조건이 설정되지 않았음을 의미함 */
	public static final int UPPER_ALARM_CFG = 0;
	/** 알람을 발생하지 않는 알람조건번호 */
//	public static final int NO_ALARM_CFG = 1;

	private final List<AlarmCfgMem> memList;

	private final int alarmCfgNo;

	private final String alarmCfgName;

	private final String moClass;

	private final String moType;

	private final boolean basicCfgYn;

	public boolean isBasicCfgYn() {
		return basicCfgYn;
	}

	public AlarmCfg(int alarmCfgNo, String alarmCfgName, String moClass, String moType, String basicCfgYn) {
		this.alarmCfgNo = alarmCfgNo;
		this.alarmCfgName = alarmCfgName;
		this.moClass = moClass;
		this.moType = moType;
		this.basicCfgYn = "y".equalsIgnoreCase(basicCfgYn);
		this.memList = new ArrayList<>();
	}

	/**
	 * 
	 * @param entry 추가할 경보 조건
	 */
	public void add(AlarmCfgMem member) {
		memList.add(member);
	}

	public String getAlarmCfgName() {
		return alarmCfgName;
	}

	public int getAlarmCfgNo() {
		return alarmCfgNo;
	}

	/**
	 * 성능번호에 해당되는 경보코드 목록을 제공합니다.
	 * 
	 * @param psId 성능ID
	 * @return 경보코드목록
	 */
	public List<Integer> getAlcdNo(String psId) {
		if (psId == null)
			return null;

		List<Integer> ret = new ArrayList<Integer>();
		for (AlarmCfgMem entry : memList) {

			if (entry.getAlarmCode() == null) {
				continue;
			}

			if (psId.equals(entry.getAlarmCode().getPsId())) {
				if (ret.contains(entry.getAlcdNo()) == false)
					ret.add(entry.getAlcdNo());
			}
		}
		return ret;
	}

	/**
	 * 조건과 일치하는 경보조건을 제공합니다.
	 * 
	 * @param psId     성능항목
	 * @param alcdNo   알람코드번호
	 * @param valuePre 이전값
	 * @param valueCur 현재값
	 * @param mo       관리대상
	 * @return
	 */
	public AlarmCfgMemMatched getMatchMember(String psId, int alcdNo, Number valuePre, Number valueCur, Moable mo) {

		AlarmCfgMemMatched ret;
		for (AlarmCfgMem entry : memList) {

			if (entry.isValid(mo) == false) {
				continue;
			}

			if (entry.isMatch(alcdNo, psId) == false) {
				continue;
			}

			if (entry.getRepeatTimes() > 0) {
				ret = entry.match(valuePre, valueCur);
				if (ret != null) {
					return ret;
				}

			}

		}

		return null;
	}

	/**
	 * 경보코드에 해당되는 경보조건을 찾아줍니다.
	 * 
	 * @param alarmCode 경보코드
	 * @return 경보조건
	 */
	public AlarmCfgMem getMem4AlcdNo(int alcdNo) {

		for (AlarmCfgMem e : memList) {
			if (e.getAlcdNo() == alcdNo) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 성능에 해당되는 알람조건을 가져온다.
	 * 
	 * @param psId
	 * @return
	 */
	public List<AlarmCfgMem> getMemList(String psId) {

		List<AlarmCfgMem> ret = new ArrayList<>();

		for (AlarmCfgMem e : memList) {
			if (psId.equals(e.getAlarmCode().getPsId())) {
				ret.add(e);
			}
		}
		return ret;
	}

	/**
	 * @return the list
	 */
	public List<AlarmCfgMem> getMemList() {
		return memList;
	}

	/**
	 * 
	 * @return 비교 조건의 수
	 */
	public int getMemSize() {
		return memList.size();
	}

	public String getMoClass() {
		return moClass;
	}

	public String getMoType() {
		return moType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("AlarmCfg[");
		sb.append("no(" + alarmCfgNo + ")");
		sb.append("name(" + alarmCfgName + ")");
		sb.append("size(" + getMemSize() + ")");
		sb.append("]");
		return sb.toString();
	}

}

package fxms.bas.alarm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fxms.bas.dbo.ao.FX_AL_CFG;
import fxms.bas.mo.Mo;

public class AlarmCfg extends FX_AL_CFG {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8393109399326673206L;

	private List<AlarmCfgMem> memList;

	public static final int NO_ALARM_CFG = 1;

	public AlarmCfg() {

	}

	/**
	 * 우선 비교할 경보등급으로 정렬한다.
	 */
	public void sortMember() {

		if (memList == null || memList.size() == 0) {
			return;
		}

		Collections.sort(memList, new Comparator<AlarmCfgMem>() {

			@Override
			public int compare(AlarmCfgMem o1, AlarmCfgMem o2) {

				return o1.getAlarmLevel() - o2.getAlarmLevel();
			}
		});
	}

	/**
	 * 
	 * @param entry
	 *            추가할 경보 조건
	 */
	public void add(AlarmCfgMem member) {
		if (memList == null)
			memList = new ArrayList<AlarmCfgMem>();
		memList.add(member);
	}

	/**
	 * 성능번호에 해당되는 경보코드 목록을 제공합니다.
	 * 
	 * @param perfNo
	 *            성능번호
	 * @return 경보코드목록
	 */
	public List<Integer> getAlcdNo(String psCode) {
		if (memList == null || memList.size() == 0 || psCode == null)
			return null;

		List<Integer> ret = new ArrayList<Integer>();
		for (AlarmCfgMem entry : memList) {

			if (entry.getAlarmCode() == null) {
				continue;
			}

			if (psCode.equals(entry.getAlarmCode().getPsCode())) {
				if (ret.contains(entry.getAlcdNo()) == false)
					ret.add(entry.getAlcdNo());
			}
		}
		return ret;
	}

	@Override
	public void setAlarmCfgNo(int alarmCfgNo) {

		super.setAlarmCfgNo(alarmCfgNo);

		if (memList != null) {
			for (AlarmCfgMem entry : memList) {
				entry.setAlarmCfgNo(alarmCfgNo);
			}
		}
	}

	/**
	 * 조건과 일치하는 경보조건을 제공합니다.
	 * 
	 * @param perfNo
	 *            성능번호
	 * @param alarmCode
	 *            경보코트
	 * @param valuePre
	 *            이전값
	 * @param valueCur
	 *            현재값
	 * @param hstime
	 *            수집일시
	 * @param dpPerfNo
	 *            연관성능번호
	 * @param dpValue
	 *            연관성능값
	 * @return 경보조건설정내역
	 */
	public AlarmCfgMem getMatchMember(String psCode, int alcdNo, Number valuePre, Number valueCur, long hstime,
			int dpPerfNo, double dpValue, Mo mo) {

		if (memList == null || memList.size() == 0)
			return null;

		for (AlarmCfgMem entry : memList) {

			if (entry.isValid(mo) == false) {
				continue;
			}

			if (entry.isMatch(alcdNo, psCode) == false) {
				continue;
			}

			// if (entry.mathMin(dpPerfNo, dpValue) == false)
			// continue;

			if (entry.getRepeatTimes() > 0) {

				if (entry.match(valuePre, valueCur)) {
					return entry;
				}

			}

		}

		return null;
	}


	/**
	 * 경보코드에 해당되는 경보조건을 찾아줍니다.
	 * 
	 * @param alarmCode
	 *            경보코드
	 * @return 경보조건
	 */
	public AlarmCfgMem getMem4AlcdNo(int alcdNo) {
		if (memList == null || memList.size() == 0)
			return null;

		for (AlarmCfgMem e : memList) {
			if (e.getAlcdNo() == alcdNo) {
				return e;
			}
		}
		return null;
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
		return memList == null ? 0 : memList.size();
	}

	public void setMemList(List<AlarmCfgMem> memList) {
		this.memList = memList;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("AlarmCfg[");
		sb.append("no(" + getAlarmCfgNo() + ")");
		sb.append("name(" + getAlarmCfgName() + ")");
		sb.append("size(" + getMemSize() + ")");
		sb.append("]");
		return sb.toString();
	}

}

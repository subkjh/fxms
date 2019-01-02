package fxms.bas.alarm;

import java.io.Serializable;

import fxms.bas.alarm.dbo.ClearAlarmDbo;
import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.Mo;

public abstract class AlarmFilter extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4609731135541142883L;

	/**
	 * 저장소에 기록된 후에 호출되는 메소드입니다.
	 * 
	 * @param alarm
	 *            생성된 경보
	 */
	public abstract void filter(OccurAlarmDbo alarm);
	
	public abstract void filter(ClearAlarmDbo alarm);

	/**
	 * 저장소에 기록되기 전에 호출되는 메소드입니다.<br>
	 * 아무 행위가 없을 경우 입력된 경보를 그래로 리턴해야 합니다.<br>
	 * 리턴이 null인 경우 이 경보는 버려집니다.
	 * 
	 * @param event
	 *            이벤트
	 * @param alarm
	 *            경보
	 * @param node
	 *            경보가 발생된 노드
	 * @return 새롭게 생성된 경보<br>
	 */
	public abstract OccurAlarmDbo filter(AlarmEvent event, OccurAlarmDbo alarm, Mo mo);
}

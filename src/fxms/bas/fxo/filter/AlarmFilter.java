package fxms.bas.fxo.filter;

import java.io.Serializable;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.mo.Mo;
import fxms.bas.vo.AlarmOccurEvent;
import fxms.bas.vo.OccurAlarm;

/**
 * 발생 또는 해제된 알람을 처리하는 필터
 * 
 * @author subkjh
 *
 */
public abstract class AlarmFilter extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4609731135541142883L;

	/**
	 * 저장소에 기록되기 전에 호출되는 메소드입니다.<br>
	 * 아무 행위가 없을 경우 입력된 경보를 그래로 리턴해야 합니다.<br>
	 * 리턴이 null인 경우 이 경보는 버려집니다.
	 * 
	 * @param event 이벤트
	 * @param alarm 경보
	 * @param node  경보가 발생된 노드
	 * @return 새롭게 생성된 경보<br>
	 */
	public abstract OccurAlarm filter(AlarmOccurEvent event, OccurAlarm alarm, Mo mo);

}

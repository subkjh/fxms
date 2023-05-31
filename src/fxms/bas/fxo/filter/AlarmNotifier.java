package fxms.bas.fxo.filter;

import java.io.Serializable;

import fxms.bas.fxo.FxActorImpl;
import fxms.bas.vo.Alarm;

public abstract class AlarmNotifier extends FxActorImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5695248441361271537L;

	/**
	 * 저장소에 기록된 후에 호출되는 메소드입니다.
	 * 
	 * @param alarm 생성된 알람
	 */
	public abstract void notify(Alarm alarm);

}

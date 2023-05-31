package fxms.bas.fxo.adapter;

import java.io.Serializable;
import java.util.List;

import fxms.bas.co.AmGroupVo;
import fxms.bas.co.AmHstVo;
import fxms.bas.vo.ClearAlarm;
import fxms.bas.vo.OccurAlarm;

/**
 * 알람 발생 후 조치할 ADAPTER
 * 
 * @author subkjh
 *
 */
public abstract class AlarmAfterAdapter extends FxAdapterImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6457460666196483945L;

	public abstract List<AmHstVo> followUp(OccurAlarm alarm, List<AmGroupVo> amGroupList) throws Exception;

	public abstract List<AmHstVo> followUp(ClearAlarm alarm, List<AmGroupVo> amGroupList) throws Exception;
	
	public abstract String getFpactCd(); 

}

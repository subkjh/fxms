package fxms.nms.co.snmp.trap.adapter;

import fxms.bas.fxo.FxActor;
import fxms.nms.co.snmp.trap.TrapNode;
import fxms.nms.co.snmp.trap.vo.TrapVo;

/**
 * 받은 트랩을 분석하는 필터
 * 
 * @author subkjh(김종훈)
 *
 */
public interface TrapAdapter extends FxActor {

	/**
	 * 
	 * @param node
	 * @param trapEvent
	 * @return null인 경우 더 이상 필터를 타지 않는다.
	 */
	public TrapVo parse(TrapNode node, TrapVo trap);
}

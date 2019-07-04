package fxms.nms.co.snmp.trap.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import fxms.bas.ao.AoCode.ClearReason;
import fxms.bas.ao.vo.Alarm;
import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxActorImpl;
import fxms.nms.api.TrapApi;
import fxms.nms.co.snmp.mo.TrapNode;
import fxms.nms.co.snmp.trap.vo.TrapThr;
import fxms.nms.co.snmp.trap.vo.TrapVo;
import fxms.nms.co.snmp.vo.OidValue;

/**
 * 정의된 트랩 임계 조건으로 경보를 판단한다.
 * 
 * @author subkjh(김종훈)
 *
 */
public class DefThrTrapActor extends FxActorImpl implements TrapActor {

	@Override
	public TrapVo parse(TrapNode node, TrapVo vo) {

		Logger.logger.trace("{} {}", node, vo);

		if (node == null)
			return vo;

		List<TrapThr> thrList = TrapApi.getApi().getThrByNode(node);
		if (thrList == null || thrList.size() == 0) {
			return null;
		}

		boolean matchTrapOid;
		boolean matchVarOid;
		List<OidValue> oidValueList = new ArrayList<OidValue>();
		Alarm alarm;

		if (vo.getList() == null || vo.getList().size() == 0) {
			return vo;
		}

		for (OidValue ov : vo.getList()) {

			for (TrapThr th : thrList) {

				try {
					matchTrapOid = th.matchTrapOid(vo.getTrapOid());

					// TRAP OID가 설정되지 않았거나 설정됐다면 같아야 하고 OID와 값이 값으면..
					if (matchTrapOid) {

						matchVarOid = isMatch(vo, th, ov.getOid(), ov.getValue());

						if (matchVarOid) {

							String instance = TrapApi.getApi().isOnceSameTrap() ? null : FxApi.getDate() + "";
							Map<String, Object> para = new HashMap<String, Object>();
							para.put(EventApi.PARA_ALARM_LEVEL, th.getAlarmLevel());
							para.put(EventApi.PARA_VALUE, ov.getValue());
							para.put(EventApi.PARA_TREAT_NAME, th.getTreatName());

							alarm = EventApi.getApi().check(node, instance, th.getAlarmCode(),
									makeMsg(node, oidValueList, th.getAlarmMsg()), para);

							if (alarm != null) {
								return vo;
							}
						}
					}

					// 위에서 매칭이 안되면 Clear 매칭여부를 판단합니다.
					if (th.matchTrapOidClear(vo.getTrapOid())) {
						if (th.getVarOidClear() != null && th.getVarValClear() != null) {
							if (th.getVarOidClear().startsWith(ov.getOid()) && ov.getValue().equals(th.getVarValClear())) {
								EventApi.getApi().checkClear(node, th.getAlarmCode(), ClearReason.Auto);
							}
						}
					}

				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

		return null;
	}

	private String makeMsg(TrapNode node, List<OidValue> oidValueList, String msgOrg) {

		String ret = msgOrg;
		for (OidValue ov : oidValueList) {
			ret = ret.replaceAll("%" + ov.getOid() + "%", ov.getValue());
		}

		return ret;
	}

	private boolean isMatch(TrapVo trap, TrapThr th, String oid, String var) {

		boolean matchVal = false;
		boolean matchOid = false;

		if (th.getVarOid() == null || th.getVarOid().trim().length() == 0)
			matchOid = true;
		else if (oid != null && th.getVarOid().startsWith(oid))
			matchOid = true;

		if (th.getVarVal() == null || th.getVarVal().trim().length() == 0)
			matchVal = true;
		else {
			if (var != null && var.equals(th.getVarVal()))
				matchVal = true;
		}

		if (matchOid && matchVal) {
			if (th.isThrowOut()) {
				Logger.logger.debug("discard" + " - " + trap);
				trap.setThrowOut(true);
				return false;
			}
		}

		return matchOid && matchVal;

	}
}

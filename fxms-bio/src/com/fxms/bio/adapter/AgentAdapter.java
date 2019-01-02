package com.fxms.bio.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgentActor;
import com.fxms.agent.FxAgentManager;
import com.fxms.agent.pdu.ResponsePdu;
import com.fxms.bio.agent.method.GetGwConfig;
import com.fxms.bio.agent.method.GetSensorList;
import com.fxms.bio.agent.method.GetSensorValue;
import com.fxms.bio.agent.processor.AgentResponseProcessor;
import com.fxms.bio.mo.DeviceMo;
import com.fxms.bio.mo.GwMo;

import fxms.bas.api.MoApi;
import fxms.bas.exception.FxTimeoutException;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;

public class AgentAdapter extends Adapter<DeviceMo> {

	private FxAgentManager manager;

	private FxAgentManager getManager() throws Exception {
		if (manager != null) {
			return manager;
		}

		List<FxAgentActor> holeList = FxActorParser.getParser().getActorList(FxAgentActor.class);
		if (holeList != null) {
			for (FxAgentActor hole : holeList) {
				try {
					manager = hole.getManager();
				} catch (Exception e) {
					Logger.logger.error(e);
				}
				break;
			}
		}

		if (manager == null) {
			throw new Exception("FxAgentManager not found");
		}

		return manager;
	}

	public AgentAdapter() {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PsVo> getValue(DeviceMo device, String psCodes[]) throws FxTimeoutException, Exception {

		GwMo gw = getGw(device);

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ps-codes", Arrays.asList(psCodes));
		parameters.put("sensor-id", device.getDeviceIdInGw());

		AgentResponseProcessor listener = new AgentResponseProcessor(gw.getGwIpaddr());
		getManager().request(gw.getGwIpaddr(), GetSensorValue.class.getSimpleName(), (Map) parameters, listener);

		try {
			ResponsePdu pdu = listener.getPdu();
			List<PsVo> voList = new ArrayList<PsVo>();

			Map<String, Object> retMap = pdu.getParameters();

			retMap.remove("sensor-id");
			for (String psCode : retMap.keySet()) {
				voList.add(new PsVo(device, null, psCode, (Number) retMap.get(psCode)));
			}
			return voList;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setValue(DeviceMo device, String method, Map<String, Object> parameters) throws Exception {

		GwMo gw = getGw(device);

		parameters.put("sensor-id", device.getDeviceIdInGw());

		AgentResponseProcessor listener = new AgentResponseProcessor(gw.getGwIpaddr());
		getManager().request(gw.getGwIpaddr(), method, (Map) parameters, listener);

		try {
			ResponsePdu pdu = listener.getPdu();
			Logger.logger.info("{}", pdu);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception {

		if (children.getParent() instanceof GwMo) {
			GwMo gw = (GwMo) children.getParent();

			AgentResponseProcessor listener = new AgentResponseProcessor(gw.getGwIpaddr());
			getManager().request(gw.getGwIpaddr(), GetSensorList.class.getSimpleName(), null, listener);

			try {
				ResponsePdu pdu = listener.getPdu();
				List<Map<String, Object>> sensorList = (List<Map<String, Object>>) pdu.getParaList("sensor-list");

				Logger.logger.info("{}", pdu);
			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		} else if (children.getParent() instanceof DeviceMo) {
			DeviceMo device = (DeviceMo) children.getParent();
			GwMo gw = getGw(device);

			AgentResponseProcessor listener = new AgentResponseProcessor(gw.getGwIpaddr());
			getManager().request(gw.getGwIpaddr(), GetGwConfig.class.getSimpleName(), null, listener);

			try {
				ResponsePdu pdu = listener.getPdu();
				Logger.logger.info("{}", pdu);

				gw.setGwVer(pdu.getParaString("version"));
				gw.setManagerIpaddr(pdu.getParaString("remote-host"));
				gw.setManagerPort(pdu.getParaInt("remote-port", 0));

			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		}

	}

	private GwMo getGw(DeviceMo device) throws Exception {
		GwMo gw = (GwMo) MoApi.getApi().getMo(device.getGwMoNo());
		if (gw == null) {
			throw new Exception("Not Found Gateway " + device.getGwMoNo());
		}

		return gw;
	}

}

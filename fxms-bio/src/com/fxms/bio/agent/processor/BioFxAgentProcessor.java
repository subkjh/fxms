package com.fxms.bio.agent.processor;

import java.util.List;
import java.util.Map;

import com.fxms.agent.FxAgentCode;
import com.fxms.agent.FxAgentPduProcessor;
import com.fxms.agent.pdu.FxAgentPdu;
import com.fxms.agent.pdu.NotifyPdu;
import com.fxms.bio.mo.ContainerMo;
import com.fxms.bio.mo.DeviceMo;
import com.fxms.bio.mo.GwMo;

import fxms.bas.api.MoApi;
import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLoader;
import fxms.bas.pso.PsVo;
import fxms.bas.pso.ValueApi;
import fxms.bas.pso.VoList;
import subkjh.bas.log.Logger;

public class BioFxAgentProcessor implements FxAgentPduProcessor {

	private final String TAG_GW = "GW";
	private final String TAG_SENSOR = "Sensor";

	public static void main(String[] args) throws Exception {
		BioFxAgentProcessor t = new BioFxAgentProcessor();

		System.out.println(MoApi.getApi().getFxMo(t.TAG_GW, "14.16.110.145"));
	}

	public BioFxAgentProcessor() throws Exception {
		MoLoader<GwMo> para = new MoLoader<GwMo>(GwMo.MO_CLASS, null) {
			@Override
			public String getKey(GwMo mo) {
				return mo.getGwIpaddr();
			}
		};

		para.putPara("mngYn", true);
		para.putPara("gwType", "T-Agent");

		MoLoader<DeviceMo> sensor = new MoLoader<DeviceMo>(DeviceMo.MO_CLASS, null) {
			@Override
			public String getKey(DeviceMo mo) {
				return mo.getDeviceIdInGw();
			}
		};

		MoApi.getApi().loadFxMo(TAG_GW, para);
		MoApi.getApi().loadFxMo(TAG_SENSOR, sensor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(NotiStatus status, FxAgentPdu receivedPdu) {
		String msg = null;
		VoList voList = null;

		try {
			if (status == NotiStatus.NotifyPdu) {
				NotifyPdu pdu = (NotifyPdu) receivedPdu;
				if ("ps-noti".equalsIgnoreCase(pdu.getMethod())) {

					String gwIp = pdu.getIpAddress();
					GwMo mo = getGwMo(gwIp);
					if (mo == null) {
						return;
					}

					PsVo vo;
					voList = new VoList("FxAgentManager", System.currentTimeMillis());

					List<Map<String, Object>> psList = (List<Map<String, Object>>) pdu.getParameters().get("ps-list");

					if (psList != null) {
						for (Map<String, Object> ps : psList) {
							vo = makePsVo(mo, ps.get(FxAgentCode.PS_LIST__ATTR__MO_NAME) + "",
									ps.get(FxAgentCode.PS_LIST__ATTR__PS_CODE) + "",
									(Number) ps.get(FxAgentCode.PS_LIST__ATTR__PS_VALUE));
							if (vo != null) {
								voList.add(vo);
							}
						}

						msg = psList.size() + " ps";
					} else {
						msg = "no ps-list";
					}

				} else {
					msg = "not ps-noti";
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			msg = "error";
		} finally {
			ValueApi.getApi().addValue(voList, true);
			Logger.logger.trace("{} : status={},pdu={}", msg, status, receivedPdu);
		}
	}

	private PsVo makePsVo(GwMo gw, String moName, String psCode, Number value) {

		if (psCode != null) {
			DeviceMo sensor = getMo(gw, moName);
			if (sensor == null) {
				return new PsVo(gw, moName, psCode, value);
			} else {
				return new PsVo(sensor, null, psCode, value);
			}
		} else {
			return null;
		}
	}

	private DeviceMo getMo(GwMo gw, String sensorId) {

		DeviceMo mo = (DeviceMo) MoApi.getApi().getFxMo(TAG_SENSOR, sensorId);
		if (mo != null) {
			return mo;
		}
		Class<? extends Mo> classOfMo = MoApi.getApi().getMoClass(DeviceMo.MO_CLASS);
		try {
			DeviceMo sensor = (DeviceMo) classOfMo.newInstance();
			String container = sensorId.split("\\.")[0];

			ContainerMo containerMo = (ContainerMo) MoApi.getApi().getMo(0, ContainerMo.MO_CLASS, container, false);
			if (containerMo == null) {
				Logger.logger.fail("container-id={} not registered", container);
				return null;
			}

			String deviceType = sensorId.split("\\.")[1];

			DeviceMo.set(sensor, containerMo.getMoNo(), gw.getMoNo(), sensorId, deviceType, null);
			sensor.setMoMemo("auto-insert");
			sensor = (DeviceMo) MoApi.getApi().addMo(sensor, "agent-notify");

			MoApi.getApi().putFxMo(TAG_SENSOR, sensor);

			return sensor;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

	private GwMo getGwMo(String gwIpaddr) {
		GwMo mo = (GwMo) MoApi.getApi().getFxMo(TAG_GW, gwIpaddr);
		if (mo != null) {
			return mo;
		}

		Class<? extends Mo> classOfMo = MoApi.getApi().getMoClass(GwMo.MO_CLASS);
		try {
			GwMo gw = (GwMo) classOfMo.newInstance();

			GwMo.set(gw, gwIpaddr, gwIpaddr, 0, "T-Agent");
			gw.setMoMemo("auto-insert");
			gw = (GwMo) MoApi.getApi().addMo(gw, "agent-notify");

			MoApi.getApi().putFxMo(TAG_GW, gw);

			return gw;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

}

package com.fxms.nms.fxactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.nms.dbo.FN_NET_AL_CUR;
import com.fxms.nms.dbo.FN_NET_AL_HST;
import com.fxms.nms.dbo.FN_NET_ITEM;
import com.fxms.nms.vo.NetItemVo;
import com.fxms.nms.vo.NetVo;

import fxms.bas.alarm.AlarmCode;
import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.property.FxServiceMember;
import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.noti.FxEvent;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;

/**
 * 발생, 해제된 경보를 이용하여 네트워크 경보를 발생, 해제하는 스레드
 * 
 * @author SUBKJH-DEV
 *
 */
public class NetAlarmMaker extends QueueFxThread<Alarm> implements FxServiceMember {

	public NetAlarmMaker() {
		super("NetAlarmMaker");
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		super.onNotify(noti);

		if (noti instanceof Alarm) {
			put((Alarm) noti);
		}

	}

	@Override
	protected void doWork(Alarm alarm) throws Exception {

		AlarmCode alarmCode = EventApi.getApi().getAlarmCodeByNo(alarm.getAlcdNo());

		if (moMap != null && alarmCode != null && alarmCode.isServiceAlarmYn()) {

			if (alarm.isCleared()) {
				clearNetAlarm(alarm);
				return;
			}

			String moNos;

			if (alarm.getUpperMoNo() > 0) {
				moNos = alarm.getUpperMoNo() + "." + alarm.getMoNo();
			} else {
				moNos = alarm.getMoNo() + "";
			}

			List<NetItemVo> itemList = moMap.get(moNos);

			if (itemList != null) {
				Map<Integer, FN_NET_AL_CUR> netAlarmMap = new HashMap<Integer, FN_NET_AL_CUR>();

				NetVo net;
				for (NetItemVo item : itemList) {
					net = netMap.get(item.getNetNo());
					if (net != null) {

						if (netAlarmMap.get(net.getNetNo()) == null) {
							Logger.logger.debug("Make Network Alarm={}, Net={}", alarm.getAlarmNo(), net.getNetNo());

							FN_NET_AL_CUR al = new FN_NET_AL_CUR();
							al.setAlarmNo(alarm.getAlarmNo());
							al.setIfMoNo(alarm.getUpperMoNo() > 0 ? alarm.getMoNo() : -1);
							al.setNeMoNo(alarm.getUpperMoNo() > 0 ? alarm.getUpperMoNo() : alarm.getMoNo());
							al.setNetName(net.getNetName());
							al.setNetNo(net.getNetNo());
							al.setRegDate(FxApi.getDate());
							netAlarmMap.put(net.getNetNo(), al);
						}
					}
				}

				if (netAlarmMap.size() > 0) {
					DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao().insertOfClass(FN_NET_AL_CUR.class,
							netAlarmMap.values());
				}

				return;
			}

		}
		Logger.logger.trace("Make Network Alarm={} not found network", alarm.getAlarmNo());

	}

	private void clearNetAlarm(Alarm alarm) {
		FxDaoExecutor tran;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		} catch (Exception e) {
			Logger.logger.error(e);
			return;
		}

		try {
			tran.start();

			Map<String, Object> para = FxDaoExecutor.makePara("alarmNo", alarm.getAlarmNo());
			List<FN_NET_AL_CUR> netList = tran.select(FN_NET_AL_CUR.class, para);
			tran.deleteOfClass(FN_NET_AL_CUR.class, para);

			for (FN_NET_AL_CUR cur : netList ) {
				cur.setRegDate(FxApi.getDate());
			}
			tran.insertOfClass(FN_NET_AL_HST.class, netList);


			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();

		} finally {
			tran.stop();
		}
	}

	@Override
	protected void onNoDatas(long index) {

	}

	/** key : NE_MO_NO [ + "." + IF_MO_NO ] */
	private Map<String, List<NetItemVo>> moMap;
	private Map<Integer, NetVo> netMap;

	@Override
	protected void doInit() {

		FxDaoExecutor tran;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		} catch (Exception e) {
			Logger.logger.error(e);
			return;
		}

		try {
			tran.start();

			NetVo net;
			List<NetVo> netList = tran.select(NetVo.class, null);
			List<NetItemVo> itemList = tran.select(NetItemVo.class, null);

			Map<Integer, NetVo> netMap = new HashMap<Integer, NetVo>();
			for (NetVo vo : netList) {
				netMap.put(vo.getNetNo(), vo);
			}

			for (FN_NET_ITEM vo : itemList) {
				net = netMap.get(vo.getNetNo());
				if (net != null) {
					net.getItemList().add(vo);
				}
			}

			Map<String, List<NetItemVo>> moMap = new HashMap<String, List<NetItemVo>>();

			for (NetItemVo vo : itemList) {
				putItem(vo.getStartNeMoNo() + "", vo, moMap);
				putItem(vo.getEndNeMoNo() + "", vo, moMap);
				if (vo.getStartIfMoNo() > 0) {
					putItem(vo.getStartNeMoNo() + "." + vo.getStartIfMoNo(), vo, moMap);
				}
				if (vo.getEndIfMoNo() > 0) {
					putItem(vo.getEndNeMoNo() + "." + vo.getEndIfMoNo(), vo, moMap);
				}
			}

			this.netMap = netMap;
			this.moMap = moMap;

		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
			tran.stop();
		}

	}

	private void putItem(String key, NetItemVo vo, Map<String, List<NetItemVo>> moMap) {
		List<NetItemVo> entry;

		entry = moMap.get(key);
		if (entry == null) {
			entry = new ArrayList<NetItemVo>();
			moMap.put(key, entry);
		}
		entry.add(vo);
	}

}

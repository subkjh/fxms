package com.fxms.bio.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.bio.mo.ContainerMo;
import com.fxms.bio.mo.DeviceMo;
import com.fxms.bio.mo.GwMo;
import com.fxms.bio.vo.IdValVO;

import fxms.bas.alarm.AoCode.ClearReason;
import fxms.bas.api.EventApi;
import fxms.bas.api.MoApi;
import fxms.bas.define.ALARM_CODE;
import fxms.bas.exception.FxTimeoutException;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exception.MoNotFoundException;
import fxms.bas.pso.PsVo;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.database.DataBase;
import subkjh.bas.dao.model.SqlBean;
import subkjh.bas.log.Logger;

public class GwMySqlAdapter extends Adapter<GwMo> {

	@SuppressWarnings("unchecked")
	@Override
	public void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception {

		GwMo gw = (GwMo) children.getParent();

		DataBase database = DBManager.getMgr().getDataBase(this.getParaStr("db-name"));
		if (database == null) {
			throw new Exception("db-name not defined");
		}
		DbTrans tran = database.createDbTrans("deploy/conf/sql/bio/gw-mysql-adapter.xml");

		ContainerMo container = getContainerMo();

		try {
			tran.start();
			List<IdValVO> idList = tran.selectQid2Res("SELECT_SENSOR_ID_LIST", null);
			DeviceMo device;
			String deviceId;
			for (IdValVO vo : idList) {
				deviceId = container.getContainerId() + "." + vo.getTag() + "." + vo.getId();
				device = (DeviceMo) MoApi.getApi().makeNewMo(DeviceMo.MO_CLASS);
				DeviceMo.set(device, container.getMoNo(), gw.getMoNo(), deviceId, vo.getTag(), null);
				children.addMo(device, true);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}

		return;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PsVo> getValue(GwMo gw, String psCodes[]) throws FxTimeoutException, Exception {

		DataBase database = DBManager.getMgr().getDataBase(getParaStr("db-name"));
		if (database == null) {
			throw new Exception("db-name not defined");
		}
		DbTrans tran = database.createDbTrans("deploy/conf/sql/bio/gw-mysql-adapter.xml");

		ContainerMo container = getContainerMo();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("startUnixTime", (System.currentTimeMillis() - 120000) / 1000L);
			para.put("unixTime", (System.currentTimeMillis() - 86400000) / 1000L);
			para.put("endUnixTime", (System.currentTimeMillis() - 60000) / 1000L);

			List<IdValVO> voList = tran.selectQid2Res("SELECT_SENSOR_VALUE", para);

			DeviceMo device;
			List<PsVo> psList = new ArrayList<PsVo>();
			String deviceId;

			for (IdValVO vo : voList) {
				deviceId = container.getContainerId() + "." + vo.getTag() + "." + vo.getId();

				device = MoApi.getApi().getMo(container.getMoNo(), DeviceMo.class, deviceId, false);
				if (device == null) {
					device = addMo(container, gw, deviceId, vo);
				}

				if (device == null) {
					Logger.logger.trace("sendor-id={}, device not found", deviceId);
				} else {
					psList.add(new PsVo(device, null, vo.getTag(), vo.getVal()));
				}

			}

			int index = 1;
			SqlBean qid;
			while (true) {
				qid = tran.getSqlBean("DELETE_SENSOR_VALUE_" + index);
				if (qid != null) {
					tran.execute(qid.getQid(), para);
				} else {
					break;
				}
				index++;
			}

			if (voList.size() == 0) {
				fireSensorNoDatas(gw);
			} else {
				EventApi.getApi().checkClear(gw, null, ALARM_CODE.NO_DATA_ON_PS_COLLECT, ClearReason.Auto);
			}

			return psList;

		} catch (Exception e) {
			Logger.logger.error(e);
			EventApi.getApi().check(gw, null, ALARM_CODE.ERR_RESPONSE_ON_PS_COLLECT, null, null);
			return null;
		} finally {
			tran.stop();
		}

	}

	@Override
	public void setValue(GwMo gw, String method, Map<String, Object> para) throws Exception {
		throwNotImplException(gw);
	}

	private DeviceMo addMo(ContainerMo container, GwMo gw, String deviceId, IdValVO vo) {
		DeviceMo device;
		try {
			device = (DeviceMo) MoApi.getApi().makeNewMo(DeviceMo.MO_CLASS);
			DeviceMo.set(device, container.getMoNo(), gw.getMoNo(), deviceId, vo.getTag(), null);
			return (DeviceMo) MoApi.getApi().addMo(device, "adapter");
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	private void fireSensorNoDatas(GwMo gw) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("gwMoNo", gw.getMoNo());
		try {
			List<DeviceMo> moList = MoApi.getApi().getMoList(DeviceMo.class, para);

			if (moList.size() == 0) {
				// GW 담당 센서가 없으면..
				EventApi.getApi().check(gw, null, ALARM_CODE.NO_DATA_ON_PS_COLLECT, null, null);
			} else {
				for (DeviceMo mo : moList) {
					EventApi.getApi().check(mo, null, ALARM_CODE.NO_DATA_ON_PS_COLLECT, null, null);
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	private ContainerMo getContainerMo() throws Exception {

		String containerId = getParaStr("container-id");
		if (containerId == null) {
			containerId = "NULL";
		}

		ContainerMo mo = null;
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("containerId", containerId);
		List<ContainerMo> moList = MoApi.getApi().getMoList(ContainerMo.class, para);
		if (moList.size() == 1) {
			mo = moList.get(0);
		}

		if (mo == null) {
			throw new MoNotFoundException("container-id=" + containerId);
		}

		return mo;
	}

}
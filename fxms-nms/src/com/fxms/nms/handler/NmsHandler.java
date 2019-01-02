package com.fxms.nms.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.nms.dbo.FN_NET;
import com.fxms.nms.dbo.FN_NET_ITEM;
import com.fxms.nms.dbo.FN_NE_STATUS;
import com.fxms.nms.mo.NeMo;
import com.fxms.nms.vo.NetVo;

import fxms.bas.api.FxApi;
import fxms.bas.dao.FxConfDao;
import fxms.bas.exception.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.module.restapi.handler.CommHandler;
import fxms.module.restapi.handler.func.MoAdd;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.FxDaoCallback;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class NmsHandler extends CommHandler {

	public Object getNeStatus(SessionVo session, Map<String, Object> parameters) throws Exception {
		long moNo = getLong(parameters, "moNo");
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", moNo);
		return FxConfDao.getDao().selectOne(FN_NE_STATUS.class, para);
	}

	public Object addMoNe(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new MoAdd().handle(session, parameters, NeMo.MO_CLASS);
	}

	public Object addNetwork(SessionVo session, Map<String, Object> parameters) throws Exception {

		FN_NET item = add(session, parameters, new FN_NET(), new FxDaoCallback<FN_NET>() {
			@Override
			public void onCall(FxDaoExecutor tran, FN_NET data) throws Exception {
				int no;

				data.setRegDate(FxApi.getDate(0));
				data.setRegUserNo(session.getUserNo());
				no = tran.getNextVal(FN_NET.FX_SEQ_NETNO, Integer.class);
				data.setNetNo(no);
			}
		});

		return item;
	}

	@SuppressWarnings("unchecked")
	public Object setNetworkItem(SessionVo session, Map<String, Object> parameters) throws Exception {

		NetVo net = new NetVo();
		ObjectUtil.toObject((Map<String, Object>) parameters.get("network"), net);
		List<Map<String, Object>> itemList = (List<Map<String, Object>>) parameters.get("item-list");
		FN_NET_ITEM item;

		for (Map<String, Object> map : itemList) {
			item = new FN_NET_ITEM();
			ObjectUtil.toObject(map, item);
			item.setNetNo(net.getNetNo());
			net.getItemList().add(item);
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			tran.deleteOfClass(FN_NET_ITEM.class, FxDaoExecutor.makePara("netNo", net.getNetNo()));
			if (net.getItemList().size() > 0) {
				tran.insertOfClass(FN_NET_ITEM.class, net.getItemList());
			}

			tran.commit();

			return net;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 망 구성 목록을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getNetworkItem(SessionVo session, Map<String, Object> parameters) throws Exception {

		int netNo = getInt(parameters, "netNo", -1);
		String netName = getString(parameters, "netName", null);

		Map<String, Object> para = new HashMap<String, Object>();

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			if (netNo < 0) {
				para.put("netName", netName);
				FN_NET net = tran.selectOne(FN_NET.class, para);
				if (net == null) {
					throw new NotFoundException("network", netName);
				}

				netNo = net.getNetNo();
			}

			para.clear();
			para.put("netNo", netNo);

			return tran.select(FN_NET_ITEM.class, para);

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public Object updateNetwork(SessionVo session, Map<String, Object> parameters) throws Exception {
		FN_NET item = update(session, parameters, new FN_NET(), null);
		return item;
	}

	public Object deleteNetwork(SessionVo session, Map<String, Object> parameters) throws Exception {
		FN_NET item = delete(session, parameters, new FN_NET(), new FxDaoCallback<FN_NET>() {
			@Override
			public void onCall(FxDaoExecutor tran, FN_NET data) throws Exception {
				tran.deleteOfClass(FN_NET_ITEM.class, FxDaoExecutor.makePara("netNo", data.getNetNo()));
			}
		});
		return item;
	}
}

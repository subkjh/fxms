package com.fxms.bio.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.bio.dao.BioDao;
import com.fxms.bio.dbo.FB_BATCH;
import com.fxms.bio.dbo.FB_BATCH_DATA;
import com.fxms.bio.dbo.FB_BATCH_FLOW;
import com.fxms.bio.dbo.FB_BATCH_IN;
import com.fxms.bio.dbo.FB_BATCH_OUT;
import com.fxms.bio.dbo.FB_CD_MTRL;
import com.fxms.bio.dbo.FB_CD_PRDC;
import com.fxms.bio.handler.func.AddBatchData;
import com.fxms.bio.handler.func.GetBatchData;
import com.fxms.bio.handler.func.GetBatchIds;
import com.fxms.bio.handler.func.GetCurBatch;
import com.fxms.bio.handler.func.MakeNewBatch;
import com.fxms.bio.handler.func.MakeNewBatchId;
import com.fxms.bio.mo.ContainerMo;
import com.fxms.bio.mo.GwMo;
import com.fxms.bio.mo.PbrMo;

import fxms.bas.dao.FxConfDao;
import fxms.bas.dbo.FX_COMMON;
import fxms.bas.exception.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.module.restapi.handler.CommHandler;
import fxms.module.restapi.handler.func.MoAdd;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;
import subkjh.bas.utils.ObjectUtil;

/**
 * 
 * BIO
 * 
 * @author SUBKJH-DEV
 *
 */
public class BioHandler extends CommHandler {

	public static void main(String[] args) throws Exception {
		BioHandler handler = new BioHandler();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("batchId", "L180130001");
		Object ret = handler.getBatch(null, parameters);
		System.out.println(ObjectUtil.toMap(ret));
	}

	/**
	 * 배치 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addBatch(SessionVo session, Map<String, Object> parameters) throws Exception {

		String batchId = getString(parameters, "batchId");
		long pbrMoNo = getLong(parameters, "pbrMoNo");
		String prdcCode = getString(parameters, "prdcCode");

		return new MakeNewBatch().handle(session.getUserNo(), session.getMngInloNo(), batchId, pbrMoNo, prdcCode);
	}

	/**
	 * 배치 데이터 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addBatchData(SessionVo session, Map<String, Object> parameters) throws Exception {

		String batchId = getString(parameters, "batchId");
		long pbrMoNo = getLong(parameters, "pbrMoNo");
		String prdcCode = getString(parameters, "prdcCode");
		String yyyymmdd = getString(parameters, "yyyymmdd");

		return new AddBatchData().handle(session.getUserNo(), batchId, yyyymmdd, pbrMoNo, prdcCode, parameters);
	}

	/**
	 * 배치플로우 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addBatchFlow(SessionVo session, Map<String, Object> parameters) throws Exception {
		FB_BATCH_FLOW data = makeData(session, parameters, FB_BATCH_FLOW.class);
		FxConfDao.getDao().insert(data);
		return data;
	}

	/**
	 * 배치 원자래 주입 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object addBatchIn(SessionVo session, Map<String, Object> parameters) throws Exception {

		List<Map<String, Object>> list = (List) parameters.get("list");
		List<FB_BATCH_IN> dataList = new ArrayList<FB_BATCH_IN>();

		for (Map<String, Object> map : list) {
			dataList.add(makeData(session, map, FB_BATCH_IN.class));
		}

		FxConfDao.getDao().insertList(FB_BATCH_IN.class, dataList);

		return dataList;
	}

	/**
	 * 배치 생산 내용 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object addBatchOut(SessionVo session, Map<String, Object> parameters) throws Exception {

		List<Map<String, Object>> list = (List) parameters.get("list");
		List<FB_BATCH_OUT> dataList = new ArrayList<FB_BATCH_OUT>();

		for (Map<String, Object> map : list) {
			dataList.add(makeData(session, map, FB_BATCH_OUT.class));
		}

		FxConfDao.getDao().insertList(FB_BATCH_OUT.class, dataList);

		return dataList;
	}

	/**
	 * 컨테이너 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addMoContainer(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new MoAdd().handle(session, parameters, ContainerMo.MO_CLASS);
	}

	/**
	 * GW 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addMoGw(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new MoAdd().handle(session, parameters, GwMo.MO_CLASS);
	}

	/**
	 * PBR 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addMoPbr(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new MoAdd().handle(session, parameters, PbrMo.MO_CLASS);
	}

	/**
	 * 원자재 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addMtrl(SessionVo session, Map<String, Object> parameters) throws Exception {

		FB_CD_MTRL cdMtrl = makeData(session, parameters, FB_CD_MTRL.class);

		FxConfDao.getDao().insert(cdMtrl);

		return cdMtrl;
	}

	/**
	 * 생산품 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addPrdc(SessionVo session, Map<String, Object> parameters) throws Exception {
		FB_CD_PRDC data = makeData(session, parameters, FB_CD_PRDC.class);
		FxConfDao.getDao().insert(data);
		return data;
	}

	/**
	 * 신규 배치번호 호출
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object createNewBatch(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new MakeNewBatchId().handle(session.getMngInloNo());
	}

	/**
	 * 배치 삭제
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object deleteBatch(SessionVo session, Map<String, Object> parameters) throws Exception {

		String batchId = getString(parameters, "batchId");

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			FB_BATCH batch = tran.selectOne(FB_BATCH.class, FxDaoExecutor.makePara("batchId", batchId));
			if (batch == null) {
				throw new NotFoundException("BATCH", batchId);
			}
			Map<String, Object> para = FxDaoExecutor.makePara("batchId", batchId);
			para.put("delYn", "Y");

			tran.updateOfClass(FB_BATCH.class, para);

			// tran.deleteOfClass(FB_BATCH_FLOW.class, para);
			// tran.deleteOfClass(FB_BATCH_IN.class, para);
			// tran.deleteOfClass(FB_BATCH_OUT.class, para);
			// tran.deleteOfClass(FB_BATCH_SENSOR_VAL.class, para);
			// tran.deleteOfClass(FB_BATCH.class, para);

			tran.commit();

			return batch;
		} catch (Exception e) {
			tran.rollback();
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 배치 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getBatch(SessionVo session, Map<String, Object> parameters) throws Exception {

		String batchId = getString(parameters, "batchId");

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			return tran.selectOne(FB_BATCH.class, FxDaoExecutor.makePara("batchId", batchId, "delYn", "N"));
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 배치아이디 기준 배치데이터 호출 /
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object getBatchDataById(SessionVo session, Map<String, Object> parameters) throws Exception {

		List<String> batchIdList = (List<String>) parameters.get("batchId");

		if (batchIdList != null) {
			return new GetBatchData().handle(batchIdList);
		}

		return null;

	}

	/**
	 * 기간별 배치데이터 호출
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getBatchDataByTerm(SessionVo session, Map<String, Object> parameters) throws Exception {

		int inloNo = session.getMngInloNo();
		long startDate = getLong(parameters, "startDate");
		long endDate = getLong(parameters, "endDate");
		String prdcCode = getString(parameters, "prdcCode", null);
		long pbrMoNo = getLong(parameters, "pbrMoNo", 0);

		return new GetBatchData().handle(inloNo, startDate, endDate, prdcCode, pbrMoNo);

	}

	/**
	 * 기간별 배치아이디 호출
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getBatchIds(SessionVo session, Map<String, Object> parameters) throws Exception {
		int inloNo = session.getMngInloNo();
		long startDate = getLong(parameters, "startDate");
		String prdcCode = getString(parameters, "prdcCode", null);
		return new GetBatchIds().handle(inloNo, startDate, prdcCode);
	}

	/**
	 * 컨테이너 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getContainerList(SessionVo session, Map<String, Object> parameters) throws Exception {

		User.USER_TYPE userType = User.USER_TYPE.getUserType(session.getUserType());
		if (userType != User.USER_TYPE.admin) {
			parameters.put("userNo", session.getUserNo());
		}

		return new BioDao().selectContainerList(parameters);
	}

	/**
	 * 진행중 배치 호출
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getCurBatch(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new GetCurBatch().handle(session.getMngInloNo());
	}

	/**
	 * 마지막 배치 호출
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getLastBatchData(SessionVo session, Map<String, Object> parameters) throws Exception {
		int inloNo = session.getMngInloNo();
		return new GetBatchData().handle(inloNo);
	}

	/**
	 * 신규 배치번호 호출
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getNewBatchId(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new MakeNewBatchId().handle(session.getMngInloNo());
	}

	/**
	 * 센서 목록 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getSensorAll(SessionVo session, Map<String, Object> parameters) throws Exception {

		User.USER_TYPE userType = User.USER_TYPE.getUserType(session.getUserType());
		if (userType != User.USER_TYPE.admin) {
			parameters.put("userNo", session.getUserNo());
		}

		return new BioDao().selectSensorList(parameters);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object setBatchFlow(SessionVo session, Map<String, Object> parameters) throws Exception {

		List<Map<String, Object>> list = (List) parameters.get("list");
		List<FB_BATCH_FLOW> dataList = new ArrayList<FB_BATCH_FLOW>();

		for (Map<String, Object> map : list) {
			dataList.add(makeData(session, map, FB_BATCH_FLOW.class));
		}

		setBatch(parameters.get("batchNo"), FB_BATCH_FLOW.class, dataList);

		return dataList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object setBatchIn(SessionVo session, Map<String, Object> parameters) throws Exception {

		List<Map<String, Object>> list = (List) parameters.get("list");
		List<FB_BATCH_IN> dataList = new ArrayList<FB_BATCH_IN>();

		for (Map<String, Object> map : list) {
			dataList.add(makeData(session, map, FB_BATCH_IN.class));
		}

		setBatch(parameters.get("batchNo"), FB_BATCH_IN.class, dataList);

		return dataList;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object setBatchOut(SessionVo session, Map<String, Object> parameters) throws Exception {

		List<Map<String, Object>> list = (List) parameters.get("list");
		List<FB_BATCH_OUT> dataList = new ArrayList<FB_BATCH_OUT>();

		for (Map<String, Object> map : list) {
			dataList.add(makeData(session, map, FB_BATCH_OUT.class));
		}

		setBatch(parameters.get("batchNo"), FB_BATCH_OUT.class, dataList);

		return dataList;
	}

	/**
	 * 배치 수정
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateBatch(SessionVo session, Map<String, Object> parameters) throws Exception {
		return update(FB_BATCH.class, session, parameters);
	}

	/**
	 * 배치데이터 수정
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateBatchData(SessionVo session, Map<String, Object> parameters) throws Exception {
		return update(FB_BATCH_DATA.class, session, parameters);
	}

	/**
	 * 원자재 수정
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateMtrl(SessionVo session, Map<String, Object> parameters) throws Exception {
		return update(FB_CD_MTRL.class, session, parameters);
	}

	private <T> T makeData(SessionVo session, Map<String, Object> map, Class<T> classOfT) throws Exception {

		T data = classOfT.newInstance();

		ObjectUtil.toObject(map, data);

		if (data instanceof FX_COMMON) {
			initFX_COMMON(session, (FX_COMMON) data);
		}

		return data;
	}

	private void setBatch(Object batchNo, Class<?> classOf, List<?> dataList) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			tran.deleteOfClass(classOf, FxDaoExecutor.makePara("batchNo", batchNo));
			tran.insertOfClass(classOf, dataList);
			tran.commit();
		} catch (Exception e) {
			tran.rollback();
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	protected Class<?> getMethodClass(String methodName) {

		if (methodName == null) {
			return null;
		}

		// if ("add-batch".equals(methodName)) {
		// return FB_BATCH.class;
		// } else if ("add-batch-data".equals(methodName)) {
		// return FB_BATCH_DATA.class;
		// } else if ("add-batch-flow".equals(methodName)) {
		// return FB_BATCH_FLOW.class;
		// } else if ("add-prdc".equals(methodName)) {
		// return FB_CD_PRDC.class;
		// } else if ("add-mtrl".equals(methodName)) {
		// return FB_CD_MTRL.class;
		// } else if ("add-mo-container".equals(methodName)) {
		// return ContainerMo.class;
		// } else if ("add-mo-pbr".equals(methodName)) {
		// return PbrMo.class;
		// } else if ("add-mo-gw".equals(methodName)) {
		// return GwMo.class;
		// }

		return null;
	}

}

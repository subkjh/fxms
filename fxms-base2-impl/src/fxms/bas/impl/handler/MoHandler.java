package fxms.bas.impl.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.FxDaoCallback;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.FxConfDao;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.impl.co.MoHandlerDao;
import fxms.bas.impl.dbo.FX_CF_INLO;
import fxms.bas.impl.dbo.FX_CF_INLO_MEM;
import fxms.bas.impl.dbo.FX_CF_MODEL;
import fxms.bas.impl.handler.func.MoAdd;
import fxms.bas.impl.handler.func.MoUpdate;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exp.MoNotFoundException;
import fxms.module.restapi.CommHandler;
import fxms.module.restapi.vo.SessionVo;

public class MoHandler extends CommHandler {

	/**
	 * 설치위치 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addInlo(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CF_INLO item = new FX_CF_INLO();
		ObjectUtil.toObject(parameters, item);
		initRegChgVo(session, item);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			int inloNo = tran.getNextVal("FX_SEQ_INLONO", Integer.class);
			item.setInloNo(inloNo);
			item.setInloFname(item.getInloName());

			List<FX_CF_INLO_MEM> memList = setInlo(tran, item);

			tran.insertOfClass(FX_CF_INLO.class, item);
			tran.deleteOfClass(FX_CF_INLO_MEM.class, null, FxDaoExecutor.makePara("memInloNo", item.getInloNo()));
			tran.insertOfClass(FX_CF_INLO_MEM.class, memList);

			tran.commit();

			if (FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
			}

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 관리대상 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addMo(SessionVo session, Map<String, Object> parameters) throws Exception {
		return new MoAdd().handle(session, parameters, parameters.get("moClass") + "");
	}

	/**
	 * 모델 추가
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object addModel(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CF_MODEL item = add(session, parameters, new FX_CF_MODEL(), new FxDaoCallback<FX_CF_MODEL>() {
			@Override
			public void onCall(FxDaoExecutor tran, FX_CF_MODEL data) throws Exception {
				int modelNo;
				modelNo = tran.getNextVal(FX_CF_MODEL.FX_SEQ_MODELNO, Integer.class);
				data.setModelNo(modelNo);
			}
		});

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
		}

		return item;

	}

	/**
	 * 설치위치 삭제
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object deleteInlo(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CF_INLO item = new FX_CF_INLO();
		ObjectUtil.toObject(parameters, item);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			FX_CF_INLO oldItem = tran.selectOne(FX_CF_INLO.class, FxDaoExecutor.makePara("inloNo", item.getInloNo()));

			if (oldItem != null) {
				tran.deleteOfClass(FX_CF_INLO.class, item, null);
				tran.commit();
				if (FxServiceImpl.fxService != null) {
					FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
				}

				return oldItem;
			}

			throw new Exception("inlo-no=" + item.getInloNo() + " not found");

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 관리대상 삭제
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object deleteMo(SessionVo session, Map<String, Object> parameters) throws Exception {

		try {
			Mo mo = null;
			Number moNo = getNumber(parameters, "moNo");
			mo = (Mo) MoApi.getApi().getMo(moNo.longValue());

			if (mo == null) {
				throw new MoNotFoundException(moNo.longValue());
			}

			MoApi.getApi().deleteMo(mo, session.getUserNo(), "user-delete");

			if (FxServiceImpl.fxService != null) {
				mo.setStatus(FxEvent.STATUS.deleted);
				FxServiceImpl.fxService.send(mo);
			}

			return mo;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	/**
	 * 모델 삭제
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object deleteModel(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CF_MODEL item = delete(session, parameters, new FX_CF_MODEL(), null);

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
		}
		return item;
	}

	/**
	 * 관리대상으로부터 구성 정보를 가져온다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object detect(SessionVo session, Map<String, Object> parameters) throws Exception {
		long moNo = getLong(parameters, "moNo");
		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}
		return MoApi.getApi().detect(mo);
	}

	/**
	 * 설치위치 목록을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getInloList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return FxConfDao.getDao().select(FX_CF_INLO.class, parameters);
	}

	/**
	 * 한 관리대상을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getMo(SessionVo session, Map<String, Object> parameters) throws Exception {

		if (parameters.get("moNo") != null) {
			return MoApi.getApi().getMo(Double.valueOf(parameters.get("moNo").toString()).longValue());
		}

		List<Mo> moList = MoApi.getApi().getMoList(parameters);

		if (moList == null || moList.size() == 0) {
			throw new MoNotFoundException(parameters.toString());
		}

		return moList.get(0);
	}

	/**
	 * 관리대상의 구성을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getMoConfig(SessionVo session, Map<String, Object> parameters) throws Exception {
		long moNo = getLong(parameters, "moNo");
		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}

		MoConfig moConfig = MoApi.getApi().getMoConfig(mo);
		return moConfig.toMap();
	}

	/**
	 * 관리대상 종류별 건수를 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getMoCount(SessionVo session, Map<String, Object> parameters) throws Exception {

		Map<String, Object> para = makePara(session, parameters);

		return new MoHandlerDao().selectMoCount(para);
	}

	/**
	 * 관리대상 목록을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getMoList(SessionVo session, Map<String, Object> parameters) throws Exception {

		if (parameters.get("moClass") == null) {
			return MoApi.getApi().getMoList(parameters);
		} else {
			Class<? extends Mo> classOfMo = MoApi.getApi().getMoClass(parameters.get("moClass") + "");
			return MoApi.getApi().getMoList(parameters, classOfMo);
		}
	}

	/**
	 * 관리대상의 설정 명령을 내린다
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object setupMo(SessionVo session, Map<String, Object> parameters) throws Exception {

		long moNo = getLong(parameters, "moNo");
		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}
		parameters.remove("moNo");

		String method = getString(parameters, "method");
		parameters.remove("method");

		MoService service = ServiceApi.getApi().getService(MoService.class, mo);
		service.setValue(mo, method, parameters);

		return parameters;
	}

	/**
	 * 관리대상의 구성정보를 실시간으로 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object testMo(SessionVo session, Map<String, Object> parameters) throws Exception {

		Mo mo = new Mo();
		ObjectUtil.toObject(parameters, mo);

		MoService service = ServiceApi.getApi().getService(MoService.class, mo);

		return service.getConfig(mo.getMoNo());
	}

	/**
	 * 설치위치 수정
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateInlo(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CF_INLO item = new FX_CF_INLO();
		ObjectUtil.toObject(parameters, item);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			item.setChgDate(FxApi.getDate(0));
			item.setChgUserNo(session.getUserNo());
			item.setInloFname(item.getInloName());

			List<FX_CF_INLO_MEM> memList = setInlo(tran, item);

			tran.updateOfClass(FX_CF_INLO.class, item, null);
			tran.deleteOfClass(FX_CF_INLO_MEM.class, null, FxDaoExecutor.makePara("memInloNo", item.getInloNo()));
			tran.insertOfClass(FX_CF_INLO_MEM.class, memList);
			item = tran.selectOne(FX_CF_INLO.class, FxDaoExecutor.makePara("inloNo", item.getInloNo()));

			tran.commit();

			if (FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
			}

			return item;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 관리대상 수정
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateMo(SessionVo session, Map<String, Object> parameters) throws Exception {
		long moNo = getLong(parameters, "moNo");
		return new MoUpdate().handle(session, parameters, moNo);
	}

	/**
	 * 모델 수정
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateModel(SessionVo session, Map<String, Object> parameters) throws Exception {

		FX_CF_MODEL item = update(session, parameters, new FX_CF_MODEL(), null);

		if (FxServiceImpl.fxService != null) {
			FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_CFG));
		}

		return item;
	}

	private List<FX_CF_INLO_MEM> setInlo(FxDaoExecutor tran, FX_CF_INLO item) throws Exception {
		FX_CF_INLO upper = null;
		List<FX_CF_INLO_MEM> list = new ArrayList<FX_CF_INLO_MEM>();
		FX_CF_INLO_MEM mem;
		int depth = 0;
		Map<Integer, Integer> dupChkMap = new HashMap<Integer, Integer>();

		mem = new FX_CF_INLO_MEM();
		mem.setInloNo(item.getInloNo());
		mem.setInloType(item.getInloType());
		mem.setMemInloNo(item.getInloNo());
		mem.setMemDepth(depth);

		list.add(mem);

		if (item.getUpperInloNo() > 0) {

			dupChkMap.put(item.getUpperInloNo(), item.getUpperInloNo());

			upper = tran.selectOne(FX_CF_INLO.class, FxDaoExecutor.makePara("inloNo", item.getUpperInloNo()));
			if (upper == null) {
				item.setUpperInloNo(-1);
			} else {
				item.setInloFname(upper.getInloName() + " > " + item.getInloName());
			}
		}

		while (upper != null) {

			depth++;

			mem = new FX_CF_INLO_MEM();
			mem.setInloNo(upper.getInloNo());
			mem.setInloType(upper.getInloType());
			mem.setMemInloNo(item.getInloNo());
			mem.setMemDepth(depth);
			list.add(mem);

			if (upper.getUpperInloNo() > 0) {
				if (dupChkMap.get(upper.getUpperInloNo()) != null) {
					break;
				}
				upper = tran.selectOne(FX_CF_INLO.class, FxDaoExecutor.makePara("inloNo", upper.getUpperInloNo()));
			} else {
				break;
			}
		}

		return list;
	}
}

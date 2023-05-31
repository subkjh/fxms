package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.event.TargetFxEvent;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.InloHandlerQid;
import fxms.bas.impl.dbo.all.FX_CF_VAR;
import fxms.bas.impl.dbo.all.FX_CO_OP;
import fxms.bas.impl.dbo.all.FX_CO_OP_ATTR;
import fxms.bas.impl.handler.dto.CdSelectQidPara;
import fxms.bas.impl.handler.dto.SelectVarPara;
import fxms.bas.impl.handler.dto.SendEventPara;
import fxms.bas.impl.handler.dto.SendReloadSignalPara;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 시스템 관련 HANDLER
 * 
 * @author subkjh
 *
 */
public class SystemHandler extends BaseHandler {

	public static void main(String[] args) throws Exception {
		SystemHandler handler = new SystemHandler();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("varGrpName", "성능조회");
		SessionVo session = new SessionVo("AAA", 1, "test", "test", USER_TYPE_CD.Operator, 0, 0);
		SelectVarPara item = handler.convert(session, parameters, SelectVarPara.class, true);
		System.out.println(handler.selectVarList(session, item));
	}

	/**
	 * 시스템 시간 조회
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object getSystemTime(SessionVo session, Map<String, Object> parameters) throws Exception {
		Map<String, Object> ret = new HashMap<String, Object>();
		long mstime = System.currentTimeMillis();
		ret.put("system-mstime", mstime);
		ret.put("system-hstime", DateUtil.getDtm(mstime));
		return ret;
	}

	/**
	 * 기능항목을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectOpList(SessionVo session, Map<String, Object> parameters) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			List<FX_CO_OP> opList = tran.select(FX_CO_OP.class, parameters);
			List<FX_CO_OP_ATTR> attrList = tran.select(FX_CO_OP_ATTR.class, parameters);

			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("op-list", opList);
			ret.put("op-attr-list", attrList);

			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 파일에 정의된 QID 내용을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectQid(SessionVo session, CdSelectQidPara para, Map<String, Object> parameters) throws Exception {

		QidDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHomeDeployConfSql(para.getQidFileName()));

		try {
			tran.start();

			Map<String, Object> wherePara = new HashMap<String, Object>(parameters);
			parameters.put("userNo", session.getUserNo());

			if (tran.getSqlBean(para.getQid()) == null) {
				throw new NotFoundException("qid", para.getQid());
			}

			return tran.selectQid2Res(para.getQid(), wherePara);

		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 시스템 변수의 값을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectVarList(SessionVo session, SelectVarPara obj) throws Exception {
		Map<String, Object> wherePara = this.makeWherePara(obj);
		wherePara.put("useYn", "Y");
		return ClassDaoEx.SelectDatas(FX_CF_VAR.class, wherePara, FX_CF_VAR.class);
	}

	/**
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object sendFxEvent(SessionVo session, SendEventPara para) throws Exception {

		if (FxServiceImpl.fxService == null) {
			throw new Exception("서비스가 실행중이 아닙니다.");
		} else {
			FxServiceImpl.fxService.sendEvent(new TargetFxEvent(para.getType(), para.getTarget()), true, true);
		}

		return para;

	}

	public Object sendReloadSignal(SessionVo session, SendReloadSignalPara para) throws Exception {

		if (FxServiceImpl.fxService == null) {
			throw new Exception("서비스가 실행중이 아닙니다.");
		} else {
			FxServiceImpl.fxService.sendEvent(new ReloadSignal(ReloadType.getReloadType(para.getType())), true, true);
		}

		return para;

	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createQidDao();
	}
}

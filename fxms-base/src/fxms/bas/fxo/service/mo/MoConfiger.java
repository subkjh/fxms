package fxms.bas.fxo.service.mo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.dao.FxConfDao;
import fxms.bas.dbo.cd.OpCode;
import fxms.bas.func.OpCodePool;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exception.MoNotFoundException;
import fxms.bas.mo.property.MoNeedManager;
import subkjh.bas.log.Logger;
import subkjh.bas.user.User;
import subkjh.bas.user.dbo.UserLogDbo;

public class MoConfiger {

	@SuppressWarnings("rawtypes")
	public void doSync(Mo mo) throws Exception {

		Logger.logger.info("{}", mo);

		Exception ex = null;

		OpCode opcode = OpCodePool.getPool().getOpCode("mo-config-sync");

		UserLogDbo log = new UserLogDbo(User.USER_NO_SYSTEM, User.USER_ID_SYSTEM, 0, "mo-config-sync");
		log.setSrtDate(FxApi.getDate(0));

		MoConfig moConfig = new MoConfig(mo);

		for (Adapter adapter : getAdapter(Adapter.class, mo)) {
			try {
				Logger.logger.info("getConfigChildren {}", adapter.getClass().getName());
				adapter.getConfigChildren(moConfig);
			} catch (Exception e) {
				Logger.logger.fail("{} {} ", mo, e.getMessage());
				ex = e;
				break;
			}
		}

		if (ex == null && moConfig != null && moConfig.sizeAll() > 0) {
			MoApi.getApi().setMoChildren(moConfig);
		}

		log.setEndDate(FxApi.getDate(0));
		log.setRetNo(0);
		log.setUserName(User.USER_NAME_SYSTEM);
		log.setInPara("mo-no=" + mo.getMoNo());

		if (ex != null) {
			log.setRetNo(-1);
			log.setRetMsg(ex.getClass().getName());
		}

		try {
			FxConfDao.getDao().insertUsrLog(log, opcode);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> getAdapter(Class<T> classOfAdapter, Mo mo) throws Exception {

		Mo mgrMo = mo;

		if (mo instanceof MoNeedManager) {
			long mgrMoNo = ((MoNeedManager) mo).getManagerMoNo();
			mgrMo = MoApi.getApi().getMo(mgrMoNo);
			if (mgrMo == null) {
				throw new MoNotFoundException(mgrMoNo);
			}
		}

		if (mo.getMoNo() == mgrMo.getMoNo()) {
			Logger.logger.info("{}", mo);
		} else {
			Logger.logger.info("{} -> {}", mgrMo, mo);
		}

		List<Adapter> adapterList = (List<Adapter>) FxActorParser.getParser().getActorList(classOfAdapter);

		List<Adapter> retList = new ArrayList<Adapter>();
		for (Adapter adapter : adapterList) {
			if (adapter.match(mgrMo)) {
				retList.add(adapter);
			}
		}

		return (List<T>) retList;

	}
}

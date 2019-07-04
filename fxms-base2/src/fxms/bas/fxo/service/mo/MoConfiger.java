package fxms.bas.fxo.service.mo;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import fxms.bas.api.CoApi;
import fxms.bas.api.MoApi;
import fxms.bas.co.OpCode;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exp.MoNotFoundException;
import fxms.bas.mo.property.MoNeedManager;

public class MoConfiger {

	@SuppressWarnings("rawtypes")
	public void doSync(Mo mo) throws Exception {

		Logger.logger.info("{}", mo);

		long mstimeStart = System.currentTimeMillis();
		Exception ex = null;
		OpCode opcode = CoApi.getApi().getOpCode("mo-config-sync");
		StringBuffer inPara = new StringBuffer();
		int retNo = 0;
		String retMsg = null;

		inPara.append("mo-no=");
		inPara.append(mo.getMoNo());

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

		if (ex != null) {
			retNo = -1;
			retMsg = ex.getClass().getName();
		}

		CoApi.getApi().logUserOp(User.USER_NO_SYSTEM, null, opcode, inPara.toString(), null, retNo, retMsg, mstimeStart);

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
			if (adapter.getFxPara().match(mgrMo)) {
				retList.add(adapter);
			}
		}

		return (List<T>) retList;

	}
}

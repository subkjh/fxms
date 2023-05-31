package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.VarApi;
import fxms.bas.impl.dpo.BroadcastDfo;
import fxms.bas.impl.dpo.var.SelectVarDfo;
import fxms.bas.impl.dpo.var.SetVarDfo;
import fxms.bas.impl.dpo.var.UpdateReloadTimeDfo;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.FxVarVo;
import subkjh.bas.co.log.Logger;

/**
 * 환경변수를 관리하는 API
 * 
 * @author subkjh
 *
 */
public class VarApiDfo extends VarApi {

	@Override
	public List<FxVarVo> getVars(Map<String, Object> para) throws Exception {
		return new SelectVarDfo().selectVar(para);
	}

	@Override
	public void updateVarInfo(String varName, Map<String, Object> para) throws Exception {
		try {

			new SetVarDfo().setVar(varName, para);

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean setVarValue(String varName, Object varVal, boolean broadcast) throws Exception {

		try {
			boolean ret = new SetVarDfo().setVar(varName, makePara("varVal", varVal));
			if (broadcast) {
				new BroadcastDfo().broadcast(new ReloadSignal(ReloadType.Var));
			}
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean setTimeUpdated(Enum<?> type, long hstime) throws Exception {

		try {
			return new UpdateReloadTimeDfo().updateReloadTime(type.name(), hstime);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean enable(String varName, boolean enable) throws Exception {
		this.updateVarInfo(varName, FxApi.makePara("useYn", enable ? "Y" : "N"));
		return true;
	}
}

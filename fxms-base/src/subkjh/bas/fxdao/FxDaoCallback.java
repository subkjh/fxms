package subkjh.bas.fxdao;

import subkjh.bas.fxdao.control.FxDaoExecutor;

public interface FxDaoCallback<DATA> {

	public void onCall(FxDaoExecutor tran, DATA data) throws Exception;
}

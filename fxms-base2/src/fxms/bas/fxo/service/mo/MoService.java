package fxms.bas.fxo.service.mo;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.service.FxService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.po.PsVo;
import fxms.bas.po.filter.ValuePeeker;

public interface MoService extends FxService {

	public List<PsVo> getValue(long moNo, String psCodes[]) throws RemoteException, Exception;

	public Mo getConfig(long moNo) throws RemoteException, Exception;

	public MoConfig getConfigChildren(MoConfig moConfig, String... moClasses) throws RemoteException, Exception;

	public void setValue(Mo mo, String method, Map<String, Object> parameters) throws RemoteException, Exception;

	/**
	 * 동기화를 요청한다.
	 * 
	 * @param mo
	 *            동기화 할 MO
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void requestSync(Mo mo) throws RemoteException, Exception;

	/**
	 * 수집된 성능을 엿보는 클래스를 설정한다.
	 * 
	 * @param moNo
	 * @param moInstance
	 * @param psCode
	 * @param peeker
	 * @param add
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void setPeekValue(long moNo, String moInstance, String psCode, ValuePeeker peeker, boolean add)
			throws RemoteException, Exception;

}

package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;

import fxms.bas.vo.PsStatReqVo;

/**
 * FxMS Application Service
 * 
 * @author subkjh
 *
 */
public interface AppService extends FxService {

	/**
	 * 수집한 데이터에 대한 통계 생성을 요청한다.
	 * 
	 * @param reqList
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean requestMakeStat(List<PsStatReqVo> reqList) throws RemoteException, Exception;

	/**
	 * 통계가 생성되었음을 통보한다.
	 * 
	 * @param req
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean responseMakeStat(PsStatReqVo req) throws RemoteException, Exception;

	/**
	 * 저장소를 확인하여 필요한 경우 저장소를 생성한다.
	 * 
	 * @param memo 메모
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String checkStorage(String memo) throws RemoteException, Exception;

	/**
	 * 통계 데이터를 생성한다.
	 * 
	 * @param psTbl
	 * @param psKindName
	 * @param psDtm
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws RemoteException, Exception;

}

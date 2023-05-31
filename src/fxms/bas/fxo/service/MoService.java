package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.mo.Mo;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.SyncMo;

/**
 * 
 * @author subkjh
 *
 */
public interface MoService extends FxService {

	/**
	 * 관리대상을 추가한다.
	 * 
	 * @param userNo    사용자번호
	 * @param moClass   관리대상분류
	 * @param datas     관리대상 데이터 
	 * @param memo      메모
	 * @param broadcast 방송여부
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Mo addMo(int userNo, String moClass, Map<String, Object> datas, String memo, boolean broadcast)
			throws RemoteException, Exception;

	/**
	 * 관리대상을 수정한다.
	 * 
	 * @param moNo      관리대상번호
	 * @param datas     수정할 데이터
	 * @param memo      메모
	 * @param broadcast 방송여부
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Mo updateMo(int userNo, long moNo, Map<String, Object> datas, boolean broadcast)
			throws RemoteException, Exception;

	/**
	 * 관리대상을 삭제한다.
	 * 
	 * @param moNo      관리대상번호
	 * @param memo      메모
	 * @param broadcast 방송여부
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Mo deleteMo(int userNo, long moNo, String memo, boolean broadcast) throws RemoteException, Exception;

	/**
	 * 관리대상으로부터 가지고 있는 모든 데이터를 수집한다.
	 * 
	 * @param moNo
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<PsVoRaw> getRtValues(long moNo) throws RemoteException, Exception;

	/**
	 * 관리대상의 환경 정보를 가져온다.
	 * 
	 * @param moNo   관리대상번호
	 * @param now    실시간처리여부
	 * @param update 저장소 기록여부
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SyncMo sync(long moNo, boolean now, boolean update) throws RemoteException, Exception;

	/**
	 * 관리대상에 값을 설정한다.
	 * 
	 * @param mo     관리대상번호
	 * @param method 기능
	 * @param datas  설정데이터
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean setupMo(long moNo, String method, Map<String, Object> datas) throws RemoteException, Exception;

	/**
	 * 관리대상 목록을 조회한다.
	 * 
	 * @param para
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Mo> getMoList(Map<String, Object> para) throws RemoteException, Exception;

	/**
	 * 
	 * @param <T>
	 * @param para
	 * @param classOfMo
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public <T extends Mo> List<T> getMoList(Map<String, Object> para, Class<T> classOfMo)
			throws RemoteException, Exception;

	/**
	 * 관리대상을 조회한다.
	 * 
	 * @param moNo
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Mo getMo(long moNo) throws RemoteException, Exception;
}

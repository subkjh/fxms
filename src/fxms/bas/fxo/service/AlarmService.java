package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.vo.Alarm;
import fxms.bas.vo.AlarmCfg;
import fxms.bas.vo.AlarmClearEvent;
import fxms.bas.vo.AlarmCode;
import fxms.bas.vo.AlarmOccurEvent;

/**
 * 
 * @author subkjh
 *
 */
public interface AlarmService extends FxService {

	/**
	 * 알람을 해제한다.
	 * 
	 * @param alarmNo
	 * @param userNo
	 * @param releaseMemo
	 * @param eventTime
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Alarm clearAlarm(AlarmClearEvent event) throws RemoteException, Exception;

	/**
	 * 
	 * @param event
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Alarm fireAlarm(AlarmOccurEvent event) throws RemoteException, Exception;

	/**
	 * 알람을 생성한다.
	 * 
	 * @param moNo       관리대상번호
	 * @param alcdNo     알람코드번호
	 * @param moInstance 인스턴스
	 * @return 생성한 알람
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Alarm fireAlarm(long moNo, int alcdNo, Object moInstance) throws RemoteException, Exception;

	/**
	 * 알람을 조회한다. 해제된 알람을 포함한다.
	 * 
	 * @param alarmNo 알람번호
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Alarm getAlarm(long alarmNo) throws RemoteException, Exception;

	/**
	 * 경보발생조건 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @param para 조건
	 * @return
	 * @throws Exception
	 */
	public List<AlarmCfg> getAlarmCfgs(Map<String, Object> para) throws RemoteException, Exception;

	/**
	 * 경보코드 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @return 경보코드목록
	 */
	public List<AlarmCode> getAlarmCodes() throws RemoteException, Exception;

	/**
	 * 알람 목록을 조회한다. 해제된 알람을 포함한다.
	 * 
	 * @param startDate 발생조회시작일시
	 * @param endDate   발생조회종료일시
	 * @param para      조건
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Alarm> getAlarms(long startDate, long endDate, Map<String, Object> para)
			throws RemoteException, Exception;

	/**
	 * 현재 알람을 조회한다.
	 * 
	 * @param para 조건
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Alarm> getCurAlarms(Map<String, Object> para) throws RemoteException, Exception;
}

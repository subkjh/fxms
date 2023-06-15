package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.api.ValueApi.StatFunction;
import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import fxms.bas.vo.PsVoRawList;

/**
 * 수집한 내용을 기록하는 서비스
 * 
 * @author subkjh
 *
 */
public interface ValueService extends FxService {

	/**
	 * 수집한 값을 기록한다.
	 * 
	 * @param voList     수집된 값
	 * @param checkAlarm 알람 확인 여부
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void addValue(PsVoRawList voList, boolean checkAlarm) throws RemoteException, Exception;

	/**
	 * 
	 * @param moNo
	 * @param moInstance
	 * @param psId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PsValueComp getCurValue(long moNo, String moInstance, String psId) throws RemoteException, Exception;

	/**
	 * 수집된 성능 값을 조회한다.
	 * 
	 * @param moNo
	 * @param psId
	 * @param psKindName
	 * @param startDtm
	 * @param endDtm
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<PsValueSeries> getSeriesValues(long moNo, String psId, String psKindName, String psKindCols[],
			long startDtm, long endDtm) throws RemoteException, Exception;

	/**
	 * 수집데이터의 통계데이터(StatFunction)를 조회한다.
	 * 
	 * @param psId       수집항목
	 * @param psKindName 통계종류
	 * @param startDtm   조회시작일시
	 * @param endDtm     조회종료일시
	 * @param statFunc   통계함수
	 * @return 관리대상을 키로한 값
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<Long, Number> getStatValue(String psId, String psKindName, long startDtm, long endDtm,
			StatFunction statFunc) throws RemoteException, Exception;

	/**
	 * 
	 * @param moNo
	 * @param psKindName
	 * @param startDtm
	 * @param endDtm
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<PsValues> getValues(long moNo, String psKindName, long startDtm, long endDtm)
			throws RemoteException, Exception;

	/**
	 * 
	 * @param moNo        관리대상
	 * @param psId        수집항목
	 * @param psKindName  통계 종류
	 * @param psKindField 통계 종류 컬럼
	 * @param startDtm
	 * @param endDtm
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<PsValues> getValues(long moNo, String moInstance, String psId, String psKindName, String psKindCol, long startDtm,
			long endDtm) throws RemoteException, Exception;

	/**
	 * 성능항목을 수집하는 모든 관리대상에 대한 수집데이터를 조회한다.
	 * 
	 * @param psId       성능항목
	 * @param psKindName 데이터 종류
	 * @param statFunc   컬럼
	 * @param startDtm   조회시작시간
	 * @param endDtm     조회종료시간
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<PsValues> getValues(String psId, String psKindName, String psKindCol, long startDtm, long endDtm)
			throws RemoteException, Exception;
}

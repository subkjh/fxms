package fxms.bas.api;

import java.rmi.RemoteException;
import java.util.List;

import fxms.bas.vo.PsStatReqVo;
import subkjh.bas.co.log.LOG_LEVEL;

/**
 * AppService에 대응하는 API
 * 
 * @author subkjh
 *
 */
public abstract class AppApi extends FxApi {

	/** use DBM */
	public static AppApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static AppApi getApi() {
		if (api != null)
			return api;

		api = makeApi(AppApi.class);

		return api;
	}

	/**
	 * 저장소를 확인하여 필요한 경우 저장소를 생성한다.
	 * 
	 * @param memo 메모
	 * @throws Exception
	 */
	public abstract String checkStorage(String memo) throws Exception;

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void reload(Enum<?> type) throws Exception {
	}

	/**
	 * 수집한 데이터에 대한 통계 생성을 요청한다.
	 * 
	 * @param reqList
	 * @throws Exception
	 */
	public abstract boolean requestMakeStat(List<PsStatReqVo> reqList) throws Exception;

	/**
	 * 통계가 생성되었음을 통보한다.
	 * 
	 * @param req
	 * @throws RemoteException
	 */
	public abstract boolean responseMakeStat(PsStatReqVo req) throws Exception;

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
	public abstract int generateStatistics(String psTbl, String psKindName, long psDtm) throws Exception;

	/**
	 * 입력된 일자와 같은 조건의 일자를 조회한다.
	 * 
	 * @param date  기준일자
	 * @param count 일자건수
	 * @return
	 * @throws Exception
	 */
	public abstract List<String> getSameDays(String date, int count) throws Exception;

}

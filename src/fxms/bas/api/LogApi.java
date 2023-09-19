package fxms.bas.api;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;

public abstract class LogApi extends FxApi {

	/** use DBM */
	public static LogApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 *
	 * @return DBM
	 */
	public synchronized static LogApi getApi() {
		if (api != null)
			return api;

		api = makeApi(LogApi.class);

		return api;
	}

	/**
	 * 시스템에서 발생된 로그를 기록한다.
	 * 
	 * @param name       이벤트명
	 * @param recvMstime 수신일시
	 * @param procCnt    처리건수
	 * @param ex         오류예외
	 */
	public void addSystemLog(Enum<?> name, long recvMstime, int procCnt, Exception ex) {

		String errJson = "";

		if (ex != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", ex.getClass().getName());
			map.put("message", ex.getMessage());

			errJson = FxmsUtil.toJson(map);
		}

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("fxsvcName", FxServiceImpl.serviceName);
		para.put("evtName", name.name());
		para.put("recvDtm", DateUtil.getDtm(recvMstime));
		para.put("procDtm", DateUtil.getDtm());
		para.put("procCnt", procCnt);
		para.put("okYn", ex == null ? "Y" : "N");
		para.put("errJson", errJson);

		try {
			doAddSystemLog(para);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	/**
	 * 
	 * @param cronRunNo 크론실행번호
	 * @param isOk      성공여부
	 * @param spentTime 소요시간(milliseconds)
	 * @param outPara   결과물
	 */
	public void closeCronLog(long cronRunNo, boolean isOk, int spentTime, Object outPara) {
		try {
			doCloseCronLog(cronRunNo, isOk, spentTime, outPara);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

	/**
	 * 
	 * @param userNo
	 * @param userName
	 * @param sessionId
	 * @param opId
	 * @param inPara
	 * @param outRet
	 * @param retNo
	 * @param retMsg
	 * @param mstimeStart
	 * @param opObjType
	 * @param opObjNo
	 * @param opObjName
	 */
	public abstract void logUserWorkHst(int userNo, String userName, String sessionId, String opId, String inPara,
			String outRet, int retNo, String retMsg, long mstimeStart, String opObjType, Object opObjNo,
			String opObjName);

	@Override
	public void onCreated() throws Exception {

	}

	/**
	 * 
	 * @param cronName 실행크론명
	 * @param inPara   입력인자
	 * @return 크론실행번호
	 */
	public long openCronLog(String cronName, Object inPara) {
		try {
			return doOpenCronLog(cronName, inPara);
		} catch (Exception e) {
			Logger.logger.error(e);
			return -1;
		}
	}

	@Override
	public void reload(Enum<?> type) throws Exception {

	}

	/**
	 * 
	 * @param para
	 * @throws Exception
	 */
	protected abstract void doAddSystemLog(Map<String, Object> para) throws Exception;

	/**
	 * 
	 * @param cronRunNo
	 * @param isOk
	 * @param spentTime
	 * @param outPara
	 * @throws Exception
	 */
	protected abstract void doCloseCronLog(long cronRunNo, boolean isOk, int spentTime, Object outPara)
			throws Exception;

	/**
	 * 
	 * @param cronName
	 * @param inPara
	 * @return
	 * @throws Exception
	 */
	protected abstract long doOpenCronLog(String cronName, Object inPara) throws Exception;
}

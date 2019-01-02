package subkjh.bas.user;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.service.app.AppServiceImpl;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.lang.Lang;
import subkjh.bas.log.Logger;
import subkjh.bas.user.dao.UserDao;
import subkjh.bas.user.dbo.OpDbo;
import subkjh.bas.user.dbo.UserLogDbo;
import subkjh.bas.user.exception.NotLoginException;
import subkjh.bas.user.exception.OpDenyException;
import subkjh.bas.user.process.UserProcMgr;

/**
 * 운용자 작업 기본 클래스
 * 
 * @author subkjh
 * 
 * @param <OUT>
 *            출력 데이터 유형
 */
public abstract class UserProc<OUT> {

	public User getUser() {
		return user;
	}

	/** 처리 시작시간 */
	private long mstimeStart;

	/** 운용자 정보 */
	private SessionVo user;

	public UserProc() {

	}

	/**
	 * 작업을 실행합니다.<br>
	 * 1. 권한을 확인합니다. 권한이 없을 경우 Exception을 발생합니다.<br>
	 * 2. 작업을 수행합니다. <br>
	 * 3. 로그를 남깁니다. <br>
	 * 
	 * @param service
	 *            서비스
	 * @param userId
	 *            운용자ID<br>
	 *            탭(0x08)로 구분하여 다음에 실제 UI사용자의 IP주소를 넘길 수 있습니다. 이때 접속 IP주소로 사용되게
	 *            됩니다.
	 * @return 출력 인수
	 * @throws Exception
	 */
	public OUT process(AppServiceImpl appService, String sessionId) throws OpDenyException, Exception {

		String retMsg = "";

		Thread.currentThread().setName(sessionId + "-" + getClass().getSimpleName());

		mstimeStart = System.currentTimeMillis();

		user = appService.getUser(sessionId);

		if (user == null) {
			throw new NotLoginException(sessionId);
		}

		if (user.isAccesable(getOpNo()) == false) {
			log(null, -1, Lang.get("접근 권한 없음"));
			throw new OpDenyException(user.getUserName(), getOpNo());
		}

		try {
			OUT ret = process();
			log(ret, 0, null);
			retMsg = "OK";
			return ret;
		} catch (Exception e) {
			retMsg = e.getMessage();
			Logger.logger.error(e);
			log(null, -1, e.getMessage());
			throw e;
		} finally {
			Logger.logger.trace(retMsg);
		}
	}

	/**
	 * 처리를 완료 한 후 로그를 남기기 위해 사용됩니다.<br>
	 * 남기고자 하는 입력 로그를 나타냅니다.
	 * 
	 * @return 입력된 인수 값
	 */
	protected abstract String getInPara();

	protected long getMoNo() {
		return 0;
	}

	protected int getOpNo() {
		OpDbo op = UserProcMgr.getMgr().getOp(this.getClass());

		return op == null ? 0 : op.getOpNo();
	}

	protected abstract String getOutRet();

	protected String getPsCode() {
		return null;
	}

	protected double getPsValue() {
		return 0;
	}

	/**
	 * 실제 처리하는 부분
	 * 
	 * @return 출력 인수
	 * @throws Exception
	 */
	protected abstract OUT process() throws Exception;

	/**
	 * 로그를 기록합니다.
	 * 
	 * @param ret
	 *            결과
	 * @param errorNo
	 *            오류번호
	 * @param errmsg
	 *            오류메세지
	 */
	private void log(OUT ret, int errorNo, String errmsg) {

		int opNo = getOpNo();
		if (opNo <= 0)
			return;

		UserLogDbo log = new UserLogDbo();

		log.setEndDate(FxApi.getDate(0));
		log.setInPara(getInPara());
		log.setOpNo(getOpNo());
		log.setOpSeqno(0);
		log.setOutRet(getOutRet());
		log.setRetMsg(errmsg);
		log.setRetNo(errorNo);
		log.setSessionId(user.getSessionId());
		log.setSrtDate(FxApi.getDate(mstimeStart));
		log.setUserNo(user.getUserNo());

		new UserDao().writeLog(log);

	}

}

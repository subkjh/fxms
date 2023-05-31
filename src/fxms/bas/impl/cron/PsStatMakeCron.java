package fxms.bas.impl.cron;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.AppApi;
import fxms.bas.api.PsApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.co.CoCode;
import fxms.bas.co.CoCode.CRE_ST_CD;
import fxms.bas.cron.Crontab;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.fxo.adapter.PsStatAfterAdapter;
import fxms.bas.impl.dbo.StatMakeReqDbo;
import fxms.bas.impl.dbo.StatMakeUpdateDbo;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsStatReqVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 수집된 데이터에 대한 통계 데이터를 생성하는 스레드
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "AppService", descr = "수집한 데이터에 대한 통계를 생성한다.")
public class PsStatMakeCron extends Crontab {

	@FxAttr(value = "0 * * * *", description = "매시간 저장소 테이블을 확인한다.")
	private String schedule;

	private String yyyymmdd = "";

	private final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	public PsStatMakeCron() {

	}

	@Override
	public void start() throws Exception {

		makeTables();

		try {
			makeStatAll();
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	protected String getSchedule() {
		return schedule;
	}

	/**
	 * 통계가 생성된 후 호출된다.
	 * 
	 * @param req
	 */
	private void afterStat(StatMakeReqDbo req) {

		List<PsStatAfterAdapter> adapters = AdapterApi.getApi().getAdapters(PsStatAfterAdapter.class);

		for (PsStatAfterAdapter a : adapters) {
			try {
				a.afterStat(req.getPsTbl(), req.getPsDataCd(), req.getPsDtm());
			} catch (Exception e) {
				Logger.logger.error(e);
				AlarmApi.getApi().fireAlarm(null, req.getKey(), ALARM_CODE.FXMSERR_PS_STAT_AFTER_ADAPTER.getAlcdNo(),
						null, null, null);
			}
		}
	}

	/**
	 * 통계 생성
	 * 
	 * @param req
	 * @throws Exception
	 */
	private void makeStat(StatMakeReqDbo req) throws Exception {

		String errmsg = null;
		StatMakeUpdateDbo dbo = new StatMakeUpdateDbo();
		dbo.setPsCreReqNo(req.getPsCreReqNo());
		try {

			// 처리중 기록
			dbo.setChgDtm(DateUtil.getDtm());
			dbo.setChgUserNo(0);
			dbo.setCreStrtDtm(DateUtil.getDtm());
			dbo.setCreStCd(CoCode.CRE_ST_CD.Processing.getCode());
			ClassDaoEx.UpdateOfClass(StatMakeUpdateDbo.class, dbo);

			try {

				// 실제 통계 생성
				int size = AppApi.getApi().generateStatistics(req.getPsTbl(), req.getPsDataCd(), req.getPsDtm());

				dbo.setRowSize(size);
				dbo.setOkYn(true);

			} catch (Exception e) {
				Logger.logger.error(e);
				errmsg = e.getClass().getSimpleName() + " : " + e.getMessage();
				dbo.setOkYn(false);
				dbo.setRowSize(-1);
			}

			// 처리 완료 기록
			dbo.setCreStCd(CoCode.CRE_ST_CD.Completed.getCode());
			dbo.setRetMsg(errmsg);
			dbo.setCreEndDtm(DateUtil.getDtm());

			ClassDaoEx.UpdateOfClass(StatMakeUpdateDbo.class, dbo);

			try {
				requestStat2nd(req.getPsTbl(), req.getPsDataCd(), req.getPsDtm());
			} catch (Exception e) {
				Logger.logger.error(e);
			}

			try {
				afterStat(req);
			} catch (Exception e) {
				Logger.logger.error(e);
			}

		} catch (Exception e) {
			AlarmApi.getApi().fireAlarm(null, req.getKey(), ALARM_CODE.FXMSERR_PS_STAT.getAlcdNo(), null, null, null);
			Logger.logger.error(e);
			throw e;
		}
	}

	private void makeStatAll() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("creblDtm <= ", DateUtil.getDtm()); // 생성 가능시간이 지난 경우만 조회
		para.put("creStCd", CRE_ST_CD.Ready.getCode()); // 대기중인 성능만
		List<StatMakeReqDbo> reqList = ClassDaoEx.SelectDatas(StatMakeReqDbo.class, para);

		Logger.logger.info("size={}", reqList.size());

		for (StatMakeReqDbo req : reqList) {

			makeStat(req); // 통계생성

			try {
				AppApi.getApi().responseMakeStat(new PsStatReqVo(req.getPsTbl(), req.getPsDataCd(), req.getPsDtm())); // 생성결과
																														// 통보
			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}
	}

	/**
	 * 보관 기간이 지난 테이블은 삭제하고 필요한 테이블은 생성한다.
	 */
	private void makeTables() {
		String today = YYYYMMDD.format(new Date(System.currentTimeMillis()));

		if (yyyymmdd.equals(today) == false) {

			try {

				AppApi.getApi().checkStorage(today);

				yyyymmdd = today;

			} catch (Exception e) {
				Logger.logger.error(e);
			}

		}
	}

	/**
	 * 생성된 통계를 이용하여 다음 통계 생성을 요청한다.
	 * 
	 * @param psTable
	 * @param psKindSrc
	 * @param psDtm
	 * @throws Exception
	 */
	private void requestStat2nd(String psTable, String psKindSrc, long psDtm) throws Exception {

		Logger.logger.info("psTable={}, psKindSrc={}, psDtm={}", psTable, psKindSrc, psDtm);

		List<PsStatReqVo> reqList = new ArrayList<PsStatReqVo>();
		List<PsKind> list = PsApi.getApi().getPsKind2Dst(PsApi.getApi().getPsKind(psKindSrc));
		for (PsKind psKind : list) {
			reqList.add(new PsStatReqVo(psTable, psKind.getPsKindName(), psKind.getHstimeStart(psDtm)));
		}

		AppApi.getApi().requestMakeStat(reqList);
	}
}

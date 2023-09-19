package fxms.bas.impl.cron;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.AlarmApi;
import fxms.bas.api.AppApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.PsApi;
import fxms.bas.co.ALARM_CODE;
import fxms.bas.co.CoCode;
import fxms.bas.co.CoCode.CRE_ST_CD;
import fxms.bas.cron.Crontab;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.fxo.adapter.PsStatAfterAdapter;
import fxms.bas.impl.api.AppApiDfo;
import fxms.bas.impl.dbo.StatMakeUpdateDbo;
import fxms.bas.impl.dbo.all.FX_PS_STAT_CRE;
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

	public static void main(String[] args) {
		try {
			AppApi.api = new AppApiDfo();
			new PsStatMakeCron().start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 통계 생성 가능 시간이 있기 때문에 매분마다 처리해도 된다.
	@FxAttr(value = "* * * * *", description = "매분마다 통계 테이블 생성한다.")
	private String schedule;

	public PsStatMakeCron() {

	}

	@Override
	public void start() throws Exception {

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
	private void afterStat(FX_PS_STAT_CRE req) {

		List<String> psIds = PsApi.getApi().getPsIds(req.getPsTbl());
		int alcdNo = ALARM_CODE.fxms_error_ps_stat_adapter.getAlcdNo();
		List<PsStatAfterAdapter> adapters = AdapterApi.getApi().getAdapters(PsStatAfterAdapter.class);

		for (PsStatAfterAdapter a : adapters) {
			try {
				a.afterStat(req.getPsTbl(), psIds, req.getPsDataCd(), req.getPsDtm());
			} catch (Exception e) {
				Logger.logger.error(e);
				AlarmApi.getApi().fireAlarm(null, getAlarmKey(req), alcdNo, null, null, null);
			}
		}
	}

	private String getAlarmKey(FX_PS_STAT_CRE req) {
		return req.getPsTbl() + "_" + req.getPsDataCd() + "_" + req.getPsDtm();
	}

	/**
	 * 통계 생성
	 * 
	 * @param req
	 * @throws Exception
	 */
	private void makeStat(FX_PS_STAT_CRE req) throws Exception {

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
			AlarmApi.getApi().fireAlarm(null, getAlarmKey(req), ALARM_CODE.fxms_error_ps_stat.getAlcdNo(), null, null,
					null);
			Logger.logger.error(e);
			throw e;
		}
	}

	@Override
	public Object getOutPara() {
		return FxApi.makePara("processedCount", this.processedCount);
	}

	private int processedCount = 0;

	private void makeStatAll() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("creblDtm <= ", DateUtil.getDtm()); // 생성 가능시간이 지난 경우만 조회
		para.put("creStCd", CRE_ST_CD.Ready.getCode()); // 대기중인 성능만
		List<FX_PS_STAT_CRE> reqList = ClassDaoEx.SelectDatas(FX_PS_STAT_CRE.class, para);

		reqList.sort(new Comparator<FX_PS_STAT_CRE>() {
			@Override
			public int compare(FX_PS_STAT_CRE o1, FX_PS_STAT_CRE o2) {
				return o1.getPsDtm() > o2.getPsDtm() ? 1 : -1;
			}
		});

		this.processedCount = 0;
		Logger.logger.info("size={}", reqList.size());

		for (FX_PS_STAT_CRE req : reqList) {

//			System.out.println(FxmsUtil.toJson(req));

			makeStat(req); // 통계생성

			try {
				// 생성결과 통보
				AppApi.getApi().responseMakeStat(new PsStatReqVo(req.getPsTbl(), req.getPsDataCd(), req.getPsDtm()));
			} catch (Exception e) {
				Logger.logger.error(e);
			}

			// 많이 적재되어 있다면 50개씩만 처리한다.
			if (++this.processedCount == 50)
				break;

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

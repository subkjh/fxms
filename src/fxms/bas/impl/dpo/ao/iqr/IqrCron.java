package fxms.bas.impl.dpo.ao.iqr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AlarmApi;
import fxms.bas.api.AppApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.api.VarApi;
import fxms.bas.cron.Crontab;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.impl.api.AlarmApiDfo;
import fxms.bas.impl.api.AppApiDfo;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.bas.impl.dbo.all.FX_PS_STAT_CRE;
import fxms.bas.impl.dpo.ao.AlcdMap;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "AppService", descr = "IQR이 적용된 성능에 대한 알람을 확인한다.")
public class IqrCron extends Crontab {

	public static void main(String[] args) throws Exception {

		AppApi.api = new AppApiDfo();
		ValueApi.api = new ValueApiDfo();
		MoApi.api = new MoApiDfo();
		AlarmApi.api = new AlarmApiDfo();
		AlarmApi.getApi().reload(ReloadType.Alarm);

		IqrCron cron = new IqrCron();
		try {
			cron.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FxAttr(name = "schedule", description = "실행계획", value = "0,5,10,15,20,25,30,35,40,45,50,55 * * * *")
	private String schedule;

	private final String VAR_NAME = "fxms.iqr.check.time";

	public IqrCron() {

	}

	@Override
	public void start() throws Exception {

		// 1. IQR 적용 성능 가져오기
		List<String> psIds = AlcdMap.getMap().getPsIdForIQR();
		Logger.logger.debug("psIds={}", psIds);
		if (psIds.size() == 0)
			return;

		// 2. 최종 처리 시간 가져오기
		long hstime = getLastTime();

		// 3. 처리할 대상 가져오기
		List<CheckIqrDto> targets = getTargets(hstime, psIds);
		if (targets.size() == 0)
			return;

		// 4. 처리하기
		AlarmMakeIqrDfo dfo = new AlarmMakeIqrDfo();
		for (CheckIqrDto dto : targets) {
			dfo.check(dto);
			if (dto.getPsDtm() > hstime) {
				hstime = dto.getPsDtm();
			}
		}

		// 5.처리시간 기록하기
		VarApi.getApi().setVarValue(VAR_NAME, hstime, false);
	}

	@Override
	protected String getSchedule() {
		return schedule;
	}

	private Long getLastTime() throws Exception {

		long hstime = VarApi.getApi().getVarValue(VAR_NAME, 0L);

		if (hstime == 0) {
			Map<String, Object> varInfo = new HashMap<String, Object>();
			varInfo.put("varGrpName", "TIME");
			varInfo.put("varDispName", "IQR 최종 처리 시간");
			varInfo.put("varDesc", "IQR알람 최종 처리 시간을 나타낸다.");
			VarApi.getApi().updateVarInfo(VAR_NAME, varInfo);

			hstime = DateUtil.getDtm(System.currentTimeMillis() - 30 * 60000L);
		}

		return hstime;
	}

	private List<CheckIqrDto> getTargets(long hstime, List<String> psIds) throws Exception {

		List<CheckIqrDto> ret = new ArrayList<>();
		CheckIqrDto dto;
		PsItem psItem;

		PsKind psKind = PsApi.getApi().getPsKind(PsKind.PSKIND_15M);
		long psDtm = psKind.getHstimeStart(hstime);
		

		ClassDaoEx dao = ClassDaoEx.open();

		for (String psId : psIds) {
			try {
				psItem = PsApi.getApi().getPsItem(psId);
			} catch (Exception e) {
				continue;
			}

			List<FX_PS_STAT_CRE> list = dao.selectDatas(FX_PS_STAT_CRE.class, FxApi.makePara("psTbl",
					psItem.getPsTable(), "creStCd", "C", "psDtm >", psDtm, "okYn", "Y", "psDataCd", psKind.getPsKindName()));
			for (FX_PS_STAT_CRE data : list) {
				dto = new CheckIqrDto();
				dto.setPsDtm(data.getPsDtm());
				dto.setPsId(psId);
				dto.setPsKindName(data.getPsDataCd());
				ret.add(dto);
			}
		}

		dao.close();

		return ret;
	}

}

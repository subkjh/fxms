package fxms.bas.cron;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.impl.dao.StatMakeHourlyCronQid;
import subkjh.bas.BasCfg;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 알람발생 통계 데이터 생성 크론
 * 
 * @author subkjh
 * @since 2000.01
 *
 */
@FxAdapterInfo(service = "AlarmService", descr = "시간단위 알람 통계")
public class AlarmStatHourlyCron extends Crontab {

	@FxAttr(name = "schedule", description = "실행계획", value = "3 * * * *")
	private String schedule;

	public static void main(String[] args) {

		FxCfg.getCfg().setFxServiceName("AlarmService");
		AlarmStatHourlyCron cron = FxActorParser.getParser().getActor(AlarmStatHourlyCron.class);

		try {
			cron.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() throws Exception {

		QidDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(StatMakeHourlyCronQid.QUERY_XML_FILE));

		StatMakeHourlyCronQid qid = new StatMakeHourlyCronQid();

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("regDtm", DateUtil.getDtm());
			para.put("regUserNo", User.USER_NO_SYSTEM);
			para.put("chgDtm", DateUtil.getDtm());
			para.put("chgUserNo", User.USER_NO_SYSTEM);

			long mstime = System.currentTimeMillis() - 86400000L;

			for (long ms = mstime; ms <= System.currentTimeMillis(); ms += 86400000L) {
				para.put("stDate", DateUtil.getYmd(ms));
				tran.execute(qid.MAKE_ALARM_STAT_HH_ALCD, para);
				tran.execute(qid.MAKE_ALARM_STAT_HH_INLO, para);
				tran.commit();
			}

		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	@Override
	protected String getSchedule() {
		return schedule;
	}
}

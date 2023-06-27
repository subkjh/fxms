package fxms.bas.cron;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.impl.dao.StatMakeDailyCronQid;
import subkjh.bas.BasCfg;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.QidDaoEx;
import subkjh.dao.util.FxTableMaker;

/**
 * 통계 생성자<br>
 * fxms-filter-list.xml 파일에 정의되어 됨
 * 
 * @author SUBKJH-DEV
 *
 */
@FxAdapterInfo(service = "AlarmService", descr = "알람 통계")
public class AlarmStatDailyCron extends Crontab {

	@FxAttr(name = "schedule", description = "실행계획", value = "1 2 * * *")
	private String schedule;

	public static void main(String[] args) {
		AlarmStatDailyCron cron = new AlarmStatDailyCron();
		cron.getAdapterPara().put("qid", "MAKE_ALARM_STAT_MO");

		try {
			cron.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		FxTableMaker.initRegChgMap(User.USER_NO_SYSTEM, para);
		para.put("stDate", DateUtil.getYmd(System.currentTimeMillis() - 86400000L));

		StatMakeDailyCronQid qid = new StatMakeDailyCronQid();

		QidDaoEx.open(BasCfg.getHome(StatMakeDailyCronQid.QUERY_XML_FILE)) //
				.execute(qid.MAKE_ALARM_STAT_MO, para) //
				.close();

	}

	@Override
	protected String getSchedule() {
		return schedule;
	}
}

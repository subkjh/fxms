package fxms.bas.impl.dpo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dao.CommQid;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.QidDaoEx;

/**
 * 동일 조건의 일자를 조회한다.
 * 
 * @author subkjh
 *
 */
public class DateSelectSameDfo implements FxDfo<String, List<String>> {

	public static void main(String[] args) {
		DateSelectSameDfo dfo = new DateSelectSameDfo();
		System.out.println(dfo.selectSameDates("20230607", 10));
	}

	@Override
	public List<String> call(FxFact fact, String date) throws Exception {
		return selectSameDates(date, 12);
	}

	@SuppressWarnings("unchecked")
	public List<String> selectSameDates(String date, int count) {

		try {
			CommQid QID = new CommQid();
			QidDaoEx dao = QidDaoEx.open(FxCfg.getHome(CommQid.QUERY_XML_FILE));
			List<String> ret = dao.selectQid2Res(QID.select_date_same_dow,
					FxApi.makePara("date", date, "count", count));

			dao.close();
			return ret;
		} catch (Exception e) {
			List<String> ret = new ArrayList<>();
			long mstime = DateUtil.toMstime(date);
			for (int i = 1; i <= count; i++) {
				mstime -= (86400000L * i);
				ret.add(DateUtil.getYmdStr(mstime));
			}
			return ret;
		}
	}
}
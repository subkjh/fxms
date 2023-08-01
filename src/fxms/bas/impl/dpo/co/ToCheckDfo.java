package fxms.bas.impl.dpo.co;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_EV_CHECK;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.dao.ClassDaoEx;

/**
 * 운영자가 확인해야 할 내용을 기록한다.
 * 
 * @author subkjh
 *
 */
public class ToCheckDfo implements FxDfo<Map<String, String>, Boolean> {

	public static void main(String[] args) {

		ToCheckDfo dfo = new ToCheckDfo();
		try {
			dfo.toCheck("관제점", "Fx-1112", "등록안됨");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Boolean call(FxFact fact, Map<String, String> data) throws Exception {
		return toCheck(data.get("eventClass"), data.get("eventKey"), data.get("eventText"));
	}

	/**
	 * 운영자가 확인해야 할 내용을 기록한다.
	 * 
	 * @param eventClass 이벤트분류
	 * @param eventKey   이벤트키
	 * @param eventText  이벤트 내용
	 * @return
	 * @throws Exception
	 */
	public boolean toCheck(String eventClass, String eventKey, String eventText) throws Exception {

		Map<String, Object> para = FxApi.makePara("eventClass", eventClass, "eventKey", eventKey);
		Map<String, Object> data = FxApi.makePara("eventClass", eventClass, "eventKey", eventKey, "eventText",
				eventText);

		ClassDaoEx.open().setOfClass(FX_EV_CHECK.class, para, data).close();

		return true;

	}
}
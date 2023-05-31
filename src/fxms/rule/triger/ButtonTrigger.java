package fxms.rule.triger;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.ValueApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.rule.FxBusinessRuleEngine;
import fxms.rule.FxRuleDefault;
import fxms.rule.FxRuleFact;
import fxms.rule.action.url.UrlAction;

/**
 * name : 버튼 클릭하면 실행하는 트리거<br>
 * descr : 한 번 룰엔진의 내용을 실행한다.<br>
 * 
 * @author subkjh
 *
 */

public class ButtonTrigger extends FxRuleTriggerImpl {

	public static void main(String[] args) throws Exception {

		ValueApi.api = new ValueApiDfo();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", "http://10.0.1.11:10005/login");
		map.put("var", "url.body");
		map.put("responseno", "url.responseno");

		map.put("method", "GET");
		map.put("para", "user-id=subkjh&password=1111");
		map.put("method", "POST");
		map.put("para", "{\"user-id\"=\"subkjh\",\"password\"=\"1111\"}");
		FxBusinessRuleEngine re = new FxBusinessRuleEngine(new FxRuleFact());
		try {
			re.addRule(new FxRuleDefault(new UrlAction(map)));
//			re.addRule(new FxRuleDefault(new PrintAction(map)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		ButtonTrigger node = new ButtonTrigger(null);
		node.setRuleEngine(re);
		node.trigger(null);
		FxRuleFact fact = node.getRuleEngine().getFact();
		System.out.println(FxmsUtil.toJson(fact));
		System.out.println(fact.getHeader("run.startTime"));
	}

	public ButtonTrigger(Map<String, Object> map) throws Exception {
		super(map);
	}

	public void trigger(TriggerListener listener) throws Exception {
		this.getRuleEngine().run();
		if (listener != null)
			listener.onFinish();
	}

	@Override
	public void stopTrigger() {
	}

}

package fxms.rule.action;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxmsUtil;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * payload를 이용하여 사용자가 작성한 자바스크립트 코드를 수행한다.
 * 
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "자바스크립트 실행 후 결과 추가", descr = "자바스크립트를 실행하여 결과를 payload에 추가한다.")
public class JavaScriptAction extends FxRuleActionImpl {

	@FxAttr(text = "자바스크립트 코드", description = "실행할 자바스크립트 코드를 작성한다.")
	private String jscode;

	@FxAttr(text = "변수명", description = "스크립트 실행 결과를 담을 변수를 입력한다.")
	private String var;

	public JavaScriptAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		String json = FxmsUtil.toJson(fact.getPayload());

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		StringBuffer sb = new StringBuffer();
		sb.append("var payload = JSON.parse('").append(json).append("');\n");
		sb.append(jscode);

		try {
			Object result = engine.eval(sb.toString());
			if (result == null)
				result = engine.get("result");
			fact.addPayload(this.var, result);
		} catch (ScriptException e) {
			System.err.println(e);
		}

	}
}

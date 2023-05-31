package fxms.rule.action.ps;

import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.vo.PsVoRaw;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;
import fxms.rule.RuleApi;
import fxms.rule.vo.JavaScriptRet;

/**
 * 
 * @author subkjh
 * @since 2023.02
 * 
 */
@FxRuleActionInfo(name = "스크립트결과이용랜덤성능생성", descr = "payload에서 관리대상을 찾아 성능을 범위안에서 랜덤하게 생성한다.")
public class MakePsValueJscodeAction extends FxRuleActionImpl {

	@FxAttr(text = "자바스크립트 코드", description = "관리대상번호를 찾을 자바스크립트 코드를 작성한다.<br>결과는 관리대상번호가 와야한다.<br>수집항목은 var psId로 설정합니다.<br>" //
			+ "샘플코드<br>" //
			+ "var psId = 'TEMP';" //
			+ "var moNo = [ 10001, 10002, 10003, 10004];" //
			+ "moNo;")
	private String jscode;

	@FxAttr(text = "시작범위", description = "데이터의 시작 범위를 지정한다.")
	private int min;

	@FxAttr(text = "종료범위", description = "데이터의 종료 범위를 지정한다.")
	private int max;

	public MakePsValueJscodeAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	private int getRandom() {
		int value = (int) (Math.random() * max);
		if (value < min) {
			value = min;
		}
		return value;
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		// 스크립트 실행하여 관리번호 가져오기
		JavaScriptRet ret = RuleApi.getApi().runScript(fact, jscode);
		String psId = (String) ret.getEngine().get("psId");

		if (psId != null) {

			if (ret.getResult() instanceof jdk.nashorn.api.scripting.ScriptObjectMirror) {
				Map<String, Object> map = FxmsUtil.toMap(ret.getResult());
				for (Object key : map.keySet()) {
					PsVoRaw vo = new PsVoRaw(RuleApi.getNumber(map.get(key)).longValue(), psId, getRandom());
					fact.addPsValue(vo);
				}
			} else {
				PsVoRaw vo = new PsVoRaw(RuleApi.getNumber(ret.getResult()).longValue(), psId, getRandom());
				fact.addPsValue(vo);
			}

		}

	}

}

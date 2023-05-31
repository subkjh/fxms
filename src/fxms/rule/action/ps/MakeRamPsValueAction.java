package fxms.rule.action.ps;

import java.util.Map;

import fxms.bas.fxo.FxAttr;
import fxms.bas.vo.PsVoRaw;
import fxms.rule.FxRuleActionImpl;
import fxms.rule.FxRuleActionInfo;
import fxms.rule.FxRuleFact;

/**
 * 
 * @author subkjh
 * @since 2023.02
 */
@FxRuleActionInfo(name = "랜덤성능생성", descr = "지정한 관리대상의 성능을 범위안에서 랜덤하게 생성한다.")
public class MakeRamPsValueAction extends FxRuleActionImpl {

	@FxAttr(text = "관리대상번호", description = "관리대상번호를 숫자로 입력한다.")
	private long moNo;

	@FxAttr(text = "수집항목", description = "임의로 생성한 수집항목을 입력한다.")
	private String psId;

	@FxAttr(text = "시작범위", description = "데이터의 시작 범위를 지정한다.")
	private int min;

	@FxAttr(text = "종료범위", description = "데이터의 종료 범위를 지정한다.")
	private int max;

	public MakeRamPsValueAction(Map<String, Object> map) throws Exception {
		super(map);
	}

	@Override
	public void execute(FxRuleFact fact) throws Exception {

		int value = (int) (Math.random() * max);
		if (value < min) {
			value = min;
		}

		PsVoRaw vo = new PsVoRaw(moNo, psId, value);
		fact.addPsValue(vo);

	}

}

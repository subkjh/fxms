package fxms.rule;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.rule.action.AddDataAction;
import fxms.rule.action.JavaScriptAction;
import fxms.rule.action.PrintAction;
import fxms.rule.action.TestAction;
import fxms.rule.action.ps.MakeRamPsValueAction;
import fxms.rule.condition.FxConditionIf;
import fxms.rule.condition.FxConditionRange;
import fxms.rule.condition.FxRuleCondition;
import fxms.rule.condition.FxRuleSwitch;
import fxms.rule.condition.FxRuleSwitchJscode;
import fxms.rule.dbo.SelectRunToStopDbo;
import fxms.rule.dbo.all.FX_BR_RULE;
import fxms.rule.dbo.all.FX_BR_RULE_FLOW;
import fxms.rule.event.FxRuleEvent;
import fxms.rule.event.FxRuleStopEvent;
import fxms.rule.triger.FxRuleTrigger;
import fxms.rule.triger.RunTrigger;
import fxms.rule.vo.JavaScriptRet;
import fxms.rule.vo.RuleVo;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 룰 API
 * 
 * @author subkjh
 * @since 2023.02
 */
public abstract class RuleApi extends FxApi {

	/** use DBM */
	public static RuleApi api;

	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 instanceof Number || obj2 instanceof Number) {
			Number n1 = getNumber(obj1);
			Number n2 = getNumber(obj2);
			return n1.floatValue() == n2.floatValue();
		} else if (obj1 instanceof Boolean || obj2 instanceof Boolean) {
			Boolean n1 = getBoolean(obj1);
			Boolean n2 = getBoolean(obj2);
			return n1.booleanValue() == n2.booleanValue();
		} else {
			String n1 = getString(obj1);
			String n2 = getString(obj2);
			return n1.equals(n2);
		}
	}

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static RuleApi getApi() {
		if (api != null)
			return api;

		api = makeApi(RuleApi.class);

		return api;
	}

	public static Boolean getBoolean(Object value) {
		Boolean bol;
		if (value instanceof Boolean) {
			bol = (Boolean) value;
		} else {
			bol = "true".equalsIgnoreCase(String.valueOf(value));
		}
		return bol;
	}

	public static Number getNumber(Object value) {
		Number val;
		if (value instanceof Number) {
			val = (Number) value;
		} else {
			val = Float.valueOf(value.toString());
		}
		return val;
	}

	public static String getString(Object value) {
		String str = null;
		if (value instanceof String) {
			str = (String) value;
		} else if (value != null) {
			str = value.toString();
		}
		return str;
	}

	/**
	 * 행위를 생성한다.
	 * 
	 * @param brActName
	 * @param brActJson
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private FxRuleAction makeAction(Object brActName, Object brActJson) throws Exception {

		if (brActName != null) {
			Class<?> classOf = Class.forName(brActName.toString());
			Constructor<?> c = classOf.getConstructor(Map.class);
			if (brActJson instanceof Map) {
				return (FxRuleAction) c.newInstance((Map) brActJson);
			} else {
				return (FxRuleAction) c.newInstance((Map) null);
			}
		}

		return null;
	}

	/**
	 * 조건을 생성한다.
	 * 
	 * @param brCondName
	 * @param brCondJson
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private FxRuleCondition makeCondition(String brCondName, Object brCondJson) throws Exception {

		if (brCondName != null && brCondName.length() > 0) {
			Class<?> classOf = Class.forName(brCondName);
			Constructor<?> c = classOf.getConstructor(Map.class);
			if (brCondJson instanceof Map) {
				return (FxRuleCondition) c.newInstance((Map) brCondJson);
			} else {
				return (FxRuleCondition) c.newInstance((Map) null);
			}
		}

		return null;
	}

	/**
	 * 룰 실행을 위해 순서 내역을 생성한다.
	 * 
	 * @param flow
	 * @param switchRule
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private FxRule makeRule(FX_BR_RULE_FLOW flow, FxRuleSwtich switchRule) throws Exception {

		String brCondName = flow.getBrCondName();
		Map brCondJson = FxmsUtil.toMapFromJson(flow.getBrCondJson());
		String brCondRetVal = flow.getBrCondRetVal();
		Object brActName = flow.getBrActName();
		Object brActJson = FxmsUtil.toMapFromJson(flow.getBrActJson());

		FxRuleCondition condition = makeCondition(brCondName, brCondJson);
		FxRuleAction action = makeAction(brActName, brActJson);
		if (condition == null) {
			return new FxRuleDefault(action);
		} else {
			if (condition instanceof FxRuleSwitch) {

				// 바로위의 스위치문과 같으면 이 스위치를 통해 액션이 이루어져야 한다.
				if (condition.equals(switchRule)) {
					switchRule.addAction(brCondRetVal, action);
					// 새롭게 Rule을 추가할 필요가 없다.
					return null;
				} else {

					// 스위치이면 조건과 액션을 추가해 준다.
					FxRuleSwtich rule = new FxRuleSwtich((FxRuleSwitch) condition);
					if (brActJson instanceof Map)
						rule.addAction(brCondRetVal, action);
					return rule;
				}
			} else {
				return new FxRuleDefault(condition, action);
			}
		}

	}

	/**
	 * 룰 플로우를 이용하여 실행할 룰엔진을 생성한다.
	 * 
	 * @param flowList
	 * @return
	 * @throws Exception
	 */
	private FxBusinessRuleEngine makeRuleEngine(List<FX_BR_RULE_FLOW> flowList) throws Exception {

		FxBusinessRuleEngine bre = new FxBusinessRuleEngine(new FxRuleFact());
		FxRule rule;
		FxRuleSwtich switchRule = null;
		for (FX_BR_RULE_FLOW flow : flowList) {
			rule = makeRule(flow, switchRule);
			if (rule instanceof FxRuleSwtich) {
				switchRule = (FxRuleSwtich) rule;
				bre.addRule(rule);
			} else if (rule != null) {
				bre.addRule(rule);
			}
		}

		return bre;
	}

	/**
	 * 등록된 룰을 이용하여 트리거를 생성한다.
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	private FxRuleTrigger makeTrigger(RuleVo vo) throws Exception {

		FX_BR_RULE rule = vo.getRule();

		Logger.logger.trace("trigger={}, json={}", rule.getTriggerName(), rule.getTriggerJson());

		FxRuleTrigger trigger = makeTrigger(rule.getTriggerName(), FxmsUtil.toMapFromJson(rule.getTriggerJson()));

		FxBusinessRuleEngine bre = makeRuleEngine(vo.getFlows());
		trigger.setRuleEngine(bre);
		return trigger;
	}

	/**
	 * 트리거를 생성한다.
	 * 
	 * @param triggerName
	 * @param triggerData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	private FxRuleTrigger makeTrigger(String triggerName, Object triggerData) throws Exception {
		if (triggerName == null) {
			throw new Exception("unknown java class");
		}

		Class<?> classOf = Class.forName(triggerName.toString());
		Constructor<?> c = classOf.getConstructor(Map.class);

		if (triggerData instanceof Map) {
			return (FxRuleTrigger) c.newInstance((Map) triggerData);
		} else {
			return (FxRuleTrigger) c.newInstance((Map) null);
		}
	}

	/**
	 * 등록된 룰과 플로우를 저장소에서 읽어온다.
	 * 
	 * @param brRuleNo 비즈니스룰번호
	 * @return
	 * @throws Exception
	 */
	protected abstract RuleVo doSelectRule(int brRuleNo) throws Exception;

	/**
	 * 항상 실행해야할 룰을 조회한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract List<RuleVo> doSelectRuleToRunAlways() throws Exception;

	/**
	 * 실행중인 룰에 대해서 종료 요청이 있는지 데이터를 가져온다.
	 * 
	 * @param brRunNo 비즈니스룰실행번호
	 * @return
	 * @throws Exception
	 */
	protected abstract SelectRunToStopDbo doSelectRuleRunHst(long brRunNo) throws Exception;

	/**
	 * 등록 또는 수정한 룰과 플로우를 저장소에 기록한다.
	 * 
	 * @param userNo
	 * @param rule
	 * @param list
	 * @throws Exception
	 */
	protected abstract void doSetRuleFlow(int userNo, FX_BR_RULE rule, List<FX_BR_RULE_FLOW> list) throws Exception;

	/**
	 * 실행중인 룰에 대해 종료를 요청핞다.
	 * 
	 * @param brRunNo 비즈니스룰실행번호
	 * @param seconds 종료 지연 시간(초)
	 * @throws Exception
	 */
	protected abstract void doUpdateRuleToStop(long brRunNo, int seconds) throws Exception;

	@Override
	public void reload(Enum<?> type) throws Exception {

	}

	/**
	 * 테스트용
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	String makeSampleJson() {
		List<Map> list = new ArrayList<>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> tmp = new HashMap<String, Object>();
		tmp.put("aaa", 1);
		tmp.put("bbb", "a");
		data.put("triggerName", RunTrigger.class.getName());
		data.put("triggerJson", tmp);
		data.put("ruleName", "TEST");
		data.put("flowList", list);

		tmp = new HashMap<String, Object>();
		tmp.put("brActName", MakeRamPsValueAction.class.getName());
		tmp.put("brActJson", makeAttrJson("moNo", 1001, "psId", "TEMP", "min", 1, "max", 50));
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		tmp.put("brActName", TestAction.class.getName());
		tmp.put("brActJson", makeAttrJson("var", "test", "min", 1, "max", 10));
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		tmp.put("brCondName", FxConditionIf.class.getName());
		tmp.put("brCondJson", makeAttrJson("name", "test", "value", 6, "condition", "<="));
		tmp.put("brActName", AddDataAction.class.getName());
		tmp.put("brActJson", makeAttrJson("var", "test111", "value", "666"));
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		tmp.put("brCondName", FxConditionRange.class.getName());
		tmp.put("brCondJson", makeAttrJson("name", "test", "min", 4, "max", 8));
		tmp.put("brActName", AddDataAction.class.getName());
		tmp.put("brActJson", makeAttrJson("var", "message", "value", "4와 8사이 값입니다."));
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		String jscode = "var result = 'off';\n ";
		jscode += " if ( payload.test <= 5 ) result = 'on';\n";
		jscode += " result;\n";
		tmp.put("brActName", JavaScriptAction.class.getName());
		tmp.put("brActJson", makeAttrJson("jscode", jscode, "var", "result"));
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		tmp.put("brCondName", FxRuleSwitchJscode.class.getName());
		tmp.put("brCondJson", makeAttrJson("jscode", jscode, "var", "result"));
		tmp.put("brActName", AddDataAction.class.getName());
		tmp.put("brActJson", makeAttrJson("var", "jsexecute", "value", "JS가 on으로 처리되었네요"));
		tmp.put("brCondRetVal", "on");
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		tmp.put("brCondName", FxRuleSwitchJscode.class.getName());
		tmp.put("brCondJson", makeAttrJson("jscode", jscode, "var", "result"));
		tmp.put("brActName", AddDataAction.class.getName());
		tmp.put("brActJson", makeAttrJson("var", "jsexecute", "value", "JS가 off으로 처리되었네요"));
		tmp.put("brCondRetVal", "off");
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		tmp.put("brCondName", FxRuleSwitchJscode.class.getName());
		tmp.put("brCondJson", makeAttrJson("jscode", jscode, "var", "result"));
		tmp.put("brActName", AddDataAction.class.getName());
		tmp.put("brActJson", makeAttrJson("var", "jsexecute", "value", "JS가 null으로 처리되었네요"));
		tmp.put("brCondRetVal", "null");
		list.add(tmp);

		tmp = new HashMap<String, Object>();
		tmp.put("brActName", PrintAction.class.getName());
		list.add(tmp);

		return FxmsUtil.toJson(data);
	}

	/**
	 * 룰과 플로우를 조회한다.
	 * 
	 * @param brRuleNo
	 * @return
	 * @throws Exception
	 */
	public RuleVo getRule(int brRuleNo) throws Exception {
		return doSelectRule(brRuleNo);
	}

	public List<RuleVo> getRuleToRunAlways() throws Exception {
		return doSelectRuleToRunAlways();
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

	/**
	 * 새로 개발된 룰 관련 항목을 저장소에 기록한다.
	 * 
	 * @param classOf
	 * @throws Exception
	 */
	public abstract void insertBrItemDef(Class<?> classOf) throws Exception;

	/**
	 * 실행중인 룰에 대한 중지 요청이 있는지 확인한다.
	 * 
	 * @param brRunNo
	 * @return
	 */
	public boolean isInterrupted(long brRunNo) {
		try {
			SelectRunToStopDbo hst = doSelectRuleRunHst(brRunNo);
			if (hst.getRunFnshReqDtm() != null && hst.getRunFnshReqDtm() > 0) {
				return hst.getRunFnshReqDtm() <= DateUtil.getDtm();
			} else {
				return false;
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			return true;
		}
	}

	/**
	 * 실행하는 폴로우 종료에 대한 로그를 남긴다.
	 * 
	 * @param brRunNo
	 * @param fact
	 */
	public abstract void logRuleEnd(long brRunNo, FxRuleFact fact);

	/**
	 * 실행하는 플로우에 대한 로그를 남긴다.
	 * 
	 * @param brRuleNo
	 * @param brRuleName
	 * @param bre
	 * @return
	 */
	public abstract long logRuleStart(int brRuleNo, String brRuleName, FxBusinessRuleEngine bre);

	/**
	 * 
	 * @param objects
	 * @return
	 */
	public String makeAttrJson(Object... objects) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < objects.length; i += 2) {
			map.put(objects[i].toString(), objects[i + 1]);
		}
		return FxmsUtil.toJson(map);
	}

	/**
	 * 룰을 생성한다.
	 * 
	 * @param ruleName
	 * @param ruleDescr
	 * @param trigger
	 * @return
	 */
	public FX_BR_RULE makeRule(String ruleName, String ruleDescr, FxRuleTrigger trigger) {
		FX_BR_RULE rule = new FX_BR_RULE();
		rule.setRuleDescr(ruleDescr);
		rule.setRuleName(ruleName);
		rule.setTriggerJson(FxmsUtil.toJson(trigger.getParaMap()));
		rule.setTriggerName(trigger.getClass().getName());
		return rule;
	}

	/**
	 * 룰 플로우를 생성한다.
	 * 
	 * @param trigger
	 * @return
	 */
	public List<FX_BR_RULE_FLOW> makeRuleFlow(FxRuleTrigger trigger) {
		List<FX_BR_RULE_FLOW> list = new ArrayList<FX_BR_RULE_FLOW>();
		FX_BR_RULE_FLOW flow;
		for (FxRule rule : trigger.getRuleEngine().getRuleList()) {
			flow = new FX_BR_RULE_FLOW();

			if (rule instanceof FxRuleSwtich) {
				FxRuleSwtich r = (FxRuleSwtich) rule;
				Map<Object, FxRuleAction> map = r.getActMap();
				for (Object key : map.keySet()) {
					FxRuleAction act = map.get(key);
					flow.setBrActJson(FxmsUtil.toJson(act.getParaMap()));
					flow.setBrActName(act.getClass().getName());
					if (r.getCondition() != null) {
						flow.setBrCondJson(FxmsUtil.toJson(r.getCondition().getParaMap()));
						flow.setBrCondName(r.getCondition().getClass().getName());
					}
					flow.setBrCondRetVal(String.valueOf(key));
				}
				list.add(flow);

			} else if (rule instanceof FxRuleDefault) {
				FxRuleDefault r = (FxRuleDefault) rule;
				flow.setBrActJson(FxmsUtil.toJson(r.getAction().getParaMap()));
				flow.setBrActName(r.getAction().getClass().getName());
				if (r.getCondition() != null) {
					flow.setBrCondJson(FxmsUtil.toJson(r.getCondition().getParaMap()));
					flow.setBrCondName(r.getCondition().getClass().getName());
				}
				flow.setBrCondRetVal(null);
				list.add(flow);
			}
		}

		return list;
	}

	/**
	 * 트리거를 생성한다.
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FxRuleTrigger makeTrigger(String json) throws Exception {

		Map<String, Object> data = FxmsUtil.toMapFromJson(json);

		FxRuleTrigger trigger = makeTrigger(data.get("triggerName").toString(), data.get("triggerJson"));

		Object flowList = data.get("flowList");
		if (flowList instanceof List) {
			List<FX_BR_RULE_FLOW> list = new ArrayList<FX_BR_RULE_FLOW>();
			FX_BR_RULE_FLOW item;
			for (Object map : (List) flowList) {
				item = new FX_BR_RULE_FLOW();
				ObjectUtil.toObject((Map) map, item);
				list.add(item);

			}

			FxBusinessRuleEngine bre = makeRuleEngine(list);
			trigger.setRuleEngine(bre);

		}

		return trigger;

	}

	@Override
	public void onCreated() throws Exception {

	}

	/**
	 * 룰을 실행한다.
	 * 
	 * @param brRuleNo
	 * @return 비즈니스룰실행번호
	 * @throws Exception
	 */
	public long runRule(int brRuleNo) throws Exception {

		RuleVo vo = doSelectRule(brRuleNo);
		if (vo != null) {
			return runRule(vo);
		} else {
			return -1L;
		}
	}

	/**
	 * 
	 * @param rule
	 * @return
	 * @throws Exception
	 */
	public long runRule(RuleVo rule) throws Exception {

		FxRuleTrigger trigger = makeTrigger(rule);

		RuleThread ruleThread = new RuleThread(rule.getRule(), trigger);
		ruleThread.start();

		return ruleThread.getBrRunNo();
	}

	/**
	 * 자바스크립트 코드를 실행하고 결과를 리턴한다.
	 * 
	 * @param fact   팩트
	 * @param jscode 자바스크립트코드
	 * @param var    변수
	 * @return 변수의 값
	 * @throws Exception
	 */
	public Object runScript(FxRuleFact fact, String jscode, String var) throws Exception {

		JavaScriptRet ret = runScript(fact, jscode);
		if (var != null) {
			return ret.getEngine().get(var);
		}

		return ret.getResult();
	}

	public JavaScriptRet runScript(FxRuleFact fact, String jscode) throws Exception {

		String json = FxmsUtil.toJson(fact.getPayload());

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		StringBuffer sb = new StringBuffer();
		sb.append("var payload = JSON.parse('").append(json).append("');\n");
		sb.append(jscode);

		Object result = engine.eval(sb.toString());
		return new JavaScriptRet(engine, result);
	}

	/**
	 * 입력한 초가 지난 후 인터럽트한다.
	 * 
	 * @param brRunNo 실행번호
	 * @param seconds 초
	 * @return
	 */
	public boolean setInterrupt(long brRunNo, int seconds) {
		try {

			// 요청 내역 업데이트
			doUpdateRuleToStop(brRunNo, seconds);

			// 요청 내역 방송
			this.broadcast(new FxRuleStopEvent(brRunNo));

			return true;
		} catch (Exception e) {
			Logger.logger.error(e);
			return false;
		}
	}

	/**
	 * 룰과 플로우를 등록, 수정한다.
	 * 
	 * @param userNo
	 * @param rule
	 * @param list
	 */
	public RuleVo setRuleFlow(int userNo, FX_BR_RULE rule, List<FX_BR_RULE_FLOW> list) {
		try {
			doSetRuleFlow(userNo, rule, list);
			broadcast(new FxRuleEvent(FxmsUtil.toJson(rule)));
			RuleVo ret = new RuleVo();
			ret.setFlows(list);
			ret.setRule(rule);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

}
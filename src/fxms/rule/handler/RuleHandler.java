package fxms.rule.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.rule.RuleApi;
import fxms.rule.RuleApiDB;
import fxms.rule.action.AddDataAction;
import fxms.rule.action.JavaScriptAction;
import fxms.rule.action.PrintAction;
import fxms.rule.action.SleepAction;
import fxms.rule.action.TestAction;
import fxms.rule.action.ps.MakeRamPsValueAction;
import fxms.rule.condition.FxConditionIf;
import fxms.rule.condition.FxConditionRange;
import fxms.rule.condition.FxRuleSwitchJscode;
import fxms.rule.dbo.DeleteRulePara;
import fxms.rule.dbo.SelectRulePara;
import fxms.rule.dbo.all.FX_BR_RULE;
import fxms.rule.dbo.all.FX_BR_RULE_FLOW;
import fxms.rule.triger.RunTrigger;
import fxms.rule.vo.RuleVo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.dao.ClassDao;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.def.FxDaoCallBefore;

/**
 * 룰 핸들러
 * 
 * @author subkjh
 * @since 2023.02
 */
public class RuleHandler extends BaseHandler {

	public static void main(String[] args) throws Exception {
		RuleApi.api = new RuleApiDB();
		RuleHandler handler = new RuleHandler();
		SessionVo session = new SessionVo("AAA", 1, "test", "test", USER_TYPE_CD.Operator, 0, 0);
		handler.testAdd(session);

	}

	/**
	 * 룰 추가
	 * 
	 * @param session
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object addRule(SessionVo session, FX_BR_RULE data) throws Exception {

		data.setUseYn("Y");

		this.insert(data, new FxDaoCallBefore<FX_BR_RULE>() {
			@Override
			public void onCall(ClassDao tran, FX_BR_RULE data) throws Exception {

				int brRuleNo = tran.getNextVal(FX_BR_RULE.FX_SEQ_BRRULENO, Integer.class);

				data.setBrRuleNo(brRuleNo);

			}
		});

		return data;
	}

	/**
	 * 룰 사용 중지
	 * 
	 * @param session
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object deleteRule(SessionVo session, DeleteRulePara data) throws Exception {
		this.update(data, null);
		return data;
	}

	/**
	 * 플로우 조회
	 * 
	 * @param session
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object getFlow(SessionVo session, SelectRulePara data) throws Exception {
		return RuleApi.getApi().getRule(data.getBrRuleNo());
	}

	/**
	 * 룰 실행
	 * 
	 * @param session
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Object runRule(SessionVo session, SelectRulePara data) throws Exception {
		return RuleApi.getApi().runRule(data.getBrRuleNo());
	}

	/**
	 * 룰 플로우 설정
	 * 
	 * @param session
	 * @param rule
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object setFlow(SessionVo session, FX_BR_RULE rule, Map<String, Object> parameters) throws Exception {

		List<Map> flow = (List<Map>) parameters.get("flows");
		FX_BR_RULE_FLOW item;
		List<FX_BR_RULE_FLOW> list = new ArrayList<FX_BR_RULE_FLOW>();
		for (Map map : flow) {
			item = convert(session, map, FX_BR_RULE_FLOW.class, true);
			list.add(item);
		}

		RuleVo vo = RuleApi.getApi().setRuleFlow(session.getUserNo(), rule, list);
		return vo;
	}

	void testAdd(SessionVo session) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> tmp = new HashMap<String, Object>();
		List<Map<String, Object>> flowList = new ArrayList<Map<String, Object>>();

		parameters.put("ruleName", "SAPMLE RULE ( SLEEP )");
		parameters.put("ruleDescr", "HANDLER를 이용한 샘플");
		parameters.put("brRuleNo", 3);
		attrMap.put("aaa", 1);
		attrMap.put("bbb", "a");
		parameters.put("triggerName", RunTrigger.class.getName());
		parameters.put("triggerJson", FxmsUtil.toJson(attrMap));

		FX_BR_RULE item;
		try {

			item = convert(session, parameters, FX_BR_RULE.class, true);
			addRule(session, item);

			int brRuleNo = item.getBrRuleNo();

			parameters = new HashMap<String, Object>();
			parameters.put("flows", flowList);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", "");
			tmp.put("brCondJson", "");
			tmp.put("brCondRetVal", "");
			tmp.put("brActName", MakeRamPsValueAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("moNo", 1001, "psId", "TEMP", "min", 1, "max", 50));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", "");
			tmp.put("brCondJson", "");
			tmp.put("brCondRetVal", "");
			tmp.put("brActName", TestAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("var", "test", "min", 1, "max", 10));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", FxConditionIf.class.getName());
			tmp.put("brCondJson", RuleApi.getApi().makeAttrJson("name", "test", "value", 6, "condition", "<="));
			tmp.put("brCondRetVal", "");
			tmp.put("brActName", AddDataAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("var", "test111", "value", "1234"));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", FxConditionRange.class.getName());
			tmp.put("brCondJson", RuleApi.getApi().makeAttrJson("name", "test", "min", 4, "max", 8));
			tmp.put("brCondRetVal", "");
			tmp.put("brActName", AddDataAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("var", "message", "value", "4와 8사이 값입니다."));
			flowList.add(tmp);

			String jscode = "var result = 'off';\n ";
			jscode += " if ( payload.test <= 5 ) result = 'on';\n";
			jscode += " result;\n";
			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", "");
			tmp.put("brCondJson", "");
			tmp.put("brCondRetVal", "");
			tmp.put("brActName", JavaScriptAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("jscode", jscode, "var", "result"));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", FxRuleSwitchJscode.class.getName());
			tmp.put("brCondJson", RuleApi.getApi().makeAttrJson("jscode", jscode, "var", "result"));
			tmp.put("brCondRetVal", "on");
			tmp.put("brActName", AddDataAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("var", "jsexecute", "value", "JS가 on으로 처리되었네요"));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", FxRuleSwitchJscode.class.getName());
			tmp.put("brCondJson", RuleApi.getApi().makeAttrJson("jscode", jscode, "var", "result"));
			tmp.put("brCondRetVal", "off");
			tmp.put("brActName", AddDataAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("var", "jsexecute", "value", "JS가 off으로 처리되었네요"));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", FxRuleSwitchJscode.class.getName());
			tmp.put("brCondJson", RuleApi.getApi().makeAttrJson("jscode", jscode, "var", "result"));
			tmp.put("brCondRetVal", "null");
			tmp.put("brActName", AddDataAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("var", "jsexecute", "value", "JS가 null으로 처리되었네요"));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", "");
			tmp.put("brCondJson", "");
			tmp.put("brCondRetVal", "");
			tmp.put("brActName", SleepAction.class.getName());
			tmp.put("brActJson", RuleApi.getApi().makeAttrJson("seconds", 10));
			flowList.add(tmp);

			tmp = new HashMap<String, Object>();
			tmp.put("brRuleNo", brRuleNo);
			tmp.put("brCondName", "");
			tmp.put("brCondJson", "");
			tmp.put("brCondRetVal", "");
			tmp.put("brActName", PrintAction.class.getName());
			tmp.put("brActJson", "");
			flowList.add(tmp);

			setFlow(session, item, parameters);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void testDelete(SessionVo session) {

		Map<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put("brRuleNo", 1);
			DeleteRulePara item = convert(session, parameters, DeleteRulePara.class, true);
			deleteRule(session, item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void testGet(SessionVo session) {

		Map<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put("brRuleNo", 1);
			SelectRulePara item = convert(session, parameters, SelectRulePara.class, true);
			System.out.println(FxmsUtil.toJson(getFlow(session, item)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void testRun(SessionVo session) {

		Map<String, Object> parameters = new HashMap<String, Object>();

		try {
			parameters.put("brRuleNo", 3);
			SelectRulePara item = convert(session, parameters, SelectRulePara.class, true);
			System.out.println(FxmsUtil.toJson(runRule(session, item)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao();
	}
}

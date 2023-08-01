package fxms.bas;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.VarApi;
import fxms.bas.cron.AlarmStatDailyCron;
import fxms.bas.cron.AlarmStatHourlyCron;
import fxms.bas.cron.MoSyncCron;
import fxms.bas.fxo.adapter.AlarmAfterLogAdapter;
import fxms.bas.fxo.adapter.AlarmAfterMailAdapter;
import fxms.bas.fxo.cron.CheckACron;
import fxms.bas.impl.adapter.SmsBizppurioAlarmAfterAdapter;
import fxms.bas.impl.api.AdapterApiDfo;
import fxms.bas.impl.cron.AlarmReleaseCron;
import fxms.bas.impl.cron.PsStatMakeCron;
import fxms.bas.impl.dpo.ao.iqr.IqrCron;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.rule.RuleApi;
import fxms.rule.RuleApiDB;
import fxms.rule.action.AddDataAction;
import fxms.rule.action.BroadcastAction;
import fxms.rule.action.JavaScriptAction;
import fxms.rule.action.PrintAction;
import fxms.rule.action.SleepAction;
import fxms.rule.action.TestAction;
import fxms.rule.action.alarm.CheckAlarmPsValueAction;
import fxms.rule.action.mo.SelectMoAction;
import fxms.rule.action.mo.SelectMoListAction;
import fxms.rule.action.ps.GetPsValueAction;
import fxms.rule.action.ps.MakePsValueJscodeAction;
import fxms.rule.action.ps.MakeRamPsValueAction;
import fxms.rule.action.ps.PeekPsValueAction;
import fxms.rule.action.ps.SavePsValueAction;
import fxms.rule.action.ps.UpdatePsValueAction;
import fxms.rule.action.url.UrlAction;
import fxms.rule.condition.FxConditionIf;
import fxms.rule.condition.FxConditionJscode;
import fxms.rule.condition.FxConditionRange;
import fxms.rule.condition.FxRuleSwitchJscode;
import fxms.rule.triger.PollingTrigger;
import fxms.rule.triger.RunTrigger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.util.SqlTool;

public class FxmsInit {

	public static void main(String[] args) {
		FxmsInit init = new FxmsInit();
		init.initAdapter();
//		init.makeSource();
	}

	public void initAdapter() {

		AdapterApiDfo api = new AdapterApiDfo();

		try {
//			api.insert(IqrCron.class);
//				api.insert(CalcTrnsChrgCron.class);

//			VarApi.getApi().setTimeUpdated(ReloadType.Adapter, DateUtil.getDtm());
//				VarApi.getApi().setTimeUpdated(ReloadType.AlarmCode, DateUtil.getDtm());
//			return;
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			api.insert(CheckACron.class);
			api.insert(MoSyncCron.class);
			api.insert(AlarmStatDailyCron.class);
			api.insert(AlarmStatHourlyCron.class);

			api.insert(SmsBizppurioAlarmAfterAdapter.class);
			api.insert(AlarmAfterLogAdapter.class);
			api.insert(AlarmAfterMailAdapter.class);

			api.insert(AlarmReleaseCron.class);
			api.insert(PsStatMakeCron.class);
			
			api.insert(IqrCron.class);

			Map<String, Object> varInfo = new HashMap<String, Object>();
			varInfo.put("varGrpName", "TIME");
			varInfo.put("varDispName", "Adapter 적용 최종시간");
			varInfo.put("varDesc", "아답터 데이터가 최종 업데이트 된 시간을 나타낸다.");
			String varName = VarApi.UPDATED_TIME_VAR + ReloadType.Adapter;
			VarApi.getApi().updateVarInfo(varName, varInfo);

			VarApi.getApi().setTimeUpdated(ReloadType.Adapter, DateUtil.getDtm());

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void initRule() {
		RuleApi api = new RuleApiDB() {

		};

		try {
			api.insertBrItemDef(AddDataAction.class);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			api.insertBrItemDef(SleepAction.class);
			api.insertBrItemDef(SelectMoAction.class);
			api.insertBrItemDef(SelectMoListAction.class);
			api.insertBrItemDef(AddDataAction.class);
			api.insertBrItemDef(BroadcastAction.class);
			api.insertBrItemDef(JavaScriptAction.class);
			api.insertBrItemDef(PrintAction.class);
			api.insertBrItemDef(TestAction.class);
			api.insertBrItemDef(CheckAlarmPsValueAction.class);
			api.insertBrItemDef(UrlAction.class);
			api.insertBrItemDef(UpdatePsValueAction.class);
			api.insertBrItemDef(MakeRamPsValueAction.class);
			api.insertBrItemDef(MakePsValueJscodeAction.class);
			api.insertBrItemDef(PeekPsValueAction.class);
			api.insertBrItemDef(SavePsValueAction.class);
			api.insertBrItemDef(GetPsValueAction.class);

			api.insertBrItemDef(FxConditionJscode.class);
			api.insertBrItemDef(FxRuleSwitchJscode.class);
			api.insertBrItemDef(FxConditionIf.class);
			api.insertBrItemDef(FxConditionRange.class);

			api.insertBrItemDef(PollingTrigger.class);
			api.insertBrItemDef(RunTrigger.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void makeSource() {
		SqlTool tool = new SqlTool();

		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/def/queries.xml", "subkjh.dao.queries.QueriesQid",
				"src/subkjh/dao/queries");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/test/test.xml", "fxms.bas.impl.dao.TestQid",
				"src/fxms/bas/impl/dao");

		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/service/AppService.xml", "fxms.bas.impl.dao.AppServiceQid",
				"src/fxms/bas/impl/dao");

		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/cron/CheckACron.xml", "fxms.bas.impl.dao.CheckACronQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/cron/StatMakeDailyCron.xml",
				"fxms.bas.impl.dao.StatMakeDailyCronQid", "src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/cron/StatMakeHourlyCron.xml",
				"fxms.bas.impl.dao.StatMakeHourlyCronQid", "src/fxms/bas/impl/dao");

		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/api/ValueApi.xml", "fxms.bas.impl.dao.ValueApiQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/api/RuleApi.xml", "fxms.bas.impl.dao.RuleApiQid",
				"src/fxms/bas/impl/dao");

		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/AlarmCfgHandler.xml",
				"fxms.bas.impl.dao.AlarmCfgHandlerQid", "src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/AlarmHandler.xml", "fxms.bas.impl.dao.AlarmHandlerQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/InloHandler.xml", "fxms.bas.impl.dao.InloHandlerQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/LoginHandler.xml", "fxms.bas.impl.dao.LoginHandlerQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/ModelHandler.xml", "fxms.bas.impl.dao.ModelHandlerQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/MoHandler.xml", "fxms.bas.impl.dao.MoHandlerQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/PsHandler.xml", "fxms.bas.impl.dao.PsHandlerQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/UiDashbHandler.xml",
				"fxms.bas.impl.dao.UiDashbHandlerQid", "src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/UiHandler.xml", "fxms.bas.impl.dao.UiHandlerQid",
				"src/fxms/bas/impl/dao");
		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/handler/UserHandler.xml", "fxms.bas.impl.dao.UserHandlerQid",
				"src/fxms/bas/impl/dao");

		tool.makeXml2JavaQid("deploy/conf/sql/fxms/bas/comm.xml", "fxms.bas.impl.dao.CommQid", "src/fxms/bas/impl/dao");

	}
}

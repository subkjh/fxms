package fxms.rule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxAttrApi;
import fxms.bas.fxo.FxAttrVo;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.rule.condition.FxRuleCondition;
import fxms.rule.dbo.SelectRunToStopDbo;
import fxms.rule.dbo.all.FX_BR_ITEM_DEF;
import fxms.rule.dbo.all.FX_BR_ITEM_DEF_ATTR;
import fxms.rule.dbo.all.FX_BR_RULE;
import fxms.rule.dbo.all.FX_BR_RULE_FLOW;
import fxms.rule.dbo.all.FX_BR_RULE_RUN_HST;
import fxms.rule.triger.FxRuleTrigger;
import fxms.rule.vo.RuleVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

public class RuleApiDB extends RuleApi {

	@Override
	protected RuleVo doSelectRule(int brRuleNo) throws Exception {

		RuleVo ret = new RuleVo();

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			Map<String, Object> para = RuleApi.makePara("brRuleNo", brRuleNo);

			List<FX_BR_RULE> infoList = tran.select(FX_BR_RULE.class, para, FX_BR_RULE.class);
			if (infoList.size() > 0) {
				ret.setRule(infoList.get(0));
				List<FX_BR_RULE_FLOW> flowList = tran.select(FX_BR_RULE_FLOW.class, para, FX_BR_RULE_FLOW.class);
				if (flowList.size() > 0) {
					flowList.sort(new Comparator<FX_BR_RULE_FLOW>() {

						@Override
						public int compare(FX_BR_RULE_FLOW o1, FX_BR_RULE_FLOW o2) {
							return o1.getFlowNo() - o2.getFlowNo();
						}
					});
					ret.setFlows(flowList);
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

		return ret;
	}

	@Override
	protected SelectRunToStopDbo doSelectRuleRunHst(long brRunNo) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			return tran.selectOne(SelectRunToStopDbo.class, makePara("brRunNo", brRunNo));
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<RuleVo> doSelectRuleToRunAlways() throws Exception {

		List<RuleVo> list = new ArrayList<RuleVo>();

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			List<FX_BR_RULE> ruleList = tran.select(FX_BR_RULE.class, makePara("alwaysRunYn", "Y", "useYn", "Y"),
					FX_BR_RULE.class);

			for (FX_BR_RULE rule : ruleList) {
				RuleVo vo = new RuleVo();
				vo.setRule(rule);
				List<FX_BR_RULE_FLOW> flowList = tran.select(FX_BR_RULE_FLOW.class,
						makePara("brRuleNo", rule.getBrRuleNo()), FX_BR_RULE_FLOW.class);
				if (flowList.size() > 0) {
					flowList.sort(new Comparator<FX_BR_RULE_FLOW>() {
						@Override
						public int compare(FX_BR_RULE_FLOW o1, FX_BR_RULE_FLOW o2) {
							return o1.getFlowNo() - o2.getFlowNo();
						}
					});
					vo.setFlows(flowList);
				}
				list.add(vo);
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

		return list;
	}

	@Override
	protected void doSetRuleFlow(int userNo, FX_BR_RULE rule, List<FX_BR_RULE_FLOW> list) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			int newBrRuleNo = -1;
			if (rule.getBrRuleNo() <= 0) {
				newBrRuleNo = tran.getNextVal(FX_BR_RULE.FX_SEQ_BRRULENO, Integer.class);
				rule.setBrRuleNo(newBrRuleNo);
			}
			FxTableMaker.initRegChg(userNo, rule);

			int flowNo = 0;
			for (FX_BR_RULE_FLOW flow : list) {
				flow.setBrRuleNo(rule.getBrRuleNo());
				flow.setFlowNo(flowNo++);
				FxTableMaker.initRegChg(userNo, flow);
			}

			if (newBrRuleNo > 0) {
				// 신규
				tran.insertOfClass(rule.getClass(), rule);
				tran.insertOfClass(FX_BR_RULE_FLOW.class, list);
			} else {
				// 수정이면 기존꺼 지우고 추가
				tran.deleteOfClass(FX_BR_RULE_FLOW.class, RuleApiDB.makePara("brRuleNo", rule.getBrRuleNo()));
				tran.insertOfClass(FX_BR_RULE_FLOW.class, list);
			}

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void doUpdateRuleToStop(long brRunNo, int seconds) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			FX_BR_RULE_RUN_HST hst = tran.selectOne(FX_BR_RULE_RUN_HST.class, makePara("brRunNo", brRunNo));
			if (hst != null) {
				hst.setRunFnshReqDtm(DateUtil.getDtm(System.currentTimeMillis() + (seconds * 1000L)));
				tran.updateOfClass(hst.getClass(), hst);
				tran.commit();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public void insertBrItemDef(Class<?> classOf) throws Exception {
		FxRuleActionInfo info = classOf.getAnnotation(FxRuleActionInfo.class);
		if (info == null) {
			return;
		}

		FX_BR_ITEM_DEF def = new FX_BR_ITEM_DEF();
		def.setBrItemDescr(info.descr());
		def.setBrItemDispName(info.name());
		def.setBrItemName(classOf.getName());
		def.setBrItemType(getName());
		def.setUseYn("Y");

		FxTableMaker.initRegChg(0, def);

		if (FxRuleAction.class.isAssignableFrom(classOf)) {
			def.setBrItemType("ACTION");
		} else if (FxRuleCondition.class.isAssignableFrom(classOf)) {
			def.setBrItemType("CONDITION");
		} else if (FxRuleTrigger.class.isAssignableFrom(classOf)) {
			def.setBrItemType("TRIGGER");
		} else {
			def.setBrItemType("NONE");
		}

//		System.out.println(FxmsUtil.toJson(def));

		List<FX_BR_ITEM_DEF_ATTR> attrList = new ArrayList<FX_BR_ITEM_DEF_ATTR>();
		List<FxAttrVo> fields = FxAttrApi.getFxAttrField(classOf);
		FX_BR_ITEM_DEF_ATTR attr;
		for (FxAttrVo vo : fields) {

			attr = new FX_BR_ITEM_DEF_ATTR();
			attr.setAttrDescr(vo.attr.description());
			attr.setAttrFieldName(vo.field.getName());
			attr.setAttrName(vo.getName());
			attr.setAttrType(vo.field.getType().getSimpleName());
			attr.setMandtYn(vo.attr.required() ? "Y" : "N");
			attr.setBrItemName(classOf.getName());
			attr.setUseYn("Y");

			FxTableMaker.initRegChg(0, attr);

			System.out.println(FxmsUtil.toJson(attr));

			attrList.add(attr);
		}

//		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
//		try {
//			tran.start();
//			if (tran.selectOne(FX_BR_ITEM_DEF.class, RuleApi.makePara("brItemName", def.getBrItemName())) != null) {
//				tran.updateOfClass(FX_BR_ITEM_DEF.class, def);
//				tran.deleteOfClass(DeleteItemDefAttr.class, RuleApi.makePara("brItemName", def.getBrItemName()));
//				if (attrList.size() > 0) {
//					tran.insertOfClass(FX_BR_ITEM_DEF_ATTR.class, attrList);
//				}
//			} else {
//				tran.insertOfClass(FX_BR_ITEM_DEF.class, def);
//				if (attrList.size() > 0) {
//					tran.insertOfClass(FX_BR_ITEM_DEF_ATTR.class, attrList);
//				}
//			}
//
//			tran.commit();
//		} catch (Exception e) {
//			tran.rollback();
//			Logger.logger.error(e);
//			throw e;
//		} finally {
//			tran.stop();
//		}

	}

	@Override
	public void logRuleEnd(long brRunNo, FxRuleFact fact) {

		ClassDao tran;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		} catch (Exception e1) {
			Logger.logger.error(e1);
			return;
		}

		try {
			tran.start();

			FX_BR_RULE_RUN_HST data = tran.selectOne(FX_BR_RULE_RUN_HST.class, makePara("brRunNo", brRunNo));
			data.setRstText(FxmsUtil.toJson(fact.getPayload()));
			data.setRunActCnt(fact.getRunRuleCnt());
			data.setErrActCnt(fact.getTotRuleCnt() - fact.getRunRuleCnt());
			if (fact.getHeader("run.endTime") != null) {
				data.setRunFnshDtm(DateUtil.getDtm(fact.getHeader("run.endTime").toString()));
			} else {
				data.setRunFnshDtm(DateUtil.getDtm());
			}

			tran.updateOfClass(FX_BR_RULE_RUN_HST.class, data);

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
		} finally {
			tran.stop();
		}

	}

	@Override
	public long logRuleStart(int brRuleNo, String brRuleName, FxBusinessRuleEngine bre) {

		ClassDao tran;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		} catch (Exception e1) {
			Logger.logger.error(e1);
			return -1;
		}

		try {
			tran.start();

			FX_BR_RULE_RUN_HST data = new FX_BR_RULE_RUN_HST();

			data.setRegDtm(DateUtil.getDtm());
			data.setBrRunNo(tran.getNextVal(FX_BR_RULE_RUN_HST.FX_SEQ_BRRUNNO, Long.class));
			data.setBrRuleNo(brRuleNo);
			data.setRegUserNo(0);
			data.setRuleName(brRuleName);
			data.setTotActCnt(bre.getRuleList().size());
			data.setRunActCnt(-1);
			data.setErrActCnt(-1);
			data.setRunStrtDtm(DateUtil.getDtm());

			tran.insertOfClass(FX_BR_RULE_RUN_HST.class, data);

			tran.commit();

			return data.getBrRunNo();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			return -1;
		} finally {
			tran.stop();
		}

	}

}

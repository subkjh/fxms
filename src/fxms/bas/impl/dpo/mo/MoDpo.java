package fxms.bas.impl.dpo.mo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.CoCode.MO_WORK_TYPE_CD;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dbo.all.FX_MO_FXSERVICE;
import fxms.bas.impl.dbo.all.FX_MO_NODE;
import fxms.bas.impl.dbo.all.FX_MX_WORK_HST;
import fxms.bas.impl.dvo.MoDefDvo;
import fxms.bas.mo.FxMo;
import fxms.bas.mo.FxServiceMo;
import fxms.bas.mo.Mo;
import fxms.bas.mo.NodeMo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.diff.DiffData;
import subkjh.dao.util.FxTableMaker;

public class MoDpo {

	private static final Map<String, MoDefDvo> defMap = new HashMap<>();

	public static void add(MoDefDvo def) {
		defMap.put(def.getMoClass(), def);
	}

	private static void initMoDef() {
		try {
			for (MoDefDvo def : new MoClassInitDfo().call(null, null)) {
				defMap.put(def.getMoClass(), def);
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		if (defMap.size() == 0) {
			defMap.put(FxMo.MO_CLASS, new MoDefDvo(FxMo.MO_CLASS, FxMo.class, FX_MO.class));
			defMap.put(FxServiceMo.MO_CLASS, new MoDefDvo(FxMo.MO_CLASS, FxServiceMo.class, FX_MO_FXSERVICE.class));
			defMap.put(NodeMo.MO_CLASS, new MoDefDvo(FxMo.MO_CLASS, NodeMo.class, FX_MO_NODE.class));
		}
	}

	public static Class<? extends FX_MO> getDboClass(String moClass) {
		if (defMap.size() == 0) {
			initMoDef();
		}
		MoDefDvo def = defMap.get(moClass);
		return def == null ? FX_MO.class : def.getDboJavaClass();
	}

	public static Class<? extends FxMo> getMoClass(String moClass) {

		if (defMap.size() == 0) {
			initMoDef();
		}

		MoDefDvo def = defMap.get(moClass);
		return def == null ? FxMo.class : def.getJavaClass();
	}

	public static String getMoClassName(Class<?> classOf) {
		Field field;
		try {
			field = classOf.getField("MO_CLASS");
			return field.get(null).toString();
		} catch (Exception e) {
		}
		return FxMo.MO_CLASS;
	}

	/**
	 * 작업 이력을 기록할 DBO를 만든다.
	 *
	 * @param mo
	 * @param workTypeCd
	 * @param userNo
	 * @param reason
	 * @return
	 */
	public static FX_MX_WORK_HST makeHstMo(long workHstNo, FX_MO mo, MO_WORK_TYPE_CD workTypeCd, int userNo,
			String reason) {

		FX_MX_WORK_HST hst;
		hst = new FX_MX_WORK_HST();
		hst.setWorkHstNo(workHstNo);
		hst.setRstCont(reason);
		hst.setAttrBfVal(null);
		hst.setAttrName(mo.getMoClass());
		hst.setMoWorkTypeCd(workTypeCd.getCode());
		hst.setMoWorkTypeName(workTypeCd.name());
		hst.setMoName(mo.getMoName());
		hst.setMoNo(mo.getMoNo());

		FxTableMaker.initRegChg(userNo, hst);

		return hst;
	}

	/**
	 * 작업 이력을 기록할 DBO 목록을 만든다.
	 *
	 * @param attrKeyMap
	 * @param mo
	 * @param diffList
	 * @return
	 */
	public static List<FX_MX_WORK_HST> makeHstMo(int userNo, Map<String, String> attrKeyMap, Mo mo,
			List<DiffData> diffList) {

		List<FX_MX_WORK_HST> list = new ArrayList<>();

		if (diffList == null) {
			return list;
		}

		FX_MX_WORK_HST hst;
		for (DiffData data : diffList) {
			hst = new FX_MX_WORK_HST();
			hst.setAttrAfVal(String.valueOf(data.getAfData()));
			hst.setAttrBfVal(String.valueOf(data.getBfData()));
			hst.setAttrName(attrKeyMap != null ? attrKeyMap.get(data.getName()) : data.getName());
			hst.setMoWorkTypeCd(MO_WORK_TYPE_CD.Update.getCode());
			hst.setMoWorkTypeName(MO_WORK_TYPE_CD.Update.name());
			hst.setMoName(mo.getMoName());
			hst.setMoNo(mo.getMoNo());
			FxTableMaker.initRegChg(userNo, hst);

			list.add(hst);
		}

		return list;
	}
}

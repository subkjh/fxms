package com.fxms.ui.bas.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.OP_TYPE;
import com.fxms.ui.VAR;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.ProgressIndicator;
import com.fxms.ui.bas.vo.Attr;
import com.fxms.ui.bas.vo.FxVarVo;
import com.fxms.ui.bas.vo.LocationTreeVo;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.bas.vo.UiTimeVo;
import com.fxms.ui.bas.vo.ui.UiGroupVo;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import FX.MS.UiConfig;
import fxms.client.log.Logger;

public class CodeMap {

	/** user.dx.index */
	public static final String USER_PROPERTY_DX_INDEX = "user.dx.index";

	private static CodeMap map;

	public static CodeMap getMap() {
		if (map != null) {
			return map;
		}

		map = new CodeMap();
		return map;
	}

	private Map<Integer, UiAlarmCfgVo> alarmCfgMap;
	private Map<Integer, UiAlarmCodeVo> alarmCodeMap;
	private Map<String, List<UiCodeVo>> cdTypeMap;
	private List<UiChartVo> chartList;
	private boolean loaded = false;
	private Map<Integer, LocationVo> locationMap;
	private List<LocationTreeVo> locationTreeList;
	private Map<String, Object> moCountMap = null;
	private Map<Integer, UiOpCodeVo> opMap;
	private ProgressIndicator progressIndicator = null;
	private Map<String, UiPsItemVo> psItemMap;
	/** key : QID */
	private Map<String, List<Attr>> qidRetMap;
	private List<UiGroupVo> uiList;
	private Map<Integer, UiUserGroupVo> userGroupMap;
	private Map<Integer, UiUserVo> userMap;
	private Map<String, Object> userProperties;
	private Map<String, FxVarVo> varMap;

	private CodeMap() {

		cdTypeMap = new HashMap<String, List<UiCodeVo>>();
		opMap = new HashMap<Integer, UiOpCodeVo>();
		psItemMap = new HashMap<String, UiPsItemVo>();
		chartList = new ArrayList<UiChartVo>();
		alarmCfgMap = new HashMap<Integer, UiAlarmCfgVo>();
		alarmCodeMap = new HashMap<Integer, UiAlarmCodeVo>();
		userMap = new HashMap<Integer, UiUserVo>();
		locationMap = new HashMap<Integer, LocationVo>();
		qidRetMap = new HashMap<String, List<Attr>>();
		varMap = new HashMap<String, FxVarVo>();
		userProperties = new HashMap<String, Object>();
		uiList = new ArrayList<UiGroupVo>();

	}

	public UiAlarmCfgVo getAlarmCfg(int alarmCfgNo) {
		return alarmCfgMap.get(alarmCfgNo);
	}

	public List<UiAlarmCfgVo> getAlarmCfgList() {
		return Arrays.asList(alarmCfgMap.values().toArray(new UiAlarmCfgVo[alarmCfgMap.size()]));
	}

	public UiAlarmCodeVo getAlarmCode(int alcdNo) {
		UiAlarmCodeVo vo = alarmCodeMap.get(alcdNo);
		return vo;
	}

	public List<UiAlarmCodeVo> getAlarmCodeList() {
		return new ArrayList<UiAlarmCodeVo>(alarmCodeMap.values());
	}

	public String getAlarmCodeName(int alcdNo) {
		UiAlarmCodeVo vo = alarmCodeMap.get(alcdNo);
		return vo != null ? vo.getAlcdName() : "alarm-code=" + alcdNo;
	}

	public List<Attr> getAttrList(String qid, Map<String, Object> para) {
		List<Attr> attrList = null;

		String key = qid + (para == null || para.size() == 0 ? "" : "?" + para.toString());

		System.out.println("attr-list =" + key);

		attrList = qidRetMap.get(key);

		if (attrList == null || attrList.size() == 0) {
			attrList = DxAsyncSelector.getSelector().getAttrValueList(qid, para);
			if (attrList != null) {
				qidRetMap.put(key, attrList);
			} else {
				return new ArrayList<Attr>();
			}
		}

		return attrList;
	}

	public List<UiChartVo> getChartList(Mo mo) {

		if (mo == null) {
			return new ArrayList<UiChartVo>();
		}

		List<UiChartVo> list = new ArrayList<UiChartVo>();

		for (UiChartVo vo : chartList) {
			if (vo.getMoClass() != null && vo.getMoClass().equals(mo.getMoClass()) && vo.getMoType() != null
					&& vo.getMoType().equals(mo.getMoType())) {
				list.add(vo);
			}
		}

		return list;
	}

	public UiChartVo getChartVo(Mo mo) {

		if (mo == null) {
			return null;
		}

		for (UiChartVo vo : chartList) {
			if (vo.getMoClass() != null && vo.getMoClass().equals(mo.getMoClass()) && vo.getMoType() != null
					&& vo.getMoType().equals(mo.getMoType())) {
				return vo;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param mo
	 *            대상
	 * @param chartId
	 *            챠트ID
	 * @return
	 */
	public UiChartVo getChartVo(Mo mo, String chartId) {

		if (mo == null) {
			return null;
		}

		for (UiChartVo vo : chartList) {
			if (vo.getMoClass() != null && vo.getMoClass().equals(mo.getMoClass()) //
					&& vo.getMoType() != null && vo.getMoType().equals(mo.getMoType()) //
					&& vo.getChartId().equals(chartId) //
			) {
				return vo;
			}
		}
		return null;
	}

	public UiCodeVo getCode(String cdType, String cdCode) {
		List<UiCodeVo> list = cdTypeMap.get(cdType);
		if (list != null) {
			for (UiCodeVo e : list) {
				if (e.getCdCode().equals(cdCode)) {
					return e;
				}
			}
		}

		return null;
	}

	public List<UiCodeVo> getCodeList(String cdType) {
		return cdTypeMap.get(cdType);
	}

	public Map<String, List<UiCodeVo>> getCodeTypeMap() {
		return cdTypeMap;
	}

	public List<LocationTreeVo> getLocationList() {
		return locationTreeList;
	}

	public String getLocationName(int inloNo) {
		LocationVo e = locationMap.get(inloNo);
		return e != null ? e.getInloFname() : "location-no=" + inloNo;
	}

	public UiOpCodeVo getOpCode(int opNo) {
		for (UiOpCodeVo op : opMap.values()) {
			if (op.getOpNo() == opNo) {
				return op;
			}
		}
		return null;
	}

	public UiOpCodeVo getOpCode(OP_NAME name) {
		if (name == null) {
			return null;
		}
		return getOpCode(name.getOpName());
	}

	public UiOpCodeVo getOpCode(String name) {
		for (UiOpCodeVo op : opMap.values()) {
			if (op.getOpName().equals(name)) {
				return op;
			}
		}
		return null;
	}

	public List<UiOpCodeVo> getOpCodeAll() {
		return new ArrayList<UiOpCodeVo>(opMap.values());
	}

	public Map<Integer, UiOpCodeVo> reloadOpCode() {
		loadOpCode();
		return opMap;
	}

	/**
	 * 
	 * @return 메뉴용 운용코드
	 */
	public List<UiOpCodeVo> getOpCodes4Dx() {
		List<UiOpCodeVo> list = new ArrayList<UiOpCodeVo>();

		for (UiOpCodeVo op : opMap.values()) {
			if (op.getOpType() == OP_TYPE.dx.getCode()) {
				list.add(op);
			}
		}
		return list;
	}

	public UiPsItemVo getPsItem(String psCode) {
		return psItemMap.get(psCode);
	}

	public List<UiPsItemVo> getPsItemList(Mo mo) {
		List<UiPsItemVo> ret = new ArrayList<UiPsItemVo>();
		for (UiPsItemVo item : psItemMap.values()) {
			if (item.getMoClass() == null || item.getMoClass().length() == 0) {
				ret.add(item);
			} else if (mo.getMoClass().equals(item.getMoClass())) {
				if (item.getMoType() == null || item.getMoType().length() == 0
						|| item.getMoType().equals(mo.getMoType())) {
					ret.add(item);
				}
			}
		}
		return ret;
	}

	public List<UiPsItemVo> getPsItemList(String psCodes) {
		List<UiPsItemVo> ret = new ArrayList<UiPsItemVo>();
		UiPsItemVo item;
		for (String psCode : psCodes.split(",")) {
			item = psItemMap.get(psCode.trim());
			if (item != null) {
				ret.add(item);
			}
		}
		return ret;
	}

	public Map<String, UiPsItemVo> getPsItemMap() {
		return psItemMap;
	}

	public String getPsItemName(String psCode) {
		UiPsItemVo item = psItemMap.get(psCode);
		return item != null ? item.getPsName() : psCode;
	}

	public UiGroupVo getUiGroupVo(int uiGroupNo) {
		for (UiGroupVo vo : uiList) {
			if (vo.getUiGroupNo() == uiGroupNo) {
				return vo;
			}
		}

		return null;
	}

	public List<UiGroupVo> getUiList() {
		return uiList;
	}

	public String getUserGroupName(int ugrpNo) {
		UiUserGroupVo vo = userGroupMap.get(ugrpNo);
		return vo != null ? vo.getUgrpName() : "group-no=" + ugrpNo;
	}

	public String getUserName(int userNo) {
		UiUserVo vo = userMap.get(userNo);
		return vo != null ? vo.getUserName() : "user-no=" + userNo;
	}

	public Object getUserProperty(String name) {
		return userProperties.get(name);

	}

	public int getUserProperty(String name, int defVal) {
		Object value = userProperties.get(name);

		if (value == null) {
			return defVal;
		}

		try {
			return Double.valueOf(value.toString()).intValue();
		} catch (Exception e) {
			return defVal;
		}
	}

	public FxVarVo getVar(VAR var) {
		return varMap.get(var.getVarName());
	}

	public boolean isLoaded() {
		return loaded;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int loadOpCode() {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("useYn", "Y");

		Map<String, Object> data = DxAsyncSelector.getSelector().selectObject("cd", "get-op-list", para);

		List<UiOpCodeVo> list = DxAsyncSelector.convert(UiOpCodeVo.class, (List) data.get("op-list"));
		List<UiOpCodeAttrVo> attrList = DxAsyncSelector.convert(UiOpCodeAttrVo.class, (List) data.get("op-attr-list"));
		List<UiOpCodeMenu> menuList = DxAsyncSelector.convert(UiOpCodeMenu.class, (List) data.get("op-menu-list"));

		Map<Integer, UiOpCodeVo> map = new HashMap<Integer, UiOpCodeVo>();

		for (UiOpCodeVo op : list) {
			map.put(op.getOpNo(), op);
		}

		attrList.sort(new Comparator<UiOpCodeAttrVo>() {

			@Override
			public int compare(UiOpCodeAttrVo c1, UiOpCodeAttrVo c2) {
				if (c1.getSeqBy() != c2.getSeqBy()) {
					return c1.getSeqBy() - c2.getSeqBy();
				} else {
					return c1.getAttrDisp().compareTo(c2.getAttrDisp());
				}
			}
		});
		UiOpCodeVo op;
		for (UiOpCodeAttrVo attr : attrList) {
			op = map.get(attr.getOpNo());
			if (op != null) {
				op.getChildren().add(attr);
			}
		}

		menuList.sort(new Comparator<UiOpCodeMenu>() {

			@Override
			public int compare(UiOpCodeMenu c1, UiOpCodeMenu c2) {
				return c1.getMenuIndex() - c2.getMenuIndex();
			}
		});

		for (UiOpCodeMenu menu : menuList) {
			op = map.get(menu.getOpNo());
			if (op != null) {
				op.getMenuList().add(menu);
			}
		}

		opMap = map;

		UiConfig.getConfig().writeInitData("op-code", data);

		return map.size();
	}

	public void reload(ProgressIndicator indicator) {

		loaded = false;

		progressIndicator = indicator;

		new Thread() {
			public void run() {
				qidRetMap.clear();

				int index = 0;
				int total = 12;
				int ret;

				if (progressIndicator != null) {
					progressIndicator.setSize(total);
				}

				Map<String, Object> map = DxAsyncSelector.getSelector().selectObject("cd", "get-system-time", null);
				Number systemTime = (Number) map.get("system-time");
				UiConfig.getConfig().setSystemTime(systemTime.longValue());

				List<UiTimeVo> list = DxAsyncSelector.getSelector().selectList("cd", "get-op-time-list", null,
						UiTimeVo.class);
				UiConfig.getConfig().writeInitData("op-time", list);

				showMsg(++index, "loading variables...");
				ret = reloadVar();
				showMsg(index, "reload variables..." + ret);

				showMsg(++index, "loading user properties...");
				ret = reloadUserProperties();
				showMsg(index, "loaded user properties..." + ret);

				showMsg(++index, "loading ui...");
				ret = reloadUi();
				showMsg(index, "loaded ui..." + ret);

				showMsg(++index, "loading codes...");
				ret = reloadCode();
				showMsg(index, "loaded codes..." + ret);

				showMsg(++index, "loading op-codes...");
				ret = loadOpCode();
				showMsg(index, "reload op-codes..." + ret);

				showMsg(++index, "loading ps-items...");
				ret = reloadPsItem();
				showMsg(index, "reload ps-items..." + ret);

				showMsg(++index, "loading users...");
				ret = loadUser();
				showMsg(index, "reload users..." + ret);

				showMsg(++index, "loading locations...");
				ret = reloadLocation();
				showMsg(index, "reload locations..." + ret);

				showMsg(++index, "loading alarm-configurations...");
				ret = reloadAlarmCfg();
				showMsg(index, "reload alarm-configurations..." + ret);

				showMsg(++index, "loading alarm-codes...");
				ret = reloadAlarmCode();
				showMsg(index, "reload alarm-codes..." + ret);

				showMsg(++index, "loading charts...");
				ret = reloadChart();
				showMsg(index, "reload charts..." + ret);

				showMsg(++index, "loading mo counts...");
				ret = selectMoCount(Mo.MO_CLASS, null);
				showMsg(index, "reload mo counts..." + ret);

				showMsg(total, "finished loading");

				loaded = true;
			}
		}.start();
	}

	public void reloadMoCount() {
		moCountMap = DxAsyncSelector.getSelector().selectObject("mo", "get-mo-count", null);
	}

	public int reloadUi() {
		List<UiGroupVo> list = DxAsyncSelector.getSelector().selectUiList();
		if (list != null) {
			uiList = list;
			return list.size();
		} else {
			return 0;
		}
	}

	public int reloadUserProperties() {
		Map<String, Object> ret = DxAsyncSelector.getSelector().getUserProperties();
		if (ret != null) {
			userProperties.putAll(ret);
			return ret.size();
		}
		return 0;
	}

	public synchronized int selectMoCount(String moClass, FxCallback<Number> callback) {

		if (moCountMap != null && callback != null) {
			Object value = moCountMap.get(moClass);
			callback.onCallback(value == null ? null : Double.valueOf(value.toString()));
			return 0;
		}

		reloadMoCount();

		Object value = moCountMap != null ? moCountMap.get(moClass) : null;

		if (callback != null) {
			callback.onCallback(value == null ? null : Double.valueOf(value.toString()));
		}

		return moCountMap != null ? moCountMap.size() : -1;
	}

	public void setUserProperty(String name, Object value, Object... others) {

		userProperties.put(name, value);

		for (int i = 0; i < others.length; i += 2) {
			if (others.length > i + 1) {
				userProperties.put(String.valueOf(others[i]), others[i + 1]);
			}
		}

		DxAsyncSelector.getSelector().setUserProperty(name, value, others);
	}

	private int loadUser() {

		Map<String, Object> para = new HashMap<String, Object>();
		List<UiUserVo> data = DxAsyncSelector.getSelector().selectList("usr", "get-user-list", para, UiUserVo.class);
		if (data != null) {
			Map<Integer, UiUserVo> map = new HashMap<Integer, UiUserVo>();

			for (UiUserVo vo : data) {
				map.put(vo.getUserNo(), vo);
			}

			userMap = map;
		}

		List<UiUserGroupVo> groupList = DxAsyncSelector.getSelector().selectList("usr", "get-user-group-list", para,
				UiUserGroupVo.class);
		if (groupList != null) {
			Map<Integer, UiUserGroupVo> map = new HashMap<Integer, UiUserGroupVo>();

			for (UiUserGroupVo vo : groupList) {
				map.put(vo.getUgrpNo(), vo);
			}

			userGroupMap = map;
		}

		return 0;

	}

	private void putLocation(Map<Integer, LocationVo> map, LocationTreeVo location) {

		map.put(location.getMe().getInloNo(), location.getMe());

		for (LocationTreeVo child : location.getChildren()) {
			putLocation(map, child);
		}

	}

	private int reloadAlarmCfg() {

		try {

			List<UiAlarmCfgVo> data = DxAsyncSelector.getSelector().selectList("ao", "get-alarm-cfg-list", null,
					UiAlarmCfgVo.class);

			if (data != null) {
				Map<Integer, UiAlarmCfgVo> map = new HashMap<Integer, UiAlarmCfgVo>();

				for (UiAlarmCfgVo vo : data) {
					map.put(vo.getAlarmCfgNo(), vo);
				}

				alarmCfgMap = map;

				return map.size();
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return 0;
	}

	private int reloadAlarmCode() {

		try {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("targetMoClass", "all");
			List<UiAlarmCodeVo> data = DxAsyncSelector.getSelector().selectList("ao", "get-alarm-code-list", para,
					UiAlarmCodeVo.class);

			if (data != null) {
				Map<Integer, UiAlarmCodeVo> map = new HashMap<Integer, UiAlarmCodeVo>();

				for (UiAlarmCodeVo vo : data) {
					map.put(vo.getAlcdNo(), vo);
				}

				alarmCodeMap = map;

				return map.size();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return 0;
	}

	private int reloadChart() {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("useYn", "Y");

		try {
			List<UiChartVo> data = DxAsyncSelector.getSelector().selectList("cd", "get-chart-list", para,
					UiChartVo.class);
			if (data != null) {
				chartList = data;

				return data.size();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return 0;
	}

	private int reloadCode() {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("useYn", "Y");

		List<UiCodeVo> data = DxAsyncSelector.getSelector().selectList("cd", "get-code-list", para, UiCodeVo.class);

		Map<String, List<UiCodeVo>> map = new HashMap<String, List<UiCodeVo>>();

		data.sort(new Comparator<UiCodeVo>() {

			@Override
			public int compare(UiCodeVo c1, UiCodeVo c2) {
				if (c1.getSeqBy() != c2.getSeqBy()) {
					return c1.getSeqBy() - c2.getSeqBy();
				} else {
					return c1.getCdName().compareTo(c2.getCdName());
				}
			}
		});

		List<UiCodeVo> entry;

		for (UiCodeVo op : data) {
			entry = map.get(op.getCdType());
			if (entry == null) {
				entry = new ArrayList<UiCodeVo>();
				map.put(op.getCdType(), entry);
			}
			entry.add(op);
		}

		cdTypeMap = map;

		UiConfig.getConfig().writeInitData("code", data);

		return map.size();

	}

	private int reloadLocation() {

		List<LocationTreeVo> data;

		try {
			data = DxAsyncSelector.getSelector().getLocationList();

			if (data != null) {
				Map<Integer, LocationVo> map = new HashMap<Integer, LocationVo>();

				for (LocationTreeVo vo : data) {
					putLocation(map, vo);
				}

				locationTreeList = data;

				locationMap = map;

				return map.size();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	private int reloadPsItem() {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("useYn", "Y");
		List<UiPsItemVo> data = DxAsyncSelector.getSelector().selectList("ps", "get-ps-item-list", para,
				UiPsItemVo.class);
		Map<String, UiPsItemVo> map = new HashMap<String, UiPsItemVo>();

		for (UiPsItemVo op : data) {
			map.put(op.getPsCode(), op);
		}

		psItemMap = map;

		UiConfig.getConfig().writeInitData("ps-item", data);

		return map.size();
	}

	private int reloadVar() {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("useYn", "Y");

		List<FxVarVo> list = DxAsyncSelector.getSelector().selectList("cd", "get-var-list", para, FxVarVo.class);

		Map<String, FxVarVo> map = new HashMap<String, FxVarVo>();

		StringBuffer sb = new StringBuffer();

		for (FxVarVo var : list) {
			sb.append("\n");
			sb.append(var.getVarName());
			sb.append("=");
			sb.append(var.getVarValue());
			map.put(var.getVarName(), var);
		}

		Logger.logger.debug(sb.toString());

		varMap = map;
		return map.size();
	}

	private void showMsg(int index, String msg) {
		if (progressIndicator != null) {
			progressIndicator.showMsg(index, msg);
		}
	}

}

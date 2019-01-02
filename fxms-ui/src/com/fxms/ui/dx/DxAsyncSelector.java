package com.fxms.ui.dx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.UiPsItemVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.lang.Lang.Type;
import com.fxms.ui.bas.mo.ContainerMo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.Attr;
import com.fxms.ui.bas.vo.ListData;
import com.fxms.ui.bas.vo.LocationTreeVo;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.bas.vo.PsValue;
import com.fxms.ui.bas.vo.PsValueMap;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.bas.vo.ui.UiGroupVo;
import com.fxms.ui.bas.vo.ui.UiPropertyVo;

import FX.MS.UI;
import fxms.client.FxmsClient;
import fxms.client.log.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Window;

public class DxAsyncSelector extends FxmsClient {

	private static DxAsyncSelector selector;

	public static <T> List<T> convert(Class<T> classOf, List<Map<String, Object>> list) {

		if (list == null) {
			return new ArrayList<T>();
		}

		List<T> ret = new ArrayList<T>();
		T data;
		try {
			for (Map<String, Object> map : list) {
				data = classOf.newInstance();
				ObjectUtil.toObject(map, data);
				ret.add(data);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}

	public static <T> T convert(Class<T> classOf, Map<String, Object> map) {

		T data;
		try {
			data = classOf.newInstance();
			ObjectUtil.toObject(map, data);
			return data;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> getData(Map<String, Object> result, int index) {
		if (result.get("list") instanceof List) {
			List list = (List) result.get("list");
			if (list.size() > index) {
				return (Map<String, Object>) list.get(index);
			}
		}

		return null;
	}

	public synchronized static DxAsyncSelector getSelector() {
		if (selector != null) {
			return selector;
		}

		try {
			selector = new DxAsyncSelector();
			return selector;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private LinkedBlockingQueue<Thread> queue;

	private DxAsyncSelector() throws Exception {

		queue = new LinkedBlockingQueue<Thread>();

		Thread thread = new Thread() {
			public void run() {
				Thread child;

				while (true) {
					try {
						child = queue.take();
					} catch (InterruptedException e) {
						continue;
					}
					if (child != null) {
						child.start();
						try {
							child.join();
						} catch (InterruptedException e) {
						}
					}
				}
			}
		};

		thread.setName("AsyncSelector");
		thread.start();

	}

	public void callMethod(UiOpCodeVo op, Map<String, Object> parameters, FxCallback<Map<String, Object>> callback) {

		Thread th = new Thread() {
			public void run() {
				try {
					Map<String, Object> ret = testRetObj(op.getOpUri(), op.getOpMethod(), parameters);
					callback.onCallback(ret);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);
	}

	public void callMethod(String uri, String method, Map<String, Object> parameters,
			FxCallback<Map<String, Object>> callback) {

		Thread th = new Thread() {
			public void run() {
				try {
					Map<String, Object> ret = testRetObj(uri, method, parameters);
					if (callback != null) {
						callback.onCallback(ret);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);
	}

	public Map<String, Object> callMethod(Node parent, UiOpCodeVo op, Map<String, Object> parameters) {

		try {
			Map<String, Object> ret = testRetObj(op.getOpUri(), op.getOpMethod(), parameters);
			if (op.getOkMsg() != null && op.getOkMsg().length() > 0) {
				FxAlert.showAlert(FxAlertType.ok, parent, op.getOpTitle(), op.getOkMsg());
			}
			return ret;
		} catch (Exception e) {
			if (e.getMessage().contains("NotFound")) {
				FxAlert.showAlert(FxAlertType.error, parent, op.getOpTitle(), Lang.getText(Type.msg, "내용을 찾을 수 없습니다."));
			} else {
				Logger.logger.error(e);
				FxAlert.showAlert(FxAlertType.error, parent, op.getOpTitle(), e.getMessage());
			}
			return null;
		}
	}

	public Map<String, Object> callMethod(Node parent, UiOpCodeVo op, String name, Object value, Object... parameters) {
		try {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put(name, value);
			for (int i = 0; i < parameters.length; i += 2) {
				para.put(parameters[i].toString(), parameters[i + 1]);
			}

			return callMethod(parent, op, para);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public UiAlarm getAlarm(long alarmNo) {

		if (alarmNo <= 0) {
			return null;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alarmNo", alarmNo);

		try {
			List<Map<String, Object>> list;

			list = testRetList("ao", "get-alarm", parameters);
			return list.size() == 1 ? convert(UiAlarm.class, list.get(0)) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<UiAlarm> getAlarmCur() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		// para.put("moNo", "530000");
		// para.put("alcdNo", "22101");
		List<Map<String, Object>> list = testRetList("ao", "get-alarm-cur", para);
		List<UiAlarm> alarmList = new ArrayList<UiAlarm>();
		UiAlarm alarm;
		try {
			for (Map<String, Object> map : list) {
				alarm = new UiAlarm();
				ObjectUtil.toObject(map, alarm);
				alarmList.add(alarm);
			}
			return alarmList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<UiAlarm> getAlarmHst(int minute) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("ocuDate >= ", FxmsClient.getDate(System.currentTimeMillis() - (minute * 60000L)));
		// para.put("alcdNo", "22101");
		List<Map<String, Object>> list = testRetList("ao", "get-alarm-hst", para);
		List<UiAlarm> alarmList = new ArrayList<UiAlarm>();
		UiAlarm alarm;
		try {
			for (Map<String, Object> map : list) {
				alarm = new UiAlarm();
				ObjectUtil.toObject(map, alarm);
				alarmList.add(alarm);
			}
			return alarmList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<Attr> getAttrValueList(String qid, Map<String, Object> para) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		if (para != null) {
			parameters.putAll(para);
		}
		parameters.put("qid", qid);
		List<Map<String, Object>> list;
		try {
			list = testRetList("cd", "get-attr-value-list", parameters);
			return convert(Attr.class, list);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ContainerMo> getContainerMoList(int inloNo) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("inloNo", inloNo);
		parameters.put("moClass", ContainerMo.MO_CLASS);
		List<Map<String, Object>> list = testRetList("mo", "get-mo-list", parameters);
		return convert(ContainerMo.class, list);
	}

	// public void selectAttrValueList(String qid, DxCallback<List<Attr>> callback)
	// {
	//
	// Thread th = new Thread() {
	// public void run() {
	// Map<String, Object> para = new HashMap<String, Object>();
	// para.put("qid", qid);
	// List<Map<String, Object>> list;
	// try {
	// list = testRetList("cd", "get-attr-value-list", para);
	// Platform.runLater(new Runnable() {
	// public void run() {
	// callback.onCallback(convert(Attr.class, list));
	// }
	// });
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// };
	//
	// addWork(th);
	//
	// }

	public void getContainerMoList(int inloNo, FxCallback<List<ContainerMo>> callback) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("inloNo", inloNo);
		parameters.put("moClass", ContainerMo.MO_CLASS);
		Thread th = new Thread() {
			public void run() {
				List<Map<String, Object>> list;
				try {
					list = testRetList("mo", "get-mo-list", parameters);
					callback.onCallback(convert(ContainerMo.class, list));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);

	}

	public List<LocationTreeVo> getLocationList() throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();

		// // para.put("inloType", ClientCode.inloType.COUNTRY);
		// para.put("inloType", ClientCode.inloType.COUNTRY);
		// para.put("upperInloNo", -1);

		List<Map<String, Object>> list = testRetList("mo", "get-inlo-list", para);

		List<LocationVo> pool = convert(LocationVo.class, list);

		return LocationTreeVo.makeTreeLocation(pool);
	}

	public Mo getMo(long moNo) {

		if (moNo <= 0) {
			return null;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("moNo", moNo);

		try {
			Map<String, Object> moData = testRetObj("mo", "get-mo", parameters);
			return Mo.makeMo(moData);
		} catch (Exception e) {
			return null;
		}
	}

	public Mo getMo(Map<String, Object> parameters) {
		try {
			Map<String, Object> moData = testRetObj("mo", "get-mo", parameters);
			return Mo.makeMo(moData);
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, Object> getMo(long moNo, String moClass) {

		if (moNo <= 0) {
			return null;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("moNo", moNo);
		parameters.put("moClass", moClass);

		List<Map<String, Object>> list;

		try {
			list = testRetList("mo", "get-mo-list", parameters);
			return list.size() == 1 ? list.get(0) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void getMoConfig(long moNo, FxCallback<List<Mo>> callback) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("moNo", moNo);
		Thread th = new Thread() {
			public void run() {
				List<Mo> moList = new ArrayList<Mo>();
				try {
					Map<String, Object> map = testRetObj("mo", "get-mo-config", parameters);
					moList.add(Mo.makeMo((Map<String, Object>) map.get("parent")));
					List<String> classList = (List<String>) map.get("mo-class-list");
					for (String moClass : classList) {
						moList.addAll(Mo.makeMoList((List<Map<String, Object>>) map.get(moClass + "-list")));
					}
					callback.onCallback(moList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);

	}

	public void getMoList(long upperMoNo, String moClass, FxCallback<List<Mo>> callback) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("upperMoNo", upperMoNo);
		parameters.put("moClass", moClass);
		Thread th = new Thread() {
			public void run() {
				List<Map<String, Object>> list;
				try {
					list = testRetList("mo", "get-mo-list", parameters);
					callback.onCallback(Mo.makeMoList(list));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);
	}

	public List<Mo> getMoList(Map<String, Object> parameters, FxCallback<List<Mo>> callback) {

		if (callback != null) {
			Thread th = new Thread() {
				public void run() {
					List<Map<String, Object>> list;
					try {
						list = testRetList("mo", "get-mo-list", parameters);
						callback.onCallback(Mo.makeMoList(list));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};

			addWork(th);

		} else {
			List<Map<String, Object>> list;
			try {
				list = testRetList("mo", "get-mo-list", parameters);
				return Mo.makeMoList(list);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return null;

	}

	public List<Mo> getMoList2(long upperMoNo) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("upperMoNo", upperMoNo);
		List<Map<String, Object>> list = testRetList("mo", "get-mo-list", parameters);
		return convert(Mo.class, list);
	}

	@SuppressWarnings("unchecked")
	public void getPsList(UiPsItemVo psItem, long moNo, String psType, long startDate, long endDate,
			FxCallback<PsValueMap> callback) {

		Thread th = new Thread() {
			public void run() {
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("psCode", psItem.getPsCode());
				para.put("psType", psType);
				para.put("startDate", startDate);
				para.put("endDate", endDate);

				PsValueMap ret = new PsValueMap(psItem, startDate, endDate);
				List<PsValue> entry;
				List<Map<String, Object>> list;
				String key;

				try {
					para.put("moNo", moNo);
					list = testRetList("ps", "get-ps-list", para);

					if (list.size() == 0) {
						callback.onCallback(ret);
						return;
					}

					List<String> keyList = new ArrayList<String>(list.get(0).keySet());
					Collections.sort(keyList);

					entry = new ArrayList<PsValue>();

					for (Map<String, Object> map : list) {
						// System.out.println("moNo | moInstance | psCode");
						key = map.get("moNo") + " | " + map.get("moInstance") + " | " + map.get("psCode");
						List<Double> timeList = (List<Double>) map.get("timeList");
						List<Number> valueList = (List<Number>) map.get("valueList");
						for (int i = 0; i < timeList.size(); i++) {
							entry.add(new PsValue(timeList.get(i), valueList.get(i)));
						}
						ret.put(key, entry);
					}

				} catch (Exception e) {
					e.printStackTrace();
					callback.onCallback(null);
					return;
				}

				callback.onCallback(ret);

			}
		};

		addWork(th);

	}

	public Map<String, Object> getUserProperties() {
		try {
			return testRetObj("usr", "get-user-properties", null);
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	public void applyNew(Node node, Map<String, Object> parameters) throws Exception {

		String result = post(getUrl("login"), gson.toJson(parameters));

		String msg = "ok".equals(result) ? Lang.getText(Type.msg, "계정 신청이 정상적으로 처리되었습니다.")
				: Lang.getText(Type.msg, "계정 신청 처리중 오류가 발생되었습니다.");
		FxAlert.showAlert(FxAlertType.ok, node, Lang.getText(Type.msg, "계정 신청"), msg);

	}

	public Map<String, Object> login(String userId, String password) throws Exception {

		Map<String, Object> ret = super.login(userId, password);

		Map<String, Object> ret2 = testRetObj("usr", "get-user", ret);

		getUserMap().putAll(ret2);

		return getUserMap();
	}

	public void selectCdCodeList(FxCallback<List<UiCodeVo>> callback) {

		Thread th = new Thread() {
			public void run() {
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("useYn", "Y");
				List<Map<String, Object>> list;
				try {
					list = testRetList("cd", "get-code-list", para);
					callback.onCallback(convert(UiCodeVo.class, list));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);

	}

	public <T> void selectConfDataList(String dataClass, Class<T> classOfD, FxCallback<List<T>> callback) {

		Thread th = new Thread() {
			public void run() {
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("dataClass", dataClass);
				List<Map<String, Object>> list;
				try {
					list = testRetList("cd", "get-conf-data-list", para);
					callback.onCallback(convert(classOfD, list));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);

	}

	public <T> List<T> selectList(String uri, String method, Map<String, Object> para, Class<T> classOfT) {
		List<Map<String, Object>> list;
		try {
			list = testRetList(uri, method, para);
			return convert(classOfT, list);
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	public <T> void selectList(String uri, String method, Map<String, Object> para, Class<T> classOfT,
			FxCallback<List<T>> callback) {

		Thread th = new Thread() {
			public void run() {
				List<Map<String, Object>> list;
				try {
					list = testRetList(uri, method, para);
					Platform.runLater(new Runnable() {
						public void run() {
							callback.onCallback(convert(classOfT, list));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);

	}

	public void selectMoList(String moClass, FxCallback<List<? extends Mo>> callback) {

		Thread th = new Thread() {
			public void run() {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("moClass", moClass);
				List<Map<String, Object>> list;
				try {
					list = testRetList("mo", "get-mo-list", parameters);
					Platform.runLater(new Runnable() {
						public void run() {
							callback.onCallback(convert(Mo.getMoClass(moClass), list));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		addWork(th);

	}

	public Map<String, Object> selectObject(String uri, String method, Map<String, Object> para) {

		Map<String, Object> map;
		try {
			map = testRetObj(uri, method, para);
			return map;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

	public void selectObject(String uri, String method, Map<String, Object> para,
			FxCallback<Map<String, Object>> callback) {

		Thread th = new Thread() {
			public void run() {
				Map<String, Object> map;
				try {
					map = testRetObj(uri, method, para);
					Platform.runLater(new Runnable() {
						public void run() {
							callback.onCallback(map);
						}
					});
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		};

		addWork(th);
	}

	public void selectQid(Map<String, Object> parameters, FxCallback<ListData> callback) throws Exception {

		Thread th = new Thread() {
			public void run() {
				Map<String, Object> ret;
				try {
					ret = testRetObj("cd", "get-qid-select", parameters);
					callback.onCallback(new ListData(parameters.get("qid") + "", ret));
				} catch (Exception e) {
					Platform.runLater(new Runnable() {
						public void run() {
							callback.onCallback(new ListData(e.getMessage()));
						}
					});
				}
			}
		};

		addWork(th);

	}

	public List<Map<String, Object>> selectQid(String file, String qid) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("qid-file-name", file);
		parameters.put("qid", qid);
		return testRetList("cd", "select-qid", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<UiGroupVo> selectUiList() {
		Map<String, Object> map;
		try {
			map = testRetObj("usr", "get-user-ui", null);

			List<Map<String, Object>> groupMapList = (List<Map<String, Object>>) map.get("group-list");
			List<Map<String, Object>> basicMapList = (List<Map<String, Object>>) map.get("basic-list");
			List<Map<String, Object>> propertyMapList = (List<Map<String, Object>>) map.get("property-list");

			List<UiGroupVo> groupList = convert(UiGroupVo.class, groupMapList);
			List<UiBasicVo> basicList = convert(UiBasicVo.class, basicMapList);
			List<UiPropertyVo> propertyList = convert(UiPropertyVo.class, propertyMapList);

			for (UiBasicVo b : basicList) {
				for (UiGroupVo g : groupList) {
					if (g.getUiGroupNo() == b.getUiGroupNo()) {
						g.getChildren().add(b);
						break;
					}
				}
			}

			for (UiPropertyVo p : propertyList) {
				for (UiBasicVo b : basicList) {
					if (b.getUiGroupNo() == p.getUiGroupNo() && b.getUiNo() == p.getUiNo()) {
						b.getChildren().add(p);
						break;
					}
				}
			}

			return groupList;

		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	public void setUserProperty(String name, Object value, Object... others) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(name, String.valueOf(value));
		for (int i = 0; i < others.length; i += 2) {
			if (others.length > i + 1) {
				parameters.put(String.valueOf(others[i]), String.valueOf(others[i + 1]));
			}
		}

		Thread th = new Thread() {
			public void run() {
				try {
					testRetList("usr", "set-user-property", parameters);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		};

		addWork(th);

	}

	private void addWork(Thread th) {
		try {
			queue.put(th);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected Window getWindow(Node parent) {
		return parent != null ? parent.getScene().getWindow() : UI.primaryStage;
	}

}

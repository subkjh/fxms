package fxms.bas.api;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fxms.bas.co.def.ALARM_CODE;
import fxms.bas.co.exp.NotFoundException;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.fxo.FxActor;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.service.mo.MoService;
import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLoader;
import fxms.bas.mo.attr.MoLocation;
import fxms.bas.mo.attr.Model;
import fxms.bas.mo.child.AutoAddChild;
import fxms.bas.mo.child.MoConfig;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;

public abstract class MoApi extends FxApi {

	class MoClassData {
		String moClass;
		Class<?> dboJavaClass;
		Class<? extends Mo> javaClass;
	}

	private static final long PROJECT_MONO = 1000;

	/** use DBM */
	public static MoApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static MoApi getApi() {
		if (api != null)
			return api;

		api = makeApi(MoApi.class);

		return api;
	}

	public static void main(String[] args) {
		try {
			MoApi.getApi().reload();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Mo systemMo;
	private List<MoClassData> moClassList = new ArrayList<MoClassData>();
	private Map<Long, Mo> moMap;
	private Map<String, Mo> moMapKey;
	private Map<Integer, Model> modelMap = new HashMap<Integer, Model>();
	private Map<Integer, MoLocation> locMap = new HashMap<Integer, MoLocation>();
	private Map<String, MoLoader<?>> fxMoMap;

	public MoApi() {
		moMap = Collections.synchronizedMap(new HashMap<Long, Mo>());
		moMapKey = Collections.synchronizedMap(new HashMap<String, Mo>());
		fxMoMap = Collections.synchronizedMap(new HashMap<String, MoLoader<?>>());
	}

	/**
	 * 관리대상을 추가한다.
	 * 
	 * @param mo     추가할 관리대상
	 * @param reason 추가하는 이유
	 * @return 추가된 관리대상
	 * @throws Exception
	 */
	public Mo addMo(Mo mo, String reason, int userNo) throws Exception {
		try {
			Mo moNew = doAdd(mo, getAutoAddChildren(mo), reason, userNo);
			addEvent(moNew, ALARM_CODE.MO_ADDED);
			return moNew;
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
			throw e;
		}
	}

	/**
	 * 관리대상을 삭제한다.
	 * 
	 * @param mo     삭제할 관리대상
	 * @param userNo 삭제하는 운용자
	 * @param reason 삭제 이유
	 * @return 삭제된 관리대상
	 * @throws Exception
	 */
	public List<Mo> deleteMo(Mo mo, int userNo, String reason) throws Exception {
		try {
			List<Mo> moList = doDelete(mo, userNo, reason);
			addEvent(mo, ALARM_CODE.MO_DELETED);
			return moList;
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
			throw e;
		}
	}

	/**
	 * 관리대상으로부터 구성 정보를 가져온다.
	 * 
	 * @param mo 관리대상
	 * @return 구성 정보
	 * @throws Exception
	 */
	public MoConfig detect(Mo mo) throws Exception {

		MoConfig children = new MoConfig(mo);

		MoService service = ServiceApi.getApi().getService(MoService.class, mo);

		return service.getConfigChildren(children);
	}

	/**
	 * 메모리에 적재된 관리대상을 조회한다.
	 * 
	 * @param tag 구분자
	 * @param key 관리대상 키
	 * @return 관리대상
	 */
	public Mo getFxMo(String tag, String key) {
		MoLoader<?> moMap = fxMoMap.get(tag);
		return moMap.getMo(key);
	}

	/**
	 * 메모리에 적재된 관리대상을 조회한다.
	 * 
	 * @param moNo 관리번호
	 * @return 관리대상
	 */
	public Mo getMo(long moNo) {
		return getMo(moNo, false);
	}

	/**
	 * 관리대상을 조회한다.
	 * 
	 * @param moNo   관리번호
	 * @param reload 저장소 직접 조회여부
	 * @return 관리대상
	 */
	public Mo getMo(long moNo, boolean reload) {

		Mo mo = null;

		if (reload == false) {
			mo = moMap.get(moNo);
		}

		if (mo == null) {
			try {
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("moNo", moNo);
				List<Mo> list = doSelectMo(para, true);
				if (list.size() > 0) {
					mo = list.get(0);
				}
			} catch (Exception e) {
				Logger.logger.error(e);
				return null;
			}
			if (mo != null) {
				moMap.put(mo.getMoNo(), mo);
				moMapKey.put(mo.getMoKey(), mo);
			}
		}

		return mo;
	}

	/**
	 * 상위관리대상에 포함된 관리대상을 조회한다.
	 * 
	 * @param upperMoNo 상위관리대상번호
	 * @param moClass   관리대상종류
	 * @param moName    관리대상명
	 * @param reload    저장소 직접 조회여부
	 * @return 관리대상
	 */
	public Mo getMo(long upperMoNo, String moClass, String moName, boolean reload) {

		Mo mo = null;
		String moUk = Mo.makeMoKey(upperMoNo, moClass, moName);

		if (reload == false) {
			mo = moMapKey.get(moUk);
		}

		if (mo == null) {
			try {

				Map<String, Object> para = new HashMap<String, Object>();
				para.put("upperMoNo", upperMoNo);
				para.put("moClass", moClass);
				para.put("moName", moName);
				List<Mo> list = doSelectMo(para, true);
				if (list.size() > 0) {
					mo = list.get(0);
				}

			} catch (Exception e) {
				FxServiceImpl.logger.error(e);
				return null;
			}
			if (mo != null) {
				moMap.put(mo.getMoNo(), mo);
				moMapKey.put(mo.getMoKey(), mo);
			}
		}

		return mo;
	}

	public Mo getMo(Map<String, Object> parameters) {
		try {
			List<Mo> moList = doSelectMo(parameters, true);
			return moList.size() > 0 ? moList.get(0) : null;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	/**
	 * 관리대상분류의 클래스를 조회한다.
	 * 
	 * @param moClass 관리대상분류
	 * @return 해당 클래스
	 */
	public Class<? extends Mo> getMoClass(String moClass) {
		Class<? extends Mo> classOfMo = getMoJavaClass(moClass);
		return classOfMo == null ? Mo.class : classOfMo;
	}

	/**
	 * 관리대상의 구성을 조회한다.
	 * 
	 * @param mo 관리대상
	 * @return 구성
	 * @throws Exception
	 */
	public MoConfig getMoConfig(Mo mo) throws Exception {

		if (mo == null) {
			throw new NotFoundException("mo", "");
		}

		MoConfig children = new MoConfig(mo);

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("upperMoNo", mo.getMoNo());
		List<Mo> list = doSelectMo(para, true);
		for (Mo a : list) {
			children.addMo(a);
		}

		return children;
	}

	/**
	 * 모델을 조회한다.
	 * 
	 * @param modelNo 모델번호
	 * @return 조회된 모델
	 */
	public Model getModel(int modelNo) {

		if (modelMap.size() == 0) {

			Map<Integer, Model> tmp = new HashMap<Integer, Model>();
			try {
				List<Model> modelList = doSelectModelList();
				for (Model model : modelList) {
					tmp.put(model.getModelNo(), model);
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}

			modelMap = tmp;

		}

		return modelMap.get(modelNo);
	}

	/**
	 * 관리대상 목록을 조회한다.
	 * 
	 * @param para 조건
	 * @return 조회된 목록
	 * @throws Exception
	 */
	public List<Mo> getMoList(Map<String, Object> para) throws Exception {
		return (List<Mo>) doSelectMo(para, true);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getMoList(Map<String, Object> para, Class<T> classOfMo) throws Exception {
		return (List<T>) doSelectMo(para, true);
	}

	/**
	 * 번호에 해당되는 설치위치를 조회한다.
	 * 
	 * @param inloNo 설치위치관리번호
	 * @return 설치위치
	 */
	public MoLocation getMoLocation(int inloNo) {

		if (locMap.size() == 0) {

			Map<Integer, MoLocation> tmp = new HashMap<Integer, MoLocation>();
			try {
				List<MoLocation> locList = doSelectLocationList();
				for (MoLocation inlo : locList) {
					tmp.put(inlo.getInloNo(), inlo);
				}

				MoLocation parent;

				for (MoLocation inlo : locList) {
					parent = tmp.get(inlo.getUpperInloNo());
					if (parent != null) {
						parent.getChildren().add(inlo);
						inlo.setParent(parent);
					}
				}

			} catch (Exception e) {
				Logger.logger.error(e);
			}

			locMap = tmp;

		}

		return locMap.get(inloNo);
	}

	/**
	 * 입력된 설치위치번호와 관련된 설치위치종류에 해당되는 설치위치를 조회한다.
	 * 
	 * @param inloNo   설치위치번호
	 * @param inloType 설치위치종류
	 * @return 해당 설치위치
	 */
	public MoLocation getMoLocation(int inloNo, String inloType) {

		MoLocation location = getMoLocation(inloNo);
		while (location != null) {
			if (location.getInloType().equals(inloType)) {
				return location;
			}
			location = getMoLocation(location.getUpperInloNo());
		}

		return null;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

	/**
	 * 
	 * @return 시스템 관리대상
	 */
	public Mo getSystemMo() {
		if (systemMo == null) {
			systemMo = getMo(PROJECT_MONO);
			if (systemMo == null) {
				Mo mo = new Mo();
				mo.setMoName("SYSTEM");
				mo.setMoNo(PROJECT_MONO);
				mo.setMoClass("MO");

				try {
					addMo(mo, "default", User.USER_NO_SYSTEM);
					systemMo = getMo(PROJECT_MONO);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}
		return systemMo;
	}

	/**
	 * 관리설치위치에 포함되는지 판단한다.
	 * 
	 * @param inloNo    판단한 설치위치번호
	 * @param mngInloNo 관리설치위치번호
	 * @return 포함여부
	 */
	public boolean isMemberLocation(int inloNo, int mngInloNo) {

		MoLocation loc = getMoLocation(inloNo);

		while (loc != null) {
			if (loc.getInloNo() == mngInloNo) {
				return true;
			}

			loc = loc.getParent();
		}

		return false;
	}

	/**
	 * 관리대상을 조회하여 메모리에 적재한다.
	 * 
	 * @param loaderTag 적재자 키. null이거나 공백이면 메모리에 적재하지 않고 loader에만 넣음
	 * @param loader    관리대상 적재자
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void loadFxMo(String loaderTag, MoLoader loader) throws Exception {

		Logger.logger.trace("moClass={}, para={}", loader.getMoClass(), loader.getPara());

		Map<String, Object> para = new HashMap<String, Object>();
		if (loader.getPara() != null) {
			para.putAll(loader.getPara());
		}
		para.put("moClass", loader.getMoClass());

		List<Mo> moList = doSelectMo(para, true);

		if (loaderTag != null) {
			fxMoMap.remove(loaderTag);
			fxMoMap.put(loaderTag, loader);
		}

		loader.setMoList(moList);
	}

	/**
	 * 새로운 관리대상 클래스를 만든다.
	 * 
	 * @param moClass 관리대상 종류
	 * @return 빈 틀래스
	 * @throws Exception
	 */
	public Mo makeNewMo(String moClass) throws Exception {
		Class<? extends Mo> classOfMo = getMoClass(moClass);
		return classOfMo.newInstance();
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		if (noti instanceof Mo) {
			Mo mo = (Mo) noti;
			moMap.remove(mo.getMoNo());
			moMapKey.remove(mo.getMoKey());
		}

		if (noti instanceof ReloadSignal) {
			ReloadSignal reload = (ReloadSignal) noti;
			if (reload.contains(ReloadSignal.RELOAD_TYPE_ALL, ReloadSignal.RELOAD_TYPE_MO,
					ReloadSignal.RELOAD_TYPE_CFG)) {
				reload();
			}
		}
	}

	/**
	 * 적재자의 관리대상을 추가한다.
	 * 
	 * @param tag
	 * @param mo
	 */
	public void putFxMo(String tag, Mo mo) {
		MoLoader<?> moMap = fxMoMap.get(tag);
		if (moMap != null) {
			moMap.addMo(mo);
		}
	}

	/**
	 * 관리대상의 구성을 저장소에 적용한다.
	 * 
	 * @param children 적용할 내용
	 * @throws Exception
	 */
	public void setMoChildren(MoConfig children) throws Exception {

		Logger.logger.info(children.getParent().getMoName());

		try {

			doSetMoChildren(children);

			for (Mo mo : children.getMoListAll()) {
				if (mo.getStatus() == FxEvent.STATUS.added) {
					addEvent(mo, ALARM_CODE.MO_ADDED);
				} else if (mo.getStatus() == FxEvent.STATUS.changed) {
					addEvent(mo, ALARM_CODE.MO_UPDATED);
				} else if (mo.getStatus() == FxEvent.STATUS.deleted) {
					addEvent(mo, ALARM_CODE.MO_DELETED);
				}
			}

		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
			throw e;
		}
	}

	/**
	 * 관리대상을 업데이트한다.
	 * 
	 * @param mo 관리대상
	 * @return 업데이트된 관리대상
	 * @throws Exception
	 */
	public Mo updateMo(Mo mo) throws Exception {

		try {

			Mo moNew = doUpdate(mo, getAutoAddChildren(mo));

			addEvent(moNew, ALARM_CODE.MO_UPDATED);

			return moNew;

		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
			throw e;
		}
	}

	/**
	 * 관리대상을 업데이트한다.
	 * 
	 * @param mo            관리대상
	 * @param newParameters 업데이트할 내용
	 * @return 업데이트된 관리대상
	 * @throws Exception
	 */
	public Mo updateMo(Mo mo, Map<String, Object> newAttr) throws Exception {

		try {

			Mo moNew = doUpdate(mo, newAttr);

			addEvent(moNew, ALARM_CODE.MO_UPDATED);

			return moNew;

		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
			throw e;
		}
	}

	protected abstract Mo doAdd(Mo mo, MoConfig children, String reason, int userNo) throws Exception;

	protected abstract List<Mo> doDelete(Mo mo, int userNo, String reason) throws Exception;

	protected abstract List<MoLocation> doSelectLocationList() throws Exception;

	/**
	 * 
	 * @param parameters
	 * @param isDeep     상세 정보 조회
	 * @return
	 * @throws Exception
	 */
	protected abstract List<Mo> doSelectMo(Map<String, Object> parameters, boolean isDeep) throws Exception;

	protected abstract List<Model> doSelectModelList() throws Exception;

	protected abstract void doSetMoChildren(MoConfig children) throws Exception;

	protected abstract Mo doUpdate(Mo mo, Map<String, Object> parameters) throws Exception;

	protected abstract Mo doUpdate(Mo mo, MoConfig children) throws Exception;

	protected Class<?> getDboJavaClass(String moClass) {
		for (MoClassData data : moClassList) {
			if (data.moClass.equals(moClass)) {
				return data.dboJavaClass;
			}
		}
		return null;
	}

	protected Class<? extends Mo> getMoJavaClass(String moClass) {
		for (MoClassData data : moClassList) {
			if (data.moClass.equals(moClass)) {
				return data.javaClass;
			}
		}
		return null;
	}

	@Override
	protected void initApi() throws Exception {
		this.loadMoClass();
	}

	protected void reload() throws Exception {

		moMap.clear();
		moMapKey.clear();
		modelMap.clear();
		locMap.clear();

		String loaderTag[] = fxMoMap.keySet().toArray(new String[fxMoMap.size()]);

		for (String tag : loaderTag) {
			try {
				loadFxMo(tag, fxMoMap.get(tag));
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

	}

	private MoConfig getAutoAddChildren(Mo parent) {
		MoConfig children = null;
		List<AutoAddChild> filterList = FxActorParser.getParser().getActorList(AutoAddChild.class);
		if (filterList.size() > 0) {
			Mo child;
			for (AutoAddChild filter : filterList) {
				if (filter.getFxPara().match(parent)) {
					try {
						child = filter.getMo(parent);
						if (child != null) {
							if (children == null) {
								children = new MoConfig(parent);
							}
							children.addDetectedMo(child);
							Logger.logger.trace("auto-add-child={}", child);
						}
					} catch (Exception e) {
						Logger.logger.error(e);
					}
				}
			}
		}
		return children;
	}

	private void loadMoClass() {

		File folder = new File(FxCfg.getPathMo());

		List<FxActor> fList = new ArrayList<FxActor>();

		if (folder.exists() == false) {
			Logger.logger.fail("FILE-NAME({}) NOT FOUND", folder.getPath());
			return;
		}

		List<File> fileList = new ArrayList<File>();
		StringBuffer sb = new StringBuffer();
		for (File f : folder.listFiles()) {
			try {
				sb.append(Logger.makeSubString(0, f.getName(), "parsing"));
				parse(f, fList, sb);
				fileList.add(f);
			} catch (Exception e) {
				Logger.logger.error(e);
				sb.append(Logger.makeSubString(0, f.getName(), "error"));
			}
		}

		Logger.logger.info(Logger.makeString("mo-class", folder.getPath(), sb.toString()));

	}

	@SuppressWarnings("unchecked")
	private void parse(File file, List<FxActor> fList, StringBuffer msg) throws Exception {

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(file));
		} catch (Exception e) {
			throw e;
		}

		Element root = document.getRootElement();
		List<Element> nodeList = root.getChildren();

		for (Element node : nodeList) {

			if (node.getName().equals("mo")) {
				parseMo(node, msg);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void parseMo(Element node, StringBuffer msg) {

		MoClassData data = new MoClassData();

		data.moClass = node.getAttributeValue("mo-class");
		String dboJavaClass = node.getAttributeValue("dbo-java-class");
		String javaClass = node.getAttributeValue("java-class");
		if (data.moClass == null || javaClass == null) {
			return;
		}

		try {
			data.javaClass = (Class<? extends Mo>) Class.forName(javaClass);
			if (dboJavaClass != null) {
				try {
					data.dboJavaClass = (Class<?>) Class.forName(dboJavaClass);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
			moClassList.add(data);
			msg.append(Logger.makeSubString(1, data.moClass,
					javaClass + (dboJavaClass == null ? "" : " (" + dboJavaClass + ")")));
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

}

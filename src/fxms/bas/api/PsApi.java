package fxms.bas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.exp.NotFoundException;
import fxms.bas.exp.PsItemNotFoundException;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * Performance & State API
 * 
 * @author subkjh
 *
 */
public abstract class PsApi extends FxApi {

	/** use DBM */
	public static PsApi api;

	public static final String MO_STATUS_PS_ID = "MoStatus";

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static PsApi getApi() {
		if (api != null)
			return api;

		api = makeApi(PsApi.class);

		try {
			api.reload(ReloadType.All);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	/**
	 * 관리대상 상태를 나타내는 성능ID 여부
	 * 
	 * @param psId
	 * @return
	 */
	public static boolean isMoStatusPsId(String psId) {
		return MO_STATUS_PS_ID.equalsIgnoreCase(psId);
	}

	public static Map<String, PsItem> toIdMap(List<PsItem> list) {
		Map<String, PsItem> psMap = new HashMap<String, PsItem>();
		for (PsItem item : list) {
			psMap.put(item.getPsId(), item);
		}
		return psMap;
	}

	public static Map<String, PsItem> toNameMap(List<PsItem> list) {
		Map<String, PsItem> psMap = new HashMap<String, PsItem>();
		for (PsItem item : list) {
			psMap.put(item.getPsName(), item);
		}
		return psMap;
	}

	/** 성능ID 기준 항목 */
	private final Map<String, PsItem> psItemMap;

	/** 테이블 기준 성능항목 */
	private Map<String, List<PsItem>> tableMap;

	protected final List<PsKind> psKindList;

	private PsKind rawPsKind = null;

	private boolean enableMoStatus = false;

	public PsApi() {
		psItemMap = new HashMap<String, PsItem>();
		tableMap = new HashMap<String, List<PsItem>>();
		psKindList = new ArrayList<PsKind>();
	}

	/**
	 * 
	 * @param psName
	 * @return
	 * @throws Exception
	 */
	public PsItem findItemByName(String psName) throws Exception {
		List<PsItem> list = selectPsItem(makePara("psName", psName));
		return list.size() == 1 ? list.get(0) : null;
	}

	/**
	 * 성능항목을 찾는다.
	 * 
	 * @param psId
	 * @return
	 * @throws PsItemNotFoundException
	 */
	public PsItem getPsItem(String psId) throws PsItemNotFoundException {
		PsItem item = null;
		synchronized (this.psItemMap) {
			item = this.psItemMap.get(psId);
			if (item != null)
				return item;
		}
		throw new PsItemNotFoundException(psId);
	}

	/**
	 * 
	 * @param psTable
	 * @return
	 */
	public List<PsItem> getPsItemList(String psTable) {
		return tableMap.get(psTable);
	}

	/**
	 * 입력 데이터에 해당되는 성능항목을 조회한다.<br>
	 * 
	 * @param moClass 관리대상분류
	 * @param moType  관리대상유형
	 * @return
	 */
	public List<PsItem> getPsItemList(String moClass, String moType) {

		List<PsItem> itemList = new ArrayList<PsItem>();

		synchronized (this.psItemMap) {
			for (PsItem o : this.psItemMap.values()) {
				if (moClass != null && moClass.equals(o.getMoClass())) {
					if (moType != null && moType.equals(o.getMoType())) {
						itemList.add(o);
					}
				}
			}
		}

		return itemList;
	}

	/**
	 * 같은 테이블, 같은 성능 그룹 목록을 조회한다.
	 * 
	 * @param psId
	 * @return
	 */
	public List<PsItem> getPsItemRelation(String psId) {
		List<PsItem> itemList = new ArrayList<PsItem>();

		PsItem item;
		try {
			item = getPsItem(psId);
			synchronized (this.psItemMap) {
				for (PsItem o : this.psItemMap.values()) {
					if (isSame(o.getPsTable(), item.getPsTable()) && isSame(o.getPsGrp(), item.getPsGrp())) {
						itemList.add(o);
					}
				}
			}
		} catch (PsItemNotFoundException e) {
		}

		return itemList;
	}

	/**
	 * 사용중인 전체 성능항목을 조회한다.
	 * 
	 * @return
	 */
	public PsItem[] getPsItems() {
		synchronized (this.psItemMap) {
			return this.psItemMap.values().toArray(new PsItem[this.psItemMap.size()]);
		}
	}

	/**
	 * 성능ID를 키로 성능 목록을 제공한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PsItem> getPsItemsIncludeNotUse() throws Exception {
		List<PsItem> list = selectPsItem(null);
		return list;
	}

	/**
	 * 성능통계종류를 조회한다.
	 * 
	 * @param psKindName
	 * @return
	 */
	public PsKind getPsKind(String psKindName) throws NotFoundException {

		for (PsKind e : this.psKindList) {
			if (e.getPsKindName().equals(psKindName)) {
				return e;
			}
		}

		throw new NotFoundException("PsKind", psKindName);
	}

	/**
	 * 원천 종류를 이용하여 통계를 생성하는 종류를 조회한다.
	 * 
	 * @param psKindSrc
	 * @return
	 */
	public List<PsKind> getPsKind2Dst(PsKind psKindSrc) {
		List<PsKind> list = new ArrayList<PsKind>();
		for (PsKind e : this.psKindList) {
			if (psKindSrc.getPsKindName().equals(e.getPsDataSrc())) {
				list.add(e);
			}
		}
		return list;
	}

	public List<PsKind> getPsKindList() {
		return this.psKindList;
	}

	/**
	 * 원천데이터를 나타내는 종류를 갖는다.
	 * 
	 * @return
	 */
	public PsKind getPsKindRaw() {
		return this.rawPsKind;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		synchronized (this.psItemMap) {
			sb.append(Logger.makeSubString("psItem.size", this.psItemMap.size()));
		}
		sb.append(Logger.makeSubString("psKind.size", this.psKindList.size()));
		return sb.toString();
	}

	public boolean isEnableMoStatus() {
		return enableMoStatus;
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		if (type == ReloadType.All || type == ReloadType.PsItem) {
			loadPsItem();
		}
	}

	/**
	 * 특정 성능항목을 조회한다.
	 * 
	 * @param psId
	 * @return
	 * @throws Exception
	 */
	protected abstract List<PsItem> selectPsItem(Map<String, Object> para) throws Exception;

	/**
	 * 성능통계 종류를 가져온다.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract List<PsKind> selectPsKind() throws Exception;

	private void loadPsItem() throws Exception {

		List<PsItem> itemList = selectPsItem(makePara("useYn", "Y"));
		List<PsKind> kindList = selectPsKind();

		if (itemList != null) {

			this.enableMoStatus = false;

			Map<String, PsItem> itemTmp = new HashMap<String, PsItem>();
			Map<String, List<PsItem>> map = new HashMap<String, List<PsItem>>();

			List<PsItem> colList;
			for (PsItem item : itemList) {
				colList = map.get(item.getPsTable());
				if (colList == null) {
					colList = new ArrayList<PsItem>();
					map.put(item.getPsTable(), colList);
				}
				colList.add(item);

				itemTmp.put(item.getPsId(), item);

				// 관리대상상태 성능ID가 있으면 설정한다.
				if (item.getPsId().equals(MO_STATUS_PS_ID)) {
					this.enableMoStatus = true;
				}
			}

			save2file("ps.item.list", itemList);
			save2file("ps.kind.list", kindList);

			Logger.logger.info(Logger.makeString("LOADED PS-ITEM INFO", itemTmp.size()));

			synchronized (this.psItemMap) {
				this.psItemMap.clear();
				this.psItemMap.putAll(itemTmp);
			}
			this.tableMap = map;
		}

		if (kindList != null) {

			this.psKindList.clear();
			for (PsKind e : kindList) {
				if (e.isRaw()) {
					this.rawPsKind = e;
				}
				this.psKindList.add(e);
			}

		}
	}

}

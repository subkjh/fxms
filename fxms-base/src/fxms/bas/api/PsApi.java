package fxms.bas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.poller.beans.PollerCfg;
import fxms.bas.poller.beans.PsMsIpaddr;
import fxms.bas.pso.item.PsItem;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

/**
 * Performance & State API
 * 
 * @author subkjh
 *
 */
public abstract class PsApi extends FxApi {

	/** use DBM */
	public static PsApi api;

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
			api.reload();
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	private Map<String, PsItem> itemMap;
	private Map<String, List<PsItem>> tableMap;
	transient private PollerCfg pollerCfg;
	private PsMsIpaddr psMsIpaddr;

	public PsApi() {
		itemMap = new HashMap<String, PsItem>();
		tableMap = new HashMap<String, List<PsItem>>();
		pollerCfg = new PollerCfg();
	}

	/**
	 * 관제할 MO 목록을 저장소로부터 읽어옵니다.
	 * 
	 * @return 관제대상MO목록<br>
	 *         조회하지 못한 경우는 반드시 null로 제공합니다.
	 */
	public abstract <T> List<T> doSelectMoList(Class<T> classOfT, Map<String, Object> para) throws Exception;

	public PsItem getItem(String psCode) {
		return itemMap.get(psCode);
	}

	/**
	 * 폴링할 관리대상 목록 전체를 가져옵니다.
	 * 
	 * @return 관리대상 목록<br>
	 *         null인 경우 자료를 가져오지 못한 경우로 이전 내용을 그대로 사용한다.
	 */
	public <T> List<T> getMoAll(Class<T> classOfT, Map<String, Object> para) {
		try {
			return doSelectMoList(classOfT, para);
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}

	}

	/**
	 * 수집주기 정보를 조회합니다.
	 * 
	 * @return 수집주기 정보
	 */
	public PollerCfg getPollerCfg() {
		return pollerCfg;
	}

	public List<PsItem> getPsItemList(String psTable) {
		return tableMap.get(psTable);
	}

	public PsItem[] getPsItems() {
		return itemMap.values().toArray(new PsItem[itemMap.size()]);
	}

	public PsMsIpaddr getPsMsIpaddr() {
		return psMsIpaddr;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return String.valueOf(psMsIpaddr);
	}

	/**
	 * 수집주기 정보를 설정합니다.
	 * 
	 * @param pollerVar
	 *            수집주기 정보
	 */
	public void setPollerCfg(PollerCfg pollerCfg) {
		this.pollerCfg = pollerCfg;
	}

	protected abstract List<PsItem> doSelectPsItemAll() throws Exception;

	@Override
	protected void initApi() throws Exception {
		psMsIpaddr = new PsMsIpaddr(FxCfg.getCfg().getString(FxCfg.PARA_PS_MS_IPADDR, FxCfg.getCfg().getIpAddress()));
	}

	@Override
	protected void reload() throws Exception {

		List<PsItem> itemList = doSelectPsItemAll();

		if (itemList != null) {

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

				itemTmp.put(item.getPsCode(), item);
			}

			itemMap = itemTmp;
			tableMap = map;
		}

	}

}

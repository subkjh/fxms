package fxms.bas.api;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.adapter.FxAdapter;
import fxms.bas.fxo.adapter.FxGetValueAdapter;
import fxms.bas.fxo.adapter.FxMoAdapter;
import fxms.bas.impl.api.AdapterApiDfo;
import fxms.bas.mo.Mo;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * FxAdapter를 관리하는 API
 * 
 * @author subkjh
 *
 */
public abstract class AdapterApi extends FxApi {

	/** use DBM */
	public static AdapterApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static AdapterApi getApi() {

		if (api != null)
			return api;

		api = makeApi(AdapterApi.class);

		return api;
	}

	public static void main(String[] args) {
		AdapterApi.api = new AdapterApiDfo();
		AdapterApi.api.loadAdapters();
		System.out.println(FxmsUtil.toJson(api.getAdapters(FxGetValueAdapter.class, (Mo) null)));
	}

	protected final List<FxAdapter> adapters;

	public AdapterApi() {
		this.adapters = new ArrayList<FxAdapter>();
	}

	/**
	 * MO에 맞는 FxMoAdapter를 조회한다.
	 * 
	 * @param <T>
	 * @param classOfT 클래스
	 * @param mo       MO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getAdapters(Class<? extends FxMoAdapter> classOfT, Mo mo) {

		List<T> ret = new ArrayList<T>();

		synchronized (this.adapters) {
			for (FxAdapter a : this.adapters) {
				if (classOfT.isInstance(a)) {
					if (mo == null || ((FxMoAdapter) a).match(mo)) {
						ret.add((T) a);
					}
				}
			}
		}

		return ret;
	}

	/**
	 * 클래스에 해당되는 아답터를 조회한다.
	 * 
	 * @param <T>
	 * @param classOfT 클래스
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getAdapters(Class<T> classOfT) {

		List<T> ret = new ArrayList<T>();

		synchronized (this.adapters) {
			for (FxAdapter a : this.adapters) {
				if (classOfT.isInstance(a)) {
					ret.add((T) a);
				}
			}
		}

		return ret;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		synchronized (this.adapters) {
			sb.append(Logger.makeSubString("adapter.size", this.adapters.size()));
		}
		return sb.toString();
	}

	@Override
	public void onCreated() throws Exception {
		loadAdapters();
	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		if (type == ReloadType.All || type == ReloadType.Adapter) {
			loadAdapters();
		}
	}

	protected abstract List<FxAdapter> getFxAdapters() throws Exception;

	/**
	 * 
	 */
	private void loadAdapters() {

		long procMstime = System.currentTimeMillis();

		try {
			
			List<FxAdapter> tmpList = getFxAdapters();
			
			if (tmpList.size() > 0) {
				synchronized (this.adapters) {
					this.adapters.clear();
					this.adapters.addAll(tmpList);
				}
			}

			LogApi.getApi().addSystemLog(ReloadType.Adapter, procMstime, this.adapters.size(), null);

		} catch (Exception e) {
			Logger.logger.error(e);
			LogApi.getApi().addSystemLog(ReloadType.Adapter, procMstime, -1, e);
		}

	}

}

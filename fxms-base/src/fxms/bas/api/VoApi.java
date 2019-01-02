package fxms.bas.api;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.define.PS_TYPE;
import fxms.bas.fxo.FxCfg;
import subkjh.bas.log.LOG_LEVEL;

public abstract class VoApi extends FxApi {

	/** use DBM */
	public static VoApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static VoApi getApi() {
		if (api != null)
			return api;

		api = makeApi(VoApi.class);

		return api;
	}

	private final List<PS_TYPE> pstypeList = new ArrayList<PS_TYPE>();
	
	public abstract String dropPsTables() throws Exception;

	public abstract String dropPsTables(PS_TYPE pstype, long hstime) throws Exception;
	
	public List<PS_TYPE> getPstypeList() {

		if (pstypeList.size() == 0) {

			String pstype = FxCfg.getCfg().getString(FxCfg.PARA_PS_TYPE, null);
			if (pstype != null) {
				String ss[] = pstype.split(",");
				PS_TYPE type;
				for (String s : ss) {
					type = PS_TYPE.getPsType(s);
					if (type != null) {
						pstypeList.add(type);
					}
				}
			}
		}

		return pstypeList;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}

	public abstract String makePsTables() throws Exception;

	public abstract String makePsTables(PS_TYPE pstype, long hstime) throws Exception;

	@Override
	protected void initApi() throws Exception {
		// nothing
	}

	@Override
	protected void reload() throws Exception {
		// nothing
	}
}

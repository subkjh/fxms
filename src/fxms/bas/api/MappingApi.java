package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.DATA_STATUS;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.mapp.MappData;
import fxms.bas.vo.mapp.MappMo;
import fxms.bas.vo.mapp.MappMoPs;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 *
 */
public abstract class MappingApi extends FxApi {

	public class MappMoAl {
		public final long moNo;
		public final int alcdNo;

		public MappMoAl(long moNo, int alcdNo) {
			this.moNo = moNo;
			this.alcdNo = alcdNo;
		}
	}

	/** use DBM */
	public static MappingApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static MappingApi getApi() {

		if (api != null)
			return api;

		api = makeApi(MappingApi.class);

		try {
			api.reload(ReloadType.MappData);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	/**
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	protected abstract List<MappMo> doSelectMapp(Map<String, Object> para) throws Exception;

	protected abstract int doSelectMappAlcdNo(String mngDiv, String mappId) throws Exception;

	protected abstract Object doSelectMappEtc(String mngDiv, Object mappId) throws Exception;

	protected abstract List<MappMoPs> doSelectMappMoPs(Map<String, Object> para) throws Exception;

	/**
	 * 관리구분의 성능 매핑 데이터를 mappId를 키로 조회한다.
	 * 
	 * @param mngDiv
	 * @return
	 * @throws Exception
	 */
	protected abstract Map<String, MappMoPs> doSelectMappMoPsAll(String mngDiv) throws Exception;

	protected abstract void doSetEtc(int userNo, MappData mappData, Object objData, String objDescr) throws Exception;

	public int getMappAlcdNo(String mngDiv, String mappId) throws Exception {
		return doSelectMappAlcdNo(mngDiv, mappId);

	}

	public Object getMappEtc(String mngDiv, Object mappId) throws Exception {
		return doSelectMappEtc(mngDiv, mappId);
	}

	/**
	 * 구분에 해당되는 매핑 데이터를 조회한다.
	 * 
	 * @param mngDiv 구분
	 * @return
	 * @throws Exception
	 */
	public Map<String, Long> getMappMoAll(String mngDiv) throws Exception {

		List<MappMo> list = doSelectMapp(makePara("mngDiv", mngDiv));
		Map<String, Long> ret = new HashMap<String, Long>();
		for (MappMo o : list) {
			ret.put(o.getMappId(), o.getMoNo());
		}
		return ret;
	}

	/**
	 * 매핑에 해당된느 관리대상 번호를 조회한다.
	 * 
	 * @param mngDiv 구분
	 * @param mappId 맵ID
	 * @return
	 */
	public long getMappMoNo(String mngDiv, Object mappId) {
		try {
			List<MappMo> list = doSelectMapp(makePara("mngDiv", mngDiv, "mappId", mappId));
			return list.size() > 0 ? list.get(0).getMoNo() : -1L;
		} catch (Exception e) {
			return -1;
		}
	}

	public List<MappMoPs> getMappMoPs(String mngDiv, long moNo) throws Exception {
		return doSelectMappMoPs(makePara("mngDiv", mngDiv, "moNo", moNo));
	}

	public MappMoPs getMappMoPs(String mngDiv, String mappId) throws Exception {
		List<MappMoPs> list = doSelectMappMoPs(makePara("mngDiv", mngDiv, "mappId", mappId));

		return list != null && list.size() == 1 ? list.get(0) : null;
	}

	/**
	 * 관제점에 대한 모든 매핑 정보를 조회한다.
	 * 
	 * @param mngDiv
	 * @return 관제점을 키로 하는 데이터
	 */
	public Map<String, MappMoPs> getMoPsAll(String mngDiv) {
		try {
			return doSelectMappMoPsAll(mngDiv);
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		if (type == ReloadType.All || type == ReloadType.MappData) {

		}

	}

	/**
	 * 
	 * @param userNo
	 * @param mappData
	 * @param objDate
	 * @param objDescr
	 * @throws Exception
	 */
	public void setMappEtc(int userNo, MappData mappData, Object objDate, String objDescr) throws Exception {
		doSetEtc(userNo, mappData, objDate, objDescr);
	}

	/**
	 * 관제점 매핑 기록
	 * 
	 * @param userNo   사용자번호
	 * @param mappData 매핑데이터
	 * @param moNo     관리대상번호
	 * @param moName   관리대상명
	 * @param psId     성능항목
	 * @param psName   성능명
	 * @throws Exception
	 */
	public abstract DATA_STATUS setMappPs(int userNo, MappData mappData, long moNo, String moName, String psId,
			String psName) throws Exception;

	public abstract DATA_STATUS removeMappPs(MappData mappData) throws Exception;

}

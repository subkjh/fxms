package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Inlo;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 설치 위치를 관리하는 API
 * 
 * @author subkjh
 *
 */

public abstract class InloApi extends FxApi {

	/** use */
	public static InloApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 *
	 * @return DBM
	 */
	public synchronized static InloApi getApi() {
		if (api != null)
			return api;

		api = makeApi(InloApi.class);

		try {
			api.reload(ReloadType.All);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		return api;
	}

	private final Map<Integer, Inlo> inloNoMap;

	private final Map<String, Inlo> inloNameMap;

	private Object lockObj = new Object();

	public InloApi() {
		this.inloNoMap = new HashMap<>();
		this.inloNameMap = new HashMap<>();
	}

	/**
	 * 설치위치를 조회한다.
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public abstract List<Inlo> selectInlos(Map<String, Object> para) throws Exception;

	/**
	 * 설치위치를 추가한다.
	 * 
	 * @param userNo
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public abstract int addInlo(int userNo, Map<String, Object> para) throws Exception;

	/**
	 * 번호에 해당되는 설치위치를 조회한다.
	 *
	 * @param inloNo 설치위치관리번호
	 * @return 설치위치
	 */
	public Inlo getInlo(Integer inloNo) {

		if (inloNo == null) {
			return null;
		}

		synchronized (this.lockObj) {
			Inlo inlo = this.inloNoMap.get(inloNo);
			if (inlo == null) {
				try {
					List<Inlo> list = selectInlos(makePara("inloNo", inloNo));
					if (list.size() > 0) {
						inlo = list.get(0);
						this.inloNoMap.put(inlo.getInloNo(), inlo);
						this.inloNameMap.put(inlo.getInloClCd() + "_" + inlo.getInloName(), inlo);
					}
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
			return inlo;
		}
	}

	/**
	 * 이름과 구분으로 설치위치를 조회한다.
	 * 
	 * @param inloName 이름
	 * @param inloClCd 설치위치구분코드
	 * @return
	 */
	public Inlo getInlo(String inloName, String inloClCd) {

		synchronized (this.lockObj) {
			Inlo inlo = this.inloNameMap.get(inloClCd + "_" + inloName);
			if (inlo == null) {
				try {
					List<Inlo> list = this.selectInlos(makePara("inloName", inloName, "inloClCd", inloClCd));
					if (list.size() > 0) {
						inlo = list.get(0);
						this.inloNoMap.put(inlo.getInloNo(), inlo);
						this.inloNameMap.put(inlo.getInloClCd() + "_" + inlo.getInloName(), inlo);
					}
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
			return inlo;
		}
	}

	/**
	 * 
	 * @param inloClCd
	 * @return
	 * @throws Exception
	 */
	public List<Inlo> getInlos(String inloClCd) throws Exception {
		return this.selectInlos(makePara("inloClCd", inloClCd));
	}

	/**
	 * 
	 * @param inloNo
	 * @param inloClCd
	 * @return
	 */
	public Inlo getUpperInlo(int inloNo, String inloClCd) {

		Inlo inlo = this.getInlo(inloNo);
		while (inlo != null) {
			if (inlo.getInloClCd().equals(inloClCd)) {
				return inlo;
			}
			if (inlo.getInloNo() == inlo.getUpperInloNo()) {
				break;
			}
			inlo = this.getInlo(inlo.getUpperInloNo());
		}

		return null;
	}

	public Inlo getInloTid(String inloTid) {
		try {
			List<Inlo> list = this.selectInlos(makePara("inloTid", inloTid));
			return list.size() > 0 ? list.get(0) : null;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		}
	}

	/**
	 * 구분에 해당되는 모든 매핑데이터를 조회한다.
	 * 
	 * @param mngDiv 구분
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, Integer> getMappInloAll(String mngDiv) throws Exception;

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		synchronized (this.lockObj) {
			sb.append(Logger.makeString("cached.value.size", this.inloNoMap.size()));
		}
		return sb.toString();
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		// 설치위치 변경
		if (type == ReloadType.Inlo || type == ReloadType.All) {

			synchronized (this.lockObj) {

				Logger.logger.debug("number of location in memory {} : cleared ", this.inloNoMap.size());

				this.inloNoMap.clear();
				this.inloNameMap.clear();

				List<Inlo> list = selectInlos(null);
				for (Inlo inlo : list) {
					this.inloNoMap.put(inlo.getInloNo(), inlo);
					this.inloNameMap.put(inlo.getInloClCd() + "_" + inlo.getInloName(), inlo);
				}

			}

		}

	}

	/**
	 * 설치위치를 업데이터한다.
	 * 
	 * @param userNo   운영자
	 * @param inloNo   설치위치
	 * @param para     속성
	 * @param mappData 매핑데이터
	 * @throws Exception
	 */
	public abstract boolean updateInlo(int userNo, int inloNo, Map<String, Object> para) throws Exception;

	/**
	 * 
	 * @return TID를 키로 설치위치 제공
	 * @throws Exception
	 */
	public Map<String, Inlo> getInloTidMap() throws Exception {
		Map<String, Inlo> ret = new HashMap<String, Inlo>();
		List<Inlo> list = InloApi.getApi().selectInlos(null);
		for (Inlo inlo : list) {
			if (inlo.getInloTid() != null)
				ret.put(inlo.getInloTid(), inlo);
		}
		return ret;
	}

	public abstract boolean removeInlo(int inloNo, String inloName) throws Exception;

}

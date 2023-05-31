package fxms.bas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Code;
import subkjh.bas.co.log.LOG_LEVEL;

/**
 * 환경변수 데이터 처리 API<br>
 * 환경 변수는 캐슁하지 않는다.
 * 
 * @author subkjh
 *
 */
public abstract class CodeApi extends FxApi {

	public static CodeApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static CodeApi getApi() {
		if (api != null)
			return api;

		api = makeApi(CodeApi.class);

		return api;
	}

	public CodeApi() {
	}

	/**
	 * 코드를 키로하는 코드 목록 조회<br>
	 * 
	 * @param cdClass
	 * @return
	 * @throws Exception
	 */
	public Map<String, Code> getCodeCdMap(String cdClass) throws Exception {
		Map<String, Code> ret = new HashMap<String, Code>();
		List<Code> list = getCodes(cdClass);
		for (Code code : list) {
			ret.put(code.getCdCode(), code);
		}
		return ret;
	}

	/**
	 * 이름을 키로하는 코드 목록 조회
	 * 
	 * @param cdClass
	 * @return
	 * @throws Exception
	 */
	public Map<String, Code> getCodeNameMap(String cdClass) throws Exception {
		Map<String, Code> ret = new HashMap<String, Code>();
		List<Code> list = getCodes(cdClass);
		for (Code code : list) {
			ret.put(code.getCdName(), code);
		}
		return ret;
	}

	/**
	 * 코드분류에 해당되는 코드 목록을 조회한다
	 * 
	 * @param cdClass
	 * @return
	 * @throws Exception
	 */
	public List<Code> getCodes(String cdClass) throws Exception {
		return selectCodes(makePara("cdCladd", cdClass));
	}

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		return sb.toString();
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void reload(Enum<?> type) throws Exception {
		if (ReloadType.Code == type || ReloadType.All == type) {
		}
	}

	protected abstract List<Code> selectCodes(Map<String, Object> para) throws Exception;
}

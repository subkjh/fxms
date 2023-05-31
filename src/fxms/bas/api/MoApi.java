package fxms.bas.api;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.event.FxEvent;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.FxMo;
import fxms.bas.mo.Mo;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.SyncMo;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 관리대상 관련 메소드를 가지는 API
 * 
 * @author subkjh
 *
 */
public abstract class MoApi extends FxApi {

	public static final String BASIC_MO_CLASS = "MO";
	public static final Mo NullMo = new FxMo(Mo.NULL_MO_NO);

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

	public static String getMoClass(Class<?> classOfT) {

		try {
			Field field = classOfT.getField("MO_CLASS");
			return field.get(null).toString();
		} catch (Exception e) {
		}

		return FxMo.MO_CLASS;
	}

	/**
	 * 입력된 인자를 이용하여 관리대상 객체를 생성한다.
	 * 
	 * @param para
	 * @param classOfMo
	 * @return
	 */
	public static Mo makeMo(Map<String, Object> para, Class<? extends Mo> classOfMo) {

		Mo mo = null;

		if (classOfMo != null) {
			try {
				mo = classOfMo.newInstance();
			} catch (Exception e) {
			}
		}

		if (mo == null) {
			mo = new FxMo();
		}

		ObjectUtil.toObject(para, mo);
		return mo;
	}

	/**
	 * 입력된 MO목록을 키가 moName인 맵을 제공한다.
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, Mo> toMoNameMap(List<Mo> list) {
		Map<String, Mo> retMap = new HashMap<String, Mo>();
		for (Mo mo : list) {
			if (mo.getMoName() != null)
				retMap.put(mo.getMoName().trim(), mo);
		}
		return retMap;
	}

	/**
	 * MO관리번호를 키로 관리대상을 가져온다.
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public static Map<Long, Mo> toMoNoMap(List<Mo> list) {
		Map<Long, Mo> retMap = new HashMap<Long, Mo>();
		for (Mo mo : list) {
			retMap.put(mo.getMoNo(), mo);
		}
		return retMap;
	}

	/**
	 * moTid기준으로 관리대상을 조회한다.
	 * 
	 * @param para
	 * @return
	 * @throws Exception
	 */
	public static <T extends Mo> Map<String, T> toMoTidMap(List<T> list) {
		Map<String, T> retMap = new HashMap<>();
		for (T mo : list) {
			if (mo.getMoTid() != null)
				retMap.put(mo.getMoTid().trim(), mo);
		}
		return retMap;
	}

	/** 조회된 관리대상을 잠시 메모리에 가지고 있는다. */
	private final Map<Long, Mo> moCacheMap;

	/** 프로젝트 관리대상을 의미한다 . */
	private Mo projectMo;

	public MoApi() {
		this.moCacheMap = new HashMap<Long, Mo>();
	}

	/**
	 * 
	 * @param userNo
	 * @param para
	 * @param reason
	 * @param broadcast
	 * @return
	 * @throws Exception
	 */
	public abstract Mo addMo(int userNo, String moClass, Map<String, Object> para, String reason, boolean broadcast)
			throws Exception;

	/**
	 * 관리대상을 삭제한다.
	 *
	 * @param userNo 삭제하는 운용자
	 * @param mo     삭제할 관리대상
	 * @param reason 삭제 이유
	 * @return 삭제된 관리대상
	 * @throws Exception
	 */
	public abstract Mo deleteMo(int userNo, long moNo, String reason, boolean broadcast)
			throws MoNotFoundException, Exception;

	/**
	 * 관리대상을 가져온다.<br>
	 * 캐쉬에 있으면 해당 내용을 사용하고 없으면 저장소에서 가져온다.
	 *
	 * @param moNo 관리번호
	 * @return 관리대상
	 */
	public abstract Mo getMo(long moNo) throws MoNotFoundException, Exception;

	/**
	 * 관리대상 목록을 조회한다.
	 *
	 * @param para 조건
	 * @return 조회된 목록
	 * @throws Exception
	 */
	public abstract List<Mo> getMoList(Map<String, Object> para) throws Exception;

	/**
	 * 관리대상을 조회한다.
	 * 
	 * @param <T>
	 * @param para      검색 조건
	 * @param classOfMo 리턴 MO분류
	 * @return
	 * @throws Exception
	 */
	public abstract <T extends Mo> List<T> getMoList(Map<String, Object> para, Class<T> classOfMo) throws Exception;

	/**
	 * 프로젝트의 관리대상을 조회한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized Mo getProjectMo() throws Exception {
		if (projectMo == null) {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("moType", "project");
			para.put("moName", FxCfg.getProjectName());
			try {
				List<Mo> list = getMoList(para);
				if (list.size() == 1) {
					projectMo = list.get(0);
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		if (projectMo == null) {
			throw new MoNotFoundException("moType=project,moName=" + FxCfg.getProjectName());
		}

		return projectMo;

	}

	/**
	 * 관리대상으로부터 가지고 있는 모든 데이터를 수집한다.
	 * 
	 * @param moNo
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract List<PsVoRaw> getRtValues(long moNo) throws Exception;

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		synchronized (this.moCacheMap) {
			sb.append(Logger.makeSubString("cached.mo.size", this.moCacheMap.size()));
		}
		return sb.toString();
	}

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void onEvent(FxEvent noti) throws Exception {

		if (noti instanceof Mo) {
		} else {
			super.onEvent(noti);
		}

	}

	@Override
	public void reload(Enum<?> type) throws Exception {

		// 관리대상 변경
		if (type == ReloadType.Mo || type == ReloadType.All) {
			synchronized (this.moCacheMap) {
				Logger.logger.debug("number of mo in memory {} : cleared ", this.moCacheMap.size());
				this.moCacheMap.clear();
			}
		}

	}

	/**
	 * 관리대상에 값을 설정한다.
	 * 
	 * @param mo     관리대상번호
	 * @param method 기능
	 * @param datas  설정데이터
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract boolean setupMo(long moNo, String method, Map<String, Object> datas) throws Exception;

	/**
	 * 관리대상의 환경 정보를 가져온다.
	 * 
	 * @param moNo   관리대상번호
	 * @param now    실시간처리여부
	 * @param update 저장소 기록여부
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public abstract SyncMo sync(long moNo, boolean now, boolean update) throws Exception;

	/**
	 * 관리대상의 속성을 수정한다.
	 * 
	 * @param userNo 사용자번호
	 * @param moNo   관리대상번호
	 * @param para   속성맵
	 * @return 업데이트된 관리대상
	 * @throws Exception
	 */
	public abstract Mo updateMo(int userNo, long moNo, Map<String, Object> para, boolean broadcast) throws Exception;

	protected Mo getMoCached(long moNo) {
		synchronized (this.moCacheMap) {
			return this.moCacheMap.get(moNo);
		}
	}

	protected void setMoCached(long moNo, Mo mo) {
		synchronized (this.moCacheMap) {
			this.moCacheMap.put(moNo, mo);
		}
	}
}

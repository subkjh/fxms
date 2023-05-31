package fxms.bas.api;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.CoCode.FXSVC_ST_CD;
import fxms.bas.exp.FxServiceNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.AppService;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.FxServiceMo;
import fxms.bas.mo.Mo;
import fxms.bas.mo.MoManageable;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 서비스 관련 API<br>
 * FxMS에서 기본적으로 사용하므로 이 클래스 또는 상속한 클래스에서는 다른 API를 사용하지 않는다.
 * 
 * @author subkjh
 *
 */
public abstract class ServiceApi extends FxApi {

	/** use DBM */
	public static ServiceApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static ServiceApi getApi() {
		if (api != null)
			return api;

		api = makeApi(ServiceApi.class);

		return api;
	}

	/** 서비스 목록 */
	private List<FxServiceMo> serviceList = new ArrayList<FxServiceMo>();
	/** 서비스 목록을 가져온 시간 */
	private long loadMstime = 0;

	/** 지정한 서비스의 URL를 갖는다. */
	private final Map<Class<?>, String> svcUrlMap = new HashMap<Class<?>, String>();

	public ServiceApi() {
	}

	@SuppressWarnings("unchecked")
	private <T> T getFxService(Class<T> classOfT, String url) throws Exception {

		Logger.logger.trace("classOfT={}, url={}", classOfT, url);

		try {
			Remote remote = Naming.lookup(url);
			if (classOfT.isInstance(remote)) {

				Logger.logger.info(Logger.makeString(classOfT.getSimpleName(), url));

				return (T) remote;
			}
			throw new FxServiceNotFoundException(url);

		} catch (NotBoundException e) {
			Logger.logger.fail(url);
			throw e;
		} catch (Exception e) {
			Logger.logger.fail(url);
			Logger.logger.error(e);
			throw e;
		}

	}

	/**
	 * 서비스 등록 정보를 가져온다.
	 * 
	 * @param fxServiceName 서비스명
	 * @param fxsvrIpAddr   서버주소, 서버주소가 정의되어 있지 않으면 명칭만 확인한다.
	 * @return
	 */
	private FxServiceMo getService(String fxServiceName, String fxsvrIpAddr) throws FxServiceNotFoundException {

		for (FxServiceMo service : getServiceList()) {

			if (service.getFxsvcName().equals(fxServiceName)) {
				if (fxsvrIpAddr == null || fxsvrIpAddr.length() == 0) {
					return service;
				} else if (fxsvrIpAddr.equals(service.getFxsvrIpAddr())) {
					return service;
				}
			}
		}

		throw new FxServiceNotFoundException(fxServiceName);
	}

	private List<FxServiceMo> getServiceByIpList(String fxsvrIpAddr) {

		if (fxsvrIpAddr == null) {
			return getServiceList();
		}

		List<FxServiceMo> moList = new ArrayList<FxServiceMo>();

		for (FxServiceMo service : getServiceList()) {
			if (fxsvrIpAddr.equals(service.getFxsvrIpAddr())) {
				moList.add(service);
			}
		}

		return moList;
	}

	/**
	 * 서비스 목록을 저장소로부터 가져온다.
	 */
	private synchronized void loadServiceList() {

		// 60초 이내에는 데이터를 다시 가져오지 않는다.
		if (loadMstime + 60000 >= System.currentTimeMillis()) {
			return;
		}

		List<FxServiceMo> list;
		try {

			// 프로젝트에 관련된 서비스만 조회한다.
			list = doServiceList(FxCfg.getProjectName());

			if (list != null) {
				if (list.size() != serviceList.size()) {
					logServicList(list);
					save2file("service.list", list);
				}
				serviceList = list;

				Logger.logger.info(Logger.makeString("LOADED SERVICE INFO", list.size()));

			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		loadMstime = System.currentTimeMillis();
	}

	/**
	 * 서비스 내역을 로깅한다.
	 * 
	 * @param list
	 */
	private void logServicList(List<FxServiceMo> list) {
		StringBuffer sb = new StringBuffer();
		for (FxServiceMo vo : list) {
			sb.append(Logger.makeSubString(vo.getFxsvrIpAddr(), vo.getFxsvcName()));
		}
		Logger.logger.info(Logger.makeString("FxService", list.size(), sb.toString()));
	}

	/**
	 * 서비스를 조회한다.
	 * 
	 * @param projName project 명
	 * @return
	 * @throws Exception
	 */
	protected abstract List<FxServiceMo> doServiceList(String projName) throws Exception;

	/**
	 * 
	 * @param status
	 * @throws Exception
	 */
	protected abstract void doSetAllServiceStatus(FXSVC_ST_CD status) throws Exception;

	/**
	 * 
	 * @param fxsvrIpAddr
	 * @param fxsvcName
	 * @param startDate
	 * @param serviceStatus
	 * @throws Exception
	 */
	protected abstract void doUpdateServiceStatus(String fxsvrIpAddr, String fxsvcName, long startDate,
			FXSVC_ST_CD serviceStatus, int rmiPort, int fxsvcPort) throws Exception;

	@Override
	public void reload(Enum<?> type) throws Exception {

		if (type == ReloadType.All) {
			loadServiceList();
		}

	}

	public FxServiceMo getMyServiceMo() {
		return getServiceMo(FxCfg.getIpAddress(), FxServiceImpl.serviceName);
	}

	/**
	 * 
	 * @param classOfT
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> classOfT) throws FxServiceNotFoundException, Exception {

		// 자신이 해당 서비스이면 자신을 넘긴다.
		if (classOfT.isInstance(FxServiceImpl.fxService)) {
			return (T) FxServiceImpl.fxService;
		}

		// 임의로 지정된 서비스의 URL이 있으면 사용한다.
		String url = this.svcUrlMap.get(classOfT);
		if (url != null) {
			return getFxService(classOfT, url);
		}

		// 등록된 서비스를 찾아 사용한다.
		FxServiceMo vo = getService(classOfT.getSimpleName(), null);
		if (vo != null) {
			return getFxService(classOfT, vo.getUrl());
		}

		throw new FxServiceNotFoundException(classOfT.getSimpleName());
	}

	public AppService getAppService() throws FxServiceNotFoundException, Exception {
		return getService(AppService.class);
	}

	/**
	 * 
	 * @param <T>
	 * @param classOfT
	 * @param mo
	 * @return
	 * @throws FxServiceNotFoundException
	 * @throws Exception
	 */
	public <T> T getService(Class<T> classOfT, Mo mo) throws FxServiceNotFoundException, Exception {

		String fxServerIp = null;

		if (mo instanceof MoManageable) {
			fxServerIp = ((MoManageable) mo).getFxServerIpAddr();
		}

		if (fxServerIp == null || fxServerIp.length() == 0) {
			fxServerIp = FxCfg.getIpAddress();
		}

		FxServiceMo service = getService(classOfT.getSimpleName(), fxServerIp);
		if (service != null) {
			return getFxService(classOfT, service.getUrl());

		}

		throw new FxServiceNotFoundException(classOfT.getSimpleName());
	}

	/**
	 * 현 서버에서 사용하는 서비스 목록을 조회한다.
	 * 
	 * @return
	 */
	public List<FxServiceMo> getServiceList() {

		loadServiceList();

		return serviceList;
	}

	/**
	 * 서비스 목록을 조회한다.
	 * 
	 * @param classOfT 조회할 서비스 클래스
	 * @param msIp     담당주소
	 * @return 서비스목록
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getServiceList(Class<T> classOfT, String fxServerIpAddr) {

		String ip = fxServerIpAddr;

		if (fxServerIpAddr == null || fxServerIpAddr.length() == 0) {
			ip = FxCfg.getIpAddress();
		}

		List<FxServiceMo> list = getServiceByIpList(ip);
		List<T> serviceList = new ArrayList<T>();

		for (FxServiceMo mo : list) {
			try {
				Remote remote = Naming.lookup(mo.getUrl());
				if (classOfT.isInstance(remote)) {
					serviceList.add((T) remote);
				}
			} catch (Exception e) {
			}
		}

		return serviceList;
	}

	public FxServiceMo getServiceMo(String fxsvrIpAddr, String fxsvcName) {
		for (FxServiceMo mo : serviceList) {
			if (mo.getFxsvrIpAddr().equals(fxsvrIpAddr) && mo.getFxsvcName().equals(fxsvcName)) {
				return mo;
			}
		}

		return null;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

	@Override
	public void onCreated() throws Exception {

		loadServiceList();

	}

	/**
	 * 모든 서비스의 상태를 변경한다.
	 * 
	 * @param status 변경할 상태
	 */
	public void setAllServiceStatus(FXSVC_ST_CD status) {
		try {
			doSetAllServiceStatus(status);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	public void setServiceUrl(Class<?> classOf, String url) {
		this.svcUrlMap.put(classOf, url);
	}

	/**
	 * 
	 * @param fxsvrIpAddr
	 * @param fxsvcName
	 * @param startDate
	 * @param serviceStatus
	 */
	public void updateServiceStatus(String fxsvrIpAddr, String fxsvcName, long startDate, FXSVC_ST_CD serviceStatus,
			int rmiPort, int fxsvcPort) {

		try {
			doUpdateServiceStatus(fxsvrIpAddr, fxsvcName, startDate, serviceStatus, rmiPort, fxsvcPort);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

}

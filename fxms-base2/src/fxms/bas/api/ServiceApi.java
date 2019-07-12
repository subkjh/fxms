package fxms.bas.api;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.co.exp.FxServiceNotFoundException;
import fxms.bas.co.vo.FxServiceVo;
import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.Mo;
import fxms.bas.mo.property.MoManageable;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

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
	private List<FxServiceVo> serviceList = new ArrayList<FxServiceVo>();
	/** 서비스를 나타내는 MO */
	private Mo myMo;

	public ServiceApi() {
	}

	/**
	 * 
	 * @param msIpaddr
	 * @param serviceName
	 * @param javaClass
	 * @throws Exception
	 */
	public void addService(String msIpaddr, String serviceName, String javaClass) throws Exception {
		try {

			doAddService(msIpaddr, serviceName, javaClass);

			FxServiceVo vo = new FxServiceVo();
			vo.setMsIpaddr(msIpaddr);
			vo.setServiceJavaClass(javaClass);
			vo.setServiceName(serviceName);

			serviceList.add(vo);

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	/**
	 * 
	 * @return 서비스관리대상
	 */
	public synchronized Mo getMyMo() {
		if (myMo == null) {
			try {
				myMo = doSelectMyMo(getMsIpAddr(), FxCfg.getFxServiceName());
			} catch (Exception e) {

				Logger.logger.error(e);

				myMo = new Mo();
				myMo.setMoNo(0);
				myMo.setMoName(getMsIpAddr() + "/" + FxCfg.getFxServiceName());
			}
		}
		return myMo;
	}

	/**
	 * 
	 * @param classOfT
	 * @return
	 * @throws Exception
	 */
	public <T> T getService(Class<T> classOfT) throws Exception {

		String url = FxCfg.getCfg().getUrl4Service(classOfT.getSimpleName());

		if (url != null) {
			try {
				return getFxService(classOfT, url);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return getService(classOfT, null);
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

		String msIp = null;

		if (mo instanceof MoManageable) {
			msIp = ((MoManageable) mo).getMsIpaddr();
		}

		if (msIp == null || msIp.length() == 0) {
			msIp = FxCfg.getCfg().getIpAddress(classOfT.getSimpleName());
			if (msIp == null) {
				msIp = FxCfg.getCfg().getIpAddress();
			}
		}

		FxServiceVo service = getService(classOfT.getSimpleName(), msIp);
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
	public List<FxServiceVo> getServiceList() {

		if (serviceList.size() == 0) {
			loadServiceList();
		}

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
	public <T> List<T> getServiceList(Class<T> classOfT, String msIp) {

		String ip = msIp;

		if (msIp == null || msIp.length() == 0) {
			ip = FxCfg.getCfg().getIpAddress(classOfT.getSimpleName());
			if (ip == null) {
				ip = FxCfg.getCfg().getIpAddress();
			}
		}

		List<FxServiceVo> list = getServiceList(ip);
		List<T> serviceList = new ArrayList<T>();

		for (FxServiceVo mo : list) {
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

	@Override
	public String getState(LOG_LEVEL level) {
		return null;
	}

	/**
	 * 서비스를 제거한다.
	 * 
	 * @param msIpaddr
	 * @param serviceName
	 * @throws Exception
	 */
	public void removeService(String msIpaddr, String serviceName) {
		try {

			doRemoveService(msIpaddr, serviceName);

			FxServiceVo vo;

			for (int i = serviceList.size(); i >= 0; i--) {
				vo = serviceList.get(i);
				if (vo.getMsIpaddr().equals(msIpaddr) && vo.getServiceName().equals(serviceName)) {
					serviceList.remove(i);
					break;
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	public void setAllServiceStatus(String status) {
		try {
			doSetAllServiceStatus(status);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	/**
	 * 
	 * @param msIpaddr
	 * @param serviceName
	 * @param startDate
	 * @param serviceStatus
	 */
	public void updateServiceStatus(String msIpaddr, String serviceName, long startDate, String serviceStatus) {

		try {
			doUpdateServiceStatus(msIpaddr, serviceName, startDate, serviceStatus);
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	protected void addNewService(FxServiceVo service) {
		if (service != null) {
			serviceList.add(service);
		}
	}

	/**
	 * 
	 * @param msIpaddr
	 * @param serviceName
	 * @param javaClass
	 * @throws Exception
	 */
	protected abstract void doAddService(String msIpaddr, String serviceName, String javaClass) throws Exception;

	/**
	 * 
	 * @param msIpaddr
	 * @param serviceName
	 * @throws Exception
	 */
	protected abstract void doRemoveService(String msIpaddr, String serviceName) throws Exception;

	/**
	 * 
	 * @param msIpaddr
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	protected abstract Mo doSelectMyMo(String msIpaddr, String serviceName) throws Exception;

	/**
	 * 서비스를 조회한다.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract List<FxServiceVo> doServiceList() throws Exception;

	/**
	 * 
	 * @param status
	 * @throws Exception
	 */
	protected abstract void doSetAllServiceStatus(String status) throws Exception;

	/**
	 * 
	 * @param msIpaddr
	 * @param serviceName
	 * @param startDate
	 * @param serviceStatus
	 * @throws Exception
	 */
	protected abstract void doUpdateServiceStatus(String msIpaddr, String serviceName, long startDate,
			String serviceStatus) throws Exception;

	/**
	 * 
	 * @return
	 */
	protected String getMsIpAddr() {
		return FxCfg.getCfg().getIpAddress();
	}

	/**
	 * 
	 * @param serviceName
	 * @param msIpaddr
	 * @return
	 */
	protected FxServiceVo getService(String serviceName, String msIpaddr) {

		for (FxServiceVo service : getServiceList()) {

			if (service.getServiceName().equals(serviceName)) {
				if (msIpaddr == null || msIpaddr.length() == 0) {
					return service;
				} else if (msIpaddr.equals(service.getMsIpaddr())) {
					return service;
				}
			}
		}

		return null;
	}

	@Override
	protected void initApi() throws Exception {

	}

	@Override
	protected void reload() throws Exception {

		loadServiceList();

	}

	@SuppressWarnings("unchecked")
	private <T> T getFxService(Class<T> classOfT, String url) throws Exception {

		Logger.logger.trace("classOfT={}, url={}", classOfT, url);

		try {
			Remote remote = Naming.lookup(url);
			if (classOfT.isInstance(remote)) {
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

	private List<FxServiceVo> getServiceList(String msIpaddr) {

		if (msIpaddr == null) {
			return getServiceList();
		}

		List<FxServiceVo> moList = new ArrayList<>();

		for (FxServiceVo service : getServiceList()) {
			if (msIpaddr.equals(service.getMsIpaddr())) {
				moList.add(service);
			}
		}

		return moList;
	}

	private void loadServiceList() {
		List<FxServiceVo> list;
		try {
			list = doServiceList();
			if (list != null) {
				serviceList = list;
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

}

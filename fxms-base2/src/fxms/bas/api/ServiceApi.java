package fxms.bas.api;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import fxms.bas.co.def.ALARM_CODE;
import fxms.bas.co.exp.FxServiceNotFoundException;
import fxms.bas.co.signal.ReloadSignal;
import fxms.bas.co.vo.FxVar;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.ServerMo;
import fxms.bas.mo.ServiceMo;
import fxms.bas.mo.attr.MoLoader;
import fxms.bas.mo.property.MoManageable;

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

	private MoLoader<ServerMo> serverLoader;
	private MoLoader<ServiceMo> serviceLoader;
	/** 환경변수 목록 */
	private Map<String, Object> varMap;

	public ServiceApi() {
		varMap = new HashMap<String, Object>();

		serverLoader = new MoLoader<ServerMo>(ServerMo.MO_CLASS, null) {
			@Override
			public String getKey(ServerMo mo) {
				return mo.getMsIpaddr();
			}
		};
		serverLoader.putPara("mngYn", true);

		serviceLoader = new MoLoader<ServiceMo>(ServiceMo.MO_CLASS, null) {
			@Override
			public String getKey(ServiceMo mo) {
				return mo.getMoName();
			}
		};
		serviceLoader.putPara("mngYn", true);
	}

	public void addService(String msIpaddr, String serviceName, String javaClass) throws Exception {

		try {
			ServerMo fxServer = getFxServer(msIpaddr);
			if (fxServer == null) {
				fxServer = (ServerMo) MoApi.getApi().makeNewMo(ServerMo.MO_CLASS);
				ServerMo.set(fxServer, msIpaddr, msIpaddr, "auto-insert");
				ServerMo newFxServer = (ServerMo) MoApi.getApi().addMo(fxServer, "service-added", User.USER_NO_SYSTEM);
				if (newFxServer != null) {
					serverLoader.addMo(newFxServer);
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		try {

			ServiceMo fxService = getService(serviceName, msIpaddr);

			if (fxService == null) {

				fxService = (ServiceMo) MoApi.getApi().makeNewMo(ServiceMo.MO_CLASS);
				ServiceMo.set(fxService, msIpaddr, serviceName);
				fxService.setServiceJavaClass(javaClass);
				fxService.setServiceStatus("ADDED");
				fxService.setStatusChgDate(FxApi.getDate(0));

				MoApi.getApi().addMo(fxService, "service-added", User.USER_NO_SYSTEM);

			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	public abstract void doSetServiceStatus(String serviceStatus) throws Exception;

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

		ServiceMo service = getService(classOfT.getSimpleName(), msIp);
		if (service != null) {
			return getFxService(classOfT, service.getUrl());

		}

		throw new FxServiceNotFoundException(classOfT.getSimpleName());
	}

	/**
	 * 서비스 목록을 조회한다.
	 * 
	 * @param classOfT
	 *            조회할 서비스 클래스
	 * @param msIp
	 *            담당주소
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

		List<ServiceMo> serviceMoList = getFxServiceMoList(ip);
		List<T> serviceList = new ArrayList<T>();

		for (ServiceMo mo : serviceMoList) {
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

	public int getVarValue(String varName, int valueDefault) {

		String value = null;

		try {
			value = getVarValue(varName);
			if (value == null) {
				return valueDefault;
			}
			return Integer.parseInt(value);

		} catch (Exception e) {
			Logger.logger.fail("var={}, value={}", varName, value);
			return valueDefault;
		}
	}

	public long getVarValue(String varName, long valueDefault) {

		try {
			String value;

			value = getVarValue(varName);
			if (value == null)
				return valueDefault;

			return Long.parseLong(value);
		} catch (Exception e) {
			Logger.logger.error(e);
			return valueDefault;
		}
	}

	public String getVarValue(String varName, String valueDefault) {
		try {
			String value = getVarValue(varName);
			if (value == null)
				return valueDefault;
			return value;
		} catch (Exception e) {
			Logger.logger.error(e);
			return valueDefault;
		}
	}

	public void removeService(String msIpaddr, String serviceName) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("msIpaddr", FxCfg.getCfg().getIpAddress());
		para.put("moClass", ServiceMo.MO_CLASS);

		List<ServiceMo> serviceList = MoApi.getApi().getMoList(para, ServiceMo.class);

		if (serviceList.size() > 0) {
			for (ServiceMo mo : serviceList) {
				if (mo.getServiceName().equals(serviceName)) {
					MoApi.getApi().deleteMo(mo, User.USER_NO_SYSTEM, "ms-delete");
					break;
				}
			}
		}

		serviceList = MoApi.getApi().getMoList(para, ServiceMo.class);
		if (serviceList.size() == 0) {
			para.put("moClass", ServerMo.MO_CLASS);
			List<ServerMo> serverList = (List<ServerMo>) MoApi.getApi().getMoList(para, ServerMo.class);
			if (serviceList.size() > 0) {
				for (ServerMo mo : serverList) {
					MoApi.getApi().deleteMo(mo, User.USER_NO_SYSTEM, "service-not-found");
				}
			}
		}
	}

	public void setVarValue(String varName, Object varValue, boolean broadcast) throws Exception {

		Logger.logger.debug("var={}, val={}, broadcast={}", varName, varValue, broadcast);

		try {

			doUpdateVarValue(varName, varValue);

			varMap.remove(varName);

			// 다시 적재 합니다.
			getVarValue(varName);

			if (broadcast && FxServiceImpl.fxService != null) {
				FxServiceImpl.fxService.send(new ReloadSignal(ReloadSignal.RELOAD_TYPE_VAR));
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

	public void updateServiceStatus(String msIpaddr, String serviceName, long startDate, String serviceStatus) throws Exception {

		try {
			ServerMo fxServer = getFxServer(msIpaddr);
			if (fxServer == null) {
				fxServer = (ServerMo) MoApi.getApi().makeNewMo(ServerMo.MO_CLASS);
				ServerMo.set(fxServer, msIpaddr, msIpaddr, "auto-insert");
				ServerMo newFxServer = (ServerMo) MoApi.getApi().addMo(fxServer, "service-added", User.USER_NO_SYSTEM);
				if (newFxServer != null) {
					serverLoader.addMo(newFxServer);
				}
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		try {

			ServiceMo fxService = getService(serviceName, msIpaddr);

			if (fxService == null) {

				fxService = (ServiceMo) MoApi.getApi().makeNewMo(ServiceMo.MO_CLASS);
				ServiceMo.set(fxService, msIpaddr, serviceName);
				fxService.setServiceStatus(serviceStatus);
				fxService.setStatusChgDate(FxApi.getDate(0));

				MoApi.getApi().addMo(fxService, "service-added", User.USER_NO_SYSTEM);

			} else {

				if (startDate != fxService.getStartDate() || serviceStatus.equals(fxService.getServiceStatus()) == false) {
					doSetServiceStatus(fxService, startDate, serviceStatus);
					fxService.setServiceStatus(serviceStatus);
					fxService.setStartDate(startDate);

					EventApi.getApi().check(fxService, String.valueOf(FxApi.getDate(0)), ALARM_CODE.SERVICE_ON_NOTI, serviceStatus,
							null);
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

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

	private Collection<ServiceMo> getFxServiceMoList() {
		if (serviceLoader.size() == 0) {
			try {
				MoApi.getApi().loadFxMo(null, serviceLoader);
			} catch (Exception e) {
				Logger.logger.error(e);
				return serviceLoader.getMoList();
			}
		}

		return serviceLoader.getMoList();
	}

	private List<ServiceMo> getFxServiceMoList(String msIpaddr) {

		if (msIpaddr == null) {
			return new ArrayList<>(getFxServiceMoList());
		}

		List<ServiceMo> moList = new ArrayList<>();

		for (ServiceMo service : getFxServiceMoList()) {
			if (msIpaddr.equals(service.getMsIpaddr())) {
				moList.add(service);
			}
		}

		return moList;
	}

	/**
	 * 
	 * @param varName
	 * @return
	 * @throws Exception
	 */
	private synchronized String getVarValue(String varName) throws Exception {

		if (varMap.size() == 0) {
			List<FxVar> varList = doSelectVarAll();
			for (FxVar var : varList) {
				varMap.put(var.getName(), var.getValue());
			}
		}

		Object value = varMap.get(varName);

		if (value == null) {
			try {
				FxVar var = doSelectVar(varName);
				String valueStr = var == null ? null : var.getValue();
				if (valueStr != null) {
					varMap.put(varName, valueStr);
				} else {
					Logger.logger.fail("VAR-NAME(" + varName + ") NOT DEFINED");
				}
				return valueStr;
			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		}
		return value.toString();
	}

	/**
	 * 환경변수 내역을 제공합니다.
	 * 
	 * @param varName
	 *            환경변수명
	 * @return 환경변수
	 * @throws Exception
	 */
	protected abstract FxVar doSelectVar(String varName) throws Exception;

	/**
	 * 환경변수를 저장소에서 조회합니다.
	 * 
	 * @return 환경변수 목록
	 * @throws Exception
	 */
	protected abstract List<FxVar> doSelectVarAll() throws Exception;

	/**
	 * 
	 * @param fxService
	 * @param startDate
	 * @param serviceStatus
	 * @throws Exception
	 */
	protected abstract void doSetServiceStatus(ServiceMo fxService, long startDate, String serviceStatus) throws Exception;

	/**
	 * 환경변수의 값을 설정합니다.<br>
	 * 
	 * @param varName
	 *            변수명
	 * @param varValue
	 *            변수값
	 * 
	 * @throws Exception
	 */
	protected abstract void doUpdateVarValue(String varName, Object varValue) throws Exception;

	/**
	 * 
	 * @param msIpaddr
	 * @return
	 */
	protected ServerMo getFxServer(String msIpaddr) {

		if (serverLoader.size() == 0) {
			try {
				MoApi.getApi().loadFxMo(null, serverLoader);
			} catch (Exception e) {
				Logger.logger.error(e);
				return null;
			}
		}

		if (serverLoader.size() == 0) {
			return null;
		}

		return serverLoader.getMo(msIpaddr);

	}

	/**
	 * 
	 * @param serviceName
	 * @param msIpaddr
	 * @return
	 */
	protected ServiceMo getService(String serviceName, String msIpaddr) {

		if (serviceLoader.size() == 0) {
			try {
				MoApi.getApi().loadFxMo(null, serviceLoader);
			} catch (Exception e) {
				Logger.logger.error(e);
				return null;
			}
		}

		if (serviceLoader.size() == 0) {
			return null;
		}

		for (ServiceMo service : serviceLoader.getMoList()) {

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

		try {
			StringBuffer sb = new StringBuffer();
			List<FxVar> varList = doSelectVarAll();
			for (FxVar var : varList) {
				sb.append(Logger.makeSubString(var.getName(), var.getValue()));
				varMap.put(var.getName(), var.getValue());
			}
			Logger.logger.info(Logger.makeString("variables", null, sb.toString()));

		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	protected void reload() throws Exception {
	}

}

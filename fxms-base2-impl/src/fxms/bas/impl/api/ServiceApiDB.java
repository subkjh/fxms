package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.EventApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.FxConfDao;
import fxms.bas.co.def.ALARM_CODE;
import fxms.bas.co.vo.FxServiceVo;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.co.FxServiceMoUpdateStatusAllDbo;
import fxms.bas.impl.co.FxServiceMoUpdateStatusDbo;
import fxms.bas.impl.mo.FxServerMo;
import fxms.bas.impl.mo.FxServiceMo;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;

public class ServiceApiDB extends ServiceApi {

	private List<FxServerMo> serverList = new ArrayList<FxServerMo>();

	private List<FxServiceMo> serviceList = new ArrayList<FxServiceMo>();

	protected void doAddService(String msIpaddr, String serviceName, String javaClass) throws Exception {

		try {
			FxServerMo fxServer = getServerMo(msIpaddr);
			if (fxServer == null) {
				fxServer = (FxServerMo) MoApi.getApi().makeNewMo(FxServerMo.MO_CLASS);
				fxServer.set(msIpaddr, msIpaddr, "auto-insert");
				MoApi.getApi().addMo(fxServer, "service-added", User.USER_NO_SYSTEM);
			}
		} catch (Exception e) {
			throw e;
		}

		try {

			FxServiceMo mo = getServiceMo(msIpaddr, serviceName);

			if (mo != null) {
				throw new Exception("Exist Service");
			}

			mo = (FxServiceMo) MoApi.getApi().makeNewMo(FxServiceMo.MO_CLASS);
			mo.set(msIpaddr, serviceName);
			mo.setServiceJavaClass(javaClass);
			mo.setServiceStatus("ADDED");
			mo.setStatusChgDate(FxApi.getDate(0));

			mo = (FxServiceMo) MoApi.getApi().addMo(mo, "service-added", User.USER_NO_SYSTEM);

		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	protected void doRemoveService(String msIpaddr, String serviceName) throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("msIpaddr", FxCfg.getCfg().getIpAddress());
		para.put("moClass", FxServiceMo.MO_CLASS);

		List<FxServiceMo> serviceList = MoApi.getApi().getMoList(para, FxServiceMo.class);

		if (serviceList.size() > 0) {
			for (FxServiceMo mo : serviceList) {
				if (mo.getServiceName().equals(serviceName)) {
					MoApi.getApi().deleteMo(mo, User.USER_NO_SYSTEM, "ms-delete");
					break;
				}
			}
		}

		serviceList = MoApi.getApi().getMoList(para, FxServiceMo.class);
		if (serviceList.size() == 0) {
			para.put("moClass", FxServerMo.MO_CLASS);
			List<FxServerMo> serverList = (List<FxServerMo>) MoApi.getApi().getMoList(para, FxServerMo.class);
			if (serviceList.size() > 0) {
				for (FxServerMo mo : serverList) {
					MoApi.getApi().deleteMo(mo, User.USER_NO_SYSTEM, "service-not-found");
				}
			}
		}
	}

	@Override
	protected Mo doSelectMyMo(String msIpaddr, String serviceName) throws Exception {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("msIpaddr", msIpaddr);
		para.put("serviceName", serviceName);
		para.put("mngYn", "Y");
		para.put("moClass", FxServiceMo.MO_CLASS);
		return MoApi.getApi().getMo(para);
	}

	@Override
	protected List<FxServiceVo> doServiceList() throws Exception {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("mngYn", "Y");
		para.put("moClass", FxServerMo.MO_CLASS);
		List<FxServerMo> tmpList = MoApi.getApi().getMoList(para, FxServerMo.class);
		if (tmpList != null) {
			serverList = tmpList;
		}

		para.put("moClass", FxServiceMo.MO_CLASS);
		List<FxServiceMo> list = MoApi.getApi().getMoList(para, FxServiceMo.class);

		if (list != null) {
			serviceList = list;
		}

		List<FxServiceVo> ret = new ArrayList<FxServiceVo>();
		for (FxServiceMo mo : list) {
			ret.add(new FxServiceVo(mo.getMsIpaddr(), mo.getServiceName(), mo.getServiceJavaClass()));
		}
		return ret;
	}

	@Override
	protected void doSetAllServiceStatus(String serviceStatus) throws Exception {

		FxServiceMoUpdateStatusAllDbo dbo = new FxServiceMoUpdateStatusAllDbo(FxCfg.getCfg().getIpAddress(),
				serviceStatus, FxApi.getDate(0));
		FxConfDao.getDao().update(dbo, null);
	}

	@Override
	protected void doUpdateServiceStatus(String msIpaddr, String serviceName, long startDate, String serviceStatus)
			throws Exception {

		try {
			FxServerMo fxServer = getServerMo(msIpaddr);
			if (fxServer == null) {
				fxServer = (FxServerMo) MoApi.getApi().makeNewMo(FxServerMo.MO_CLASS);
				fxServer.set(msIpaddr, msIpaddr, "auto-insert");
				MoApi.getApi().addMo(fxServer, "service-added", User.USER_NO_SYSTEM);
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		try {

			FxServiceMo fxService = getServiceMo(serviceName, msIpaddr);

			if (fxService == null) {

				FxServiceMo mo = (FxServiceMo) MoApi.getApi().makeNewMo(FxServiceMo.MO_CLASS);
				mo.set(msIpaddr, serviceName);
				mo.setServiceStatus(serviceStatus);
				mo.setStatusChgDate(FxApi.getDate(0));

				mo = (FxServiceMo) MoApi.getApi().addMo(mo, "service-added", User.USER_NO_SYSTEM);

				this.addNewService(new FxServiceVo(msIpaddr, serviceName, null));

			} else {

				if (startDate != fxService.getStartDate()
						|| serviceStatus.equals(fxService.getServiceStatus()) == false) {

					FxConfDao.getDao().update(new FxServiceMoUpdateStatusDbo(fxService.getMoNo(), startDate,
							serviceStatus, FxApi.getDate(0)), null);

					fxService.setServiceStatus(serviceStatus);
					fxService.setStartDate(startDate);

					EventApi.getApi().check(fxService, String.valueOf(FxApi.getDate(0)), ALARM_CODE.SERVICE_ON_NOTI,
							serviceStatus, null);
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@Override
	protected void initApi() throws Exception {
		super.initApi();
	}

	private FxServerMo getServerMo(String msIpaddr) {
		for (FxServerMo mo : serverList) {
			if (mo.getMsIpaddr().equals(msIpaddr)) {
				return mo;
			}
		}
		return null;
	}

	private FxServiceMo getServiceMo(String msIpaddr, String serviceName) {
		for (FxServiceMo mo : serviceList) {
			if (mo.getMsIpaddr().equals(msIpaddr) && mo.getServiceName().equals(serviceName)) {
				return mo;
			}
		}

		return null;
	}

}

package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;

import fxms.bas.api.AppApi;
import fxms.bas.api.FxApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.CoCode.FXSVC_ST_CD;
import fxms.bas.event.FxEvent;
import fxms.bas.event.NotiFilter;
import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.Mo;
import fxms.bas.signal.AliveSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.PsStatReqVo;
import subkjh.bas.co.log.Logger;

/**
 * 
 * @author subkjh
 *
 */
public class AppServiceImpl extends FxServiceImpl implements AppService {

	/**
	 *
	 */
	private static final long serialVersionUID = 2429276239016556946L;

	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(AppService.class.getSimpleName(), AppServiceImpl.class, args);
	}

	public AppServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public String checkStorage(String memo) throws RemoteException, Exception {

		String ret = null;

		try {
			ret = AppApi.getApi().checkStorage(memo);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		} finally {
			logger.fail("{} --> {}", memo, ret);
		}

	}

	@Override
	public NotiFilter getNotiFilter() throws RemoteException, Exception {
		NotiFilter notiFilter = new NotiFilter();
		notiFilter.add(Mo.class);
		notiFilter.add(Alarm.class);
		return notiFilter;
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		super.onNotify(noti);

		if (noti instanceof AliveSignal) {

			AliveSignal signal = (AliveSignal) noti;

			ServiceApi.getApi().updateServiceStatus(signal.getMsIpaddr(), signal.getServiceName(),
					signal.getStartTime(), FXSVC_ST_CD.ON, signal.getRmiPort(), signal.getServicePort());

		}

	}

	@Override
	public boolean requestMakeStat(List<PsStatReqVo> reqList) throws RemoteException, Exception {

		Boolean ret = null;
		try {
			ret = AppApi.getApi().requestMakeStat(reqList);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("req.size={} --> {}", reqList.size(), ret);
		}

	}

	@Override
	public boolean responseMakeStat(PsStatReqVo req) throws RemoteException, Exception {
		Boolean ret = null;
		try {
			ret = AppApi.getApi().responseMakeStat(req);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("{} --> {}", req, ret);
		}
	}

	@Override
	protected void onChanged(ReloadType type) throws Exception {

		super.onChanged(type);

		if (type == ReloadType.PsItem) {
			this.checkStorage("recieved reload signal");
		}

	}

	@Override
	protected void onInit(StringBuffer sb) throws Exception {
		super.onInit(sb);

		FxApi.initServiceApi(AppApi.class);
	}

	@Override
	protected void onStarted() throws Exception {

		super.onStarted();

		try {
			ServiceApi.getApi().setAllServiceStatus(FXSVC_ST_CD.INIT);

			ServiceApi.getApi().updateServiceStatus(FxCfg.getIpAddress(), FxServiceImpl.serviceName,
					FxCfg.getCfg().getLong(FX_PARA.fxmsStartTime.getKey(), 0), FXSVC_ST_CD.ON,
					FxCfg.getCfg().getRmiPort(), FxCfg.getCfg().getFxServicePort());

		} catch (Exception e) {
			logger.error(e);
		}

	}

	@Override
	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws RemoteException, Exception {
		int ret = 0;
		try {
			ret = AppApi.getApi().generateStatistics(psTbl, psKindName, psDtm);
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("{}.{}.{} --> {}", psTbl, psKindName, psDtm, ret);
		}
	}
}

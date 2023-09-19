package fxms.bas.fxo.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.event.NotiFilter;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.mo.Mo;
import fxms.bas.vo.Alarm;
import fxms.bas.vo.PsVoRaw;
import fxms.bas.vo.SyncMo;
import subkjh.bas.co.log.Logger;

/**
 * 관리대상을 관리하는 서비스
 * 
 * @author subkjh
 *
 */
public class MoServiceImpl extends FxServiceImpl implements MoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3490724989488252127L;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FxServiceImpl.start(MoService.class.getSimpleName(), MoServiceImpl.class, args);
	}

	public MoServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	protected void onInit(StringBuffer sb) throws Exception {
		super.onInit(sb);

		FxApi.initServiceApi(MoApi.class);
	}

	@Override
	public Mo addMo(int userNo, String moClass, Map<String, Object> datas, String memo, boolean broadcast)
			throws RemoteException, Exception {

		Mo mo = null;

		try {
			mo = MoApi.getApi().addMo(userNo, moClass, datas, memo, broadcast);
			return mo;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("userNo={}, datas={}, memo={}, broadcast={} --> {}", userNo, datas, memo, broadcast,
					mo == null ? "(error)" : mo.getMoName());
		}
	}

	@Override
	public Mo deleteMo(int userNo, long moNo, String memo, boolean broadcast) throws RemoteException, Exception {

		Mo mo = null;
		try {
			mo = MoApi.getApi().getMo(moNo);
			MoApi.getApi().deleteMo(userNo, moNo, memo, broadcast);
			return mo;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("userNo={}, moNo={}, memo={},  broadcast={} --> {}", userNo, moNo, memo, broadcast,
					mo == null ? "(error)" : mo.getMoName());
		}
	}

	@Override
	public Mo getMo(long moNo) throws RemoteException, Exception {
		Mo mo = null;
		try {
			mo = MoApi.getApi().getMo(moNo);
			return mo;
		} catch (MoNotFoundException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("moNo={} --> {}", moNo, mo == null ? "(error)" : mo.getMoName());
		}
	}

	@Override
	public List<Mo> getMoList(Map<String, Object> para) throws RemoteException, Exception {
		List<Mo> list = null;
		try {
			list = MoApi.getApi().getMoList(para);
			return list;
		} catch (MoNotFoundException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("para={} --> {}", para, list == null ? -1 : list.size());
		}
	}

	@Override
	public <T extends Mo> List<T> getMoList(Map<String, Object> para, Class<T> classOfMo)
			throws RemoteException, Exception {
		List<T> list = null;
		try {
			list = MoApi.getApi().getMoList(para, classOfMo);
			return list;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("para={} --> {}", para, list == null ? -1 : list.size());
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
	public List<PsVoRaw> getRtValues(long moNo) throws RemoteException, Exception {

		List<PsVoRaw> retList = null;

		try {
			retList = MoApi.getApi().getRtValues(moNo);
			return retList;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("moNo={} --> {}", moNo, retList == null ? "(error)" : retList.size());
		}

	}

	@Override
	public boolean setupMo(long moNo, String method, Map<String, Object> para) throws RemoteException, Exception {

		boolean ret = false;

		try {
			ret = MoApi.getApi().setupMo(moNo, method, para);
			return ret;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("moNo={}, method={}, para={} --> {}", moNo, method, para, ret);
		}

	}

	@Override
	public SyncMo sync(long moNo, boolean now, boolean update) throws RemoteException, Exception {

		SyncMo mo = null;
		try {
			mo = MoApi.getApi().sync(moNo, now, update);
			return mo;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			Logger.logger.info("moNo={}, now={}, update={} --> {}", moNo, now, update,
					mo == null ? "(error)" : mo.toLogString());
		}
	}

	@Override
	public Mo updateMo(int userNo, long moNo, Map<String, Object> datas, boolean broadcast)
			throws RemoteException, Exception {

		Mo mo = null;

		try {
			mo = MoApi.getApi().updateMo(userNo, moNo, datas, broadcast);
			return mo;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			logger.info("userNo={}, moNo={}, datas={}, broadcast={} --> {}", userNo, moNo, datas, broadcast,
					mo.getMoNo());

		}
	}

}

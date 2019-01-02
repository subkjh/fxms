package fxms.bas.fxo.service.mo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exception.MoNotFoundException;
import fxms.bas.poller.signal.MoPollingSecondChangeSignal;
import fxms.bas.pso.PsVo;
import fxms.bas.pso.ValueApi;
import fxms.bas.pso.VoSubNotifier;
import fxms.bas.pso.filter.ValuePeeker;
import subkjh.bas.log.Logger;

@SuppressWarnings({ "rawtypes", "unchecked" })
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

	private MoConfigThread moConfiger;

	public MoServiceImpl(String name, int port) throws RemoteException, Exception {
		super(name, port);
	}

	@Override
	public Mo getConfig(long moNo) throws RemoteException, Exception {

		Thread.currentThread().setName("getConfig-" + moNo);

		Mo mo = MoApi.getApi().getMo(moNo, true);
		if (mo == null) {
			MoNotFoundException ex = new MoNotFoundException(moNo);
			logger.fail(ex.getMessage());
			throw ex;
		}

		MoConfig confMo = new MoConfig(mo);

		getConfigChildren(confMo, "MO");

		return mo;
	}

	@Override
	public MoConfig getConfigChildren(MoConfig children, String... moClasses) throws RemoteException, Exception {

		Thread.currentThread().setName("getConfigChildren-" + children.getParent().getMoNo());

		logger.info("{} {}", children.getParent(), Arrays.toString(moClasses));

		for (Adapter adapter : getAdapter(Adapter.class, children.getParent())) {
			try {
				Logger.logger.info("getConfigChildren {}", adapter.getClass().getName());
				adapter.getConfigChildren(children, moClasses);
			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		}

		logger.info("{} {} detected count {}", children.getParent(), Arrays.toString(moClasses), children.sizeAll());

		return children;
	}

	@Override
	public List<PsVo> getValue(long moNo, String psCodes[]) throws RemoteException, Exception {

		Thread.currentThread().setName("getValue-" + moNo);

		Mo mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}

		List<PsVo> retList = new ArrayList<PsVo>();
		List<PsVo> voList;

		for (Adapter adapter : getAdapter(Adapter.class, mo)) {

			try {
				voList = adapter.getValue(mo, psCodes);
				if (voList != null && voList.size() > 0) {
					retList.addAll(voList);
				}
			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}

		}

		return retList;

	}

	@Override
	public void requestSync(Mo mo) throws RemoteException, Exception {

		logger.info(String.valueOf(mo));

		if (mo != null) {
			getMoConfiger().put(mo);
		}
	}

	@Override
	public void setPeekValue(long moNo, String moInstance, String psCode, ValuePeeker peeker, boolean add)
			throws RemoteException, Exception {

		Thread.currentThread().setName("SetPeekValue-" + moNo + "-" + psCode);

		logger.info("moNo={}, moInstance={}, psCode={}, peeker={}, add={}", moNo, moInstance, psCode, peeker, add);

		VoSubNotifier.getVoNotifier().setPeeker(moNo, moInstance, psCode, peeker, add);

		// TODO 폴링주기는 변경하는게 맞는지 검토 필요함.
		MoPollingSecondChangeSignal signal = new MoPollingSecondChangeSignal(moNo, add ? 10 : 0);
		onNotify(signal);

	}

	@Override
	public void setValue(Mo mo, String method, Map<String, Object> parameters) throws RemoteException, Exception {

		Thread.currentThread().setName("setValue-" + mo.getMoNo());

		logger.info(Logger.makeString(mo + ", " + method + "\nparameters=" + parameters));

		for (Adapter adapter : getAdapter(Adapter.class, mo)) {
			try {
				adapter.setValue(mo, method, parameters);
				logger.info("{} SET OK : method={}, parameters={}", mo, method, parameters);
				return;
			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			}
		}

		throw new Exception("Not implement Adapter");
	}

	private <T> List<T> getAdapter(Class<T> classOfAdapter, Mo mo) throws Exception {
		return new MoConfiger().getAdapter(classOfAdapter, mo);
	}

	private synchronized MoConfigThread getMoConfiger() {
		if (moConfiger == null) {
			moConfiger = new MoConfigThread();
			moConfiger.start();
		}
		return moConfiger;
	}

	private long updateServicePsTime = System.currentTimeMillis() - 480000L;

	@Override
	public void onCycle(long mstime) {

		if (System.currentTimeMillis() - 600000L > updateServicePsTime) {
			try {
				ValueApi.getApi().doUpdateServicePsCode();
			} catch (Exception e) {
				logger.error(e);
			}

			updateServicePsTime = System.currentTimeMillis();
		}

		super.onCycle(mstime);
	}

}

package fxms.bas.alarm.treat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.alarm.dbo.OccurAlarmDbo;
import fxms.bas.dao.FxConfDao;
import fxms.bas.dbo.am.AmGroupVo;
import fxms.bas.dbo.am.AmHstVo;
import fxms.bas.dbo.am.AmMoVo;
import fxms.bas.dbo.am.AmUserVo;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.thread.QueueFxThread;
import fxms.bas.noti.FxEvent;
import fxms.bas.signal.ReloadSignal;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;

public class TreatFxThread extends QueueFxThread<OccurAlarmDbo> {

	private static TreatFxThread fxThread;

	public static TreatFxThread getTreatMgr() {
		if (fxThread == null) {
			fxThread = new TreatFxThread();
			fxThread.start();
		}

		return fxThread;
	}

	private Map<Long, List<AmGroupVo>> moAmMap = new HashMap<Long, List<AmGroupVo>>();

	public TreatFxThread() {
		super(TreatFxThread.class.getSimpleName(), 0);
	}

	@Override
	public void onNotify(FxEvent noti) throws Exception {

		super.onNotify(noti);

		if (noti instanceof ReloadSignal) {
			loadAmGroup();
		}
	}

	private void loadAmGroup() {
		try {

			FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
			try {

				tran.start();

				Map<Long, List<AmGroupVo>> moAmMap = new HashMap<Long, List<AmGroupVo>>();

				Map<String, Object> para = new HashMap<String, Object>();
				para.put("enableYn", "Y");
				List<AmGroupVo> groupList = tran.select(AmGroupVo.class, para);
				if (groupList.size() > 0) {
					
					List<AmUserVo> userList = tran.select(AmUserVo.class, para);
					List<AmMoVo> moList = tran.select(AmMoVo.class, para);
					
					
					for (AmUserVo user : userList) {
						for (AmGroupVo group : groupList) {
							if (user.getAmGroupNo() == group.getAmGroupNo()) {
								group.getAmList().add(user);
								break;
							}
						}
					}

					List<AmGroupVo> entry;

					for (AmMoVo mo : moList) {
						entry = moAmMap.get(mo.getMoNo());
						if (entry == null) {
							entry = new ArrayList<AmGroupVo>();
							moAmMap.put(mo.getMoNo(), entry);
						}

						for (AmGroupVo group : groupList) {
							if (mo.getAmGroupNo() == group.getAmGroupNo()) {
								entry.add(group);
								break;
							}
						}
					}
				}

				this.moAmMap = moAmMap;

			} catch (Exception e) {
				Logger.logger.error(e);
				throw e;
			} finally {
				tran.stop();
			}
		} catch (Exception e) {
			Logger.logger.error(e);
		}
	}

	@Override
	protected void doInit() {
		loadAmGroup();
	}

	@Override
	protected void doWork(OccurAlarmDbo e) throws Exception {

		if (e.getTreatName() == null || e.getTreatName().length() == 0) {
			return;
		}

		List<AmGroupVo> groupList = moAmMap.get(e.getMoNo());
		if (groupList == null) {
			groupList = moAmMap.get(e.getUpperMoNo());
		}

		List<TreatFilter> filterList = FxActorParser.getParser().getActorList(TreatFilter.class);
		List<AmHstVo> hstList = new ArrayList<AmHstVo>();
		List<AmHstVo> entry;

		for (TreatFilter filter : filterList) {
			if (filter.getName().equals(e.getTreatName())) {
				entry = filter.treat(e, groupList);
				if (entry != null) {
					hstList.addAll(entry);
				}
			}
		}

		if (hstList.size() > 0) {
			FxConfDao.getDao().insertList(AmHstVo.class, hstList);
		}

	}

	@Override
	protected void onNoDatas(long index) {

	}

}

package fxms.bas.cron;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.co.CoCode.MO_WORK_TYPE_CD;
import fxms.bas.fxo.FX_PARA;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.fxo.service.MoService;
import fxms.bas.fxo.thread.FxThreadPool;
import fxms.bas.fxo.thread.SubFxThread;
import fxms.bas.impl.dbo.all.FX_MX_WORK_HST;
import fxms.bas.mo.Mo;
import fxms.bas.vo.SyncMo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 관리대상에 대한 동기화 요청 내용을 읽어 실제 동기화 요청하고 결과를 기록한다.
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "MoService", descr = "관리대상 동기화 요청")
public class MoSyncCron extends Crontab {

	@FxAttr(name = "schedule", description = "실행계획", value = "0 4 * * *")
	private String schedule;

	public MoSyncCron() {

	}

	/**
	 * 동기화 요청 목록을 읽는다.
	 * 
	 * @return
	 */
	private List<FX_MX_WORK_HST> selectList() {

		ClassDao tran;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		} catch (Exception e1) {
			return null;
		}

		try {
			tran.start();

			Map<String, Object> para = new HashMap<>();
			para.put("rstNo", 0);
			para.put("moWorkTypeCd", MO_WORK_TYPE_CD.Sync.getCode());

			return tran.select(FX_MX_WORK_HST.class, para, FX_MX_WORK_HST.class);

		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 동기화된 결과를 기록한다.
	 * 
	 * @param list
	 */
	private void update(List<FX_MX_WORK_HST> list) {

		ClassDao tran;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		} catch (Exception e1) {
			return;
		}

		try {
			tran.start();

			for (FX_MX_WORK_HST obj : list) {
				FxTableMaker.initRegChg(0, obj);
				tran.updateOfClass(FX_MX_WORK_HST.class, obj);
			}

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			return;
		} finally {
			tran.stop();
		}

	}

	@Override
	public void start() throws Exception {

		// 목록을 가져온다.
		List<FX_MX_WORK_HST> workList = selectList();

		FxThreadPool<FX_MX_WORK_HST> pool = new FxThreadPool<FX_MX_WORK_HST>("CONF-SYNC",
				FxCfg.get(FX_PARA.threadSize, 10)) {

			@Override
			protected List<FX_MX_WORK_HST> getObjectList() {
				// 스레드가 처리할 내용으로 가져온 동기화 요청 내역을 넘긴다.
				return workList;
			}

			@Override
			protected SubFxThread<FX_MX_WORK_HST> makeSub(final String name) {
				return new SubFxThread<FX_MX_WORK_HST>(name) {
					@Override
					protected void doSub(FX_MX_WORK_HST node) throws Exception {

						node.setStrtDtm(DateUtil.getDtm());

						try {

							// 관리대상 조회
							Mo mo = null;
							try {
								mo = MoApi.getApi().getMo(node.getMoNo());
							} catch (Exception e) {
							}

							if (mo != null) {

								node.setMoName(mo.getMoName());

								// 동기화 처리
								MoService service = ServiceApi.getApi().getService(MoService.class, mo);
								if (service != null) {
									SyncMo syncMo = service.sync(node.getMoNo(), true, true);
									node.setRstNo(1);
									node.setEndDtm(DateUtil.getDtm());
									return;
								} else {
									node.setRstCont("MoService Not Found");
								}

							} else {
								node.setRstCont("Mo Not Found");
							}

						} catch (Exception e) {
							if (e.getMessage() != null && e.getMessage().length() < 100) {
								node.setRstCont(e.getClass().getName() + " : " + e.getMessage());
							} else {
								node.setRstCont(e.getClass().getName());
							}
						}

						node.setEndDtm(DateUtil.getDtm());
						node.setRstNo(-1);
					}

				};
			}

			@Override
			public void stop(String msg) {

				// 동기화 결과 기록
				update(workList);

				super.stop(msg);
			}

		};

		pool.start();

	}

	@Override
	protected String getSchedule() {
		return schedule;
	}
}

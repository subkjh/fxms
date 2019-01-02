package fxms.bas.pso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.ServiceApi;
import fxms.bas.api.VoApi;
import fxms.bas.define.PS_TYPE;
import fxms.bas.fxo.service.app.AppService;
import fxms.bas.fxo.service.app.mgr.RemakeStatReq;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;

/**
 * 수집된 성능을 기록하는 스레드
 * 
 * @author subkjh
 * 
 */
public class VoSubInsert extends VoSub {

	class Data {
		RemakeStatReq req;
		long mstime;
	}

	private Map<String, Data> statReqMap = new HashMap<String, Data>();

	/**
	 * 
	 * @param name
	 *            스레드명
	 */
	public VoSubInsert() {
		super(VoSubInsert.class.getSimpleName());
	}

	@Override
	public String getState(LOG_LEVEL level) {

		StringBuffer sb = new StringBuffer();
		sb.append("REQ-REMAKE-STAT(" + statReqMap.size() + ")");
		sb.append(super.getState(level));

		return super.toString() + sb.toString();
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(VoList voList) throws Exception {

		Map<String, Integer> retMap = ValueApi.getApi().doInsertValue(voList);

		try {
			if (retMap != null) {

				List<PS_TYPE> pstypeList = VoApi.getApi().getPstypeList();
				long psDate;

				Data data;
				RemakeStatReq req;

				for (String psTable : retMap.keySet()) {
					if (retMap.get(psTable) > 0) {
						for (PS_TYPE pstype : pstypeList) {

							psDate = pstype.getHstimeStart(PS_TYPE.getHstimeByMstime(voList.getMstime()));
							req = new RemakeStatReq(psTable, pstype.name(), psDate);
							data = statReqMap.get(req.getKey());
							if (data == null) {
								data = new Data();
								data.req = req;
								statReqMap.put(req.getKey(), data);
							}
							data.mstime = System.currentTimeMillis();

						}
					}
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
		}

		sendReq();
	}

	// @Override
	// protected BackupSender<VoList> makeBackupSender(String name) {
	//
	// if (ValueApi.getApi().isEnableBackupSender() == false)
	// return null;
	//
	// return new BackupSender<VoList>(name, new
	// File(ValueApi.getApi().getFolderBackup()), VoList.class) {
	// @Override
	// protected boolean send(VoList valueList) {
	// try {
	// ValueApi.getApi().doInsertValue(valueList.getMstime(),
	// valueList.getValueList());
	// return true;
	// } catch (Exception e) {
	// Logger.logger.error(e);
	// }
	// return false;
	// }
	// };
	//
	// }

	@Override
	protected void onNoDatas(long index) {
		sendReq();
	}

	private void sendReq() {
		Data data;
		AppService appService = null;

		String keys[] = statReqMap.keySet().toArray(new String[statReqMap.size()]);

		for (String key : keys) {
			data = statReqMap.get(key);

			// 10 seconds
			if (System.currentTimeMillis() - data.mstime > 10000) {

				try {
					if (appService == null) {
						appService = ServiceApi.getApi().getService(AppService.class);
					}
					appService.reqMakeStat(data.req.getPsTable(), data.req.getPsType(), data.req.getPsDate());

					Logger.logger.trace("send req {}", key);

					statReqMap.remove(key);
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

	}

}

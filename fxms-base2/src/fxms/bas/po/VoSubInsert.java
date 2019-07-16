package fxms.bas.po;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.VoApi;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.po.noti.NotiReqMakeStat;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 수집된 성능을 기록하는 스레드
 * 
 * @author subkjh
 * 
 */
public class VoSubInsert extends VoSub {

	private Map<String, NotiReqMakeStat> statReqMap = new HashMap<String, NotiReqMakeStat>();

	/**
	 * 
	 * @param name 스레드명
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

				NotiReqMakeStat newData;
				NotiReqMakeStat data;

				for (String psTable : retMap.keySet()) {
					if (retMap.get(psTable) > 0) {
						for (PS_TYPE pstype : pstypeList) {

							psDate = pstype.getHstimeStart(PS_TYPE.getHstimeByMstime(voList.getMstime()));
							newData = new NotiReqMakeStat(psTable, pstype.name(), psDate);
							data = statReqMap.get(newData.getKey());
							if (data == null) {
								newData.mstime = System.currentTimeMillis();
								statReqMap.put(newData.getKey(), data);
							} else {
								data.mstime = System.currentTimeMillis();
							}
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
		NotiReqMakeStat data;

		String keys[] = statReqMap.keySet().toArray(new String[statReqMap.size()]);

		for (String key : keys) {
			data = statReqMap.get(key);

			// 10 seconds
			if (System.currentTimeMillis() - data.mstime > 10000) {

				try {

					FxServiceImpl.fxService.send(data);

					Logger.logger.trace("send req {}", key);

					statReqMap.remove(key);
					
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

	}

}

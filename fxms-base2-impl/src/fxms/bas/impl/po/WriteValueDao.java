package fxms.bas.impl.po;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.database.DBManager;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.fxo.FxCfg;
import fxms.bas.po.PsVo;

/**
 * 성능을 기록하는 DAO
 * 
 * @author subkjh
 * 
 */
public class WriteValueDao {

	public WriteValueDao() {
	}

	/**
	 * 
	 * @param psDate
	 * @param valueList
	 * @param isRaw
	 * @return
	 * @throws Exception
	 */
	public Map<String, Integer> write(long psDate, PsVo valArr[], boolean isRaw)
			throws Exception {

		List<PsValTable> tableList;
		int count = 0;
		ValueBatchSaver saver;
		Map<String, Integer> map = new HashMap<String, Integer>();

		try {
			VoPsTableMaker saveBean = new VoPsTableMaker();
			tableList = saveBean.makePsTableList(psDate, valArr, PS_TYPE.RAW);

			for (PsValTable psTable : tableList) {

				saver = new ValueBatchSaver(DBManager.getMgr().getDataBase(FxCfg.DB_PSVALUE)//
						, psTable);
				
				count = saver.executeBatch();

				Logger.logger.info("TABLE({}) DATAS({}) ADDED", psTable.getRealPsTable(), count);

				map.put(psTable.getPsTable(), count);

			}

			return map;
		} catch (Exception e1) {
			Logger.logger.error(e1);
			throw e1;
		}

	}

}

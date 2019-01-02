package fxms.bas.api.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.PsApi;
import fxms.bas.api.VoApiDB;
import fxms.bas.define.PS_TYPE;
import fxms.bas.pso.item.PsItem;
import subkjh.bas.log.Logger;

/**
 * 성능 기록 객체 생성
 * 
 * @author subkjh
 * @since 2013.05.20
 */
public abstract class PsTableMaker<VALUE> {

	public PsTableMaker() {

	}

	public abstract List<PsValTable> makePsTableList(long psDate, VALUE valArr[], PS_TYPE pstype) throws Exception;

	/**
	 * 
	 * @param psCodeArr
	 *            성능항목배열
	 * @return 테이블 목록
	 * @throws Exception
	 */
	protected List<PsValTable> makePerfData(String psCodeArr[], PS_TYPE pstype, long psDate) throws Exception {

		List<PsValTable> psTableList = new ArrayList<PsValTable>();
		PsValTable psTable;
		PsItem item;
		String tableName;
		PsApi api = PsApi.getApi();

		for (String psCode : psCodeArr) {

			item = api.getItem(psCode);

			if (item == null) {
				Logger.logger.fail("PS-CODE({}) NOT FOUND", psCode);
				continue;
			}

			tableName = item.getPsTable() + pstype.getTableTag(psDate);

			psTable = null;
			for (PsValTable pt : psTableList) {
				if (pt.getRealPsTable().equals(tableName)) {
					psTable = pt;
					break;
				}
			}

			if (psTable == null) {
				psTable = new PsValTable(tableName, item.getPsTable());
				psTableList.add(psTable);

				psTable.addColumn(null, VoApiDB.MO_NO.getName(), ValueSaver.TYPE.Long);
				psTable.addColumn(null, VoApiDB.MO_INSTANCE.getName(), ValueSaver.TYPE.String);
				psTable.addColumn(null, VoApiDB.PS_DATE.getName(), ValueSaver.TYPE.Long);

			}

			if (item.getDataScale() > 0) {
				psTable.addColumn(item, item.getPsColumn(), ValueSaver.TYPE.Double);
			} else {
				psTable.addColumn(item, item.getPsColumn(), ValueSaver.TYPE.Long);
			}

		}

		return psTableList;
	}

}

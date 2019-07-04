package fxms.bas.impl.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import fxms.bas.co.def.PS_TYPE;
import fxms.bas.po.PsVo;
import fxms.bas.po.item.PsItem;

/**
 * 
 * @author subkjh
 * 
 */
public class VoPsTableMaker extends PsTableMaker<PsVo> {

	public VoPsTableMaker() {
	}

	@Override
	public List<PsValTable> makePsTableList(long psDate, PsVo valArr[], PS_TYPE pstype) throws Exception {

		PsValTable psTable;
		Object valueArray[];
		int column;
		PsItem item = null;
		int countIgnore = 0;
		List<String> igList = new ArrayList<String>();
		String psCodes[];
		List<PsValTable> psTableList;

		psCodes = getPsCodeList(valArr);
		psTableList = makePerfData(psCodes, pstype, psDate);
		
		for (PsVo val : valArr) {

			if (val == null || val.getValue() == null)
				continue;

			// 1. 성능의 테이블 및 컬럼 번호를 구합니다.
			column = -1;
			psTable = null;

			for (PsValTable pt : psTableList) {
				column = pt.getIndexForPerfNo(val.getPsCode());
				if (column >= 0) {
					psTable = pt;
					item = pt.getItem(val.getPsCode());
					break;
				} else {
					if (igList.contains(val.getPsCode()) == false) {
						igList.add(val.getPsCode());
					}
					countIgnore++;
				}
			}

			if (column < 0)
				continue;

			// 값을 설정합니다.

			valueArray = psTable.getValue(val.getMoNo(), val.getMoInstance());
			if (valueArray == null) {

				valueArray = new Object[psTable.getDbColumns().size()];
				for (int i = 0; i < valueArray.length; i++) {
					valueArray[i] = null;
				}

				valueArray[0] = val.getMoNo();
				valueArray[1] = val.getMoInstance();
				valueArray[2] = psDate;

				psTable.putValue(val.getMoNo(), val.getMoInstance(), valueArray);
			}

			// valueArray[column] = val.getValue();
			valueArray[column] = item.convert(val.getValue());

		}

		if (countIgnore > 0) {
			Logger.logger.debug("VALUE-IGNORE-COUNT(" + countIgnore + ")PERF-NO-LIST(" + igList + ")");
		}

		return psTableList;
	}

	/**
	 * 
	 * @param list
	 * @return 중복되지 않은 성능번호 목록
	 */
	private String[] getPsCodeList(PsVo valArr[]) {
		Map<String, String> psCodeMap = new HashMap<String, String>();
		for (PsVo e : valArr) {
			psCodeMap.put(e.getPsCode(), e.getPsCode());
		}
		return psCodeMap.values().toArray(new String[psCodeMap.size()]);
	}

}

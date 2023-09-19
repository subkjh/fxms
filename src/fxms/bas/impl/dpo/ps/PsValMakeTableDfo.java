package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.api.ValueSaver;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.vo.PsValTable;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;

/**
 * 함수를 이용하여 통계 생성한다.
 * 
 * @author subkjh
 *
 */
public class PsValMakeTableDfo extends PsDpo implements FxDfo<PsVoList, List<PsValTable>> {

	public static void main(String[] args) {

		MoApi.api = new MoApiDfo();

		PsValMakeTableDfo dfo = new PsValMakeTableDfo();
		PsVoList datas = new PsVoList("test", System.currentTimeMillis(), null);

		try {
			datas.add(2, MoApi.getApi().getMo(1000), PsApi.getApi().getPsItem("ePowerFactor"));
			datas.add(2, MoApi.getApi().getMo(1000), PsApi.getApi().getPsItem("ePower"));
			System.out.println(FxmsUtil.toJson(dfo.getTables(datas)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PsValTable> call(FxFact fact, PsVoList datas) throws Exception {
		return getTables(datas);
	}

	public List<PsValTable> getTables(PsVoList datas) throws Exception {

		final Map<String, PsValTable> map = makeTableInfo(datas);
		final PsKind psKind = PsApi.getApi().getPsKindRaw();
		
		PsValTable psTable;
		Object valueArray[];
		int column;
		PsItem item;
		String tableName;

		for (PsVo val : datas) {

			item = val.getPsItem();

			// 1. 성능의 테이블 및 컬럼 번호를 구합니다.
			tableName = psKind.getTableName(item, datas.getHstime());
			psTable = map.get(tableName);
			if (psTable == null) {
				continue;
			}

			column = psTable.getIndexPsId(item.getPsId());
			if (column < 0)
				continue;

			// 테이블에 컬럼이 없으면 넣어준다.

			// 값을 설정합니다.

			valueArray = psTable.getValue(val.getMo().getMoNo());
			if (valueArray == null) {

				valueArray = new Object[psTable.getColSize()];
				for (int i = 0; i < valueArray.length; i++) {
					valueArray[i] = null;
				}

				valueArray[0] = val.getMo().getMoNo();
				valueArray[1] = datas.getHstime();

				psTable.putValue(val.getMo().getMoNo(),  valueArray);
			}

			valueArray[column] = val.getValue();

		}

		return new ArrayList<PsValTable>(map.values());
	}

	/**
	 * 수집데이터의 테이블 정보를 만든다.
	 * 
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	private Map<String, PsValTable> makeTableInfo(PsVoList datas) throws Exception {

		final Map<String, PsValTable> map = new HashMap<>();
		final PsKind psKind = PsApi.getApi().getPsKindRaw();
		PsValTable psTable;
		int column;
		PsItem item;
		String tableName;

		for (PsVo val : datas) {

			item = val.getPsItem();

			// 1. 성능의 테이블 및 컬럼 번호를 구합니다.
			tableName = psKind.getTableName(item, datas.getHstime());
			psTable = map.get(tableName);
			if (psTable == null) {
				psTable = new PsValTable(tableName, item.getPsTable());
				psTable.addColumn(null, PsDpo.MO_NO.getName(), ValueSaver.TYPE.Long);
//				psTable.addColumn(null, PsDpo.MO_INSTANCE.getName(), ValueSaver.TYPE.String);
				psTable.addColumn(null, PsDpo.PS_DATE.getName(), ValueSaver.TYPE.Long);
				map.put(tableName, psTable);
			}

			// 테이블에 컬럼이 없으면 넣어준다.
			column = psTable.getIndexPsId(item.getPsId());
			if (column < 0) {
				psTable.addColumn(item, item.getPsColumn(),
						item.getDataScale() > 0 ? ValueSaver.TYPE.Double : ValueSaver.TYPE.Long);
			}

		}

		return map;
	}
}

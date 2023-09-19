package fxms.bas.impl.dpo.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.PsApi;
import fxms.bas.exp.PsItemNotFoundException;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.def.Column;
import subkjh.dao.def.Index;
import subkjh.dao.def.Index.INDEX_TYPE;
import subkjh.dao.def.Table;

/**
 * 성능항목을 이용하여 테이블 정보를 가져온다.
 * 
 * @author subkjh
 *
 */
public class PsTableGetInfoDfo extends PsDpo implements FxDfo<PsKind, List<Table>> {

	public static void main(String[] args) throws Exception {
		PsTableGetInfoDfo dfo = new PsTableGetInfoDfo();
		List<Table> tables = dfo.call(null, new PsKind("5M", "5M", 0, "5 minutes"));
		for (Table tab : tables) {
			System.out.println(tab.getDebug());
		}
	}

	@Override
	public List<Table> call(FxFact fact, PsKind psKind) throws Exception {
		return getTableInfo(psKind, DateUtil.getDtm());
	}

	public List<Table> getTableInfo(PsKind psKind, long hstime) throws Exception {

		PsItem items[] = PsApi.getApi().getPsItems();
		if (items == null) {
			throw new PsItemNotFoundException("*");
		}

		Map<String, Table> tableMap = new HashMap<String, Table>();
		Table table;
		String tableName;

		for (PsItem item : items) {

			if (item.hasTable() == false) {
				continue;
			}

			tableName = psKind.getTableName(item, hstime);
			table = tableMap.get(tableName);

			if (table == null) {
				table = this.makeTableInfo(tableName, item, psKind);
				tableMap.put(table.getName(), table);
			}

			// 동일 테이블의 여러개의 컬럼을 추가할 수 있다.
			List<Column> colList = item.makeColumns(psKind);
			for (Column col : colList) {
				table.addColumn(col);
			}

		}

		return new ArrayList<>(tableMap.values());

	}

	public Table makeTableInfo(String tableName, PsItem psItem, PsKind psKind) {
		Index index;

		Table table = new Table();
		table.setName(tableName);
		table.addColumn(MO_NO);
		table.addColumn(PS_DATE);

		if (psKind.isRaw() == false) {
			table.addColumn(DATA_COUNT);
			table.addColumn(INS_DATE);
		}

		index = new Index(table.getName() + "__PK", INDEX_TYPE.PK);
		index.addColumn(MO_NO.getName());
		index.addColumn(PS_DATE.getName());
		table.addIndex(index);

		index = new Index(table.getName() + "__KEY1", INDEX_TYPE.KEY);
		index.addColumn(PS_DATE.getName());
		table.addIndex(index);

		return table;
	}
}

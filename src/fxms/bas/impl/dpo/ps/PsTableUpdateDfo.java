package fxms.bas.impl.dpo.ps;

import java.util.List;

import fxms.bas.api.PsApi;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.database.MySql;
import subkjh.dao.def.Table;
import subkjh.dao.util.MakeTable;

/**
 * 수집 데이터를 기록한 테이블을 생성한다.
 * 
 * @author subkjh
 *
 */
public class PsTableUpdateDfo extends PsDpo implements FxDfo<Void, Void> {

	public static void main(String[] args) {
		PsTableUpdateDfo dfo = new PsTableUpdateDfo();
		try {
			dfo.showUpdateColumn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Void call(FxFact fact, Void data) throws Exception {
		showUpdateColumn();
		return null;
	}

	/**
	 * 
	 * @param psItem
	 * @return
	 * @throws Exception
	 */
	public void showUpdateColumn() throws Exception {

		MakeTable maker = new MakeTable(new MySql());
		for (PsKind psKind : PsApi.getApi().getPsKindList()) {
			List<Table> tables = new PsTableGetInfoDfo().getTableInfo(psKind, DateUtil.getDtm());
			System.out.println(maker.getModifyColumnQuery(tables));
		}

	}
}

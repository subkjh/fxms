package subkjh.bas.fxdao.control;

import java.util.List;
import java.util.Map;

import subkjh.bas.dao.control.DbTrans;

public abstract class DiffSqlDao extends DiffDao<Map<String, Object>> {

	public DiffSqlDao(DbTrans srcTran, DbTrans dstTran) {
		super(srcTran, dstTran);
	}

	protected List<Map<String, Object>> select(DbTrans tran, String qid) throws Exception {
		return tran.selectSql2Map(qid, null);
	}

}

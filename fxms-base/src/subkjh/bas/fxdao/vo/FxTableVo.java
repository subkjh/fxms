package subkjh.bas.fxdao.vo;

import java.util.ArrayList;
import java.util.List;

public class FxTableVo extends FX_TAB {

	private List<FX_TAB_IDX> idxList;
	private List<FX_TAB_COL> colList;

	public List<FX_TAB_IDX> getIndexes() {
		if (idxList == null) {
			idxList = new ArrayList<FX_TAB_IDX>();
		}

		return idxList;
	}

	public FX_TAB_COL getColumn(String colName) {
		for (FX_TAB_COL col : getColumns()) {
			if (col.getColName().equals(colName)) {
				return col;
			}
		}

		return null;
	}

	public List<FX_TAB_COL> getColumns() {
		if (colList == null) {
			colList = new ArrayList<FX_TAB_COL>();
		}

		return colList;
	}

	public List<FX_TAB_COL> getSequenceColumns() {
		List<FX_TAB_COL> ret = new ArrayList<FX_TAB_COL>();
		for (FX_TAB_COL col : getColumns()) {
			if (col.getSeqName() != null && col.getSeqName().length() > 0) {
				ret.add(col);
			}
		}
		return ret;
	}
}

package fxms.bas.impl.co.vo;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.dao.data.Column;
import subkjh.bas.dao.data.Index;
import subkjh.bas.dao.data.Table;
import subkjh.bas.dao.define.COLUMN_OP;
import fxms.bas.impl.dbo.FX_TAB_COL;
import fxms.bas.impl.dbo.FX_TAB_IDX;

public class FxTableVo {

	private List<FX_TAB_IDX> idxList;
	private List<FX_TAB_COL> colList;

	private String tabName;

	private String tabComment;

	public FxTableVo(String tabName, String tabComment) {
		this.tabName = tabName;
		this.tabComment = tabComment;
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

	public List<FX_TAB_IDX> getIndexes() {
		if (idxList == null) {
			idxList = new ArrayList<FX_TAB_IDX>();
		}

		return idxList;
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

	public String getTabComment() {
		return tabComment;
	}

	public Table getTable() throws Exception {

		String idxColNames[];
		Column column;
		Table table = new Table();

		// set table
		table.setName(getTabName());
		table.setComment(getTabComment());

		// set indexes
		for (FX_TAB_IDX idx : getIndexes()) {
			table.addIndex(makeIndex(idx));
		}

		// set columns
		for (FX_TAB_COL col : getColumns()) {
			table.addColumn(getColumn(col));
		}

		// rename column of index
		for (Index idx : table.getIndexList()) {
			idxColNames = idx.getColArr();
			idx.setColumnNameList(null);

			for (String colName : idxColNames) {
				column = table.getColumn(colName);
				if (idx.isPk()) {
					column.setPk(true);
				}
				idx.addColumn(column.getName());
			}

			if (isEmpty(idx.getFkColumn()) == false) {
				column = table.getColumn(idx.getFkColumn());
				idx.setFkColumn(column.getName());
			}
		}

		return table;

	}

	public String getTabName() {
		return tabName;
	}

	private Column getColumn(FX_TAB_COL col) throws Exception {

		Column column = new Column();
		column.setName(col.getColName());
		column.setComments(col.getColComment());
		column.setOperator(col.isUpdatebleYn() ? COLUMN_OP.all : COLUMN_OP.insert);
		column.setSequence(col.getSeqName());
		column.setDataLength(col.getColSize());
		column.setNullable(col.isNullableYn());
		column.setDataType(col.getColType());

		if (column.getDataScale() > 0) {
			column.setDataTypeDefined(column.getDataType() + "(" + column.getDataLength() + "," + column.getDataScale() + ")");
		} else {
			column.setDataTypeDefined(column.getDataType() + "(" + column.getDataLength() + ")");
		}

		return column;
	}

	private boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	private Index makeIndex(FX_TAB_IDX fxIdx) throws Exception {

		Index idx = new Index();
		idx.setName(fxIdx.getIdxName());
		idx.setColumns(fxIdx.getColNameList());
		idx.setFkColumn(fxIdx.getFkColNameList());
		idx.setFkTable(fxIdx.getFkTabName());
		idx.setType(fxIdx.getIdxType());

		return idx;
	}
}

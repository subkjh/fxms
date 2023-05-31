package fxms.bas.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.impl.dbo.all.FX_TBL_COL_DEF;
import fxms.bas.impl.dbo.all.FX_TBL_IDX_DEF;
import subkjh.dao.def.Column;
import subkjh.dao.def.Column.COLUMN_OP;
import subkjh.dao.def.Index;
import subkjh.dao.def.Index.INDEX_TYPE;
import subkjh.dao.def.Table;

public class FxTableVo {

	private List<FX_TBL_IDX_DEF> idxList;
	private List<FX_TBL_COL_DEF> colList;

	private String tabName;

	private String tabComment;

	public FxTableVo(String tabName, String tabComment) {
		this.tabName = tabName;
		this.tabComment = tabComment;
	}

	public FX_TBL_COL_DEF getColumn(String colName) {
		for (FX_TBL_COL_DEF col : getColumns()) {
			if (col.getColName().equals(colName)) {
				return col;
			}
		}

		return null;
	}

	public List<FX_TBL_COL_DEF> getColumns() {
		if (colList == null) {
			colList = new ArrayList<FX_TBL_COL_DEF>();
		}

		return colList;
	}

	public List<FX_TBL_IDX_DEF> getIndexes() {
		if (idxList == null) {
			idxList = new ArrayList<FX_TBL_IDX_DEF>();
		}

		return idxList;
	}

	public List<FX_TBL_COL_DEF> getSequenceColumns() {
		List<FX_TBL_COL_DEF> ret = new ArrayList<FX_TBL_COL_DEF>();
		for (FX_TBL_COL_DEF col : getColumns()) {
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

//		String idxColNames[];
		Column column;
		Table table = new Table();

		// set table
		table.setName(getTabName());
		table.setComment(getTabComment());

		// set indexes
		for (FX_TBL_IDX_DEF idx : getIndexes()) {
			table.addIndex(makeIndex(idx));
		}

		// set columns
		for (FX_TBL_COL_DEF col : getColumns()) {
			table.addColumn(getColumn(col));
		}

		// rename column of index
		for (Index idx : table.getIndexList()) {

			String oldCols[] = idx.getColumnNames().toArray(new String[idx.getColumnNames().size()]);
			idx.clearColumns();

			for (String colName : oldCols) {
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

	private Column getColumn(FX_TBL_COL_DEF col) throws Exception {

		Column column = new Column();
		column.setName(col.getColName());
		column.setComments(col.getColCmnt());
		column.setOperator("y".equalsIgnoreCase(col.isUpdblYn()) ? COLUMN_OP.all : COLUMN_OP.insert);
		column.setSequence(col.getSeqName());
		column.setDataLength(col.getColSize());
		column.setNullable("y".equalsIgnoreCase(col.isNulblYn()));
		column.setDataType(col.getColTypeCd());

		if (column.getDataScale() > 0) {
			column.setDataTypeDefined(
					column.getDataType() + "(" + column.getDataLength() + "," + column.getDataScale() + ")");
		} else {
			column.setDataTypeDefined(column.getDataType() + "(" + column.getDataLength() + ")");
		}

		return column;
	}

	private boolean isEmpty(String s) {
		return s == null || s.isEmpty();
	}

	private Index makeIndex(FX_TBL_IDX_DEF fxIdx) throws Exception {

		Index idx = new Index(fxIdx.getIdxName(), INDEX_TYPE.getType(fxIdx.getIdxTypeCd()));
		idx.setColumns(fxIdx.getColNameList());
		idx.setFkColumn(fxIdx.getFkColNameList());
		idx.setFkTable(fxIdx.getFkTblName());

		return idx;
	}
}

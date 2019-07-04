package fxms.bas.po.vo;

import java.io.Serializable;

import fxms.bas.api.FxApi;

public class UpdateDataVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8472963331167092968L;

	private String dbColHstimeName;
	private String dbColName;
	private String dbTblName;
	private long moNo;
	private Object value;
	private String updateFilter;

	public UpdateDataVo() {
	}

	public UpdateDataVo(long moNo, String updateFilter, Object value, String dbTblName, String dbColName,
			String dbColHstimeName) {
		this.moNo = moNo;
		this.updateFilter = updateFilter;
		this.value = value;
		this.dbTblName = dbTblName;
		this.dbColName = dbColName;
		this.dbColHstimeName = dbColHstimeName;
	}

	public String getDbColName() {
		return dbColName;
	}

	public long getMoNo() {
		return moNo;
	}

	public String getSqlUpdate() {
		if (dbTblName == null || dbTblName.trim().length() == 0 || dbColName == null || dbColName.trim().length() == 0)
			return null;

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE " + dbTblName + " SET ");
		sb.append(dbColName + " = ");
		if (value instanceof Number) {
			sb.append(value.toString());
		} else if (value instanceof Boolean) {
			sb.append("'" + (((Boolean) value) ? "Y" : "N") + "'");
		} else {
			sb.append("'" + value.toString().replaceAll("'", "") + "'");
		}

		if (dbColHstimeName != null && dbColHstimeName.trim().length() > 0) {
			sb.append(" , " + dbColHstimeName + " = " + FxApi.getDate(0));
		}

		sb.append(" WHERE MO_NO = " + moNo);

		return sb.toString();

	}

	public String getUpdateFilter() {
		return updateFilter;
	}

	public Object getValue() {
		return value;
	}

}

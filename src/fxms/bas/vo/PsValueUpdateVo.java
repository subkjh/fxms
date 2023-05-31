package fxms.bas.vo;

import java.io.Serializable;

import subkjh.bas.co.utils.DateUtil;

public class PsValueUpdateVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8472963331167092968L;

	private String dbColHstimeName;
	private String dbColName;
	private String dbTblName;
	private long moNo;
	private String psId;
	private Object value;
	private String updateFilter;

	public PsValueUpdateVo() {
	}

	public PsValueUpdateVo(long moNo, String updateFilter, String psId, Object value, String dbTblName,
			String dbColName, String dbColHstimeName) {
		this.moNo = moNo;
		this.updateFilter = updateFilter;
		this.value = value;
		this.psId = psId;
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
			sb.append(" , " + dbColHstimeName + " = " + DateUtil.getDtm());
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

	public String getPsId() {
		return psId;
	}

}

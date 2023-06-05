package subkjh.dao.database;

import java.util.Map;

import subkjh.dao.def.Column;

public class PostgreSQL extends DataBase {

	public enum DATA_TYPE {
		bigint(0, "signed eight-byte integer", Long.class) //
		, bigserial(0, "serial8	autoincrementing eight-byte integer", Long.class) //
		, bit(0, "fixed-length bit string", null) //
		, varbit("n", "variable-length bit string", null) //
		, bool(0, "logical Boolean (true/false)", Boolean.class)//
		, box(0, "rectangular box on a plane", Object.class) //
		, bytea(0, "binary data. byte array", Object.class) //
		, CHAR(0, "fixed-length character string", String.class) //
		, varchar(0, "variable-length character", String.class) //
		, cidr(0, "IPv4 or IPv6 network", String.class) //
		, circle(0, "circle on a plane", Object.class) //
		, date(0, "calendar date(year, month, day)", String.class) //
		, float8(0, "double precision floating-point number (8 bytes)", Double.class) //
		, inet(0, "IPv4 or IPv6 host address", String.class) //
		, INTEGER(0, "signed four-byte integer", Integer.class) //
		, INT(0, "signed four-byte integer", Integer.class) //
		, int4(0, "signed four-byte integer", Integer.class) //
		, interval(0, "time span", Object.class) //
		, json(0, "textual JSON data", String.class) //
		, jsonb(0, "binary JSON data, decomposed", Object.class) //
		, line(0, "infinite line on a plane", Object.class) //
		, lseg(0, "line segment on a plane", Object.class) //

		, macaddr(0, "MAC (Media Access Control) address", String.class) //
		, macaddr8(0, "MAC (Media Access Control) address (EUI-64 format)", String.class) //
		, money(0, "currency amount", Float.class) //

		, numeric(0, "exact numeric of selectable precision", Number.class) //
		, decimal(0, "exact numeric of selectable precision", Number.class) //
		, path(0, "geometric path on a plane", String.class) //
		, pg_lsn(0, "PostgreSQL Log Sequence Number", Long.class) //
		, pg_snapshot(0, "geometric point on a plane", String.class) //
		, polygon(0, "closed geometric path on a plane", String.class) //

		, real(0, "single precision floating-point number (4 bytes)", Float.class) //
		, float4(0, "single precision floating-point number (4 bytes)", Float.class) //
		, smallint(0, "signed two-byte integer", Short.class) //
		, int2(0, "signed two-byte integer", Short.class) //

		, smallserial(0, "autoincrementing two-byte integer", Short.class) //
		, serial2(0, "autoincrementing two-byte integer", Short.class) //
		, serial(0, "autoincrementing four-byte integer", Integer.class) //
		, serial4(0, "autoincrementing four-byte integer", Integer.class) //
		, text(0, "variable-length character string", String.class) //

		, TIME(0, "time of day (no time zone)", String.class) //
		, timetz(0, "time of day, including time zone", String.class) //

		, timestamp(0, "date and time (no time zone)", String.class) //
		, timestamptz(0, "date and time, including time zone", String.class) //
		, tsquery(0, "text search query", String.class) //
		, tsvector(0, "text search document", String.class) //
		, txid_snapshot(0, "user-level transaction ID snapshot (deprecated; see pg_snapshot)", String.class) //

		, uuid(0, "	universally unique identifier", String.class) //
		, xml(0, "XML data", String.class)

		;

		DATA_TYPE(Object len, String descr, Class<?> classOfJava) {

		}
	}

	final String JDRIVER = "org.postgresql.Driver";

	/**
	 * 
	 */
	private static final long serialVersionUID = -6739926612081498320L;

	@Override
	public Exception makeException(Exception e, String msg) {
		return e;
	}

	@Override
	public String makeUrl(Map<String, Object> para) {
		return null;
	}

	@Override
	public String getDataTypeFull(Column column) {

		String datatype = column.getDataType().toLowerCase();

		if (datatype.startsWith("varchar")) {
			return "varchar(" + column.getDataLength() + ") ";
		} else if (datatype.equalsIgnoreCase("char")) {
			return column.getDataType() + "(" + column.getDataLength() + ")";
		} else if (datatype.equals("number")) {
			if (column.getDataScale() == 0) {
				return column.getDataLength() > 9 ? "int8" : "int4";
			} else {
				return column.getDataLength() > 9 ? "float8" : "float4";
			}
		} else {
			return column.getDataType();
		}
	}

}

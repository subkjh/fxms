package com.fxms.ui.bas.renderer.column;

import com.fxms.ui.bas.lang.Lang;

import javafx.scene.control.TableColumn;

public class TableColumnData {

	public static void main(String[] args) {
		TableColumnData col = new TableColumnData(null, "H0_설치_메모");
		System.out.println(col);
	}

	public enum TYPE {

		hidden('H')

		, string('S')

		, seqno('N')

		, date('D')

		, alarmLevel('A')

		, count('C')

		, alarmStaus('U')

		, okfail('O')

		, managed('M')
		
		, traffic('F')

		, yn('Y');

		private char tag;

		private TYPE(char tag) {
			this.tag = tag;
		}

		public static TYPE getType(char type) {
			for (TYPE e : TYPE.values()) {
				if (e.tag == type) {
					return e;
				}
			}

			return string;
		}
	}

	private int length;
	private String text;
	private String name;
	private TYPE type = TYPE.hidden;

	public TableColumnData(String qid, String fname) {

		int i = fname.indexOf('_');

		try {
			type = TYPE.getType(fname.charAt(0));
		} catch (Exception e) {
			type = TYPE.string;
		}

		try {

			// 타입과 길이가 지정된 경우
			length = Integer.valueOf(fname.substring(1, i));
			name = fname.substring(i + 1);

		} catch (Exception e) {
			// 일반 컬럼명인 경우
			type = TYPE.hidden;
			name = fname;
		}

		text = Lang.getText(Lang.Type.column, name);

		name = getJavaFieldName(name);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return type + ", " + length + ", " + text;
	}

	public int getLength() {
		return length;
	}

	public TYPE getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public TableColumn<Object[], ?> makeTableColumn(int index) {
		if (type == TYPE.seqno) {
			return new NoTableColumn(index, getText(), getLength());
		} else if (type == TYPE.date) {
			return new DateTableColumn(index, getText(), getLength());
		} else if (type == TYPE.alarmLevel) {
			return new AlarmLevelTableColumn(index, getText(), getLength());
		} else if (type == TYPE.alarmStaus) {
			return new AlarmStatusTableColumn(index, getText(), getLength());
		} else if (type == TYPE.traffic) {
			return new TrafficTableColumn(index, getText(), getLength());
		} else if (type == TYPE.managed) {
			return new MngTableColumn(index, getText(), getLength());
		} else if (type == TYPE.okfail) {
			return new OkFailTableColumn(index, getText(), getLength());
		} else if (type == TYPE.yn) {
			return new YnTableColumn(index, getText(), getLength());
		} else {
			return new StringTableColumn(index, getText(), getLength());
		}
	}

	public static String getJavaFieldName(String columnName) {
		int index = columnName.toUpperCase().startsWith("IS_") ? 3 : 0;
		String field = (columnName.charAt(index) + "").toLowerCase();

		index++;

		for (int size = columnName.length(); index < size; index++) {
			if (columnName.charAt(index) == '_') {
				index++;
				field += (columnName.charAt(index) + "").toUpperCase();
			} else {
				field += (columnName.charAt(index) + "").toLowerCase();

			}
		}

		return field;
	}
}

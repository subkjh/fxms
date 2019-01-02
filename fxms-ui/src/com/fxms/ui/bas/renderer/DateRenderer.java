package com.fxms.ui.bas.renderer;

public class DateRenderer extends TextRenderer {

	public DateRenderer(Object value) {
		setValue(value, null);
	}
	
	public DateRenderer() {

	}

	@Override
	public void setValue(Object value, String type) {
		String date;
		if (value instanceof Number) {
			if (((Number) value).intValue() == 0) {
				setText("-");
				return;
			} else {
				date = String.valueOf(((Number) value).longValue());
			}
		} else {
			date = String.valueOf(value);
		}

		if (date.length() == 8) {
			char chs[] = date.toCharArray();
			StringBuffer sb = new StringBuffer();
			sb = sb.append(chs[0]).append(chs[1]).append(chs[2]).append(chs[3]).append("-");
			sb = sb.append(chs[4]).append(chs[5]).append("-").append(chs[6]).append(chs[7]);
			setText(sb.toString());
		} else if (date.length() == 14) {
			char chs[] = date.toCharArray();
			StringBuffer sb = new StringBuffer();
			sb = sb.append(chs[0]).append(chs[1]).append(chs[2]).append(chs[3]).append("-");
			sb = sb.append(chs[4]).append(chs[5]).append("-").append(chs[6]).append(chs[7]);
			sb.append(" ");
			sb = sb.append(chs[8]).append(chs[9]).append(":").append(chs[10]).append(chs[11]).append(":")
					.append(chs[12]).append(chs[13]);
			setText(sb.toString());
		} else {
			setText(date);
		}

	}

}

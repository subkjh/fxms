package com.fxms.ui.bas.renderer;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.control.Label;

public class MsdateRenderer extends Label {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public MsdateRenderer(Object msdate) {
		String date;
		if (msdate instanceof Number) {
			if (((Number) msdate).intValue() == 0) {
				setText("-");
			} else {
				date = YYYYMMDDHHMMSS.format(new Date(((Number) msdate).longValue()));
				setText(date);
			}
		} else {
			setText(String.valueOf(msdate));
		}
	}

	public void setMsdate(long msdate) {
		String date = YYYYMMDDHHMMSS.format(new Date(msdate));
		setText(date);
	}

}

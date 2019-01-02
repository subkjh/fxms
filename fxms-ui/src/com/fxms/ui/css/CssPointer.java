package com.fxms.ui.css;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import FX.MS.UiConfig;

public class CssPointer {

	public static final String FxInnerWindowTitleBar = "fx-inner-window-title-bar";
	public static final String FxInnerWindowTitle = "fx-inner-window-title";
	public static final String FxInnerWindowClose = "fx-inner-window-close";
	public static final String FxWindowClose = "fx-window-close";
	public static final String FxWindowTitle = "fx-window-title";
	public static final String FxWindowTitleMsg = "fx-window-title-msg";
	public static final String FxWindowTitleBar = "fx-window-title-bar";
	public static final String FxWindowButton = "fx-window-button";

	public static void main(String[] args) {
		CssPointer.getStyle("fxms.css", "alarm-major-cleared");
		System.out.println(getStyleSheet("fxms.css"));
		System.out.println(CssPointer.class.getResource("fxms.css").toExternalForm());
	}

	public static String getStyleSheet(String name) {

		File file = new File(UiConfig.getConfig().getFolder("deploy", "conf", "css") + "/" + name);

		if (file.exists()) {
			return "file:/" + file.getPath().replaceAll("\\\\", "/");
		}

		return CssPointer.class.getResource(name).toExternalForm();
	}

	public static String getStyle(String filename, String selector) {
		String content = getString(CssPointer.class.getResourceAsStream(filename));

		String ss[] = content.split("[}|{]");
		for (String s : ss) {
			System.out.println(s);
		}
		return content;
	}

	public static String getString(InputStream inputStream) {
		BufferedReader in = null;
		StringBuffer ret = new StringBuffer();
		String val = "";

		try {
			in = new BufferedReader(new InputStreamReader(inputStream));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}

					ret.append(val);
					ret.append("\n");

				} catch (IOException e) {
					break;
				}
			}
		} catch (Exception e) {

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret.toString();

	}
}

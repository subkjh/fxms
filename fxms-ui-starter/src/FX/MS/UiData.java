package FX.MS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UiData {

	public enum UI_LOG_LEVEL {
		warning, error, info;
	}

	public static final String server = "server";
	public static final String port = "port";
	private static Map<String, String> confMap = null;

	private static String getConfigFile() {

		String cfgfile = System.getProperty("user.home") + "/fxms/deploy/conf";
		File folder = new File(cfgfile);

		if (folder.exists() == false) {
			folder.mkdirs();
		}

		return folder.getPath() + "/cfg.data";
	}

	public static String getConfig(String name, String defValue) {
		if (confMap == null) {
			confMap = new HashMap<String, String>();
			loadConfig();
		}
		String ret = confMap.get(name);
		return ret == null ? defValue : ret;
	}

	public static void setConfig(String name, String value) {
		confMap.put(name, value);
	}

	private static void loadConfig() {

		BufferedReader in = null;
		String val;
		System.out.println(getConfigFile());

		try {
			in = new BufferedReader(new FileReader(new File(getConfigFile())));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}

					String ss[] = val.split("=");

					if (ss.length == 2) {
						confMap.put(ss[0].trim(), ss[1].trim());
					}

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

	}

	public static boolean writeConfig() {

		File file = new File(getConfigFile());

		FileOutputStream outStream = null;
		try {
			String datas;
			outStream = new FileOutputStream(file, false);

			List<String> keyList = new ArrayList<>(confMap.keySet());
			Collections.sort(keyList);

			outStream.write("#\n".getBytes());
			outStream.write("# Fxms UI Configuration \n".getBytes());
			outStream.write("#\n\n".getBytes());

			outStream.write("#######################################################################\n".getBytes());
			outStream.write("### Basic\n\n".getBytes());

			for (String key : keyList) {
				if (key.startsWith("FILE") == false) {
					datas = key + "=" + confMap.get(key) + "\n";
					outStream.write(datas.getBytes());
				}
			}

			outStream.write("\n\n".getBytes());
			outStream.write("#######################################################################\n".getBytes());
			outStream.write("### Download File List\n\n".getBytes());

			for (String key : keyList) {
				if (key.startsWith("FILE")) {
					datas = key + "=" + confMap.get(key) + "\n";
					outStream.write(datas.getBytes());
				}
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
}

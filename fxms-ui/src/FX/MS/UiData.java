package FX.MS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.vo.ListData;

public class UiData {

	public enum UI_LOG_LEVEL {
		warning, error, info;
	}

	private static final SimpleDateFormat YMD = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final String server = "server";
	public static final String port = "port";
	public static final String userid = "userid";
	public static final String password = "password";
	public static final String fullscreen = "fullscreen";
	private static final String filename = System.getProperty("user.home") + "/fxms/logs/ui.log";
	private static final String searchFileName = System.getProperty("user.home") + "/fxms/logs/search.log";

	private static Map<String, String> confMap = null;

	

	public static String getConfig(String name, String defValue) {
		if (confMap == null) {
			confMap = new HashMap<String, String>();
			loadConfig();
		}
		String ret = confMap.get(name);
		return ret == null ? defValue : ret;
	}

	public static List<String> getSearchHistory() {
		List<String> ret = new ArrayList<String>();

		BufferedReader in = null;
		String val;

		try {
			in = new BufferedReader(new FileReader(new File(searchFileName)));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}

					ret.add(val);

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

		return ret;

	}

	public static ListData getUiLog() {
		ListData listData = new ListData();
		listData.setColumns(new String[] { "D150_DATE", "S100_LOG_LEVEL", "S600_MESSAGE" });
		getLines(listData);
		return listData;
	}

	public static void log(UI_LOG_LEVEL logLevel, String msg) {
		String date = YMD.format(new Date(System.currentTimeMillis()));
		StringBuffer sb = new StringBuffer();
		sb.append(logLevel);
		sb.append("\t");
		sb.append(date);
		sb.append("\t");
		sb.append(msg);
		sb.append("\n");

		writeLog(sb.toString(), true);
	}

	public static void logStart() {
		String date = YMD.format(new Date(System.currentTimeMillis()));
		StringBuffer sb = new StringBuffer();
		sb.append("start");
		sb.append("\t");
		sb.append(date);
		sb.append("\n");

		writeLog(sb.toString(), false);
	}

	public static void setConfig(String name, String value) {
		confMap.put(name, value);
		writeConfig();
	}

	public static boolean writeLog(String datas, boolean append) {
		return write(filename, datas, append);
	}

	public static boolean writeSearch(String datas, boolean append) {
		return write(searchFileName, datas + "\n", append);
	}

	private static List<String> getLines(ListData listData) {
		List<String> ret = new ArrayList<String>();

		BufferedReader in = null;
		String val;

		try {
			in = new BufferedReader(new FileReader(new File(filename)));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}

					String ss[] = val.split("\t");
					if (ss.length > 2) {
						listData.getDataList().add(0, new String[] { ss[1], ss[0], ss[2] });
					} else {
						listData.getDataList().add(0, new String[] { ss[1], ss[0], "" });
					}

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret;

	}

	private static void loadConfig() {

		BufferedReader in = null;
		String val;

		try {
			in = new BufferedReader(new FileReader(new File(UiConfig.getConfig().getCfgData())));

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

	private static boolean write(String filename, String datas, boolean append) {

		File file = new File(filename);

		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, append);
			outStream.write(datas.getBytes());
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

	public static boolean writeConfig() {

		File file = new File(UiConfig.getConfig().getCfgData());

		FileOutputStream outStream = null;
		try {
			String datas;
			outStream = new FileOutputStream(file, false);

			List<String> keyList = new ArrayList<>(confMap.keySet());
			Collections.sort(keyList);

			outStream.write("#\n".getBytes());
			outStream.write("# Fxms UI Configuration \n".getBytes());
			outStream.write("#\n\n".getBytes());

			outStream.write("#####################################################################\n".getBytes());
			outStream.write("### Basic\n\n".getBytes());

			for (String key : keyList) {
				if (key.startsWith("FILE-") == false) {
					datas = key + "=" + confMap.get(key) + "\n";
					outStream.write(datas.getBytes());
				}
			}

			outStream.write("\n\n".getBytes());
			outStream.write("######################################################################\n".getBytes());
			outStream.write("### Download File List\n\n".getBytes());

			for (String key : keyList) {
				if (key.startsWith("FILE-")) {
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

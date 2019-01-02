package FX.MS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.dx.DxAsyncSelector;
import com.google.gson.Gson;

public class UiConfig {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");

	public synchronized static String getTime(long milliseconds) {
		return YYYYMMDDHHMMSS.format(new Date(milliseconds));
	}

	private static UiConfig config;

	public static UiConfig getConfig() {

		if (config == null) {
			config = new UiConfig();
		}

		return config;
	}

	public void updateUiSize(int opNo, double width, double height) {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("opNo", opNo);
		para.put("uiWidth", width);
		para.put("uiHeight", height);

		DxAsyncSelector.getSelector().callMethod("cd", "update-op-size", para, null);

	}

	public String getFolder(String... folders) {
		String folder = getHome();
		for (String f : folders) {
			folder = folder + "/" + f;
		}

		File file = new File(folder);
		if (file.exists() == false) {
			file.mkdirs();
		}

		return folder;
	}

	private long systemTime;
	private long clientTime;

	public String getNowSystemTime() {
		return HHMMSS.format(new Date(systemTime + (System.currentTimeMillis() - clientTime)));
	}

	public void setTime(long systemTime) {

		clientTime = System.currentTimeMillis();
		this.systemTime = systemTime;

		//System.out.println(getTime(clientTime) + " : " + getTime(systemTime));

	}

	public void writeInitData(String name, Object obj) {

		write(UiConfig.getConfig().getFolder("datas"), name + ".json", new Gson().toJson(obj));
	}

	public String getHome() {
		return System.getProperty("user.home") + "/fxms";
	}

	public String getHomeLogs() {
		return getFolder("logs");
	}

	public String getCfgData() {
		String file = getFolder("deploy", "conf");
		String name = new File(file).getPath() + "/cfg.data";
		return name;
	}

	public void write(String folder, String filename, String datas) {

		File file = new File(folder + "/" + filename);

		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, false);
			outStream.write(datas.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
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

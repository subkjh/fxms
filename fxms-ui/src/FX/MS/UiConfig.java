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
	private static UiConfig config;

	public static UiConfig getConfig() {

		if (config == null) {
			config = new UiConfig();
		}

		return config;
	}

	public synchronized static String getTime(long milliseconds) {
		return YYYYMMDDHHMMSS.format(new Date(milliseconds));
	}

	/** 서버 시간 */
	private long systemTime;
	/** 서버 시간 받은 시점의 로컬 시간 */
	private long clientTime;

	public String getCfgData() {
		String file = getFolder("deploy", "conf");
		String name = new File(file).getPath() + "/cfg.data";
		return name;
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

	public String getHome() {
		return System.getProperty("user.home") + "/fxms";
	}

	public String getHomeLogs() {
		return getFolder("logs");
	}

	/**
	 * 
	 * @return HHMMSS 형식의 시스템(서버) 시간
	 */
	public String getNowSystemTime() {
		return HHMMSS.format(new Date(systemTime + (System.currentTimeMillis() - clientTime)));
	}

	/**
	 * 
	 * @param systemTime
	 *            mstime의 시스템(서버) 시간
	 */
	public void setSystemTime(long systemTime) {
		clientTime = System.currentTimeMillis();
		this.systemTime = systemTime;
	}

	/**
	 * 화면의 크기를 기록한다.
	 * 
	 * @param opNo
	 *            화면번호
	 * @param width
	 *            넓이
	 * @param height
	 *            높이
	 */
	public void updateUiSize(int opNo, double width, double height) {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("opNo", opNo);
		para.put("uiWidth", width);
		para.put("uiHeight", height);

		DxAsyncSelector.getSelector().callMethod("cd", "update-op-size", para, null);

	}

	public void writeInitData(String name, Object obj) {
		write(UiConfig.getConfig().getFolder("datas"), name + ".json", new Gson().toJson(obj));
	}

	private void write(String folder, String filename, String datas) {

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

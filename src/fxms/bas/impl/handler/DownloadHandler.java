package fxms.bas.impl.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import subkjh.bas.co.log.Logger;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 
 * @author subkjh
 *
 */
public class DownloadHandler extends BaseHandler {

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected byte[] onProcess(HttpExchange he, InetSocketAddress client, String callFunc, String body)
			throws Exception {

		Gson son = new Gson();
		Map<String, Object> map = son.fromJson(body, HashMap.class);

		String method = getString(map, "method");
		if ("download".equalsIgnoreCase(method)) {
			String filename = getString(map, "file-name");
			return download(filename);
		} else if ("list".equalsIgnoreCase(method)) {
			String folder = getString(map, "folder");
			return list(folder);
		}

		throw new Exception("Not Implement");
	}

	private byte[] download(String filename) throws Exception {

		File file = new File(FxCfg.getHome(filename));

		Logger.logger.debug("file-name={}, file={}, exist={}", filename, file.getPath(), file.exists());

		if (file.exists() == false) {
			throw new FileNotFoundException(file.getName());
		}

		FileInputStream inStream = null;
		byte bytes[] = new byte[(int) file.length()];

		try {
			inStream = new FileInputStream(file);
			inStream.read(bytes);
			return bytes;
		} catch (IOException e) {
			throw e;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void fillFileList(File file, List<Map<String, Object>> list) {
		for (File f : file.listFiles()) {

			if (f.isDirectory()) {
				fillFileList(f, list);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", f.getPath().replaceAll(FxCfg.getHome() + File.separator, ""));
				map.put("size", f.length());
				map.put("lastModified", f.lastModified());
				list.add(map);
			}
		}
	}

	private byte[] list(String folder) throws Exception {

		File file = new File(FxCfg.getHome(folder));

		Logger.logger.debug("folder={}, exist={}", folder, file.isDirectory());

		if (file.isDirectory() == false) {
			throw new FileNotFoundException(file.getName());
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		fillFileList(file, list);

		return new Gson().toJson(list).getBytes();
	}

}

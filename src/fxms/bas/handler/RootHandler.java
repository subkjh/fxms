package fxms.bas.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;

public class RootHandler extends FxHttpHandler {

	@Override
	protected byte[] onProcess(HttpExchange he, InetSocketAddress client, String callMethod, String body)
			throws Exception {

		String path = he.getRequestURI().getPath();
		byte bytes[] = null;
		String filename;

		Logger.logger.info("{}", path);

		if (path == null || path.length() == 0 || path.equals("/")) {

			String response = "<h1>Welcome FxMS Framework</h1>";

			filename = FxCfg.getFile(FxCfg.getHomeDeploy(), "web", "fxms.html");
			File file = new File(filename);
			if (file.exists()) {
				bytes = FileUtil.getBytes(file);
			} else {
				bytes = response.getBytes();
			}
		} else {

			filename = FxCfg.getFile(FxCfg.getHomeDeploy(), "web", path.substring(1));
			File file = new File(filename);
			if (file.exists()) {
				bytes = FileUtil.getBytes(file);
			}
		}

		if (bytes == null) {
			throw new FileNotFoundException(path.substring(1));
		} else {
			if (filename != null) {
				if (filename.toLowerCase().endsWith("html")) {
					he.getResponseHeaders().add("Content-Type", "text/html");
				} else if (filename.toLowerCase().endsWith("js")) {
					he.getResponseHeaders().add("Content-Type", "text/javascript");
				}
			}
		}

		return bytes;

	}

}

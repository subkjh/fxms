package subkjh.bas.fao;

import java.io.File;

import subkjh.bas.co.log.Logger;

public abstract class BasFolderParser<DATA> extends BasFileParser<DATA> {

	public void parseFolder(File folder) {

		for (File file : folder.listFiles()) {
			if (isTargetFile(file)) {
				try {
					read(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		onFinishedAll(folder);
	}

	public abstract boolean isTargetFile(File file);

	protected void onFinishedAll(File folder) {
		Logger.logger.debug(folder.getPath());
	}
}

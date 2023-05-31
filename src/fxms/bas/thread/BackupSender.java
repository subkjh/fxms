package fxms.bas.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.thread.CycleFxThread;
import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;

/**
 * 전송되지 않은 데이터를 전송하는 스레드
 * 
 * @author subkjh(김종훈)
 *
 * @param <DATA>
 */
public class BackupSender<DATA> extends CycleFxThread {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private File folder;
	private String seqno = "";
	private int index = 0;
	private BatchSaver<DATA> batch;

	/**
	 * 
	 * 
	 * @param folder
	 * @param classOfDATA
	 *            전송할 데이터의 클래스
	 */
	public BackupSender(String name, File folder) {
		super(name, 10);

		this.folder = folder;
	}

	@Override
	public String getState(LOG_LEVEL level) {
		return super.toString() + "FOLDER(" + folder.getPath() + ")";
	}

	void backup(List<DATA> obj) {

		if (obj == null) {
			return;
		}

		String filename = folder.getPath() + File.separator + obj.getClass().getSimpleName() + "-" + getNextSeqno() + ".jof";
		try {
			writeObjectToFile(filename, obj);
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
		}

	}

	void setBatchSender(BatchSaver<DATA> a) {
		batch = a;
	}

	private String getNextSeqno() {

		String no = YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));

		if (seqno.equals(no)) {
			index++;
			return no + "-" + index;
		}

		index = 0;
		seqno = no;
		return seqno;
	}

	private boolean writeObjectToFile(String filename, Object obj) throws Exception {

		ObjectOutputStream outStream = null;
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			outStream = new ObjectOutputStream(fos);
			outStream.writeObject(obj);
			outStream.flush();
			return true;
		} catch (IOException e) {
			throw e;
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
				}
		}

	}

	@SuppressWarnings("unchecked")
	protected void doCycle(long mstime) {
		File fileArray[];
		List<DATA> objRead;
		long ptime;

		counter.setStatus(FXTHREAD_STATUS.Running);

		fileArray = folder.listFiles();
		if (fileArray != null) {

			for (File file : fileArray) {

				// 변경 후 30초 이상 지난 경우 처리합니다.
				if (file.lastModified() < System.currentTimeMillis() - 30000) {

					try {

						ptime = System.currentTimeMillis();

						objRead = (List<DATA>) FileUtil.readObjectToFile(file.getPath());

						batch.doInsert(objRead);

						if (file.delete()) {
							Logger.logger.debug("file({}) sent and deleted", file.getPath());
						} else {
							Logger.logger.fail("file({}) sent and delete failed", file.getPath());
						}

						counter.addOk(System.currentTimeMillis() - ptime);
					} catch (Exception e) {
						Logger.logger.error(e);

						if (file.delete()) {
							Logger.logger.fail("deleted {}", file.getName());
						} else {
							Logger.logger.fail("delete failed {}", file.getName());
						}
					}

				}

				// 하루가 지났음에도 불구하고 남아 있다면 그냥 삭제합니다.
				if (file.lastModified() < System.currentTimeMillis() - 86400000L) {
					if (file.delete())
						Logger.logger.debug("file({}) deleted with expired", file.getPath());
				}

			}
		}

		counter.setStatus(FXTHREAD_STATUS.Waiting);

	}

}

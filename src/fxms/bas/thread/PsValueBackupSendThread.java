package fxms.bas.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.fxo.thread.FxThread;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;

/**
 * 전송되지 않은 데이터를 전송하는 스레드입니다.
 * 
 * @author subkjh
 * 
 * @param <DATA>
 *            전송할 데이터 종류
 */
public abstract class PsValueBackupSendThread<DATA> extends FxThread {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private Class<?> classOfDATA;
	private File folder;
	private String seqno = "";
	private int index = 0;

	/**
	 * 
	 * 
	 * @param folder
	 * @param classOfDATA
	 *            전송할 데이터의 클래스
	 */
	public PsValueBackupSendThread(String name, File folder, Class<?> classOfDATA) {
		setName(name + "-BS");
		this.folder = folder;
		this.classOfDATA = classOfDATA;
	}

	public void backup(Object obj) {

		if (obj == null) {
			return;
		}

		String filename = folder.getPath() + File.separator + obj.getClass().getSimpleName() + "-" + getNextSeqno()
				+ ".jof";
		try {
			writeObjectToFile(filename, obj);
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
		}

	}

	@Override
	public String getState(LOG_LEVEL level) {
		return super.toString() + "FOLDER(" + folder.getPath() + ")";
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
	@Override
	protected void doWork() {

		File fileArray[];
		Object objRead;
		long ptime;

		while (isContinue()) {

			counter.setStatus(FXTHREAD_STATUS.Running);

			fileArray = folder.listFiles();
			if (fileArray != null) {
				for (File file : fileArray) {

					// 변경 후 30초 이상 지난 경우 처리합니다.
					if (file.lastModified() < System.currentTimeMillis() - 30000) {

						try {

							ptime = System.currentTimeMillis();

							objRead = FileUtil.readObjectToFile(file.getPath());
							if (classOfDATA.isInstance(objRead)) {
								if (send((DATA) objRead)) {
									if (file.delete()) {
										Logger.logger.debug("file({}) sent and deleted", file.getPath() );
									} else {
										Logger.logger.fail("file({}) sent and delete failed", file.getPath() );
									}
								}
							} else {
								if (file.delete()) {
									Logger.logger.fail("java-class({}) not {} and deleted", objRead.getClass().getName(), classOfDATA.getName() );
								} else {
									Logger.logger.fail("java-class({}) not {} and delete failed", objRead.getClass().getName(), classOfDATA.getName() );
								}
							}

							counter.addOk(System.currentTimeMillis() - ptime);
						} catch (Exception e) {
							Logger.logger.error(e);
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

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
			}

		}
	}

	/**
	 * 실제 처리하는 과정
	 * 
	 * @param data
	 * @return
	 */
	protected abstract boolean send(DATA data);

}

package fxms.bas.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.thread.QueueFxThread;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * 기록하거나 수정하는 스레드
 * 
 * @author subkjh
 * 
 * @param <ITEM>
 *            큐의 자료 종류
 * @param <DATA>
 *            기록하는 자료 종류
 */
public abstract class BatchSaver<DATA> extends QueueFxThread<DATA> {

	protected long countSaved;
	protected long countReq;
	private BackupSender<DATA> backupSender;
	private List<DATA> list = new ArrayList<DATA>();
	private int SIZE = 1000;
	private File folder;

	public BatchSaver(String name, int batchSize, String folderName) {
		super(1);
		SIZE = batchSize;

		setName(name);

		if (folderName != null) {

			if (folderName.charAt(0) != '/')
				folderName = BasCfg.getHome() + folderName;

			folder = new File(folderName);
			folder.mkdirs();

			if (folder != null) {
				BackupSender<DATA> backupSender = new BackupSender<DATA>(getName() + "-BakSnd", folder);
				backupSender.start();
			}
		}
	}

	/**
	 * 기록하는 작업을 수행합니다.
	 * 
	 * @throws Exception
	 */
	public abstract void doInsert(List<DATA> list) throws Exception;

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append("COUNT-IN-QUEUE(" + queue.size() + ")");
		sb.append("COUNT-SAVED(" + countSaved + ")");
		sb.append("COUNT-REQUESTED(" + countReq + ")");
		sb.append("BATCH-SIZE(" + SIZE + ")");
		sb.append("FILE-FOLDER(" + (folder == null ? "Not Defined" : folder.getPath()) + ")");
		sb.append(" " + super.getState(level));
		return sb.toString();
	}

	private void insert() {

		long ptime = System.currentTimeMillis();

		try {
			doInsert(list);
			getCounter().addOk(System.currentTimeMillis() - ptime);
		} catch (Exception e) {
			Logger.logger.error(e);
			if (backupSender != null) {
				backupSender.backup(list);
			}
		}
		list.clear();
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(DATA e) throws Exception {
		list.add(e);
		if (list.size() >= SIZE) {
			insert();
		}
	}

	/**
	 * 데이터가 일정 시간 안 들어오면 호출 되는 메소드
	 */
	@Override
	protected void onNoDatas(long index) {
		if (list.size() > 0) {
			insert();
		}
	}

}

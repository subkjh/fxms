package subkjh.bas.dao.control.sync;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import subkjh.bas.dao.control.CommDao;
import subkjh.bas.dao.control.DaoListener;
import subkjh.bas.dao.data.Table;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.FileUtil;

public class DumpDaoLsnr extends CommDao implements DaoListener {

	private List<Object[]> dataList;
	private int insertCnt = 0;
	private int size;
	private Table tab;
	private String folder;

	public DumpDaoLsnr(String folder, Table tab, int size) {
		this.folder = folder;
		this.tab = tab;
		this.size = size;
		dataList = new ArrayList<Object[]>();
	}
	public void onExecuted(Object data, Exception ex) throws Exception
	{
		
	}
	@Override
	public void onFinish(Exception ex) {
		try {
			FileUtil.writeObjectToFile(folder + File.separator + tab.getName() + ".data.jof", dataList);
		} catch (Exception e) {
			Logger.logger.error(e);
			e.printStackTrace();
		}

		System.out.println(tab.getName() + "-SIZE(" + insertCnt + ")");
		Logger.logger.info(tab.getName() + "-SIZE(" + insertCnt + ")");
	}

	@Override
	public void onSelected(int rowNo, Object obj) {
		
		Object dataArr[] = (Object [])obj;
		dataList.add(dataArr);
		insertCnt++;

		if (insertCnt % batchsize == 0 || insertCnt >= size) {
			String msg = makeString(tab.getName(), insertCnt, size);
			System.out.println(msg);
			Logger.logger.debug(msg);
		}
	}

	@Override
	public void onStart(String colNames[]) throws Exception {

		dataList.add(colNames);

		System.out.println("dumpping... " + tab.getName() + " " + Arrays.toString(colNames));

		Logger.logger.info("dumpping... " + tab.getName() + " " + Arrays.toString(colNames));

	}
}

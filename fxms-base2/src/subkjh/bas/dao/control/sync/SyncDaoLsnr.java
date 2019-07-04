package subkjh.bas.dao.control.sync;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import subkjh.bas.co.log.Logger;
import subkjh.bas.dao.control.CommDao;
import subkjh.bas.dao.control.DaoListener;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.data.Table;

public class SyncDaoLsnr extends CommDao implements DaoListener {

	private int insertCnt = 0;
	private PreparedStatement psDst;
	private int size;
	private String sql;
	private Table tabDst;
	private DbTrans tranDst;

	SyncDaoLsnr(Table tabDst, DbTrans tranDst, int size) {
		this.tranDst = tranDst;
		this.tabDst = tabDst;
		this.size = size;
	}

	public void onExecuted(Object data, Exception ex) throws Exception
	{
		
	}
	
	@Override
	public void onFinish(Exception ex) {
		try {
			psDst.close();
		} catch (SQLException e) {
			Logger.logger.error(e);
		}
		psDst = null;

		Logger.logger.info(tabDst.getName() + " = " + insertCnt);

	}

	@Override
	public void onSelected(int rowNo, Object obj) {

		Object dataArr[] = ( Object [])obj;
		try {

			setPreSt(psDst, dataArr);

			insertCnt++;

			if (batchsize <= 0) {
				psDst.executeUpdate();
			} else {

				psDst.addBatch();

				if (insertCnt % batchsize == 0 || insertCnt >= size) {
					String msg = makeString(tabDst.getName(), insertCnt, size);
					System.out.println(msg);
					Logger.logger.debug(msg);

					psDst.executeBatch();
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);

		}

	}

	@Override
	public void onStart(String colNames[]) throws Exception {

		String cols = null;
		String vals = null;
		for (String col : colNames) {
			if (cols == null) {
				cols = col;
				vals = " ? ";
			} else {
				cols += ", " + col;
				vals += " , ?";
			}
		}
		sql = "insert into " + tabDst.getName() + " ( " + cols + " ) values ( " + vals + " )";

		psDst = tranDst.getPrepareStatement(sql);

		Logger.logger.info(tabDst.getName());
		Logger.logger.info(sql);

	}
}

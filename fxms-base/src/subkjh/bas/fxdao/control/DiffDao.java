package subkjh.bas.fxdao.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public abstract class DiffDao<DATA> {

	private DbTrans srcTran;
	private DbTrans dstTran;

	public DiffDao() {
	}

	public DiffDao(DbTrans srcTran, DbTrans dstTran) {
		this.srcTran = srcTran;
		this.dstTran = dstTran;
	}

	public void diffByQid(String qid1, String qid2) throws Exception {
		diff(qid1, qid2, true);
	}

	public void diffBySql(String sql1, String sql2) throws Exception {
		diff(sql1, sql2, false);
	}

	public DbTrans getDstTran() {
		return dstTran;
	}

	public DbTrans getSrcTran() {
		return srcTran;
	}

	public void setTrans(DbTrans srcTran, DbTrans dstTran) {
		this.srcTran = srcTran;
		this.dstTran = dstTran;
	}

	private Map<String, DATA> convert(List<DATA> list) {

		Map<String, DATA> map = new HashMap<String, DATA>();
		for (DATA o : list) {
			map.put(makeKey(o), o);
		}
		return map;
	}

	private Map<String, Object[]> diff(DATA data1, DATA data2) {
		Map<String, Object> map1 = ObjectUtil.toMap(data1);
		Map<String, Object> map2 = ObjectUtil.toMap(data2);
		return diff0(map1, map2);
	}

	private Map<String, Object[]> diff0(Map<String, Object> map1, Map<String, Object> map2) {

		Map<String, Object[]> diffMap = new HashMap<String, Object[]>();
		Object obj1, obj2;

		for (String key : map1.keySet()) {
			obj1 = map1.get(key);
			obj2 = map2.get(key);

			if (obj1 == null && obj2 == null) {
				continue;
			}

			if ((obj1 != null && obj2 != null) && obj1.equals(obj2) == true) {
				continue;
			}

			diffMap.put(key, new Object[] { obj1, obj2 });

		}

		return diffMap;
	}

	private void diff(String qid1, String qid2, boolean isQid) throws Exception {

		try {
			srcTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		try {
			dstTran.start();
		} catch (Exception e) {
			e.printStackTrace();
			srcTran.stop();
			throw e;
		}

		List<DATA> addList = new ArrayList<DATA>();
		List<DATA> chgList = new ArrayList<DATA>();
		List<DATA> delList = new ArrayList<DATA>();
		List<DATA> nothingList = new ArrayList<DATA>();
		Map<String, Object[]> diffMap;
		DATA data2;
		try {

			List<DATA> srcList;
			if (isQid)
				srcList = selectByQid(srcTran, qid1);
			else
				srcList = selectBySql(srcTran, qid1);

			List<DATA> dstList;
			if (isQid)
				dstList = selectByQid(dstTran, qid2);
			else
				dstList = selectBySql(dstTran, qid2);

			Map<String, DATA> srcMap = convert(srcList);
			Map<String, DATA> dstMap = convert(dstList);

			// 조회 결과 통보
			onSelected(srcList, dstList);

			for (DATA data : srcList) {

				data2 = dstMap.get(makeKey(data));

				// 추가된 데이터
				if (data2 == null) {
					addList.add(data);
				}
				// 수정된 데이터
				else {
					diffMap = diff(data, data2);
					if (diffMap.size() > 0) {
						onDiff(data, data2, diffMap);
						chgList.add(data);
					} else {
						nothingList.add(data);
					}
				}
			}

			// 삭제된 데이터
			for (DATA data : dstList) {
				if (srcMap.get(makeKey(data)) == null) {
					delList.add(data);
				}
			}

			onCompleted(addList, chgList, delList, nothingList);

		} catch (Exception ex) {
			Logger.logger.error(ex);
			onCompleted(ex);
		} finally {
			close();
		}

	}

	protected void close() {
		if (srcTran != null) {
			try {
				srcTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (dstTran != null) {
			try {
				dstTran.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract String makeKey(DATA data);

	protected abstract void onCompleted(Exception ex);

	protected void onCompleted(List<DATA> addList, List<DATA> chgList, List<DATA> delList, List<DATA> nothingList)
			throws Exception {

		System.out.println("I N S E R T ----------------------------------------- " + addList.size());
		for (DATA data : addList) {
			System.out.println(data);
		}
		System.out.println("C H A N G E ----------------------------------------- " + chgList.size());
		for (DATA data : chgList) {
			System.out.println(data);
		}
		System.out.println("D E L E T E ----------------------------------------- " + delList.size());
		for (DATA data : delList) {
			System.out.println(data);
		}
	}

	protected void onDiff(DATA data1, DATA data2, Map<String, Object[]> diffMap) {

		StringBuffer sb = new StringBuffer();
		sb.append(makeKey(data1));
		for (String key : diffMap.keySet()) {
			sb.append(", ");
			sb.append(key);
			sb.append("=");
			sb.append(Arrays.toString(diffMap.get(key)));
		}

		Logger.logger.debug(sb.toString());
	}

	protected void onSelected(List<DATA> srcList, List<DATA> dstList) {
		Logger.logger.debug("SELECTE COUNT : SRC(" + (srcList == null ? 0 : srcList.size()) + ")DST("
				+ (dstList == null ? 0 : dstList.size()) + ")");
	}

	@SuppressWarnings("unchecked")
	protected List<DATA> selectByQid(DbTrans tran, String qid) throws Exception {
		return tran.selectQid2Res(qid, null);
	}

	@SuppressWarnings("unchecked")
	protected List<DATA> selectBySql(DbTrans tran, String sql) throws Exception {
		return (List<DATA>) tran.selectSql2Map(sql, null);
	}

}

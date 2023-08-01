package fxms.bas.impl.dpo.inlo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_INLO_MEM;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Inlo;
import fxms.bas.vo.InloExt;
import fxms.bas.vo.InloMem;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 설치위치 관계 설정 DFO
 * 
 * @author subkjh
 *
 */
public class InloMemSetDfo implements FxDfo<Void, Integer> {

	public static void main(String[] args) {
		InloMemSetDfo dfo = new InloMemSetDfo();
		try {
			dfo.setInitMemAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Integer call(FxFact fact, Void data) throws Exception {

		int userNo = fact.getUserNo();
		int upperInloNo = fact.getInt("upperInloNo");
		int inloNo = fact.getInt("inloNo");

		return setInloMem(userNo, upperInloNo, inloNo);
	}

	public int setInloMem(int userNo, int upperInloNo, int inloNo) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			List<FX_CF_INLO_MEM> memList = selectUpperMem(tran, upperInloNo, inloNo);

			for (FX_CF_INLO_MEM mem : memList) {
				FxTableMaker.initRegChg(userNo, mem);
			}

			tran.deleteOfClass(FX_CF_INLO_MEM.class, FxApi.makePara("lowerInloNo", inloNo)); // 이전 내용 삭제

			int ret = tran.insertOfClass(FX_CF_INLO_MEM.class, memList); // 신규로 추가

			tran.commit();

			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void setInitMemAll() throws Exception {

		List<Inlo> list = new SelectInloListDfo().selectInlo(null); // 설치위치 조회

		List<InloExt> voList = new MakeInloTreeDfo().getInloTree(list); // 계층으로 생성

		List<InloMem> memList = new ArrayList<InloMem>(); // 관계 생성
		for (InloExt upper : voList) {
			memList.addAll(upper.makeMemList());
		}

		setInloMemAll(0, memList); // 관계 기록

	}

	/**
	 * 설치위치 상하관계 기록
	 * 
	 * @param userNo
	 * @param memList
	 * @throws Exception
	 */
	private void setInloMemAll(int userNo, List<InloMem> memList) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			FX_CF_INLO_MEM mem;
			List<FX_CF_INLO_MEM> dataList = new ArrayList<FX_CF_INLO_MEM>();
			for (InloMem o : memList) {
				mem = new FX_CF_INLO_MEM();
				mem.setInloNo(o.getInloNo());
				mem.setLowerDepth(o.getLowerDepth());
				mem.setLowerInloNo(o.getLowerInloNo());
				FxTableMaker.initRegChg(0, mem);
				dataList.add(mem);
			}

			tran.deleteAllOfClass(FX_CF_INLO_MEM.class);

			tran.insertOfClass(FX_CF_INLO_MEM.class, dataList);

			tran.commit();

		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	/**
	 * 
	 * @param tran
	 * @param upperInloNo
	 * @param inloNo
	 * @return
	 * @throws Exception
	 */
	private List<FX_CF_INLO_MEM> selectUpperMem(ClassDao tran, int upperInloNo, int inloNo) throws Exception {

		List<FX_CF_INLO_MEM> memList = new ArrayList<FX_CF_INLO_MEM>();

		// 1. 자신과 자신 매핑
		FX_CF_INLO_MEM mine = new FX_CF_INLO_MEM();
		mine.setInloNo(inloNo);
		mine.setLowerInloNo(inloNo);
		mine.setLowerDepth(0);
		FxTableMaker.initRegChg(0, mine);

		memList.add(mine);

		// 2. 상위와 자신 매핑
		if (upperInloNo > 0) {
			List<FX_CF_INLO_MEM> list = tran.selectDatas(FX_CF_INLO_MEM.class, FxApi.makePara("lowerInloNo", upperInloNo));
			for (FX_CF_INLO_MEM mem : list) {
				mem.setLowerInloNo(inloNo);
				mem.setLowerDepth(mem.getLowerDepth() + 1);
				FxTableMaker.initRegChg(0, mem);

			}
			memList.addAll(list);
		}

		return memList;

	}
}

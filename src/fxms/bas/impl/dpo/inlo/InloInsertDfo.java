package fxms.bas.impl.dpo.inlo;

import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.Inlo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 설치위치 등록
 * 
 * @author subkjh
 *
 */
public class InloInsertDfo implements FxDfo<Map<String, Object>, Integer> {

	@Override
	public Integer call(FxFact fact, Map<String, Object> data) throws Exception {

		int userNo = fact.getUserNo();
		Inlo upper = new SelectInloListDfo().selectInlo((int) data.get("inloNo"));
		return insertInlo(userNo, upper, data);
	}

	/**
	 * 
	 * @param userNo 등록자번호
	 * @param upper  상위위치
	 * @param para   등록할 위치정보
	 * @return
	 * @throws Exception
	 */
	public int insertInlo(int userNo, Inlo upper, Map<String, Object> para) throws Exception {

		FX_CF_INLO item = new FX_CF_INLO();
		ObjectUtil.toObject(para, item);
		FxTableMaker.initRegChg(userNo, item);

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			// 관리번호 새로 가져오기
			int inloNo = tran.getNextVal(FX_CF_INLO.FX_SEQ_INLONO, Integer.class);
			item.setInloNo(inloNo);

			if (upper != null && upper.getInloNo() > 0) {
				item.setInloAllName(InloDpo.getAllName(upper.getInloAllName(), item.getInloName()));
			} else {
				item.setInloAllName(item.getInloName());
			}

			// 설치위치 기록
			FxTableMaker.initRegChg(userNo, item);

			tran.insertOfClass(FX_CF_INLO.class, item);

			tran.commit();

			return inloNo;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

}

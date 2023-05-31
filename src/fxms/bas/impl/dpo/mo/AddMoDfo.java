package fxms.bas.impl.dpo.mo;

import java.util.Map;

import fxms.bas.co.CoCode.MO_WORK_TYPE_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dbo.all.FX_MX_WORK_HST;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class AddMoDfo implements FxDfo<Map<String, Object>, Long> {

	@Override
	public Long call(FxFact fact, Map<String, Object> datas) throws Exception {
		int userNo = fact.getUserNo();
		String moClass = fact.getString("moClass");
		return addMo(userNo, moClass, datas, "");
	}

	public Long addMo(int userNo, String moClass, Map<String, Object> datas, String reason) throws Exception {

		datas.put("moClass", moClass);

		FX_MO mo = MoDpo.getDboClass(moClass).newInstance();
		Map<String, Object> attrMap = ObjectUtil.toObject(datas, mo);
		try {
			mo.setMoAddJson(FxmsUtil.toJson(attrMap));
		} catch (Exception e) {
			Logger.logger.fail("{}", attrMap);
		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			mo.setMoNo(tran.getNextVal(FX_MO.FX_SEQ_MONO, Long.class));

			// 데이터 삭제할 때 실제 삭제하지 않고 DEL_YN = 'Y'만 설정하여 UK 위반이 발생되어
			// 상위관리번호를 자신의 번호로 한다.
			mo.setUpperMoNo(mo.getMoNo());

			// 등록, 수정일시 설정
			mo.setChgUserNo(userNo);
			mo.setRegUserNo(userNo);
			mo.setChgDtm(DateUtil.getDtm());
			mo.setRegDtm(mo.getChgDtm());

			// 관리대상을 기록한다.
			// insertOfClass 함수에서 FX_MO을 상속받은 FX_MO_XXXX이면 자동으로 FX_MO를 먼저 등록하고 FX_MO_XXXX를
			// 등록한다.
			tran.insertOfClass(mo.getClass(), mo);

			// 처리 이력을 기록한다.
			long workHstNo = tran.getNextVal(FX_MX_WORK_HST.FX_SEQ_WORKHSTNO, Long.class);
			tran.insertOfClass(FX_MX_WORK_HST.class,
					MoDpo.makeHstMo(workHstNo, mo, MO_WORK_TYPE_CD.Add, userNo, reason));

			tran.commit();

			return mo.getMoNo();

		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}
}

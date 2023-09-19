package fxms.bas.impl.dpo.mo;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.MoNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

public class UpdateMoDfo implements FxDfo<Map<String, Object>, Boolean> {

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {

		int userNo = fact.getUserNo();
		long moNo = fact.getMoNo();

		try {
			return updateMo(userNo, moNo, data);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

	}

	public boolean updateMo(int userNo, long moNo, Map<String, Object> para) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			// 있는 내역 조회
			Map<String, Object> wehrePara = FxApi.makePara("moNo", moNo);

			FX_MO mo2 = null;
			FX_MO mo = tran.selectOne(FX_MO.class, wehrePara);
			if (mo == null)
				throw new MoNotFoundException(moNo);

			// 입력된 속성만 다시 설정 후 업데이트
			Map<String, Object> attrMap = ObjectUtil.toObject(para, mo);

			// 확장된 MO인 경우 속성에 값을 설정한다.
			Class<?> classOfT = MoDpo.getDboClass(mo.getMoClass());
			if (classOfT != FX_MO.class) {
				mo2 = (FX_MO) tran.selectOne(classOfT, wehrePara);
				if (mo2 != null) {
					attrMap = ObjectUtil.toObject(para, mo2);
				}
			}

			// 테이블 컬럼과 매핑되지 않은 경우 JSON 컬럼에 넣는다.
			try {
				if (mo2 != null) {
					mo2.setMoAddJson(FxmsUtil.toJson(attrMap));
				} else {
					mo.setMoAddJson(FxmsUtil.toJson(attrMap));
				}
			} catch (Exception e) {
				Logger.logger.error(e);
			}

			if (mo2 != null) {
				// FX_MO을 상속받아서 updateOfClass에서 자동으로 FX_MO에도 수정한다.
				mo2.setChgUserNo(userNo);
				mo2.setChgDtm(DateUtil.getDtm());
				tran.updateOfClass(classOfT, mo2);
			} else {
				mo.setChgUserNo(userNo);
				mo.setChgDtm(DateUtil.getDtm());
				tran.updateOfClass(FX_MO.class, mo);
			}

			tran.commit();
			return true;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

}

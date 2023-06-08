package fxms.bas.impl.dpo.ao;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.co.CoCode.ALARM_LEVEL;
import fxms.bas.exp.AlcdNotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_AL_CD;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.AlarmCode;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.database.DBManager;

/**
 * 테스트 알람을 발생하는 DFO
 * 
 * @author subkjh
 *
 */
public class AlcdSelectDfo implements FxDfo<Void, List<AlarmCode>> {

	public static void main(String[] args) {

		AlcdSelectDfo dfo = new AlcdSelectDfo();
		FxFact fact = new FxFact("para", FxApi.makePara("moClass", "SENSOR"));
		try {
			dfo.call(fact, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<AlarmCode> call(FxFact fact, Void data) throws Exception {
		return selectAlarmCodeAll();
	}

	public List<AlarmCode> selectAlarmCodeAll() throws Exception {

		List<FX_AL_CD> list = ClassDaoEx.SelectDatas(FX_AL_CD.class, null);
		
		List<AlarmCode> ret = new ArrayList<AlarmCode>();
		for (FX_AL_CD a : list) {
			ret.add(new AlarmCode(a.getAlcdNo(), a.getAlcdName(), ALARM_LEVEL.getLevel(a.getAlarmLevel()) //
					, a.getPsId(), a.getCmprCd(), a.getAlarmMsg(), a.getFpactCd()//
					, a.getMoClass(), a.getAutoRlseSec(), a.isSvcAlarmYn()));
		}
		return ret;
	}

	public AlarmCode selectAlarmCode(int alcdNo) throws Exception {

		FX_AL_CD a = ClassDaoEx.SelectData(FX_AL_CD.class, FxApi.makePara("alcdNo", alcdNo));

		if (a == null) {
			throw new AlcdNotFoundException(alcdNo);
		}

		return new AlarmCode(a.getAlcdNo(), a.getAlcdName(), ALARM_LEVEL.getLevel(a.getAlarmLevel()) //
				, a.getPsId(), a.getCmprCd(), a.getAlarmMsg(), a.getFpactCd()//
				, a.getMoClass(), a.getAutoRlseSec(), a.isSvcAlarmYn());

	}

}

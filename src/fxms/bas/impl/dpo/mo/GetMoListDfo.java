package fxms.bas.impl.dpo.mo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.FxMo;
import fxms.bas.mo.Mo;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.exp.TableNotFoundException;

public class GetMoListDfo implements FxDfo<Map<String, Object>, List<Mo>> {

	public static void main(String[] args) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", 1002176L);
		GetMoListDfo dpo = new GetMoListDfo();
		FxFact fact = new FxFact("para", para);
		try {
//			System.out.println(FxmsUtil.toJson(dpo.call(fact, "para")));
//			System.out.println(FxmsUtil.toJson(dpo.selectMoList(para, SensorMo.class)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Mo> call(FxFact fact, Map<String, Object> data) throws Exception {

		return selectMoList(data);

	}

	public List<Mo> selectMoList(Map<String, Object> para) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		if (para == null) {
			para = FxApi.makePara("delYn", "N");
		} else {
			para.put("delYn", "N");
		}
		try {
			tran.start();
			return (List<Mo>) selectMoList(tran, para, Mo.class);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	public <T extends Mo> List<T> selectMoList(Map<String, Object> para, Class<T> classOfT) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		if (para == null) {
			para = FxApi.makePara("delYn", "N");
		} else {
			para.put("delYn", "N");
		}
		try {
			tran.start();
			return (List<T>) selectMoList(tran, para, classOfT);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Mo> List<T> selectMoList(ClassDao tran, Map<String, Object> wherePara, Class<T> classOfT)
			throws Exception {

		String inMoClass = MoApi.getMoClass(classOfT);
		if (FxMo.MO_CLASS.equals(inMoClass) == false) {
			wherePara.put("moClass", inMoClass);
		}

		List<Mo> moList = new ArrayList<Mo>();
		List<? extends Mo> tmpList = tran.select(FX_MO.class, wherePara, FxMo.class);

		// 기본 관리대상 정보만 조회하면 여기서 리턴
		if (tmpList.size() == 0) {
			return (List<T>) moList;
		}

		// MO분류 검색
		List<String> moClassList = new ArrayList<>();
		for (Mo mo : tmpList) {
			if (FxMo.MO_CLASS.equals(mo.getMoClass())) {
				moList.add(mo);
			} else if (moClassList.contains(mo.getMoClass()) == false) {
				moClassList.add(mo.getMoClass());
			}
		}

		// 검색 조건에서 MO분류 제외
		wherePara.remove("moClass");

		// 테이블 JOIN 조회
		Class<? extends FX_MO> classOfDbo;
		Class<? extends Mo> classOfMo;
		for (String moClass : moClassList) {

			classOfDbo = MoDpo.getDboClass(moClass);
			classOfMo = MoDpo.getMoClass(moClass);

			// 요구하는 클래스가 아닌 경우 무시한다.
			if (classOfT.isAssignableFrom(classOfMo) == false) {
				continue;
			}

			try {
				// 상세한 내역으로 다시 조회한다.
				List<Mo> entry = (List<Mo>) tran.select(classOfDbo, wherePara, classOfMo);
				moList.addAll(entry);

			} catch (TableNotFoundException e) {

				Logger.logger.fail("MO_CLASS changed {} -> {}", moClass, FxMo.MO_CLASS);

				// 테이블이 없으면 그냥 MO로 취급한다.
				for (Mo mo : tmpList) {
					if (moClass.equals(mo.getMoClass())) {
						mo.setMoClass(FxMo.MO_CLASS);
						moList.add(mo);
					}
				}
			}
		}

		// 속성을 가져온다.
		// selectAttr(tran, moList);
		for (Mo mo : moList) {
			if (mo.getAttrMap() != null && mo.getMoAddJson() != null) {
				try {
					mo.getAttrMap().putAll(FxmsUtil.toMapFromJson(mo.getMoAddJson()));
				} catch (Exception e) {
				}
			}
		}

		return (List<T>) moList;

	}

}

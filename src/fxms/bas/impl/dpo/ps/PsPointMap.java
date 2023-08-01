package fxms.bas.impl.dpo.ps;

import java.rmi.NotBoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.exp.AlcdNotFoundException;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.dbo.all.FX_PS_POINT;
import fxms.bas.impl.dvo.MoDvo;
import fxms.bas.vo.PsPoint;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDaoEx;

public class PsPointMap {

	private static PsPointMap map = null;
	private static final Object lockObj = new Object();

	/**
	 * 데이터가 없는 인스턴스만 제공<br>
	 * load()함수를 호출해야 데이터가 적재됨
	 * 
	 * @return
	 */
	public static PsPointMap getInstance() {

		synchronized (lockObj) {
			if (map == null) {
				map = new PsPointMap();
			}
		}

		return map;
	}

	/**
	 * 데이터가 적재된 인스턴스 제공
	 * 
	 * @return
	 */
	public static PsPointMap getMap() {

		synchronized (lockObj) {
			if (map == null) {
				map = new PsPointMap();
				try {
					map.load();
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}
		}

		return map;
	}

	public static void main(String[] args) {
		System.out.println(FxmsUtil.toJson(PsPointMap.getMap().pointMap));

	}

	private final Map<String, PsPoint> pointMap;

	private PsPointMap() {
		this.pointMap = new HashMap<>();
	}

	/**
	 * 
	 * @param alcdNo
	 * @return
	 * @throws NotFoundException
	 */
	public PsPoint getPsPoint(String pointId) throws AlcdNotFoundException {
		synchronized (this.pointMap) {
			PsPoint ret = this.pointMap.get(pointId);
			return ret;
		}
	}

	public void load() throws NotBoundException, Exception {

		ClassDaoEx dao = ClassDaoEx.open();
		List<PsPoint> list = dao.selectDatas(FX_PS_POINT.class, null, PsPoint.class);
		List<MoDvo> list2 = dao.selectDatas(FX_MO.class, FxApi.makePara("delYn", "N"), MoDvo.class);
		Map<String, MoDvo> map = new HashMap<>();
		for (MoDvo mo : list2) {
			map.put(mo.getMoTid(), mo);
		}
		dao.close();

		synchronized (this.pointMap) {
			this.pointMap.clear();

			MoDvo mo;
			for (PsPoint p : list) {
				mo = map.get(p.getMoTid());
				if (mo != null) {
					p.setMoNo(mo.getMoNo());
					p.setMoName(mo.getMoName());
				}
				this.pointMap.put(p.getPointId(), p);
			}
		}
		
		Logger.logger.info(Logger.makeString("PsPoint loaded", pointMap.size()));


	}

}

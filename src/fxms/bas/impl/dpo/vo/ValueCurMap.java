package fxms.bas.impl.dpo.vo;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;

/**
 * 이전값 보관용으로 이전 값과 현재 값의 변화 추이를 확인하기 위함입니다.<br>
 * 시스템 시작하고 이후 데이터만 보관한다.<br>
 * 저장소에 이전 데이터를 읽어오지 않는다.<br>
 */
public class ValueCurMap {

	/**
	 * Long : MO_NO, String : PS_ID
	 */
	private final Map<Long, Map<String, PsValueComp>> cacheValMap;

	public ValueCurMap() {
		this.cacheValMap = new HashMap<>();
	}

	/**
	 * 
	 * @param moNo
	 * @param psId
	 * @return
	 * @throws Exception
	 */
	public PsValueComp getCurValueInCache(long moNo, String psId) throws Exception {

		synchronized (this.cacheValMap) {

			Map<String, PsValueComp> map = this.cacheValMap.get(moNo);
			if (map != null) {
				return map.get(psId);
			}
		}

		return null;

	}

	/**
	 * 수집한 성능값을 메모리에 적재합니다.
	 * 
	 * @param mstime 수집일시
	 * @param value  값
	 */
	public void setCurValueInCache(PsVoList values) {

		long psDtm = values.getHstime();

		for (PsVo value : values) {
			setCurValueInCache(new PsValueComp(value.getMo().getMoNo(), value.getPsItem().getPsId(), null, null, psDtm,
					value.getValue()));
		}

	}

	/**
	 * 
	 * @param value
	 */
	public void setCurValueInCache(PsValueComp value) {

		Map<String, PsValueComp> map;

		synchronized (this.cacheValMap) {

			map = this.cacheValMap.get(value.getMoNo());
			if (map == null) {
				map = new HashMap<>();
				this.cacheValMap.put(value.getMoNo(), map);
				map.put(value.getPsId(), value);
			} else {

				PsValueComp prev = map.get(value.getPsId());
				if (prev == null) {
					map.put(value.getPsId(), value);
				} else {
					map.put(value.getPsId(), new PsValueComp(value.getMoNo(), value.getPsId(), prev.getCurDate(),
							prev.getCurValue(), value.getCurDate(), value.getCurValue()));
				}
			}
		}
	}

}

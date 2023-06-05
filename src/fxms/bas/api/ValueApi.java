package fxms.bas.api;

import java.util.List;
import java.util.Map;

import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValueComp;
import fxms.bas.vo.PsValueSeries;
import fxms.bas.vo.PsValues;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.co.log.LOG_LEVEL;

/**
 * 수집된 관리대상의 상태값을 관리하는 API
 * 
 * @author subkjh
 *
 */
public abstract class ValueApi extends FxApi {

	public enum StatFunction {
		Min {
			@Override
			public Number getValue(List<Number> list) {
				if (list == null || list.size() == 0) {
					return null;
				}

				Number retValue = list.get(0);

				for (Number value : list) {
					if (value.floatValue() < retValue.floatValue()) {
						retValue = value;
					}
				}
				return retValue;
			}
		},
		Max {
			@Override
			public Number getValue(List<Number> list) {
				if (list == null || list.size() == 0) {
					return null;
				}

				Number retValue = list.get(0);

				for (Number value : list) {
					if (value.floatValue() > retValue.floatValue()) {
						retValue = value;
					}
				}
				return retValue;
			}
		},
		Sum {
			@Override
			public Number getValue(List<Number> list) {
				if (list == null || list.size() == 0) {
					return null;
				}

				float retValue = 0;
				for (Number value : list) {
					retValue += value.floatValue();
				}
				return retValue;
			}
		},
		Avg {
			@Override
			public Number getValue(List<Number> list) {

				if (list == null || list.size() == 0) {
					return null;
				}

				return Sum.getValue(list).floatValue() / Count.getValue(list).intValue();
			}
		},
		Count {
			@Override
			public Number getValue(List<Number> list) {
				return list == null ? 0 : list.size();
			}
		};

		public static StatFunction get(String name) {
			for (StatFunction vo : StatFunction.values()) {
				if (vo.name().equals(name)) {
					return vo;
				}
			}
			return Avg;
		}

		private StatFunction() {

		}

		public abstract Number getValue(List<Number> list);
	}

	/** use DBM */
	public static ValueApi api;

	/**
	 * 사용할 DBM를 제공합니다.
	 * 
	 * @return DBM
	 */
	public synchronized static ValueApi getApi() {
		if (api != null)
			return api;

		api = makeApi(ValueApi.class);

		return api;
	}

	/**
	 * 
	 */
	public ValueApi() {
	}

	/**
	 * 수집한 데이터를 기록한다.
	 * 
	 * @param voList
	 * @param checkAlarm
	 */
	public abstract int addValue(PsVoRawList voList, boolean checkAlarm);

	/**
	 * 현재 성능을 조회합니다.
	 * 
	 * @param moNo     MO번호
	 * @param instance 인스턴스
	 * @param perfNo   성능항목
	 * @return 현재값
	 */
	public abstract PsValueComp getCurValue(long moNo, String moInstance, String psId) throws Exception;

	@Override
	public String getState(LOG_LEVEL level) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		return sb.toString();
	}

	/**
	 * 성능항목 기준으로 관리대상의 통계값을 구한다.
	 * 
	 * @param psId
	 * @param psKind
	 * @param startDtm
	 * @param endDtm
	 * @param stat
	 * @return
	 * @throws Exception
	 */
	public abstract Map<Long, Number> getStatValue(String psId, PsKind psKind, long startDtm, long endDtm,
			StatFunction statFunc) throws Exception;

	/**
	 * 관리대상이 수집한 성능을 조회한다.
	 * 
	 * @param moNo       관리대상번호
	 * @param psId       성능ID
	 * @param psKindName 성능데이터코드
	 * @param startDtm   시작일시
	 * @param endDtm     종료일시
	 * @return
	 * @throws Exception
	 */
	public abstract List<PsValueSeries> getValues(long moNo, String psId, String psKindName, long startDtm, long endDtm)
			throws Exception;

	/**
	 * 관리대상의 모든 수집 데이터를 조회한다.
	 * 
	 * @param moNo       관리대상번호
	 * @param psKindName 통계종류 ( 5분, 1시간, 1일 등등 )
	 * @param startDtm   시작일시
	 * @param endDtm     종료일시
	 * @return
	 * @throws Exception
	 */
	public abstract List<PsValues> getValues(long moNo, String psKindName, long startDtm, long endDtm) throws Exception;

	/**
	 * 관리대상의 입력된 성능ID에 해당되는 데이터를 조회한다.
	 * 
	 * @param moNo       관리대상번호
	 * @param psId       성능ID
	 * @param psKindName 통계종류 ( 5분, 1시간, 1일 등등 )
	 * @param psKindCol  컬럼
	 * @param startDtm   시작일시
	 * @param endDtm     종료일시
	 * @return
	 * @throws Exception
	 */
	public abstract List<PsValues> getValues(long moNo, String psId, String psKindName, String psKindCol, long startDtm,
			long endDtm) throws Exception;

	/**
	 * 관리대상의 모든 수집 데이터를 조회한다.
	 * 
	 * @param psId       성능ID
	 * @param psKindName 통계종류 ( 5분, 1시간, 1일 등등 )
	 * @param psKindCol  컬럼
	 * @param startDtm   시작일시
	 * @param endDtm     종료일시
	 * @return
	 * @throws Exception
	 */
	public abstract List<PsValues> getValues(String psId, String psKindName, String psKindCol, long startDtm,
			long endDtm) throws Exception;

	@Override
	public void onCreated() throws Exception {

	}

	@Override
	public void reload(Enum<?> type) throws Exception {
	}

}
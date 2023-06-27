package fxms.bas.impl.dpo.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ValueApi;
import fxms.bas.exp.AttrNotFoundException;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.handler.dto.GetOperatingRateDto;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsValues;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 관리대상의 가동시간, 가동율을 조회한다.
 *
 * @author subkjh
 *
 */
public class GetOperatingRateDfo implements FxDfo<GetOperatingRateDto, List<Map<String, Object>>> {

	public static void main(String[] args) throws AttrNotFoundException, Exception {
		MoApi.api = new MoApiDfo();
		ValueApi.api = new ValueApiDfo();

		GetOperatingRateDfo dfo = new GetOperatingRateDfo();

		List<Map<String, Object>> result = dfo.getOperatingRate(
				FxAttrApi.toObject(FxApi.makePara("moNo", 10002L, "psKindName", "15M"), GetOperatingRateDto.class));

		System.out.println(FxmsUtil.toJson(result));
	}

	class Data {
		long moNo; // 관리대상번호
		long operatingSeconds; // 가동시간(초)
		float operatingRate; // 가동율
	}

	@Override
	public List<Map<String, Object>> call(FxFact fact, GetOperatingRateDto data) throws Exception {
		return getOperatingRate(data);
	}

	public List<Map<String, Object>> getOperatingRate(GetOperatingRateDto dto) throws Exception {

		List<Map<String, Object>> ret = new ArrayList<>();

		PsItem psItem = PsApi.getApi().getPsItem(PsApi.MO_STATUS_PS_ID);
		PsKind psKind = PsApi.getApi().getPsKind(dto.getPsKindName());

		List<PsValues> list = ValueApi.getApi().getValues(dto.getMoNo(), dto.getMoInstance(), psItem.getPsId(),
				psKind.getPsKindName(), psItem.getDefKindCol(), dto.getStartDate(), dto.getEndDate());

		for (PsValues value : list) {
			
			Data data = new Data();
			data.moNo = value.getMoNo();
			data.operatingSeconds = 0;
			long seconds = 0;

			for (Number num : value.getValueOnly()) {
				data.operatingSeconds += (num.floatValue() * psKind.getIntervalSeconds());
				seconds += psKind.getIntervalSeconds();
			}

			data.operatingRate = Float.parseFloat(String.format("%.2f", data.operatingSeconds * 100f / seconds));

			ret.add(ObjectUtil.toMap(data));
		}

		return ret;
	}

}
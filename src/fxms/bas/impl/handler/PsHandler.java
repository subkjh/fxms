package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.api.PsApi;
import fxms.bas.api.ServiceApi;
import fxms.bas.api.ValueApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.MoService;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.AlarmHandlerQid;
import fxms.bas.impl.dao.PsHandlerQid;
import fxms.bas.impl.dbo.all.FX_PS_ITEM;
import fxms.bas.impl.dbo.all.FX_PS_STAT_KIND;
import fxms.bas.impl.dpo.vo.GetOperatingRateDfo;
import fxms.bas.impl.dto.ValueAddDto;
import fxms.bas.impl.handler.dto.GetOperatingRateDto;
import fxms.bas.impl.handler.dto.GetValuesDto;
import fxms.bas.impl.handler.dto.SelectPsValueMinMaxDto;
import fxms.bas.impl.handler.dto.SelectPsValueRtListPara;
import fxms.bas.impl.vo.PsItemVo;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsVoRawList;
import subkjh.bas.BasCfg;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 수집한 성능을 제공한다.<br>
 * 
 * @author subkjh
 *
 */
public class PsHandler extends BaseHandler {

	public static void main(String[] args) throws Exception {
		PsHandler handler = new PsHandler();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("moNo", 100001);
		parameters.put("psId", "INLPG");
		SessionVo session = new SessionVo("AAA", 1, "test", "test", USER_TYPE_CD.Operator, 0, 0);
		SelectPsValueMinMaxDto item = handler.convert(session, parameters, SelectPsValueMinMaxDto.class, true);
		System.out.println(handler.selectPsValueMinMaxHourlyList(session, item));
	}

	private final PsHandlerQid QID = new PsHandlerQid();

	@MethodDescr(name = "수집값 등록", description = "외부에서 수집 데이터를 등록한다.")
	public Object addValue(SessionVo session, ValueAddDto data) throws Exception {

		PsVoRawList voList = new PsVoRawList("ui", System.currentTimeMillis());

		if (voList.add(data.getMoNo(), data.getPsId(), data.getValue()) == null) {
			throw new Exception("parameters is not enough : " + FxmsUtil.toJson(data));
		}

		ValueApi.getApi().addValue(voList, true);

		return data;
	}

	/**
	 * 
	 * 
	 * @param session
	 * @param para
	 * @return
	 * @throws Exception
	 */
	@MethodDescr(name = "실시간 조회", description = "실시간으로 값을 수집한다.")
	public Object getPsValueRtList(SessionVo session, SelectPsValueRtListPara para) throws Exception {

		MoService service = ServiceApi.getApi().getService(MoService.class);

		return service.getRtValues(para.getMoNo());
	}

	/**
	 * 
	 * @param session
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@MethodDescr(name = "관리대상수집데이터조회", description = "관리대상이 수집한 내용을 보여준다.")
	public Object getValues(SessionVo session, GetValuesDto dto) throws Exception {
		PsItem psItem = PsApi.getApi().getPsItem(dto.getPsId());
		return ValueApi.getApi().getValues(dto.getMoNo(), dto.getPsId(), dto.getPsKindName(), psItem.getDefKindCol(),
				dto.getStartDate(), dto.getEndDate());
	}

	@MethodDescr(name = "수집항목조회", description = "처리되고 있는 수집 항목을 조회한다.")
	public Object selectPsItemList(SessionVo session) throws Exception {
		return ClassDaoEx.SelectDatas(FX_PS_ITEM.class, FxApi.makePara("useYn", "Y"), PsItemVo.class);
	}

	@MethodDescr(name = "성능통계종류조회", description = "수집한 데이터에 대한 통계를 생성할 수 있는 종류를 조회한다.")
	public Object selectPsKindList(SessionVo session) throws Exception {
		return ClassDaoEx.SelectDatas(FX_PS_STAT_KIND.class, null, FX_PS_STAT_KIND.class);
	}

	@MethodDescr(name = "수집값조회", description = "수집값을 조회한다.")
	public Object selectPsValueList(SessionVo session, GetValuesDto obj) throws Exception {
		PsKind psKind = PsApi.getApi().getPsKind(obj.getPsKindName());
		psKind.checkDateRange(obj.getStartDate(), obj.getEndDate());
		return ValueApi.getApi().getValues(obj.getMoNo(), obj.getPsId(), obj.getPsKindName(), obj.getStartDate(),
				obj.getEndDate());
	}

	@MethodDescr(name = "1분단위 최소,최대,평균조회", description = "분단위 최소, 최대, 평균 조회")
	public Object selectPsValueMinMaxAvgList(SessionVo session, SelectPsValueMinMaxDto data) throws Exception {

		PsItem psItem = PsApi.getApi().getPsItem(data.getPsId());
		PsKind psKind = PsApi.getApi().getPsKind(data.getPsKindName());

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", data.getMoNo());
		para.put("psItem", psItem.makeColumn(psKind, null).getName());
		para.put("tableName", psKind.getTableName(psItem, DateUtil.getHstime(data.getPsDate())));

		return selectListQid(QID.select_ps_value_min_max_avg_list, para);

	}

	@MethodDescr(name = "시간별 최소,최대,평균조회", description = "시간단위 최소, 최대, 평균 조회")
	public Object selectPsValueMinMaxHourlyList(SessionVo session, SelectPsValueMinMaxDto data) throws Exception {

		PsItem psItem = PsApi.getApi().getPsItem(data.getPsId());
		PsKind psKind = PsApi.getApi().getPsKind(data.getPsKindName());

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", data.getMoNo());
		para.put("psItem", psItem.makeColumn(psKind, null).getName());
		para.put("tableName", psKind.getTableName(psItem, DateUtil.getHstime(data.getPsDate())));

		return selectListQid(QID.select_ps_value_min_max_hourly_list, para);

	}

	@MethodDescr(name = "관리대상 가동율 조회", description = "관리대상의 가동시간, 가동율을 조회한다.")
	public Object getOperatingRate(SessionVo session, GetOperatingRateDto dto) throws Exception {
		return new GetOperatingRateDfo().getOperatingRate(dto);
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(AlarmHandlerQid.QUERY_XML_FILE));
	}
}

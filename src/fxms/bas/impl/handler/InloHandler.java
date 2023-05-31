package fxms.bas.impl.handler;

import java.util.List;
import java.util.Map;

import fxms.bas.api.InloApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.InloHandlerQid;
import fxms.bas.impl.dao.UserHandlerQid;
import fxms.bas.impl.dbo.all.FX_CF_INLO;
import fxms.bas.impl.dpo.inlo.MakeInloTreeDfo;
import fxms.bas.impl.dpo.inlo.SelectInloListDfo;
import fxms.bas.impl.handler.dto.inlo.InloAddDto;
import fxms.bas.impl.handler.dto.inlo.InloClCdDto;
import fxms.bas.impl.handler.dto.inlo.InloDeleteDto;
import fxms.bas.impl.handler.dto.inlo.InloSelectStateDto;
import fxms.bas.impl.handler.dto.inlo.InloUpdateDto;
import fxms.bas.vo.Inlo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 설치위치 관련 HANDLER
 * 
 * @author subkjh
 *
 */
public class InloHandler extends BaseHandler {

	public final InloHandlerQid QID = new InloHandlerQid();

	@MethodDescr(name = "설치위치 삭제", description = "설치위치를 삭제한다.")
	public Object deleteInlo(SessionVo session, InloDeleteDto inlo) throws Exception {
		return InloApi.getApi().removeInlo(inlo.getInloNo(), inlo.getInloName());
	}

	@MethodDescr(name = "설치위치 트리조회", description = "설치위치 트리를 조회한다.")
	public Object getInloTree(SessionVo session) throws Exception {
		Map<String, Object> para = makePara4Ownership(session, null);
		List<Inlo> allList = new SelectInloListDfo().selectInlo(para); // 설치위치
		return new MakeInloTreeDfo().makeTree(allList); // 설치위치 계층화
	}

	@MethodDescr(name = "설치위치 추가", description = "설치위치를 추가한다.")
	public Object insertInlo(SessionVo session, InloAddDto data, Map<String, Object> etcData) throws Exception {
		Map<String, Object> datas = ObjectUtil.toMap(data);
		if (etcData != null) {
			datas.putAll(etcData);
		}
		int inloNo = InloApi.getApi().addInlo(session.getUserNo(), datas);
		return InloApi.getApi().getInlo(inloNo);
	}

	@MethodDescr(name = "설치위치 알람상태조회", description = "설치위치에 포함된 관리대상의 알람 상태를 조회한다.")
	public Object selectInloAlarmStateList(SessionVo session, InloSelectStateDto dto) throws Exception {

		Map<String, Object> para = ObjectUtil.toMap(dto);
		para.put("inloNo", session.getInloNo());

		return selectListQid(QID.select_inlo_alarm_state_list, para);

	}

	@MethodDescr(name = "설치위치 목록조회", description = "설치위치 목록을 조회한다.")
	public Object selectInloGridList(SessionVo session, InloClCdDto item, Map<String, Object> parameters)
			throws Exception {

		Map<String, Object> wherePara = makePara4Ownership(session, parameters);
		wherePara.put("inloClCd", item.getInloClCd());

		return ClassDaoEx.SelectDatas(FX_CF_INLO.class, parameters, FX_CF_INLO.class);
	}

	@MethodDescr(name = "설치위치 온라인상태조회", description = "설치위치에 포함된 관리대상의 온라인 상태를 조회한다.")
	public Object selectInloOnlineStateList(SessionVo session, InloSelectStateDto dto) throws Exception {
		Map<String, Object> para = ObjectUtil.toMap(dto);
		para.put("inloNo", session.getInloNo());
		return selectListQid(QID.select_inlo_online_state_list, para);
	}

	@MethodDescr(name = "설치위치 수정", description = "설치위치를 수정한다.")
	public Object updateInlo(SessionVo session, InloUpdateDto data, Map<String, Object> etcData) throws Exception {
		Map<String, Object> datas = ObjectUtil.toMap(data);
		if (etcData != null) {
			datas.putAll(etcData);
		}
		InloApi.getApi().updateInlo(session.getUserNo(), data.getInloNo(), datas);
		return InloApi.getApi().getInlo(data.getInloNo());
	}
	
	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(InloHandlerQid.QUERY_XML_FILE));
	}

}

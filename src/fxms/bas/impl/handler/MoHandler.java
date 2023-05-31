package fxms.bas.impl.handler;

import java.util.Map;

import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.InloHandlerQid;
import fxms.bas.impl.dao.MoHandlerQid;
import fxms.bas.impl.dpo.mo.GetMoTreeDpo;
import fxms.bas.impl.handler.dto.mo.MoDto;
import fxms.bas.impl.handler.dto.mo.MoSearchCountDto;
import fxms.bas.impl.handler.dto.mo.MoSearchStateDto;
import fxms.bas.impl.handler.dto.mo.MoSetupDto;
import fxms.bas.impl.handler.dto.mo.MoTestDto;
import fxms.bas.mo.FxMo;
import fxms.bas.vo.SyncMo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 관리대상 관련 핸들러
 * 
 * @author subkjh
 *
 */
public class MoHandler extends BaseHandler {

	private final MoHandlerQid QID = new MoHandlerQid();

	@MethodDescr(name = "관리대상 삭제", description = "등록된 관리대상을 삭제한다.")
	public Object deleteMo(SessionVo session, MoDto data) throws Exception {
		long moNo = data.getMoNo();
		return MoApi.getApi().deleteMo(session.getUserNo(), moNo, "UI", true);
	}

	@MethodDescr(name = "관리대상트리", description = "관리대상 트리를 조회한다.")
	public Object getMoTree(SessionVo session, MoSearchCountDto item) throws Exception {
		Map<String, Object> para = makePara4Ownership(session, ObjectUtil.toMap(item));
		return new GetMoTreeDpo().getTree(para);
	}

	@MethodDescr(name = "관리대상 추가", description = "관리대상을 추가한다.")
	public Object insertMo(SessionVo session, Map<String, Object> datas) throws Exception {
		Object val = datas.get("moClass");
		String moClass = val != null ? val.toString() : FxMo.MO_CLASS;
		return MoApi.getApi().addMo(session.getUserNo(), moClass, datas, "UI", true);
	}

	@MethodDescr(name = "관리대상 알람상태 조회", description = "관리대상의 알람 발생 여부를 조회한다.")
	public Object selectMoAlarmStateList(SessionVo session, MoSearchStateDto data) throws Exception {
		Map<String, Object> para = ObjectUtil.toMap(data);
		para.put("userNo", session.getUserNo());

		return selectListQid(QID.select_mo_alarm_state_list, para);
	}

	@MethodDescr(name = "관리대상건수조회", description = "관리대상의 수를 조회한다.")
	public Object selectMoCountGridList(SessionVo session, MoSearchCountDto item) throws Exception {
		return selectListQid(QID.select_mo_count_grid_list, item);
	}

	@MethodDescr(name = "관리대상 목록 조회", description = "관리대상의 목록을 조회한다.")
	public Object selectMoList(SessionVo session, Map<String, Object> parameters) throws Exception {
		return MoApi.getApi().getMoList(parameters);
	}

	@MethodDescr(name = "관리대상 온라인상태 조회", description = "관리대상의 온라인 상태를 조회한다.")
	public Object selectMoOnlineStateList(SessionVo session, MoSearchStateDto data) throws Exception {

		Map<String, Object> para = ObjectUtil.toMap(data);
		para.put("userNo", session.getUserNo());

		return selectListQid(QID.select_mo_online_state_list, para);
	}

	@MethodDescr(name = "관리대상 제어", description = "관리대상의 설정 명령을 내린다.")
	public Object setupMo(SessionVo session, MoSetupDto data, Map<String, Object> parameters) throws Exception {

		MoApi.getApi().setupMo(data.getMoNo(), data.getMethod(), parameters);

		return parameters;
	}

	@MethodDescr(name = "관리대상 확인", description = "관리대상에 통신하여 상태를 확인한다.")
	public Object testMo(SessionVo session, MoTestDto data) throws Exception {

		SyncMo syncMo = MoApi.getApi().sync(data.getMoNo(), data.getRealtime(), true);
		return syncMo.toMap();

	}

	@MethodDescr(name = "관리대상 수정", description = "등록된 관리대상을 수정한다.")
	public Object updateMo(SessionVo session, MoDto mo, Map<String, Object> datas) throws Exception {
		long moNo = mo.getMoNo();
		return MoApi.getApi().updateMo(session.getUserNo(), moNo, datas, false);
	}
	
	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(MoHandlerQid.QUERY_XML_FILE));
	}
}

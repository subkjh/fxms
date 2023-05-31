package fxms.bas.impl.handler;

import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.AlarmCfgHandlerQid;
import fxms.bas.impl.dbo.all.FX_AL_CD;
import fxms.bas.impl.dbo.all.FX_AL_CFG;
import fxms.bas.impl.dbo.all.FX_AL_CFG_MEM;
import fxms.bas.impl.dpo.ao.AlCfgCopyDfo;
import fxms.bas.impl.dpo.ao.AlCfgDeleteDfo;
import fxms.bas.impl.dpo.ao.AlCfgMemInsertDfo;
import fxms.bas.impl.dpo.ao.AlCfgMemUpdateDfo;
import fxms.bas.impl.dpo.ao.AlCfgSelectDfo;
import fxms.bas.impl.dpo.ao.AlCfgUpdateDfo;
import fxms.bas.impl.dpo.ao.AlcdSelectDfo;
import fxms.bas.impl.dto.AlCfgAddDto;
import fxms.bas.impl.dto.AlCfgApplyDto;
import fxms.bas.impl.dto.AlCfgCopyDto;
import fxms.bas.impl.dto.AlCfgDeleteDto;
import fxms.bas.impl.dto.AlCfgDto;
import fxms.bas.impl.dto.AlCfgMemAddDto;
import fxms.bas.impl.dto.AlCfgMemDto;
import fxms.bas.impl.dto.AlCfgSelectDto;
import fxms.bas.impl.handler.dto.mo.MoDto;
import fxms.bas.impl.vo.AlarmCodeVo;
import fxms.bas.mo.FxMo;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.BasCfg;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 알람조건 핸들러
 * 
 * @author subkjh
 *
 */
public class AlarmCfgHandler extends BaseHandler {

	public static void main(String[] args) {

	}

	private AlarmCfgHandlerQid QID = new AlarmCfgHandlerQid();

	@MethodDescr(name = "알람조건 사용여부 설정", description = "알람조건에 대한 사용 여부를 설정한다.")
	public Object applyAlarmCfg(SessionVo session, AlCfgApplyDto data) throws Exception {
		ClassDaoEx.open().updateOfClass(FX_AL_CFG.class, data).close();
		return data;
	}

	@MethodDescr(name = "알람조건 복사", description = "알람조건을 복사한다.")
	public Object copyAlarmCfg(SessionVo session, AlCfgCopyDto data) throws Exception {
		int alarmCfgNo = new AlCfgCopyDfo().copyAlarmCfg(data);
		return alarmCfgNo;
	}

	@MethodDescr(name = "알람조건 삭제", description = "알람조건을 삭제한다.")
	public Object deleteAlarmCfg(SessionVo session, AlCfgDeleteDto data) throws Exception {
		return new AlCfgDeleteDfo().delete(data);
	}

	@MethodDescr(name = "알람조건 상세조건 삭제", description = "알람조건의 상세조건을 삭제한다.")
	public Object deleteAlarmCfgMem(SessionVo session, AlCfgMemDto item) throws Exception {
		delete(FX_AL_CFG_MEM.class, ObjectUtil.toMap(item), null);
		broadcast(new ReloadSignal(ReloadType.AlarmCfg));
		return item;
	}

	@MethodDescr(name = "알람조건 추가", description = "알람조건을 추가한다.")
	public Object insertAlarmCfg(SessionVo session, AlCfgAddDto dto) throws Exception {

		FX_AL_CFG cfg = ObjectUtil.toObject(dto, FX_AL_CFG.class);
		FxTableMaker.initRegChg(0, cfg);
		if (cfg.getMoClass() == null) {
			cfg.setMoClass(FxMo.MO_CLASS);
		}
		if (cfg.getInloNo() <= 0) {
			cfg.setInloNo(session.getInloNo());
		}

		int alarmCfgNo = ClassDaoEx.getNextVal(FX_AL_CFG.FX_SEQ_ALARMCFGNO, Integer.class);
		cfg.setAlarmCfgNo(alarmCfgNo);
		ClassDaoEx.InsertOfClass(FX_AL_CFG.class, cfg);

		return cfg;
	}

	@MethodDescr(name = "알람상세조건 추가", description = "알람상세조건을 추가한다.")
	public Object insertAlarmCfgMem(SessionVo session, AlCfgMemAddDto dto) throws Exception {

		new AlCfgSelectDfo().selectAlCfg(dto.getAlarmCfgNo()); // 알람조건 확인

		new AlcdSelectDfo().selectAlarmCode(dto.getAlarmCfgNo()); // 알람코드 확인

		FX_AL_CFG_MEM ret = new AlCfgMemInsertDfo().insert(dto); //

		broadcast(new ReloadSignal(ReloadType.AlarmCfg));

		return ret;
	}

	@MethodDescr(name = "알람조건목록 조회", description = "알람조건의 목록을 조회한다.")
	public Object selectAlarmCfgGridList(SessionVo session, AlCfgSelectDto dto, Map<String, Object> datas)
			throws Exception {
		datas.put("userNo", session.getUserNo());
		return selectListQid(QID.select_alarm_cfg_grid_list, datas);
	}

	@MethodDescr(name = "알람조건목록 조회", description = "알람조건의 목록을 조회한다.")
	public Object selectAlarmCfgList(SessionVo session, AlCfgSelectDto para) throws Exception {
		return selectListQid(QID.select_alarm_cfg_simple_list, makeWherePara(para));
	}

	@MethodDescr(name = "알람상세조건 조회", description = "하나의 알람조건의 상세 내역을 조회한다.")
	public Object selectAlarmCfgMemList(SessionVo session, AlCfgDto dto) throws Exception {
		return ClassDaoEx.SelectDatas(FX_AL_CFG_MEM.class, dto, FX_AL_CFG_MEM.class);
	}

	@MethodDescr(name = "관리대상 알람상세조건 조회", description = "관리대상에 설정된 알람상세조건을 조회한다.")
	public Object selectAlarmCfgMemListForMono(SessionVo session, MoDto para) throws Exception {
		return selectListQid(QID.select_alarm_cfg_mem_list_for_mono, para);
	}

	@MethodDescr(name = "알람코드목록 조회", description = "알람코드 목록을 조회한다.")
	public Object selectAlcdList(SessionVo session, Map<String, Object> parameters) throws Exception {
		parameters.put("useYn", "Y");
		return ClassDaoEx.SelectDatas(FX_AL_CD.class, parameters, AlarmCodeVo.class);
	}

	@MethodDescr(name = "알람조건 수정", description = "알람조건의 기본 정보를 수정한다.")
	public Object updateAlarmCfg(SessionVo session, AlCfgDto dto, Map<String, Object> datas) throws Exception {
		return new AlCfgUpdateDfo().udpate(dto, datas);
	}

	@MethodDescr(name = "알람상세조건 수정", description = "알람상세 조건을 수정한다.")
	public Object updateAlarmCfgMem(SessionVo session, AlCfgMemDto dto, Map<String, Object> datas) throws Exception {
		return new AlCfgMemUpdateDfo().update(dto, datas);
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(AlarmCfgHandlerQid.QUERY_XML_FILE));
	}
}

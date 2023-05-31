package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.all.FX_CO_CD;
import fxms.bas.impl.dpo.co.CdCodeDeleteDfo;
import fxms.bas.impl.dpo.co.CdCodeInsertDfo;
import fxms.bas.impl.dpo.co.CdCodeUpdateDfo;
import fxms.bas.impl.dto.CdClassDto;
import fxms.bas.impl.dto.CdCodeAddDto;
import fxms.bas.impl.dto.CdCodeDto;
import fxms.bas.impl.vo.CodeVo;
import fxms.bas.signal.ReloadSignal;
import fxms.bas.signal.ReloadSignal.ReloadType;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 코드 관련
 * 
 * @author subkjh
 *
 */
public class CodeHandler extends BaseHandler {

	public static void main(String[] args) throws Exception {
		CodeHandler handler = new CodeHandler();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cdClass", "MO_CLASS");
		SessionVo session = new SessionVo("AAA", 1, "test", "test", USER_TYPE_CD.Operator, 0, 0);
		CdClassDto item = handler.convert(session, parameters, CdClassDto.class, true);
		handler.selectCodeList(session, item);
	}

	@MethodDescr(name = "코드 삭제", description = "등록된 코드를 삭제한다.")
	public Object deleteCode(SessionVo session, CdCodeDto dto) throws Exception {
		boolean ret = new CdCodeDeleteDfo().delete(dto);
		if (ret)
			broadcast(new ReloadSignal(ReloadType.Code));
		return ret;
	}

	@MethodDescr(name = "코드 추가", description = "코드를 추가한다.")
	public Object insertCode(SessionVo session, CdCodeAddDto dto) throws Exception {
		boolean ret = new CdCodeInsertDfo().insert(dto);
		if (ret) {
			broadcast(new ReloadSignal(ReloadType.Code));
		}
		return ret;
	}

	@MethodDescr(name = "코드 조회", description = "코드 목록을 조회한다.")
	public Object selectCodeList(SessionVo session, CdClassDto dto) throws Exception {
		return ClassDaoEx.SelectDatas(FX_CO_CD.class, dto, CodeVo.class);
	}

	@MethodDescr(name = "코드 수정", description = "코드를 수정한다.")
	public Object updateCode(SessionVo session, CdCodeDto dto, Map<String, Object> data) throws Exception {

		boolean ret = new CdCodeUpdateDfo().udpate(dto, data);
		if (ret) {
			broadcast(new ReloadSignal(ReloadType.Code));
		}

		return ret;
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createQidDao();
	}
}

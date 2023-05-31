package fxms.bas.impl.handler;

import java.util.Map;

import fxms.bas.api.UserApi;
import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.MethodDescr;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dao.UserHandlerQid;
import fxms.bas.impl.dbo.all.FX_UR_UGRP;
import fxms.bas.impl.dpo.user.UgrpAddDfo;
import fxms.bas.impl.dpo.user.UgrpDeleteDfo;
import fxms.bas.impl.dpo.user.UgrpOpSetDpo;
import fxms.bas.impl.dpo.user.UgrpUpdateDfo;
import fxms.bas.impl.dpo.user.UserApplyNewDfo;
import fxms.bas.impl.dpo.user.UserConfirmDpo;
import fxms.bas.impl.dpo.user.UserDisableDfo;
import fxms.bas.impl.dpo.user.UserPwdChangeDfo;
import fxms.bas.impl.dpo.user.UserPwdResetDpo;
import fxms.bas.impl.dpo.user.UserSelectDfo;
import fxms.bas.impl.dpo.user.UserUpdateDfo;
import fxms.bas.impl.dto.AddUserGroupDto;
import fxms.bas.impl.dto.ApplyNewReqDto;
import fxms.bas.impl.dto.ConfirmUserDto;
import fxms.bas.impl.dto.SelectUserDto;
import fxms.bas.impl.dto.UgrpDto;
import fxms.bas.impl.dto.UgrpOpDto;
import fxms.bas.impl.dto.UserAccessHstSelectDto;
import fxms.bas.impl.dto.UserChangePwdDto;
import fxms.bas.impl.dto.UserDto;
import fxms.bas.impl.dto.UserNewApplySelectDto;
import fxms.bas.impl.dto.UserWorkHstSelectDto;
import fxms.bas.impl.vo.UserGroupVo;
import subkjh.bas.BasCfg;
import subkjh.bas.co.user.User;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;

/**
 * 사용자 관리
 * 
 * @author subkjh
 *
 */
public class UserHandler extends BaseHandler {

	private UserHandlerQid QID = new UserHandlerQid();

	@MethodDescr(name = "사용자그룹추가", description = "사용자그룹을 등록한다.")
	public Object addUserGroup(SessionVo session, AddUserGroupDto dto, Map<String, Object> datas) throws Exception {
		return new UgrpAddDfo().addUgrp(dto.getUgrpName(), datas);
	}

	@MethodDescr(name = "가입신청", description = "사용할 수 있도록 가입신청을 한다.")
	public Object applyNewUser(SessionVo session, ApplyNewReqDto dto) throws Exception {
		return new UserApplyNewDfo().applyNew(dto, null);
	}

	@MethodDescr(name = "사용자 암호변경", description = "사용자의 암호를 변경한다.")
	public Object changeMyUserPwd(SessionVo session, UserChangePwdDto obj) throws Exception {
		User user = new UserSelectDfo().select(session.getUserNo(), session.getUserId());
		return new UserPwdChangeDfo().changePwd(user, obj.getNewUserPwd(), obj.getNewUserPwdAgain());
	}

	@MethodDescr(name = "가입 승인/거절", description = "신규 운용자가 요청한 내용을 승인/거절을 처리한다.")
	public Object confirmNewUser(SessionVo session, ConfirmUserDto dto) throws Exception {
		return new UserConfirmDpo().run(null, dto);
	}

	@MethodDescr(name = "사용자그룹삭제", description = "사용자그룹을 삭제한다.")
	public Object deleteUserGroup(SessionVo session, UgrpDto dto) throws Exception {
		return new UgrpDeleteDfo().delete(dto.getUgrpNo(), dto.getUgrpName());
	}

	@MethodDescr(name = "회원 계정 정지", description = "회원 계정을 정지한다.")
	public Object disableUser(SessionVo session, UserDto dto) throws Exception {
		return new UserDisableDfo().disable(dto.getUserNo(), dto.getUserId());
	}

	@MethodDescr(name = "로그아웃", description = "로그아웃한다.")
	public Object logout(SessionVo session) throws Exception {
		UserApi.getApi().logout(session.getSessionId(), ACCS_ST_CD.LOGOUT_OK);
		return session.getSessionId();
	}

	@MethodDescr(name = "회원 암호 리셋", description = "회원의 암호를 리셋하여 메일로 전달한다.")
	public Object resetUserPwd(SessionVo session, UserDto dto) throws Exception {
		return new UserPwdResetDpo().resetUserPwd(dto.getUserNo(), dto.getUserId());
	}

	@MethodDescr(name = "회원 접속이력 조회", description = "입력한 기간동안 회원이 접속한 이력을 조회한다.")
	public Object selectUserAccessHstGridList(SessionVo session, UserAccessHstSelectDto dto) throws Exception {
		Map<String, Object> wherePara = makeWherePara(dto);
		wherePara.put("userNo", session.getUserNo());
		return selectListQid(QID.select_user_access_hst_grid_list, makePara4Ownership(session, wherePara));
	}

	@MethodDescr(name = "사용자 그리드용 목록 조회", description = "사용자 목록을 조회한다.")
	public Object selectUserGridList(SessionVo session, SelectUserDto dto) throws Exception {

		Map<String, Object> para = ObjectUtil.toMap(dto);
		para.put("userNo", session.getUserNo());

		return selectListQid(QID.select_user_grid_list, para);
	}

	@MethodDescr(name = "사용자그룹목록조회", description = "사용자그룹 목록을 조회한다.")
	public Object selectUserGroupGridList(SessionVo session, Map<String, Object> parameters) throws Exception {
		// 담당 관리대상만 카운트함.
		parameters.put("userNo", session.getUserNo());
		return selectListQid(QID.select_user_group_grid_list, parameters);
	}

	@MethodDescr(name = "사용자그룹목록조회", description = "사용자그룹 목록을 조회한다.")
	public Object selectUserGroupList(SessionVo session, Map<String, Object> para) throws Exception {
		// 화면에 표시 가능한 내용만 조회한다.
		para.put("uiDispYn", "Y");
		return ClassDaoEx.SelectDatas(FX_UR_UGRP.class, makePara4Ownership(session, para), UserGroupVo.class);
	}

	@MethodDescr(name = "사용자그룹기능목록조회", description = "사용자 그룹의 기능(메뉴) 목록을 조회한다.")
	public Object selectUserGroupOpGridList(SessionVo session, UgrpDto dto) throws Exception {
		return selectListQid(QID.select_user_group_op_grid_list, makePara4Ownership(session, ObjectUtil.toMap(dto)));
	}

	@MethodDescr(name = "로그인사용자정보조회", description = "현재 로그인된 사용자(자신)의 정보를 조회한다.")
	public Object selectUserInfo(SessionVo session) throws Exception {
		return new UserSelectDfo().select(session.getUserNo(), session.getUserId());
	}

	@MethodDescr(name = "신규가입이력조회", description = "신규 가입 신청한 목록을 조회한다.")
	public Object selectUserNewApplyGridList(SessionVo session, UserNewApplySelectDto dto, Map<String, Object> datas)
			throws Exception {

		Map<String, Object> wherePara = ObjectUtil.toMap(dto);
		if (datas != null) {
			wherePara.putAll(datas);
		}

		if (session.getUserTypeCd() == USER_TYPE_CD.SuperAdmin) {
			return selectListQid(QID.select_user_new_apply_grid_list, wherePara);
		} else {
			return selectListQid(QID.select_user_new_apply_grid_list, makePara4Ownership(session, wherePara));
		}
	}

	@MethodDescr(name = "사용자작업이력조회", description = "사용자 작업 이력을 조회한다.")
	public Object selectUserWorkHstGridList(SessionVo session, UserWorkHstSelectDto item, Map<String, Object> datas)
			throws Exception {

		Map<String, Object> wherePara = makeWherePara(item);
		if (datas != null) {
			wherePara.putAll(datas);
		}
		if (wherePara.get("userNo") == null) {
			wherePara.put("userNo", session.getUserNo());
		}

		return selectListQid(QID.select_user_work_hst_grid_list, makePara4Ownership(session, wherePara));
	}

	@MethodDescr(name = "사용자그룹기능설정", description = "사용자그룹이 갖는 권한을 설정한다.")
	public Object setUserGroupOp(SessionVo session, UgrpOpDto ugrp) throws Exception {
		return new UgrpOpSetDpo().setOp(ugrp.getUgrpNo(), ugrp.getOpId(), "y".equalsIgnoreCase(ugrp.getUseYn()));
	}

	@MethodDescr(name = "사용자그룹 정보 수정", description = "사용자그룹의 정보를 수정한다.")
	public Object updateUserGroup(SessionVo session, UgrpDto ugrp, Map<String, Object> datas) throws Exception {
		return new UgrpUpdateDfo().updateUgrp(ugrp, datas);
	}

	@MethodDescr(name = "사용자 정보 수정", description = "사용자의 정보를 수정한다.")
	public Object updateUserInfo(SessionVo session, UserDto user, Map<String, Object> datas) throws Exception {
		return new UserUpdateDfo().updateUser(user, datas);
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG)
				.createQidDao(BasCfg.getHome(UserHandlerQid.QUERY_XML_FILE));
	}
}

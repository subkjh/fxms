package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.co.CoCode.NEW_USER_REG_ST_CD;
import fxms.bas.exp.AttrNotFoundException;
import fxms.bas.exp.FxOkException;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dbo.all.FX_UR_USER_NEW_REQ;
import fxms.bas.impl.dpo.FxDpo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.ConfirmUserDto;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.bas.co.user.exception.UserNotFoundException;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

/**
 * 신청 계정 처리
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserConfirmDpo implements FxDpo<ConfirmUserDto, Integer> {

	public static void main(String[] args) throws AttrNotFoundException, Exception {
		UserConfirmDpo dfo = new UserConfirmDpo();
		ConfirmUserDto dto = new ConfirmUserDto();

		Map<String, Object> datas = FxApi.makePara("applyUserMail", "hong@test.com", "newUserRegStCd",
				NEW_USER_REG_ST_CD.Approval.getCode(), "ugrpNo", 1);
		datas = FxAttrApi.toObject(datas, dto);
		System.out.println(dfo.run(null, dto));
	}

	@Override
	public Integer run(FxFact fact, ConfirmUserDto data) throws Exception {

		// 해당 ID로 운영자가 있는지 확인
		try {
			new UserSelectDfo().call(fact, data.getApplyUserMail());

			FX_UR_USER_NEW_REQ req = makeReq(data);
			req.setNewUserRegStCd(NEW_USER_REG_ST_CD.Reject.getCode());
			req.setProcRsn(Lang.get("This email address is already in use."));
			ClassDaoEx.open().updateOfClass(FX_UR_USER_NEW_REQ.class, req).close();

			throw new FxOkException(Lang.get("This email address is already in use.", data.getApplyUserMail()));

		} catch (UserNotFoundException e) {

		}

		// 회원가입 요청내역 업데이트용 데이터
		FX_UR_USER_NEW_REQ req = makeReq(data);

		if (req != null && req.getNewUserRegStCd().equals(NEW_USER_REG_ST_CD.Approval.getCode())) {

			// 승인이면 요청내용 업데이트 및 운영자 등록

			FX_UR_USER user = makeUser(req);

			new UserInitNewDfo().initUser(user);

			ClassDaoEx.open().updateOfClass(FX_UR_USER_NEW_REQ.class, req) //
					.insertOfClass(FX_UR_USER.class, user)//
					.close();

			return user.getUserNo();

		} else {

			// 미승인이면 미승인 내역 업데이트

			ClassDaoEx.open().updateOfClass(FX_UR_USER_NEW_REQ.class, req).close();
			return -1;

		}

	}

	private FX_UR_USER_NEW_REQ makeReq(ConfirmUserDto dto) throws Exception {

		FX_UR_USER_NEW_REQ data = ClassDaoEx.SelectData(FX_UR_USER_NEW_REQ.class, FxApi.makePara("applyUserId",
				dto.getApplyUserMail(), "newUserRegStCd", NEW_USER_REG_ST_CD.Init.getCode()));

		if (data == null) {
			throw new Exception(Lang.get("There is no record of membership registration.", dto.getApplyUserMail()));
		}

		ObjectUtil.toObject(ObjectUtil.toMap(dto), data);
		FxTableMaker.initRegChg(0, data);
		data.setProcDtm(DateUtil.getDtm());

		return data;
	}

	private FX_UR_USER makeUser(FX_UR_USER_NEW_REQ data) {
		FX_UR_USER user = new FX_UR_USER();
		user.setAccsNetmask(data.getAccsNetmask());
		user.setAccsNetwork(data.getAccsNetwork());
		user.setInloNo(data.getApplyInloNo());
		user.setUserId(data.getApplyUserId());
		user.setUserMail(data.getApplyUserMail());
		user.setUserName(data.getApplyUserName());
		user.setUserPwd(data.getApplyUserPwd());
		user.setUserTelNo(data.getApplyTelNo());
		user.setUserTypeCd(USER_TYPE_CD.User.getCode());
		user.setUseYn(true);
		user.setUseStrtDate(data.getUseStrtDate());
		user.setUseEndDate(data.getUseEndDate());
		user.setAuthority("ROLE_USER");
		return user;
	}
}

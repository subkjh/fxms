package fxms.bas.impl.dpo.user;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.co.CoCode.NEW_USER_REG_ST_CD;
import fxms.bas.exp.AttrNotFoundException;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dbo.all.FX_UR_USER_NEW_REQ;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.ApplyNewReqDto;
import subkjh.bas.co.lang.Lang;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.util.FxTableMaker;

/**
 * 계정 생성 요청
 * 
 * @author SUBKJH-DEV
 *
 */
public class UserApplyNewDfo implements FxDfo<ApplyNewReqDto, Boolean> {

	public static void main(String[] args) throws AttrNotFoundException, Exception {
		UserApplyNewDfo dfo = new UserApplyNewDfo();
		ApplyNewReqDto dto = new ApplyNewReqDto();

		Map<String, Object> datas = FxApi.makePara("applyUserName", "test7678", "applyUserMail", "hong206@test.com",
				"applyUserPwd", "1234567");
		datas = FxAttrApi.toObject(datas, dto);

		System.out.println(FxmsUtil.toJson(dto));

		System.out.println(dfo.call(new FxFact(), dto));

	}

	@Override
	public Boolean call(FxFact fact, ApplyNewReqDto dto) throws Exception {
		Map<String, Object> datas = fact.getMap("datas");
		return applyNew(dto, datas);
	}

	public boolean applyNew(ApplyNewReqDto dto, Map<String, Object> datas) throws Exception {

		if (datas == null) {
			datas = new HashMap<>();
		}

		datas.putAll(ObjectUtil.toMap(dto));

		FX_UR_USER_NEW_REQ req = new FX_UR_USER_NEW_REQ();
		ObjectUtil.toObject(datas, req);

		req.setApplyUserId(req.getApplyUserMail());

		if (req.getApplyUserPwd() == null) {
			req.setApplyUserPwd("a");
		}

		if (req.getApplyTelNo() == null) {
			req.setApplyTelNo("0000-0000-0000");
		}

		req.setApplyDtm(DateUtil.getDtm());
		req.setApplyUserPwd(User.encodingPassword(req.getApplyUserPwd()));
		req.setNewUserRegStCd(NEW_USER_REG_ST_CD.Init.getCode());
		FxTableMaker.initRegChg(0, req);

		FX_UR_USER user = ClassDaoEx.SelectData(FX_UR_USER.class, FxApi.makePara("userId", req.getApplyUserId()));
		if (user != null) {
			throw new Exception(Lang.get("The account you applied for is already in use.", req.getApplyUserId()));
		}

		ClassDaoEx.open().insertOfClass(FX_UR_USER_NEW_REQ.class, req).close();
		
		return true;

	}
}

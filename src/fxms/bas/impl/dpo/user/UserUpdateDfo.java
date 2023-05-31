package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_UR_USER;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.UserDto;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 사용자 정보 수정
 * 
 * @author subkjh
 *
 */
public class UserUpdateDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) {
		UserUpdateDfo dfo = new UserUpdateDfo();
		try {
			Map<String, Object> datas = FxApi.makePara("userNo", 2, "userId", "subkjh", "userName", "SUBKJH");
			dfo.call(null, datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> datas) throws Exception {
		UserDto dto = ObjectUtil.toObject(datas, UserDto.class);
		return updateUser(dto, datas);
	}

	public Boolean updateUser(UserDto user, Map<String, Object> datas) throws Exception {
		datas.remove("userPwd"); // 사용자 암호는 여기서 변경할 수 없다.
		ClassDaoEx.open().setOfClass(FX_UR_USER.class, user, datas).close();
		return true;
	}
}
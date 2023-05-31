package fxms.bas.impl.dpo.user;

import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.impl.dbo.all.FX_UR_UGRP;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dto.UgrpDto;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 사용자 그룹 정보 수정
 * 
 * @author subkjh
 *
 */
public class UgrpUpdateDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) {
		UgrpUpdateDfo dfo = new UgrpUpdateDfo();
		try {
			Map<String, Object> datas = FxApi.makePara("ugrpNo", 1000, "ugrpName", "테스트그룹22", "ugrpDesc", "테스트용 사용자그룹");
			dfo.call(null, datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> datas) throws Exception {
		UgrpDto dto = ObjectUtil.toObject(datas, UgrpDto.class);
		return updateUgrp(dto, datas);
	}

	/**
	 * 
	 * @param ugrp
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	public Boolean updateUgrp(UgrpDto ugrp, Map<String, Object> datas) throws Exception {
		ClassDaoEx.open().setOfClass(FX_UR_UGRP.class, ugrp, datas).close();
		return true;
	}
}
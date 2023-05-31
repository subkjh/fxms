package fxms.bas.impl.cron;

import java.util.List;

import fxms.bas.api.FxApi;
import fxms.bas.co.CoCode.NEW_USER_REG_ST_CD;
import fxms.bas.cron.Crontab;
import fxms.bas.exp.FxOkException;
import fxms.bas.fxo.FxAttr;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.impl.dbo.all.FX_UR_USER_NEW_REQ;
import fxms.bas.impl.dpo.user.UserConfirmDpo;
import fxms.bas.impl.dto.ConfirmUserDto;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 
 * @author subkjh
 *
 */
@FxAdapterInfo(service = "AppService", descr = "회원 가입을 요청한 내역을 처리한다.")
public class UserConfirmCron extends Crontab {

	public static void main(String[] args) {
		Logger.logger.setLevel(LOG_LEVEL.info);
		UserConfirmCron cron = new UserConfirmCron();
		try {
			cron.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FxAttr(name = "schedule", description = "실행계획", value = "* * * * *")
	private String schedule;

	public UserConfirmCron() {

	}

	@Override
	protected String getSchedule() {
		return schedule;
	}

	@Override
	public void start() throws Exception {

		UserConfirmDpo dpo = new UserConfirmDpo();

		// 처리 목록 조회
		List<FX_UR_USER_NEW_REQ> list = ClassDaoEx.SelectDatas(FX_UR_USER_NEW_REQ.class,
				FxApi.makePara("procUserNo is ", null, "newUserRegStCd", NEW_USER_REG_ST_CD.Init.getCode()));

		// 하나씩 처리함
		for (FX_UR_USER_NEW_REQ req : list) {
			ConfirmUserDto dto = ObjectUtil.initObject(req, new ConfirmUserDto());
			dto.setNewUserRegStCd(NEW_USER_REG_ST_CD.Approval.getCode());
			dto.setUgrpNo(User.USER_GROUP_EMPTY);
			try {
				dpo.run(null, dto);
			} catch (FxOkException e) {
			} catch (Exception e) {
				throw e;
			}
		}
	}

}

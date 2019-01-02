package fxms.bas.fxo.service.app.proc.mo;

import java.util.List;

import fxms.bas.api.MoApi;
import fxms.bas.mo.Mo;
import fxms.bas.mo.exception.MoNotFoundException;
import subkjh.bas.user.User;
import subkjh.bas.user.UserProc;

public class DeleteMoProc extends UserProc<List<Mo>> {

	private Mo mo;

	public DeleteMoProc(long moNo) throws Exception {
		mo = MoApi.getApi().getMo(moNo);
		if (mo == null) {
			throw new MoNotFoundException(moNo);
		}
	}

	@Override
	protected String getInPara() {
		return mo.getMoName();
	}

	@Override
	protected String getOutRet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Mo> process() throws Exception {

		MoApi.getApi().deleteMo(mo, User.USER_NO_SYSTEM, "user-delete");
		// TODO Auto-generated method stub
		return null;
	}

}

package fxms.bas.impl.api;

import java.util.List;
import java.util.Map;

import fxms.bas.api.OpApi;
import fxms.bas.exp.NotFoundException;
import fxms.bas.impl.dpo.op.SelectOpDfo;
import fxms.bas.impl.dpo.op.SetUseOpDfo;
import fxms.bas.vo.OpCode;

public class OpApiDfo extends OpApi {

	@Override
	protected List<OpCode> doSelectOpCodes(Map<String, Object> para) throws Exception {
		return new SelectOpDfo().selectOpCode(para);
	}

	@Override
	protected boolean doUpdateOpId(int userNo, String opId, boolean use) throws NotFoundException, Exception {
		return new SetUseOpDfo().updateOpId(userNo, opId, use);

	}

}

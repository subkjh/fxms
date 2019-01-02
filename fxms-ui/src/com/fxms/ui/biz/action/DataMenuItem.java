package com.fxms.ui.biz.action;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.OP_TYPE;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.NeedSelectedData;

public abstract class DataMenuItem<DATA> extends FxMenuItem implements NeedSelectedData<DATA> {

	private DATA data;

	public DataMenuItem(OP_NAME opname) {
		super(opname);
		onSelectedData(null);
	}

	public DataMenuItem(UiOpCodeVo opCode) {
		super(opCode);
		onSelectedData(null);
	}

	public DATA getSelectedData() {
		return data;
	}

	@Override
	public void onSelectedData(DATA data) {

		this.data = data;

		if ( getOpCode() == null) {
			return;
		}
		
		if (getOpCode().getOpType() == OP_TYPE.update.getCode()
				|| getOpCode().getOpType() == OP_TYPE.show.getCode()
				|| getOpCode().getOpType() == OP_TYPE.delete.getCode()) {

			this.setDisable(getOpCode() == null || data == null);

		} else if (getOpCode().getOpType() == OP_TYPE.add.getCode()) {

			this.data = null;

		}

		System.out.println("DataMenuItem : " + getOpCode() + " = " + this.data);
	}

	@Override
	protected void onAction() {
		onAction(data);
	}

	protected abstract void onAction(DATA data);

}

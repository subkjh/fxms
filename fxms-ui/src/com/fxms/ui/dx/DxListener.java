package com.fxms.ui.dx;

import com.fxms.ui.UiCode;

public interface DxListener<DATA> {

	public void onData(UiCode.Action action, DATA data);

}

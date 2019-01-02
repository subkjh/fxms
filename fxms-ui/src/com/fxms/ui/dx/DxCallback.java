package com.fxms.ui.dx;

import java.util.Map;

import com.fxms.ui.bas.pane.list.ListPaneBase;

public interface DxCallback extends FxCallback<Map<String, Object>> {

	/**
	 * 
	 * @param listPane
	 * @return 다시 조회할 callback
	 */
	public static DxCallback makeRefreshCallback(ListPaneBase listPane) {
		return new DxCallback() {
			@Override
			public void onCallback(Map<String, Object> data) {
				listPane.doSearch();
			}
		};
	}

}

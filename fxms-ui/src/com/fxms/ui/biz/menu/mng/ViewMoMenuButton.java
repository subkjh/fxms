package com.fxms.ui.biz.menu.mng;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.event.FX_EVENT_TYPE;
import com.fxms.ui.bas.event.FxEventDispatcher;
import com.fxms.ui.bas.event.FxEventHandler;
import com.fxms.ui.bas.menu.FxCounterMenuButton;
import com.fxms.ui.bas.vo.ui.UiBasicVo;
import com.fxms.ui.dx.FxCallback;

public abstract class ViewMoMenuButton extends FxCounterMenuButton implements FxEventHandler {

	private String moClass;
	private String moClassName;

	public ViewMoMenuButton(String moClass, String moClassName) {
		this.moClass = moClass;
		this.moClassName = moClassName;
	}

	@Override
	public boolean initDxNode(UiBasicVo vo) {
		refreshMoCount();
		return super.initDxNode(vo);
	}

	@Override
	public void onAddedInParent() {
		super.onAddedInParent();
		FxEventDispatcher.getDispatcher().addHandler(FX_EVENT_TYPE.Mo, this);
	}

	@Override
	public void onFxEvent(FX_EVENT_TYPE type, Object target) {
		if (type == FX_EVENT_TYPE.Mo) {
			refreshMoCount();
		}
	}

	@Override
	public void onRemovedFromParent() {
		super.onRemovedFromParent();
		FxEventDispatcher.getDispatcher().addHandler(FX_EVENT_TYPE.Mo, this);
	}

	private void refreshMoCount() {
		CodeMap.getMap().selectMoCount(moClass, new FxCallback<Number>() {
			@Override
			public void onCallback(Number count) {
				getTopLabel().setText("MO");
				getCenterButton().setText(count == null ? "-" : String.valueOf(count.intValue()));
				getBottomLabel().setText(moClassName);
			}
		});
	}

}

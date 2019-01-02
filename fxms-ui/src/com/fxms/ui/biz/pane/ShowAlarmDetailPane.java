package com.fxms.ui.biz.pane;

import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.pane.ButtonPane;
import com.fxms.ui.bas.pane.ShowDetailPane;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.property.FxUiChild;
import com.fxms.ui.bas.property.FxUiList;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.biz.action.FxButton;
import com.fxms.ui.dx.DxAsyncSelector;

import javafx.scene.Node;

public class ShowAlarmDetailPane extends ShowDetailPane implements FxUiChild, FxUi {

	private UiAlarm alarm;
	private Node fxUiParent;

	public ShowAlarmDetailPane() {
	}

	public ShowAlarmDetailPane(UiAlarm alarm) {

		init(CodeMap.getMap().getOpCode(OP_NAME.AlarmDetailShow));

		showAlarm(alarm.getAlarmNo());
	}

	@Override
	public Node getFxUiParent() {
		return fxUiParent;
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return CodeMap.getMap().getOpCode(OP_NAME.AlarmDetailShow);
	}

	@Override
	public void initData(Map<String, Object> data) {
		long alarmNo = 0;

		try {
			alarmNo = Double.valueOf(data.get("alarmNo") + "").longValue();
			showAlarm(alarmNo);
		} catch (Exception e) {
			FxAlert.showAlert(FxAlertType.error, this, "error", e.getMessage());
		}

	}

	@Override
	public void setFxUiParent(Node fxUiParent) {
		this.fxUiParent = fxUiParent;
	}

	private FxButton makeAck() {
		FxButton button = new FxButton(OP_NAME.AlarmAck) {
			@Override
			protected void onAction() {
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("alarmNo", alarm.getAlarmNo());

				if (DxAsyncSelector.getSelector().callMethod(this, getOpCode(), para) != null) {

					if (fxUiParent instanceof FxUiList) {
						((FxUiList) fxUiParent).doSearch();
					}

					hide();
				}
			}
		};

		button.setDisable(alarm.getAckDate() > 0);

		return button;
	}

	private FxButton makeClear() {
		FxButton button = new FxButton(OP_NAME.AlarmClear) {
			@Override
			protected void onAction() {
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("alarmNo", alarm.getAlarmNo());

				if (DxAsyncSelector.getSelector().callMethod(this, getOpCode(), para) != null) {

					if (fxUiParent instanceof FxUiList) {
						((FxUiList) fxUiParent).doSearch();
					}

					hide();
				}
			}
		};

		button.setDisable(alarm.isClearYn());

		return button;
	}

	private FxButton makeMoButton() {
		return new FxButton(OP_NAME.MoTreeShow) {
			@Override
			protected void onAction() {
				try {
					Mo mo;
					try {

					} catch (Exception e) {
						e.printStackTrace();
						return;
					}

					if (alarm.getUpperMoNo() > 0) {
						mo = DxAsyncSelector.getSelector().getMo(alarm.getUpperMoNo());
					} else {
						mo = DxAsyncSelector.getSelector().getMo(alarm.getMoNo());
					}

					if (mo == null) {
						FxAlert.showAlert(FxAlertType.error, getParent(), getOpCode().getOpTitle(),
								Lang.getText(Lang.Type.msg, "관리대상을 찾을 수 없습니다."));
					} else {
						getOpCode().showScreen(getParent(), ObjectUtil.toMap(mo), null, null);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
	}

	private void showAlarm(long alarmNo) {

		alarm = DxAsyncSelector.getSelector().getAlarm(alarmNo);

		if (alarm != null) {
			Map<String, Object> map = ObjectUtil.toMap(alarm);

			super.initData(map);

			ButtonPane hbox = new ButtonPane();
			hbox.getChildren().addAll(makeAck(), makeClear(), makeMoButton());
			setTop(hbox);
		}
	}
}

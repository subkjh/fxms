package com.fxms.ui.node.alarm;

import java.util.Comparator;
import java.util.List;

import com.fxms.ui.bas.property.AlarmRefresher;
import com.fxms.ui.bas.renderer.column.FxTableColumm;
import com.fxms.ui.bas.utils.ObjectUtil;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.biz.action.AlarmAckMenuItem;
import com.fxms.ui.biz.action.AlarmClearMenuItem;
import com.fxms.ui.biz.action.AlarmShowDetailMenuItem;
import com.fxms.ui.biz.action.FxContextMenu;
import com.fxms.ui.dx.DxAlarmReceiver;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

public class AlarmListPane extends ScrollPane implements AlarmRefresher {

	protected final TableView<UiAlarm> table = new TableView<>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AlarmListPane() {

		setContent(table);
		setFitToHeight(true);
		setFitToWidth(true);
		setStyle("-fx-background-color: transparent;");
		setColumns();

		FxContextMenu menu = makeContextMenu();

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				menu.onSelectedData(ObjectUtil.toMap(newSelection));
			}
		});

		table.setContextMenu(menu);
	}

	protected TableView<UiAlarm> getTable() {
		return table;
	}

	protected UiAlarm findAlarm(long alarmNo) {

		for (UiAlarm a : table.getItems()) {
			if (a.getAlarmNo() == alarmNo) {
				return a;
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	private FxContextMenu makeContextMenu() {
		FxContextMenu menu = new FxContextMenu();
		menu.getItems().add(new AlarmAckMenuItem(this));
		menu.getItems().add(new AlarmClearMenuItem(this));
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(new AlarmShowDetailMenuItem(this));

		return menu;
	}

	private void setColumns() {
		table.getColumns().add(new FxTableColumm.AlarmStatus<UiAlarm>("St", 25) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getAlarmKind().name();
			}
		});
		table.getColumns().add(new FxTableColumm.AlarmLevel<UiAlarm>("Lv", 20) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getAlarmKind().name() + "," + data.getAlarmLevel();
			}
		});
		table.getColumns().add(new FxTableColumm.UptimeData<UiAlarm>("발생시간", 80) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getOcuDate();
			}
		});

		table.getColumns().add(new FxTableColumm.StringData<UiAlarm>("별칭", 150) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getMoAname();
			}
		});
		table.getColumns().add(new FxTableColumm.StringData<UiAlarm>("관리명", 200) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getMoName();
			}
		});
		table.getColumns().add(new FxTableColumm.StringData<UiAlarm>("설치위치", 150) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getInloName();
			}
		});
		table.getColumns().add(new FxTableColumm.StringData<UiAlarm>("경보명", 150) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getAlcdName();
			}
		});
		table.getColumns().add(new FxTableColumm.StringData<UiAlarm>("경보내용", 300) {
			@Override
			protected Object getValue(UiAlarm data) {
				return data.getAlarmMsg();
			}
		});
	}

	@Override
	public void onRefresh(DxAlarmReceiver.AlarmFilter alarmFilter) {

		table.getItems().clear();
		table.refresh();

		List<UiAlarm> alarmList = DxAlarmReceiver.getBorader().getAlarmList(alarmFilter);

		alarmList.sort(new Comparator<UiAlarm>() {
			@Override
			public int compare(UiAlarm arg0, UiAlarm arg1) {
				return (int) (arg0.getAlarmNo() - arg1.getAlarmNo());
			}
		});

		for (UiAlarm alarm : alarmList) {
			table.getItems().add(0, alarm);
		}
	}

}

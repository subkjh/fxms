package com.fxms.ui.biz.pane;

import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.property.MoIpAddressable;
import com.fxms.ui.biz.action.FxContextMenu;
import com.fxms.ui.biz.action.MoChartMenu;
import com.fxms.ui.biz.action.mo.MoAlarmMenuItem;
import com.fxms.ui.biz.action.mo.MoShowDetailMenuItem;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.FxCallback;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.ImageView;

public class MoTreeTablePane extends ScrollPane implements FxUi {

	private TreeItem<Mo> root;
	private TreeTableView<Mo> treeTableView;
	private final TreeItem<Mo> tagToSelect = new TreeItem<>(new Mo());
	private final TreeItem<Mo> tagLoading = new TreeItem<>(new Mo("loading..."));
	private UiOpCodeVo opCode;
	private MoShowDetailMenuItem<Mo> moMenu;

	public MoTreeTablePane() {
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public void init(UiOpCodeVo opcode) {
		this.opCode = opcode;
	}

	@Override
	public void initData(Map<String, Object> data) {

		Mo mo;
		try {
			mo = DxAsyncSelector.getSelector().getMo(opCode.getSearchMap(data));
		} catch (Exception e) {
			FxAlert.showAlert(FxAlertType.error, this, "Not Found", e.getMessage());
			return;
		}

		if (mo == null) {
			FxAlert.showAlert(FxAlertType.error, this, "Not Found", data.toString());
			return;
		}

		root = new TreeItem<>(mo);
		root.setExpanded(true);

		treeTableView = new TreeTableView<>(root);
		FxContextMenu<Mo> contextMenu = makeContextMenu(treeTableView);
		treeTableView.setContextMenu(contextMenu);

		treeTableView.setTableMenuButtonVisible(true);
		treeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Mo>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Mo>> observable, TreeItem<Mo> old_val,
					TreeItem<Mo> new_val) {
				if (new_val != null) {
					Mo mo = new_val.getValue();
					moMenu.setOpCode(CodeMap.getMap().getOpCode("mo-" + mo.getMoClass().toLowerCase() + "-update"));
					contextMenu.onSelectedData(new_val.getValue());
				}
			}
		});

		TreeTableColumn<Mo, String> item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "MO_CLASS"));
		item.setPrefWidth(150);
		item.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mo, String> param) -> new ReadOnlyStringWrapper(
				param.getValue().getValue().getMoClass()));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "MO_NAME"));
		item.setPrefWidth(150);
		item.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mo, String> param) -> new ReadOnlyStringWrapper(
				param.getValue().getValue().getMoName()));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "MO_ANAME"));
		item.setPrefWidth(150);
		item.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mo, String> param) -> new ReadOnlyStringWrapper(
				param.getValue().getValue().getMoAname()));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "MO_TYPE"));
		item.setPrefWidth(100);
		item.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mo, String> param) -> new ReadOnlyStringWrapper(
				param.getValue().getValue().getMoType()));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "CHG_DATE"));
		item.setPrefWidth(120);
		item.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mo, String> param) -> new ReadOnlyStringWrapper(
				param.getValue().getValue().getChgDate() + ""));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "IP_ADDRESS"));
		item.setPrefWidth(100);
		item.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mo, String> param) -> new ReadOnlyStringWrapper(
				(param.getValue().getValue() instanceof MoIpAddressable
						? ((MoIpAddressable) param.getValue().getValue()).getIpAddress()
						: "")));
		treeTableView.getColumns().add(item);

		item = new TreeTableColumn<>(Lang.getText(Lang.Type.column, "REMARKS"));
		item.setPrefWidth(300);
		item.setCellValueFactory((TreeTableColumn.CellDataFeatures<Mo, String> param) -> new ReadOnlyStringWrapper(
				param.getValue().getValue().getRemarks()));
		treeTableView.getColumns().add(item);

		this.setContent(treeTableView);
		this.setFitToHeight(true);
		this.setFitToWidth(true);

		// treeTableView.setContextMenu(new DxContextMenu());

		DxAsyncSelector.getSelector().getMoList(mo.getMoNo(), null, new FxCallback<List<Mo>>() {
			@Override
			public void onCallback(List<Mo> data) {
				for (Mo mo : data) {
					makeTree(root, mo);
				}
			}
		});
	}

	private void loadMo4Upper(TreeItem<Mo> upperItem) {

		upperItem.getChildren().add(tagLoading);

		Object obj = upperItem.getValue();
		if (obj instanceof Mo) {
			DxAsyncSelector.getSelector().getMoList(((Mo) obj).getMoNo(), null, new FxCallback<List<Mo>>() {
				@Override
				public void onCallback(List<Mo> data) {
					upperItem.getChildren().remove(tagLoading);

					for (Mo mo : data) {
						makeTree(upperItem, mo);
					}
				}

			});
		}
	}

	@SuppressWarnings("rawtypes")
	private FxContextMenu<Mo> makeContextMenu(TreeTableView<Mo> treeTableView) {

		moMenu = new MoShowDetailMenuItem<Mo>(treeTableView);
		FxContextMenu<Mo> menu = new FxContextMenu<>();
		menu.getItems().add(moMenu);
		menu.getItems().add(new MoAlarmMenuItem(treeTableView));
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(new MoChartMenu(treeTableView));
		
		return menu;
	}

	private void makeTree(TreeItem<Mo> upperItem, Mo mo) {

		TreeItem<Mo> item = new TreeItem<>(mo, new ImageView(ImagePointer.getMoImage(mo)));

		item.addEventHandler(TreeItem.treeNotificationEvent(), new EventHandler<TreeItem.TreeModificationEvent<Mo>>() {
			@Override
			public void handle(TreeItem.TreeModificationEvent<Mo> event) {
				if (event.getEventType() == TreeItem.branchExpandedEvent()) {
					if (event.getSource().getChildren().contains(tagToSelect)) {
						event.getSource().getChildren().remove(tagToSelect);
						loadMo4Upper(event.getSource());
					}
				}
			}
		});

		upperItem.getChildren().add(item);

		if (Mo.isLeaf(mo) == false) {
			item.getChildren().add(tagToSelect);
		}
	}

}

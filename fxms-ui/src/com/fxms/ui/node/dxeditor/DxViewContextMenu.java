package com.fxms.ui.node.dxeditor;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.FxWindow;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.vo.ui.UiGroupVo;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.OpenScreenPane;

import FX.MS.UI;
import FX.MS.UiData;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;

public class DxViewContextMenu extends ContextMenu {

	private DxPane dxPane;
	private Menu dxListMenu;

	public DxViewContextMenu(DxPane dxPane) {

		this.dxPane = dxPane;

		MenuItem editMi = null;
		UiOpCodeVo opcode = CodeMap.getMap().getOpCode(OP_NAME.DxEditorUi);
		if (opcode != null) {
			editMi = new MenuItem(opcode.getOpTitle());
			editMi.setOnAction((ActionEvent e) -> {
				FxStage.showStage(dxPane, new DxEditor(dxPane.getUiGroup().getUiGroupNo(), new FxCallback<String>() {
					@Override
					public void onCallback(String data) {
						dxPane.setUiGroupNo(dxPane.getUiGroup().getUiGroupNo());
					}
				}), opcode, null, "Close", null);
			});
		}

		getItems().add(getDxMenuItem());
		getItems().add(getNewOpenMenuItem());
		getItems().add(new SeparatorMenuItem());
		if (editMi != null)
			getItems().add(editMi);
		getItems().add(getStyleMenu());
		getItems().add(new SeparatorMenuItem());
		getItems().add(getOpenMenuItem());
		getItems().add(new SeparatorMenuItem());
		getItems().add(getExitMenuItem());
	}

	public void setSelected(int uiGroupNo) {

		for (MenuItem mi : dxListMenu.getItems()) {
			if (mi instanceof RadioMenuItem && mi.getUserData() instanceof UiGroupVo) {
				if (((UiGroupVo) mi.getUserData()).getUiGroupNo() == uiGroupNo) {
					((RadioMenuItem) mi).setSelected(true);
					break;
				}
			}
		}

	}

	private Menu getDxMenuItem() {

		dxListMenu = new Menu(Lang.getText(Lang.Type.menu, "Change Dx Screen"));

		ToggleGroup toggleGroup = new ToggleGroup();

		for (UiGroupVo vo : CodeMap.getMap().getUiList()) {
			RadioMenuItem item = new RadioMenuItem(vo.getUiGroupNo() + ". " + vo.getUiGroupName());
			item.setUserData(vo);
			item.setOnAction((ActionEvent e) -> {
				UiGroupVo data = (UiGroupVo) item.getUserData();
				dxPane.setUiGroupVo(data);
				CodeMap.getMap().setUserProperty(CodeMap.USER_PROPERTY_DX_INDEX, data.getUiGroupNo());
				item.setSelected(true);
			});
			item.setToggleGroup(toggleGroup);
			dxListMenu.getItems().add(item);
		}

		return dxListMenu;
	}

	private MenuItem getExitMenuItem() {

		MenuItem menu = new MenuItem(Lang.getText(Lang.Type.menu, "Exit FxMS"));

		menu.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});

		return menu;
	}

	private FxStage searchWindowStage;

	private MenuItem getOpenMenuItem() {

		MenuItem menu = new MenuItem(Lang.getText(Lang.Type.menu, "Search Windows"));

		menu.setOnAction((ActionEvent e) -> {
			if (searchWindowStage != null) {
				searchWindowStage.show();
			} else {
				searchWindowStage = FxStage.showStage(dxPane, new OpenScreenPane(), "Search Windows");
			}
		});

		return menu;
	}

	private Menu getNewOpenMenuItem() {

		Menu menu = new Menu(Lang.getText(Lang.Type.menu, "Open New Dx Windows"));

		for (UiGroupVo vo : CodeMap.getMap().getUiList()) {
			MenuItem item = new MenuItem(vo.getUiGroupNo() + ". " + vo.getUiGroupName());
			item.setUserData(vo);
			item.setOnAction((ActionEvent e) -> {
				FxWindow.showStage(getOwnerNode(), new DxPane((UiGroupVo) item.getUserData()), vo.getUiGroupName());
			});
			menu.getItems().add(item);
		}

		return menu;
	}

	private Menu getStyleMenu() {

		Menu menu = new Menu(Lang.getText(Lang.Type.menu, "Window Style"));

		CheckMenuItem item = new CheckMenuItem("Full Screen");
		item.setSelected(UiData.getConfig(UiData.fullscreen, "n").equalsIgnoreCase("y"));
		item.setOnAction((ActionEvent e) -> {
			UiData.setConfig(UiData.fullscreen, item.isSelected() ? "y" : "n");
			UI.primaryStage.setFullScreen(item.isSelected());
		});
		menu.getItems().add(item);

		return menu;
	}

}

package com.fxms.ui.node.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.editor.CodeEditor;
import com.fxms.ui.bas.editor.FxEditor;
import com.fxms.ui.bas.editor.FxEditor.EDITOR_TYPE;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.lang.Lang.Type;
import com.fxms.ui.bas.editor.ListEditor;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.mo.NeMo;
import com.fxms.ui.bas.property.FxEditorNode;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.vo.Attr;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.network.NetworkDrawPane.TOPOLOGY_TYPE;
import com.fxms.ui.node.tree.FxTreeAllPanelStage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class NetworkEditor extends BorderPane implements FxEditorNode, FxUi {

	private UiOpCodeVo opcode;
	private CodeEditor topologyType;
	private ListEditor ringDirection;
	private ListEditor ringStartAngle;
	private final NetworkDrawPane canvas;
	private HBox hBox = new HBox(8);

	public NetworkEditor() {

		hBox.setPadding(new Insets(15, 12, 15, 12));

		topologyType = (CodeEditor) FxEditor.makeEditor("topologyType", EDITOR_TYPE.Code.name(), "TOPOLOGY_TYPE",
				"토폴로지 종류", 200, null);
		topologyType.valueProperty().addListener(new ChangeListener<UiCodeVo>() {
			@Override
			public void changed(ObservableValue<? extends UiCodeVo> observable, UiCodeVo oldValue, UiCodeVo newValue) {
				TOPOLOGY_TYPE type = TOPOLOGY_TYPE.getType(newValue.getCdCode());

				if (type == TOPOLOGY_TYPE.RING) {
					if (hBox.getChildren().contains(ringDirection) == false) {
						hBox.getChildren().add(1, ringDirection);
						hBox.getChildren().add(2, ringStartAngle);
					}
				} else {
					hBox.getChildren().remove(ringDirection);
					hBox.getChildren().remove(ringStartAngle);
				}

				canvas.setTopologyType(type);
			}
		});

		ringDirection = (ListEditor) FxEditor.makeEditor("ringDirection", EDITOR_TYPE.List.name(),
				"0=East to West,1=West to East", Lang.getText(Type.prompt, "링 방향"), 150, null);
		ringDirection.valueProperty().addListener(new ChangeListener<Attr>() {
			@Override
			public void changed(ObservableValue<? extends Attr> observable, Attr oldValue, Attr newValue) {
				canvas.setRingDirection(Integer.valueOf(newValue.getAttrId()));
			}
		});

		ringStartAngle = (ListEditor) FxEditor.makeEditor("ringStartAngle", EDITOR_TYPE.List.name(),
				"90=Top,180=Left,270=Buttom,0=Right", Lang.getText(Type.prompt, "장비 시작점"), 120, null);
		ringStartAngle.valueProperty().addListener(new ChangeListener<Attr>() {
			@Override 
			public void changed(ObservableValue<? extends Attr> observable, Attr oldValue, Attr newValue) {
				canvas.setRingRotationAngle(Integer.valueOf(newValue.getAttrId()));
			}
		});

		Button bTest = new Button(Lang.getText(Type.button, "장비추가"));
		bTest.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				FxTreeAllPanelStage.show(NetworkEditor.this, NeMo.MO_CLASS, new FxCallback<Mo>() {

					@Override
					public void onCallback(Mo data) {
						NetworkNode node = new NetworkNode(data.getMoNo());
						canvas.addNode(node);
					}
				});
			}
		});

		Label tooltip = new Label();
		canvas = new NetworkDrawPane(tooltip);
		canvas.setEditable(true);

		hBox.getChildren().addAll(topologyType, bTest, tooltip);

		setTop(hBox);
		setCenter(canvas);
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opcode;
	}

	@Override
	public void init(UiOpCodeVo opcode) {
		this.opcode = opcode;
	}

	@Override
	public void initData(Map<String, Object> data) {
		canvas.initData(data);
		topologyType.setAttrId(canvas.getNet().getTopologyType(), null);
	}

	@Override
	public Map<String, Object> getInputData() {

		UiNetVo net = canvas.getNet();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("item-list", new ArrayList<UiNetItemVo>(net.getItemList()));
		net.getItemList().clear();
		map.put("network", net);

		return map;
	}

	@Override
	public Map<String, Object> getDefaultData() {
		return null;
	}

}

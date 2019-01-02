package com.fxms.ui.node.diagram;

import java.util.Optional;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.FxWindow;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.editor.FxEditor;
import com.fxms.ui.bas.editor.FxEditor.EDITOR_TYPE;
import com.fxms.ui.bas.editor.QidEditor;
import com.fxms.ui.bas.vo.Attr;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.node.diagram.event.DiagEvent;
import com.fxms.ui.node.diagram.event.DiagEventLine;
import com.fxms.ui.node.diagram.event.DiagEventNode;
import com.fxms.ui.node.diagram.menuitem.DiagMenu4Edit;
import com.fxms.ui.node.diagram.node.DiagNode;
import com.fxms.ui.node.diagram.node.DiagNodeLine;
import com.fxms.ui.node.diagram.vo.DiagMainVo;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

public class FxDiagramEdit extends BorderPane {

	private DiagNode linkStartNode;
	private final DiagEventNode nodeEvent;
	private final DiagEventLine lineEvent;
	private final DiagMenu4Edit contextMenu;
	private final FxDiagram diagramPane;
	private final TextField txtName;

	public FxDiagramEdit() {

		txtName = new TextField();
		txtName.setPromptText("다이아그램 명칭을 입력하세요");
		txtName.setPrefWidth(300);

		diagramPane = new FxDiagram() {
			@Override
			public DiagNode addDiagNode(DiagNodeVo vo) {

				DiagNode addedNode = super.addDiagNode(vo);

				if (addedNode instanceof Node) {
					nodeEvent.addEvent((Node) addedNode);
					((Node) addedNode).setOnContextMenuRequested((ContextMenuEvent event) -> {
						contextMenu.setNode(addedNode);
					});
				}

				return addedNode;
			}

			@Override
			public DiagNodeLine addLine(DiagNode startNode, DiagNode endNode) {
				DiagNodeLine line = super.addLine(startNode, endNode);
				if (line instanceof Node) {

					lineEvent.addEvent((Node) line);

					((Node) line).setOnContextMenuRequested((ContextMenuEvent event) -> {
						contextMenu.setNode(line);
					});
				}

				return line;
			}
		};

		contextMenu = new DiagMenu4Edit(this);
		nodeEvent = new DiagEventNode(this);
		lineEvent = new DiagEventLine(this);

		ScrollPane sp = new ScrollPane();
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.setContent(diagramPane);
		sp.setContextMenu(contextMenu);

		setTop(makeTop());
		setCenter(sp);

		new DiagEvent().addEvent(this);

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		setPrefSize(primaryScreenBounds.getWidth() - 200, primaryScreenBounds.getHeight() - 200);
	}

	public ContextMenu getContextMenu() {
		return contextMenu;
	}

	public FxDiagram getDiagramPane() {
		return diagramPane;
	}

	public void setDiagMainVo(DiagMainVo vo) {
		diagramPane.setDiagram(vo);
		txtName.setText(vo != null ? vo.getDiagTitle() : "");
	}

	public boolean setLinkNode(DiagNode linkNode) {

		if (linkNode != null) {

			if (linkStartNode == null) {
				linkStartNode = linkNode;
				return false;
			}

			if (linkStartNode.equals(linkNode) == false) {
				diagramPane.addLine(linkStartNode, linkNode);
				return true;
			}
		}

		return true;
	}

	private HBox makeTop() {

		HBox hBox = new HBox(8);
		hBox.setPadding(new Insets(15, 12, 15, 12));

		QidEditor diagList = (QidEditor) FxEditor.makeEditor("diagNo", EDITOR_TYPE.Qid.name(), "SELECT_DIAGRAM_LIST",
				"수정할 다이아그램을 선택하세요.", 0, null);
		diagList.valueProperty().addListener(new ChangeListener<Attr>() {
			@Override
			public void changed(ObservableValue<? extends Attr> observable, Attr oldValue, Attr newValue) {
				diagramPane.setDiagNo(Double.valueOf(newValue.getAttrId()).intValue());
				txtName.setText(newValue.getAttrText());
			}
		});

		Button bSave = new Button("Save");
		bSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				DiagMainVo mainVo = diagramPane.getDiagMainVo();
				mainVo.setDiagTitle(txtName.getText());
				UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.DiagramSet);
				opCode.setOkMsg("저장되었습니다.");
				DxAsyncSelector.getSelector().callMethod(FxDiagramEdit.this, opCode, mainVo.getSaveMap());
			}
		});

		Button bSaveAs = new Button("Save as");
		bSaveAs.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				DiagMainVo mainVo = diagramPane.getDiagMainVo();

				TextInputDialog dialog = new TextInputDialog(mainVo.getDiagTitle());
				dialog.setTitle("Save As");
				dialog.setHeaderText("Diagram");
				dialog.setContentText("Please enter new title : ");

				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					mainVo.setDiagNo(-1);
					mainVo.setDiagTitle(result.get());
					UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.DiagramSet);
					DxAsyncSelector.getSelector().callMethod(FxDiagramEdit.this, opCode, mainVo.getSaveMap());
				}

			}
		});

		Button bPreview = new Button("Preview");
		bPreview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				DxDiagram parent = new DxDiagram();
				parent.setDiagram(diagramPane.getDiagMainVo());
				FxWindow.showStage(bPreview, parent, "Preview " + diagramPane.getDiagMainVo().getDiagTitle());
			}
		});

		hBox.getChildren().addAll(diagList, new Separator(Orientation.VERTICAL), txtName,
				new Separator(Orientation.VERTICAL), bSave, bSaveAs, bPreview);

		return hBox;
	}
}

package com.fxms.ui.biz.main;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.grid.FxGridPane;
import com.fxms.ui.bas.grid.GridCfg;
import com.fxms.ui.bas.grid.GridNode;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.node.alarm.AckAlarmBox;
import com.fxms.ui.node.alarm.AlarmCountBarChartPane;
import com.fxms.ui.node.alarm.ClearAlarmBox;
import com.fxms.ui.node.alarm.CurAlarmBox;
import com.fxms.ui.node.status.ViewPsPane;

import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DxMainPane extends FxGridPane {

	public static Stage dxStage;

	public DxMainPane(Stage stage, String title, GridCfg cfg) {
		super(title, cfg);

		setId("dx-main-pane");

		dxStage = stage;

		for (Node node : getNodeList()) {

			if (node instanceof Pane) {
				((Pane) node).setBorder(new Border(
						new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
			}

			add(node.getId(), node, (GridNode) node.getProperties().get(GridNode.class));
		}
	}

	public List<Node> getNodeList() {
		List<Node> nodeList = new ArrayList<Node>();
		Node item;

		item = new AlarmCountBarChartPane();
		item.setId("alarm-count-bar");
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(1, 1), new GridNode.Size(4, 2)));
		nodeList.add(item);

		item = new ViewPsPane();
		((ViewPsPane) item).set(DxAsyncSelector.getSelector().getMo(1000136), CodeMap.getMap().getPsItem("TEMP"));
		item.setId("alarm-count-bar");
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(36, 1), new GridNode.Size(8, 4)));
		nodeList.add(item);

		item = new ViewPsPane();
		((ViewPsPane) item).set(DxAsyncSelector.getSelector().getMo(1000137), CodeMap.getMap().getPsItem("TEMP"));
		item.setId("alarm-count-bar");
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(44, 1), new GridNode.Size(8, 4)));
		nodeList.add(item);

		// Map<String, Object> parameters = new HashMap<String, Object>();
		// parameters.put("diagNo", 50003);
		// CdOp opCode = CodeMap.getMap().getCdOp("get-diagram");
		// Map<String, Object> retMap = DxAsyncSelector.getSelector().callMethod(opCode,
		// parameters);
		//
		// System.out.println(retMap.get("main"));
		//
		// DiagMainVo mainVo = new DiagMainVo();
		// mainVo.setMap(retMap);
		//
		// item = new DxDiagram();
		// ((DxDiagram) item).setDiagram(mainVo);
		// item.setId("diagram");
		// item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(36, 6),
		// new GridNode.Size(20, 10)));
		// nodeList.add(item);

		item = new CurAlarmBox();
		item.setId("cur-alarm");
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(1, 5), new GridNode.Size(12, 8)));
		nodeList.add(item);

		item = new AckAlarmBox();
		item.setId("acked-alarm");
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(13, 5), new GridNode.Size(4, 8)));
		nodeList.add(item);

		item = new ClearAlarmBox();
		item.setId("clear-alarm");
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(17, 5), new GridNode.Size(4, 8)));
		nodeList.add(item);

		return nodeList;
	}

}

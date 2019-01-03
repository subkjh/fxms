package com.fxms.ui.biz.main;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.bas.grid.FxGridPane;
import com.fxms.ui.bas.grid.GridCfg;
import com.fxms.ui.bas.grid.GridNode;
import com.fxms.ui.bas.menu.FxMenuButton;
import com.fxms.ui.biz.menu.mng.GotoDashButton;
import com.fxms.ui.biz.menu.mng.ViewAlarmConfigButton;
import com.fxms.ui.biz.menu.mng.ViewAlarmHistoryButton;
import com.fxms.ui.biz.menu.mng.ViewAlarmStatButton;
import com.fxms.ui.biz.menu.mng.ViewLocationTreeButton;
import com.fxms.ui.biz.menu.mng.ViewMoContainerButton;
import com.fxms.ui.biz.menu.mng.ViewMoGwButton;
import com.fxms.ui.biz.menu.mng.ViewMoPbrButton;
import com.fxms.ui.biz.menu.mng.ViewMoSensorButton;
import com.fxms.ui.biz.menu.mng.ViewMoTreeButton;
import com.fxms.ui.biz.menu.mng.ViewMyAccountButton;
import com.fxms.ui.biz.menu.mng.ViewPerfButton;
import com.fxms.ui.biz.menu.mng.ViewPerfIfButton;
import com.fxms.ui.biz.menu.mng.ViewUserLoginHstButton;
import com.fxms.ui.biz.menu.mng.ViewUserMngButton;
import com.fxms.ui.biz.menu.mng.ViewUserOpHstButton;
import com.fxms.ui.biz.menu.mng.ViewWebButton;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.item.ReloadDataButton;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

public class MxMainPane extends FxGridPane {

	public MxMainPane(Stage stage, String title, GridCfg cfg) {

		super(title, cfg);

		this.getGrid().setId("mx-main-pane");
		// this.getGrid().setBackground(makeBackground());

		for (Node node : getNodeList()) {
					add(node.getId(), node, (GridNode) node.getProperties().get(GridNode.class));
		}
	}

	Background makeBackground() {
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
		Background background2 = new Background(
				new BackgroundImage(ImagePointer.getImage("background/dashboard-background.jpg"),
						BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize));
		return background2;
	}

	private List<Node> getNodeList() {

		List<Node> nodeList = new ArrayList<Node>();

		FxMenuButton item = new ViewLocationTreeButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(2, 1), new GridNode.Size(4, 2)));
		nodeList.add(item);

		item = new ViewMoContainerButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(6, 1), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewMoPbrButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(9, 1), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewMoGwButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(12, 1), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewMoSensorButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(15, 1), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewMoTreeButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(18, 1), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewAlarmHistoryButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(2, 3), new GridNode.Size(7, 3)));
		nodeList.add(item);

		item = new ViewAlarmConfigButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(9, 3), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewAlarmStatButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(12, 3), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewPerfButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(2, 6), new GridNode.Size(7, 2)));
		nodeList.add(item);

		item = new ViewPerfIfButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(9, 6), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewUserOpHstButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(2, 8), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewUserLoginHstButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(5, 8), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewUserMngButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(8, 8), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new ViewMyAccountButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(20, 8), new GridNode.Size(3, 2)));
		nodeList.add(item);

		item = new GotoDashButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(16, 10), new GridNode.Size(7, 2)));
		nodeList.add(item);

		item = new ViewWebButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(13, 10), new GridNode.Size(2, 2)));
		nodeList.add(item);

		item = new ReloadDataButton();
		item.getProperties().put(GridNode.class, new GridNode(new GridNode.XY(15, 11), new GridNode.Size(1, 1)));
		nodeList.add(item);

		return nodeList;
	}

}

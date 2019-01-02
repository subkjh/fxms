package test;

import com.fxms.ui.UiCode;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.node.network.NetworkEditor;

import FX.MS.UI;
import javafx.application.Application;
import javafx.stage.Stage;

public class TestSinglePane extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		UI.primaryStage = primaryStage;

		try {
			DxAsyncSelector.getSelector().setServer("125.7.128.42", 10005);
			DxAsyncSelector.getSelector().login("SYSTEM", "SYSTEM");
		} catch (Exception e1) {
			e1.printStackTrace();
			System.exit(0);
		}

		UiCode.reload(null);

		try {

			// Scene scene = new Scene(new MoDetailPane(1000124));
			// Scene scene = new Scene(new DxEditor());

			// AlarmCountBarChartPane pane = new AlarmCountBarChartPane();
			// Scene scene = new Scene(pane);
			// pane.onAddedInParent();

			// CurAlarmCountBox pane = new CurAlarmCountBox();
			// Scene scene = new Scene(pane);

			// Scene scene = new Scene(new ClockHtml());
			// Scene scene = new Scene(new Label("1234567890"));
			// Scene scene = new Scene(new DxPane(1));
			// Scene scene = new Scene(new AlarmCountPiePane());

			// CurAlarmListScrollPane pane = new CurAlarmListScrollPane();
			// Scene scene = new Scene(pane);
			// pane.onAddedInParent();

			// FxDiagramEdit pane = new FxDiagramEdit();
			 NetworkEditor pane = new NetworkEditor();
			 
//			NetworkMonitorPane pane = new NetworkMonitorPane();
//			Map<String, Object> data = new HashMap<String, Object>();
//			data.put("netNo", 10001);
//			pane.initData(data);

			// Scene scene = new Scene();
			// Scene scene = new Scene(new AlarmCfgPane(null));

			// TreeCfgPane pane = new TreeCfgPane();
			// pane.init(CodeMap.getMap().getCdOp(OP_NAME.Tree));
			// Scene scene = new Scene(pane);

			// FxTreePane pane = new FxTreePane();
			// Scene scene = new Scene(new FxWindow(pane, "User Tree"));
			// DxAlarmReceiver.getBorader().add(pane);

			// EventMon2Pane pane = new EventMon2Pane();
			// pane.onAddedInParent();
			// BorderPane bpane = new BorderPane();
			// bpane.setCenter(pane);
			// Scene scene = new Scene(bpane, 200, 100);
			// pane.initDxNode(null);

			// Scene scene = new Scene(new SnmpEditor());
			// Scene scene = new Scene(new ViewDataMngButton());

			// LocationTreeTableViewPane pane = new LocationTreeTableViewPane();
			// LocationTreeBasePanel pane = new LocationTreeBasePanel("test", null);
			// Scene scene = new Scene(new TableCfgPane("AAA"));

			// SearchListPane pane = new
			// SearchListPane(CodeMap.getMap().getCdOp(OpName.EtcTableUi));
			// pane.getListPane().getTableContextMenu().getItems().addAll(
			// new BasCurMenuItem(OpName.EtcTableUpdate,
			// pane.getListPane().getSearchCallback()),
			// new BasCurMenuItem(OpName.EtcTableDelete,
			// pane.getListPane().getSearchCallback()),
			// new SeparatorMenuItem(),
			// new BasNewMenuItem(OpName.EtcTableAdd,
			// pane.getListPane().getSearchCallback())
			// );
			// Scene scene = new Scene(pane);

			// ViewChartPane pane = new ViewChartPane();

			// AlarmCodeTreePane pane = new AlarmCodeTreePane(null);

			// AlarmCodeTreeEditor pane = new AlarmCodeTreeEditor();

			// FxWindowTitle pane = new FxWindowTitle("Test Window Titie");
			// pane.setPrefWidth(800);
			// pane.showMsg("DxItemTrafficChart");

			// OpenScreenPane pane = new OpenScreenPane();
			// OpTreeTablePane pane = new OpTreeTablePane();
			// pane.init(CodeMap.getMap().getOpCode(OP_NAME.UserOpTree));

			// CodeMap.getMap().getOpCode(OP_NAME.MoChildGrid).showScreen(null, null, null,
			// null);

			// MoGridPane pane = new MoGridPane();
			// pane.init(CodeMap.getMap().getOpCode(OP_NAME.MoChildGridContainer));

			//
			// DxItemTrafficChart pane = new DxItemTrafficChart();
			// Mo mo = DxAsyncSelector.getSelector().getMo(1040147);
			// pane.viewChart(mo);

			// SystemTime node = new SystemTime();
			// ScrollPane pane = new ScrollPane();
			// pane.setContent(node);
			// node.onAddedInParent();

			FxStage.showStage(null, pane, "Test");

			// Scene scene = new Scene(pane);
			// scene.getStylesheets().add(CssPointer.getStyleSheet("fxms.css"));
			//
			// primaryStage.setTitle("TEST");
			// primaryStage.getIcons().add(ImagePointer.getImage("s16x16/fx.jpg"));
			// primaryStage.setScene(scene);
			// primaryStage.initStyle(StageStyle.DECORATED);
			// primaryStage.show();

			// SnapshotParameters snapshotParameters = new SnapshotParameters();
			// snapshotParameters.setTransform(new Scale(100 / pane.getWidth(), 100 /
			// pane.getHeight()));
			// WritableImage image = new WritableImage(100, 100);
			// image = pane.snapshot(snapshotParameters, image);
			// pane2.add(new ImageView(image));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
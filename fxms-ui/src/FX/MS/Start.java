package FX.MS;

import java.io.File;

import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.lang.Lang.Type;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.dx.DxAsyncSelector;

import fxms.client.log.LOG_LEVEL;
import fxms.client.log.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Start extends Application {

	private Label msgLabel;
	private ProgressBar progressBar;
	private Node centerNode;
	private final int WIDTH = 340;
	private final Button bAccess = new Button(Lang.getText(Type.button, "접속"));
	private final HBox buttonBox = new HBox(10);
	private Button bExit = new Button(Lang.getText(Type.button, "종료"));
	

	public static void main(String[] args) {
		Logger.logger.setLevel(LOG_LEVEL.trace);
		launch(args);
	}


	@Override
	public void start(Stage stage) throws Exception {
		
		centerNode = makeCenter();

		BorderPane pane = new BorderPane();
		pane.setTop(makeTop());
		pane.setCenter(centerNode);
		pane.setBottom(makeBottom());
		pane.setPrefWidth(WIDTH);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(CssPointer.getStyleSheet("login.css"));

		stage.initStyle(StageStyle.UNDECORATED);
		stage.setOpacity(0.85);
		stage.setScene(scene);
		stage.show();


	}

	private Node makeBottom() {

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(4, 4, 8, 4));
		progressBar = new ProgressBar();
		progressBar.setProgress(0);
		progressBar.setPrefWidth(WIDTH);

		msgLabel = new Label("");
		msgLabel.setPrefHeight(24);
		msgLabel.setPrefWidth(WIDTH);

		pane.setTop(progressBar);
		pane.setCenter(msgLabel);

		bAccess.setDefaultButton(true);

		buttonBox.setPadding(new Insets(8, 4, 8, 4));
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.getChildren().addAll(bAccess, bExit);

		bExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		});

		pane.setBottom(buttonBox);

		return pane;
	}

	private Node makeCenter() {

		String server = UiData.getConfig(UiData.server, "125.7.128.42");
		String port = UiData.getConfig(UiData.port, "10005");

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(16, 8, 8, 8));

		TextField tfIp = new TextField(server == null ? "" : server);
		tfIp.setPromptText(Lang.getText(Type.msg, "접속 서버 주소를 입력하세요"));

		TextField tfPort = new TextField(port == null ? "10005" : port);
		tfPort.setPromptText(Lang.getText(Type.msg, "접속 서버 포트번호를 입력하세요"));
		vbox.getChildren().add(tfIp);
		vbox.getChildren().add(tfPort);

		bAccess.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				DxAsyncSelector.getSelector().setServer(tfIp.getText(), Integer.valueOf(tfPort.getText()));
				try {
					DxAsyncSelector.getSelector().download("deploy/ui/exe/fxms.jar", new File("fxms.jar"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		return vbox;
	}

	private Node makeTop() {
		Label title = new Label("FX Management System");
		title.setId("login-title");
		title.setPadding(new Insets(8, 8, 8, 8));
		title.setPrefWidth(WIDTH);

		return title;
	}

}

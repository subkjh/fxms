package FX.MS;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.UiCode;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.lang.Lang.Type;
import com.fxms.ui.bas.pane.EditorPane;
import com.fxms.ui.bas.property.ProgressIndicator;
import com.fxms.ui.css.CssPointer;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.dx.FxCallback;

import FX.MS.UiData.UI_LOG_LEVEL;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginStage extends Stage implements ProgressIndicator {

	private Label msgLabel;
	private ProgressBar progressBar;
	private FxCallback<String> callback;
	private Node centerNode;
	private int size = -1;
	private final int WIDTH = 340;
	private final Button bLogin = new Button("Login");
	private final HBox buttonBox = new HBox(10);
	private Button bNewOne = new Button("New Account");
	private Button bExit = new Button("Exit");
	private CheckBox check = new CheckBox("Save Account");

	public LoginStage(Stage ownerStage, FxCallback<String> callback) {

		this.callback = callback;
		centerNode = makeCenter();

		BorderPane pane = new BorderPane();
		pane.setTop(makeTop());
		pane.setCenter(centerNode);
		pane.setBottom(makeBottom());
		pane.setPrefWidth(WIDTH);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(CssPointer.getStyleSheet("login.css"));

		initOwner(ownerStage);
		initStyle(StageStyle.UNDECORATED);
		setOpacity(0.85);
		setScene(scene);
		show();

	}

	@Override
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void showMsg(int index, String msg) {

		Platform.runLater(new Runnable() {
			public void run() {

				msgLabel.setText(index + "/" + size + " " + msg);
				UiData.log(UI_LOG_LEVEL.info, index + "/" + size + " " + msg);

				if (index == size && isShowing()) {
					hide();
					callback.onCallback("OK");
				}
			}
		});

	}

	private void initUi() {

		progressBar.setProgress(-1);

		new Thread() {

			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				UiCode.reload(LoginStage.this);
			}

		}.start();
	}

	private String login(String userId, String password, String host, String port) {

		centerNode.setDisable(true);
		bNewOne.setDisable(true);
		bLogin.setDisable(true);

		try {
			DxAsyncSelector.getSelector().setServer(host, Integer.valueOf(port));
			DxAsyncSelector.getSelector().login(userId, password);

			UiData.setConfig(UiData.server, host);
			UiData.setConfig(UiData.port, port);

			if (check.isSelected()) {
				UiData.setConfig(UiData.userid, userId);
				UiData.setConfig(UiData.password, password);
			} else {
				UiData.setConfig(UiData.userid, null);
				UiData.setConfig(UiData.password, null);
			}

			initUi();

			return "logined";
		} catch (Exception e) {
			centerNode.setDisable(false);
			bNewOne.setDisable(false);
			bLogin.setDisable(false);
			return "login error : " + e.getMessage();
		}
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

		bLogin.setDefaultButton(true);

		buttonBox.setPadding(new Insets(8, 4, 8, 4));
		buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
		buttonBox.getChildren().addAll(bNewOne, bLogin, bExit);

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

		String server = UiData.getConfig(UiData.server, null); //"125.7.128.42");
		String port = UiData.getConfig(UiData.port, "10005");
		String userid = UiData.getConfig(UiData.userid, null);
		String password = UiData.getConfig(UiData.password, null);

		check.setSelected(userid != null);

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(16, 16, 8, 16));

		TextField userTextField = new TextField();
		userTextField.setPrefWidth(WIDTH - 50);
		userTextField.setPromptText(Lang.getText(Type.msg, "계정을 입력하세요"));
		userTextField.setText(userid);

		TextField tfIp = new TextField(server == null ? "" : server);
		tfIp.setPromptText(Lang.getText(Type.msg, "접속 서버 주소를 입력하세요"));

		TextField tfPort = new TextField(port == null ? "10005" : port);
		tfPort.setPromptText(Lang.getText(Type.msg, "접속 서버 포트번호를 입력하세요"));

		PasswordField pwBox = new PasswordField();
		pwBox.setPromptText(Lang.getText(Type.msg, "암호를 입력하세요"));
		pwBox.setText(password == null ? "" : password);

		vbox.getChildren().addAll(userTextField, pwBox, tfIp, tfPort, check);

		bLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String msg = login(userTextField.getText(), pwBox.getText(), tfIp.getText(), tfPort.getText());
				msgLabel.setText(msg);
			}
		});

		bNewOne.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				try {
					DxAsyncSelector.getSelector().setServer(tfIp.getText(),
							Integer.valueOf(tfPort.getText()).intValue());
					DxAsyncSelector.getSelector().login("GUEST", "guest");
					CodeMap.getMap().loadOpCode();
				} catch (Exception e2) {
					FxAlert.showAlert(FxAlertType.error, bNewOne, "Error", Lang.getText(Type.msg, "기능이 구현되지 않았습니다."));
					return;
				}

				UiOpCodeVo opCode = CodeMap.getMap().getOpCode(OP_NAME.UserNewApply);
				if (opCode != null) {

					EditorPane pane = new EditorPane(Lang.getText(Type.button, "신청"), Lang.getText(Type.button, "취소"),
							false);
					pane.init(opCode);
					pane.initData(null);
					pane.setCallback(new DxCallback() {
						@Override
						public void onCallback(Map<String, Object> data) {
							pane.getScene().getWindow().hide();
						}
					});
					FxStage.showDialog(bNewOne, pane, opCode, null, null, null);

				} else {
					FxAlert.showAlert(FxAlertType.error, bNewOne, "Error", Lang.getText(Type.msg, "기능이 구현되지 않았습니다."));
				}
			}
		});

		return vbox;
	}

	private Node makeTop() {
		Label title = new Label("FxMS version 0.5.1");
		title.setId("login-title");
		title.setPadding(new Insets(8, 8, 8, 8));
		title.setPrefWidth(WIDTH);

		return title;
	}

}

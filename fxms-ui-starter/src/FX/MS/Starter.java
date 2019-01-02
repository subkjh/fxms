package FX.MS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import FX.MS.Lang.Type;
import fxms.client.FxmsClient;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Starter extends Application {

	public static void main(String[] args) {
		
		String folder = System.getProperty("user.home") + "/fxms/logs";
		Logger.logger = Logger.createLogger(folder, "fxms");
		Logger.logger.setLevel(LOG_LEVEL.trace);
//
//		System.out.println(System.getProperties());
//		// System.out.println(System.getenv());
		launch(args);
	}
	private Node centerNode;
	private final int WIDTH = 340;
	private final Button bAccess = new Button(Lang.getText(Type.button, "Access"));
	private final HBox buttonBox = new HBox(10);

	private Button bExit = new Button(Lang.getText(Type.button, "Exit"));

	private final String FILE_PREFIX = "FILE://";

	@Override
	public void start(Stage stage) throws Exception {

		centerNode = makeCenter();

		BorderPane pane = new BorderPane();
		pane.setTop(makeTop());
		pane.setCenter(centerNode);
		pane.setBottom(makeBottom());
		pane.setPrefWidth(WIDTH);

		Scene scene = new Scene(pane);
		scene.getStylesheets().add(Starter.class.getResource("login.css").toExternalForm());

		stage.initStyle(StageStyle.UNDECORATED);
		stage.setOpacity(0.85);
		stage.setScene(scene);
		stage.show();

	}

	private void download(FxmsClient client, List<UiFileVo> fileList) throws Exception {

		String destFilename;

		for (UiFileVo file : fileList) {
			destFilename = getHome() + File.separator + file.getName().replaceAll("deploy/ui", "");

			Logger.logger.debug("download : " + destFilename);

			client.download(file.getName(), new File(destFilename));

			UiData.setConfig(FILE_PREFIX+ file.getName(), file.getValue());
		}

		UiData.writeConfig();
	}

	private void downloadAndStart(String host, int port) throws Exception {

		FxmsClient client = new FxmsClient(host, port);
		try {
			File file = new File(getHome());
			if (file.exists() == false) {
				file.mkdirs();
			}

			List<Map<String, Object>> mapList = (List<Map<String, Object>>) client.ls("deploy/ui");
			List<UiFileVo> fileList = new ArrayList<UiFileVo>();
			UiFileVo vo;

			for (Map<String, Object> e : mapList) {
				vo = new UiFileVo();
				vo.setName(String.valueOf(e.get("name")));
				vo.setSize(Double.valueOf(e.get("size").toString()).longValue());
				vo.setLastModified(Double.valueOf(e.get("lastModified").toString()).longValue());
				fileList.add(vo);
				
				Logger.logger.debug(vo.toString());
			}

			fileList = getChangedList(fileList);
			if (fileList.size() > 0) {
				download(client, fileList);
			}

			String cmd;

			cmd = "start java -Dfile.encoding=UTF-8 -Dfxms.home=" + file.getPath() + " -jar " + file.getPath()
					+ "/bin/fxms.jar";

			Logger.logger.info(cmd);

			makeBat(cmd);

			cmd = file.getPath() + File.separator + "bin/fxms.bat";

			Runtime.getRuntime().exec(cmd);

			Thread.sleep(500);

			System.exit(0);

		} catch (Exception e1) {
			throw e1;
		}
	}

	/**
	 * 변경된 파일 목록을 제공한다.
	 * 
	 * @param fileList
	 * @return
	 */
	private List<UiFileVo> getChangedList(List<UiFileVo> fileList) {
		List<UiFileVo> list = new ArrayList<UiFileVo>();

		String value;
		UiFileVo orgFile = new UiFileVo();
		for (UiFileVo file : fileList) {
			value = UiData.getConfig(FILE_PREFIX + file.getName(), null);
			if (value == null) {
				list.add(file);
			} else {
				orgFile.setValue(value);
				if (orgFile.getSize() != file.getSize() || orgFile.getLastModified() != file.getLastModified()) {
					list.add(file);
				}
			}
		}

		return list;
	}

	private String getHome() {
		return System.getProperty("user.home") + "/fxms";
	}
	
	
	private void makeBat(String cmd) {

		File file = new File(getHome() + File.separator + "bin/fxms.bat");

		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, false);
			outStream.write(cmd.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	private Node makeBottom() {

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

		return buttonBox;
	}

	private Node makeCenter() {

		String server = UiData.getConfig(UiData.server, null);
		String port = UiData.getConfig(UiData.port, null);

		if (server != null && port != null) {
			try {
				downloadAndStart(server, Integer.valueOf(port));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(32, 24, 24, 24));

		TextField tfIp = new TextField();
		tfIp.setPromptText(Lang.getText(Type.msg, "접속 서버 주소를 입력하세요"));

		TextField tfPort = new TextField();
		tfPort.setPromptText(Lang.getText(Type.msg, "접속 서버 포트번호를 입력하세요"));
		vbox.getChildren().add(tfIp);
		vbox.getChildren().add(tfPort);

		bAccess.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				UiData.setConfig(UiData.server, tfIp.getText());
				UiData.setConfig(UiData.port, tfPort.getText());
				UiData.writeConfig();

				try {
					downloadAndStart(tfIp.getText(), Integer.valueOf(tfPort.getText()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		return vbox;
	}

	private Node makeTop() {
		
		Label title = new Label("Fx Access");
		title.setId("login-title");
		title.setPadding(new Insets(8, 8, 8, 8));
		title.setPrefWidth(WIDTH);

		return title;
	}

}

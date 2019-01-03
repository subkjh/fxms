package FX.MS;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.dx.FxCallback;
import com.fxms.ui.node.dxeditor.DxPane;

import fxms.client.log.LOG_LEVEL;
import fxms.client.log.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * FXMS C/S MAIN START
 * 
 * @author SUBKJH-DEV
 *
 */
public class UI extends Application implements FxCallback<String> {

	public static DxPane dxPane;
	public static Stage primaryStage;

	public static DxPane getDxPane() {
		return dxPane;
	}

	public static void main(String[] args) {

		String folder = UiConfig.getConfig().getHomeLogs();
		Logger.logger = Logger.createLogger(folder, "fxms");
		Logger.logger.setLevel(LOG_LEVEL.trace);

		launch(args);

	}

	@Override
	public void onCallback(String data) {

		FxMain main = new FxMain();
		main.init(CodeMap.getMap().getOpCode(OP_NAME.Main));
		main.initData(null);

	}

	@Override
	public void start(Stage primaryStage) {

		UI.primaryStage = primaryStage;

		UiData.logStart();

		try {
			new LoginStage(primaryStage, this);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
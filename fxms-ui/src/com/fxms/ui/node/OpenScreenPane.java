package com.fxms.ui.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.dx.DxAsyncSelector;

import FX.MS.UiData;
import fxms.parser.TextParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

public class OpenScreenPane extends BorderPane {

	private final ComboBox<String> cbText;

	public OpenScreenPane() {

		// ImageView icon = new ImageView(ImagePointer.getImage("fx-32-32.jpg"));

		cbText = new ComboBox<>();
		cbText.setPrefSize(580, 32);
		cbText.setPromptText("조회할 화면 정보를 입력하세요");
		cbText.setEditable(true);
		List<String> hisList = UiData.getSearchHistory();
		for (String s : hisList) {
			cbText.getItems().add(0, s);
		}
		// text.setFont(new Font(16));

		Button bOpen = new Button("Open");
		bOpen.setPrefHeight(31);
		bOpen.setDefaultButton(true);
		bOpen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				showScreen(cbText.getSelectionModel().getSelectedItem().toString());
			}
		});

		setCenter(cbText);
		setRight(bOpen);
		setAlignment(bOpen, Pos.CENTER_RIGHT);
		setMargin(bOpen, new Insets(1, 1, 1, 1));
	}

	private void showScreen(String text) {

		TextParser parser = new TextParser(text) {
			@Override
			protected List<Map<String, Object>> selectQid(String qid) throws Exception {
				return DxAsyncSelector.getSelector().selectQid("ai-screen/screen-qid.xml", qid);
			}
		};

		parser.show();

		List<Map<String, Object>> screenList = parser.getParameters();
		System.out.println(screenList);

		if (screenList.size() >= 1) {
			Map<String, Object> data;

			if (screenList.size() == 1) {
				data = screenList.get(0);
			} else {
				data = doSelect(screenList);
			}

			if (data != null) {

				Object screen = data.remove("screen");
				if (screen == null) {
					FxAlert.showAlert(FxAlertType.error, this, "error", "어떤 화면인지 데이터가 부족합니다.");
					return;
				}

				UiOpCodeVo opCode = CodeMap.getMap().getOpCode(screen.toString());
				if (opCode != null) {
					opCode.showScreen();
					addToHistory(text);
					return;
				}
				FxAlert.showAlert(FxAlertType.error, this, "error", "등록화면 없음");
			}
		}

	}

	private void addToHistory(String text) {
		if (cbText.getItems().contains(text) == false) {
			cbText.getItems().add(0, text);
			UiData.writeSearch(text, true);
		}
	}

	class Data {
		UiOpCodeVo opCode;
		Map<String, Object> map;

		public String toString() {
			return opCode.getOpName();
		}
	}

	private Map<String, Object> doSelect(List<Map<String, Object>> screenList) {
		List<Data> choices = new ArrayList<>();
		UiOpCodeVo opCode;
		Object screen;
		for (Map<String, Object> map : screenList) {
			screen = map.get("screen");
			if (screen != null) {
				opCode = CodeMap.getMap().getOpCode(screen.toString());
				if (opCode != null) {
					Data data = new Data();
					data.opCode = opCode;
					data.map = map;
					choices.add(data);
				}
			}
		}

		ChoiceDialog<Data> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Choice Screen");
		dialog.setHeaderText("Look, a Choice Dialog");
		dialog.setContentText("Choose screen :");

		Optional<Data> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get().map;
		}

		return null;

	}
}

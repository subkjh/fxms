package com.fxms.ui.dx.item;

import com.fxms.ui.bas.property.ProgressIndicator;

import FX.MS.UiData;
import FX.MS.UI;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class DxItemInformation extends Label implements ProgressIndicator {

	class Remover extends Thread {

		static final long SHOW_TIME = 5000;
		long mstime = SHOW_TIME;

		public void run() {

			while (mstime > 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mstime -= 100;
			}

			Platform.runLater(new Runnable() {
				public void run() {
					DxItemInformation.getInformation().remove();
				}
			});
		}

	}

	private static DxItemInformation information;

	public static DxItemInformation getInformation() {

		if (information == null) {
			information = new DxItemInformation();
		}

		return information;
	}

	private Remover remover;
	private int size;

	public DxItemInformation() {
		getStyleClass().add("dx-message");
		setWrapText(true);
		setLayoutX(5);
	}

	public void remove() {
		remover = null;
		if (UI.getDxPane().getMainPane().getChildren().contains(DxItemInformation.getInformation())) {
			Platform.runLater(new Runnable() {
				public void run() {
					UI.getDxPane().getMainPane().getChildren().remove(DxItemInformation.getInformation());

				}
			});
		}
	}

	@Override
	public void setSize(int size) {
		this.size = size;
	}

	public void showInformation(String value) {

		Platform.runLater(new Runnable() {
			public void run() {
				if (UI.getDxPane().getMainPane().getChildren().contains(DxItemInformation.getInformation()) == false) {
					UI.getDxPane().getMainPane().getChildren().add(DxItemInformation.getInformation());
				}
				
				DxItemInformation.getInformation().setLayoutY(UI.getDxPane().getHeight()-30);

				setText(value);
				
				UiData.log(UiData.UI_LOG_LEVEL.info, value);

				if (remover == null) {
					remover = new Remover();
					remover.start();
				}
			}
		});

	}

	@Override
	public void showMsg(int index, String msg) {
		showInformation(index + "/" + size + " " + msg);
	}

}

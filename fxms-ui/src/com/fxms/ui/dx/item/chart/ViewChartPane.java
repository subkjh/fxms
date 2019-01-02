package com.fxms.ui.dx.item.chart;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.code.UiChartVo;
import com.fxms.ui.bas.editor.FxEditor;
import com.fxms.ui.bas.editor.ListEditor;
import com.fxms.ui.bas.editor.MoTreeEditor;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.lang.Lang.Type;
import com.fxms.ui.bas.mo.ContainerMo;
import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.pane.SearchFilterPane;
import com.fxms.ui.bas.vo.Attr;
import com.fxms.ui.dx.FxCallback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public class ViewChartPane extends BorderPane {

	private TabPane chartBox = new TabPane();
	private SearchFilterPane searchPane = new SearchFilterPane();
	private Mo mo;
	private ListEditor chartCombo;

	public ViewChartPane() {

		chartCombo = (ListEditor) FxEditor.makeEditor("chart", FxEditor.EDITOR_TYPE.List.name(), null, "조회할 차트를 선택하세요",
				0, null);

		MoTreeEditor moTree = (MoTreeEditor) FxEditor.makeEditor("moNo", FxEditor.EDITOR_TYPE.MoTree.name(),
				ContainerMo.MO_CLASS, "조회할 관리대상을 선택하세요", 0, null);

		moTree.setCallback(new FxCallback<Mo>() {

			@Override
			public void onCallback(Mo data) {

				boolean isSameMoClass = (mo != null && mo.getMoClass().equals(data.getMoClass())
						&& mo.getMoType().equals(data.getMoType()));

				mo = data;

				if (isSameMoClass == false) {
					List<UiChartVo> chartList = CodeMap.getMap().getChartList(data);
					List<Attr> attrList = new ArrayList<Attr>();
					for (UiChartVo vo : chartList) {
						attrList.add(new Attr(vo.getChartName(), vo.getChartName(), vo));
					}
					chartCombo.initAttrList(attrList);
					chartCombo.select(0);
				}
			}
		});

		searchPane.addEditor(moTree);
		searchPane.addEditor(chartCombo);
		searchPane.addButton(getSearchButton());
		searchPane.addButton(getClearButton());

		chartBox.setPadding(new Insets(8, 8, 8, 8));

		setTop(searchPane);
		setCenter(chartBox);

		double height = Screen.getPrimary().getVisualBounds().getHeight() * .75;
		setMinSize(height * 1.6181, height);
	}

	private Button getClearButton() {
		Button bClear = new Button("챠트지우기");
		bClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				chartBox.getTabs().clear();
			}
		});

		return bClear;
	}

	private Button getSearchButton() {
		Button bSearch = new Button("조회");
		bSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				Attr attr = chartCombo.getSelectionModel().getSelectedItem();

				if (mo == null || attr == null) {
					FxStage.showMessage(ViewChartPane.this, true, Lang.getText(Type.msg, "입력 조건을 확인하세요"));
					return;
				}

				UiChartVo chart = (UiChartVo) attr.getUserData();

				FxChart node = chart.makeChart();

				Tab tab = new Tab();
				tab.setClosable(true);
				tab.setText("'" + mo.getMoAname() + "' " + chart.getChartName());
				tab.setContent((Pane) node);
				chartBox.getTabs().add(0, tab);
				chartBox.getSelectionModel().select(0);

				node.viewChart(mo, chart);

			}
		});

		return bSearch;
	}
}

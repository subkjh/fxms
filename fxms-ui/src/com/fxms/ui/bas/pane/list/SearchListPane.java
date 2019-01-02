package com.fxms.ui.bas.pane.list;

import java.util.Map;

import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.pane.SearchFilterPane;
import com.fxms.ui.bas.property.FxUi;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SearchListPane extends BorderPane implements FxUi {

	private final SearchFilterPane filterPane;
	private final ListPaneBase listPane;
	private UiOpCodeVo opCode;

	public SearchListPane() {

		listPane = new ListPaneBase() {
			@Override
			protected Map<String, Object> getSearchParameters() {
				Map<String, Object> p = super.getSearchParameters();
				p.putAll(filterPane.getInputData());
				return p;
			}

			@Override
			protected void setDisable2Search(boolean value) {
				super.setDisable2Search(value);
				filterPane.setDisable(value);
			}

		};

		filterPane = new SearchFilterPane();
		filterPane.addButton(getSearchButton());
		filterPane.addButton(getInitButton());

		setPadding(new Insets(5, 5, 5, 5));
		setTop(filterPane);
		setCenter(listPane);

	}

	private Button getInitButton() {
		Button bSearch = new Button(Lang.getText(Lang.Type.button, "Clear"));
		bSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				filterPane.clearEditor();

				if (listPane != null) {
					listPane.doClear();
				}
			}
		});

		return bSearch;
	}

	private Button getSearchButton() {
		Button bSearch = new Button(Lang.getText(Lang.Type.button, "Search"));
		bSearch.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (listPane != null) {
					listPane.doSearch();
				}
			}
		});

		return bSearch;
	}

	public ListPaneBase getListPane() {
		return listPane;
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	@Override
	public void init(UiOpCodeVo opCode) {

		this.opCode = opCode;
		listPane.init(opCode);
		filterPane.makeFilters(opCode);

	}

	@Override
	public void initData(Map<String, Object> data) {
		filterPane.initData(data);
		listPane.initData(data);
	}

}

package com.fxms.ui.bas.pane.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.UiCode;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.code.UiOpCodeVo;
import com.fxms.ui.bas.property.FxUiList;
import com.fxms.ui.bas.renderer.column.TableColumnData;
import com.fxms.ui.bas.renderer.column.TableColumnData.TYPE;
import com.fxms.ui.bas.vo.ListData;
import com.fxms.ui.biz.action.FxContextMenu;
import com.fxms.ui.dx.DxAsyncSelector;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.dx.FxCallback;

import FX.MS.UiConfig;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.stage.Window;

public class ListPaneBase extends ScrollPane implements FxUiList {

	private final List<TableColumnData> columnList;
	private long endTime;
	private UiOpCodeVo opCode;
	private Map<String, Object> parameters;
	private DxCallback searchCallback;
	private long startTime;
	private final TableView<Object[]> table;

	public ListPaneBase() {

		columnList = new ArrayList<TableColumnData>();
		table = new TableView<>();

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				onChangedSelection(makeMap(oldSelection), makeMap(newSelection));
			}
		});

		setContent(table);
		setFitToHeight(true);
		setFitToWidth(true);
	}

	public void addData(int index, Object element[]) {
		table.getItems().add(index, element);
		table.refresh();
	}

	public void doClear() {
		table.getItems().clear();

		onChangedSelection(null, null);

		showMessage("화면 초기화 되었습니다.");
	}

	@Override
	public void doSearch() {

		setDisable2Search(true);
		showMessage("조회중입니다." + "..");
		
		startTime = System.currentTimeMillis();

		try {
			
			Map<String, Object> newPara = opCode.getSearchMap(getSearchParameters());
			
			DxAsyncSelector.getSelector().selectQid(newPara, new FxCallback<ListData>() {
				@Override
				public void onCallback(ListData data) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							endTime = System.currentTimeMillis();
							showData(data);
							setDisable2Search(false);
						}
					});
				}
			});

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public UiOpCodeVo getOpCode() {
		return opCode;
	}

	public Map<String, Object> getDefaultData() {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		return parameters;
	}

	public DxCallback getSearchCallback() {

		if (searchCallback == null) {
			searchCallback = new DxCallback() {
				@Override
				public void onCallback(Map<String, Object> data) {
					doSearch();
				}

			};
		}
		return searchCallback;
	}

	public Map<String, Object> getSelectedData() {
		Object[] data = table.getSelectionModel().getSelectedItem();
		if (data == null) {
			return null;
		}

		return makeMap(data);
	}

	@SuppressWarnings("unchecked")
	public FxContextMenu<Map<String, Object>> getTableContextMenu() {
		if (table.getContextMenu() == null) {
			table.setContextMenu(new FxContextMenu<Map<String, Object>>());
		}

		return (FxContextMenu<Map<String, Object>>) table.getContextMenu();
	}

	public TableView<Object[]> getTableView() {
		return table;
	}

	@Override
	public void init(UiOpCodeVo opcode) {

		this.opCode = opcode;

		if (opCode == null) {
			table.setPlaceholder(new Label("무엇을 조회할 지 알 수 없습니다."));
			return;
		}

		FxContextMenu<Map<String, Object>> menu = opCode.makeContextMenu(this, getSearchCallback());
		if (menu != null) {
			setTableContextMenu(menu);
		}
	}

	@Override
	public void initData(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public final void showData(ListData dataList) {

		if (table.getColumns().size() == 0) {
			setColumns(dataList);
		}

		table.getItems().clear();

		if (dataList.getErrmsg() != null) {
			showMessage(dataList.getErrmsg());
			return;
		}

		showMessage(UiCode.getDate(endTime) + " selected, " + (endTime - startTime) + " ms, "
				+ dataList.getDataList().size() + " datas");

		table.getItems().addAll(dataList.getDataList());
		table.refresh();
	}

	private Map<String, Object> makeMap(Object[] data) {
		if (data == null) {
			return null;
		}

		Map<String, Object> ret = new HashMap<String, Object>();

		for (int i = 0; i < columnList.size(); i++) {
			ret.put(columnList.get(i).getName(), data[i]);
		}

		return ret;
	}

	private void setColumns(ListData dataList) {

		String name;
		TableColumnData colData;
		int width = 0;

		for (int index = 0; index < dataList.getColumns().length; index++) {

			name = dataList.getColumns()[index];

			colData = new TableColumnData(dataList.getQid(), name);

			columnList.add(colData);

			if (colData.getType() == TYPE.hidden) {
				continue;
			}

			width += colData.getLength();

			table.getColumns().add(colData.makeTableColumn(index));
		}

		if (opCode.getUiWidth() != width + 30) {
			opCode.setUiWidth(width + 30);
			
			UiConfig.getConfig().updateUiSize(opCode.getOpNo(), opCode.getUiWidth(), -1);

		}

	}

	protected Map<String, Object> getSearchParameters() {

		Map<String, Object> p = new HashMap<String, Object>();

		p.putAll(opCode.getConstMap());

		if (this.parameters != null) {
			p.putAll(this.parameters);
		}

		return p;
	}

	protected void onChangedSelection(Map<String, Object> oldMap, Map<String, Object> newMap) {

		System.out.println("ListPane :: " + newMap);

		getTableContextMenu().onSelectedData(newMap);
	}

	protected void setDisable2Search(boolean value) {
		setDisable(value);
	}

	protected void showMessage(String s) {
		if (getScene() != null) {
			Window window = getScene().getWindow();
			if (window instanceof FxStage) {
				FxStage stage = (FxStage) window;
				stage.showMsg(s);
			}
		}
	}

	void setTableContextMenu(FxContextMenu<Map<String, Object>> contextMenu) {
		if (table != null && contextMenu != null) {
			table.setContextMenu(contextMenu);
		}
	}

}

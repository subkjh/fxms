package com.fxms.ui.bas.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.OP_TYPE;
import com.fxms.ui.bas.FxAlert;
import com.fxms.ui.bas.FxAlert.FxAlertType;
import com.fxms.ui.bas.FxDialog;
import com.fxms.ui.bas.FxStage;
import com.fxms.ui.bas.editor.FxEditor;
import com.fxms.ui.bas.editor.FxEditor.EDITOR_TYPE;
import com.fxms.ui.bas.property.FxUi;
import com.fxms.ui.bas.property.FxUiChild;
import com.fxms.ui.bas.property.FxUiList;
import com.fxms.ui.biz.action.FxContextMenu;
import com.fxms.ui.biz.action.FxDataMenuItem;
import com.fxms.ui.biz.action.FxMenuItem;
import com.fxms.ui.dx.DxCallback;
import com.fxms.ui.dx.FxCallfront;
import com.fxms.ui.dx.confirm.Confirmer;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import subkjh.lib.compiler.eval.Eval;

public class UiOpCodeVo {

	public static void main(String[] args) {
		String str = "now() - 100";

		System.out.println(str.contains("now()"));
		System.out.println(str.replaceAll("now\\(\\)", System.currentTimeMillis() + ""));

		try {
			System.out.println(new Eval().compute("1000 - 100", null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static UiOpCodeVo merge(UiOpCodeVo op1, UiOpCodeVo op2) {
		UiOpCodeVo op = new UiOpCodeVo();
		op.setOpTitle(op1.getOpTitle());
		op.getChildren().addAll(op1.getChildren());
		op.getChildren().addAll(op2.getChildren());

		return op;
	}

	private String opTypeText;

	private List<UiOpCodeAttrVo> children = null;

	private String errMsg;

	private List<UiOpCodeMenu> menuList = null;

	private String mngDiv;

	private String okMsg;

	private String opHint;

	private String opDescr;

	private String opMethod;

	private String opName;

	private int opNo;

	private String opTitle;

	private String confirmMsg;

	private int opType;

	private String opUri;

	private int seqBy = 0;

	private int ugrpNo;

	private int uiHeight;

	private String uiJavaClass;

	private int uiWidth;

	private int upperOpNo;

	public UiOpCodeVo() {
	}

	public UiOpCodeAttrVo getAttr(String name) {
		for (UiOpCodeAttrVo attr : getChildren()) {
			if (attr.getAttrName().equals(name)) {
				return attr;
			}
		}

		return null;
	}

	public List<String> getAttrGroupList() {
		List<String> list = new ArrayList<String>();

		if (children != null) {
			for (UiOpCodeAttrVo attr : children) {
				if (attr.getAttrGroup() != null && list.contains(attr.getAttrGroup()) == false) {
					list.add(attr.getAttrGroup());
				}
			}
		}

		return list;
	}

	public List<UiOpCodeAttrVo> getChildren() {
		if (children == null) {
			children = new ArrayList<UiOpCodeAttrVo>();
		}
		return children;
	}

	public List<UiOpCodeAttrVo> getChildren(FxEditor.EDITOR_TYPE type) {
		List<UiOpCodeAttrVo> list = new ArrayList<UiOpCodeAttrVo>();

		if (children != null) {
			for (UiOpCodeAttrVo attr : children) {
				if (attr.getAttrType().equals(type.name())) {
					list.add(attr);
				}
			}
		}

		return list;
	}

	public List<UiOpCodeAttrVo> getChildren(String groupName) {
		List<UiOpCodeAttrVo> list = new ArrayList<UiOpCodeAttrVo>();

		if (children != null) {
			for (UiOpCodeAttrVo attr : children) {
				if (groupName == null || groupName.equals(attr.getAttrGroup())) {
					list.add(attr);
				}
			}
		}

		return list;
	}

	public String getConfirmMsg() {
		return confirmMsg;
	}

	/**
	 * 
	 * @return 상수 맵
	 */
	public Map<String, Object> getConstMap() {

		Map<String, Object> map = new HashMap<String, Object>();
		if (children == null) {
			return map;
		}
		Object value;

		for (UiOpCodeAttrVo vo : children) {

			value = vo.getAttrDefaultValue();
			if (value == null) {
				continue;
			}

			if (vo.getAttrType().equalsIgnoreCase(EDITOR_TYPE.Const.name())) {
				map.put(vo.getAttrName(), value);
			}
		}
		return map;

	}

	public String getErrMsg() {
		return errMsg;
	}

	public List<UiOpCodeMenu> getMenuList() {
		if (menuList == null) {
			menuList = new ArrayList<UiOpCodeMenu>();
		}

		return menuList;
	}

	/**
	 * 관리구분
	 * 
	 * @return 관리구분
	 */
	public String getMngDiv() {
		return mngDiv;
	}

	public String getOkMsg() {
		return okMsg;
	}

	/**
	 * 기능설명
	 * 
	 * @return 기능설명
	 */
	public String getOpDescr() {
		return opDescr;
	}

	public String getOpHint() {
		return opHint;
	}

	/**
	 * 기능메소드
	 * 
	 * @return 기능메소드
	 */
	public String getOpMethod() {
		return opMethod;
	}

	/**
	 * 기능명
	 * 
	 * @return 기능명
	 */
	public String getOpName() {
		return opName;
	}

	/**
	 * 기능번호
	 * 
	 * @return 기능번호
	 */
	public int getOpNo() {
		return opNo;
	}

	public String getOpTitle() {
		return opTitle == null || opTitle.trim().length() == 0 ? opName : opTitle;
	}

	/**
	 * 기능구분
	 * 
	 * @return 기능구분
	 */
	public int getOpType() {
		return opType;
	}

	public String getOpTypeText() {
		return opTypeText;
	}

	/**
	 * 기능URI
	 * 
	 * @return 기능URI
	 */
	public String getOpUri() {
		return opUri;
	}

	/**
	 * 
	 * @return 검색에 필요한 맵
	 */
	public Map<String, Object> getSearchMap(Map<String, Object> org) {

		Map<String, Object> map = new HashMap<String, Object>();
		if (children == null) {
			return map;
		}

		Object data;

		for (UiOpCodeAttrVo vo : children) {
			data = org.get(vo.getAttrName());
			if (data != null)
				map.put(vo.getAttrName(), data);
		}

		return map;

	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public int getSeqBy() {
		return seqBy;
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @return 운용자그룹번호
	 */
	public int getUgrpNo() {
		return ugrpNo;
	}

	public double getUiHeight() {
		if (uiHeight <= 0 && uiWidth > 0) {
			double ret = uiWidth / 1.6181;
			return ret > 600 ? 600 : ret;
		}
		return uiHeight;
	}

	public String getUiJavaClass() {
		return uiJavaClass;
	}

	public double getUiWidth() {

		double width;

		if (uiWidth <= 0 && uiHeight > 0) {
			width = (uiHeight * 1.6181);
		} else {
			width = uiWidth;
		}

		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

		return (int) (bounds.getWidth() - 30 < width ? bounds.getWidth() - 30 : width);
	}

	/**
	 * 상위기능번호
	 * 
	 * @return 상위기능번호
	 */
	public int getUpperOpNo() {
		return upperOpNo;
	}

	public boolean isCall() {
		return opUri != null && opUri.length() > 0 && opMethod != null && opMethod.length() > 0;
	}

	/**
	 * 이 메뉴가 가지는 콘텍스메뉴를 생성한다.
	 * 
	 * @param parent
	 * @param callback
	 * @return
	 */
	public FxContextMenu<Map<String, Object>> makeContextMenu(Node parent, DxCallback callback) {

		if (menuList == null || menuList.size() == 0) {
			return null;
		}

		FxContextMenu<Map<String, Object>> contextMenu = new FxContextMenu<Map<String, Object>>();
		UiOpCodeVo menuOpCode;

		for (UiOpCodeMenu vo : menuList) {
			if (vo.getMenuOpNo() == 0) {
				contextMenu.getItems().add(new SeparatorMenuItem());
			} else {
				menuOpCode = CodeMap.getMap().getOpCode(vo.getMenuOpNo());
				if (menuOpCode != null) {

					FxMenuItem menuItem;

					menuItem = new FxDataMenuItem(parent, menuOpCode, callback) {
						@Override
						public void onSelectedData(Map<String, Object> data) {

							super.onSelectedData(data);

							if (vo.isEnable(data) == false) {
								setDisable(true);
							}

						}
					};

					contextMenu.getItems().add(menuItem);

				}
			}
		}

		return contextMenu;
	}

	public Map<String, Object> makeParameters(String name, Object value, Object... parameters) {

		Map<String, Object> para = new HashMap<String, Object>();
		para.put(name, value);

		try {
			for (int i = 0; i < parameters.length; i += 2) {
				para.put(parameters[i].toString(), parameters[i + 1]);
			}
			para.putAll(getConstMap());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return para;
	}

	public Object makeScreen() {

		if (getUiJavaClass() != null && getUiJavaClass().length() > 0) {

			try {
				Object obj = Class.forName(getUiJavaClass()).newInstance();
				if (obj instanceof Parent) {
					setSize((Parent) obj);
				}
				return obj;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			return null;
		} else {
			System.err.println("화면 클래스가 정의되지 않았습니다.");
			return null;
		}
	}

	public void setConfirmMsg(String confirmMsg) {
		this.confirmMsg = confirmMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * 관리구분
	 * 
	 * @param mngDiv
	 *            관리구분
	 */
	public void setMngDiv(String mngDiv) {
		this.mngDiv = mngDiv;
	}

	public void setOkMsg(String okMsg) {
		this.okMsg = okMsg;
	}

	/**
	 * 기능설명
	 * 
	 * @param opDescr
	 *            기능설명
	 */
	public void setOpDescr(String opDescr) {
		this.opDescr = opDescr;
	}

	public void setOpHint(String opHint) {
		this.opHint = opHint;
	}

	/**
	 * 기능메소드
	 * 
	 * @param opMethod
	 *            기능메소드
	 */
	public void setOpMethod(String opMethod) {
		this.opMethod = opMethod;
	}

	/**
	 * 기능명
	 * 
	 * @param opName
	 *            기능명
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * 기능번호
	 * 
	 * @param opNo
	 *            기능번호
	 */
	public void setOpNo(int opNo) {
		this.opNo = opNo;
	}

	public void setOpTitle(String opTitle) {
		this.opTitle = opTitle;
	}

	/**
	 * 기능구분
	 * 
	 * @param opType
	 *            기능구분
	 */
	public void setOpType(int opType) {
		this.opType = opType;
	}

	public void setOpTypeText(String opTypeText) {
		this.opTypeText = opTypeText;
	}

	/**
	 * 기능URI
	 * 
	 * @param opUri
	 *            기능URI
	 */
	public void setOpUri(String opUri) {
		this.opUri = opUri;
	}

	/**
	 * 정렬순서
	 * 
	 * @param seqBy
	 *            정렬순서
	 */
	public void setSeqBy(int seqBy) {
		this.seqBy = seqBy;
	}

	public void setSize(Node node) {
		if (node instanceof Region) {
			Region r = (Region) node;

			if (getUiWidth() <= 0 || getUiHeight() <= 0) {
				r.setMinSize(1.6181 * 240, 240);
				return;
			}

			r.setPrefSize(getUiWidth(), getUiHeight());
		}
	}

	/**
	 * 운용자그룹번호
	 * 
	 * @param ugrpNo
	 *            운용자그룹번호
	 */
	public void setUgrpNo(int ugrpNo) {
		this.ugrpNo = ugrpNo;
	}

	public void setUiHeight(int uiHeight) {
		this.uiHeight = uiHeight;
	}

	public void setUiJavaClass(String uiJavaClass) {
		this.uiJavaClass = uiJavaClass;
	}

	public void setUiWidth(int uiWidth) {
		this.uiWidth = uiWidth;
	}

	/**
	 * 상위기능번호
	 * 
	 * @param upperOpNo
	 *            상위기능번호
	 */
	public void setUpperOpNo(int upperOpNo) {
		this.upperOpNo = upperOpNo;
	}

	public void showDialog(Node parent, Map<String, Object> initData, FxCallfront callfront, DxCallback callback) {

		Object screen = makeScreen();

		if (screen instanceof Confirmer) {

			Confirmer c = (Confirmer) screen;
			c.confirm(parent, this, initData, callback);

		} else if (screen instanceof FxUi) {

			Node node = (Node) screen;
			((FxUi) node).init(this);
			((FxUi) node).initData(initData);

			String opText = null;
			if (getOpType() == OP_TYPE.add.getCode() //
					|| getOpType() == OP_TYPE.update.getCode() //
					|| getOpType() == OP_TYPE.delete.getCode()) {
				opText = getOpTypeText();
			}

			FxDialog.showDialog(parent, node, this, opText, "Close", callback);

		} else {

			FxAlert.showAlert(FxAlertType.error, parent, getOpTitle(), "화면이 정의되지 않았습니다.");

		}

	}

	public void showScreen() {
		showScreen(null, null, null, null);
	}

	/**
	 * 
	 * @param parent
	 * @param initData
	 * @param callfront
	 * @param callback
	 */
	public void showScreen(Node parent, Map<String, Object> initData, FxCallfront callfront, DxCallback callback) {

		Object screen = makeScreen();

		if (screen instanceof FxUiChild) {
			((FxUiChild) screen).setFxUiParent(parent);
		}

		if (screen instanceof Confirmer) {

			Confirmer c = (Confirmer) screen;
			c.confirm(parent, this, initData, callback);

		} else if (screen instanceof FxUi) {

			Node node = (Node) screen;
			((FxUi) node).init(this);
			((FxUi) node).initData(initData);

			String opText = null;
			if (getOpType() == OP_TYPE.add.getCode() //
					|| getOpType() == OP_TYPE.update.getCode() //
					|| getOpType() == OP_TYPE.delete.getCode()) {
				opText = getOpTypeText();
			}

			if (getOpType() == OP_TYPE.ui.getCode()) {
				// UI이면 독립 윈도우로 보인다.
				FxStage.showDialog(null, node, this, opText, callfront, callback);
			} else {
				FxStage.showDialog(parent, node, this, opText, callfront, callback);
			}

			if (screen instanceof FxUiList) {
				((FxUiList) screen).doSearch();
			}

		} else {

			FxAlert.showAlert(FxAlertType.error, parent, getOpTitle(), "화면이 정의되지 않았습니다.");

		}

	}

	public String toString() {
		return opName;
	}

}

package com.fxms.ui.bas.code;

import com.fxms.ui.bas.editor.FxEditor;
import com.fxms.ui.bas.editor.FxEditor.EDITOR_TYPE;
import com.fxms.ui.bas.renderer.FxRenderer;

import fxms.client.FxmsClient;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import subkjh.lib.compiler.eval.Eval;

public class UiOpCodeAttrVo {

	private String attrDefaultValue;

	private String attrDisp;

	private String attrGroup;

	private String attrName;

	private String attrType;

	private String attrValueList;

	private int attrWidth = 0;

	private boolean nullableYn;

	private Integer opNo;

	private String promptText;

	private boolean readOnlyYn;

	private Integer seqBy = 0;

	public boolean isEditable() {
		if (attrType.equalsIgnoreCase(EDITOR_TYPE.var.name()) //
				|| attrType.equalsIgnoreCase(EDITOR_TYPE.Const.name())) {
			return false;
		}

		return true;
	}

	public UiOpCodeAttrVo() {
	}

	/**
	 * 속성기본값
	 * 
	 * @return 속성기본값
	 */
	public String getAttrDefaultValue() {

		if (attrDefaultValue == null) {
			return null;
		}

		String value = attrDefaultValue;

		if (value.contains("now()")) {
			value = value.replaceAll("now\\(\\)", System.currentTimeMillis() + "");
			try {
				Number time = new Eval().compute(value, null);
				return String.valueOf(FxmsClient.getDate(time.longValue()));
			} catch (Exception e) {
				return null;
			}
		} else {
			return value;
		}

	}

	/**
	 * 속성화면명
	 * 
	 * @return 속성화면명
	 */
	public String getAttrDisp() {
		return attrDisp;
	}

	/**
	 * 속성그룹명
	 * 
	 * @return 속성그룹명
	 */
	public String getAttrGroup() {
		return attrGroup;
	}

	/**
	 * 속성명
	 * 
	 * @return 속성명
	 */
	public String getAttrName() {
		return attrName;
	}

	/**
	 * 속성종류
	 * 
	 * @return 속성종류
	 */
	public String getAttrType() {
		return attrType;
	}

	/**
	 * 속성값목록
	 * 
	 * @return 속성값목록
	 */
	public String getAttrValueList() {
		return attrValueList;
	}

	public int getAttrWidth() {
		return attrWidth;
	}

	/**
	 * 기능번호
	 * 
	 * @return 기능번호
	 */
	public Integer getOpNo() {
		return opNo;
	}

	public String getPromptText() {
		return promptText;
	}

	/**
	 * 정렬순서
	 * 
	 * @return 정렬순서
	 */
	public Integer getSeqBy() {
		return seqBy;
	}

	public boolean isNullableYn() {
		return nullableYn;
	}

	public boolean isReadOnlyYn() {
		return readOnlyYn;
	}

	public Node makeEditor() {

		if (isEditable() == false) {
			return null;
		}

		Node editor = FxEditor.makeEditor(attrName, attrType, attrValueList, promptText,
				attrWidth <= 0 ? 200 : attrWidth, getAttrDefaultValue());

		if (editor == null) {
			return null;
		}

		if (isReadOnlyYn()) {
			if (editor instanceof TextField) {
				((TextField) editor).setEditable(false);
			} else {
				editor.setDisable(true);
			}
		}

		editor.setUserData(this);

		return editor;
	}

	public Node makeRenderer() {

		Node editor = FxRenderer.makeRenderer(attrType);

		if (editor == null) {
			return null;
		}

		return editor;
	}

	/**
	 * 속성기본값
	 * 
	 * @param attrDefaultValue
	 *            속성기본값
	 */
	public void setAttrDefaultValue(String attrDefaultValue) {
		this.attrDefaultValue = attrDefaultValue;
	}

	/**
	 * 속성화면명
	 * 
	 * @param attrDisp
	 *            속성화면명
	 */
	public void setAttrDisp(String attrDisp) {
		this.attrDisp = attrDisp;
	}

	/**
	 * 속성그룹명
	 * 
	 * @param attrGroup
	 *            속성그룹명
	 */
	public void setAttrGroup(String attrGroup) {
		this.attrGroup = attrGroup;
	}

	/**
	 * 속성명
	 * 
	 * @param attrName
	 *            속성명
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	/**
	 * 속성종류
	 * 
	 * @param attrType
	 *            속성종류
	 */
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}

	/**
	 * 속성값목록
	 * 
	 * @param attrValueList
	 *            속성값목록
	 */
	public void setAttrValueList(String attrValueList) {
		this.attrValueList = attrValueList;
	}

	public void setAttrWidth(int attrWidth) {
		this.attrWidth = attrWidth;
	}

	public void setNullableYn(boolean nullableYn) {
		this.nullableYn = nullableYn;
	}

	/**
	 * 기능번호
	 * 
	 * @param opNo
	 *            기능번호
	 */
	public void setOpNo(Integer opNo) {
		this.opNo = opNo;
	}

	public void setPromptText(String promptText) {
		this.promptText = promptText;
	}

	public void setReadOnlyYn(boolean readOnlyYn) {
		this.readOnlyYn = readOnlyYn;
	}

	/**
	 * 정렬순서
	 * 
	 * @param seqBy
	 *            정렬순서
	 */
	public void setSeqBy(Integer seqBy) {
		this.seqBy = seqBy;
	}
}

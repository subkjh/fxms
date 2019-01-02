package com.fxms.ui.bas.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxms.ui.bas.code.UiCodeVo;
import com.fxms.ui.bas.vo.Attr;

import javafx.scene.Node;

public interface FxEditor {

	public enum EDITOR_TYPE {
		var, Const, List, Qid, MoTree, Code;
	}

	/**
	 * 에디터를 기본 설정. 에디터 생성후 호출됨
	 * 
	 * @param attrValueList
	 *            기본값
	 * @param promptText
	 *            프롬프트 문자열
	 * @param width
	 *            길이
	 */
	public void init(String attrValueList, String promptText, int width);

	/**
	 * 데이터 초기화
	 */
	public void clearEditor();

	/**
	 * 
	 * @return 입력/선택된 값
	 */
	public String getAttrId();

	/**
	 *
	 * @param id
	 *            선택할 값
	 * @param objectData
	 *            참고용 데이터
	 */
	public void setAttrId(Object id, Map<String, Object> objectData);

	public static Node makeEditor(String attrName, String attrType, String attrValueList, String promptText, int width,
			String attrDefaultValue) {

		String className = FxEditor.class.getPackage().getName() + "." + attrType + "Editor";

		FxEditor editor = null;
		try {
			Object obj = Class.forName(className).newInstance();
			if (obj instanceof FxEditor) {
				editor = (FxEditor) obj;
			}
		} catch (Exception e) {
		}

		if (editor == null) {
			editor = new TextEditor();
		}

		editor.init(attrValueList, promptText, width);

		if (attrDefaultValue != null && attrDefaultValue.trim().length() > 0) {
			editor.setAttrId(attrDefaultValue, null);
		}

		((Node) editor).setId(attrName);

		return (Node) editor;

	}

	public static List<UiCodeVo> convert(List<Attr> attrList) {
		List<UiCodeVo> codeList = new ArrayList<UiCodeVo>();
		UiCodeVo e;
		for (Attr attr : attrList) {
			e = new UiCodeVo();
			e.setCdCode(attr.getAttrId());
			e.setCdName(attr.getAttrText());
			codeList.add(e);
		}

		return codeList;
	}

	public static List<Attr> makeAttrList(String line) {

		Map<String, String> map = makeMap(line);

		List<Attr> attrList = new ArrayList<Attr>();

		for (String key : map.keySet()) {
			attrList.add(new Attr(key, map.get(key), null));
		}

		return attrList;
	}

	public static Map<String, String> makeMap(String query) {

		Map<String, String> map = new HashMap<String, String>();

		if (query == null || query.trim().length() == 0) {
			return map;
		}

		if (query.indexOf(':') > 0 && query.indexOf('~') > 0) {
			float unit = Float.parseFloat(query.substring(0, query.indexOf(':')));
			float startNo = Float.parseFloat(query.substring(query.indexOf(':') + 1, query.indexOf('~')));
			float endNo = Float.parseFloat(query.substring(query.indexOf('~') + 1));

			for (float i = startNo; i <= endNo; i += unit) {
				map.put(String.valueOf(i), String.valueOf(i));
			}
		} else {

			String ss[] = query.split(",|&");
			for (String s : ss) {
				String nv[] = s.split("=");
				if (nv.length == 2) {
					map.put(nv[0].trim(), nv[1].trim());
				} else if (nv.length == 1) {
					map.put(nv[0].trim(), nv[0].trim());
				}
			}
		}

		return map;
	}

	public static Map<String, Object> parseQuery(String query) {

		Map<String, Object> map = new HashMap<String, Object>();

		String ss[] = query.split("&");
		for (String s : ss) {
			String nv[] = s.split("=");
			if (nv.length == 2) {
				map.put(nv[0].trim(), nv[1].trim());
			} else if (nv.length == 1) {
				map.put(nv[0].trim(), nv[0].trim());
			}
		}

		return map;
	}

	public static void main(String[] args) {
		String qid = "AAA?moClass=CONTAINER&type=aa";
		String query = null;

		int pos = qid.indexOf('?');

		if (pos > 0) {
			query = qid.substring(pos + 1);
			qid = qid.substring(0, pos);
		}

		System.out.println(qid + "\t" + query);
		System.out.println(FxEditor.parseQuery(query));
	}
}

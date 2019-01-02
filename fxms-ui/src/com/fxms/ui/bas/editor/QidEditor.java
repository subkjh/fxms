package com.fxms.ui.bas.editor;

import java.util.Map;

import com.fxms.ui.bas.code.CodeMap;
import com.fxms.ui.bas.vo.Attr;

public class QidEditor extends AttrComboBox {

	@Override
	public void initList(String attrValueList) {

		getItems().clear();

		String ss[] = attrValueList.split(";");
		String qid = ss[ss.length - 1].trim();

		if (ss.length > 1) {
			getItems().addAll(FxEditor.makeAttrList(ss[0]));
		}

		int pos = qid.indexOf('?');
		Map<String, Object> para = null;
		if (pos > 0) {
			String query = qid.substring(pos + 1);
			qid = qid.substring(0, pos);
			para = FxEditor.parseQuery(query);
		}

		for (Attr attr : CodeMap.getMap().getAttrList(qid, para)) {
			getItems().add(attr);
		}
	}

}

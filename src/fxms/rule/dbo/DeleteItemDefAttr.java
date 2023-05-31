package fxms.rule.dbo;

import subkjh.dao.def.FxColumn;
import subkjh.dao.def.FxIndex;
import subkjh.dao.def.FxTable;
import subkjh.dao.def.Index.INDEX_TYPE;

/**
 * FX_BR_ITEM_DEF_ATTR에서 룰 속성 삭제용
 * 
 * @since 2023.01.26 16:58
 * @author subkjh autometic create by subkjh.dao
 *
 */

@FxTable(name = "FX_BR_ITEM_DEF_ATTR", comment = "룰정의액션테이블")
@FxIndex(name = "FX_BR_ITEM_DEF_ATTR__PK", type = INDEX_TYPE.PK, columns = { "BR_ITEM_NAME" })
public class DeleteItemDefAttr {

	public DeleteItemDefAttr() {
	}

	@FxColumn(name = "BR_ITEM_NAME", size = 200, comment = "비즈니스룰항목명")
	private String brItemName;

}

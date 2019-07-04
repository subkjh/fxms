package fxms.nms.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.nms.dbo.FN_NET;
import fxms.nms.dbo.FN_NET_ITEM;

/**
 * 
 * @author SUBKJH-DEV
 *
 */
public class NetVo extends FN_NET {

	private List<FN_NET_ITEM> itemList;

	public List<FN_NET_ITEM> getItemList() {

		if (itemList == null) {
			itemList = new ArrayList<FN_NET_ITEM>();
		}

		return itemList;
	}
}

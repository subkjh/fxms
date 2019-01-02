package com.fxms.ui.bas.property;

import com.fxms.ui.bas.vo.ui.UiBasicVo;

public interface DxNode extends FxNode {

	/**
	 * 노드를 그리기 위한 정보를 제공한다.
	 * 
	 * @param vo
	 * @return
	 */
	public boolean initDxNode(UiBasicVo vo);

}

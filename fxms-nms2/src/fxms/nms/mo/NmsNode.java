package fxms.nms.mo;

import fxms.nms.mo.property.Modelable;

public interface NmsNode extends Modelable {

	/**
	 * 
	 * @return
	 */
	public String getIpAddress();

	/**
	 * 
	 * @return
	 */
	public String getNodeName();

}

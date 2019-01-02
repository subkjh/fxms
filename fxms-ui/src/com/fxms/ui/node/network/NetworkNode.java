package com.fxms.ui.node.network;

import javafx.scene.control.Button;

public class NetworkNode extends Button {
	
	private long moNo;

	public NetworkNode(long moNo)
	{
		super(moNo+"");
		
		setId(moNo+"");
		
		this.moNo = moNo;
	}

	public long getMoNo() {
		return moNo;
	}
	
	

}

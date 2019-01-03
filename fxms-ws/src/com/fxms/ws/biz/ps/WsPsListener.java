package com.fxms.ws.biz.ps;

public interface WsPsListener {

	public void onValue(long moNo, String psCode, long hstime, Number value);

}

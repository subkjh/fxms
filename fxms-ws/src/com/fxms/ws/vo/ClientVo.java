package com.fxms.ws.vo;

import org.java_websocket.WebSocket;

import fxms.module.restapi.vo.SessionVo;

public class ClientVo {

	private WebSocket websocket;

	private SessionVo user;

	public ClientVo(WebSocket websocket, SessionVo user) {
		this.websocket = websocket;
		this.user = user;
	}

	public WebSocket getWebsocket() {
		return websocket;
	}

	public SessionVo getUser() {
		return user;
	}

}

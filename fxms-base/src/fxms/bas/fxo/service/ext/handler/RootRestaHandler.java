package fxms.bas.fxo.service.ext.handler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map.Entry;

import fxms.module.restapi.FxHttpHandler;

import java.util.Set;

public class RootRestaHandler extends FxHttpHandler {

	@Override
	protected byte[] onProcess(InetSocketAddress client, Set<Entry<String, List<String>>> header, String body) throws Exception {
		String response = "<h1>Server start success if you see this message</h1>" + "<h1>Port: " + 9000 + "</h1>";
		return response.getBytes();
	}

	
}

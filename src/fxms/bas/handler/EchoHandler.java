package fxms.bas.handler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;

public class EchoHandler extends FxHttpHandler {

	@Override
	protected byte[] onProcess(HttpExchange he, InetSocketAddress client, String callFunc, String body)
			throws Exception {

		he.getResponseHeaders().add("Content-Type", "text/html");

		StringBuffer response = new StringBuffer();
		response.append("--- HEADER --- \n");
		for (Entry<String, List<String>> entry : he.getRequestHeaders().entrySet()) {
			response.append(entry.toString());
			response.append("\n");
		}
		response.append("--- BODY --- \n");
		response.append(body);
		return response.toString().getBytes();
	}

}

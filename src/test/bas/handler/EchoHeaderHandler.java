package test.bas.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EchoHeaderHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange he) throws IOException {
		Headers headers = he.getRequestHeaders();
		
		System.out.println(he.getRequestMethod());
		
		Set<Entry<String, List<String>>> entries = headers.entrySet();
		String response = "";
		for (Entry<String, List<String>> entry : entries)
			response += entry.toString() + "\n";
		he.sendResponseHeaders(200, response.length());
		OutputStream os = he.getResponseBody();
		os.write(response.toString().getBytes());
		os.close();
	}
}

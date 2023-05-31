package subkjh.bas.net.soproth;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class AliveClientJSonSoproth extends JSonSoproth {

	private String ip;
	private String name;

	public AliveClientJSonSoproth(String ip, String name) {
		this.ip = ip;
		this.name = name;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void processJSon(String jsonMsg) throws Exception {

		Gson gson = new Gson();
		Map map = gson.fromJson(jsonMsg, HashMap.class);

		Object value = map.get("stop");
		if (value != null && value.toString().equalsIgnoreCase("yes")) {
			this.stopSoproth("Server request to finish");
		}
		
		Object echo = map.get("echo");
		if ( echo != null ) {
			Thread.sleep(3000);
			
			send(jsonMsg.getBytes());
		}

	}

	@Override
	protected void initProcess() throws Exception {
		Gson gson = new Gson();

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("ip", ip);
		para.put("name", name);

		String json = gson.toJson(para);

		send(json.getBytes(charset));
	}

	
}

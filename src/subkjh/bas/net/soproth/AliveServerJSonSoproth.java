package subkjh.bas.net.soproth;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class AliveServerJSonSoproth extends JSonSoproth {

	public AliveServerJSonSoproth() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void processJSon(String jsonMsg) throws Exception {

		Gson gson = new Gson();
		Map map = gson.fromJson(jsonMsg, HashMap.class);

		Object ip = map.get("ip");
		Object name = map.get("name");

		if (ip != null && name != null) {

			this.setSoprothId(ip + "/" + name);

			map.clear();
			map.put("echo", "hi");
			send(gson.toJson(map).getBytes());

			return;
		}

		Object echo = map.get("echo");
		if (echo != null) {
			Thread.sleep(3000);

			send(jsonMsg.getBytes());
		}

	}

	@Override
	protected void initProcess() throws Exception {

	}

}
package fxms.bas.impl.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;

import fxms.bas.fxo.service.app.AppService;
import fxms.bas.impl.co.LoginHandlerDao;
import fxms.module.restapi.CommHandler;
import fxms.module.restapi.vo.SessionMap;
import fxms.module.restapi.vo.SessionVo;

public class LoginHandler extends CommHandler {

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("userId", "test");
		map.put("password", "ipaddr");
		map.put("ipaddr", "ipaddr");

		Gson son = new Gson();

		System.out.println(son.toJson(map));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected byte[] onProcess(InetSocketAddress client, Set<Entry<String, List<String>>> header, String body)
			throws Exception {

		Gson son = new Gson();
		Map<String, Object> map = son.fromJson(body, HashMap.class);

		String userId = getString(map, "user-id");
		String password = getString(map, "password");
		String ipAddr = client.getHostName();
		
		return login(userId, password, ipAddr);

	}

	private byte[] login(String userId, String password, String ipAddr) throws Exception {

		AppService service = getAppService();
		try {
			SessionVo vo = service.login(userId, password, ipAddr);
			SessionMap.getSessionMap().setNewSession(vo, ipAddr);
			
			new LoginHandlerDao().makeUserMo(vo.getUserNo());
			
			return getResult(vo.getSessionId(), null, null, true, vo);
		
		} catch (Exception e) {
			return getResult("nothing", 0, "login", false, e.getClass().getSimpleName());
		}
	}

}

package FX;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Naming;
import java.util.Arrays;

import fxms.bas.fxo.service.FxService;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * FxService에 대한 각 메소드를 호출하는 클래스
 * 
 * @author subkjh
 *
 */
public class MsCall {

	public static void main(String[] args) {

		try {
			String host = args[0];
			int port = Integer.valueOf(args[1]);
			String method = args[2];
			String paraStr[] = Arrays.copyOfRange(args, 3, args.length);
			MsCall call = new MsCall();
			call.call(host, port, method, paraStr);
		} catch (Exception e) {
			System.out.println("host port method parameters...");
		}

	}

	private boolean call(FxService service, String method, String paraStr[], boolean ignoreCase) throws Exception {
		
		boolean isMethod;
		
		for (Method m : service.getClass().getMethods()) {

			isMethod = ignoreCase ? m.getName().equalsIgnoreCase(method) : m.getName().equals(method);

			if (isMethod && m.getParameterTypes().length == (paraStr.length)) {

				Object para[] = new Object[paraStr.length];
				for (int i = 0; i < para.length; i++) {
					para[i] = ObjectUtil.convert(m.getParameterTypes()[i], paraStr[i]);
				}
				try {

					System.out.println("--- R E S U L T ---");
					System.out.println("\n");

					Object obj = m.invoke(service, para);

					if (obj != null) {
						System.out.println(obj);
					}

					return true;
				} catch (InvocationTargetException e) {
					e.getTargetException().printStackTrace();
					return true;
				} catch (IllegalArgumentException e) {
					continue;
				} catch (IllegalAccessException e) {
					continue;
				}

			}
		}

		return false;
	}

	private boolean call(String host, int port, String method, String paraStr[]) {

		System.out.println(Logger.fill("METHOD", 30, '.') + method);
		System.out.println(Logger.fill("PARAMETERS", 30, '.') + Arrays.toString(paraStr));
		System.out.println("\n");

		try {
			FxService ms = (FxService) Naming.lookup("rmi://" + host + ":" + port + "/FxService");
			if (call(ms, method, paraStr, false) == false) {
				if (call(ms, method, paraStr, true) == false) {
					System.out.println("Method Not Found");
				}
			}
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return false;
	}

}

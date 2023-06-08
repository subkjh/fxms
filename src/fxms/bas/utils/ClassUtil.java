package fxms.bas.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fxms.bas.api.ValueApi;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;

/**
 * classes.xml를 이용하여 사용할 클래스를 가져온다.
 * 
 * @author subkjh
 *
 */
public class ClassUtil {

	class Data {
		Class<?> classOf;
		Map<String, Object> para;
	}

	class Node {
		final String org;
		final String use;
		final String service;
		final Map<String, Object> para;

		Node(String org, String use, String service) {
			this.org = org;
			this.use = use;
			this.service = service;
			this.para = new HashMap<>();
		}
	}

	public static void main(String[] args) throws Exception {
		ClassUtil util =	new ClassUtil("ValueService");
		ValueApi api = util.makeObject4Use(ValueApi.class);
		System.out.println(api.getState(LOG_LEVEL.debug));

	}
	
	private final Map<String, Data> classMap;
	private final String serviceName;

	public ClassUtil(String serviceName) {
		this.serviceName = serviceName;
		this.classMap = new HashMap<String, Data>();
		loadClassDef();
	}

	/**
	 * 클래스를 생성합니다.
	 * 
	 * @param classOfT
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T makeObject4Use(Class<T> classOfT) throws Exception {

		if (classMap.size() == 0)
			return (T) classOfT.newInstance();

		Data data = classMap.get(classOfT.getName());
		if (data == null) {
			try {
				return (T) classOfT.newInstance();
			} catch (Exception e) {
				Logger.logger.fail("not define class : {}, {}", classOfT.getName(),
						FxCfg.getHomeDeployConf("classes.xml"));
				throw e;
			}
		} else {

			try {
				T t = (T) data.classOf.newInstance();

				// 파라메터가 있으면 설정한다.
				FxAttrApi.toObject(data.para, t);

				return t;

			} catch (Exception e) {
				Logger.logger.fail("not define class : {}, {}", classOfT.getName(),
						FxCfg.getHomeDeployConf("classes.xml"));
				throw e;
			}
		}
	}

	/**
	 * deploy/conf/classes.xml 파일을 읽는다.
	 * 
	 * @param serviceName
	 */
	private void loadClassDef() {

		Map<String, Node> nodeMap = this.parseClassDef();

		this.loadClassDef(nodeMap);

	}

	private void loadClassDef(Map<String, Node> nodeMap) {

		StringBuffer sb = new StringBuffer();
		Class<?> orgClass;
		Object useObj;

		List<String> keys = new ArrayList<>(nodeMap.keySet());
		keys.sort(new Comparator<String>() {

			@Override
			public int compare(String arg0, String arg1) {
				return arg0.compareTo(arg1);
			}
		});

		for (String key : keys) {
			
			Node node = nodeMap.get(key);

			try {
				orgClass = Class.forName(node.org);
				useObj = Class.forName(node.use).newInstance();
				if (orgClass.isInstance(useObj)) {
					Data data = new Data();
					data.classOf = useObj.getClass();
					data.para = node.para;
					classMap.put(node.org, data);
					
					FxAttrApi.toObject(data.para, useObj);

					sb.append(Logger.makeSubString(node.org, node.use + " " + data.para));
				} else {
					sb.append(Logger.makeSubString(node.org, "error"));
				}
			} catch (Exception e) {
				Logger.logger.error(e);
				sb.append(Logger.makeSubString(node.org, "error"));
			}

		}

		FxServiceImpl.logger.info(Logger.makeString("class-define-file(" + serviceName + ")", sb.toString()));
	}

	/**
	 * 마지막에 선언된 값으로 처리한다.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Node> parseClassDef() {

		String filterName = FxCfg.getHomeDeployConf("classes.xml");

		File f = new File(filterName);

		if (f.exists() == false) {
			FxServiceImpl.logger.fail("File not found [" + f.getPath() + "]");
			return null;
		}

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(f));
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
		}

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();
		String org, use, service, enable;
		Map<String, Node> retMap = new HashMap<>();
		Node node;

		for (Element child : children) {

			if ("class".equals(child.getName())) {
				org = child.getAttributeValue("org");
				use = child.getAttributeValue("use");
				enable = child.getAttributeValue("enable");
				service = child.getAttributeValue("service");

				// 특정서비스가 지정되어 있다면 그 서비스만 사용할 수 있습니다.
				if (service != null && service.equalsIgnoreCase(this.serviceName) == false) {
					continue;
				}

				if ("false".equalsIgnoreCase(enable)) {
					continue;
				}

				node = new Node(org, use, service);
				retMap.put(org, node);

				// 파라메터 있으면 가져온다.
				List<Element> c2List = child.getChildren();
				for (Element c2 : c2List) {
					if ("para".equals(c2.getName())) {
						String name = c2.getAttributeValue("name");
						String value = c2.getAttributeValue("value");
						// 동일한 이름 여러개가 있으면 List로 만들어 사용한다.
						FxmsUtil.addPara(node.para, name, value);
					}
				}

			}
		}
		return retMap;
	}

}

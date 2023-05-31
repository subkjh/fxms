package fxms.bas.fxo;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fxms.bas.fxo.service.FxServiceImpl;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * FxActor 파싱
 * 
 * @author subkjh
 *
 */
public class FxActorParser {

	/**
	 * 분석된 파일
	 * 
	 * @author subkjh(Kim,JongHoon)
	 *
	 */
	class ParsedFile {
		String path;
		long lastModified;

		ParsedFile(File f) {
			path = f.getPath();
			lastModified = f.lastModified();
		}
	}

	private static FxActorParser parser;

	public synchronized static FxActorParser getParser() {

		if (parser != null) {
			return parser;
		}

		parser = new FxActorParser();

		return parser;
	}

	public static void main(String[] args) {
		FxServiceImpl.serviceName = "WebService";
		new FxActorParser();
	}

	private List<FxActor> actorList;
	private List<ParsedFile> parsedFileList;

	private long lastParesedTime = 0;

	/**
	 * 
	 * @param classOfF 가져올 필터의 종류
	 */
	private FxActorParser() {

		parsedFileList = new ArrayList<ParsedFile>();
		actorList = new ArrayList<FxActor>();

		reload();
	}

	@SuppressWarnings("unchecked")
	public <T> T getActor(Class<T> classOfT) {

		FxActor[] list = getActorList();

		for (FxActor actor : list) {
			if (classOfT.isInstance(actor)) {
				return (T) actor;
			}
		}

		return null;
	}

	/**
	 * 한 번 생성된 클래스를 재사용합니다.
	 * 
	 * @param classOfT
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getActor(Class<T> classOfT, String className) {

		FxActor[] list = getActorList();

		for (FxActor f : list) {
			if (classOfT.isInstance(f)) {
				if (f.getClass().getName().equals(className))
					return (T) f;
			}
		}

		for (FxActor f : list) {
			if (classOfT.isInstance(f)) {
				if (f.getName().equals(className))
					return (T) f;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getActor(Class<T> classOfT, String name, String value) {

		FxActor[] list = getActorList();

		for (FxActor f : list) {
			if (classOfT.isInstance(f)) {
				if (value.equals(ObjectUtil.get(f, name) + ""))
					return (T) f;

				if (f.getMatch().matchAttr(name, value)) {
					return (T) f;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getActorList(Class<T> classOfT) {

		List<FxActor> fList = new ArrayList<FxActor>();
		FxActor[] list = getActorList();

		for (FxActor f : list) {
			if (classOfT.isInstance(f)) {
				fList.add(f);
			}
		}

		return (List<T>) fList;
	}

	/**
	 * 파일이 변경되었는지 판단한다.
	 * 
	 * @return true이면 변경된 경우
	 */
	public boolean isChanged() {

		File folder = new File(BasCfg.getPathFilter());

		if (folder.exists() == false) {
			return false;
		}

		// XML 파일만 가져온다.
		List<File> xmlFileList = new ArrayList<File>();
		for (File f : folder.listFiles()) {
			if (this.isXmlFile(f)) {
				xmlFileList.add(f);
			}
		}

		if (xmlFileList.size() != parsedFileList.size()) {
			return true;
		}

		A: for (File f : xmlFileList) {
			for (ParsedFile parsed : parsedFileList) {
				if (f.getPath().equals(parsed.path)) {
					if (f.lastModified() != parsed.lastModified) {
						return true;
					}
					continue A;
				}
			}
			return true;
		}

		return false;
	}

	public synchronized void reload() {

		File folder = new File(BasCfg.getPathFilter());

		List<FxActor> fList = new ArrayList<FxActor>();
		Logger.logger.info(folder.getPath());

		if (folder.exists() == false) {
			Logger.logger.fail("FILE-NAME({}) NOT FOUND", folder.getPath());
			return;
		}

		List<ParsedFile> fileList = new ArrayList<ParsedFile>();
		StringBuffer sb = new StringBuffer();

		for (File f : folder.listFiles()) {
			if (isXmlFile(f)) {
				try {
					sb.append(Logger.makeSubString(0, f.getName(), "parsing"));
					parse(f, fList, sb);
					fileList.add(new ParsedFile(f));
				} catch (Exception e) {
					sb.append(Logger.makeSubString(0, f.getName(), "error"));
					Logger.logger.error(e);
				}
			}
		}
		Logger.logger.info(Logger.makeString("FxActor", folder.getPath(), sb.toString()));

		lastParesedTime = System.currentTimeMillis();

		actorList.clear();
		actorList.addAll(fList);

		parsedFileList.clear();
		parsedFileList.addAll(fileList);
	}

	private synchronized FxActor[] getActorList() {

		if (lastParesedTime == 0) {
			reload();
		}

		return actorList.toArray(new FxActor[actorList.size()]);
	}

	public static boolean isXmlFile(File f) {
		if (f == null) {
			return false;
		}
		String name = f.getName().toLowerCase();
		return name.lastIndexOf("xml") == name.length() - 3;
	}

	@SuppressWarnings("unchecked")
	private int parse(File file, List<FxActor> fList, StringBuffer sb) throws Exception {

		Logger.logger.trace("{} parsing", file.getName());

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(file));
		} catch (Exception e) {
			throw e;
		}

		int cnt = 0;
		Element root = document.getRootElement();
		List<Element> nodeList = root.getChildren();
		String name, javaClass, allMatch, service;
		FxActor actor;
		Class<?> classOf = null;
		Map<String, Object> para;

		for (Element node : nodeList) {

			if (node.getName().equals("java-class") == false)
				continue;

			name = node.getAttributeValue("name").trim();
			service = node.getAttributeValue("service");
			javaClass = node.getAttributeValue("class").trim();
			allMatch = node.getAttributeValue("all-match");

			if (service != null && service.equals(FxServiceImpl.serviceName) == false) {
				sb.append(Logger.makeSubString(1, name, "x"));
				continue;
			}

			try {
				classOf = Class.forName(javaClass);
			} catch (ClassNotFoundException e) {
				sb.append(Logger.makeSubString(1, name, "class-not-found"));
				continue;
			}

			try {

				Object obj = classOf.newInstance();

				if (obj instanceof FxActor) {

					actor = (FxActor) obj;

					if (name != null) {
						actor.setName(name);
					}

					if (actor.getMatch() != null) {
						setMatch(actor.getMatch(), node);
						actor.getMatch().setAllMatch("true".equalsIgnoreCase(allMatch));
					}

					setPara(actor.getPara(), node);

					para = FxAttrApi.toObject(actor.getPara(), actor);

					actor.onCreated();

					try {
						sb.append(Logger.makeSubString(1, name, javaClass + " " + FxmsUtil.toJson(para)));
					} catch (Exception e) {
						sb.append(Logger.makeSubString(1, name, javaClass));
					}

					fList.add(actor);
					cnt++;

				} else {
					sb.append(Logger.makeSubString(1, name, "*** not FxActor ***"));
					continue;
				}

			} catch (Exception e) {
				Logger.logger.error(e);
				sb.append(Logger.makeSubString(1, name, "*** error *** " + e.getMessage()));
				continue;
			}

		}

		return cnt;
	}

	@SuppressWarnings("unchecked")
	private void setMatch(FxMatch match, Element element) {
		List<Element> children = element.getChildren();
		List<Attribute> attrList;
		String value;
		String field, method, result;

		for (Element child : children) {

			if (child.getName().equals("compare")) {

				attrList = child.getAttributes();

				field = result = method = value = null;

				for (Attribute e : attrList) {
					if (e.getName().equals("field"))
						field = e.getValue();
					else if (e.getName().equals("result"))
						result = e.getValue();
					else if (e.getName().equals("method"))
						method = e.getValue();
					else if (e.getName().equals("value"))
						value = e.getValue();
				}
				match.add(field, method, value, "false".equalsIgnoreCase(result) == false);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void setPara(Map<String, Object> para, Element element) {
		if (para == null) {
			return;
		}

		List<Element> children = element.getChildren();
		String name, value, os;
		String osName = System.getProperty("os.name", "").toLowerCase();

		for (Element child : children) {
			if (child.getName().equals("para")) {
				name = child.getAttributeValue("name");
				value = child.getAttributeValue("value");
				os = child.getAttributeValue("os");
				if (os == null || osName.indexOf(os.toLowerCase()) >= 0 || os.equals("*")) {
					// 동일한 이름 여러개가 있으면 List로 만들어 사용한다.
					FxmsUtil.addPara(para, name, value);
				}
			}
		}

	}
}

package com.fxms.ui.bas.lang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.fxms.ui.bas.property.ProgressIndicator;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Lang {

	private static Map<String, String> map = new HashMap<String, String>();
	
	public enum Type {
		button, msg, datetime, column, menu, prompt;
	}

	public static String getText(Type type, String name) {

		String ret = map.get(type.name() + "." + name);
		if (ret != null) {
			return ret;
		}

		ret = map.get(name);
		return ret == null ? name : ret;
	}

	public static void load(ProgressIndicator indicator) {

		if (indicator != null) {
			indicator.showMsg(0, "language...");
		}

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(Lang.class.getResourceAsStream("lang.xml"));
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Loading language fail");
			alert.setHeaderText(e.getClass().getSimpleName());
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

		Element root = document.getRootElement();
		List<Element> nodeList = root.getChildren();

		put(null, nodeList);

		for (Element node : nodeList) {
			if (node.getName().equals("lang-class")) {
				put(node.getAttributeValue("class"), node.getChildren());
			}
		}

		if (indicator != null) {
			indicator.showMsg(0, "language done");
		}

	}

	private static void put(String classOfLang, List<Element> nodeList) {

		String name, text;

		for (Element node : nodeList) {

			if (node.getName().equals("lang")) {
				name = node.getAttributeValue("name").trim();
				text = node.getAttributeValue("text").trim();
				if (classOfLang != null) {
					map.put(classOfLang + "." + name, text);
				} else {
					map.put(name, text);
				}
			}
		}
	}
}

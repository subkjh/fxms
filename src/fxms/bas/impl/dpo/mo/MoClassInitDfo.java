package fxms.bas.impl.dpo.mo;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dvo.MoDefDvo;

public class MoClassInitDfo implements FxDfo<Void, List<MoDefDvo>> {

	public static void main(String[] args) {
		MoClassInitDfo dfo = new MoClassInitDfo();
		try {
			for (MoDefDvo vo : dfo.call(null, null)) {
				System.out.println(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MoDefDvo> call(FxFact fact, Void moNo) throws Exception {
		return parse();
	}

	public List<MoDefDvo> parse() throws Exception {

		List<MoDefDvo> ret = new ArrayList<>();

		String folder = FxCfg.getHomeDeploy("mo");
		for (File f : new File(folder).listFiles()) {
			parse(f, ret);
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	private void parse(File file, List<MoDefDvo> list) throws Exception {

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(file));
		} catch (Exception e) {
			throw e;
		}

		Element root = document.getRootElement();
		List<Element> nodeList = root.getChildren();
		String moClass, javaClass, dboJavaClass;

		for (Element node : nodeList) {

			if (node.getName().equals("mo") == false)
				continue;

			moClass = node.getAttributeValue("mo-class").trim();
			javaClass = node.getAttributeValue("java-class");
			dboJavaClass = node.getAttributeValue("dbo-java-class").trim();

			list.add(new MoDefDvo(moClass, javaClass, dboJavaClass));

		}
	}

}

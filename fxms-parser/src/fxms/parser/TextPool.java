package fxms.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import fxms.parser.dao.ParserDao;

public abstract class TextPool {

	public static TextPool textPool;

	public static void main(String[] args) {
		TextPool pool = new TextPool() {
			@Override
			protected List<Map<String, Object>> selectQid(String qid) throws Exception {
				return new ParserDao().selectList(qid);
			}
		};
		pool.show();
	}

	private final List<AttrToken> attrList = new ArrayList<AttrToken>();
	private final List<CommandToken> commandList = new ArrayList<CommandToken>();
	private final List<CountToken> countList = new ArrayList<CountToken>();
	private final List<OrderToken> orderList = new ArrayList<OrderToken>();
	private final List<String> postpositionList = new ArrayList<String>();
	private final List<ScreenToken> screenList = new ArrayList<ScreenToken>();

	private final List<TermToken> termList = new ArrayList<TermToken>();

	public TextPool() {
		try {
			parse(new File("deploy/conf/ai-screen/postposition.xml"));
			parse(new File("deploy/conf/ai-screen/attr-define.xml"));
			parse(new File("deploy/conf/ai-screen/screen-define.xml"));
			parse(new File("deploy/conf/ai-screen/synonym.xml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextPool.textPool = this;

	}

	public AttrToken getAttr(String id) {
		for (AttrToken token : attrList) {
			if (token.getId().equals(id)) {
				return token;
			}
		}

		return null;
	}

	public List<AttrToken> getAttrList() {
		return attrList;
	}

	public List<CommandToken> getCommandList() {
		return commandList;
	}

	public List<CountToken> getCountList() {
		return countList;
	}

	public List<OrderToken> getOrderList() {
		return orderList;
	}

	public List<String> getPostpositionList() {
		return postpositionList;
	}

	public List<ScreenToken> getScreenList() {
		return screenList;
	}

	public List<TermToken> getTermList() {
		return termList;
	}

	public List<Map<String, Object>> makeParameters(List<ScreenToken> screenList, CommandToken command,
			List<Token> tokenList) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (screenList == null || screenList.size() == 0) {
			Map<String, Object> map = makeParameters(null, tokenList);
			list.add(map);
			return list;
		}

		for (ScreenToken screen : screenList) {
			Map<String, Object> map = makeParameters(screen, tokenList);
			list.add(map);
		}

		return list;

	}

	private Map<String, Object> makeParameters(ScreenToken screen, List<Token> tokenList) {

		Map<String, Object> map = new HashMap<String, Object>();

		if (screen == null) {
			map.put("screen", null);
			return map;
		} else {
			map.put("screen", screen.getId());
		}

		TermToken.setTime(tokenList, map);

		AttrToken at;

		for (Token token : tokenList) {

			if (token instanceof TermToken) {
				continue;
			}

			if (token instanceof AttrToken) {
				at = (AttrToken) token;
				if (at.getId() != null) {
					if (screen.isTag(at.getScreenTag())) {
						map.put(at.getId(), at.getValue());
						if (at.getAppendPara().size() > 0) {
							map.putAll(at.getAppendPara());
						}
					}
				}
			}
		}

		Map<String, Object> ret = new HashMap<String, Object>();
		for (String src : map.keySet()) {
			ret.put(rename(screen.getId(), src), map.get(src));
		}

		return ret;

	}

	private void fillAttr(Element element) {
		String qid = element.getAttributeValue("qid");
		String id = element.getAttributeValue("id");
		String type = element.getAttributeValue("type");

		List<Map<String, Object>> mapList;
		try {
			mapList = selectQid(qid);
			for (Map<String, Object> map : mapList) {

				if (map.get("ID") == null || map.get("TEXT") == null) {
					continue;
				}

				AttrToken token = new AttrToken(id//
						, map.get("TEXT").toString()//
						, type);

				if (token.getText().trim().length() > 0) {
					token.setValue(map.get("ID").toString());
					attrList.add(token);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SynonymPool synonymPool = new SynonymPool();

	public List<String> getSynonym(String name) {
		return synonymPool.get(name);
	}

	private void fillScreen(Element element) {
		String qid = element.getAttributeValue("qid");
		String id, text;
		List<Map<String, Object>> mapList;
		ScreenToken token;

		try {
			mapList = selectQid(qid);
			for (Map<String, Object> map : mapList) {
				if (map.get("ID") == null || map.get("TEXT") == null) {
					continue;
				}
				id = map.get("ID").toString();
				text = map.get("TEXT").toString();

				if (text.trim().length() > 0) {
					text = text.replaceAll(" ", ",");
					token = new ScreenToken(id, text);
					screenList.add(token);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private AttrToken makeAttrToken(Element element) {

		AttrToken token = new AttrToken(element.getAttributeValue("id") //
				, element.getAttributeValue("text")//
				, element.getAttributeValue("type"));

		token.setValue(element.getAttributeValue("value"));
		token.setPostAttr(element.getAttributeValue("post-attr"));
		token.setScreenTag(element.getAttributeValue("screen-tag"));

		List<Element> list = (List<Element>) element.getChildren();
		if (list != null) {
			String name, value;

			for (Element child : list) {
				if (child.getName().equals("para")) {
					name = child.getAttributeValue("name");
					value = child.getAttributeValue("value");
					if (name != null && value != null) {
						token.getAppendPara().put(name, value);
					}
				}

			}
		}

		return token;
	}

	private CommandToken makeCommandToken(Element element) {

		CommandToken token = new CommandToken(element.getAttributeValue("id") //
				, element.getAttributeValue("text"));

		return token;
	}

	private CountToken makeCountToken(Element element) {

		CountToken token = new CountToken(element.getAttributeValue("id") //
				, element.getAttributeValue("text"));

		return token;
	}

	private OrderToken makeOrderToken(Element element) {

		OrderToken token = new OrderToken(element.getAttributeValue("text") //
				, element.getAttributeValue("value"));

		return token;
	}

	private ScreenToken makeScreenToken(Element element) {

		ScreenToken token = new ScreenToken(element.getAttributeValue("id") //
				, element.getAttributeValue("text"));

		return token;
	}

	private String rename(String screenId, String srcName) {
		Map<String, String> map = renameMap.get(screenId);
		String dstName = null;
		if (map != null) {
			dstName = map.get(srcName);
		}

		return dstName == null ? srcName : dstName;
	}

	private Map<String, Map<String, String>> renameMap = new HashMap<String, Map<String, String>>();

	@SuppressWarnings("unchecked")
	private void makeRenamePara(Element element) {

		String screenId = element.getAttributeValue("screen-id");
		if (screenId == null) {
			return;
		}

		Map<String, String> map = renameMap.get(screenId);
		if (map == null) {
			map = new HashMap<String, String>();
			renameMap.put(screenId, map);
		}

		List<Element> children = element.getChildren();
		if (children != null && children.size() > 0) {
			String src, dst;

			for (Element e : children) {
				if (e.getName().equals("rename")) {
					src = e.getAttributeValue("src");
					dst = e.getAttributeValue("dst");
					if (src != null && dst != null) {
						map.put(src, dst);
					}
				}
			}
		}
	}

	private TermToken makeTermToken(Element element) {

		TermToken token = new TermToken(element.getAttributeValue("id") //
				, element.getAttributeValue("text") //
				, element.getAttributeValue("type"));

		token.setValue(element.getAttributeValue("value"));

		return token;
	}

	@SuppressWarnings("unchecked")
	private int parse(File file) throws Exception {

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
		AttrToken attrToken;
		for (Element node : nodeList) {

			if (node.getName().equals("attr")) {
				attrToken = makeAttrToken(node);
				attrList.add(attrToken);
			} else if (node.getName().equals("attr-term")) {
				termList.add(makeTermToken(node));
			} else if (node.getName().equals("attr-count")) {
				countList.add(makeCountToken(node));
			} else if (node.getName().equals("attr-order")) {
				orderList.add(makeOrderToken(node));
			} else if (node.getName().equals("postposition")) {
				postpositionList.add(node.getAttributeValue("text"));
			} else if (node.getName().equals("screen")) {
				screenList.add(makeScreenToken(node));
			} else if (node.getName().equals("command")) {
				commandList.add(makeCommandToken(node));
			} else if (node.getName().equals("rename-para")) {
				makeRenamePara(node);
			} else if (node.getName().equals("attr-list")) {
				fillAttr(node);
			} else if (node.getName().equals("screen-list")) {
				fillScreen(node);
			} else if (node.getName().equals("synonym")) {
				synonymPool.add(node.getAttributeValue("text"));
			}
		}

		return cnt;
	}

	public void show() {

		System.out.println(postpositionList);

		for (ScreenToken token : screenList) {
			System.out.println(token);
		}

		for (CommandToken token : commandList) {
			System.out.println(token);
		}

		// for (AttrToken token : attrList) {
		// System.out.println(token);
		// }

		for (AttrToken token : termList) {
			System.out.println(token);
		}
		for (OrderToken token : orderList) {
			System.out.println(token);
		}

		for (AttrToken token : countList) {
			System.out.println(token);
		}
	}

	protected abstract List<Map<String, Object>> selectQid(String qid) throws Exception;
}

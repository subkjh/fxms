package fxms.rule.vo;

import javax.script.ScriptEngine;

public class JavaScriptRet {

	private final ScriptEngine engine;
	private final Object result;

	public JavaScriptRet(ScriptEngine engine, Object result) {
		this.engine = engine;
		this.result = result;
	}

	public ScriptEngine getEngine() {
		return engine;
	}

	public Object getResult() {
		return result;
	}
}

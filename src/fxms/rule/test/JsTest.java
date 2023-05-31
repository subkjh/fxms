package fxms.rule.test;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import fxms.bas.fxo.FxmsUtil;

public class JsTest {

	public static void main(String[] args) {
		JsTest c = new JsTest();
		c.test3();
	}

	void test() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		try {
			Object result = engine.eval("Math.min(2, 3)");

			if (result instanceof Integer) {
				System.out.println(result);
			}
		} catch (ScriptException e) {
			System.err.println(e);
		}
	}

	void test2() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		String s = "var x = 1; var y = 2; var a = x + y;";

		try {
			Object result = engine.eval(s);
			System.out.println(result);
			System.out.println(engine.get("a"));
		} catch (ScriptException e) {
			System.err.println(e);
		}
	}

	void test3() {
		Map<String, Object> obj = new HashMap<String, Object>();
		Map<String, Object> obj2 = new HashMap<String, Object>();
		obj2.put("k", 1);
		obj2.put("z", 2);
		obj.put("a1", "test");
		obj.put("s1", obj2);
		String json = FxmsUtil.toJson(obj);

		// const obj = JSON.parse(str);

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		String s = "var ret = 'zzz'; var obj = JSON.parse('" + json + "');";
		s += "if ( obj.s1.k == 1 ) ret = '1'; obj";

		try {
			Object result = engine.eval(s);
			if (result instanceof jdk.nashorn.api.scripting.ScriptObjectMirror) {
				jdk.nashorn.api.scripting.ScriptObjectMirror r = (jdk.nashorn.api.scripting.ScriptObjectMirror) result;
				System.out.println(r.getOrDefault("a1", "-"));
			} else {
				System.out.println(result.getClass().getName());
			}

			System.out.println(engine.get("ret"));
		} catch (ScriptException e) {
			System.err.println(e);
		}
	}
}

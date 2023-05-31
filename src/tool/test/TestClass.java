package tool.test;

import java.lang.reflect.ParameterizedType;

import fxms.bas.mo.FxMo;
import fxms.bas.mo.Mo;

public class TestClass<T extends Mo> {

	public static void main(String[] args) {

		TestClass<FxMo> a = new TestClass<FxMo>();
	}

	private final Class<T> persistentClass;

	public TestClass() {
		this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];

		System.out.println(persistentClass);
	}

}

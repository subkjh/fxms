package subkjh.bas.fxdao;


import java.lang.reflect.Field;

import subkjh.bas.dao.define.COLUMN_TYPE;
import subkjh.bas.dao.define.INDEX_TYPE;
import subkjh.bas.fxdao.define.FxColumn;
import subkjh.bas.fxdao.define.FxIndex;
import subkjh.bas.fxdao.define.FxTable;

@FxTable(name = "FX_TEST_T2")
@FxIndex(name = "PK", type = INDEX_TYPE.PK, columns = { "moNo" })
@FxIndex(name = "FK", type = INDEX_TYPE.FK, columns = { "moNo" }, fkTable="FX_TEST_T1", fkColumn="moNo")

public class T2 extends T1 {

	@FxColumn(type = COLUMN_TYPE.CHAR, size = 1)
	private boolean mng;

	public static void main(String[] args) {
		T2 t = new T2();

		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			FxColumn annotation = field.getAnnotation(FxColumn.class);
			System.out.println(field.getName());
			if (annotation != null) {
				field.setAccessible(true);
				try {
					System.out.println(annotation.name());
					field.get(t);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("------------------------------");
	}

}

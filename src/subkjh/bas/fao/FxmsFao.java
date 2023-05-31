package subkjh.bas.fao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.ObjectUtil;

/**
 * 
 * @author subkjh
 *
 */
public class FxmsFao {

	@SuppressWarnings("rawtypes")
	private Map<String, List> dataMap = new HashMap<String, List>();

	public FxmsFao() {

	}

	private File getFile(Class<?> classOfT) {
		File file = new File(FxCfg.getFile("datas", "fxms", classOfT.getSimpleName() + ".dat"));
		return file;
	}

	private <T> List<T> read(final Class<T> classOfT) throws FileNotFoundException, Exception {

		final List<T> list = new ArrayList<T>();
		final int colSize = ObjectUtil.getFields(classOfT).size();
		FaoParser parser = new FaoParser() {

			private List<String> colList;
			private Map<String, Object> dataMap = new HashMap<String, Object>();

			@Override
			protected boolean isCompeletedData(List<String> data) {
				return data.size() == colSize + 1;
			}

			@Override
			protected void onData(int index, String line, List<String> data) throws Exception {

				if (index == 0) {
					data.remove(0); // 'COLUMN' 제거
					colList = new ArrayList<String>(data);
				} else {
					T o = classOfT.newInstance();
					for (int i = 1; i < data.size(); i++) {
						dataMap.put(colList.get(i - 1), data.get(i));
					}
					ObjectUtil.toObject(dataMap, o);
					list.add(o);
				}
			}

		};

		try {
			parser.read(getFile(classOfT));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	@SuppressWarnings("rawtypes")
	private boolean writeToFile(Class<?> classOfT, Collection datas) {

		StringBuffer sb = new StringBuffer();
		Map<String, Object> map;
		File file = getFile(classOfT);
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, false);

			sb.append(">>");
			for (Field field : ObjectUtil.getFields(classOfT)) {
				sb.append("|");
				sb.append(field.getName());
			}
			outStream.write(sb.toString().getBytes());

			for (Object data : datas) {
				sb = new StringBuffer();
				map = ObjectUtil.toMap(data);
				sb.append("\n");
				sb.append(">>");
				for (String key : map.keySet()) {
					sb.append("|");
					sb.append(map.get(key));
				}
				sb.append("\n");
				outStream.write(sb.toString().getBytes());
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void add(Object o) throws Exception {
		List list = dataMap.get(o.getClass().getName());
		list.add(o);
		this.writeToFile(o.getClass(), list);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> get(Class<T> classOfT) throws Exception {
		List list;

		try {
			list = read(classOfT);
		} catch (FileNotFoundException e) {
			list = new ArrayList();
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}

		dataMap.put(classOfT.getName(), list);
		return list;

	}

	@SuppressWarnings("rawtypes")
	public void set(Class<?> classOfT, Map map) throws Exception {

		try {
			writeToFile(classOfT, map.values());
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		}
	}

}

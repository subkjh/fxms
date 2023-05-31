package subkjh.bas.co.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.dao.util.DaoUtil;

public class FileLogger {

	public static final String separator = "|";

	private FileOutputStream outStream = null;
	private String filename;
	private String colNames[];

	public static void main(String[] args) throws Exception {

		String aa = "aaa|bbb";

		System.out.println(aa.replaceAll("\\|", "\\\\|"));

		FileLogger logger = new FileLogger("datas/test.data");

		String colNames[] = new String[] { "A_V", "DD_A23", "KKK" };

		logger.onStart(colNames);

		Object data[] = new Object[3];
		for (int i = 0; i < 10; i++) {

			data[0] = "aaa|bbb|ccc";
			data[1] = Math.random() * 1000;
			data[2] = Math.random() * 600000 + "|";

			logger.onSelected(i, data);
		}

		logger.onFinished(null);
	}

	public FileLogger(String filename) {

		this.filename = filename;

		Path file = Paths.get(filename);
		if (file.toFile().exists()) {
			Path movePath = Paths.get(filename + ".bak");
			try {
				Files.move(file, movePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				Logger.logger.error(e);
			}
		}

	}

	/**
	 * 
	 * @param filename
	 * @param dataList
	 * @throws Exception
	 */
	public FileLogger(String filename, List<?> dataList) throws Exception {

		this(filename);

		Map<String, Object> map = ObjectUtil.toMap(dataList.get(0));

		open(map.keySet().toArray(new String[map.size()]), false);

		for (int i = 0; i < dataList.size(); i++) {
			onSelected(i, dataList.get(i));
		}

		onFinished(null);
	}

	public void onFinished(Exception ex) throws Exception {
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				Logger.logger.error(e);
			}
			outStream = null;
		}
	}

	private String makeString(Object o) {
		if (o == null) {
			return "";
		}
		String val;
		val = o.toString();
		val = val.replaceAll("\\" + separator, "\\\\" + separator);
		return val;
	}

	public void onSelected(int rowNo, Object data) throws Exception {

		StringBuffer sb = new StringBuffer();

		sb.append(rowNo);

		if (data.getClass().isArray()) {
			Object dataArr[] = (Object[]) data;
			for (int i = 0; i < dataArr.length; i++) {
				sb.append(separator);
				sb.append(makeString(dataArr[i]));
			}
		} else {

			Map<String, Object> map = ObjectUtil.toMap(data);

			Object obj;
			for (String key : colNames) {
				sb.append(separator);
				obj = map.get(key);
				sb.append(makeString(obj));
			}
		}

		outStream.write(sb.toString().getBytes());
		outStream.write("\n".getBytes());

		System.out.println(parse(sb.toString()));
	}

	public void onStart(String[] colNames) throws Exception {
		open(colNames, true);
	}

	private void open(String[] colNames, boolean toChange) throws Exception {

		this.colNames = Arrays.copyOf(colNames, colNames.length);

		if (toChange) {
			for (int i = 0; i < colNames.length; i++) {
				this.colNames[i] = DaoUtil.getJavaFieldName(colNames[i], false);
			}
		}

		if (outStream == null) {
			outStream = new FileOutputStream(filename, false);
			outStream.write("COLUMNS".getBytes());
			for (String colNm : colNames) {
				outStream.write(separator.getBytes());
				outStream.write(colNm.getBytes());
			}
			outStream.write("\n".getBytes());
		}

	}

	public static List<String> parse(String line) {

		StringBuffer sb = new StringBuffer();
		List<String> list = new ArrayList<String>();
		char chs[] = line.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			if (i < chs.length - 1 && chs[i] == '\\' && chs[i + 1] == '|') {
				i++;
				sb.append(chs[i]);
			} else if (chs[i] == '|') {
				list.add(sb.toString());
				sb = new StringBuffer();
			} else {
				sb.append(chs[i]);
			}
		}

		list.add(sb.toString());

		return list;
	}

}

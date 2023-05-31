package subkjh.bas.fao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.FileUtil;

public abstract class BasFileParser<DATA> {

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final long INT32BIT = Long.valueOf("ffffffff", 16);

	public static void main(String[] args) {
		BasFileParser<String> parser = new BasFileParser<String>() {

			@Override
			protected boolean isCompeletedData(String data) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			protected void onData(int index, String line, String data) throws Exception {
				// TODO Auto-generated method stub

			}

			@Override
			protected String parse(String line) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

		};

		System.out.println(INT32BIT);
		System.out.println(parser.getValUptime("2017.12.27 09:02"));
		System.out.println(parser.getValMac("1:23:2:f:2:4"));
	}

	private String charset = "euc-kr";

	private int rowIndex = 0;

	public BasFileParser() {
	}

	public BasFileParser(String charset) {
		this.charset = charset;
	}

	protected void backup(File file) throws Exception {
		try {
			File zipFile = new File(
					file.getParent() + File.separator + "backup" + File.separator + file.getName() + ".zip");
			FileUtil.zipFile(file, zipFile);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

		boolean bret = file.delete();
		Logger.logger.debug("DELETE " + file.getPath() + " " + bret);

	}

	public synchronized String getValHstime(Object stime) {
		long time;
		if (stime instanceof Number) {
			time = ((Number) stime).longValue();
		} else if (stime instanceof Timestamp) {
			time = ((Timestamp) stime).getTime() / 1000L;
		} else {
			time = getValLong(stime, 0L);
		}
		return YYYYMMDDHHMMSS.format(new Date(time * 1000L));

	}

	public long getValInt(Object s, int defVal) {
		if (s instanceof Number) {
			return ((Number) s).intValue();
		} else {
			try {
				return Double.valueOf(s.toString()).intValue();
			} catch (Exception e) {
				return defVal;
			}
		}
	}

	public long getValLong(Object s, long defVal) {
		if (s instanceof Number) {
			return ((Number) s).longValue();
		} else {
			try {
				return Double.valueOf(s.toString()).longValue();
			} catch (Exception e) {
				return defVal;
			}
		}
	}

	public String getValMac(Object o) {
		String mac = getValStrToUpperCase(o);

		if (mac.indexOf(':') > 0) {
			String ss[] = mac.split(":");
			StringBuffer sb = new StringBuffer();
			for (String s : ss) {
				if (s.length() == 1) {
					sb.append("0");
				}
				sb.append(s);
			}
			return sb.toString();
		} else {
			mac = mac.replaceAll("\\.", "");
		}
		return mac;
	}

	public long getValMstime(Object date) {
		StringBuffer sb = new StringBuffer();
		if (date != null) {
			char chs[] = date.toString().toCharArray();
			for (char ch : chs) {
				if (ch >= '0' && ch <= '9') {
					sb.append(ch);
				}
			}
		}

		for (int i = sb.length(); i < 14; i++) {
			sb.append("0");
		}

		return DateUtil.toMstime(sb.toString());

	}

	public String getValStr(Object s) {
		if (s == null) {
			return null;
		}
		return s.toString();
	}

	public String getValStrToUpperCase(Object s) {
		if (s == null) {
			return null;
		}
		return s.toString().toUpperCase();
	}

	public Timestamp getValTimestamp(Object o) {

		if (o instanceof Timestamp) {
			return (Timestamp) o;
		}

		return new Timestamp(getValLong(o, 0) * 1000L);
	}

	public Timestamp getValTimestampByDate(Object o) {

		if (o instanceof Timestamp) {
			return (Timestamp) o;
		}

		long mstime = getValMstime(o);

		return new Timestamp(mstime);
	}

	public long getValUptime(Object o) {
		long uptime = System.currentTimeMillis() - getValMstime(getValStr(o));
		uptime /= 10;

		return uptime % INT32BIT;
		// 2017.12.24 04:11
		// [80:8c:97:8e:d2:64, 7280942584, 10.3.52-0000, 10.3.52-0000,
		// 2017.10.31 02:56, 192.168.35.130, 58.122.245.9, [상용]UHD_STB_#3,
		// 2017.12.24 04:11]

	}

	protected abstract boolean isCompeletedData(DATA data);

	protected abstract void onData(int index, String line, DATA data) throws Exception;

	protected void onFinished(File file, int size, Exception ex) {
		try {
			Logger.logger.info("FILE({}) DATA-COUNT({})", file.getName(), size);
		} catch (Exception e) {
			Logger.logger.error(e);
		} finally {
		}
	}

	protected boolean onRead(String line) throws Exception {

		DATA data;
		try {
			data = parse(line);
		} catch (Exception e) {
			return false;
		}

		if (isCompeletedData(data)) {
			onData(rowIndex, line, data);
			rowIndex++;
			return true;
		}

		return false;
	}

	protected void onStart(File file) throws Exception {
		Logger.logger.debug(String.valueOf(file));
	}

	protected abstract DATA parse(String line) throws Exception;

	public void read(File file) throws Exception {

		this.rowIndex = 0;

		long ptime = System.currentTimeMillis();
		BufferedReader in = null;
		String val;
		String valPrev = null;
		Exception ex = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));

			onStart(file);

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					if (val.trim().length() == 0 || val.equals("\\"))
						continue;

					if (valPrev != null) {
						if (onRead(valPrev + val) == false) {
							valPrev = valPrev + val;
						} else {
							valPrev = null;
						}
					} else {
						if (onRead(val) == false) {
							valPrev = val;
						} else {
							valPrev = null;
						}
					}

					if (System.currentTimeMillis() > ptime + 5000) {
						Logger.logger.debug(file.getName() + " READ DATA COUNT = " + rowIndex);
						ptime += 5000;
					}

				} catch (IOException e) {
					break;
				}
			}

		} catch (Exception e) {
			Logger.logger.error(e);
			ex = e;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}

			onFinished(file, rowIndex + 1, ex);
		}

	}
}
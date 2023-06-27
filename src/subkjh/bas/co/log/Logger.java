package subkjh.bas.co.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author subkjh
 *
 */
public class Logger {

	public static final String LINE = "-----------------------------------------------------------------------------------------------";

	public static boolean debug = false;

	public static final String INFORM = ">>>>> ";

	/** 기본 로거 */
	public static Logger logger = new Logger("logs", "fxms");

	/** 로거 보관 MAP */
	private static Map<String, Logger> loggers = new HashMap<String, Logger>();

	private static final String THIS_CLASS_NAME = Logger.class.getName();

	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

	private static final SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	public static Logger createLogger(String folder, String name) {
		Logger logger = loggers.get(name);
		if (logger == null) {
			logger = new Logger(folder, name);
			loggers.put(name, logger);
		}
		return logger;
	}

	public static String fill(char fillChar, int size, String s) {
		StringBuffer sb = new StringBuffer();
		if (s == null) {
			for (int i = 0; i < size; i++) {
				sb.append(fillChar);
			}
		} else {
			for (int i = s.length(); i < size; i++) {
				sb.append(fillChar);
			}
			sb.append(s);
		}
		return sb.toString();
	}

	public static String fill(String s, int size, char fillChar) {
		String s1 = String.valueOf(s);

		StringBuffer sb = new StringBuffer();
		sb.append(s1);
		for (int i = s1.length(); i < size; i++) {
			sb.append(fillChar);
		}

		return sb.toString();
	}

	/**
	 * 눈으로 확인할 수 있는 시간 형식으로 넘긴다.
	 * 
	 * @return yyyyMMddHHmmss의 값
	 */
	public static synchronized String getDate(long mstime) {
		if (mstime <= 0) {
			return YYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
		} else {
			return YYYYMMDDHHMMSS.format(new Date(mstime));
		}
	}

	public static String getParaString(Object... parameters) {
		StringBuffer sb = new StringBuffer();
		for (Object o : parameters) {
			if (o == null) {
				sb.append("null");
			} else {
				sb.append(o.getClass().getName());
			}
			sb.append(", ");
		}

		return sb.toString();
	}

	public static String getString(Map<String, Object> para) {
		List<String> lines = new ArrayList<String>();
		Object obj;
		synchronized (para) {
			for (String key : para.keySet()) {
				obj = para.get(key);
				if (obj != null)
					lines.add(fill(key, 40, '.') + " " + obj);
			}
		}

		java.util.Collections.sort(lines);

		String ret = "";
		for (String s : lines)
			ret += s + "\n";

		return ret;
	}

	public static void main(String[] args) {
		String a = "a" + File.separator + "b" + File.separator + "c";
		String format = "...{}  {}...{";
		Logger.logger.info(format, a, "XXX", "aaa");
	}

	public static String makeString(Object name, Object value) {
		return makeString(name, value, null);
	}

	public static String makeString(Object name, Object value, String postMsg) {
		StringBuffer sb = new StringBuffer();

		sb.append("\n");
		sb.append(Logger.LINE);
		sb.append("\n");
		sb.append("FX-SERVICE >>> ");
		sb.append(Logger.fill(name.toString(), 38, '.'));
		if (value != null) {
			sb.append(Logger.fill('.', 42, " " + value.toString()));
		}
		if (postMsg != null) {
			sb.append(postMsg);
		}
		sb.append("\n");
		sb.append(Logger.LINE);

		return sb.toString();
	}

	public static String makeString(String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(LINE);
		sb.append("\n");
		sb.append(msg);
		sb.append("\n");
		sb.append(LINE);
		return sb.toString();

	}

	public static String makeSubString(int level, String name, Object value) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(">>");
		for (int i = 0; i < (2 + level * 2); i++) {
			sb.append(" ");
		}
		sb.append(Logger.fill(name, (50 - (level * 2)), '-'));
		sb.append(" ");
		sb.append(value);
		return sb.toString();
	}

	public static String makeSubString(String name, Object value) {
		return makeSubString(0, name, value);
	}

	private static StackTraceElement getCaller(Class<?> cls) {

		StackTraceElement list[] = Thread.currentThread().getStackTrace();

		for (int i = 1; i < list.length; i++) {

			if (list[i].getClassName().equals(THIS_CLASS_NAME) == false) {
				if (cls != null) {
					if (cls.getName().startsWith(list[i].getClassName()) == false)
						return list[i];
				} else {
					return list[i];
				}
			}
		}

		return null;
	}

	private static String getThrowableString(Throwable e) {
		StackTraceElement list[] = e.getStackTrace();
		String line = e.toString();
		String ss[];
		String cn;
		for (StackTraceElement caller : list) {
			ss = caller.getClassName().split("\\.");
			cn = ss[ss.length - 1] + "." + caller.getMethodName();
			if (line.length() != 0)
				line += "\t";
			line += cn + "(" + caller.getFileName() + ":" + caller.getLineNumber() + ")" + "\n";
		}

		return line;
	}

	private static String getTime() {
		Calendar cal = Calendar.getInstance();
		String s = String.format("%02d,%02d:%02d:%02d.%03d", cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND),
				cal.get(Calendar.MILLISECOND));

		return s;
	}

	/** 기록할 화일 */
	private File file = null;
	/** 기록할 폴더 */
	private String folder = null;
	/** 콘솔에 출력 여부 */
	private boolean isPrintOutConsole = true;
	/** 로그 남길 등급 */
	private LOG_LEVEL level = LOG_LEVEL.trace;
	/** 출력 PrintStream */
	private PrintStream m_out = null;
	/** 로커 화일 관리 개수 */
	private int maxBackupFileCount = 21;
	/** 출력 화일명 */
	private String nameLogFile;
	/** 화일 Output Stream */
	private FileOutputStream outStream = null;
	/** java파일정보 출력 여부 */
	private boolean printJavaInfo = true;

	/** 스레드 정보 출력 여부 */
	private boolean printThreadName = true;

	/** 타이틀 출력 여부 */
	private boolean printTitle = true;

	/** 화일 오픈한 시간 */
	private String yyyymmddOpenedFile = "";

	private PrintStream ps = null;

	/** 스레드별 등급 */
	private Map<String, Integer> thLevelMap;

	/**
	 * 로그를 기록하는 클래스입니다.<br>
	 * 로그는 일일 단위로 자동으로 백업되면 백업 화일명은 name.YYYYMMDD.log입니다.<br>
	 * 보관 기관이 경과된 화일 또한 자동으로 삭제되며 보관 기관은 setMaxBackupFileCount()를 이용하여 설정할 수
	 * 있습니다.<br>
	 * 기본 보관 화일 개수는 21건 입니다.
	 * 
	 * @param folder 로그 보관 폴더
	 * @param name   확장자를 제외한 로그 화일명
	 */
	public Logger(String folder, String name) {

		this.folder = folder;
		if (folder != null) {
			File f = new File(folder);
			if (f.exists() == false)
				f.mkdirs();
		}

		this.nameLogFile = name == null ? null : name.replace(" ", "").toLowerCase();
	}

	public void check(String format, Object... objArray) {
		if (LOG_LEVEL.trace.contains(getLevel4Th()))
			logc(null, "C", format, objArray);
	}

	public void debug(String format, Object... objArray) {
		if (LOG_LEVEL.debug.contains(getLevel4Th()))
			logc(null, "D", format, objArray);
	}

	/**
	 * debug 로그를 출력합니다. 태그 정보는 D입니다.
	 * 
	 * @param obj     출력한 내용
	 * @param classes Caller를 찾을 때 제외할 클래스들
	 */
	public void debugc(Class<?> cls, String format, Object... objArray) {
		if (LOG_LEVEL.debug.contains(getLevel4Th()))
			logc(cls, "D", format, objArray);
	}

	/**
	 * Throwable을 출렵합니다.
	 * 
	 * @param e
	 */
	public void error(Object e) {
		String s;
		if (e instanceof Throwable) {
			s = getThrowableString((Throwable) e);
		} else {
			s = e + "";
		}
		logc(null, "E", s);
	}

	public void fail(String format, Object... obj) {
		logc(null, "F", format, obj);
	}

	/**
	 * @return 로그 폴더를 알려줍니다.
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * @return 현재 로그 레벨을 알려줍니다.
	 */
	public LOG_LEVEL getLevel() {
		return level;
	}

	/**
	 * @return 로그 화일 보관 개수를 알려줍니다.
	 */
	public int getMaxBackupFileCount() {
		return maxBackupFileCount;
	}

	public String getNameLogFile() {
		return nameLogFile;
	}

	public void info(String format, Object... objArray) {
		if (LOG_LEVEL.info.contains(getLevel4Th()))
			logc(null, "I", format, objArray);
	}

	/**
	 * info 레벨의 로그를 출력합니다. 태그 정보는 I입니다. <br>
	 * 
	 * @param obj
	 * @param classes
	 */
	public void infoc(Class<?> cls, String format, Object... objArray) {
		if (LOG_LEVEL.debug.contains(getLevel4Th()))
			logc(cls, "I", format, objArray);
	}

	public boolean isPrintJavaInfo() {
		return printJavaInfo;
	}

	/**
	 * @return 콘솔로 로그를 출력하는지 여부
	 */
	public boolean isPrintOutConsole() {
		return isPrintOutConsole;
	}

	public boolean isPrintThreadName() {
		return printThreadName;
	}

	public boolean isPrintTitle() {
		return printTitle;
	}

	public boolean isTrace() {
		return LOG_LEVEL.trace.contains(getLevel4Th());
	}

	/**
	 * @param level 로그 레벨을 설정합니다.
	 */
	public void setLevel(LOG_LEVEL level) {
		thLevelMap = null;
		this.level = level;
	}

	public void setLevel4Th(String name, Integer level) {

		if (level == null && thLevelMap == null)
			return;

		if (thLevelMap == null) {
			thLevelMap = new HashMap<String, Integer>();
		}

		if (level == null)
			thLevelMap.remove(name);
		else
			thLevelMap.put(name, level);
	}

	/**
	 * @param maxBackupFileCount 로그 화일 보관 개수를 설정합니다.
	 */
	public void setMaxBackupFileCount(int maxBackupFileCount) {
		this.maxBackupFileCount = maxBackupFileCount <= 0 ? 1 : maxBackupFileCount;
	}

	/**
	 * java파일정보 출력 여부 설정
	 * 
	 * @param printJavaInfo
	 */
	public void setPrintJavaInfo(boolean printJavaInfo) {
		this.printJavaInfo = printJavaInfo;
	}

	/**
	 * @param isPrintOutConsole 콘솔로 로그를 출력할지 결정합니다.
	 */
	public void setPrintOutConsole(boolean isPrintOutConsole) {
		this.isPrintOutConsole = isPrintOutConsole;
	}

	/**
	 * 사용할 PrintStream을 설정합니다.<br>
	 * 여기서 설정된 PrintStream 날짜와 상관없이 계속 사용됩니다.
	 * 
	 * @param ps
	 * @since 2013.10.17
	 */
	public void setPrintStream(PrintStream ps) {
		this.ps = ps;
	}

	/**
	 * 스레드 정보 출력 여부
	 * 
	 * @param printThreadName
	 */
	public void setPrintThreadName(boolean printThreadName) {
		this.printThreadName = printThreadName;
	}

	/**
	 * 타이틀 출력 여부
	 * 
	 * @param printTitle
	 */
	public void setPrintTitle(boolean printTitle) {
		this.printTitle = printTitle;
	}

	/**
	 * info 레벨의 로그를 출력합니다.<br>
	 * 태그 정보는 S입니다.
	 * 
	 * @param obj
	 */
	public void tag(String format, Object... objArray) {
		if (LOG_LEVEL.info.contains(getLevel4Th()))
			logc(null, "S", format, objArray);
	}

	public void trace(String format, Object... objArray) {
		if (LOG_LEVEL.trace.contains(getLevel4Th())) {
			logc(null, "T", format, objArray);
		}
	}

	/**
	 * trace 레벨의 로그를 출력합니다. 태그 정보는 T입니다.
	 * 
	 * @param obj
	 * @param classes
	 */
	public void tracec(Class<?> cls, String format, Object... objArray) {
		if (LOG_LEVEL.debug.contains(getLevel4Th()))
			logc(cls, "T", format, objArray);
	}

	private int getLevel4Th() {

		if (thLevelMap == null)
			return level.getNum();

		Integer thLevel = thLevelMap.get(Thread.currentThread().getName());
		if (thLevel == null)
			return level.getNum();

		return thLevel.intValue();
	}

	/**
	 * 출력할 메시지를 제공합니다.
	 * 
	 * @param msg     출력할 메시지
	 * @param tag     메시지 종류<br>
	 *                보통 한 문자로 T, D, I, F, E를 사용합니다. 기타 다른 문자열을 사용해도 상관없습니다.
	 * @param classes 이 클래스 내의 Stack Trace는 제외합니다.
	 * @return 출력할 메시지
	 */
	private String getLine(String msg, String tag, Class<?> cls) throws Exception {

		StackTraceElement caller = getCaller(cls);
		StringBuffer sb = new StringBuffer();
		sb.append(getTime() + " ");

		String ss[];
		String cn;

		sb.append("[" + tag + "]");

		if (printThreadName)
			sb.append(" [" + Thread.currentThread().getName() + "]");

		// 컴파일할때 debug=on으로 해야 파일명, 라인수가 나온다.

		if (printJavaInfo) {
			if (caller != null) {
				ss = caller.getClassName().split("\\.");
				cn = ss[ss.length - 1] + "." + caller.getMethodName();

				sb.append(cn + " (" + caller.getFileName() + ":" + caller.getLineNumber() + ")");
			}
		}

		sb.append(" " + msg);
		return sb.toString();
	}

	private PrintStream getPrintStream() throws Exception {

		if (folder == null || nameLogFile == null)
			return null;

		String yyyymmdd = YYYYMMDD.format(new Date(System.currentTimeMillis()));

		// 일자가 변경되지 않았다면
		if (yyyymmdd.equals(yyyymmddOpenedFile)) {
			return m_out == null ? System.out : m_out;
		}

		if (m_out != null) {
			m_out.close();
			m_out = null;
		}
		if (outStream != null) {
			try {
				outStream.close();
				outStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 현재 화일을 전날 일자를 붙어 백업합니다.
		if (file != null) {
			File dest = new File(folder + File.separator + nameLogFile + "." + yyyymmddOpenedFile + ".log");
			file.renameTo(dest);
		}

		file = new File(folder + File.separator + nameLogFile + ".log");

		// 어제 마지막 변경되었으면 오늘 처음 쓰기 시작되면 어제 날자로 백업합니다.
		if (file.exists() && file.lastModified() < getTodayFirstTime()) {
			File dest = new File(folder + File.separator + nameLogFile + "."
					+ YYYYMMDD.format(new Date(file.lastModified())) + ".log");
			file.renameTo(dest);

			file = new File(folder + File.separator + nameLogFile + ".log");
		}

		try {
			outStream = new FileOutputStream(file, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return System.out;
		}

		m_out = new PrintStream(outStream);

		printTitle(m_out);

		yyyymmddOpenedFile = yyyymmdd;

		File f = new File(folder);
		String files[] = f.list();
		if (files != null) {
			List<String> list = new ArrayList<String>();
			for (String s : files) {
				if (s.startsWith(nameLogFile + ".")) {
					list.add(s);
				}
			}

			// 내림차순으로 정렬한다.
			Collections.sort(list, new Comparator<String>() {
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});

			// 초과된 내용을 제거한다.
			int removeSize = list.size() - (maxBackupFileCount + 1);
			if (removeSize > 0) {
				for (int i = 0; i < removeSize; i++) {
					new File(folder + File.separator + list.get(i)).delete();
				}
			}
		}

		return m_out;
	}

	/**
	 * 입력된 시간의 하루 첫 시간을 제공합니다.
	 * 
	 * @return 하루 첫 시간
	 */
	private long getTodayFirstTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	private void logc(Class<?> cls, String tag, String format, Object... objArray) {

		if (format == null) {
			return;
		}

		int index = 0;
		StringBuffer sb = new StringBuffer();
		char chs[] = format.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			if (i < chs.length - 1 && chs[i] == '{' && chs[i + 1] == '}') {
				if (index < objArray.length) {
					sb.append(objArray[index]);
					i++;
					index++;
					continue;
				}
			}
			sb.append(chs[i]);
		}

		try {
			String s = getLine(sb.toString(), tag, cls);
			print(s);
		} catch (Exception e) {
		}

	}

	private synchronized void print(String str) {

		PrintStream ps = this.ps;

		if (ps == null) {

			try {
				ps = getPrintStream();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

		}

		if (ps != null)
			ps.println(str);

		// 콘솔에 출력을 요청했고 위에서 콘솔로 출력을 하지 않았다면 출력한다.
		if (isPrintOutConsole) {
			if (System.out.equals(ps) == false) {
				System.out.println(str);
			}
		}
	}

	private void printTitle(PrintStream out) {

		if (printTitle == false)
			return;

		out.println("################################################################################");
		out.println("#");
		out.println("# logger.file=" + nameLogFile);
		out.println("# logger.level=" + getLevel());
		out.println("# logger.count=" + maxBackupFileCount);
		out.println("# date=" + getTime());
		out.println("# properties=" + System.getProperties());
		out.println("# env=" + System.getenv());
		out.println("#");
		out.println("################################################################################");
		out.println();
	}

	/**
	 * 
	 */
	protected void finalize() {
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

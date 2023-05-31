package subkjh.bas.co.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import subkjh.bas.co.log.Logger;

public class FileUtil {
	
	public interface StringListener {
		public void  onLine(String line);
	}

	public static void main(String[] args) {
		System.out.println(FileUtil.getLineSize(new File("datas/oiv10402.txt")));

		try {
			FileUtil.zipFile(new File("tmp/error_sql.sql"), new File("tmp/bakcup/error_sql.zip"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void append(File source, File target) throws Exception {

		FileOutputStream outStream = null;
		BufferedReader inStream = null;
		String val;
		try {
			outStream = new FileOutputStream(target, true);
			inStream = new BufferedReader(new FileReader(source));
			while (true) {

				try {
					val = inStream.readLine();
					if (val == null) {
						break;
					}

					val += "\n";
					outStream.write(val.getBytes());
				} catch (IOException e) {
					break;
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (inStream != null)
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static byte[] getBytes(File file, long startIndex, int size) throws Exception {
		if (file.exists() == false)
			throw new FileNotFoundException("FILE-NAME(" + file.getPath() + ")");

		int len = (int) (file.length() - startIndex > size ? size : file.length() - startIndex);

		RandomAccessFile inStream = null;
		byte bytes[] = new byte[len];

		try {
			inStream = new RandomAccessFile(file, "r");
			inStream.seek(startIndex);
			inStream.read(bytes);
			return bytes;
		} catch (IOException e) {
			throw e;
		} finally {

			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 화일에서 주석처리된 내용을 제외한 목록을 가져온다.
	 * 
	 * @param file
	 * @return 문자열 목록
	 */
	public static List<String> getLines(File file) {
		try {
			return getLines(file, Charset.forName("utf-8"));
		} catch (Exception e) {
			return new ArrayList<String>();
		}
	}

	public static List<String> getLines(File file, Charset charset) throws IOException {
		List<String> ret = new ArrayList<String>();

		BufferedReader in = null;
		String val;

		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file),charset));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					if (val.trim().length() == 0)
						continue;
					if (val.startsWith("#"))
						continue;
					ret.add(val.trim());

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret;

	}

	public static List<String> getLines(File file, String lastStr) {
		List<String> ret = new ArrayList<String>();

		BufferedReader in = null;
		String val;
		String line = "";

		try {
			in = new BufferedReader(new FileReader(file));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					if (val.trim().length() == 0)
						continue;
					if (val.startsWith("#"))
						continue;
					line += val.trim() + "\n";
					if (val.trim().indexOf(lastStr) == val.trim().length() - 1) {
						ret.add(line);
						line = "";
					}

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret;

	}

	/**
	 * 화일 내용을 문자열로 제공합니다
	 * 
	 * @param file
	 * @return 문자열
	 */
	public static String getString(File file) {
		BufferedReader in = null;
		String ret = "";
		String val = "";

		try {
			in = new BufferedReader(new FileReader(file));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					ret += val + "\n";

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret;

	}

	public static String getString(File file, int lineCount) {
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		String val = "";
		int no = 0;

		try {
			in = new BufferedReader(new FileReader(file));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}

					sb.append(val);
					sb.append("\n");

					no++;

					if (no >= lineCount)
						break;

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return sb.toString();

	}

	public static int getLineSize(File file) {
		BufferedReader in = null;
		String val = "";
		int no = 0;

		try {
			in = new BufferedReader(new FileReader(file));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}

					no++;

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return no;

	}

	public static String getString(InputStream inputStream) {
		BufferedReader in = null;
		String ret = "";
		String val = "";

		try {
			in = new BufferedReader(new InputStreamReader(inputStream));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					ret += val + "\n";

				} catch (IOException e) {
					break;
				}
			}
		} catch (Exception e) {

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret;

	}

	public static byte[] getBytes(File file) throws Exception {
		if (file.exists() == false)
			throw new FileNotFoundException("FILE-NAME(" + file.getPath() + ")");

		FileInputStream inStream = null;
		byte bytes[] = new byte[(int) file.length()];

		try {
			inStream = new FileInputStream(file);
			inStream.read(bytes);
			return bytes;
		} catch (IOException e) {
			throw e;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void move(File source, File target) throws Exception {

		FileOutputStream outStream = null;
		FileInputStream inStream = null;
		byte bytes[] = new byte[4096];
		int len = 0;
		boolean isOk = false;
		try {
			outStream = new FileOutputStream(target, false);
			inStream = new FileInputStream(source);
			while (inStream.available() >= 0) {
				len = inStream.read(bytes);
				if (len > 0)
					outStream.write(bytes, 0, len);
				if (len < 0)
					break;
			}
			isOk = true;
		} catch (IOException e) {
			throw e;
		} finally {
			if (outStream != null) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (inStream != null) {
				try {
					inStream.close();
					if (isOk)
						source.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 화일복사
	 * 
	 * @param source 복사할 화일
	 * @param target 새로만들 화일
	 * @throws Exception
	 * 
	 * @since 2013.05.15 by subkjh
	 */
	public static void copy(File source, File target) throws Exception {

		FileOutputStream outStream = null;
		FileInputStream inStream = null;
		byte bytes[] = new byte[4096];
		int len = 0;
		try {

			// make target folder
			target.getParentFile().mkdirs();

			outStream = new FileOutputStream(target, false);
			inStream = new FileInputStream(source);
			while (inStream.available() >= 0) {
				len = inStream.read(bytes);
				if (len > 0)
					outStream.write(bytes, 0, len);
				if (len < 0)
					break;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (outStream != null) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Object readObjectToFile(String filename) throws Exception {

		ObjectInputStream inStream = null;
		try {
			FileInputStream fos = new FileInputStream(filename);
			inStream = new ObjectInputStream(fos);
			return inStream.readObject();
		} catch (IOException e) {
			throw e;
		} finally {
			if (inStream != null)
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public static boolean writeObjectToFile(String filename, Object obj) throws Exception {

		ObjectOutputStream outStream = null;
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			outStream = new ObjectOutputStream(fos);
			outStream.writeObject(obj);
			outStream.flush();
			return true;
		} catch (IOException e) {
			throw e;
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	/**
	 * 파일에 내용을 기록합니다.
	 * 
	 * @param filename 파일
	 * @param datas    기록한 내용
	 * @param append   파일에 추가할 것인지 새롭게 기록할 것인지
	 * @return 처리 결과
	 */
	public static boolean writeToFile(String filename, byte[] datas, boolean append) {

		File file = new File(filename);
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, append);
			outStream.write(datas);
			return true;
		} catch (Exception e) {
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

	/**
	 * 파일에 내용을 기록합니다.
	 * 
	 * @param filename 파일
	 * @param datas    기록할 내용
	 * @param append   파일이 존재할 때 덧붙일 것인지 아니면 새롭게 기록할 것인지
	 * @return 처리 결과
	 */
	public static boolean writeToFile(String filename, String datas, boolean append) {

		File file = new File(filename);

		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, append);
			outStream.write(datas.getBytes());
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

	/**
	 * 폴더내의 입력된 날자와 그 이전의 폴더를 삭제합니다.
	 * 
	 * @param folder   폴더
	 * @param yyyymmdd 기준날자
	 * @param logger   로거
	 * @return
	 * @throws Exception
	 */
	public static int deleteFolder(File folder, String yyyymmdd, Logger logger) throws Exception {
		logger.debug(yyyymmdd);

		File fileArray[];
		File folderArray[];
		int countDeleted = 0;

		if (folder.exists() && folder.isDirectory()) {
			folderArray = folder.listFiles();

			for (File f : folderArray) {
				if (f.getName().length() == 8 && f.getName().compareTo(yyyymmdd) < 0) {
					if (f.isDirectory()) {
						fileArray = f.listFiles();
						for (File file : fileArray) {
							if (file.delete()) {
								countDeleted++;
								logger.debug(file.getName());
							}
						}
						if (f.delete()) {
							countDeleted++;
							logger.debug(f.getName());
						}
					}
				}
			}
		}

		return countDeleted;
	}

	public static int delete(File f) throws Exception {

		int count = 0;

		if (f.exists() == false) {
			return 0;
		}

		if (f.isDirectory()) {
			for (File e : f.listFiles()) {
				count += delete(e);
			}
		}

		f.delete();
		count++;

		return count;

	}

	/**
	 * 
	 * @param file
	 * @param zipFile
	 * @throws Exception
	 */
	public static void zipFile(File file, File zipFile) throws Exception {

		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		FileInputStream fis = null;

		if (file.exists() == false) {
			throw new FileNotFoundException(file.getName());
		}

		File zipFolder = new File(zipFile.getParent());
		zipFolder.mkdirs();

		try {
			fos = new FileOutputStream(zipFile.getPath());
			zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(file.getName());
			zos.putNextEntry(ze);
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

		} catch (IOException e) {
			throw e;
		} finally {
			if (zos != null) {
				try {
					zos.closeEntry();
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String zip(File file, String destfile) throws Exception {

		if (destfile == null || destfile.trim().length() == 0)
			destfile = file.getPath() + ".zip";

		ZipOutputStream out = null;
		try {
			FileOutputStream dest = new FileOutputStream(destfile);
			CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
			out = new ZipOutputStream(new BufferedOutputStream(checksum));
			zip(out, file, file.getName());

			// if (logger != null)
			// logger.debug("Checksum: " + checksum.getChecksum().getValue());

		} catch (Exception e) {
			throw e;
		} finally {
			if (out != null)
				out.close();
		}

		return destfile;
	}

	private static final int BUFFER = 2048;

	private static void zip(ZipOutputStream out, File file, String entryName) throws Exception {

		if (file.isDirectory()) {
			File children[] = file.listFiles();
			for (File child : children)
				zip(out, child, entryName + File.separator + child.getName());
		} else {

			BufferedInputStream origin;
			byte data[] = new byte[BUFFER];

			// if (logger != null)
			// logger.debug("Adding: " + file.getName());

			FileInputStream fi = new FileInputStream(file);
			origin = new BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(entryName);
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();
		}
	}

	public static void unzip(String filename, String destFolder) throws Exception {

		File folder = new File(destFolder);
		if (folder.exists() == false)
			folder.mkdirs();

		String destFilename;
		File destFile;
		try {
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(filename);
			CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
			ZipEntry entry;
			String name;

			while ((entry = zis.getNextEntry()) != null) {

				name = convert(entry.getName());

				int count;
				byte data[] = new byte[BUFFER];

				destFilename = destFolder != null ? destFolder + File.separator + name : name;
				destFile = new File(destFilename);
				destFile.getParentFile().mkdirs();

				FileOutputStream fos = new FileOutputStream(destFilename);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();

		} catch (Exception e) {
			throw e;
		}
	}

	private static String convert(String str) {
		byte bytes[] = str.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == '\\')
				bytes[i] = (byte) File.separatorChar;
			else if (bytes[i] == '/')
				bytes[i] = (byte) File.separatorChar;
		}
		return new String(bytes);
	}
	

	public static void read(File file, Charset charset, StringListener listener) throws IOException {

		BufferedReader in = null;
		String val;

		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file),charset));

			while (true) {
				try {
					val = in.readLine();
					if (val == null) {
						break;
					}					
					listener.onLine(val);
				} catch (IOException e) {
					break;
				}
			}
			
			listener.onLine(null);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
	}


}

package fxms.bas.vo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * 화일 내용 전달을 위한 객체
 * 
 * @author subkjh
 * 
 */
public class FileVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9207888237053374967L;

	public static void main(String[] args) throws Exception {
		FileVo bean = new FileVo(new File("src/com/daims/ems/beans/FileBean.java"));
		System.out.println(bean.getExt());
		System.out.println(bean);
	}

	private byte[] bytes;
	private String filename;

	/** 경로(화일명포함) */
	private String path;

	/**
	 * 
	 * @param file
	 *            보낼 파일
	 * @throws Exception
	 */
	public FileVo(File file) throws Exception {
		filename = file.getName();
		path = file.getPath();

		long size = file.length();
		if (size > Integer.MAX_VALUE)
			throw new Exception("File is too big");

		bytes = new byte[(int) size];

		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(file);
			if (inStream.read(bytes) != size)
				throw new Exception("Can not read");
		} catch (IOException e) {
			throw e;
		} finally {
			if (inStream != null)
				try {
					inStream.close();
				} catch (IOException e) {
					throw e;
				}
		}
	}

	/**
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * 
	 * @return "."이 포함된 확장자<br>
	 *         확장자가 없을 경우 ""
	 */
	public String getExt() {
		try {
			return filename.substring(filename.lastIndexOf('.'));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(bytes);
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return path + "|" + bytes.length;
	}

	/**
	 * 
	 * @param file
	 *            기록될 화일
	 * @throws Exception
	 */
	public void write(File file) throws Exception {
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, false);
			outStream.write(bytes);
			outStream.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					throw e;
				}
		}
	}
}

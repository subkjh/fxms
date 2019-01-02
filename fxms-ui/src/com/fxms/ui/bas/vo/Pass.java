package com.fxms.ui.bas.vo;

import java.io.Serializable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Pass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 637408961553659391L;

	static final byte[] cbc_iv = new byte[] { (byte) 0xff, (byte) 0xdd, (byte) 0xba, (byte) 0xa8, 0x76, 0x54, 0x32,
			0x11 };

	public static void main(String[] args) throws Exception {
		Pass c = new Pass();
		String userId = c.getEncrypt3("subkjh");
		String passwd = c.getEncrypt3("kimjonghoon");

		System.out.println(userId);
		System.out.println(passwd);

		System.out.println(c.getDecrypt3(userId));
		System.out.println(c.getDecrypt3(passwd));
	}

	protected String getDecrypt3(String s) throws Exception {

		byte bytes[] = new byte[s.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(i * 2, (i + 1) * 2), 16);
		}
		return decrypt3(bytes);
	}

	protected String getEncrypt3(String plaintext) throws Exception {
		byte bytes[] = encrypt3(plaintext);
		String ret = "";

		for (byte b : bytes) {
			ret += String.format("%02x", b);
		}

		return ret;
	}

	private String decrypt3(byte cipherbytes[]) throws Exception {

		byte keys[] = new byte[24];

		// Cipher c3ede = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		Cipher c3ede = Cipher.getInstance("DESede/CBC/NoPadding");
		IvParameterSpec ivSpec = new IvParameterSpec(cbc_iv);
		SecretKey key = new SecretKeySpec(keys, "DESede");

		c3ede.init(Cipher.DECRYPT_MODE, key, ivSpec);
		byte[] outputtext = c3ede.doFinal(cipherbytes);

		return new String(trimBytes(outputtext), "utf-8");
	}

	private byte[] encrypt3(String plaintext) throws Exception {

		byte keys[] = new byte[24];

		// Cipher c3ede = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		Cipher c3ede = Cipher.getInstance("DESede/CBC/NoPadding");
		IvParameterSpec ivSpec = new IvParameterSpec(cbc_iv);
		SecretKey key = new SecretKeySpec(keys, "DESede");

		c3ede.init(Cipher.ENCRYPT_MODE, key, ivSpec);

		byte[] inputtext = makeMultiple8bytes(plaintext.getBytes());

		// System.out.println("input length = " + inputtext.length);
		byte[] outputtext = c3ede.doFinal(inputtext);

		return outputtext;
	}

	/**
	 * 입력되는 byte의 길이가 8의 배수여야 한다네..
	 * 
	 * @param bytes
	 * @return
	 */
	private byte[] makeMultiple8bytes(byte bytes[]) {
		if (bytes.length % 8 == 0)
			return bytes;
		int n = 8 - bytes.length % 8;
		byte ret[] = new byte[bytes.length + n];
		for (int i = 0; i < ret.length; i++)
			ret[i] = 0x00;
		System.arraycopy(bytes, 0, ret, 0, bytes.length);
		return ret;
	}

	/**
	 * null 값이 있는 곳까지만 바이트를 제공합니다.
	 * 
	 * @param inputbytes
	 * @return
	 */
	private byte[] trimBytes(byte inputbytes[]) {
		for (int i = 0; i < inputbytes.length; i++) {
			if (inputbytes[i] == 0x00) {
				byte outputbytes[] = new byte[i];
				System.arraycopy(inputbytes, 0, outputbytes, 0, outputbytes.length);
				return outputbytes;
			}
		}

		return inputbytes;
	}

	/**
	 * 입력된 내용이 바른지 확인합니다.
	 * 
	 * @throws Exception
	 */
	public void checkValid() throws Exception {

	}
}

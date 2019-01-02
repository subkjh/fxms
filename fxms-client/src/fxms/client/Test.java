package fxms.client;

import java.io.UnsupportedEncodingException;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		String str = "전북대학교";
		
		System.out.println(str.getBytes().length);
		System.out.println(str.getBytes("utf-8").length);
		System.out.println(str.getBytes("euc-kr").length);
		System.out.println(str.length());
	}
}

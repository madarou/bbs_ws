package com.bbs.util;

import java.security.Key;

public class Encrypyt {
			
	public static byte[] key="anything".getBytes();
	
	public static String en(String str) throws Exception{
		byte[] encryptData = DES.encrypt(str.getBytes(), DES.toKey(key));
		return DES.bytesToHexString(encryptData);
	}
	public static String de(String str) throws Exception{
		byte[] decryptData = DES.decrypt(DES.hexStringToBytes(str), DES.toKey(key));
		return new String(decryptData,"UTF-8");
	}
	
}

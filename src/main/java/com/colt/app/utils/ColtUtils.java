package com.colt.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import com.colt.app.enums.CharacterType;

public class ColtUtils {
	
	public static String[] HEX_CHAR = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
    
	public static String HLLF_DIGIT_CHAR_STRING = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static String HALF_DIGIT_STRING = "1234567890";
	
	public static String[] CHAR_STR = {HLLF_DIGIT_CHAR_STRING, HALF_DIGIT_STRING};
	
	public static String USER_AUTH_DETAILS = "user_auth_details";
	
	public static String IS_USER_LOGIN = "isLogin";
	
	public static String random(int length, CharacterType type) {
		String target = type.getCharStr();
		
		StringBuilder builder = new StringBuilder(length);
		Random random = new Random();
		int maxLength = target.length();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(maxLength);
			builder.append(target.charAt(number));
		}
		
		return builder.toString();
	}
	
	public static String md5encoder(String password) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("md5");
            byte[] result = md5.digest(password.getBytes());
 
            StringBuilder sb = new StringBuilder(32);
            for (int i = 0; i < result.length; i++) {
                byte x = result[i];
                int h = 0x0f & (x >>> 4);
                int l = 0x0f & x;
                sb.append(HEX_CHAR[h]).append(HEX_CHAR[l]);
            }
            return sb.toString();
 
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
	}
}

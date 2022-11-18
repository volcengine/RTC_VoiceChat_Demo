package com.volcengine.vertcdemo.core;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtil {

    private static final String SALT = "&%5123***&&%%$$#@";

    public static String encrypt(String dataStr) {
        try {
            dataStr = dataStr + SALT;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes(StandardCharsets.UTF_8));
            byte[] s = m.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < s.length; i++) {
                result.append(Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}

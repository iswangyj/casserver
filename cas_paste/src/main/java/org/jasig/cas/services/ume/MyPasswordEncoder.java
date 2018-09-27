package org.jasig.cas.services.ume;

import org.jasig.cas.authentication.handler.PasswordEncoder;

import java.security.MessageDigest;

/**
 * @author SxL
 * Created on 9/25/2018 3:29 PM.
 */
public final class MyPasswordEncoder implements PasswordEncoder {
    public MyPasswordEncoder() {
    }

    public String encode(String password) {
        return password == null ? null : MD5(password);
    }

    public static String MD5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }
}
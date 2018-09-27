package org.jasig.cas.services.ume;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

/**
 * @author SxL
 * Created on 9/25/2018 3:30 PM.
 */
public class V3Encipher {
    private static final String m_strKey1 = "zxcvbnm,./asdfg";
    private static final String m_strKey2 = "hjkl;'qwertyuiop";
    private static final String m_strKey3 = "[]\\1234567890-";
    private static final String m_strKey4 = "=` ZXCVBNM<>?:LKJ";
    private static final String m_strKey5 = "HGFDSAQWERTYUI";
    private static final String m_strKey6 = "OP{}|+_)(*&^%$#@!~";

    public V3Encipher() {
    }

    public static String DecodePasswd(String varCode) {
        String des = "";
        String strKey = "";
        if (varCode != null && varCode.length() != 0) {
            strKey = "zxcvbnm,./asdfghjkl;'qwertyuiop[]\\1234567890-=` ZXCVBNM<>?:LKJHGFDSAQWERTYUIOP{}|+_)(*&^%$#@!~";
            if (varCode.length() % 2 == 1) {
                varCode = varCode + "?";
            }

            des = "";

            int n;
            for(n = 0; n <= varCode.length() / 2 - 1; ++n) {
                char b = varCode.charAt(n * 2);
                int a = strKey.indexOf(varCode.charAt(n * 2 + 1));
                des = des + (char)(b ^ a);
            }

            n = des.indexOf(1);
            return n > 0 ? des.substring(0, n) : des;
        } else {
            return "";
        }
    }

    public static String Sha256Encrypt(String salt, String pwd){
        String msg = salt + " " + pwd;
        MessageDigest messageDigest;
        try {
            messageDigest = DigestUtils.getSha256Digest();
            byte[] byteFinal = messageDigest.digest(msg.getBytes("utf-8"));

            return  Hex.encodeHexString(byteFinal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

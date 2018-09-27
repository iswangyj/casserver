package org.jasig.cas.web.casca;

import java.util.Arrays;

/**
 * @author SxL
 * Created on 9/25/2018 5:35 PM.
 */
public class IscJcrypt {
    public static boolean LoadFlag = false;
    public String strResult;
    public int lenResult;
    public int ErrCode;
    public String ErrMsg;

    public native int serverHello(byte[] var1, byte[] var2, byte[] var3);

    public native int serverAuth(byte[] var1, byte[] var2, byte[] var3, byte[] var4);

    public native int ParseCert(byte[] var1, byte[] var2, int var3, byte[] var4);

    public IscJcrypt(String result, int len, int code, String msg) {
        this.strResult = result;
        this.lenResult = len;
        this.ErrCode = code;
        this.ErrMsg = msg;
    }

    public static IscJcrypt serverHello(String strClientHello) {
        IscJcrypt is = new IscJcrypt("", 0, 0, "");
        byte[] flag = new byte[]{48};
        byte[] strout = new byte[4096];
        is.ErrCode = is.serverHello(strClientHello.getBytes(), flag, strout);
        is.strResult = new String(strout);
        is.lenResult = is.strResult.indexOf("\u0000");
        if (is.lenResult > 0) {
            byte[] tmp = new byte[is.lenResult];
            tmp = Arrays.copyOf(strout, is.lenResult);
            is.strResult = new String(tmp);
        }

        return is;
    }

    private int getValidLen(byte[] data) {
        int i;
        for(i = 0; i < data.length && data[i] != 0; ++i) {
            ;
        }

        return i;
    }

    public static IscJcrypt serverAuth(String strClientAuth, String strAuthSrvInfo, String strOCSPIP) {
        IscJcrypt is = new IscJcrypt("", 0, 0, "");
        byte[] strout = new byte[4096];
        is.ErrCode = is.serverAuth(strClientAuth.getBytes(), strAuthSrvInfo.getBytes(), strOCSPIP.getBytes(), strout);
        is.lenResult = is.getValidLen(strout);
        if (is.lenResult > 0) {
            byte[] tmp = new byte[is.lenResult];
            tmp = Arrays.copyOf(strout, is.lenResult);
            is.strResult = new String(tmp);
            is.lenResult = is.strResult.length();
        }

        return is;
    }

    public static IscJcrypt ParseCert(String strClientAuth, String parseStr, int base64Flag) {
        IscJcrypt is = new IscJcrypt("", 0, 0, "");
        byte[] strout = new byte[4096];
        is.ErrCode = is.ParseCert(strClientAuth.getBytes(), parseStr.getBytes(), base64Flag, strout);
        is.lenResult = is.getValidLen(strout);
        if (is.lenResult > 0) {
            byte[] tmp = new byte[is.lenResult];
            tmp = Arrays.copyOf(strout, is.lenResult);
            is.strResult = new String(tmp);
            is.lenResult = is.strResult.length();
        }

        return is;
    }

    public static void main(String[] args) {
        String ClientHello = "eG36y2dUi5CSsgA=";
        IscJcrypt isc = serverHello(ClientHello);
        System.out.println("---[ServerAuth.java]---result1 = " + isc.ErrCode);
        System.out.println("---[ServerAuth.java]---ServerHello is:" + isc.strResult);
        String ServerHello = "NzQzMTo1ZDE3NAE=";
        String ClientAuth = "AwDqLvN/pM9CEg+7XHeI5YVGDTO6Tj8kKCrnyIRofnRQR6F2sgZWCGWZ4x4Ef7/vKoJvX1xDixVAkRE5B/WvZwcE/znmnnYB/y6/EYFWczHEz96ow1Q2Sz0ac4tfJ6SobSowggLhMIICa6ADAgECAgIFUjANBgkqgRyBRQGHaAsFADA+MREwDwYDVQQDDAhDQeS4reW/gzEYMBYGA1UECgwP5Lit5py656CU56m25omAMQ8wDQYDVQQGDAbkuK3lm70wIBgOMjAxMzA0MDcwODEyMDAYDjIwMTcwNDA2MTYwMDAwMIGBMQ8wDQYDVQQGDAbkuK3lm70xEjAQBgNVBAgMCeWMl+S6rOW4gjEPMA0GA1UEAwwG5rOV5rW3MQwwCgYDVQQEDAPms5UxDDAKBgNVBCoMA+a1tzEPMA0GA1UEDAwG5ZGY5belMRwwGgYJKoZIhvcNAQkBDA1maEBrZWdvbmcuY29tMHowFAYHKoZIzj0CAQYJKoEcgUUBh2gIA2IABK7ScBZ6bC5orC6Ba6GInGb013VN54fHUfASma5x3sIhZGf5ovtljO+5wAh/XCg0r2ROa9SmNHWZfc+lHTEzbJmo2TyMZEUkFBTa7Vj9sMrNgm7Hev1pYmRQziZJYigD84IBAKOB6zCB6DAbBgNVHREBAQAEETAPgQ1maEBrZWdvbmcuY29tME4GA1UdIwEBAAREMEKAQJ/r+fz4XB+yYhMgo44u19atVZ4Mi8CNqX7XZdwUZ2pOsPlSCxm7q5gMesq2csPZNXsomJo2w8gGiAjkZFWhXMswTAYDVR0OAQEABEIEQPM6GiaRYdpwqNsPK8UnfeZPT9ux+NzOdDypJYqcbC4FPif3LSirx32RHzz/2FHcd1G10/4pxEd1L0SMMjD6jfAwDgYDVR0PAQEBBAQDAgbAMBsGCCqBHNAUBAEBAQEABAygChMIMjAxMzA0MDcwDQYJKoEcgUUBh2gLBQADYQClvhGT0SMI4H3uzvztBaxp4cMjTb4acVPZBBJQ8M8ZyqACtgp1X85VEq9TgIPblsoyxttwWoEaLx8myzWl3L7cjlFXyjuVPNx2LhiYNkTrPLdhkGNRrcmrb5infkZHXwY=";
        String strOCSPIP = "192.168.0.97";
        isc = serverAuth(ClientAuth, ServerHello, strOCSPIP);
        System.out.println("\n---[ServerAuth.java]---result2 = " + isc.ErrCode);
        System.out.println("---[ServerAuth.java]--- output is:" + isc.strResult + ", outlen is :" + isc.strResult.length());
        String certItem = "cn";
        isc = ParseCert(ClientAuth, certItem, 0);
        System.out.println("\n---[ServerAuth.java]---result3 = " + isc.ErrCode);
        System.out.println("---[ServerAuth.java]--- certItem is: " + certItem + ",output is:" + isc.strResult + ", outlen is :" + isc.strResult.length());
    }

    static {
        if (!LoadFlag) {
            System.out.println("***********" + System.getProperty("java.library.path") + "*********************");
            System.loadLibrary("AuthNative_jni");
            LoadFlag = true;
        }

    }
}

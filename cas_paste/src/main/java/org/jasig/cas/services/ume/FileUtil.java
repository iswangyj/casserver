package org.jasig.cas.services.ume;


import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author SxL
 * Created on 9/25/2018 3:26 PM.
 */
public class FileUtil {
    private static Logger log = Logger.getLogger(FileUtil.class);
    private static final String FILE_SEPERATOR_USING = "/";
    private static final String FILE_SEPERATOR = "\\\\";
    private static final String regexFileNameExcluded = "[\\\\/*?:\"<>|]";

    public FileUtil() {
    }

    public static String getString4InputStream(InputStream is, String inputCharset) {
        if (null == inputCharset || "".equals(inputCharset)) {
            inputCharset = "UTF-8";
        }

        ByteArrayOutputStream bos = null;

        try {
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            boolean var4 = false;

            int len;
            while((len = is.read(b, 0, 1024)) > 0) {
                bos.write(b, 0, len);
            }

            String res = new String(bos.toByteArray(), inputCharset);
            is.close();
            bos.close();
            String var6 = res;
            return var6;
        } catch (Exception var16) {
            log.error(var16, var16);
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

        return null;
    }
}

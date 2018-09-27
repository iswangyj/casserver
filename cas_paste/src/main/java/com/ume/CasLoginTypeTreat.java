package com.ume;

import auth.IscJcrypt;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.services.ume.CasServerCfgPub;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.context.ContextLoader;
import sun.misc.BASE64Decoder;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 1:08 PM.
 */
public class CasLoginTypeTreat {
    public static final String ERR_AUTH = "ERR_AUTH";
    public static final String ERR_UID = "ERR_UID";
    public static final String ERR_CADOC = "ERR_CADOC";
    public static final Map<String, String> mapErrMsg = new HashMap<String, String>() {
        {
            this.put("ERR_AUTH", "璇佷功鏉冮檺楠岃瘉澶辫触锛�");
            this.put("ERR_UID", "瑙ｆ瀽韬\ue0a1唤淇℃伅澶辫触锛�");
            this.put("ERR_CADOC", "鍙傛暟缁撴瀯閿欒\ue1e4锛�");
        }
    };
    private Logger log = Logger.getLogger(CasLoginTypeTreat.class);
    @NotNull
    private DataSource dataSource = null;
    @NotNull
    private JdbcTemplate jdbcTemplate = null;
    private String sql = "select count(1) count from cas_open_client_ip where ip=? and open_status='1'";
    private String sqlCheckAuth = "select count(1) count from cas_open_client_ip where ip=? and open_status='1' and union_account=?";
    private String sqlGetUsernameByUserSn = "select account from spp_user where user_sn=?";
    private String sqlGetUsernameBySysparam = "select count(1) count from fs_sys_para where parakey='LOGIN_OPEN_USER_PASSWORD_ALL' and paravalue1='true'";

    public CasLoginTypeTreat() {
        this.dataSource = (DataSource)ContextLoader.getCurrentWebApplicationContext().getBean("dataSourceDm");
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    public boolean getLoginType(String ipAddress) {
        boolean bUserName = false;
        SqlRowSet rsPara = this.jdbcTemplate.queryForRowSet(this.sqlGetUsernameBySysparam);
        rsPara.first();
        if (null != rsPara && !rsPara.isAfterLast()) {
            int count = rsPara.getInt("count");
            if (count > 0) {
                bUserName = true;
            }
        }

        if (!bUserName) {
            SqlRowSet rs = this.jdbcTemplate.queryForRowSet(this.sql, new Object[]{ipAddress});
            rs.first();
            if (null != rs && !rs.isAfterLast()) {
                int count = rs.getInt("count");
                if (count > 0) {
                    bUserName = true;
                }
            }
        }

        return bUserName;
    }

    public boolean checkUserNameAuth(UsernamePasswordCredential credential) {
        String username = credential.getUsername();
        String bSpecial = credential.getbSpecial();
        if (null != bSpecial && !"".equals(bSpecial)) {
            boolean bAuth = false;

            try {
                String ipAddress = null;

                try {
                    ipAddress = new String((new BASE64Decoder()).decodeBuffer(bSpecial), "utf-8");
                } catch (IOException var8) {
                    var8.printStackTrace();
                }

                if (null != ipAddress && !"".equals(ipAddress)) {
                    SqlRowSet rs = this.jdbcTemplate.queryForRowSet(this.sqlCheckAuth, new Object[]{ipAddress, username});
                    rs.first();
                    if (null != rs && !rs.isAfterLast()) {
                        int count = rs.getInt("count");
                        if (count > 0) {
                            bAuth = true;
                        }
                    }

                    return bAuth;
                } else {
                    return bAuth;
                }
            } catch (Exception var9) {
                this.log.error(var9, var9);
                return bAuth;
            }
        } else {
            return true;
        }
    }

    public void setUsernameByCaInfo(UsernamePasswordCredential credential) throws Exception {
        String bSpecial = credential.getbSpecial();
        if (null == bSpecial || "".equals(bSpecial)) {
            SqlRowSet rsUsername = this.jdbcTemplate.queryForRowSet(this.sqlGetUsernameByUserSn, new Object[]{credential.getUsername()});
            rsUsername.first();
            if (null != rsUsername && !rsUsername.isAfterLast() && !rsUsername.wasNull()) {
                try {
                    String username = rsUsername.getString("account");
                    credential.setUsername(username);
                } catch (Exception var5) {
                    this.log.error(var5, var5);
                    throw new Exception("褰撳墠璇佷功娌℃湁瀵瑰簲鐨勭粺涓�璐﹀彿淇℃伅锛岃\ue1ec鑱旂郴闃熶紞绠＄悊绯荤粺鎺ㄩ�佺浉鍏虫暟鎹\ue1c6紒");
                }
            } else {
                throw new Exception("褰撳墠璇佷功娌℃湁瀵瑰簲鐨勭粺涓�璐﹀彿淇℃伅锛岃\ue1ec鑱旂郴闃熶紞绠＄悊绯荤粺鎺ㄩ�佺浉鍏虫暟鎹\ue1c6紒");
            }
        }
    }

    public static String parseCAKeyUid(String strDoc) {
        if (null != strDoc && !"".equals(strDoc)) {
            Object var1 = null;

            try {
                return parseCAKeyUidOrEmail(strDoc);
            } catch (Exception var3) {
                var3.printStackTrace();
                return "ERR_CADOC";
            }
        } else {
            return "ERR_CADOC";
        }
    }

    private static String parseCAKeyUid(Document doc) throws Exception {
        try {
            String serverIP = CasServerCfgPub.getProperty("CA_CHECK_SERVER_IP");
            boolean bCheckAuth = false;
            if (null != serverIP && !"".equals(serverIP)) {
                bCheckAuth = true;
            }

            String str_clientAuth = doc.selectSingleNode("//RLT/ClientAuth").getText();
            if (null != str_clientAuth && !"".equals(str_clientAuth)) {
                int check = 0;
                String key;
                IscJcrypt caInfo;
                if (bCheckAuth) {
                    key = doc.selectSingleNode("//RLT/ServerHello").getText();
                    if (null == key || "".equals(key)) {
                        return "ERR_CADOC";
                    }

                    caInfo = IscJcrypt.serverAuth(str_clientAuth, key, serverIP);
                    check = caInfo.ErrCode;
                    if (check != 0) {
                        return "ERR_AUTH";
                    }
                }

                key = CasServerCfgPub.getProperty("CAS_LOGIN_TYPE");
                if (null == key || "".equals(key)) {
                    key = "uid";
                }

                caInfo = IscJcrypt.ParseCert(str_clientAuth, key, 0);
                return check == 0 ? caInfo.strResult : "ERR_UID";
            } else {
                return "ERR_CADOC";
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return "ERR_CADOC";
        } catch (Throwable var8) {
            var8.printStackTrace();
            return "ERR_CADOC";
        }
    }

    private static String parseCAKeyUidOrEmail(String str_clientAuth) throws Exception {
        try {
            if (null != str_clientAuth && !"".equals(str_clientAuth)) {
                boolean check = false;
                String key = CasServerCfgPub.getProperty("CA_LOGIN_TYPE");
                if (null == key || "".equals(key)) {
                    key = "uid";
                }

                IscJcrypt caInfo = IscJcrypt.ParseCert(str_clientAuth, key, 0);
                return !check ? caInfo.strResult : "ERR_UID";
            } else {
                return "ERR_CADOC";
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            return "ERR_CADOC";
        } catch (Throwable var5) {
            var5.printStackTrace();
            return "ERR_CADOC";
        }
    }
}

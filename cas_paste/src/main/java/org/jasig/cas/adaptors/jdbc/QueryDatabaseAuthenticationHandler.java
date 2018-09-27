package org.jasig.cas.adaptors.jdbc;

import com.ume.CasLoginTypeTreat;
import com.ume.TestUtil;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.dao.SppUserMapper;
import org.jasig.cas.entity.SppUser;
import org.jasig.cas.services.ume.CasServerCfgPub;
import org.jasig.cas.services.ume.MyPasswordEncoder;
import org.jasig.cas.services.ume.V3Encipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import sun.misc.BASE64Decoder;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 1:44 PM.
 */
public class QueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
    @NotNull
    private String sql;

    @Autowired
    private SppUserMapper sppUserMapper;
    private String LOGIN_TYPE_PASSWORD = "1";
    private String LOGIN_TYPE_CA = "2";
    private String sqlGetUsernameByUserSn = "select account from spp_user where user_sn=? or mailfrom=?";
    private String sqlGetUsernameBySysparam = "select count(1) count from fs_sys_para where parakey='LOGIN_OPEN_USER_PASSWORD_ALL' and paravalue1='true'";

    public QueryDatabaseAuthenticationHandler() {
    }

    @Override
    protected final HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential) throws GeneralSecurityException, PreventedException {
        String username = credential.getUsername();
        String bSpecial = credential.getbSpecial();
        String strCA = credential.getStrCA();
        SimplePrincipal sp = null;
        String userUniqueName = null;
        String url = credential.getService();
        String service = CasServerCfgPub.getServicePath(url);
        SppUser user = sppUserMapper.selectByAccount(username);
        try {
            String encryptedPassword;
            if (CasServerCfgPub.getMode()) {
                encryptedPassword = this.getPasswordEncoder().encode(credential.getPassword());
                if (null != strCA && !"".equals(strCA)) {
                    encryptedPassword = this.LOGIN_TYPE_CA;
                    this.setUsernameByCaInfo(credential);
                    username = credential.getUsername();
                } else {
                    final String dbPassword = getJdbcTemplate().queryForObject(this.sql, String.class, username);
                    String dp = V3Encipher.Sha256Encrypt(user.getSalt(), credential.getPassword());

                    if (!dp.equals(dbPassword)) {
                        throw new FailedLoginException("Password does not match value on record.");
                    }
                }
                sp = new SimplePrincipal(username);
            } else {
                encryptedPassword = MyPasswordEncoder.MD5(credential.getPassword());

                try {
                    encryptedPassword = (String) this.getJdbcTemplate().queryForObject(this.sql, String.class, new Object[]{username});
                    if (null == encryptedPassword || "".equals(encryptedPassword) || null == encryptedPassword || "".equals(encryptedPassword)) {
                        throw new Exception("瀵嗙爜涓嶅彲浠ヤ负绌猴紒");
                    }

                    if (!encryptedPassword.equals(encryptedPassword)) {
                        throw new Exception("楠岃瘉澶辫触锛�");
                    }

                    userUniqueName = username;
                } catch (Exception var14) {
                    var14.printStackTrace();
                    sql = "select APP_PASSWORD from cas_sys_user_apps u join cas_sys_apps a on a.sid=u.app_sid where (a.ADDRESS_HTTP like '" + service + "%' or a.ADDRESS_HTTPS like '" + service + "%') and app_username=?";
                    encryptedPassword = (String) this.getJdbcTemplate().queryForObject(sql, String.class, new Object[]{username});
                }

                if (!encryptedPassword.equals(encryptedPassword)) {
                    throw new FailedLoginException("Password does not match value on record.");
                }

                String userUniqueSID = null;
                if (null == userUniqueName) {
                    sql = "select USER_UNIQUE_SID from cas_sys_user_apps u join cas_sys_apps a on a.sid=u.app_sid where (a.ADDRESS_HTTP like '" + service + "%' or a.ADDRESS_HTTPS like '" + service + "%') and app_username=?";
                    userUniqueSID = (String) this.getJdbcTemplate().queryForObject(sql, String.class, new Object[]{username});
                    sql = "select username_unique from cas_sys_user_unique where sid=?";
                    userUniqueName = (String) this.getJdbcTemplate().queryForObject(sql, String.class, new Object[]{userUniqueSID});
                }

                sp = new SimplePrincipal(userUniqueName);
                credential.setUsername(userUniqueName);
            }
        } catch (IncorrectResultSizeDataAccessException var15) {
            if (var15.getActualSize() == 0) {
                throw new AccountNotFoundException(username + " not found with SQL query");
            }

            throw new FailedLoginException("Multiple records found for " + username);
        } catch (DataAccessException var16) {
            throw new PreventedException("SQL exception while executing query for " + username, var16);
        }

        return this.createHandlerResult(credential, sp, (List) null);
    }

    private void setUsernameByCaInfo(UsernamePasswordCredential credential) throws FailedLoginException {
        String strCA = credential.getStrCA();
        String keyUidOrEmail = CasLoginTypeTreat.parseCAKeyUid(strCA);
        String exceptionMsg = (String) CasLoginTypeTreat.mapErrMsg.get(keyUidOrEmail);
        if (null != exceptionMsg) {
            throw new FailedLoginException(exceptionMsg);
        } else {
            SqlRowSet rsUsername = this.getJdbcTemplate().queryForRowSet(this.sqlGetUsernameByUserSn, new Object[]{keyUidOrEmail, keyUidOrEmail});
            rsUsername.first();
            if (null != rsUsername && !rsUsername.isAfterLast() && !rsUsername.wasNull()) {
                try {
                    String username = rsUsername.getString("account");
                    credential.setUsername(username);
                } catch (Exception var7) {
                    throw new FailedLoginException("");
                }
            } else {
                throw new FailedLoginException("");
            }
        }
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
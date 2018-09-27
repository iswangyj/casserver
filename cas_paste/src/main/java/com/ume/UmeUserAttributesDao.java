package com.ume;

import org.jasig.cas.dao.SppUserMapper;
import org.jasig.cas.entity.SppUser;
import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AttributeNamedPersonImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 1:23 PM.
 */
public class UmeUserAttributesDao extends StubPersonAttributeDao {
    @NotNull
    private JdbcTemplate jdbcTemplate;
    @NotNull
    private DataSource dataSource;
    @Autowired
    private SppUserMapper sppUserMapper;

    public UmeUserAttributesDao() {
    }

    @Override
    public IPersonAttributes getPerson(String uid) {
        SppUser user = sppUserMapper.selectByAccount(uid);

        Map<String, List<Object>> attribute = new HashMap<String, List<Object>>(16);
//        attribute.put("user", Collections.singletonList((Object)user));
        attribute.put("accountId", Collections.singletonList((Object) user.getAccountId()));
        attribute.put("name", Collections.singletonList((Object) user.getAccount()));
        attribute.put("realName", Collections.singletonList((Object) user.getName()));

        return new AttributeNamedPersonImpl(attribute);
//        if (uid == null) {
//            throw new IllegalArgumentException("Illegal to invoke getPerson(String) with a null argument.");
//        } else {
//            Map<String, List<Object>> attributes = new HashMap();
//            String userUniqueSID = null;
//            String split = "@-#-@";
//            String attributeSql;
//            if (CasServerCfgPub.getMode()) {
//                attributeSql = "select * from fs_org_person where account=?";
//                SqlRowSet rsInfo = this.jdbcTemplate.queryForRowSet(attributeSql, new Object[]{uid});
//                rsInfo.first();
//                if (null != rsInfo && !rsInfo.isAfterLast()) {
//                    ArrayList lstUserInfo = new ArrayList();
//
//                    try {
//                        userUniqueSID = rsInfo.getString("account_id");
//                        if (null != userUniqueSID && !"".equals(userUniqueSID)) {
//                            lstUserInfo.add("RYBH@-#-@" + userUniqueSID);
//                        }
//                    } catch (Exception var13) {
//                        ;
//                    }
//
//                    String loginType;
//                    try {
//                        loginType = rsInfo.getString("department");
//                        if (null != loginType && !"".equals(loginType)) {
//                            lstUserInfo.add("SZBM@-#-@" + loginType);
//                        }
//                    } catch (Exception var12) {
//                        ;
//                    }
//
//                    try {
//                        loginType = rsInfo.getString("name");
//                        if (null != loginType && !"".equals(loginType)) {
//                            lstUserInfo.add("RYXM@-#-@" + loginType);
//                        }
//                    } catch (Exception var11) {
//                        ;
//                    }
//
//                    try {
//                        loginType = rsInfo.getString("cert_style");
//                        if (null != loginType && !"".equals(loginType)) {
//                            lstUserInfo.add("DLFS@-#-@" + loginType);
//                        }
//                    } catch (Exception var10) {
//                        ;
//                    }
//
//                    attributes.put("atts", lstUserInfo);
//                }
//            } else {
//                attributeSql = "select SID from cas_sys_user_unique where username_unique=?";
//                userUniqueSID = (String)this.jdbcTemplate.queryForObject(attributeSql, String.class, new Object[]{uid});
//            }
//
//            attributeSql = "select concat(concat(replace(address_http,'&','@##@'),'@-#-@'),app_username) info from cas_sys_user_apps u join cas_sys_apps a on a.sid=u.app_sid where user_unique_sid=? and address_http is not null union select concat(concat(replace(address_https,'&','@##@'),'@-#-@'),app_username) info from cas_sys_user_apps u join cas_sys_apps a on a.sid=u.app_sid where user_unique_sid=? and address_https is not null";
//            List<Object> lstAttribute = this.jdbcTemplate.queryForList("select concat(concat(replace(address_http,'&','@##@'),'@-#-@'),app_username) info from cas_sys_user_apps u join cas_sys_apps a on a.sid=u.app_sid where user_unique_sid=? and address_http is not null union select concat(concat(replace(address_https,'&','@##@'),'@-#-@'),app_username) info from cas_sys_user_apps u join cas_sys_apps a on a.sid=u.app_sid where user_unique_sid=? and address_https is not null", Object.class, new Object[]{userUniqueSID, userUniqueSID});
//            attributes.put("apps", lstAttribute);
//            return new AttributeNamedPersonImpl(attributes);
//        }
    }

    public final void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }
}

package com.ume;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.context.ContextLoader;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 1:19 PM.
 */
public class CasLoginTypeTreatSingleton {
    private static CasLoginTypeTreatSingleton cltt = null;
    @NotNull
    private DataSource dataSource = null;
    @NotNull
    private JdbcTemplate jdbcTemplate = null;
    private String sql = "select count(1) count from cas_open_client_ip where ip=? and open_status='1'";

    private CasLoginTypeTreatSingleton() {
        this.dataSource = (DataSource)ContextLoader.getCurrentWebApplicationContext().getBean("dataSourceDm");
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    public static synchronized CasLoginTypeTreatSingleton getInstance() {
        if (null == cltt) {
            cltt = new CasLoginTypeTreatSingleton();
        }

        return cltt;
    }

    public boolean getLoginType(String ipAddress) {
        boolean bUserName = false;
        SqlRowSet rs = this.jdbcTemplate.queryForRowSet(this.sql, new Object[]{ipAddress});
        rs.first();
        if (null != rs && !rs.isAfterLast()) {
            int count = rs.getInt("count");
            if (count > 0) {
                bUserName = true;
            }
        }

        return bUserName;
    }
}

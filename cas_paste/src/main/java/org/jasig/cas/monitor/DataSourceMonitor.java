package org.jasig.cas.monitor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author SxL
 * Created on 9/25/2018 2:58 PM.
 */
public class DataSourceMonitor extends AbstractPoolMonitor {
    @NotNull
    private final JdbcTemplate jdbcTemplate;
    @NotNull
    private String validationQuery;

    public DataSourceMonitor(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setValidationQuery(String query) {
        this.validationQuery = query;
    }

    @Override
    protected StatusCode checkPool() throws Exception {
        return (StatusCode)this.jdbcTemplate.query(this.validationQuery, new ResultSetExtractor<StatusCode>() {
            @Override
            public StatusCode extractData(ResultSet rs) throws SQLException {
                return rs.next() ? StatusCode.OK : StatusCode.WARN;
            }
        });
    }

    @Override
    protected int getIdleCount() {
        return -1;
    }

    @Override
    protected int getActiveCount() {
        return -1;
    }
}

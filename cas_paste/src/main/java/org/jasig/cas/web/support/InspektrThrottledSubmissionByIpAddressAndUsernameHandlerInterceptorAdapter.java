package org.jasig.cas.web.support;

import com.github.inspektr.audit.AuditActionContext;
import com.github.inspektr.audit.AuditPointRuntimeInfo;
import com.github.inspektr.audit.AuditTrailManager;
import com.github.inspektr.common.web.ClientInfo;
import com.github.inspektr.common.web.ClientInfoHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:56 PM.
 */
public class InspektrThrottledSubmissionByIpAddressAndUsernameHandlerInterceptorAdapter extends AbstractThrottledSubmissionHandlerInterceptorAdapter {
    private static final String DEFAULT_APPLICATION_CODE = "CAS";
    private static final String DEFAULT_AUTHN_FAILED_ACTION = "AUTHENTICATION_FAILED";
    private static final String INSPEKTR_ACTION = "THROTTLED_LOGIN_ATTEMPT";
    private final AuditTrailManager auditTrailManager;
    private final JdbcTemplate jdbcTemplate;
    private String applicationCode = "CAS";
    private String authenticationFailureCode = "AUTHENTICATION_FAILED";

    public InspektrThrottledSubmissionByIpAddressAndUsernameHandlerInterceptorAdapter(AuditTrailManager auditTrailManager, DataSource dataSource) {
        this.auditTrailManager = auditTrailManager;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected boolean exceedsThreshold(HttpServletRequest request) {
        String query = "SELECT AUD_DATE FROM COM_AUDIT_TRAIL WHERE AUD_CLIENT_IP = ? AND AUD_USER = ? AND AUD_ACTION = ? AND APPLIC_CD = ? AND AUD_DATE >= ? ORDER BY AUD_DATE DESC";
        String userToUse = this.constructUsername(request, this.getUsernameParameter());
        Calendar cutoff = Calendar.getInstance();
        cutoff.add(13, -1 * this.getFailureRangeInSeconds());
        List<Timestamp> failures = this.jdbcTemplate.query("SELECT AUD_DATE FROM COM_AUDIT_TRAIL WHERE AUD_CLIENT_IP = ? AND AUD_USER = ? AND AUD_ACTION = ? AND APPLIC_CD = ? AND AUD_DATE >= ? ORDER BY AUD_DATE DESC", new Object[]{request.getRemoteAddr(), userToUse, this.authenticationFailureCode, this.applicationCode, cutoff.getTime()}, new int[]{12, 12, 12, 12, 93}, new RowMapper<Timestamp>() {
            @Override
            public Timestamp mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getTimestamp(1);
            }
        });
        if (failures.size() < 2) {
            return false;
        } else {
            return 1000.0D / (double)(((Timestamp)failures.get(0)).getTime() - ((Timestamp)failures.get(1)).getTime()) > this.getThresholdRate();
        }
    }

    @Override
    protected void recordSubmissionFailure(HttpServletRequest request) {
    }

    @Override
    protected void recordThrottle(HttpServletRequest request) {
        super.recordThrottle(request);
        String userToUse = this.constructUsername(request, this.getUsernameParameter());
        ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
        AuditPointRuntimeInfo auditPointRuntimeInfo = new AuditPointRuntimeInfo() {
            private static final long serialVersionUID = 1L;

            @Override
            public String asString() {
                return String.format("%s.recordThrottle()", this.getClass().getName());
            }
        };
        AuditActionContext context = new AuditActionContext(userToUse, userToUse, "THROTTLED_LOGIN_ATTEMPT", this.applicationCode, new Date(), clientInfo.getClientIpAddress(), clientInfo.getServerIpAddress(), auditPointRuntimeInfo);
        this.auditTrailManager.record(context);
    }

    public final void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public final void setAuthenticationFailureCode(String authenticationFailureCode) {
        this.authenticationFailureCode = authenticationFailureCode;
    }

    protected String constructUsername(HttpServletRequest request, String usernameParameter) {
        String username = request.getParameter(usernameParameter);
        return "[username: " + (username != null ? username : "") + "]";
    }
}

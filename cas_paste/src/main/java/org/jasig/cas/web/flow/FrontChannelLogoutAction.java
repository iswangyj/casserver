package org.jasig.cas.web.flow;

import org.jasig.cas.logout.LogoutManager;
import org.jasig.cas.logout.LogoutRequest;
import org.jasig.cas.logout.LogoutRequestStatus;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:42 PM.
 */
public final class FrontChannelLogoutAction extends AbstractLogoutAction {
    @NotNull
    private final LogoutManager logoutManager;

    public FrontChannelLogoutAction(LogoutManager logoutManager) {
        this.logoutManager = logoutManager;
    }

    @Override
    protected Event doInternalExecute(HttpServletRequest request, HttpServletResponse response, RequestContext context) throws Exception {
        List<LogoutRequest> logoutRequests = WebUtils.getLogoutRequests(context);
        Integer startIndex = this.getLogoutIndex(context);
        if (logoutRequests != null && startIndex != null) {
            for(int i = startIndex; i < logoutRequests.size(); ++i) {
                LogoutRequest logoutRequest = (LogoutRequest)logoutRequests.get(i);
                if (logoutRequest.getStatus() == LogoutRequestStatus.NOT_ATTEMPTED) {
                    logoutRequest.setStatus(LogoutRequestStatus.SUCCESS);
                    this.putLogoutIndex(context, i + 1);
                    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(logoutRequest.getService().getId());
                    builder.queryParam("SAMLRequest", new Object[]{URLEncoder.encode(this.logoutManager.createFrontChannelLogoutMessage(logoutRequest), "UTF-8")});
                    return this.result("redirectApp", "logoutUrl", builder.build().toUriString());
                }
            }
        }

        return new Event(this, "finish");
    }

    public LogoutManager getLogoutManager() {
        return this.logoutManager;
    }
}
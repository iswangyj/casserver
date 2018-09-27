package org.jasig.cas.web.flow;

import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.logout.LogoutRequest;
import org.jasig.cas.logout.LogoutRequestStatus;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:45 PM.
 */
public final class LogoutAction extends AbstractLogoutAction {
    @NotNull
    private ServicesManager servicesManager;
    private boolean followServiceRedirects;

    public LogoutAction() {
    }

    @Override
    protected Event doInternalExecute(HttpServletRequest request, HttpServletResponse response, RequestContext context) throws Exception {
        boolean needFrontSlo = false;
        this.putLogoutIndex(context, 0);
        List<LogoutRequest> logoutRequests = WebUtils.getLogoutRequests(context);
        if (logoutRequests != null) {
            Iterator var6 = logoutRequests.iterator();

            while(var6.hasNext()) {
                LogoutRequest logoutRequest = (LogoutRequest)var6.next();
                if (logoutRequest.getStatus() == LogoutRequestStatus.NOT_ATTEMPTED) {
                    needFrontSlo = true;
                    break;
                }
            }
        }

        String service = request.getParameter("service");
        String msg = request.getParameter("em");
        if (this.followServiceRedirects && service != null) {
            RegisteredService rService = this.servicesManager.findServiceBy(new SimpleWebApplicationServiceImpl(service));
            if (rService != null && rService.isEnabled()) {
                context.getFlowScope().put("logoutRedirectUrl", service);
                if (null != msg && !"".equals(msg)) {
                    context.getFlowScope().put("umeMsg", URLEncoder.encode(msg, "utf-8"));
                }
            }
        }

        return needFrontSlo ? new Event(this, "front") : new Event(this, "finish");
    }

    public void setFollowServiceRedirects(boolean followServiceRedirects) {
        this.followServiceRedirects = followServiceRedirects;
    }

    public void setServicesManager(ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }
}

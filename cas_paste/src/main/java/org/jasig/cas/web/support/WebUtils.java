package org.jasig.cas.web.support;

import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.logout.LogoutRequest;
import org.jasig.cas.services.ume.CasServerCfgPub;
import org.springframework.util.Assert;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 5:57 PM.
 */
public final class WebUtils {
    public static final String CAS_ACCESS_DENIED_REASON = "CAS_ACCESS_DENIED_REASON";

    private WebUtils() {
    }

    public static HttpServletRequest getHttpServletRequest(RequestContext context) {
        Assert.isInstanceOf(ServletExternalContext.class, context.getExternalContext(), "Cannot obtain HttpServletRequest from event of type: " + context.getExternalContext().getClass().getName());
        return (HttpServletRequest)context.getExternalContext().getNativeRequest();
    }

    public static HttpServletResponse getHttpServletResponse(RequestContext context) {
        Assert.isInstanceOf(ServletExternalContext.class, context.getExternalContext(), "Cannot obtain HttpServletResponse from event of type: " + context.getExternalContext().getClass().getName());
        return (HttpServletResponse)context.getExternalContext().getNativeResponse();
    }

    public static WebApplicationService getService(List<ArgumentExtractor> argumentExtractors, HttpServletRequest request) {
        if (null == request.getParameter("service") && null != CasServerCfgPub.getPortalPage() && !"".equals(CasServerCfgPub.getPortalPage())) {
            request.setAttribute("service", CasServerCfgPub.getPortalPage());
        }

        Iterator var2 = argumentExtractors.iterator();

        WebApplicationService service;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            ArgumentExtractor argumentExtractor = (ArgumentExtractor)var2.next();
            service = argumentExtractor.extractService(request);
        } while(service == null);

        return service;
    }

    public static WebApplicationService getService(List<ArgumentExtractor> argumentExtractors, RequestContext context) {
        HttpServletRequest request = getHttpServletRequest(context);
        return getService(argumentExtractors, request);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Assert.notNull(request, "Request must not be null");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie[] arr$ = cookies;
            int len$ = cookies.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Cookie cookie = arr$[i$];
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;
    }

    public static WebApplicationService getService(RequestContext context) {
        return (WebApplicationService)context.getFlowScope().get("service");
    }

    public static void putTicketGrantingTicketInRequestScope(RequestContext context, String ticketValue) {
        context.getRequestScope().put("ticketGrantingTicketId", ticketValue);
    }

    public static void putTicketGrantingTicketInFlowScope(RequestContext context, String ticketValue) {
        context.getFlowScope().put("ticketGrantingTicketId", ticketValue);
    }

    public static String getTicketGrantingTicketId(RequestContext context) {
        String tgtFromRequest = (String)context.getRequestScope().get("ticketGrantingTicketId");
        String tgtFromFlow = (String)context.getFlowScope().get("ticketGrantingTicketId");
        return tgtFromRequest != null ? tgtFromRequest : tgtFromFlow;
    }

    public static void putServiceTicketInRequestScope(RequestContext context, String ticketValue) {
        context.getRequestScope().put("serviceTicketId", ticketValue);
    }

    public static String getServiceTicketFromRequestScope(RequestContext context) {
        return context.getRequestScope().getString("serviceTicketId");
    }

    public static void putLoginTicket(RequestContext context, String ticket) {
        context.getFlowScope().put("loginTicket", ticket);
    }

    public static String getLoginTicketFromFlowScope(RequestContext context) {
        String lt = (String)context.getFlowScope().remove("loginTicket");
        return lt != null ? lt : "";
    }

    public static String getLoginTicketFromRequest(RequestContext context) {
        return context.getRequestParameters().get("lt");
    }

    public static void putLogoutRequests(RequestContext context, List<LogoutRequest> requests) {
        context.getFlowScope().put("logoutRequests", requests);
    }

    public static List<LogoutRequest> getLogoutRequests(RequestContext context) {
        return (List)context.getFlowScope().get("logoutRequests");
    }
}

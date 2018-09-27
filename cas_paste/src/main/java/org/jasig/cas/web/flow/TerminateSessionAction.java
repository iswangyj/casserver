package org.jasig.cas.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.EventFactorySupport;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 5:47 PM.
 */
public class TerminateSessionAction {
    private final EventFactorySupport eventFactorySupport = new EventFactorySupport();
    @NotNull
    private final CentralAuthenticationService centralAuthenticationService;
    @NotNull
    private final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
    @NotNull
    private final CookieRetrievingCookieGenerator warnCookieGenerator;

    public TerminateSessionAction(CentralAuthenticationService cas, CookieRetrievingCookieGenerator tgtCookieGenerator, CookieRetrievingCookieGenerator warnCookieGenerator) {
        this.centralAuthenticationService = cas;
        this.ticketGrantingTicketCookieGenerator = tgtCookieGenerator;
        this.warnCookieGenerator = warnCookieGenerator;
    }

    public Event terminate(RequestContext context) {
        String tgtId = WebUtils.getTicketGrantingTicketId(context);
        HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        if (tgtId == null) {
            tgtId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
        }

        if (tgtId != null) {
            WebUtils.putLogoutRequests(context, this.centralAuthenticationService.destroyTicketGrantingTicket(tgtId));
        }

        HttpServletResponse response = WebUtils.getHttpServletResponse(context);
        this.ticketGrantingTicketCookieGenerator.removeCookie(response);
        this.warnCookieGenerator.removeCookie(response);
        return this.eventFactorySupport.success(this);
    }
}

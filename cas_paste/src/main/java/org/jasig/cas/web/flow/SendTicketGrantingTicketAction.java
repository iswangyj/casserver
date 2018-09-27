package org.jasig.cas.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 5:47 PM.
 */
public final class SendTicketGrantingTicketAction extends AbstractAction {
    @NotNull
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    public SendTicketGrantingTicketAction() {
    }

    @Override
    protected Event doExecute(RequestContext context) {
        String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
        String ticketGrantingTicketValueFromCookie = (String)context.getFlowScope().get("ticketGrantingTicketId");
        if (ticketGrantingTicketId == null) {
            return this.success();
        } else {
            this.ticketGrantingTicketCookieGenerator.addCookie(WebUtils.getHttpServletRequest(context), WebUtils.getHttpServletResponse(context), ticketGrantingTicketId);
            if (ticketGrantingTicketValueFromCookie != null && !ticketGrantingTicketId.equals(ticketGrantingTicketValueFromCookie)) {
                this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketValueFromCookie);
            }

            return this.success();
        }
    }

    public void setTicketGrantingTicketCookieGenerator(CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
}


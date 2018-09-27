package org.jasig.cas.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 5:44 PM.
 */
public final class GenerateServiceTicketAction extends AbstractAction {
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    public GenerateServiceTicketAction() {
    }

    @Override
    protected Event doExecute(RequestContext context) {
        Service service = WebUtils.getService(context);
        String ticketGrantingTicket = WebUtils.getTicketGrantingTicketId(context);

        try {
            String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicket, service);
            WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
            return this.success();
        } catch (TicketException var5) {
            return this.isGatewayPresent(context) ? this.result("gateway") : this.error();
        }
    }

    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    protected boolean isGatewayPresent(RequestContext context) {
        return StringUtils.hasText(context.getExternalContext().getRequestParameterMap().get("gateway"));
    }
}

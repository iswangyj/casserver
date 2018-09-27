package org.jasig.cas.web.flow;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
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
 * Created on 9/25/2018 5:36 PM.
 */
public abstract class AbstractNonInteractiveCredentialsAction extends AbstractAction {
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    public AbstractNonInteractiveCredentialsAction() {
    }

    protected final boolean isRenewPresent(RequestContext context) {
        return StringUtils.hasText(context.getRequestParameters().get("renew"));
    }

    @Override
    protected final Event doExecute(RequestContext context) {
        Credential credential = this.constructCredentialsFromRequest(context);
        if (credential == null) {
            return this.error();
        } else {
            String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);
            Service service = WebUtils.getService(context);
            if (this.isRenewPresent(context) && ticketGrantingTicketId != null && service != null) {
                try {
                    String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, new Credential[]{credential});
                    WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);
                    return this.result("warn");
                } catch (AuthenticationException var7) {
                    this.onError(context, credential);
                    return this.error();
                } catch (TicketException var8) {
                    this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
                    this.logger.debug("Attempted to generate a ServiceTicket using renew=true with different credential", var8);
                }
            }

            try {
                WebUtils.putTicketGrantingTicketInRequestScope(context, this.centralAuthenticationService.createTicketGrantingTicket(new Credential[]{credential}));
                this.onSuccess(context, credential);
                return this.success();
            } catch (Exception var6) {
                this.onError(context, credential);
                return this.error();
            }
        }
    }

    public final void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    protected void onError(RequestContext context, Credential credential) {
    }

    protected void onSuccess(RequestContext context, Credential credential) {
    }

    protected abstract Credential constructCredentialsFromRequest(RequestContext var1);
}

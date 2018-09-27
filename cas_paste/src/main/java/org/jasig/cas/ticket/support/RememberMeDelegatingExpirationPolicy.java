package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 5:06 PM.
 */
public final class RememberMeDelegatingExpirationPolicy implements ExpirationPolicy, Serializable {
    private static final long serialVersionUID = -2735975347698196127L;
    @NotNull
    private ExpirationPolicy rememberMeExpirationPolicy;
    @NotNull
    private ExpirationPolicy sessionExpirationPolicy;

    public RememberMeDelegatingExpirationPolicy() {
    }

    public boolean isExpired(TicketState ticketState) {
        Boolean b = (Boolean)ticketState.getAuthentication().getAttributes().get("org.jasig.cas.authentication.principal.REMEMBER_ME");
        return b != null && !b.equals(Boolean.FALSE) ? this.rememberMeExpirationPolicy.isExpired(ticketState) : this.sessionExpirationPolicy.isExpired(ticketState);
    }

    public void setRememberMeExpirationPolicy(ExpirationPolicy rememberMeExpirationPolicy) {
        this.rememberMeExpirationPolicy = rememberMeExpirationPolicy;
    }

    public void setSessionExpirationPolicy(ExpirationPolicy sessionExpirationPolicy) {
        this.sessionExpirationPolicy = sessionExpirationPolicy;
    }
}

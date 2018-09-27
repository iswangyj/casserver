package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;

/**
 * @author SxL
 * Created on 9/25/2018 5:06 PM.
 */
public final class NeverExpiresExpirationPolicy implements ExpirationPolicy {
    private static final long serialVersionUID = 3833747698242303540L;

    public NeverExpiresExpirationPolicy() {
    }

    @Override
    public boolean isExpired(TicketState ticketState) {
        return false;
    }
}


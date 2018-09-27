package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 5:07 PM.
 */
public final class TimeoutExpirationPolicy implements ExpirationPolicy, Serializable {
    private static final long serialVersionUID = -7636642464326939536L;
    private final long timeToKillInMilliSeconds;

    private TimeoutExpirationPolicy() {
        this.timeToKillInMilliSeconds = 0L;
    }

    public TimeoutExpirationPolicy(long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    public boolean isExpired(TicketState ticketState) {
        return ticketState == null || System.currentTimeMillis() - ticketState.getLastTimeUsed() >= this.timeToKillInMilliSeconds;
    }
}

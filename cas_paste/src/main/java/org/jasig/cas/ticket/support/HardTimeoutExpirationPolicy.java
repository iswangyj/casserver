package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 5:05 PM.
 */
public final class HardTimeoutExpirationPolicy implements ExpirationPolicy, Serializable {
    private static final long serialVersionUID = 6728077010285422290L;
    private final long timeToKillInMilliSeconds;

    private HardTimeoutExpirationPolicy() {
        this.timeToKillInMilliSeconds = 0L;
    }

    public HardTimeoutExpirationPolicy(long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    @Override
    public boolean isExpired(TicketState ticketState) {
        return ticketState == null || System.currentTimeMillis() - ticketState.getCreationTime() >= this.timeToKillInMilliSeconds;
    }
}


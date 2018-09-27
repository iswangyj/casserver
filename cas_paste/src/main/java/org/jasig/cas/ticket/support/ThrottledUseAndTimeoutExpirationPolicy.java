package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 5:06 PM.
 */
public final class ThrottledUseAndTimeoutExpirationPolicy implements ExpirationPolicy, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThrottledUseAndTimeoutExpirationPolicy.class);
    private static final long serialVersionUID = 205979491183779408L;
    private long timeToKillInMilliSeconds;
    private long timeInBetweenUsesInMilliSeconds;

    public ThrottledUseAndTimeoutExpirationPolicy() {
    }

    public void setTimeInBetweenUsesInMilliSeconds(long timeInBetweenUsesInMilliSeconds) {
        this.timeInBetweenUsesInMilliSeconds = timeInBetweenUsesInMilliSeconds;
    }

    public void setTimeToKillInMilliSeconds(long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    public boolean isExpired(TicketState ticketState) {
        if (ticketState.getCountOfUses() == 0 && System.currentTimeMillis() - ticketState.getLastTimeUsed() < this.timeToKillInMilliSeconds) {
            LOGGER.debug("Ticket is not expired due to a count of zero and the time being less than the timeToKillInMilliseconds");
            return false;
        } else if (System.currentTimeMillis() - ticketState.getLastTimeUsed() >= this.timeToKillInMilliSeconds) {
            LOGGER.debug("Ticket is expired due to the time being greater than the timeToKillInMilliseconds");
            return true;
        } else if (System.currentTimeMillis() - ticketState.getLastTimeUsed() <= this.timeInBetweenUsesInMilliSeconds) {
            LOGGER.warn("Ticket is expired due to the time being less than the waiting period.");
            return true;
        } else {
            return false;
        }
    }
}

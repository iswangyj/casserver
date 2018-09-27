package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author SxL
 * Created on 9/25/2018 5:06 PM.
 */
public final class MultiTimeUseOrTimeoutExpirationPolicy implements ExpirationPolicy, Serializable {
    private static final long serialVersionUID = -5704993954986738308L;
    private final long timeToKillInMilliSeconds;
    private final int numberOfUses;

    private MultiTimeUseOrTimeoutExpirationPolicy() {
        this.timeToKillInMilliSeconds = 0L;
        this.numberOfUses = 0;
    }

    public MultiTimeUseOrTimeoutExpirationPolicy(int numberOfUses, long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
        this.numberOfUses = numberOfUses;
        Assert.isTrue(this.numberOfUses > 0, "numberOfUsers must be greater than 0.");
        Assert.isTrue(this.timeToKillInMilliSeconds > 0L, "timeToKillInMilliseconds must be greater than 0.");
    }

    public MultiTimeUseOrTimeoutExpirationPolicy(int numberOfUses, long timeToKill, TimeUnit timeUnit) {
        this(numberOfUses, timeUnit.toMillis(timeToKill));
    }

    public boolean isExpired(TicketState ticketState) {
        return ticketState == null || ticketState.getCountOfUses() >= this.numberOfUses || System.currentTimeMillis() - ticketState.getLastTimeUsed() >= this.timeToKillInMilliSeconds;
    }
}

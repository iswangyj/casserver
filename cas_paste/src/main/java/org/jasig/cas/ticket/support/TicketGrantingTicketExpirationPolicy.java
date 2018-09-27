package org.jasig.cas.ticket.support;

import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.TicketState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author SxL
 * Created on 9/25/2018 5:07 PM.
 */
public final class TicketGrantingTicketExpirationPolicy implements ExpirationPolicy, InitializingBean, Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketGrantingTicketExpirationPolicy.class);
    private static final long serialVersionUID = 7670537200691354820L;
    private long maxTimeToLiveInMilliSeconds;
    private long timeToKillInMilliSeconds;

    public TicketGrantingTicketExpirationPolicy() {
        this.maxTimeToLiveInMilliSeconds = 0L;
        this.timeToKillInMilliSeconds = 0L;
    }

    public TicketGrantingTicketExpirationPolicy(long maxTimeToLive, long timeToKill, TimeUnit timeUnit) {
        this.maxTimeToLiveInMilliSeconds = timeUnit.toMillis(maxTimeToLive);
        this.timeToKillInMilliSeconds = timeUnit.toMillis(timeToKill);
    }

    /** @deprecated */
    @Deprecated
    public void setMaxTimeToLiveInMilliSeconds(long maxTimeToLiveInMilliSeconds) {
        this.maxTimeToLiveInMilliSeconds = maxTimeToLiveInMilliSeconds;
    }

    public void setTimeToKillInMilliSeconds(long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    public void setMaxTimeToLiveInSeconds(long maxTimeToLiveInSeconds) {
        if (this.maxTimeToLiveInMilliSeconds == 0L) {
            this.maxTimeToLiveInMilliSeconds = TimeUnit.SECONDS.toMillis(maxTimeToLiveInSeconds);
        }

    }

    public void setTimeToKillInSeconds(long timeToKillInSeconds) {
        if (this.timeToKillInMilliSeconds == 0L) {
            this.timeToKillInMilliSeconds = TimeUnit.SECONDS.toMillis(timeToKillInSeconds);
        }

    }

    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(this.maxTimeToLiveInMilliSeconds >= this.timeToKillInMilliSeconds, "maxTimeToLiveInMilliSeconds must be greater than or equal to timeToKillInMilliSeconds.");
    }

    public boolean isExpired(TicketState ticketState) {
        if (System.currentTimeMillis() - ticketState.getCreationTime() >= this.maxTimeToLiveInMilliSeconds) {
            LOGGER.debug("Ticket is expired because the time since creation is greater than maxTimeToLiveInMilliSeconds");
            return true;
        } else if (System.currentTimeMillis() - ticketState.getLastTimeUsed() >= this.timeToKillInMilliSeconds) {
            LOGGER.debug("Ticket is expired because the time since last use is greater than timeToKillInMilliseconds");
            return true;
        } else {
            return false;
        }
    }
}

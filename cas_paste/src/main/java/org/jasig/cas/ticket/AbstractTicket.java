package org.jasig.cas.ticket;

import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * @author SxL
 * Created on 9/25/2018 3:31 PM.
 */
@MappedSuperclass
public abstract class AbstractTicket implements Ticket, TicketState {
    private static final long serialVersionUID = -8506442397878267555L;
    @Lob
    @Column(
            name = "EXPIRATION_POLICY",
            nullable = false
    )
    private ExpirationPolicy expirationPolicy;
    @Id
    @Column(
            name = "ID",
            nullable = false
    )
    private String id;
    @ManyToOne(
            targetEntity = TicketGrantingTicketImpl.class
    )
    private TicketGrantingTicket ticketGrantingTicket;
    @Column(
            name = "LAST_TIME_USED"
    )
    private long lastTimeUsed;
    @Column(
            name = "PREVIOUS_LAST_TIME_USED"
    )
    private long previousLastTimeUsed;
    @Column(
            name = "CREATION_TIME"
    )
    private long creationTime;
    @Column(
            name = "NUMBER_OF_TIMES_USED"
    )
    private int countOfUses;

    protected AbstractTicket() {
    }

    public AbstractTicket(String id, TicketGrantingTicket ticket, ExpirationPolicy expirationPolicy) {
        Assert.notNull(expirationPolicy, "expirationPolicy cannot be null");
        Assert.notNull(id, "id cannot be null");
        this.id = id;
        this.creationTime = System.currentTimeMillis();
        this.lastTimeUsed = System.currentTimeMillis();
        this.expirationPolicy = expirationPolicy;
        this.ticketGrantingTicket = ticket;
    }

    @Override
    public final String getId() {
        return this.id;
    }

    protected final void updateState() {
        this.previousLastTimeUsed = this.lastTimeUsed;
        this.lastTimeUsed = System.currentTimeMillis();
        ++this.countOfUses;
    }

    @Override
    public final int getCountOfUses() {
        return this.countOfUses;
    }

    @Override
    public final long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public final TicketGrantingTicket getGrantingTicket() {
        return this.ticketGrantingTicket;
    }

    @Override
    public final long getLastTimeUsed() {
        return this.lastTimeUsed;
    }

    @Override
    public final long getPreviousTimeUsed() {
        return this.previousLastTimeUsed;
    }

    @Override
    public final boolean isExpired() {
        return this.expirationPolicy.isExpired(this) || this.getGrantingTicket() != null && this.getGrantingTicket().isExpired() || this.isExpiredInternal();
    }

    protected boolean isExpiredInternal() {
        return false;
    }

    @Override
    public final int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public final String toString() {
        return this.getId();
    }
}


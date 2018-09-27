package org.jasig.cas.ticket.registry;

import org.jasig.cas.monitor.TicketRegistryState;
import org.jasig.cas.ticket.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author SxL
 * Created on 9/25/2018 4:43 PM.
 */
public abstract class AbstractTicketRegistry implements TicketRegistry, TicketRegistryState {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbstractTicketRegistry() {
    }

    @Override
    public final <T extends Ticket> T getTicket(String ticketId, Class<? extends Ticket> clazz) {
        Assert.notNull(clazz, "clazz cannot be null");
        Ticket ticket = this.getTicket(ticketId);
        if (ticket == null) {
            return null;
        } else if (!clazz.isAssignableFrom(ticket.getClass())) {
            throw new ClassCastException("Ticket [" + ticket.getId() + " is of type " + ticket.getClass() + " when we were expecting " + clazz);
        } else {
            return (T) ticket;
        }
    }

    @Override
    public int sessionCount() {
        this.logger.debug("sessionCount() operation is not implemented by the ticket registry instance {}. Returning unknown as {}", this.getClass().getName(), -2147483648);
        return -2147483648;
    }

    @Override
    public int serviceTicketCount() {
        this.logger.debug("serviceTicketCount() operation is not implemented by the ticket registry instance {}. Returning unknown as {}", this.getClass().getName(), -2147483648);
        return -2147483648;
    }
}

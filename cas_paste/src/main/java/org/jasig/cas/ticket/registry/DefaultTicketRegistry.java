package org.jasig.cas.ticket.registry;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SxL
 * Created on 9/25/2018 4:44 PM.
 */
public final class DefaultTicketRegistry extends AbstractTicketRegistry {
    private final Map<String, Ticket> cache;

    public DefaultTicketRegistry() {
        this.cache = new ConcurrentHashMap();
    }

    public DefaultTicketRegistry(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.cache = new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
    }

    @Override
    public void addTicket(Ticket ticket) {
        Assert.notNull(ticket, "ticket cannot be null");
        this.logger.debug("Added ticket [{}] to registry.", ticket.getId());
        this.cache.put(ticket.getId(), ticket);
    }

    @Override
    public Ticket getTicket(String ticketId) {
        if (ticketId == null) {
            return null;
        } else {
            this.logger.debug("Attempting to retrieve ticket [{}]", ticketId);
            Ticket ticket = (Ticket)this.cache.get(ticketId);
            if (ticket != null) {
                this.logger.debug("Ticket [{}] found in registry.", ticketId);
            }

            return ticket;
        }
    }

    @Override
    public boolean deleteTicket(String ticketId) {
        if (ticketId == null) {
            return false;
        } else {
            this.logger.debug("Removing ticket [{}] from registry", ticketId);
            return this.cache.remove(ticketId) != null;
        }
    }

    @Override
    public Collection<Ticket> getTickets() {
        return Collections.unmodifiableCollection(this.cache.values());
    }

    @Override
    public int sessionCount() {
        int count = 0;
        Iterator var2 = this.cache.values().iterator();

        while(var2.hasNext()) {
            Ticket t = (Ticket)var2.next();
            if (t instanceof TicketGrantingTicket) {
                ++count;
            }
        }

        return count;
    }

    @Override
    public int serviceTicketCount() {
        int count = 0;
        Iterator var2 = this.cache.values().iterator();

        while(var2.hasNext()) {
            Ticket t = (Ticket)var2.next();
            if (t instanceof ServiceTicket) {
                ++count;
            }
        }

        return count;
    }
}


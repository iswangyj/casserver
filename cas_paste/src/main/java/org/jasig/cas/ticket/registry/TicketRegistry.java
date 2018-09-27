package org.jasig.cas.ticket.registry;

import org.jasig.cas.ticket.Ticket;

import java.util.Collection;

/**
 * @author SxL
 * Created on 9/25/2018 4:54 PM.
 */
public interface TicketRegistry {
    void addTicket(Ticket var1);

    <T extends Ticket> T getTicket(String var1, Class<? extends Ticket> var2);

    Ticket getTicket(String var1);

    boolean deleteTicket(String var1);

    Collection<Ticket> getTickets();
}

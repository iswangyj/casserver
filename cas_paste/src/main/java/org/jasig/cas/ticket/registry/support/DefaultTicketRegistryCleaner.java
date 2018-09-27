package org.jasig.cas.ticket.registry.support;

import org.jasig.cas.logout.LogoutManager;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.RegistryCleaner;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 4:54 PM.
 */
public final class DefaultTicketRegistryCleaner implements RegistryCleaner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private TicketRegistry ticketRegistry;
    @NotNull
    private LockingStrategy lock = new NoOpLockingStrategy();
    @NotNull
    private LogoutManager logoutManager;
    private boolean logUserOutOfServices = true;

    public DefaultTicketRegistryCleaner() {
    }

    @Override
    public void clean() {
        this.logger.info("Beginning ticket cleanup.");
        this.logger.debug("Attempting to acquire ticket cleanup lock.");
        if (!this.lock.acquire()) {
            this.logger.info("Could not obtain lock.  Aborting cleanup.");
        } else {
            this.logger.debug("Acquired lock.  Proceeding with cleanup.");

            try {
                List<Ticket> ticketsToRemove = new ArrayList();
                Collection<Ticket> ticketsInCache = this.ticketRegistry.getTickets();
                Iterator var3 = ticketsInCache.iterator();

                label77:
                while(true) {
                    Ticket ticket;
                    if (!var3.hasNext()) {
                        this.logger.info("{} tickets found to be removed.", ticketsToRemove.size());
                        var3 = ticketsToRemove.iterator();

                        while(true) {
                            if (!var3.hasNext()) {
                                break label77;
                            }

                            ticket = (Ticket)var3.next();
                            if (this.logUserOutOfServices && ticket instanceof TicketGrantingTicket) {
                                this.logoutManager.performLogout((TicketGrantingTicket)ticket);
                            }

                            this.ticketRegistry.deleteTicket(ticket.getId());
                        }
                    }

                    ticket = (Ticket)var3.next();
                    if (ticket.isExpired()) {
                        ticketsToRemove.add(ticket);
                    }
                }
            } finally {
                this.logger.debug("Releasing ticket cleanup lock.");
                this.lock.release();
            }

            this.logger.info("Finished ticket cleanup.");
        }
    }

    public void setTicketRegistry(TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }

    public void setLock(LockingStrategy strategy) {
        this.lock = strategy;
    }

    public void setLogUserOutOfServices(boolean logUserOutOfServices) {
        this.logUserOutOfServices = logUserOutOfServices;
    }

    public void setLogoutManager(LogoutManager logoutManager) {
        this.logoutManager = logoutManager;
    }
}


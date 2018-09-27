package org.jasig.cas.ticket.registry;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedClientIF;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.springframework.beans.factory.DisposableBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author SxL
 * Created on 9/25/2018 4:52 PM.
 */
public final class MemCacheTicketRegistry extends AbstractDistributedTicketRegistry implements DisposableBean {
    @NotNull
    private final MemcachedClientIF client;
    @Min(0L)
    private final int tgtTimeout;
    @Min(0L)
    private final int stTimeout;

    public MemCacheTicketRegistry(String[] hostnames, int ticketGrantingTicketTimeOut, int serviceTicketTimeOut) {
        try {
            this.client = new MemcachedClient(AddrUtil.getAddresses(Arrays.asList(hostnames)));
        } catch (IOException var5) {
            throw new IllegalArgumentException("Invalid memcached host specification.", var5);
        }

        this.tgtTimeout = ticketGrantingTicketTimeOut;
        this.stTimeout = serviceTicketTimeOut;
    }

    /** @deprecated */
    @Deprecated
    public MemCacheTicketRegistry(long ticketGrantingTicketTimeOut, long serviceTicketTimeOut, String[] hostnames) {
        this(hostnames, (int)(ticketGrantingTicketTimeOut / 1000L), (int)(serviceTicketTimeOut / 1000L));
    }

    public MemCacheTicketRegistry(MemcachedClientIF client, int ticketGrantingTicketTimeOut, int serviceTicketTimeOut) {
        this.tgtTimeout = ticketGrantingTicketTimeOut;
        this.stTimeout = serviceTicketTimeOut;
        this.client = client;
    }

    @Override
    protected void updateTicket(Ticket ticket) {
        this.logger.debug("Updating ticket {}", ticket);

        try {
            if (!(Boolean)this.client.replace(ticket.getId(), this.getTimeout(ticket), ticket).get()) {
                this.logger.error("Failed updating {}", ticket);
            }
        } catch (InterruptedException var3) {
            this.logger.warn("Interrupted while waiting for response to async replace operation for ticket {}. Cannot determine whether update was successful.", ticket);
        } catch (Exception var4) {
            this.logger.error("Failed updating {}", ticket, var4);
        }

    }

    @Override
    public void addTicket(Ticket ticket) {
        this.logger.debug("Adding ticket {}", ticket);

        try {
            if (!(Boolean)this.client.add(ticket.getId(), this.getTimeout(ticket), ticket).get()) {
                this.logger.error("Failed adding {}", ticket);
            }
        } catch (InterruptedException var3) {
            this.logger.warn("Interrupted while waiting for response to async add operation for ticket {}.Cannot determine whether add was successful.", ticket);
        } catch (Exception var4) {
            this.logger.error("Failed adding {}", ticket, var4);
        }

    }

    @Override
    public boolean deleteTicket(String ticketId) {
        this.logger.debug("Deleting ticket {}", ticketId);

        try {
            return (Boolean)this.client.delete(ticketId).get();
        } catch (Exception var3) {
            this.logger.error("Failed deleting {}", ticketId, var3);
            return false;
        }
    }

    @Override
    public Ticket getTicket(String ticketId) {
        try {
            Ticket t = (Ticket)this.client.get(ticketId);
            if (t != null) {
                return this.getProxiedTicketInstance(t);
            }
        } catch (Exception var3) {
            this.logger.error("Failed fetching {} ", ticketId, var3);
        }

        return null;
    }

    @Override
    public Collection<Ticket> getTickets() {
        throw new UnsupportedOperationException("GetTickets not supported.");
    }

    public void destroy() throws Exception {
        this.client.shutdown();
    }

    /** @deprecated */
    @Deprecated
    public void setSynchronizeUpdatesToRegistry(boolean sync) {
    }

    @Override
    protected boolean needsCallback() {
        return true;
    }

    private int getTimeout(Ticket t) {
        if (t instanceof TicketGrantingTicket) {
            return this.tgtTimeout;
        } else if (t instanceof ServiceTicket) {
            return this.stTimeout;
        } else {
            throw new IllegalArgumentException("Invalid ticket type");
        }
    }
}

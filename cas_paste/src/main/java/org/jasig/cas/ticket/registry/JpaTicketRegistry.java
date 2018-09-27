package org.jasig.cas.ticket.registry;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.ServiceTicketImpl;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 4:50 PM.
 */
public final class JpaTicketRegistry extends AbstractDistributedTicketRegistry {
    @NotNull
    @PersistenceContext
    private EntityManager entityManager;
    @NotNull
    private String ticketGrantingTicketPrefix = "TGT";

    public JpaTicketRegistry() {
    }

    @Override
    protected void updateTicket(Ticket ticket) {
        this.entityManager.merge(ticket);
        this.logger.debug("Updated ticket [{}].", ticket);
    }

    @Transactional(
            readOnly = false
    )
    @Override
    public void addTicket(Ticket ticket) {
        this.entityManager.persist(ticket);
        this.logger.debug("Added ticket [{}] to registry.", ticket);
    }

    @Transactional(
            readOnly = false
    )
    @Override
    public boolean deleteTicket(String ticketId) {
        Ticket ticket = this.getRawTicket(ticketId);
        if (ticket == null) {
            return false;
        } else if (ticket instanceof ServiceTicket) {
            this.removeTicket(ticket);
            this.logger.debug("Deleted ticket [{}] from the registry.", ticket);
            return true;
        } else {
            this.deleteTicketAndChildren(ticket);
            this.logger.debug("Deleted ticket [{}] and its children from the registry.", ticket);
            return true;
        }
    }

    private void deleteTicketAndChildren(Ticket ticket) {
        List<TicketGrantingTicketImpl> ticketGrantingTicketImpls = this.entityManager.createQuery("select t from TicketGrantingTicketImpl t where t.ticketGrantingTicket.id = :id", TicketGrantingTicketImpl.class).setLockMode(LockModeType.PESSIMISTIC_WRITE).setParameter("id", ticket.getId()).getResultList();
        List<ServiceTicketImpl> serviceTicketImpls = this.entityManager.createQuery("select s from ServiceTicketImpl s where s.ticketGrantingTicket.id = :id", ServiceTicketImpl.class).setParameter("id", ticket.getId()).getResultList();
        Iterator var4 = serviceTicketImpls.iterator();

        while(var4.hasNext()) {
            ServiceTicketImpl s = (ServiceTicketImpl)var4.next();
            this.removeTicket(s);
        }

        var4 = ticketGrantingTicketImpls.iterator();

        while(var4.hasNext()) {
            TicketGrantingTicketImpl t = (TicketGrantingTicketImpl)var4.next();
            this.deleteTicketAndChildren(t);
        }

        this.removeTicket(ticket);
    }

    private void removeTicket(Ticket ticket) {
        try {
            if (this.logger.isDebugEnabled()) {
                Date creationDate = new Date(ticket.getCreationTime());
                this.logger.debug("Removing Ticket [{}] created: {}", ticket, creationDate.toString());
            }

            this.entityManager.remove(ticket);
        } catch (Exception var3) {
            this.logger.error("Error removing {} from registry.", ticket, var3);
        }

    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Ticket getTicket(String ticketId) {
        return this.getProxiedTicketInstance(this.getRawTicket(ticketId));
    }

    private Ticket getRawTicket(String ticketId) {
        try {
            return ticketId.startsWith(this.ticketGrantingTicketPrefix) ? (Ticket)this.entityManager.find(TicketGrantingTicketImpl.class, ticketId, LockModeType.PESSIMISTIC_WRITE) : (Ticket)this.entityManager.find(ServiceTicketImpl.class, ticketId);
        } catch (Exception var3) {
            this.logger.error("Error getting ticket {} from registry.", ticketId, var3);
            return null;
        }
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public Collection<Ticket> getTickets() {
        List<TicketGrantingTicketImpl> tgts = this.entityManager.createQuery("select t from TicketGrantingTicketImpl t", TicketGrantingTicketImpl.class).getResultList();
        List<ServiceTicketImpl> sts = this.entityManager.createQuery("select s from ServiceTicketImpl s", ServiceTicketImpl.class).getResultList();
        List<Ticket> tickets = new ArrayList();
        tickets.addAll(tgts);
        tickets.addAll(sts);
        return tickets;
    }

    public void setTicketGrantingTicketPrefix(String ticketGrantingTicketPrefix) {
        this.ticketGrantingTicketPrefix = ticketGrantingTicketPrefix;
    }

    @Override
    protected boolean needsCallback() {
        return false;
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public int sessionCount() {
        return this.countToInt(this.entityManager.createQuery("select count(t) from TicketGrantingTicketImpl t").getSingleResult());
    }

    @Transactional(
            readOnly = true
    )
    @Override
    public int serviceTicketCount() {
        return this.countToInt(this.entityManager.createQuery("select count(t) from ServiceTicketImpl t").getSingleResult());
    }

    private int countToInt(Object result) {
        int intval;
        if (result instanceof Long) {
            intval = ((Long)result).intValue();
        } else if (result instanceof Integer) {
            intval = (Integer)result;
        } else {
            intval = ((Number)result).intValue();
        }

        return intval;
    }
}

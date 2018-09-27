package org.jasig.cas.ticket.registry;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ExpirationPolicy;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;

import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 3:42 PM.
 */
public abstract class AbstractDistributedTicketRegistry extends AbstractTicketRegistry {
    public AbstractDistributedTicketRegistry() {
    }

    protected abstract void updateTicket(Ticket var1);

    protected abstract boolean needsCallback();

    protected final Ticket getProxiedTicketInstance(Ticket ticket) {
        if (ticket == null) {
            return null;
        } else {
            return (Ticket)(ticket instanceof TicketGrantingTicket ? new AbstractDistributedTicketRegistry.TicketGrantingTicketDelegator(this, (TicketGrantingTicket)ticket, this.needsCallback()) : new AbstractDistributedTicketRegistry.ServiceTicketDelegator(this, (ServiceTicket)ticket, this.needsCallback()));
        }
    }

    private static final class TicketGrantingTicketDelegator extends AbstractDistributedTicketRegistry.TicketDelegator<TicketGrantingTicket> implements TicketGrantingTicket {
        private static final long serialVersionUID = 5312560061970601497L;

        protected TicketGrantingTicketDelegator(AbstractDistributedTicketRegistry ticketRegistry, TicketGrantingTicket ticketGrantingTicket, boolean callback) {
            super(ticketRegistry, ticketGrantingTicket, callback);
        }

        @Override
        public Authentication getAuthentication() {
            return ((TicketGrantingTicket)this.getTicket()).getAuthentication();
        }

        @Override
        public List<Authentication> getSupplementalAuthentications() {
            return ((TicketGrantingTicket)this.getTicket()).getSupplementalAuthentications();
        }

        @Override
        public ServiceTicket grantServiceTicket(String id, Service service, ExpirationPolicy expirationPolicy, boolean credentialsProvided) {
            ServiceTicket t = ((TicketGrantingTicket)this.getTicket()).grantServiceTicket(id, service, expirationPolicy, credentialsProvided);
            this.updateTicket();
            return t;
        }

        @Override
        public void markTicketExpired() {
            ((TicketGrantingTicket)this.getTicket()).markTicketExpired();
            this.updateTicket();
        }

        @Override
        public boolean isRoot() {
            return ((TicketGrantingTicket)this.getTicket()).isRoot();
        }

        @Override
        public TicketGrantingTicket getRoot() {
            return ((TicketGrantingTicket)this.getTicket()).getRoot();
        }

        @Override
        public List<Authentication> getChainedAuthentications() {
            return ((TicketGrantingTicket)this.getTicket()).getChainedAuthentications();
        }

        @Override
        public Map<String, Service> getServices() {
            return ((TicketGrantingTicket)this.getTicket()).getServices();
        }

        @Override
        public void removeAllServices() {
            ((TicketGrantingTicket)this.getTicket()).removeAllServices();
        }
    }

    private static final class ServiceTicketDelegator extends AbstractDistributedTicketRegistry.TicketDelegator<ServiceTicket> implements ServiceTicket {
        private static final long serialVersionUID = 8160636219307822967L;

        protected ServiceTicketDelegator(AbstractDistributedTicketRegistry ticketRegistry, ServiceTicket serviceTicket, boolean callback) {
            super(ticketRegistry, serviceTicket, callback);
        }

        @Override
        public Service getService() {
            return ((ServiceTicket)this.getTicket()).getService();
        }

        @Override
        public boolean isFromNewLogin() {
            return ((ServiceTicket)this.getTicket()).isFromNewLogin();
        }

        @Override
        public boolean isValidFor(Service service) {
            boolean b = ((ServiceTicket)this.getTicket()).isValidFor(service);
            this.updateTicket();
            return b;
        }

        @Override
        public TicketGrantingTicket grantTicketGrantingTicket(String id, Authentication authentication, ExpirationPolicy expirationPolicy) {
            TicketGrantingTicket t = ((ServiceTicket)this.getTicket()).grantTicketGrantingTicket(id, authentication, expirationPolicy);
            this.updateTicket();
            return t;
        }
    }

    private static class TicketDelegator<T extends Ticket> implements Ticket {
        private static final long serialVersionUID = 1780193477774123440L;
        private final AbstractDistributedTicketRegistry ticketRegistry;
        private final T ticket;
        private final boolean callback;

        protected TicketDelegator(AbstractDistributedTicketRegistry ticketRegistry, T ticket, boolean callback) {
            this.ticketRegistry = ticketRegistry;
            this.ticket = ticket;
            this.callback = callback;
        }

        protected void updateTicket() {
            this.ticketRegistry.updateTicket(this.ticket);
        }

        protected T getTicket() {
            return this.ticket;
        }

        @Override
        public final String getId() {
            return this.ticket.getId();
        }

        @Override
        public final boolean isExpired() {
            if (!this.callback) {
                return this.ticket.isExpired();
            } else {
                TicketGrantingTicket t = this.getGrantingTicket();
                return this.ticket.isExpired() || t != null && t.isExpired();
            }
        }

        @Override
        public final TicketGrantingTicket getGrantingTicket() {
            TicketGrantingTicket old = this.ticket.getGrantingTicket();
            return old != null && this.callback ? (TicketGrantingTicket)this.ticketRegistry.getTicket(old.getId(), Ticket.class) : old;
        }

        @Override
        public final long getCreationTime() {
            return this.ticket.getCreationTime();
        }

        @Override
        public final int getCountOfUses() {
            return this.ticket.getCountOfUses();
        }

        @Override
        public int hashCode() {
            return this.ticket.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return this.ticket.equals(o);
        }
    }
}


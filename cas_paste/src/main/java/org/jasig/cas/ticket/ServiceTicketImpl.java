package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author SxL
 * Created on 9/25/2018 3:33 PM.
 */
@Entity
@Table(
        name = "SERVICETICKET"
)
public final class ServiceTicketImpl extends AbstractTicket implements ServiceTicket {
    private static final long serialVersionUID = -4223319704861765405L;
    @Lob
    @Column(
            name = "SERVICE",
            nullable = false
    )
    private Service service;
    @Column(
            name = "FROM_NEW_LOGIN",
            nullable = false
    )
    private boolean fromNewLogin;
    @Column(
            name = "TICKET_ALREADY_GRANTED",
            nullable = false
    )
    private Boolean grantedTicketAlready = false;

    public ServiceTicketImpl() {
    }

    protected ServiceTicketImpl(String id, TicketGrantingTicketImpl ticket, Service service, boolean fromNewLogin, ExpirationPolicy policy) {
        super(id, ticket, policy);
        Assert.notNull(ticket, "ticket cannot be null");
        Assert.notNull(service, "service cannot be null");
        this.service = service;
        this.fromNewLogin = fromNewLogin;
    }

    @Override
    public boolean isFromNewLogin() {
        return this.fromNewLogin;
    }

    @Override
    public Service getService() {
        return this.service;
    }

    @Override
    public boolean isValidFor(Service serviceToValidate) {
        this.updateState();
        return serviceToValidate.matches(this.service);
    }

    @Override
    public TicketGrantingTicket grantTicketGrantingTicket(String id, Authentication authentication, ExpirationPolicy expirationPolicy) {
        synchronized(this) {
            if (this.grantedTicketAlready) {
                throw new IllegalStateException("TicketGrantingTicket already generated for this ServiceTicket.  Cannot grant more than one TGT for ServiceTicket");
            }

            this.grantedTicketAlready = true;
        }

        return new TicketGrantingTicketImpl(id, (TicketGrantingTicketImpl)this.getGrantingTicket(), authentication, expirationPolicy);
    }

    @Override
    public Authentication getAuthentication() {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof ServiceTicket) {
            Ticket serviceTicket = (Ticket)object;
            return serviceTicket.getId().equals(this.getId());
        } else {
            return false;
        }
    }
}


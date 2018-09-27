package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 3:36 PM.
 */
@Entity
@Table(
        name = "TICKETGRANTINGTICKET"
)
public final class TicketGrantingTicketImpl extends AbstractTicket implements TicketGrantingTicket {
    private static final long serialVersionUID = -8608149809180911599L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketGrantingTicketImpl.class);
    @Lob
    @Column(
            name = "AUTHENTICATION",
            nullable = false
    )
    private Authentication authentication;
    @Column(
            name = "EXPIRED",
            nullable = false
    )
    private Boolean expired;
    @Lob
    @Column(
            name = "SERVICES_GRANTED_ACCESS_TO",
            nullable = false
    )
    private final HashMap<String, Service> services;
    @Lob
    @Column(
            name = "SUPPLEMENTAL_AUTHENTICATIONS",
            nullable = false
    )
    private final ArrayList<Authentication> supplementalAuthentications;

    public TicketGrantingTicketImpl() {
        this.expired = false;
        this.services = new HashMap();
        this.supplementalAuthentications = new ArrayList();
    }

    public TicketGrantingTicketImpl(String id, TicketGrantingTicket ticketGrantingTicket, Authentication authentication, ExpirationPolicy policy) {
        super(id, ticketGrantingTicket, policy);
        this.expired = false;
        this.services = new HashMap();
        this.supplementalAuthentications = new ArrayList();
        Assert.notNull(authentication, "authentication cannot be null");
        this.authentication = authentication;
    }

    public TicketGrantingTicketImpl(String id, Authentication authentication, ExpirationPolicy policy) {
        this(id, (TicketGrantingTicket)null, authentication, policy);
    }

    @Override
    public Authentication getAuthentication() {
        return this.authentication;
    }

    @Override
    public synchronized ServiceTicket grantServiceTicket(String id, Service service, ExpirationPolicy expirationPolicy, boolean credentialsProvided) {
        ServiceTicket serviceTicket = new ServiceTicketImpl(id, this, service, this.getCountOfUses() == 0 || credentialsProvided, expirationPolicy);
        this.updateState();
        List<Authentication> authentications = this.getChainedAuthentications();
        service.setPrincipal(((Authentication)authentications.get(authentications.size() - 1)).getPrincipal());
        this.services.put(id, service);
        return serviceTicket;
    }

    @Override
    public synchronized Map<String, Service> getServices() {
        Map<String, Service> map = new HashMap(this.services.size());
        Iterator var2 = this.services.keySet().iterator();

        while(var2.hasNext()) {
            String ticket = (String)var2.next();
            map.put(ticket, this.services.get(ticket));
        }

        return Collections.unmodifiableMap(map);
    }

    @Override
    public void removeAllServices() {
        this.services.clear();
    }

    @Override
    public boolean isRoot() {
        return this.getGrantingTicket() == null;
    }

    @Override
    public void markTicketExpired() {
        this.expired = true;
    }

    @Override
    public TicketGrantingTicket getRoot() {
        TicketGrantingTicket current = this;

        for(TicketGrantingTicket parent = this.getGrantingTicket(); parent != null; parent = parent.getGrantingTicket()) {
            current = parent;
        }

        return (TicketGrantingTicket)current;
    }

    @Override
    public boolean isExpiredInternal() {
        return this.expired;
    }

    @Override
    public List<Authentication> getSupplementalAuthentications() {
        return this.supplementalAuthentications;
    }

    @Override
    public List<Authentication> getChainedAuthentications() {
        List<Authentication> list = new ArrayList();
        list.add(this.getAuthentication());
        if (this.getGrantingTicket() == null) {
            return Collections.unmodifiableList(list);
        } else {
            list.addAll(this.getGrantingTicket().getChainedAuthentications());
            return Collections.unmodifiableList(list);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof TicketGrantingTicket) {
            Ticket ticket = (Ticket)object;
            return ticket.getId().equals(this.getId());
        } else {
            return false;
        }
    }
}


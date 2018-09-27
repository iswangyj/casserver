package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;

import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 3:35 PM.
 */
public interface TicketGrantingTicket extends Ticket {
    String PREFIX = "TGT";

    Authentication getAuthentication();

    List<Authentication> getSupplementalAuthentications();

    ServiceTicket grantServiceTicket(String var1, Service var2, ExpirationPolicy var3, boolean var4);

    Map<String, Service> getServices();

    void removeAllServices();

    void markTicketExpired();

    boolean isRoot();

    TicketGrantingTicket getRoot();

    List<Authentication> getChainedAuthentications();
}

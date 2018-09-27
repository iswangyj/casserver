package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Service;

/**
 * @author SxL
 * Created on 9/25/2018 3:33 PM.
 */
public interface ServiceTicket extends Ticket {
    String PREFIX = "ST";

    Service getService();

    boolean isFromNewLogin();

    boolean isValidFor(Service var1);

    TicketGrantingTicket grantTicketGrantingTicket(String var1, Authentication var2, ExpirationPolicy var3);
}

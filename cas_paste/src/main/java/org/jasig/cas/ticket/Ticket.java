package org.jasig.cas.ticket;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 3:34 PM.
 */
public interface Ticket extends Serializable {
    String getId();

    boolean isExpired();

    TicketGrantingTicket getGrantingTicket();

    long getCreationTime();

    int getCountOfUses();
}

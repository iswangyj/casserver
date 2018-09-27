package org.jasig.cas.ticket;

import java.io.Serializable;

/**
 * @author SxL
 * Created on 9/25/2018 3:32 PM.
 */
public interface ExpirationPolicy extends Serializable {
    boolean isExpired(TicketState var1);
}

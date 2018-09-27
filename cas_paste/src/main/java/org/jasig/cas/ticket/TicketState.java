package org.jasig.cas.ticket;

import org.jasig.cas.authentication.Authentication;

/**
 * @author SxL
 * Created on 9/25/2018 3:37 PM.
 */
public interface TicketState {
    int getCountOfUses();

    long getLastTimeUsed();

    long getPreviousTimeUsed();

    long getCreationTime();

    Authentication getAuthentication();
}

package org.jasig.cas.logout;

import org.jasig.cas.ticket.TicketGrantingTicket;

import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 2:52 PM.
 */

public interface LogoutManager {
    List<LogoutRequest> performLogout(TicketGrantingTicket var1);

    String createFrontChannelLogoutMessage(LogoutRequest var1);
}

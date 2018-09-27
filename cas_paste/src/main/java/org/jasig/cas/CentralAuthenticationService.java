package org.jasig.cas;

import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.logout.LogoutRequest;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.validation.Assertion;

import java.util.List;

/**
 * @author SxL
 * Created on 9/25/2018 1:41 PM.
 */
public interface CentralAuthenticationService {
    String createTicketGrantingTicket(Credential... var1) throws AuthenticationException, TicketException;

    String grantServiceTicket(String var1, Service var2) throws TicketException;

    String grantServiceTicket(String var1, Service var2, Credential... var3) throws AuthenticationException, TicketException;

    Assertion validateServiceTicket(String var1, Service var2) throws TicketException;

    List<LogoutRequest> destroyTicketGrantingTicket(String var1);

    String delegateTicketGrantingTicket(String var1, Credential... var2) throws AuthenticationException, TicketException;
}

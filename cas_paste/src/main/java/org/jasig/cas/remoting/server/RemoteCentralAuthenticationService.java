package org.jasig.cas.remoting.server;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.logout.LogoutRequest;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.validation.Assertion;
import org.springframework.util.Assert;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author SxL
 * Created on 9/25/2018 3:07 PM.
 */
public final class RemoteCentralAuthenticationService implements CentralAuthenticationService {
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;
    @NotNull
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public RemoteCentralAuthenticationService() {
    }

    @Override
    public String createTicketGrantingTicket(Credential... credentials) throws AuthenticationException, TicketException {
        Assert.notNull(credentials, "credentials cannot be null");
        this.checkForErrors(credentials);
        return this.centralAuthenticationService.createTicketGrantingTicket(credentials);
    }

    @Override
    public String grantServiceTicket(String ticketGrantingTicketId, Service service) throws TicketException {
        return this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service);
    }

    @Override
    public String grantServiceTicket(String ticketGrantingTicketId, Service service, Credential... credentials) throws AuthenticationException, TicketException {
        this.checkForErrors(credentials);
        return this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, credentials);
    }

    @Override
    public Assertion validateServiceTicket(String serviceTicketId, Service service) throws TicketException {
        return this.centralAuthenticationService.validateServiceTicket(serviceTicketId, service);
    }

    @Override
    public List<LogoutRequest> destroyTicketGrantingTicket(String ticketGrantingTicketId) {
        return this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
    }

    @Override
    public String delegateTicketGrantingTicket(String serviceTicketId, Credential... credentials) throws AuthenticationException, TicketException {
        this.checkForErrors(credentials);
        return this.centralAuthenticationService.delegateTicketGrantingTicket(serviceTicketId, credentials);
    }

    private void checkForErrors(Credential... credentials) {
        if (credentials != null) {
            Credential[] var2 = credentials;
            int var3 = credentials.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Credential c = var2[var4];
                Set<ConstraintViolation<Credential>> errors = this.validator.validate(c, new Class[0]);
                if (!errors.isEmpty()) {
                    throw new IllegalArgumentException("Error validating credentials: " + errors.toString());
                }
            }

        }
    }

    public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}

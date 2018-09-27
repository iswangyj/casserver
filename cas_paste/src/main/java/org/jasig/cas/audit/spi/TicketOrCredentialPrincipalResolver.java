package org.jasig.cas.audit.spi;

import com.github.inspektr.common.spi.PrincipalResolver;
import org.aspectj.lang.JoinPoint;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.util.AopUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 1:48 PM.
 */
public final class TicketOrCredentialPrincipalResolver implements PrincipalResolver {
    @NotNull
    private final TicketRegistry ticketRegistry;

    public TicketOrCredentialPrincipalResolver(TicketRegistry ticketRegistry) {
        this.ticketRegistry = ticketRegistry;
    }

    @Override
    public String resolveFrom(JoinPoint joinPoint, Object retVal) {
        return this.resolveFromInternal(AopUtils.unWrapJoinPoint(joinPoint));
    }

    @Override
    public String resolveFrom(JoinPoint joinPoint, Exception retVal) {
        return this.resolveFromInternal(AopUtils.unWrapJoinPoint(joinPoint));
    }

    @Override
    public String resolve() {
        return "audit:unknown";
    }

    protected String resolveFromInternal(JoinPoint joinPoint) {
        Object arg1 = joinPoint.getArgs()[0];
        if (arg1 instanceof Credential) {
            return arg1.toString();
        } else {
            if (arg1 instanceof String) {
                Ticket ticket = this.ticketRegistry.getTicket((String)arg1);
                if (ticket instanceof ServiceTicket) {
                    ServiceTicket serviceTicket = (ServiceTicket)ticket;
                    return serviceTicket.getGrantingTicket().getAuthentication().getPrincipal().getId();
                }

                if (ticket instanceof TicketGrantingTicket) {
                    TicketGrantingTicket tgt = (TicketGrantingTicket)ticket;
                    return tgt.getAuthentication().getPrincipal().getId();
                }
            } else {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                if (securityContext != null) {
                    Authentication authentication = securityContext.getAuthentication();
                    if (authentication != null) {
                        return ((UserDetails)authentication.getPrincipal()).getUsername();
                    }
                }
            }

            return "audit:unknown";
        }
    }
}


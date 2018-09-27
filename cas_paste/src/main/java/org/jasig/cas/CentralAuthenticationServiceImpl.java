package org.jasig.cas;

import com.github.inspektr.audit.annotation.Audit;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.authentication.*;
import org.jasig.cas.authentication.principal.*;
import org.jasig.cas.logout.LogoutManager;
import org.jasig.cas.logout.LogoutRequest;
import org.jasig.cas.services.*;
import org.jasig.cas.services.support.RegisteredServiceDefaultAttributeFilter;
import org.jasig.cas.ticket.*;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.jasig.cas.validation.Assertion;
import org.jasig.cas.validation.ImmutableAssertion;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 1:42 PM.
 */
public class CentralAuthenticationServiceImpl implements CentralAuthenticationService{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @NotNull
    private final TicketRegistry ticketRegistry;
    @NotNull
    private final TicketRegistry serviceTicketRegistry;
    @NotNull
    private final AuthenticationManager authenticationManager;
    @NotNull
    private final UniqueTicketIdGenerator ticketGrantingTicketUniqueTicketIdGenerator;
    @NotNull
    private final Map<String, UniqueTicketIdGenerator> uniqueTicketIdGeneratorsForService;
    @NotNull
    private final ServicesManager servicesManager;
    @NotNull
    private final LogoutManager logoutManager;
    @NotNull
    private ExpirationPolicy ticketGrantingTicketExpirationPolicy;
    @NotNull
    private ExpirationPolicy serviceTicketExpirationPolicy;
    @NotNull
    private PersistentIdGenerator persistentIdGenerator = new ShibbolethCompatiblePersistentIdGenerator();
    private RegisteredServiceAttributeFilter defaultAttributeFilter = new RegisteredServiceDefaultAttributeFilter();
    @NotNull
    private ContextualAuthenticationPolicyFactory<ServiceContext> serviceContextAuthenticationPolicyFactory = new AcceptAnyAuthenticationPolicyFactory();

    public CentralAuthenticationServiceImpl(TicketRegistry ticketRegistry, TicketRegistry serviceTicketRegistry, AuthenticationManager authenticationManager, UniqueTicketIdGenerator ticketGrantingTicketUniqueTicketIdGenerator, Map<String, UniqueTicketIdGenerator> uniqueTicketIdGeneratorsForService, ExpirationPolicy ticketGrantingTicketExpirationPolicy, ExpirationPolicy serviceTicketExpirationPolicy, ServicesManager servicesManager, LogoutManager logoutManager) {
        this.ticketRegistry = ticketRegistry;
        if (serviceTicketRegistry == null) {
            this.serviceTicketRegistry = ticketRegistry;
        } else {
            this.serviceTicketRegistry = serviceTicketRegistry;
        }

        this.authenticationManager = authenticationManager;
        this.ticketGrantingTicketUniqueTicketIdGenerator = ticketGrantingTicketUniqueTicketIdGenerator;
        this.uniqueTicketIdGeneratorsForService = uniqueTicketIdGeneratorsForService;
        this.ticketGrantingTicketExpirationPolicy = ticketGrantingTicketExpirationPolicy;
        this.serviceTicketExpirationPolicy = serviceTicketExpirationPolicy;
        this.servicesManager = servicesManager;
        this.logoutManager = logoutManager;
    }

    @Audit(
            action = "TICKET_GRANTING_TICKET_DESTROYED",
            actionResolverName = "DESTROY_TICKET_GRANTING_TICKET_RESOLVER",
            resourceResolverName = "DESTROY_TICKET_GRANTING_TICKET_RESOURCE_RESOLVER"
    )
    @Profiled(
            tag = "DESTROY_TICKET_GRANTING_TICKET",
            logFailuresSeparately = false
    )
    @Transactional(
            readOnly = false
    )
    @Override
    public List<LogoutRequest> destroyTicketGrantingTicket(String ticketGrantingTicketId) {
        Assert.notNull(ticketGrantingTicketId);
        this.logger.debug("Removing ticket [{}] from registry.", ticketGrantingTicketId);
        TicketGrantingTicket ticket = (TicketGrantingTicket)this.ticketRegistry.getTicket(ticketGrantingTicketId, TicketGrantingTicket.class);
        if (ticket == null) {
            this.logger.debug("TicketGrantingTicket [{}] cannot be found in the ticket registry.", ticketGrantingTicketId);
            return Collections.emptyList();
        } else {
            this.logger.debug("Ticket found. Processing logout requests and then deleting the ticket...");
            List<LogoutRequest> logoutRequests = this.logoutManager.performLogout(ticket);
            this.ticketRegistry.deleteTicket(ticketGrantingTicketId);
            return logoutRequests;
        }
    }

    @Audit(
            action = "SERVICE_TICKET",
            actionResolverName = "GRANT_SERVICE_TICKET_RESOLVER",
            resourceResolverName = "GRANT_SERVICE_TICKET_RESOURCE_RESOLVER"
    )
    @Profiled(
            tag = "GRANT_SERVICE_TICKET",
            logFailuresSeparately = false
    )
    @Transactional(
            readOnly = false
    )
    @Override
    public String grantServiceTicket(String ticketGrantingTicketId, Service service, Credential... credentials) throws AuthenticationException, TicketException {
        Assert.notNull(ticketGrantingTicketId, "ticketGrantingticketId cannot be null");
        Assert.notNull(service, "service cannot be null");
        TicketGrantingTicket ticketGrantingTicket = (TicketGrantingTicket)this.ticketRegistry.getTicket(ticketGrantingTicketId, TicketGrantingTicket.class);
        if (ticketGrantingTicket == null) {
            this.logger.debug("TicketGrantingTicket [{}] cannot be found in the ticket registry.", ticketGrantingTicketId);
            throw new InvalidTicketException(ticketGrantingTicketId);
        } else {
            synchronized(ticketGrantingTicket) {
                if (ticketGrantingTicket.isExpired()) {
                    this.ticketRegistry.deleteTicket(ticketGrantingTicketId);
                    this.logger.debug("TicketGrantingTicket[{}] has expired and is now deleted from the ticket registry.", ticketGrantingTicketId);
                    throw new InvalidTicketException(ticketGrantingTicketId);
                }
            }

            RegisteredService registeredService = this.servicesManager.findServiceBy(service);
            this.verifyRegisteredServiceProperties(registeredService, service);
            if (!registeredService.isSsoEnabled() && credentials == null && ticketGrantingTicket.getCountOfUses() > 0) {
                this.logger.warn("ServiceManagement: Service [{}] is not allowed to use SSO.", service.getId());
                throw new UnauthorizedSsoServiceException();
            } else {
                List<Authentication> authns = ticketGrantingTicket.getChainedAuthentications();
                String uniqueTicketIdGenKey;
                if (authns.size() > 1 && !registeredService.isAllowedToProxy()) {
                    uniqueTicketIdGenKey = String.format("ServiceManagement: Proxy attempt by service [%s] (registered service [%s]) is not allowed.", service.getId(), registeredService.toString());
                    this.logger.warn(uniqueTicketIdGenKey);
                    throw new UnauthorizedProxyingException(uniqueTicketIdGenKey);
                } else {
                    if (credentials != null) {
                        Authentication current = this.authenticationManager.authenticate(credentials);
                        Authentication original = ticketGrantingTicket.getAuthentication();
                        if (!current.getPrincipal().equals(original.getPrincipal())) {
                            throw new MixedPrincipalException(current, current.getPrincipal(), original.getPrincipal());
                        }

                        ticketGrantingTicket.getSupplementalAuthentications().add(current);
                    }

                    this.getAuthenticationSatisfiedByPolicy(ticketGrantingTicket, new ServiceContext(service, registeredService));
                    uniqueTicketIdGenKey = service.getClass().getName();
                    if (!this.uniqueTicketIdGeneratorsForService.containsKey(uniqueTicketIdGenKey)) {
                        this.logger.warn("Cannot create service ticket because the key [{}] for service [{}] is not linked to a ticket id generator", uniqueTicketIdGenKey, service.getId());
                        throw new UnauthorizedSsoServiceException();
                    } else {
                        UniqueTicketIdGenerator serviceTicketUniqueTicketIdGenerator = (UniqueTicketIdGenerator)this.uniqueTicketIdGeneratorsForService.get(uniqueTicketIdGenKey);
                        String generatedServiceTicketId = serviceTicketUniqueTicketIdGenerator.getNewTicketId("ST");
                        this.logger.debug("Generated service ticket id [{}] for ticket granting ticket [{}]", generatedServiceTicketId, ticketGrantingTicket.getId());
                        ServiceTicket serviceTicket = ticketGrantingTicket.grantServiceTicket(generatedServiceTicketId, service, this.serviceTicketExpirationPolicy, credentials != null);
                        this.serviceTicketRegistry.addTicket(serviceTicket);
                        if (this.logger.isInfoEnabled()) {
                            List<Authentication> authentications = serviceTicket.getGrantingTicket().getChainedAuthentications();
                            String formatString = "Granted %s ticket [%s] for service [%s] for user [%s]";
                            String principalId = ((Authentication)authentications.get(authentications.size() - 1)).getPrincipal().getId();
                            String type;
                            if (authentications.size() == 1) {
                                type = "service";
                            } else {
                                type = "proxy";
                            }

                            this.logger.info(String.format("Granted %s ticket [%s] for service [%s] for user [%s]", type, serviceTicket.getId(), service.getId(), principalId));
                        }

                        return serviceTicket.getId();
                    }
                }
            }
        }
    }

    @Audit(
            action = "SERVICE_TICKET",
            actionResolverName = "GRANT_SERVICE_TICKET_RESOLVER",
            resourceResolverName = "GRANT_SERVICE_TICKET_RESOURCE_RESOLVER"
    )
    @Profiled(
            tag = "GRANT_SERVICE_TICKET",
            logFailuresSeparately = false
    )
    @Transactional(
            readOnly = false
    )
    @Override
    public String grantServiceTicket(String ticketGrantingTicketId, Service service) throws TicketException {
        try {
            return this.grantServiceTicket(ticketGrantingTicketId, service, (Credential[])null);
        } catch (AuthenticationException var4) {
            throw new IllegalStateException("Unexpected authentication exception", var4);
        }
    }

    @Audit(
            action = "PROXY_GRANTING_TICKET",
            actionResolverName = "GRANT_PROXY_GRANTING_TICKET_RESOLVER",
            resourceResolverName = "GRANT_PROXY_GRANTING_TICKET_RESOURCE_RESOLVER"
    )
    @Profiled(
            tag = "GRANT_PROXY_GRANTING_TICKET",
            logFailuresSeparately = false
    )
    @Transactional(
            readOnly = false
    )
    @Override
    public String delegateTicketGrantingTicket(String serviceTicketId, Credential... credentials) throws AuthenticationException, TicketException {
        Assert.notNull(serviceTicketId, "serviceTicketId cannot be null");
        Assert.notNull(credentials, "credentials cannot be null");
        ServiceTicket serviceTicket = (ServiceTicket)this.serviceTicketRegistry.getTicket(serviceTicketId, ServiceTicket.class);
        if (serviceTicket != null && !serviceTicket.isExpired()) {
            RegisteredService registeredService = this.servicesManager.findServiceBy(serviceTicket.getService());
            this.verifyRegisteredServiceProperties(registeredService, serviceTicket.getService());
            if (!registeredService.isAllowedToProxy()) {
                this.logger.warn("ServiceManagement: Service [{}] attempted to proxy, but is not allowed.", serviceTicket.getService().getId());
                throw new UnauthorizedProxyingException();
            } else {
                Authentication authentication = this.authenticationManager.authenticate(credentials);
                TicketGrantingTicket ticketGrantingTicket = serviceTicket.grantTicketGrantingTicket(this.ticketGrantingTicketUniqueTicketIdGenerator.getNewTicketId("TGT"), authentication, this.ticketGrantingTicketExpirationPolicy);
                this.ticketRegistry.addTicket(ticketGrantingTicket);
                return ticketGrantingTicket.getId();
            }
        } else {
            this.logger.debug("ServiceTicket [{}] has expired or cannot be found in the ticket registry", serviceTicketId);
            throw new InvalidTicketException(serviceTicketId);
        }
    }

    @Audit(
            action = "SERVICE_TICKET_VALIDATE",
            actionResolverName = "VALIDATE_SERVICE_TICKET_RESOLVER",
            resourceResolverName = "VALIDATE_SERVICE_TICKET_RESOURCE_RESOLVER"
    )
    @Profiled(
            tag = "VALIDATE_SERVICE_TICKET",
            logFailuresSeparately = false
    )
    @Transactional(
            readOnly = false
    )
    @Override
    public Assertion validateServiceTicket(String serviceTicketId, Service service) throws TicketException {
        Assert.notNull(serviceTicketId, "serviceTicketId cannot be null");
        Assert.notNull(service, "service cannot be null");
        ServiceTicket serviceTicket = (ServiceTicket)this.serviceTicketRegistry.getTicket(serviceTicketId, ServiceTicket.class);
        if (serviceTicket == null) {
            this.logger.info("ServiceTicket [{}] does not exist.", serviceTicketId);
            throw new InvalidTicketException(serviceTicketId);
        } else {
            RegisteredService registeredService = this.servicesManager.findServiceBy(service);
            this.verifyRegisteredServiceProperties(registeredService, serviceTicket.getService());

            ImmutableAssertion var12;
            try {
                synchronized(serviceTicket) {
                    if (serviceTicket.isExpired()) {
                        this.logger.info("ServiceTicket [{}] has expired.", serviceTicketId);
                        throw new InvalidTicketException(serviceTicketId);
                    }

                    if (!serviceTicket.isValidFor(service)) {
                        this.logger.error("ServiceTicket [{}] with service [{}] does not match supplied service [{}]", new Object[]{serviceTicketId, serviceTicket.getService().getId(), service});
                        throw new TicketValidationException(serviceTicket.getService());
                    }
                }

                TicketGrantingTicket root = serviceTicket.getGrantingTicket().getRoot();
                Authentication authentication = this.getAuthenticationSatisfiedByPolicy(root, new ServiceContext(serviceTicket.getService(), registeredService));
                Principal principal = authentication.getPrincipal();
                Map<String, Object> attributesToRelease = this.defaultAttributeFilter.filter(principal.getId(), principal.getAttributes(), registeredService);
                if (registeredService.getAttributeFilter() != null) {
                    attributesToRelease = registeredService.getAttributeFilter().filter(principal.getId(), attributesToRelease, registeredService);
                }

                String principalId = this.determinePrincipalIdForRegisteredService(principal, registeredService, serviceTicket);
                Principal modifiedPrincipal = new SimplePrincipal(principalId, attributesToRelease);
                AuthenticationBuilder builder = AuthenticationBuilder.newInstance(authentication);
                builder.setPrincipal(modifiedPrincipal);
                var12 = new ImmutableAssertion(builder.build(), serviceTicket.getGrantingTicket().getChainedAuthentications(), serviceTicket.getService(), serviceTicket.isFromNewLogin());
            } finally {
                if (serviceTicket.isExpired()) {
                    this.serviceTicketRegistry.deleteTicket(serviceTicketId);
                }

            }

            return var12;
        }
    }

    private String determinePrincipalIdForRegisteredService(Principal principal, RegisteredService registeredService, ServiceTicket serviceTicket) {
        String principalId = null;
        String serviceUsernameAttribute = registeredService.getUsernameAttribute();
        if (registeredService.isAnonymousAccess()) {
            principalId = this.persistentIdGenerator.generate(principal, serviceTicket.getService());
        } else if (StringUtils.isBlank(serviceUsernameAttribute)) {
            principalId = principal.getId();
        } else if (principal.getAttributes().containsKey(serviceUsernameAttribute)) {
            principalId = principal.getAttributes().get(serviceUsernameAttribute).toString();
        } else {
            principalId = principal.getId();
            Object[] errorLogParameters = new Object[]{principalId, registeredService.getUsernameAttribute(), principal.getAttributes(), registeredService.getServiceId(), principalId};
            this.logger.warn("Principal [{}] did not have attribute [{}] among attributes [{}] so CAS cannot provide on the validation response the user attribute the registered service [{}] expects. CAS will instead return the default username attribute [{}]", errorLogParameters);
        }

        this.logger.debug("Principal id to return for service [{}] is [{}]. The default principal id is [{}].", new Object[]{registeredService.getName(), principal.getId(), principalId});
        return principalId;
    }

    @Audit(
            action = "TICKET_GRANTING_TICKET",
            actionResolverName = "CREATE_TICKET_GRANTING_TICKET_RESOLVER",
            resourceResolverName = "CREATE_TICKET_GRANTING_TICKET_RESOURCE_RESOLVER"
    )
    @Profiled(
            tag = "CREATE_TICKET_GRANTING_TICKET",
            logFailuresSeparately = false
    )
    @Transactional(
            readOnly = false
    )
    @Override
    public String createTicketGrantingTicket(Credential... credentials) throws AuthenticationException, TicketException {
        Assert.notNull(credentials, "credentials cannot be null");
        Authentication authentication = this.authenticationManager.authenticate(credentials);
        TicketGrantingTicket ticketGrantingTicket = new TicketGrantingTicketImpl(this.ticketGrantingTicketUniqueTicketIdGenerator.getNewTicketId("TGT"), authentication, this.ticketGrantingTicketExpirationPolicy);
        this.ticketRegistry.addTicket(ticketGrantingTicket);
        return ticketGrantingTicket.getId();
    }

    public void setPersistentIdGenerator(PersistentIdGenerator persistentIdGenerator) {
        this.persistentIdGenerator = persistentIdGenerator;
    }

    public void setServiceContextAuthenticationPolicyFactory(ContextualAuthenticationPolicyFactory<ServiceContext> policy) {
        this.serviceContextAuthenticationPolicyFactory = policy;
    }

    public void setTicketGrantingTicketExpirationPolicy(ExpirationPolicy ticketGrantingTicketExpirationPolicy) {
        this.ticketGrantingTicketExpirationPolicy = ticketGrantingTicketExpirationPolicy;
    }

    public void setServiceTicketExpirationPolicy(ExpirationPolicy serviceTicketExpirationPolicy) {
        this.serviceTicketExpirationPolicy = serviceTicketExpirationPolicy;
    }

    private Authentication getAuthenticationSatisfiedByPolicy(TicketGrantingTicket ticket, ServiceContext context) throws TicketException {
        ContextualAuthenticationPolicy<ServiceContext> policy = this.serviceContextAuthenticationPolicyFactory.createPolicy(context);
        if (policy.isSatisfiedBy(ticket.getAuthentication())) {
            return ticket.getAuthentication();
        } else {
            Iterator var4 = ticket.getSupplementalAuthentications().iterator();

            Authentication auth;
            do {
                if (!var4.hasNext()) {
                    throw new UnsatisfiedAuthenticationPolicyException(policy);
                }

                auth = (Authentication)var4.next();
            } while(!policy.isSatisfiedBy(auth));

            return auth;
        }
    }

    private void verifyRegisteredServiceProperties(RegisteredService registeredService, Service service) {
        String msg;
        if (registeredService == null) {
            msg = String.format("ServiceManagement: Unauthorized Service Access. Service [%s] is not found in service registry.", service.getId());
            this.logger.warn(msg);
            throw new UnauthorizedServiceException("screen.service.error.message", msg);
        } else if (!registeredService.isEnabled()) {
            msg = String.format("ServiceManagement: Unauthorized Service Access. Service %s] is not enabled in service registry.", service.getId());
            this.logger.warn(msg);
            throw new UnauthorizedServiceException("screen.service.error.message", msg);
        }
    }
}

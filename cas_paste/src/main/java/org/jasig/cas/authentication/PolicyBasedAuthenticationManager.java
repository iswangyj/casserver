package org.jasig.cas.authentication;

import com.github.inspektr.audit.annotation.Audit;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author SxL
 * Created on 9/25/2018 2:03 PM.
 */
public class PolicyBasedAuthenticationManager implements AuthenticationManager {
    private static final Principal NULL_PRINCIPAL = new PolicyBasedAuthenticationManager.NullPrincipal();
    protected final Logger logger;
    @NotNull
    private List<AuthenticationMetaDataPopulator> authenticationMetaDataPopulators;
    @NotNull
    private AuthenticationPolicy authenticationPolicy;
    @NotNull
    private final Map<AuthenticationHandler, PrincipalResolver> handlerResolverMap;

    public PolicyBasedAuthenticationManager(AuthenticationHandler... handlers) {
        this(Arrays.asList(handlers));
    }

    public PolicyBasedAuthenticationManager(List<AuthenticationHandler> handlers) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.authenticationMetaDataPopulators = new ArrayList();
        this.authenticationPolicy = new AnyAuthenticationPolicy();
        Assert.notEmpty(handlers, "At least one authentication handler is required");
        this.handlerResolverMap = new LinkedHashMap(handlers.size());
        Iterator var2 = handlers.iterator();

        while(var2.hasNext()) {
            AuthenticationHandler handler = (AuthenticationHandler)var2.next();
            this.handlerResolverMap.put(handler, (PrincipalResolver) null);
        }

    }

    public PolicyBasedAuthenticationManager(Map<AuthenticationHandler, PrincipalResolver> map) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.authenticationMetaDataPopulators = new ArrayList();
        this.authenticationPolicy = new AnyAuthenticationPolicy();
        Assert.notEmpty(map, "At least one authentication handler is required");
        this.handlerResolverMap = map;
    }

    @Audit(
            action = "AUTHENTICATION",
            actionResolverName = "AUTHENTICATION_RESOLVER",
            resourceResolverName = "AUTHENTICATION_RESOURCE_RESOLVER"
    )
    @Profiled(
            tag = "AUTHENTICATE",
            logFailuresSeparately = false
    )

    @Override
    public final Authentication authenticate(Credential... credentials) throws AuthenticationException {
        AuthenticationBuilder builder = this.authenticateInternal(credentials);
        Authentication authentication = builder.build();
        Principal principal = authentication.getPrincipal();
        if (principal instanceof PolicyBasedAuthenticationManager.NullPrincipal) {
            throw new UnresolvedPrincipalException(authentication);
        } else {
            Iterator var5 = authentication.getSuccesses().values().iterator();

            while(var5.hasNext()) {
                HandlerResult result = (HandlerResult)var5.next();
                builder.addAttribute("authenticationMethod", result.getHandlerName());
            }

            this.logger.info("Authenticated {} with credentials {}.", principal, Arrays.asList(credentials));
            this.logger.debug("Attribute map for {}: {}", principal.getId(), principal.getAttributes());
            var5 = this.authenticationMetaDataPopulators.iterator();

            while(var5.hasNext()) {
                AuthenticationMetaDataPopulator populator = (AuthenticationMetaDataPopulator)var5.next();
                Credential[] var7 = credentials;
                int var8 = credentials.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    Credential credential = var7[var9];
                    populator.populateAttributes(builder, credential);
                }
            }

            return builder.build();
        }
    }

    public final void setAuthenticationMetaDataPopulators(List<AuthenticationMetaDataPopulator> populators) {
        this.authenticationMetaDataPopulators = populators;
    }

    public void setAuthenticationPolicy(AuthenticationPolicy policy) {
        this.authenticationPolicy = policy;
    }

    protected AuthenticationBuilder authenticateInternal(Credential... credentials) throws AuthenticationException {
        AuthenticationBuilder builder = new AuthenticationBuilder(NULL_PRINCIPAL);
        Credential[] var3 = credentials;
        int var4 = credentials.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Credential c = var3[var5];
            builder.addCredential(new BasicCredentialMetaData(c));
        }

        Credential[] var18 = credentials;
        int var7 = credentials.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Credential credential = var18[var8];
            boolean found = false;
            Iterator var10 = this.handlerResolverMap.keySet().iterator();

            while(var10.hasNext()) {
                AuthenticationHandler handler = (AuthenticationHandler)var10.next();
                if (handler.supports(credential)) {
                    found = true;

                    try {
                        HandlerResult result = handler.authenticate(credential);
                        builder.addSuccess(handler.getName(), result);
                        this.logger.info("{} successfully authenticated {}", handler.getName(), credential);
                        PrincipalResolver resolver = (PrincipalResolver)this.handlerResolverMap.get(handler);
                        Principal principal;
                        if (resolver == null) {
                            principal = result.getPrincipal();
                            this.logger.debug("No resolver configured for {}. Falling back to handler principal {}", handler.getName(), principal);
                        } else {
                            principal = this.resolvePrincipal(handler.getName(), resolver, credential);
                        }

                        if (principal != null) {
                            builder.setPrincipal(principal);
                        }

                        if (this.authenticationPolicy.isSatisfiedBy(builder.build())) {
                            return builder;
                        }
                    } catch (GeneralSecurityException var13) {
                        this.logger.info("{} failed authenticating {}", handler.getName(), credential);
                        builder.addFailure(handler.getName(), var13.getClass());
                    } catch (PreventedException var14) {
                        builder.addFailure(handler.getName(), var14.getClass());
                    }
                }
            }

            if (!found) {
                this.logger.warn("Cannot find authentication handler that supports {}, which suggests a configuration problem.", credential);
            }
        }

        if (builder.getSuccesses().isEmpty()) {
            throw new AuthenticationException(builder.getFailures(), builder.getSuccesses());
        } else if (!this.authenticationPolicy.isSatisfiedBy(builder.build())) {
            throw new AuthenticationException(builder.getFailures(), builder.getSuccesses());
        } else {
            return builder;
        }
    }

    protected Principal resolvePrincipal(String handlerName, PrincipalResolver resolver, Credential credential) {
        if (resolver.supports(credential)) {
            try {
                Principal p = resolver.resolve(credential);
                this.logger.debug("{} resolved {} from {}", new Object[]{resolver, p, credential});
                return p;
            } catch (Exception var5) {
                this.logger.error("{} failed to resolve principal from {}", new Object[]{resolver, credential, var5});
            }
        } else {
            this.logger.warn("{} is configured to use {} but it does not support {}, which suggests a configuration problem.", new Object[]{handlerName, resolver, credential});
        }

        return null;
    }

    private static AuthenticationException createAuthenticationException(Authentication authn) {
        return new AuthenticationException(authn.getFailures(), authn.getSuccesses());
    }

    static class NullPrincipal implements Principal {
        private static final String NOBODY = "nobody";

        NullPrincipal() {
        }

        @Override
        public String getId() {
            return "nobody";
        }

        @Override
        public Map<String, Object> getAttributes() {
            return Collections.emptyMap();
        }
    }
}

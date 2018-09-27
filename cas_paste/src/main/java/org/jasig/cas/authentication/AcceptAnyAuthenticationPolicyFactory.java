package org.jasig.cas.authentication;

import org.jasig.cas.services.ServiceContext;

/**
 * @author SxL
 * Created on 9/25/2018 1:51 PM.
 */
public class AcceptAnyAuthenticationPolicyFactory implements ContextualAuthenticationPolicyFactory<ServiceContext> {
    public AcceptAnyAuthenticationPolicyFactory() {
    }

    @Override
    public ContextualAuthenticationPolicy<ServiceContext> createPolicy(final ServiceContext context) {
        return new ContextualAuthenticationPolicy<ServiceContext>() {
            @Override
            public ServiceContext getContext() {
                return context;
            }

            @Override
            public boolean isSatisfiedBy(Authentication authentication) {
                return true;
            }
        };
    }
}
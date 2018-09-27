package org.jasig.cas.authentication;

import org.jasig.cas.services.ServiceContext;

import java.util.Iterator;

/**
 * @author SxL
 * Created on 9/25/2018 2:06 PM.
 */
public class RequiredHandlerAuthenticationPolicyFactory implements ContextualAuthenticationPolicyFactory<ServiceContext> {
    public RequiredHandlerAuthenticationPolicyFactory() {
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
                Iterator var2 = context.getRegisteredService().getRequiredHandlers().iterator();

                String required;
                do {
                    if (!var2.hasNext()) {
                        return true;
                    }

                    required = (String)var2.next();
                } while(authentication.getSuccesses().containsKey(required));

                return false;
            }
        };
    }
}

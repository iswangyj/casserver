package org.jasig.cas.authentication;

import java.util.Iterator;

/**
 * @author SxL
 * Created on 9/25/2018 2:02 PM.
 */
public class NotPreventedAuthenticationPolicy extends AnyAuthenticationPolicy {
    public NotPreventedAuthenticationPolicy() {
    }

    @Override
    public boolean isSatisfiedBy(Authentication authentication) {
        Iterator var2 = authentication.getFailures().keySet().iterator();

        String handler;
        do {
            if (!var2.hasNext()) {
                return super.isSatisfiedBy(authentication);
            }

            handler = (String)var2.next();
        } while(!((Class)authentication.getFailures().get(handler)).isAssignableFrom(PreventedException.class));

        return false;
    }
}

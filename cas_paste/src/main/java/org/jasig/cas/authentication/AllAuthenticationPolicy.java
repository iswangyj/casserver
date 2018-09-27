package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 1:53 PM.
 */
public class AllAuthenticationPolicy implements AuthenticationPolicy {
    public AllAuthenticationPolicy() {
    }

    @Override
    public boolean isSatisfiedBy(Authentication authn) {
        return authn.getSuccesses().size() == authn.getCredentials().size();
    }
}

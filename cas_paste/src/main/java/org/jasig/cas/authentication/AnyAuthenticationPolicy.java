package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 1:53 PM.
 */
public class AnyAuthenticationPolicy implements AuthenticationPolicy {
    private boolean tryAll = false;

    public AnyAuthenticationPolicy() {
    }

    public void setTryAll(boolean tryAll) {
        this.tryAll = tryAll;
    }

    @Override
    public boolean isSatisfiedBy(Authentication authn) {
        if (this.tryAll) {
            return authn.getCredentials().size() == authn.getSuccesses().size() + authn.getFailures().size();
        } else {
            return authn.getSuccesses().size() > 0;
        }
    }
}

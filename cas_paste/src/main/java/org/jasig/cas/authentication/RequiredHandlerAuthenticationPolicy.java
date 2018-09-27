package org.jasig.cas.authentication;

import javax.validation.constraints.NotNull;

/**
 * @author SxL
 * Created on 9/25/2018 2:06 PM.
 */
public class RequiredHandlerAuthenticationPolicy implements AuthenticationPolicy {
    @NotNull
    private final String requiredHandlerName;
    private boolean tryAll = false;

    public RequiredHandlerAuthenticationPolicy(String requiredHandlerName) {
        this.requiredHandlerName = requiredHandlerName;
    }

    public void setTryAll(boolean tryAll) {
        this.tryAll = tryAll;
    }

    @Override
    public boolean isSatisfiedBy(Authentication authn) {
        boolean credsOk = true;
        if (this.tryAll) {
            credsOk = authn.getCredentials().size() == authn.getSuccesses().size() + authn.getFailures().size();
        }

        return credsOk && authn.getSuccesses().containsKey(this.requiredHandlerName);
    }
}

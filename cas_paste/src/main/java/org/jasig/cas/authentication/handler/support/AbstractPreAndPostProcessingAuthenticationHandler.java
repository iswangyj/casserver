package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.authentication.AbstractAuthenticationHandler;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;

/**
 * @author SxL
 * Created on 9/25/2018 2:16 PM.
 */
public abstract class AbstractPreAndPostProcessingAuthenticationHandler extends AbstractAuthenticationHandler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AbstractPreAndPostProcessingAuthenticationHandler() {
    }

    protected boolean preAuthenticate(Credential credential) {
        return true;
    }

    protected HandlerResult postAuthenticate(Credential credential, HandlerResult result) {
        return result;
    }

    @Override
    public final HandlerResult authenticate(Credential credential) throws GeneralSecurityException, PreventedException {
        if (!this.preAuthenticate(credential)) {
            throw new FailedLoginException();
        } else {
            return this.postAuthenticate(credential, this.doAuthentication(credential));
        }
    }

    protected abstract HandlerResult doAuthentication(Credential var1) throws GeneralSecurityException, PreventedException;
}


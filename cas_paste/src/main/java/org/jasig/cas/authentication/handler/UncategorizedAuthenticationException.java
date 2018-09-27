package org.jasig.cas.authentication.handler;

/**
 * @author SxL
 * Created on 9/25/2018 2:15 PM.
 */
public abstract class UncategorizedAuthenticationException extends AuthenticationException {
    public UncategorizedAuthenticationException(String code) {
        super(code);
    }

    public UncategorizedAuthenticationException(String code, Throwable throwable) {
        super(code, throwable);
    }
}


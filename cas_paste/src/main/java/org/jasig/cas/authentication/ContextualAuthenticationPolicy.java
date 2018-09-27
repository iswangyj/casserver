package org.jasig.cas.authentication;

/**
 * @author SxL
 * Created on 9/25/2018 1:57 PM.
 */
public interface ContextualAuthenticationPolicy<T> extends AuthenticationPolicy {
    T getContext();
}
